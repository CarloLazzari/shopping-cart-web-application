package model.dao.mySQLJDBCImpl;

import model.mo.User;
import model.dao.UserDAO;

import java.sql.*;
import java.util.ArrayList;


public class UserDAOMySQLJDBCImpl implements UserDAO {

    Connection conn;

    public UserDAOMySQLJDBCImpl(Connection conn){ this.conn = conn; }

    /* Per l'admin */
    @Override
    public User findByUsername(String username) {

        PreparedStatement ps;
        User user = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM user "
                    + " WHERE "
                    + " USERNAME = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1,username);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public ArrayList<Integer> countOrderByUsername() {

        PreparedStatement ps;
        int result;
        ArrayList<Integer> resultList = new ArrayList<>();

        try {
            String sql
                    = " SELECT count(ID_ORDINE) "
                    + " FROM ordine "
                    + " RIGHT JOIN user ON NOME_EFFETTUANTE = USERNAME  "
                    + " GROUP BY USERNAME ORDER BY USERNAME asc";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                result = resultSet.getInt("count(ID_ORDINE)");
                resultList.add(result);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultList;
    }

    /* Per l'admin */
    @Override
    public ArrayList<User> findAllUsers() {

        PreparedStatement ps;
        User user;
        ArrayList<User> users = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM user ";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                user = read(resultSet);
                users.add(user);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }


    /* Per l'admin */
    @Override
    public void block(User user) {

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE user"
                    + " SET BLOCCATO = 'Y'"
                    + " WHERE "
                    + " USERNAME = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.executeUpdate();
            ps.close();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void unblock(User user) {

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE user"
                    + " SET BLOCCATO = 'N'"
                    + " WHERE "
                    + " USERNAME = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.executeUpdate();
            ps.close();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int countOrders(String Username){

        int result = 0;

        PreparedStatement ps;

        try {
            String sql
                    = "SELECT count(ID_ORDINE)"
                    + " FROM USER JOIN ordine ON USERNAME = NOME_EFFETTUANTE "
                    + " WHERE USERNAME = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1,Username);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next())
                result = resultSet.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    User read(ResultSet rs){

        User user = new User();
        try{
            user.setData(rs.getDate("DATA_N"));
        } catch (SQLException e) {
        }
        try{
            user.setEmail(rs.getString("EMAIL"));
        } catch (SQLException e) {
        }
        try{
            user.setFirstname(rs.getString("NOME"));
        } catch (SQLException e) {
        }
        try{
            user.setSurname(rs.getString("COGNOME"));
        } catch (SQLException e) {
        }
        try{
            user.setIndirizzo(rs.getString("INDIRIZZO"));
        } catch (SQLException e) {
        }
        try{
            user.setUsername(rs.getString("USERNAME"));
        } catch (SQLException e) {
        }
        try{
            user.setPassword(rs.getString("PASSWORD"));
        } catch (SQLException e) {
        }
        try{
            user.setBlocked(rs.getString("BLOCCATO"));
        }  catch (SQLException ex) {
        }
        try{
            user.setAdmin(rs.getString("IS_ADMIN"));
        } catch (SQLException ex) {
        }

        return user;

    }

    /* Da implementare nei cookie*/
    @Override
    public User findLoggedUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User create(String username, String password, String nome, String cognome, String email, String dataNascita, String indirizzo, String blocked, String isAdmin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}