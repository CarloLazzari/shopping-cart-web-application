package model.dao;

import model.dao.CookieImpl.CookieDAOFactory;
import model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;
import model.mo.FornitoDa;

import java.util.Map;

/* In computer software, a data access object (DAO) is a pattern that provides an abstract interface to some type of database or other persistence mechanism. 
By mapping application calls to the persistence layer, the DAO provides some specific data operations without exposing details of the database. 
This isolation supports the single responsibility principle. 
It separates what data access the application needs, in terms of domain-specific objects and data types (the public interface of the DAO), 
    from how these needs can be satisfied with a specific DBMS, database schema, etc. (the implementation of the DAO). */

/* In class-based programming, the factory method pattern is a creational pattern that uses factory methods to deal with the problem of creating objects
    without having to specify the exact class of the object that will be created. 
This is done by creating objects by calling a factory method—either specified in an interface and implemented by child classes, 
    or implemented in a base class and optionally overridden by derived classes—rather than by calling a constructor. */

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
