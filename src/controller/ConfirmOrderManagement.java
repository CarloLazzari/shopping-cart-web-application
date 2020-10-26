package controller;

import model.dao.*;
import model.dao.exception.DuplicatedObjectException;
import model.mo.*;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConfirmOrderManagement {


    private ConfirmOrderManagement() {}

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

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

            commonView(daoFactory,sessionDAOFactory,request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "confirmOrderManagement/confirmView");

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

    public static void viewProductDetails(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

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

            String ISBN = (request.getParameter("ISBN"));
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            Fumetto fumetto = fumettoDAO.showFumetto(ISBN);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("fumetto",fumetto);
            request.setAttribute("viewUrl", "productsManagement/viewDetails");

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

    public static void confirmOrder(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage;
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

            /* DAO */
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            ContenutoNellOrdineDAO contenutoNellOrdineDAO = daoFactory.getContenutoNellOrdineDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            OrdineDAO ordineDAO = daoFactory.getOrdineDao();
            CartaDAO cartaDAO = daoFactory.getCartaDAO();
            CentroVenditaDAO centroVenditaDAO = daoFactory.getCentroVenditaDAO();
            MagazzinoDAO magazzinoDAO = daoFactory.getMagazzinoDAO();
            UserDAO userDAO = daoFactory.getUserDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();


            /* Lists */
            ArrayList<FornitoDa> fornitoDaArrayList = fornitoDaDAO.findInCart(loggedUser.getUsername());
            ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findInCartCNM(loggedUser.getUsername());


            /* Richiesta dei parametri */
            String indirizzoSpedizione = request.getParameter("indirizzoSpedizione");
            String metodoPagamento = request.getParameter("metodoPagamento");

            ArrayList<Carrello> cartItems = carrelloDAO.viewCart(loggedUser.getUsername());

            int max = ordineDAO.selectMaxOrderId();
            int newOrderID = max+1;
            /* Seleziono l'id più grande, e aggiungo 1, in modo da non avere duplicati*/

            /* Setto i parametri*/
            User user = userDAO.findByUsername(loggedUser.getUsername());
            Carta carta = cartaDAO.findByNumeroCarta(metodoPagamento);

            /* Setto la data dell'ordine a quella corrente, cosi non avrò mai duplicati*/
            Date in = new Date();
            LocalDateTime LDT = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
            Date out = Date.from(LDT.atZone(ZoneId.systemDefault()).toInstant());

            /* Controllo della disponibilita' di magazzino*/
            StringBuilder errorMessage = new StringBuilder();
            int i;
            for(i=0; i< cartItems.size(); i++){

                Carrello carrello = carrelloDAO.findByISBNAndUsername(cartItems.get(i).getFumetto().getISBN(), loggedUser.getUsername());

                if((contenutoNelMagazzinoDAO.getAvailability(cartItems.get(i).getFumetto().getISBN(),contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino()) >= (carrelloDAO.getQuantity(cartItems.get(i).getFumetto().getISBN(),loggedUser.getUsername())))){

                }
                else  errorMessage.append(cartItems.get(i).getFumetto().getISBN()).append(" non e' disponibile\n");

            }

            /* Se il messaggio di errore esiste, lo setto come attributo da passare alla view*/
            if(errorMessage.length()!=0){

                errorMessage.append("Impossibile effettuare l'ordine.");
                applicationMessage= String.valueOf(errorMessage);

                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage",applicationMessage);
                request.setAttribute("viewUrl", "ProductsManagement/view");
            }

            /* Se non ho nessun messaggio di errore */
            /* E il metodo di pagamento è valido e la carta non è scaduta, procedo con l'ordine*/
            else if(errorMessage.length()==0 && carta != null && (carta.getDataScadenza().compareTo(out))>0) {
                try {

                    /* Creo un singolo ordine, poi lo prendo come parametro, e lo uso per costruirci su - */
                    /* Le righe nel database di contenutoNellOrdine che contiene i vari prodotti nel singolo ordine */
                    /* Per ogni singolo ordine il nome del fornitore può non essere unico, è da spostarlo in contenutoNellOrdine */

                    ordineDAO.create(user, indirizzoSpedizione, newOrderID, carta, "IN PREPARAZIONE", (java.sql.Date) out);

                    Ordine ordine = ordineDAO.findByOrderIdentificativo(newOrderID);
                    for (Carrello cartItem : cartItems) {

                        CentroVendita centroVendita = centroVenditaDAO.findByNomeCentro(fornitoDaArrayList.get(i).getCentroVendita().getNomeCentro());
                        Magazzino magazzino = magazzinoDAO.findByNomeMagazzino(contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino());
                        contenutoNellOrdineDAO.create(ordine, cartItem.getFumetto(), cartItem.getQuantita(),centroVendita, magazzino);
                    }
                    carrelloDAO.flushCart(loggedUser.getUsername());
                    applicationMessage = "L'effettuazione dell'ordine è andata a buon fine.";

                } catch (RuntimeException | DuplicatedObjectException e) {
                    logger.log(Level.INFO, "Errore nella conferma dell'ordine.");
                    throw new RuntimeException(e);
                }

                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage",applicationMessage);
                request.setAttribute("viewUrl", "OrderManagement/confirmView");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


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

    private static void commonView(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request){

        UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
        User loggedUser = sessionUserDAO.findLoggedUser();

        CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
        FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
        ArrayList<Fumetto> fumetti = fumettoDAO.findInCart(loggedUser.getUsername());
        ArrayList<Carrello> cartItems = carrelloDAO.viewCart(loggedUser.getUsername());

        float totalPrice = carrelloDAO.calculatePrice(loggedUser.getUsername());

        request.setAttribute("totalPrice",totalPrice);
        request.setAttribute("fumetti",fumetti);
        request.setAttribute("cartItems",cartItems);
        /* Passo tutti i prodotti nel carrello e calcolo la somma dei singoli prezzi per il prezzo totale*/

    }


}
