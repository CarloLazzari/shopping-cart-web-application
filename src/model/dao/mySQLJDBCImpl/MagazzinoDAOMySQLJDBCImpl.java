package model.dao.mySQLJDBCImpl;

import model.dao.MagazzinoDAO;
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

    /* Per l'admin */
    @Override
    public Magazzino findByNomeMagazzino(String nome_magazzino){

        PreparedStatement ps;
        Magazzino magazzino = null;

        try {
            String sql
                    = " SELECT * "
                    + " FROM magazzino "
                    + " WHERE "
                    + " NOME_MAGAZZINO = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nome_magazzino);

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

    /* Per l'admin */
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

    Magazzino read(ResultSet rs) throws SQLException {

        Magazzino magazzino = new Magazzino();
        CentroVendita centroVendita = new CentroVendita();
        magazzino.setCentroVendita(centroVendita);

        try {
            magazzino.getCentroVendita().setNomecentro(rs.getString("NOME_CENTRO_REF"));
        } catch (SQLException e) {
        }
        try {
            magazzino.setIndirizzo(rs.getString("INDIRIZZO"));
        } catch (SQLException e) {
        }
        try{
            magazzino.setNomeMagazzino(rs.getString("NOME_MAGAZZINO"));
        }
        catch (SQLException e) {
        }

        return magazzino;

    }
}