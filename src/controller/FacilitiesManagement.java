package controller;

import model.dao.*;
import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;
import model.mo.Magazzino;
import model.mo.User;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilitiesManagement {

    private FacilitiesManagement(){

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
            
            commonView(daoFactory, sessionDAOFactory, request);

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("viewUrl","facilitiesManagement/view");

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

    public static void insertView(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        User loggedUser;

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

            String whichInsertMode = request.getParameter("whichInsertMode");

            if(whichInsertMode.equals("M"))
                request.setAttribute("viewUrl","warehousesManagement/insView");
            else if(whichInsertMode.equals("C"))
                request.setAttribute("viewUrl","centresManagement/insView");

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

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

    public static void insert(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory;
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
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            assert daoFactory != null;
            daoFactory.beginTransaction();

            /* Setto i parametri */
            MagazzinoDAO magazzinoDAO = daoFactory.getMagazzinoDAO();
            CentroVenditaDAO centroVenditaDAO = daoFactory.getCentroVenditaDAO();

            String nomeMagazzino = request.getParameter("nomeMagazzino");
            String nomeFornitore = request.getParameter("nomeFornitore");
            String whichInsertMode = request.getParameter("whichInsertMode");

            CentroVendita centroVendita = centroVenditaDAO.findByNomeCentro(nomeFornitore);

            if(centroVendita!=null && whichInsertMode.equals("M")){

                String indirizzo = request.getParameter("indirizzo");

                try {
                    magazzinoDAO.create(centroVendita,nomeMagazzino,indirizzo);
                } catch (DuplicatedObjectException e) {
                    applicationMessage = "Magazzino già esistente!";
                    logger.log(Level.INFO, "Tentativo di inserimento di un magazzino già esistente.");
                }

            }
            else if(centroVendita==null && whichInsertMode.equals("C")){

                String indirizzo = request.getParameter("indirizzo");

                try {
                    centroVenditaDAO.create(nomeFornitore,indirizzo);
                } catch (DuplicatedObjectException e) {
                    applicationMessage = "Centro Vendita già esistente!";
                    logger.log(Level.INFO, "Tentativo di inserimento di un centro vendita già esistente.");
                }

            }
            else {
                applicationMessage="Errore! Qualcosa è andato storto.";
            }

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl", "facilitiesManagement/view");

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

    public static void modifyView(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory;
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            assert daoFactory != null;
            daoFactory.beginTransaction();

            String nomeMagazzino = request.getParameter("nomeMagazzino");
            String nomeFornitore = request.getParameter("nomeFornitore");
            MagazzinoDAO magazzinoDAO = daoFactory.getMagazzinoDAO();
            CentroVenditaDAO centroVenditaDAO = daoFactory.getCentroVenditaDAO();

            Magazzino magazzino = magazzinoDAO.findByNomeMagazzino(nomeMagazzino);
            CentroVendita centroVendita = centroVenditaDAO.findByNomeCentro(nomeFornitore);

            if(magazzino!=null) {
                request.setAttribute("magazzino", magazzino);
                request.setAttribute("viewUrl","warehousesManagement/insView");
            }
            else if(centroVendita!=null) {
                request.setAttribute("centroVendita", centroVendita);
                request.setAttribute("viewUrl","centresManagement/insView");
            }

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);

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

    public static void modify(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory;
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            assert daoFactory != null;
            daoFactory.beginTransaction();

            String nomeMagazzino = request.getParameter("nomeMagazzino");
            String nomeFornitore = request.getParameter("nomeFornitore");
            String whichModifyMode = request.getParameter("whichModifyMode");

            MagazzinoDAO magazzinoDAO = daoFactory.getMagazzinoDAO();
            Magazzino magazzino = magazzinoDAO.findByNomeMagazzino(nomeMagazzino);
            CentroVenditaDAO centroVenditaDAO = daoFactory.getCentroVenditaDAO();
            CentroVendita centroVendita = centroVenditaDAO.findByNomeCentro(nomeFornitore);

            if(magazzino!=null && centroVendita!=null && whichModifyMode.equals("M")){

                String indirizzo = request.getParameter("indirizzo");
                magazzino.setCentroVendita(centroVendita);
                magazzino.setIndirizzo(indirizzo);

                try {
                    magazzinoDAO.update(magazzino);
                } catch (DuplicatedObjectException e) {
                    applicationMessage = "Magazzino già esistente.";
                    logger.log(Level.INFO, "Tentativo di inserimento di un magazzino già esistente.");
                }

            }
            else if(centroVendita!=null && whichModifyMode.equals("C")){

                String indirizzo = request.getParameter("indirizzo");
                centroVendita.setIndirizzo(indirizzo);

                try {
                    centroVenditaDAO.update(centroVendita);
                }  catch (DuplicatedObjectException e) {
                    applicationMessage = "Centro Vendita già esistente.";
                    logger.log(Level.INFO, "Tentativo di inserimento di un centro vendita già esistente.");
                }
            }
            else {
                applicationMessage="Errore! Qualcosa è andato storto.";
            }

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl","facilitiesManagement/view");
            request.setAttribute("applicationMessage",applicationMessage);

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

    private static void commonView(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request) {

        ArrayList<CentroVendita> centres;
        ArrayList<Magazzino> warehouses;

        CentroVenditaDAO centroVenditaDAO = daoFactory.getCentroVenditaDAO();
        MagazzinoDAO magazzinoDAO = daoFactory.getMagazzinoDAO();

        centres = centroVenditaDAO.findAllCentri();
        warehouses = magazzinoDAO.findAllMagazzini();

        request.setAttribute("centres",centres);
        request.setAttribute("warehouses",warehouses);

    }
}
