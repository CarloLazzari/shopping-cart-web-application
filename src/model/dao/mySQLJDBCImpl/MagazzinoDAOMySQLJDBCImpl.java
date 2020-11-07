package model.dao.mySQLJDBCImpl;

import model.dao.MagazzinoDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;
import model.mo.Magazzino;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MagazzinoDAOMySQLJDBCImpl implements MagazzinoDAO {

    Connection conn;

    public MagazzinoDAOMySQLJDBCImpl(Connection conn){ this.conn = conn;}

    @Override
    public Magazzino create(CentroVendita centroVendita, String nomeMagazzino, String indirizzo) throws DuplicatedObjectException {

        PreparedStatement ps;

        Magazzino magazzino = new Magazzino();
        magazzino.setCentroVendita(centroVendita);
        magazzino.setNomeMagazzino(nomeMagazzino);
        magazzino.setIndirizzo(indirizzo);

        try {
            String sql
                    = " SELECT NOME_MAGAZZINO "
                    + " FROM magazzino "
                    + " WHERE NOME_MAGAZZINO = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i, magazzino.getNomeMagazzino());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists=resultSet.next();
            resultSet.close();

            if(exists) {
                throw new DuplicatedObjectException("MagazzinoDAOMySQLJDCBImpl: Tentativo di inserimento di un magazzino già esistente");
            }

            sql
                = " INSERT INTO magazzino"
                + " (NOME_CENTRO_APPARTENENZA, INDIRIZZO, NOME_MAGAZZINO) "
                + " VALUES (?,?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, magazzino.getCentroVendita().getNomeCentro());
            ps.setString(i++, magazzino.getIndirizzo());
            ps.setString(i, magazzino.getNomeMagazzino());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return magazzino;
    }

    @Override
    public void update(Magazzino magazzino) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {
            String sql
                    = " SELECT NOME_MAGAZZINO"
                    + " FROM magazzino"
                    + " WHERE NOME_MAGAZZINO <> ?"
                    + " AND INDIRIZZO = ?"
                    + " AND NOME_CENTRO_APPARTENENZA = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, magazzino.getNomeMagazzino());
            ps.setString(i++, magazzino.getIndirizzo());
            ps.setString(i, magazzino.getCentroVendita().getNomeCentro());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();

            if (exists) {
                throw new DuplicatedObjectException("MagazzinoDAOMySQLJDCBImpl: Tentativo di inserimento di magazzino già esistente");
            }

            sql
                = " UPDATE magazzino "
                + " SET "
                + " INDIRIZZO = ?"
                + " WHERE NOME_MAGAZZINO = ?";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, magazzino.getIndirizzo());
            ps.setString(i, magazzino.getNomeMagazzino());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public Magazzino findByNomeMagazzino(String nomeMagazzino){

        PreparedStatement ps;
        Magazzino magazzino = null;

        try {
            String sql
                    = " SELECT * "
                    + " FROM magazzino "
                    + " WHERE "
                    + " NOME_MAGAZZINO = ? ";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i, nomeMagazzino);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                magazzino = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return magazzino;
    }

    @Override
    public ArrayList<Magazzino> findAllMagazzini() {

        PreparedStatement ps;
        Magazzino magazzino;
        ArrayList<Magazzino> magazzini = new ArrayList<>();

        try{

            String sql
                    = " SELECT * "
                    + " FROM magazzino ";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                magazzino = read(resultSet);
                magazzini.add(magazzino);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return magazzini;
    }

    @Override
    public void delete(Magazzino magazzino) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    Magazzino read(ResultSet rs) throws SQLException {

        Magazzino magazzino = new Magazzino();
        CentroVendita centroVendita = new CentroVendita();
        magazzino.setCentroVendita(centroVendita);

        try {
            magazzino.getCentroVendita().setNomecentro(rs.getString("NOME_CENTRO_APPARTENENZA"));
        } catch (SQLException ignored) {
        }
        try {
            magazzino.setIndirizzo(rs.getString("INDIRIZZO"));
        } catch (SQLException ignored) {
        }
        try{
            magazzino.setNomeMagazzino(rs.getString("NOME_MAGAZZINO"));
        }
        catch (SQLException ignored) {
        }

        return magazzino;
        
    }
}
