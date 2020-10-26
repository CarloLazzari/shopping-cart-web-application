package model.dao.mySQLJDBCImpl;

import model.dao.ContenutoNellOrdineDAO;
import model.dao.OrdineDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.*;

import java.sql.*;
import java.util.ArrayList;

/* Un unica classe che implementa 2 interfacce per l'inserimento di un ordine*/
/* Quando io creo un nuovo ordine, ovvero effettuo un ordine dall'applicativo web */
/* - creo si un nuovo ordine, ma un ordine può contenere più prodotti */
/* - quindi creo una tabella di relazione ContenutoNellOrdine che prende come attributi IDOrdine e ISBNFumetto come FK, e quantita*/
/* Quindi le colonne dell'ordine saranno*/
/* OrderID | Data | IndirizzoDest | NomeEffettuante | Dispatcher | MagazzinoRef */

public class OrdineEContenutoNellOrdineDAOMySQLJDBCImpl implements OrdineDAO, ContenutoNellOrdineDAO {

    Connection conn;

    public OrdineEContenutoNellOrdineDAOMySQLJDBCImpl(Connection conn){ this.conn = conn;}

    /* Per il sistema */
    @Override
    public Ordine create(User user,
                         String indirizzoDest,
                         int orderID,
                         Carta carta,
                         String stato,
                         Date data) {

        PreparedStatement ps;
        Ordine ordine = new Ordine();

        ordine.setUser(user);
        ordine.setCarta(carta);
        ordine.setIndirizzoDestinazione(indirizzoDest);
        ordine.setOrderID(orderID);
        ordine.setData(data);


        try {
            String sql
                    = " SELECT ID_ORDINE "
                    + " FROM ordine "
                    + " WHERE "
                    + " ID_ORDINE = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;

            ps.setInt(i++, ordine.getOrderID());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();

            if(exists){
                throw new DuplicatedObjectException("OrdineDAOMySQLJDCBImpl: Tentativo di inserimento di ordine già esistente");
            }

            sql
                = " INSERT INTO ordine"
                + " (ID_ORDINE, DATA, INDIRIZZO_SPEDIZIONE, NOME_EFFETTUANTE, MODALITA_PAGAMENTO, STATO)"
                + " VALUES (?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setInt(i++,ordine.getOrderID());
            ps.setDate(i++, ordine.getData());
            ps.setString(i++, ordine.getIndirizzoDestinazione());
            ps.setString(i++, ordine.getUser().getUsername());
            ps.setString(i++, ordine.getCarta().getNumeroCarta());
            ps.setString(i++, ordine.getStato());

            ps.executeUpdate();

        } catch (SQLException | DuplicatedObjectException e) {
            throw new RuntimeException(e);
        }

        return ordine;

    }

    public ContenutoNellOrdine create(Ordine ordine, Fumetto fumetto, int quantita,  CentroVendita centroVendita,  Magazzino magazzino) {

        ContenutoNellOrdine contenutoNellOrdine = new ContenutoNellOrdine();
        contenutoNellOrdine.setOrdine(ordine);
        contenutoNellOrdine.setFumetto(fumetto);
        contenutoNellOrdine.setQuantita(quantita);
        contenutoNellOrdine.setMagazzino(magazzino);
        contenutoNellOrdine.setCentroVendita(centroVendita);

        PreparedStatement ps;

            try {
                String sql
                        = " SELECT ORDINE_ID_REF, ISBN_FUMETTO_REF "
                        + " FROM contenuto_nell_ordine "
                        + " WHERE ORDINE_ID_REF = ? AND "
                        + " ISBN_FUMETTO_REF = ?";

                int i = 1;
                ps = conn.prepareStatement(sql);
                ps.setInt(i++, contenutoNellOrdine.getOrdine().getOrderID());
                ps.setString(i++,contenutoNellOrdine.getFumetto().getISBN());

                ResultSet resultSet = ps.executeQuery();

                boolean exist;
                exist = resultSet.next();
                resultSet.close();

                if (exist) {
                    throw new DuplicatedObjectException("ContenutoNellOrdineDAOJDBCImpl.create: Tentativo di inserimento di un ContenutoNellOrdine già esistente.");
                }

                sql
                        = " INSERT INTO contenuto_nell_ordine "
                        + " (ORDINE_ID_REF, ISBN_FUMETTO_REF, QUANTITA, NOME_MAGAZZINO, DISPATCHER)"
                        + " VALUES (?,?,?,?,?)";

                i = 1;
                ps = conn.prepareStatement(sql);
                ps.setInt(i++, contenutoNellOrdine.getOrdine().getOrderID());
                ps.setString(i++,contenutoNellOrdine.getFumetto().getISBN());
                ps.setInt(i++, contenutoNellOrdine.getQuantita());
                ps.setString(i++, contenutoNellOrdine.getMagazzino().getNomeMagazzino());
                ps.setString(i++, contenutoNellOrdine.getCentroVendita().getNomeCentro());

                ps.executeUpdate();

            }
            catch (SQLException | DuplicatedObjectException e) {
                throw new RuntimeException();
            }

        return contenutoNellOrdine;

    }

