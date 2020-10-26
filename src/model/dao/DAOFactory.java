package model.dao;

import model.dao.CookieImpl.CookieDAOFactory;
import model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;
import model.mo.FornitoDa;

import java.util.Map;

public abstract class DAOFactory {

    /* List of DAO types supported by the factory */
    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";
    public static final String COOKIEIMPL = "CookieImpl";

    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    public abstract UserDAO getUserDAO();
    public abstract CartaDAO getCartaDAO();
    public abstract CentroVenditaDAO getCentroVenditaDAO();
    public abstract FumettoDAO getFumettoDAO();
    public abstract MagazzinoDAO getMagazzinoDAO();
    public abstract ContenutoNelMagazzinoDAO getContenutoNelMagazzinoDAO();
    public abstract OrdineDAO getOrdineDao();
    public abstract ContenutoNellOrdineDAO getContenutoNellOrdineDAO();
    public abstract CarrelloDAO getCarrelloDAO();
    public abstract FornitoDaDAO getFornitoDaDAO();

    public static DAOFactory getDAOFactory(String whichFactory, Map factoryParameters){

        if(whichFactory.equals(MYSQLJDBCIMPL)){
            return new MySQLJDBCDAOFactory(factoryParameters);
        }
        else if (whichFactory.equals(COOKIEIMPL)){
            return new CookieDAOFactory(factoryParameters);
        }
        else {
            return null;
        }
    }

}