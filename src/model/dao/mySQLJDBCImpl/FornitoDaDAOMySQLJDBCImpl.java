package model.dao.mySQLJDBCImpl;

import jdk.nashorn.internal.ir.annotations.Ignore;
import model.dao.FornitoDaDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;
import model.mo.ContenutoNelMagazzino;
import model.mo.FornitoDa;
import model.mo.Fumetto;

import java.sql.*;
import java.util.ArrayList;

public class FornitoDaDAOMySQLJDBCImpl implements FornitoDaDAO {

    Connection conn;

    public FornitoDaDAOMySQLJDBCImpl(Connection conn){ this.conn = conn; }

    @Override
    public FornitoDa create(Fumetto fumetto, CentroVendita centroVendita) throws DuplicatedObjectException{

        PreparedStatement ps;

        FornitoDa fornitoDa = new FornitoDa();

        fornitoDa.setFumetto(fumetto);
        fornitoDa.setCentroVendita(centroVendita);

        try {
            String sql
                    = " SELECT ISBN_FUMETTO_REFERENZIATO, CENTRO_VENDITA_REFERENZIATO "
                    + " FROM fornito_da "
                    + " WHERE ISBN_FUMETTO_REFERENZIATO = ?"
                    + " AND CENTRO_VENDITA_REFERENZIATO = ? ";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,fumetto.getISBN());
            ps.setString(i++, centroVendita.getNomeCentro());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();
            ps.close();

            if (exists) {
                throw new DuplicatedObjectException("FornitoDAOMySQLJDCBImpl: Tentativo di inserimento di un prodotto nel magazzino già esistente");
            }

            sql
                = " INSERT INTO "
                + " fornito_da "
                + " (ISBN_FUMETTO_REFERENZIATO, CENTRO_VENDITA_REFERENZIATO) "
                + " VALUES (?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++,fornitoDa.getFumetto().getISBN());
            ps.setString(i++, fornitoDa.getCentroVendita().getNomeCentro());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fornitoDa;
    }


    /*
    public FornitoDa update(FornitoDa fornitoDa) {

        PreparedStatement ps;

        try {

            String sql
                    = "SELECT ISBN_FUMETTO_REFERENZIATO, CENTRO_VENDITA_REFERENZIATO"
                    + " FROM fornito_da "
                    + " WHERE "
                    + " ISBN_FUMETTO_REFERENZIATO <> ?"
                    + " AND CENTRO_VENDITA_REFERENZIATO <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,fornitoDa.getFumetto().getISBN());
            ps.setString(i++, fornitoDa.getCentroVendita().getNomeCentro());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();

            if (exists) {
                throw new DuplicatedObjectException("FornitoDaDAOMySQLJDCBImpl: Tentativo di inserimento di un prodotto già esistente");
            }

            sql
                = " UPDATE fornito_da "
                + " SET "
                + " ISBN_FUMETTO_REFERENZIATO = ? AND"
                + " CENTRO_VENDITA_REFERENZIATO = ?";

            ps.executeUpdate();

        } catch (SQLException | DuplicatedObjectException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    */

