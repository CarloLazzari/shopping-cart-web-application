package model.dao.mySQLJDBCImpl;

import model.dao.CentroVenditaDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CentroVenditaDAOMySQLJDBCImpl implements CentroVenditaDAO {

    Connection conn;

    public CentroVenditaDAOMySQLJDBCImpl(Connection conn){ this.conn = conn;}

    @Override
    public CentroVendita create(String nomeCentro, String indirizzo) throws DuplicatedObjectException {

        PreparedStatement ps;

        CentroVendita centroVendita = new CentroVendita();
        centroVendita.setNomecentro(nomeCentro);
        centroVendita.setIndirizzo(indirizzo);

        try {
            String sql
                    = " SELECT NOME_CENTRO "
                    + " FROM centro_vendita "
                    + " WHERE NOME_CENTRO = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i,centroVendita.getNomeCentro());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();

            if (exists) {
                throw new DuplicatedObjectException("CentroVenditaDAOMySQLJDCBImpl: Tentativo di inserimento di un centro vendita già esistente");
            }

            sql
                = " INSERT INTO centro_vendita"
                + "(NOME_CENTRO, INDIRIZZO)"
                + " VALUES (?,?)";

            ps=conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, centroVendita.getNomeCentro());
            ps.setString(i, centroVendita.getIndirizzo());

            ps.executeUpdate();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  centroVendita;

    }

    @Override
    public void update(CentroVendita centroVendita) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {
            String sql
                    = " SELECT NOME_CENTRO "
                    + " FROM centro_vendita "
                    + " WHERE NOME_CENTRO <> ?"
                    + " AND INDIRIZZO = ?";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++, centroVendita.getNomeCentro());
            ps.setString(i, centroVendita.getIndirizzo());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();

            if (exists) {
                throw new DuplicatedObjectException("CentroVenditaDAOMySQLJDCBImpl: Tentativo di inserimento di un centro vendita già esistente");
            }

            sql
                = " UPDATE centro_vendita"
                + " SET "
                + " INDIRIZZO = ?"
                + " WHERE NOME_CENTRO = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(i++, centroVendita.getNomeCentro());
            ps.setString(i, centroVendita.getIndirizzo());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public CentroVendita findByNomeCentro(String nomeCentro) {

        PreparedStatement ps;
        CentroVendita centroVendita = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM centro_vendita "
                    + " WHERE "
                    + " NOME_CENTRO = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i,nomeCentro);

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
        } catch (SQLException ignored) {
        }
        try{
            centroVendita.setIndirizzo(rs.getString("INDIRIZZO"));
        } catch (SQLException ignored) {
        }

        return centroVendita;
    }


    @Override
    public void delete(CentroVendita centroVendita) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
