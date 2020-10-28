package controller;

import model.dao.*;
import model.mo.ContenutoNelMagazzino;
import model.mo.FornitoDa;
import model.mo.Fumetto;
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
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDao = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDao.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            int whichHalf = 0;

            if(request.getParameter("whichHalf")!=null){
                whichHalf= Integer.parseInt(request.getParameter("whichHalf"));
            }

            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();

            /* Lists */
            ArrayList<Fumetto> fumettoArrayList = fumettoDAO.findRandomFumetti();
            ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findRandomContenutoNelMagazzino();
            ArrayList<FornitoDa> fornitoDaArrayList = fornitoDaDAO.findRandomFornitoDa();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("fumettoArrayList",fumettoArrayList);
            request.setAttribute("contenutoNelMagazzinoArrayList",contenutoNelMagazzinoArrayList);
            request.setAttribute("fornitoDaArrayList", fornitoDaArrayList);
            request.setAttribute("whichHalf",whichHalf);
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

            int whichHalf = 0;

            if(request.getParameter("whichHalf")!=null){
                whichHalf = Integer.parseInt(request.getParameter("whichHalf"));
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();

            /* Lists */
            ArrayList<Fumetto> fumettoArrayList = fumettoDAO.findRandomFumetti();
            ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findRandomContenutoNelMagazzino();
            ArrayList<FornitoDa> fornitoDaArrayList = fornitoDaDAO.findRandomFornitoDa();

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
            request.setAttribute("fumettoArrayList",fumettoArrayList);
            request.setAttribute("contenutoNelMagazzinoArrayList",contenutoNelMagazzinoArrayList);
            request.setAttribute("fornitoDaArrayList", fornitoDaArrayList);
            request.setAttribute("whichHalf",whichHalf);
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

            int whichHalf = 0;

            if(request.getParameter("whichHalf")!=null){
                whichHalf= Integer.parseInt(request.getParameter("whichHalf"));
            }

            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();

            /* Lists */
            ArrayList<Fumetto> fumettoArrayList = fumettoDAO.findRandomFumetti();
            ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findRandomContenutoNelMagazzino();
            ArrayList<FornitoDa> fornitoDaArrayList = fornitoDaDAO.findRandomFornitoDa();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            loggedUser = sessionUserDAO.findLoggedUser();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            carrelloDAO.flushCart(loggedUser.getUsername());

            sessionUserDAO.delete(null);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("fumettoArrayList",fumettoArrayList);
            request.setAttribute("contenutoNelMagazzinoArrayList",contenutoNelMagazzinoArrayList);
            request.setAttribute("fornitoDaArrayList", fornitoDaArrayList);
            request.setAttribute("whichHalf",whichHalf);
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
