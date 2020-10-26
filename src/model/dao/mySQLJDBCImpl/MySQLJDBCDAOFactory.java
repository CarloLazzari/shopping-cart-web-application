package model.dao.mySQLJDBCImpl;

import model.dao.*;
import model.mo.ContenutoNelMagazzino;
import services.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;
    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters = factoryParameters;
    }

    /*Inizia la transazione*/
    @Override
    public void beginTransaction(){
        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            String serverTimezone = Configuration.SERVER_TIMEZONE;
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        }
        catch(ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitTransaction() {
        try{
            this.connection.commit();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {
        try{
            this.connection.rollback();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeTransaction() {
        try{
            this.connection.close();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDAO getUserDAO() { return new UserDAOMySQLJDBCImpl(connection); }

    @Override
    public CartaDAO getCartaDAO() { return new CartaDAOMySQLJDBCImpl(connection); }

    @Override
    public CentroVenditaDAO getCentroVenditaDAO() {
        return new CentroVenditaDAOMySQLJDBCImpl(connection);
    }

    @Override
    public FumettoDAO getFumettoDAO() {
        return new FumettoEContenutoNelMagazzinoDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ContenutoNelMagazzinoDAO getContenutoNelMagazzinoDAO(){ return new FumettoEContenutoNelMagazzinoDAOMySQLJDBCImpl(connection);}

    @Override
    public ContenutoNellOrdineDAO getContenutoNellOrdineDAO(){ return new OrdineEContenutoNellOrdineDAOMySQLJDBCImpl(connection);}

    @Override
    public OrdineDAO getOrdineDao() {  return new OrdineEContenutoNellOrdineDAOMySQLJDBCImpl(connection); }

    @Override
    public MagazzinoDAO getMagazzinoDAO() {
        return new MagazzinoDAOMySQLJDBCImpl(connection);
    }

    @Override
    public CarrelloDAO getCarrelloDAO() { return new CarrelloDAOMySQLJDBCImpl(connection);}

    @Override
    public FornitoDaDAO getFornitoDaDAO() { return new FornitoDaDAOMySQLJDBCImpl(connection); }

}