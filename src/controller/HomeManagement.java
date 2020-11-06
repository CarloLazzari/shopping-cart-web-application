package controller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import model.dao.*;
import model.mo.*;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeManagement {

    private HomeManagement(){

    }

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
            assert sessionDAOFactory != null;
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDao = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDao.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            assert daoFactory != null;
            daoFactory.beginTransaction();

            int whichHalf = 0;

            if(request.getParameter("whichHalf")!=null){
                whichHalf= Integer.parseInt(request.getParameter("whichHalf"));
            }

            commonView(daoFactory, sessionDAOFactory, request);

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("whichHalf",whichHalf);
            request.setAttribute("viewUrl","homeManagement/view");

        } catch(Exception e){
            logger.log(Level.SEVERE, "Controller error",e);
            try{
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable ignored){

            }
            throw new RuntimeException(e);
        } finally {
            try{
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable ignored){

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
            assert sessionDAOFactory != null;
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            int whichHalf = 0;

            if(request.getParameter("whichHalf")!=null){
                whichHalf = Integer.parseInt(request.getParameter("whichHalf"));
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            assert daoFactory != null;
            daoFactory.beginTransaction();

            /* Lists */
            String username = request.getParameter("Username");
            String password = request.getParameter("Password");

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

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("whichHalf",whichHalf);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable ignored) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable ignored) {
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
            assert sessionDAOFactory != null;
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            assert daoFactory != null;
            daoFactory.beginTransaction();

            int whichHalf = 0;

            if(request.getParameter("whichHalf")!=null){
                whichHalf= Integer.parseInt(request.getParameter("whichHalf"));
            }

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            loggedUser = sessionUserDAO.findLoggedUser();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            carrelloDAO.flushCart(loggedUser.getUsername());

            sessionUserDAO.delete(null);

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("whichHalf",whichHalf);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable ignored) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable ignored) {
            }
        }
    }

    public static void commonView(DAOFactory daoFactory,DAOFactory sessionDAOFactory,HttpServletRequest request){

        FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
        ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
        FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();
        ArrayList<Fumetto> fumettoArrayList;

        User loggedUser;
        UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
        loggedUser = sessionUserDAO.findLoggedUser();

        /* Lists */
        if(loggedUser!=null) {
            if (loggedUser.getAdmin().equals("Y"))
                fumettoArrayList = fumettoDAO.findRandomFumetti();
            else
                fumettoArrayList = fumettoDAO.findRandomFumettiUnblocked();
        }
        else
            fumettoArrayList = fumettoDAO.findRandomFumetti();

        ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findRandomContenutoNelMagazzino(fumettoArrayList);
        ArrayList<FornitoDa> fornitoDaArrayList = fornitoDaDAO.findRandomFornitoDa(fumettoArrayList);

        request.setAttribute("fumettoArrayList",fumettoArrayList);
        request.setAttribute("contenutoNelMagazzinoArrayList",contenutoNelMagazzinoArrayList);
        request.setAttribute("fornitoDaArrayList", fornitoDaArrayList);

    }

}
