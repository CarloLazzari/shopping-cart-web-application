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

            String ISBN = request.getParameter("ISBN");
            String nomeFornitore = request.getParameter("nomeFornitore");
            String nomeMagazzino = request.getParameter("nomeMagazzino");
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();

            Fumetto fumetto = fumettoDAO.showFumetto(ISBN);
            FornitoDa fornitoDa = fornitoDaDAO.findByFumettoISBNandCentroVenditaRef(ISBN,nomeFornitore);
            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN,nomeMagazzino);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("fumetto",fumetto);
            request.setAttribute("fornitoDa",fornitoDa);
            request.setAttribute("contenutoNelMagazzino",contenutoNelMagazzino);
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

            /* DAO */
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
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
            ArrayList<Fumetto> comicsInCart = new ArrayList<>();
            comicsInCart = fumettoDAO.findInCart(loggedUser.getUsername());

            int max = ordineDAO.selectMaxOrderID();
            int newOrderID = max+1;
            /* Seleziono l'id più grande, e aggiungo 1, in modo da non avere duplicati*/

            /* Setto i parametri*/
            User user = userDAO.findByUsername(loggedUser.getUsername());
            Carta carta = cartaDAO.findByCardNumber(metodoPagamento);

            /* Setto la data dell'ordine a quella corrente*/
            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

            /* Controllo della disponibilita' di magazzino*/
            StringBuilder errorMessage = new StringBuilder();
            int i;
            for(i=0; i< cartItems.size(); i++){
                if((contenutoNelMagazzinoDAO.getAvailability(cartItems.get(i).getFumetto().getISBN(),contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino()) >= cartItems.get(i).getQuantita())){
                    /*Do nothing*/
                }
                else  errorMessage.append(comicsInCart.get(i).getTitolo()).append(" ").append(comicsInCart.get(i).getNumero()).append(" non e' disponibile o non è disponibile con quella quantità.").append(" ");
            }

            /* Se il messaggio di errore esiste, lo setto come attributo da passare alla view*/
            if(errorMessage.length()!=0){

                errorMessage.append("Impossibile effettuare l'ordine.");
                applicationMessage= String.valueOf(errorMessage);

                commonView(daoFactory, sessionDAOFactory, request);

                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage",applicationMessage);
                request.setAttribute("viewUrl", "confirmOrderManagement/confirmView");
            }

            /* Se non ho nessun messaggio di errore */
            /* .. il metodo di pagamento è valido, la carta non è scaduta */
            /* e la carta appartiene effettivamente all'utente loggato, procedo con l'ordine. */
            else if((carta != null) && (carta.getDataScadenza().compareTo(sqlDate) > 0) && (carta.getUser().getUsername().equals(loggedUser.getUsername()))) {
                try {

                    /* Creo un singolo ordine, poi lo prendo come parametro, e lo uso per costruirci su - */
                    /* - le righe nel database di "contenutoNellOrdine" che contiene i vari prodotti nel singolo ordine */

                    ordineDAO.create(user, indirizzoSpedizione, newOrderID, carta, "In preparazione", sqlDate);

                    Ordine ordine = ordineDAO.findByOrderID(newOrderID);
                    for (i=0; i<cartItems.size(); i++) {

                        CentroVendita centroVendita = centroVenditaDAO.findByNomeCentro(fornitoDaArrayList.get(i).getCentroVendita().getNomeCentro());
                        Magazzino magazzino = magazzinoDAO.findByNomeMagazzino(contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino());
                        contenutoNellOrdineDAO.create(ordine, cartItems.get(i).getFumetto(), cartItems.get(i).getQuantita(),centroVendita, magazzino);
                        /* Devo rimuovere anche i prodotti comprati dal magazzino */
                        /* Per ogni prodotto nel carrello rimuovo di una certa quantità nel magazzino */
                        /* Questa quantità è pari alla quantità presente nel carrello quindi ciclo su essa*/
                        int index;
                        for(index=0; index<cartItems.get(i).getQuantita(); index++) {
                            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(cartItems.get(i).getFumetto().getISBN(), contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino());
                            contenutoNelMagazzinoDAO.removeQuantityFromWarehouse(contenutoNelMagazzino);
                        }
                    }
                    carrelloDAO.flushCart(loggedUser.getUsername());
                    applicationMessage = "L'effettuazione dell'ordine è andata a buon fine.";

                } catch (RuntimeException e) {
                    logger.log(Level.INFO, "Errore nella conferma dell'ordine.");
                    throw new RuntimeException(e);
                } catch (DuplicatedObjectException e) {
                    e.printStackTrace();
                }

                int whichHalf = 0;

                if(request.getParameter("whichHalf")!=null){
                    whichHalf= Integer.parseInt(request.getParameter("whichHalf"));
                }

                /* Lists */
                ArrayList<Fumetto> fumettoArrayList = fumettoDAO.findRandomFumetti();
                ArrayList<ContenutoNelMagazzino> contents = contenutoNelMagazzinoDAO.findRandomContenutoNelMagazzino();
                ArrayList<FornitoDa> forniture = fornitoDaDAO.findRandomFornitoDa();

                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("fumettoArrayList",fumettoArrayList);
                request.setAttribute("contenutoNelMagazzinoArrayList",contents);
                request.setAttribute("fornitoDaArrayList", forniture);
                request.setAttribute("whichHalf",whichHalf);
                request.setAttribute("applicationMessage",applicationMessage);
                request.setAttribute("viewUrl", "homeManagement/view");

            } else {
                applicationMessage = "Errore nella conferma dell'ordine. La carta di credito non esiste o è scaduta.";
                logger.log(Level.INFO,"Errore nella conferma dell'ordine. La carta di credito fornita non esiste o è scaduta.");

                commonView(daoFactory, sessionDAOFactory, request);

                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage",applicationMessage);
                request.setAttribute("viewUrl", "confirmOrderManagement/confirmView");
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
