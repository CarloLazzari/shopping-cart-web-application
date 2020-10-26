package controller;

import model.dao.CarrelloDAO;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.User;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeManagement {

    private HomeManagement(){

    }

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();

        try{

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDao = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDao.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("viewUrl","homeManagement/view");

        } catch(Exception e){
            logger.log(Level.SEVERE, "Controller error",e);
            try{
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t){

            }
            throw new RuntimeException(e);
        } finally {
            try{
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t){

            }
        }

    }

    public static void logon(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String username = request.getParameter("USERNAME");
            String password = request.getParameter("PASSWORD");

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUsername(username);

            if (user == null || !user.getPassword().equals(password) || user.getBlocked().equals("Y")) {
                sessionUserDAO.delete(null);
                applicationMessage = "Password o Username errati, o sei stato bloccato!";
                loggedUser=null;
            }
            else {
                loggedUser = sessionUserDAO.create(user.getUsername(), null, user.getFirstname(), user.getSurname(), null, null, null, null, user.getAdmin());
                applicationMessage="Login effettuato.";
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);

            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            loggedUser = sessionUserDAO.findLoggedUser();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            carrelloDAO.flushCart(loggedUser.getUsername());

            sessionUserDAO.delete(null);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

}
