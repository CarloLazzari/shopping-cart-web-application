package controller;

import model.dao.*;
import model.mo.ContenutoNellOrdine;
import model.mo.Ordine;
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

public class OrdersManagement {

    private OrdersManagement(){}

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory;
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
            request.setAttribute("whichUserUsername",whichUserUsername);

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","ordersManagement/view");

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


    public static void viewDetails(HttpServletRequest request, HttpServletResponse response){

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

            int ORDER_ID = Integer.parseInt(request.getParameter("ORDER_ID"));
            ContenutoNellOrdineDAO contenutoNellOrdineDAO = daoFactory.getContenutoNellOrdineDAO();
            ArrayList<ContenutoNellOrdine> contenutoNellOrdine = contenutoNellOrdineDAO.findByOrderID(ORDER_ID);

            OrdineDAO ordineDAO = daoFactory.getOrdineDao();
            float prezzo = ordineDAO.calculatePrice(ORDER_ID);
            ArrayList<Float> prices = new ArrayList<>();

            prices = contenutoNellOrdineDAO.calculatePriceCNOforWhichOrderID(ORDER_ID);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("contenutoNellOrdine",contenutoNellOrdine);
            request.setAttribute("prezzo",prezzo);
            request.setAttribute("prices",prices);
            request.setAttribute("viewUrl","ordersManagement/viewDetails");

        } catch(Exception e){
            logger.log(Level.SEVERE, "Controller error.",e);
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



    public static void modifyStatus(HttpServletRequest request, HttpServletResponse response){

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

            String STATO = request.getParameter("STATO");
            int ORDER_ID = Integer.parseInt(request.getParameter("ORDER_ID"));
            OrdineDAO ordineDAO = daoFactory.getOrdineDao();
            Ordine ordine = ordineDAO.findByOrderIdentificativo(ORDER_ID);

            try {
                ordineDAO.updateStatus(ordine,STATO);
            } catch (RuntimeException e) {
                logger.log(Level.INFO, "Error in updating the order status.");
                applicationMessage="Error in updating the order status.";
            }

            String whichUserUsername = request.getParameter("whichUserUsername");
            request.setAttribute("whichUserUsername",whichUserUsername);

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","ordersManagement/view");

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

    private static void commonView(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request) {

        UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
        User loggedUser = sessionUserDAO.findLoggedUser();
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = userDAO.findByUsername(loggedUser.getUsername());

        ContenutoNellOrdineDAO contenutoNellOrdineDAO = daoFactory.getContenutoNellOrdineDAO();

        OrdineDAO ordineDAO = daoFactory.getOrdineDao();
        ArrayList<Ordine> ordini;
        ArrayList<Float> prices = new ArrayList<>();

        String whichUserUsername = request.getParameter("whichUserUsername");

        if(user.getAdmin().equals("Y")){
            if(whichUserUsername==null) {
                ordini = ordineDAO.findAllOrdini();
                prices = contenutoNellOrdineDAO.calculatePriceCNO();
            }
            else {
                prices = contenutoNellOrdineDAO.calculatePriceforWhichUsername(whichUserUsername);
                ordini = ordineDAO.findAllOrdiniByNomeEffettuante(whichUserUsername);
            }

        }
        else {
            prices = contenutoNellOrdineDAO.calculatePriceforWhichUsername(loggedUser.getUsername());
            ordini = ordineDAO.findAllOrdiniByNomeEffettuante(loggedUser.getUsername());
        }

        request.setAttribute("ordini", ordini);
        request.setAttribute("prices", prices);
        request.setAttribute("whichUserUsername",whichUserUsername);

    }

}
