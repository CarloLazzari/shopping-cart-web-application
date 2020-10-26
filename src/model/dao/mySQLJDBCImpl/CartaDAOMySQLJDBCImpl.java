package model.dao.mySQLJDBCImpl;

import model.dao.CartaDAO;
import model.mo.Carta;
import model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartaDAOMySQLJDBCImpl implements CartaDAO {

    Connection conn;

    public CartaDAOMySQLJDBCImpl(Connection conn){ this.conn = conn;}

    /* Per lo user */


    @Override
    public Carta findByNumeroCarta(String numeroCarta) {

        Carta carta = null;

        PreparedStatement ps;

        try {
            String sql
                    = " SELECT *"
                    + " FROM carta "
                    + " WHERE NUMERO_CARTA = ? "
                    + " AND DELETED='0'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, numeroCarta);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                carta = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carta;

    }

    @Override
    public Carta findByNumeroCartaAndIntestatario(String numeroCarta, String intestatario) {

        Carta carta = null;

        PreparedStatement ps;

        try {
            String sql
                    = " SELECT *"
                    + " FROM carta "
                    + " WHERE NUMERO_CARTA = ? AND INTESTATARIO = ?"
                    + " AND DELETED='0'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, numeroCarta);
            ps.setString(2, intestatario);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                carta = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carta;
    }

    /* Nelle find by ci va il parametro di ricerca */
    /* Per lo user */
    @Override
    public ArrayList<Carta> findByCartaIntestatario(String nomeIntestatario) throws SQLException {

        PreparedStatement ps;

        Carta carta;
        ArrayList<Carta> carte = new ArrayList<>();

        try {
            String sql
                        = " SELECT *"
                        + " FROM carta "
                        + " WHERE "
                        + " INTESTATARIO = ?"
                        + " AND DELETED = '0'";

            ps = conn.prepareStatement(sql);
            ps.setString(1,nomeIntestatario);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                carta=read(resultSet);
                carte.add(carta);
            }
            resultSet.close();
            ps.close();

        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carte;

    }

    /* Per l'admin */
    @Override
    public ArrayList<Carta> findAllCarte() {

        PreparedStatement ps;
        Carta carta;
        ArrayList<Carta> carte = new ArrayList<>();

        try{

            String sql
                    = " SELECT * "
                    + " FROM carta "
                    + " WHERE DELETED = 0";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                carta=read(resultSet);
                carte.add(carta);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carte;
    }

    Carta read(ResultSet rs){

        Carta carta = new Carta();
        User user = new User();
        carta.setUser(user);

        try{
            carta.setNumeroCarta(rs.getString("NUMERO_CARTA"));
        } catch (SQLException e) {
        }
        try{
            carta.getUser().setUsername(rs.getString("INTESTATARIO"));
        } catch (SQLException e) {
        }
        try{
            carta.setDataScadenza(rs.getDate("DATA_SCADENZA"));
        } catch (SQLException e) {
        }

        return carta;

    }

    @Override
    public void Delete(Carta carta) throws UnsupportedOperationException {

        throw new UnsupportedOperationException("Not implemented yet.");

    }

}