    public FornitoDa findByFumettoISBNandCentroVenditaRef(String ISBN, String nomeCentro){

        PreparedStatement ps;
        FornitoDa fornitoDa = new FornitoDa();

        try {

            String sql
                    = " SELECT * "
                    + " FROM fornito_da "
                    + " WHERE ISBN_FUMETTO_REFERENZIATO = ?"
                    + " AND CENTRO_VENDITA_REFERENZIATO = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, ISBN);
            ps.setString(i++, nomeCentro);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                fornitoDa = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fornitoDa;
    }

    @Override
    public ArrayList<FornitoDa> findAllFornitoDa() {

        PreparedStatement ps;
        FornitoDa fornitoDa;
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();

        try{

            String sql
                    = " SELECT *"
                    + " FROM fornito_da JOIN fumetto f on f.ISBN = fornito_da.ISBN_FUMETTO_REFERENZIATO"
                    + " JOIN contenuto_nel_magazzino cnm on f.ISBN = cnm.ISBN_FUMETTO "
                    + " WHERE DELETED='N'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fornitoDa = read(resultSet);
                fornitoDaArrayList.add(fornitoDa);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fornitoDaArrayList;

    }

    @Override
    public ArrayList<FornitoDa> findAllFornitoDaUnblocked() {

        PreparedStatement ps;
        FornitoDa fornitoDa;
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();

        try{

            String sql
                    = " SELECT *"
                    + " FROM fornito_da "
                    + " JOIN fumetto f on f.ISBN = fornito_da.ISBN_FUMETTO_REFERENZIATO JOIN contenuto_nel_magazzino cnm on f.ISBN = cnm.ISBN_FUMETTO"
                    + " WHERE BLOCCATO = 'N' AND DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fornitoDa = read(resultSet);
                fornitoDaArrayList.add(fornitoDa);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fornitoDaArrayList;

    }

    @Override
    public ArrayList<FornitoDa> findInCart(String username) {

        FornitoDa fornitoDa;
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT ISBN_FUMETTO_REFERENZIATO, CENTRO_VENDITA_REFERENZIATO "
                    + " FROM fornito_da "
                    + " JOIN ha_nel_carrello "
                    + " ON ISBN_FUMETTO_REFERENZIATO = ISBN_FUM "
                    + " WHERE USERNAME_UTENTE = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1,username);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fornitoDa=read(resultSet);
                fornitoDaArrayList.add(fornitoDa);

            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fornitoDaArrayList;

    }

    @Override
    public ArrayList<FornitoDa> freeSearch(String searchString) {

        PreparedStatement ps;
        FornitoDa fornitoDa;
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM fornito_da "
                    + " JOIN fumetto ON ISBN_FUMETTO_REFERENZIATO = ISBN JOIN contenuto_nel_magazzino cnm on fumetto.ISBN = cnm.ISBN_FUMETTO"
                    + " WHERE TITOLO = ? OR AUTORE = ? OR NUMERO = ? AND DELETED ='N'";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fornitoDa = read(resultSet);
                fornitoDaArrayList.add(fornitoDa);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  fornitoDaArrayList;
    }

    @Override
    public ArrayList<FornitoDa> freeSearchUnblocked(String searchString) {

        PreparedStatement ps;
        FornitoDa fornitoDa;
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM fornito_da "
                    + " JOIN fumetto ON ISBN_FUMETTO_REFERENZIATO = ISBN JOIN contenuto_nel_magazzino cnm on fumetto.ISBN = cnm.ISBN_FUMETTO"
                    + " WHERE TITOLO = ? OR AUTORE = ? OR NUMERO = ? AND DELETED ='N'"
                    + " AND BLOCCATO = 'N'";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fornitoDa = read(resultSet);
                fornitoDaArrayList.add(fornitoDa);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  fornitoDaArrayList;
    }

    @Override
    public ArrayList<FornitoDa> findBy(String searchMode, String searchString) {

        PreparedStatement ps;
        FornitoDa fornitoDa;
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM fornito_da "
                    + " JOIN fumetto ON ISBN_FUMETTO_REFERENZIATO = ISBN JOIN contenuto_nel_magazzino cnm on fumetto.ISBN = cnm.ISBN_FUMETTO"
                    + " WHERE ? = ? AND DELETED='N'";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,searchMode);
            ps.setString(i++,searchString);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fornitoDa = read(resultSet);
                fornitoDaArrayList.add(fornitoDa);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  fornitoDaArrayList;
    }

    @Override
    public ArrayList<FornitoDa> findByUnblocked(String searchMode, String searchString) {

        PreparedStatement ps;
        FornitoDa fornitoDa;
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM fornito_da "
                    + " JOIN fumetto ON ISBN_FUMETTO_REFERENZIATO = ISBN JOIN contenuto_nel_magazzino cnm on fumetto.ISBN = cnm.ISBN_FUMETTO"
                    + " WHERE ? = ?"
                    + " AND BLOCCATO = 'N' AND DELETED = 'N'";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,searchMode);
            ps.setString(i++,searchString);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fornitoDa = read(resultSet);
                fornitoDaArrayList.add(fornitoDa);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  fornitoDaArrayList;
    }

    public FornitoDa read(ResultSet rs){
        
        FornitoDa fornitoDa = new FornitoDa();
        Fumetto fumetto = new Fumetto();
        CentroVendita centroVendita = new CentroVendita();

        fornitoDa.setFumetto(fumetto);
        fornitoDa.setCentroVendita(centroVendita);

        try{
            fornitoDa.getFumetto().setISBN(rs.getString("ISBN_FUMETTO_REFERENZIATO"));
        } catch (SQLException e) {
        }
        try{
            fornitoDa.getCentroVendita().setNomecentro(rs.getString("CENTRO_VENDITA_REFERENZIATO"));
        } catch (SQLException e) {
        }
        
        return fornitoDa;
        
    }

    @Override
    public FornitoDa update(FornitoDa fornitoDa) {
        throw new UnsupportedOperationException("Not implement yet");
    }

    @Override
    public FornitoDa delete(FornitoDa fornitoDa) {
        throw new UnsupportedOperationException("Not implement yet");
    }

}