    /* Per lo user e per l'admin*/
    @Override
    public ArrayList<Ordine> findAllOrdiniByNomeEffettuante(String Nome_Effettuante) {

        PreparedStatement ps;
        Ordine ordine;
        ArrayList<Ordine> ordini = new ArrayList<>();

        try {
            String sql
                    = " SELECT *"
                    + " FROM ordine JOIN user u on u.USERNAME = ordine.NOME_EFFETTUANTE"
                    + " WHERE NOME_EFFETTUANTE = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1,Nome_Effettuante);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                ordine = read(resultSet);
                ordini.add(ordine);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return ordini;
    }

    /* Per lo user e per l'admin*/
    @Override
    public ArrayList<Ordine> findAllOrdini() {

        PreparedStatement ps;
        Ordine ordine;
        ArrayList<Ordine> ordini = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM ordine JOIN user u on u.USERNAME = ordine.NOME_EFFETTUANTE"
                    + " JOIN carta c on c.NUMERO_CARTA = ordine.MODALITA_PAGAMENTO";


            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                ordine = read(resultSet);
                ordini.add(ordine);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ordini;

    }

    @Override
    public float calculatePrice(int OrderID) {

        PreparedStatement ps;
        float result = 0;

        try {
            String sql
                    = " SELECT SUM(QUANTITA*PREZZO)"
                    + " FROM contenuto_nell_ordine "
                    + " JOIN fumetto f on f.ISBN = contenuto_nell_ordine.ISBN_FUMETTO_REF"
                    + " JOIN ordine ON ORDINE_ID_REF = ID_ORDINE"
                    + " WHERE ORDINE_ID_REF = ?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1,OrderID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                result = resultSet.getInt("SUM(QUANTITA*PREZZO)");
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return result;
    }

    @Override
    public Ordine findByOrderIdentificativo(int ID) {

        PreparedStatement ps;
        Ordine ordine = null;
        try {

            String sql
                    = " SELECT *"
                    + " FROM ordine "
                    + " WHERE "
                    + " ID_ORDINE = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1,ID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                ordine = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ordine;
    }

    @Override
    public int selectMaxOrderId() {

        PreparedStatement ps;
        String result = null;
        int max = 0;

        try {
            String sql
                    = " SELECT max(ID_ORDINE) "
                    + " FROM ordine ";
                    // " WHERE NOME_EFFETTUANTE = ?";

            ps = conn.prepareStatement(sql);
            //ps.setString(1,username);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                result = resultSet.getString(1);
            }
            max = Integer.parseInt(result);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return max;
    }

    /* Per lo user e per l'admin */
    @Override
    public ArrayList<ContenutoNellOrdine> findAllContenutoNellOrdine() {

        PreparedStatement ps;
        ContenutoNellOrdine contenutoNellOrdine;
        ArrayList<ContenutoNellOrdine> contents = new ArrayList<>();

        try {
            String sql
                = " SELECT * "
                + " FROM contenuto_nell_ordine ";

            ps= conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                contenutoNellOrdine = read_c(resultSet);
                contents.add(contenutoNellOrdine);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return contents;
    }


    @Override
    public ArrayList<ContenutoNellOrdine> findByOrderID(int ID_Ordine) {

        PreparedStatement ps;
        ContenutoNellOrdine contenutoNellOrdine;
        ArrayList<ContenutoNellOrdine> contents = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM contenuto_nell_ordine "
                    + " WHERE ORDINE_ID_REF = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, ID_Ordine);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                contenutoNellOrdine = read_c(resultSet);
                contents.add(contenutoNellOrdine);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return contents;
    }

    @Override
    public ArrayList<Float> calculatePriceCNO() {

        PreparedStatement ps;
        float result = 0;
        ArrayList<Float> prices = new ArrayList<>();

        try {
            String sql
                    = " SELECT SUM(QUANTITA*PREZZO)"
                    + " FROM contenuto_nell_ordine "
                    + " JOIN fumetto f on f.ISBN = contenuto_nell_ordine.ISBN_FUMETTO_REF"
                    + " JOIN ordine ON ORDINE_ID_REF = ID_ORDINE"
                    + " GROUP BY ORDINE_ID_REF "
                    + " ORDER BY ORDINE_ID_REF ASC";

            ps= conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                result = resultSet.getInt("SUM(QUANTITA*PREZZO)");
                prices.add(result);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return prices;
    }

    @Override
    public ArrayList<Float> calculatePriceCNOforWhichOrderID(int OrderID) {

        PreparedStatement ps;
        float result = 0;
        ArrayList<Float> prices = new ArrayList<>();

        try {
            String sql
                    = " SELECT (QUANTITA*PREZZO)"
                    + " FROM contenuto_nell_ordine "
                    + " JOIN fumetto f on f.ISBN = contenuto_nell_ordine.ISBN_FUMETTO_REF"
                    + " JOIN ordine ON ORDINE_ID_REF = ID_ORDINE"
                    + " WHERE ORDINE_ID_REF = ?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1,OrderID);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                result = resultSet.getInt("(QUANTITA*PREZZO)");
                prices.add(result);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return prices;
    }

    @Override
    public ArrayList<Float> calculatePriceforWhichUsername(String username) {

        PreparedStatement ps;
        float result = 0;
        ArrayList<Float> prices = new ArrayList<>();

        try {
            String sql
                    = " SELECT SUM(QUANTITA*PREZZO)"
                    + " FROM "
                    + " user JOIN ordine ON USERNAME = NOME_EFFETTUANTE "
                    + " JOIN contenuto_nell_ordine ON ORDINE_ID_REF = ID_ORDINE "
                    + " JOIN fumetto ON ISBN_FUMETTO_REF = ISBN"
                    + " WHERE USERNAME = ?"
                    + " GROUP BY ID_ORDINE";

            ps = conn.prepareStatement(sql);
            ps.setString(1,username);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                result = resultSet.getInt("SUM(QUANTITA*PREZZO)");
                prices.add(result);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return prices;

    }

    @Override
    public ContenutoNellOrdine findByOrderIDandISBN(int ID_Ordine_Ref, String ISBN) {

        PreparedStatement ps;
        ContenutoNellOrdine contenutoNellOrdine = new ContenutoNellOrdine();

        try {
            String sql
                    = " SELECT * "
                    + " FROM contenuto_nell_ordine "
                    + " WHERE ORDINE_ID_REF = ?"
                    + " AND ISBN_FUMETTO_REF = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, ID_Ordine_Ref);
            ps.setString(2, ISBN);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                contenutoNellOrdine = read_c(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return contenutoNellOrdine;
    }

    @Override
    public int countOrdersByUsername(String username) {

        PreparedStatement ps;
        int resultInt = 0;

        try {
            String sql
                    = " SELECT count(ID_ORDINE) "
                    + " FROM ordine"
                    + " WHERE NOME_EFFETTUANTE = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1,username);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next())
                resultInt = resultSet.getInt(1);

            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return resultInt;

    }

    @Override
    public void updateStatus(Ordine ordine, String stato){

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE ordine "
                    + " SET "
                    + " STATO = ?"
                    + " WHERE ID_ORDINE = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, stato);
            ps.setInt(2, ordine.getOrderID());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    Ordine read(ResultSet rs){

        Ordine ordine = new Ordine();
        User user = new User();
        Carta carta = new Carta();

        ordine.setUser(user);
        ordine.setCarta(carta);

        try{
            ordine.setOrderID(rs.getInt("ID_ORDINE"));
        }catch (SQLException e) {
        }
        try{
            ordine.setData(rs.getDate("DATA"));
        }catch (SQLException e) {
        }
        try{
            ordine.setIndirizzoDestinazione(rs.getString("INDIRIZZO_SPEDIZIONE"));
        }catch (SQLException e) {
        }
        try{
            ordine.getUser().setUsername(rs.getString("NOME_EFFETTUANTE"));
        }catch (SQLException e) {
        }
        try{
            ordine.getCarta().setNumeroCarta(rs.getString("MODALITA_PAGAMENTO"));
        }catch (SQLException e) {
        }
        try{
            ordine.setStato(rs.getString("STATO"));
        } catch (SQLException e) {
        }

        return ordine;

    }

    ContenutoNellOrdine read_c(ResultSet rs){

        ContenutoNellOrdine contenutoNellOrdine = new ContenutoNellOrdine();
        Ordine ordine = new Ordine();
        Fumetto fumetto = new Fumetto();
        CentroVendita centroVendita = new CentroVendita();
        Magazzino magazzino = new Magazzino();

        contenutoNellOrdine.setOrdine(ordine);
        contenutoNellOrdine.setFumetto(fumetto);
        contenutoNellOrdine.setCentroVendita(centroVendita);
        contenutoNellOrdine.setMagazzino(magazzino);

        try{
            contenutoNellOrdine.getOrdine().setOrderID(rs.getInt("ORDINE_ID_REF"));
        } catch (SQLException e) {
        }
        try{
            contenutoNellOrdine.getFumetto().setISBN(rs.getString("ISBN_FUMETTO_REF"));
        } catch (SQLException e) {
        }
        try{
            contenutoNellOrdine.setQuantita(rs.getInt("QUANTITA"));
        } catch (SQLException e) {
        }
        try {
            contenutoNellOrdine.getCentroVendita().setNomecentro(rs.getString("DISPATCHER"));
        }catch (SQLException e) {
        }
            try{
            contenutoNellOrdine.getMagazzino().setNomeMagazzino(rs.getString("NOME_MAGAZZINO"));
        }catch (SQLException e) {
        }

        return contenutoNellOrdine;
    }

    @Override
    public void update(Ordine ordine) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void delete(Ordine ordine) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void delete(ContenutoNellOrdine contenutoNellOrdine) { throw new UnsupportedOperationException("Not implement yet"); }
    @Override
    public void update(ContenutoNellOrdine contenutoNellOrdine) { throw new UnsupportedOperationException("Not implement yet");  }

}