package controller;

import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.User;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersManagement {

    private UsersManagement(){}

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory;
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            commonView(daoFactory, sessionDAOFactory, request);

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("viewUrl","usersManagement/view");

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

    public static void block(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try{

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDao = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDao.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String whichUserUsername = request.getParameter("whichUserUsername");
            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUsername(whichUserUsername);

                userDAO.block(user);

            applicationMessage = "Utente "+ whichUserUsername + " bloccato";

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","usersManagement/view");

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

    public static void unblock(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try{

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDao = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDao.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String whichUserUsername = request.getParameter("whichUserUsername");
            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUsername(whichUserUsername);

                userDAO.unblock(user);

            applicationMessage = "Utente "+ whichUserUsername + " bloccato";

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","usersManagement/view");

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


    public static void commonView(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request){

        UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
        User loggedUser = sessionUserDAO.findLoggedUser();
        UserDAO userDAO = daoFactory.getUserDAO();

        ArrayList<User> users;
        ArrayList<Integer> usersOrdersCount;

        users = userDAO.findAllUsers();
        usersOrdersCount=userDAO.countOrderByUsername();

        request.setAttribute("users", users);
        request.setAttribute("usersOrdersCount",usersOrdersCount);

    }


}
