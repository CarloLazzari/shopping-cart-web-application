package model.dao.mySQLJDBCImpl;

import model.dao.CarrelloDAO;
import model.mo.Carrello;
import model.mo.Fumetto;
import model.mo.User;

import java.sql.*;
import java.util.ArrayList;

public class CarrelloDAOMySQLJDBCImpl implements CarrelloDAO {

    Connection conn;

    public CarrelloDAOMySQLJDBCImpl(Connection conn){ this.conn= conn; }

    @Override
    public Carrello addToCart(Fumetto fumetto, User user) {

        Carrello cart = new Carrello();
        cart.setUser(user);
        cart.setFumetto(fumetto);

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT USERNAME_UTENTE, ISBN_FUM "
                    + " FROM ha_nel_carrello "
                    + " WHERE "
                    + " USERNAME_UTENTE = ? AND"
                    + " ISBN_FUM = ?";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++, cart.getUser().getUsername());
            ps.setString(i++, cart.getFumetto().getISBN());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();
            ps.close();

            if (exists) {

                sql
                        = " UPDATE ha_nel_carrello "
                        + " SET "
                        + " QUANTITA = QUANTITA + 1 "
                        + " WHERE USERNAME_UTENTE = ? AND ISBN_FUM = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, cart.getUser().getUsername());
                ps.setString(i++, cart.getFumetto().getISBN());
                ps.executeUpdate();
                ps.close();

            }

            else {
                sql
                        = " INSERT INTO ha_nel_carrello "
                        + " (USERNAME_UTENTE, ISBN_FUM, QUANTITA)"
                        + " VALUES (?,?,'1')";

                i = 1;
                ps = conn.prepareStatement(sql);
                ps.setString(i++, cart.getUser().getUsername());
                ps.setString(i++, cart.getFumetto().getISBN());
                ps.executeUpdate();
                ps.close();
            }

        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart;
    }


    @Override
    public void removeFromCart(Carrello carrello) {

        PreparedStatement ps;

        try {
            String sql
                    = " DELETE "
                    + " FROM ha_nel_carrello "
                    + " WHERE USERNAME_UTENTE = ? AND ISBN_FUM = ?";

            int i = 1;
            ps = conn.prepareStatement(sql);
            ps.setString(i++, carrello.getUser().getUsername());
            ps.setString(i++, carrello.getFumetto().getISBN());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addQuantity(Carrello carrello) {

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE ha_nel_carrello"
                    + " SET QUANTITA = QUANTITA + 1 "
                    + " WHERE USERNAME_UTENTE = ? AND ISBN_FUM = ?";

            int i = 1;
            ps = conn.prepareStatement(sql);
            ps.setString(i++,carrello.getUser().getUsername());
            ps.setString(i++, carrello.getFumetto().getISBN());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void substractQuantity(Carrello carrello) {

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE ha_nel_carrello"
                    + " SET QUANTITA = QUANTITA - 1"
                    + " WHERE USERNAME_UTENTE = ? AND ISBN_FUM = ?";

            int i = 1;
            ps = conn.prepareStatement(sql);
            ps.setString(i++,carrello.getUser().getUsername());
            ps.setString(i++, carrello.getFumetto().getISBN());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public float calculatePrice(String username){

        float result = 0;
        PreparedStatement ps;

        try {
            String sql
                    = " SELECT SUM(QUANTITA*PREZZO) "
                    + " FROM ha_nel_carrello "
                    + " JOIN fumetto "
                    + " ON ISBN_FUM=ISBN"
                    + " WHERE USERNAME_UTENTE = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,username);
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                result= resultSet.getFloat("SUM(QUANTITA*PREZZO)");
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;

    }

    @Override
    public int getQuantity(String username, String ISBN) {

        int quantity = 0;
        PreparedStatement ps;

        try {
            String sql
                    = " SELECT QUANTITA "
                    + " FROM ha_nel_carrello "
                    + " WHERE "
                    + " ISBN_FUM = ? AND USERNAME_UTENTE = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,ISBN);
            ps.setString(i++,username);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                quantity = (resultSet.getInt("QUANTITA"));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return quantity;
    }

    @Override
    public Carrello findByISBNAndUsername(String ISBNProdotto, String username) {

        PreparedStatement ps;
        Carrello carrello = null;

        try {
            String sql
                    = " SELECT * "
                    + " FROM ha_nel_carrello "
                    + " WHERE ISBN_FUM = ? "
                    + " AND USERNAME_UTENTE = ?";
            
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, ISBNProdotto);
            ps.setString(i++, username);
            ResultSet resultSet = ps.executeQuery();
            
            if(resultSet.next()){
                carrello=read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carrello;
    }

    @Override
    public void flushCart(String username) {

        PreparedStatement ps;

        try {
            String sql
                    = " DELETE "
                    + " FROM ha_nel_carrello "
                    + " WHERE USERNAME_UTENTE = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ArrayList<Carrello> viewCart(String username) {

        PreparedStatement ps;
        Carrello cart;
        ArrayList<Carrello> cart_items = new ArrayList<>();

        try {

            String sql
                    = " SELECT * "
                    + " FROM ha_nel_carrello "
                    + " WHERE USERNAME_UTENTE = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1,username);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                cart = read(resultSet);
                cart_items.add(cart);
            }

            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart_items;
    }

    Carrello read(ResultSet rs){

        Carrello cart = new Carrello();
        User user = new User();
        Fumetto fumetto = new Fumetto();

        cart.setUser(user);
        cart.setFumetto(fumetto);

        try{
            cart.getUser().setUsername(rs.getString("USERNAME_UTENTE"));
        } catch (SQLException e) {
        }
        try{
            cart.getFumetto().setISBN(rs.getString("ISBN_FUM"));
        } catch (SQLException e) {
        }
        try {
            cart.setQuantita(rs.getInt("QUANTITA"));
        } catch (SQLException e) {
        }

        return cart;

    }
}