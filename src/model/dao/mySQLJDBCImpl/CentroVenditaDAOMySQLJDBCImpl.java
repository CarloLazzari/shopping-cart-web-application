package model.dao.mySQLJDBCImpl;

import model.dao.CentroVenditaDAO;
import model.mo.CentroVendita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CentroVenditaDAOMySQLJDBCImpl implements CentroVenditaDAO {

    Connection conn;

    public CentroVenditaDAOMySQLJDBCImpl(Connection conn){ this.conn = conn;}

    /* Per l'admin */
    @Override
    public CentroVendita findByNomeCentro(String nome_centro) {

        PreparedStatement ps;
        CentroVendita centroVendita = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM centro_vendita "
                    + " WHERE "
                    + " NOME_CENTRO = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1,nome_centro);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                centroVendita = read(resultSet);
            }
            resultSet.close();
            ps.close();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return centroVendita;
    }

    @Override
    public ArrayList<CentroVendita> findAllCentri() {

        PreparedStatement ps;
        CentroVendita centroVendita;
        ArrayList<CentroVendita> centri = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM centro_vendita ";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                centroVendita = read(resultSet);
                centri.add(centroVendita);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return centri;
    }


    CentroVendita read(ResultSet rs){

        CentroVendita centroVendita = new CentroVendita();
        try{
            centroVendita.setNomecentro(rs.getString("NOME_CENTRO"));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        try{
            centroVendita.setIndirizzo(rs.getString("INDIRIZZO"));
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return centroVendita;
    }

    @Override
    public CentroVendita create(String nome_centro, String indirizzo) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void delete(CentroVendita centroVendita) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void update(CentroVendita centroVendita) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
