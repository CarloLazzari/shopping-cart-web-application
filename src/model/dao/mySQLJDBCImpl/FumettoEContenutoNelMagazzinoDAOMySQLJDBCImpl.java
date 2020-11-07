
package model.dao.mySQLJDBCImpl;

import model.dao.ContenutoNelMagazzinoDAO;
import model.dao.FumettoDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.ContenutoNelMagazzino;
import model.mo.Fumetto;
import model.mo.Magazzino;

import java.sql.*;
import java.util.ArrayList;

public class FumettoEContenutoNelMagazzinoDAOMySQLJDBCImpl implements ContenutoNelMagazzinoDAO, FumettoDAO {

    Connection conn;

    public FumettoEContenutoNelMagazzinoDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    /* Unico metodo create per fumetto e fumetto_contenuto_nel_magazzino*/
    @Override
    public ContenutoNelMagazzino create(Fumetto fumetto, Magazzino magazzino, int quantita, String deleted) throws DuplicatedObjectException {


        PreparedStatement ps;

        ContenutoNelMagazzino contenutoNelMagazzino = new ContenutoNelMagazzino();
        contenutoNelMagazzino.setFumetto(fumetto);
        contenutoNelMagazzino.setMagazzino(magazzino);
        contenutoNelMagazzino.setQuantita(quantita);
        contenutoNelMagazzino.setDeleted(deleted);


        /* ----------------------------------------------------------- */

        try {

            String sql
                    = " SELECT ISBN_FUMETTO, NOME_MAGAZZINO_REF"
                    + " FROM contenuto_nel_magazzino "
                    + " WHERE "
                    + " ISBN_FUMETTO = ? AND"
                    + " NOME_MAGAZZINO_REF = ?";


            ps = conn.prepareStatement(sql);
            int i = 1;

            ps.setString(i++, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(i, contenutoNelMagazzino.getMagazzino().getNomeMagazzino());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();

            if (exists) {
                throw new DuplicatedObjectException("ContenutoNelMagazzinoDAOMySQLJDCBImpl: Tentativo di inserimento di un prodotto nel magazzino già esistente");
            }

            sql
                    = " INSERT INTO contenuto_nel_magazzino "
                    + " (ISBN_FUMETTO, NOME_MAGAZZINO_REF, QUANTITA, DELETED) "
                    + " VALUES (?,?,?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(i++, contenutoNelMagazzino.getMagazzino().getNomeMagazzino());
            ps.setInt(i++, contenutoNelMagazzino.getQuantita());
            ps.setString(i, contenutoNelMagazzino.getDeleted());

            ps.executeUpdate();
            ps.close();
            /* Quando eseguo una query di select, uso executeQuery, che ritorna un resultSet; */
            /* Quando invece eseguo una query insert, update, o delete, uso executeUpdate. */

            } catch (SQLException e) {
                throw new RuntimeException();
            }

        return contenutoNelMagazzino;
    }

    @Override
    public void updateWarehouse(ContenutoNelMagazzino contenutoNelMagazzino) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT ISBN_FUMETTO, NOME_MAGAZZINO_REF"
                    + " FROM contenuto_nel_magazzino "
                    + " WHERE "
                    + " ISBN_FUMETTO <> ? AND"
                    + " NOME_MAGAZZINO_REF <> ? AND"
                    + " QUANTITA = ? AND"
                    + " DELETED = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;

            ps.setString(i++, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(i++, contenutoNelMagazzino.getMagazzino().getNomeMagazzino());
            ps.setInt(i++, contenutoNelMagazzino.getQuantita());
            ps.setString(i, contenutoNelMagazzino.getDeleted());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();

            if (exists) {
                throw new DuplicatedObjectException("ContenutoNelMagazzinoDAOMySQLJDCBImpl: Tentativo di inserimento di un prodotto nel magazzino già esistente");
            }

            sql
                = " UPDATE contenuto_nel_magazzino"
                + " SET "
                + " QUANTITA = ?,"
                + " DELETED = ?"
                + " WHERE "
                + " ISBN_FUMETTO = ? AND NOME_MAGAZZINO_REF = ?";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setInt(i++, contenutoNelMagazzino.getQuantita());
            ps.setString(i++, contenutoNelMagazzino.getDeleted());
            ps.setString(i++, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(i, contenutoNelMagazzino.getMagazzino().getNomeMagazzino());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void addQuantityToWarehouse(ContenutoNelMagazzino contenutoNelMagazzino) {

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE contenuto_nel_magazzino "
                    + " SET QUANTITA = QUANTITA + 1 "
                    + " WHERE ISBN_FUMETTO = ? AND NOME_MAGAZZINO_REF = ?";

            int i = 1;
            ps = conn.prepareStatement(sql);
            ps.setString(i++, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(i, contenutoNelMagazzino.getMagazzino().getNomeMagazzino());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeQuantityFromWarehouse(ContenutoNelMagazzino contenutoNelMagazzino) {

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE contenuto_nel_magazzino"
                    + " SET QUANTITA=QUANTITA - 1 "
                    + " WHERE ISBN_FUMETTO = ? AND NOME_MAGAZZINO_REF = ?";

            int i = 1;
            ps = conn.prepareStatement(sql);
            ps.setString(i++, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(i, contenutoNelMagazzino.getMagazzino().getNomeMagazzino());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getAvailability(String ISBNProdotto, String Magazzino) {

        int quantity = 0;
        PreparedStatement ps;

        try {
            String sql
                    = " SELECT QUANTITA "
                    + " FROM contenuto_nel_magazzino "
                    + " WHERE "
                    + " ISBN_FUMETTO = ? AND NOME_MAGAZZINO_REF = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,ISBNProdotto);
            ps.setString(i,Magazzino);

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
    public ArrayList<ContenutoNelMagazzino> findInCartCNM(String username) {

        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = new ArrayList<>();

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT ISBN_FUMETTO, NOME_MAGAZZINO_REF "
                    + " FROM contenuto_nel_magazzino "
                    + " JOIN ha_nel_carrello ON "
                    + " ISBN_FUMETTO = ISBN_FUM "
                    + " WHERE USERNAME_UTENTE = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1,username);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                contenutoNelMagazzino=read(resultSet);
                contenutoNelMagazzinoArrayList.add(contenutoNelMagazzino);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return contenutoNelMagazzinoArrayList;
    }


    @Override
    public void deleteFromWarehouse(ContenutoNelMagazzino contenutoNelMagazzino) {

        PreparedStatement ps;

        try{

            String sql
                    = " UPDATE contenuto_nel_magazzino "
                    + " SET DELETED = 'Y' "
                    + " WHERE "
                    + " ISBN_FUMETTO = ? AND"
                    + " NOME_MAGAZZINO_REF = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(i, contenutoNelMagazzino.getMagazzino().getNomeMagazzino());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public void undeleteFromWarehouse(ContenutoNelMagazzino contenutoNelMagazzino) {

        PreparedStatement ps;

        try{

            String sql
                    = " UPDATE contenuto_nel_magazzino "
                    + " SET DELETED = 'N' "
                    + " WHERE "
                    + " ISBN_FUMETTO = ? AND"
                    + " NOME_MAGAZZINO_REF = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, contenutoNelMagazzino.getFumetto().getISBN());
            ps.setString(2,contenutoNelMagazzino.getMagazzino().getNomeMagazzino());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public ContenutoNelMagazzino findByFumettoISBNRefAndMagazzinoRef(String ISBNProdotto, String Magazzino) {

        PreparedStatement ps;
        ContenutoNelMagazzino contenutoNelMagazzino = null;

        try {

            String sql
                    = " SELECT *"
                    + " FROM contenuto_nel_magazzino "
                    + " WHERE ISBN_FUMETTO = ? AND"
                    + " NOME_MAGAZZINO_REF = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, ISBNProdotto);
            ps.setString(2, Magazzino);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                contenutoNelMagazzino = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contenutoNelMagazzino;

    }



    @Override
    public ArrayList<ContenutoNelMagazzino> findAllContenutoNelMagazzino() {

        PreparedStatement ps;
        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contents = new ArrayList<>();

        try{

            String sql
                    = " SELECT * "
                    + " FROM contenuto_nel_magazzino "
                    + " WHERE DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                contenutoNelMagazzino=read(resultSet);
                contents.add(contenutoNelMagazzino);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contents;

    }

    @Override
    public ArrayList<ContenutoNelMagazzino> findAllContenutoNelMagazzinoUnblocked() {

        PreparedStatement ps;
        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contents = new ArrayList<>();

        try{

            String sql
                    = " SELECT * "
                    + " FROM contenuto_nel_magazzino "
                    + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                    + " WHERE BLOCCATO = 'N' AND DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                contenutoNelMagazzino=read(resultSet);
                contents.add(contenutoNelMagazzino);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contents;

    }

    @Override
    public ArrayList<ContenutoNelMagazzino> freeSearchCNM(String searchString) {

        PreparedStatement ps;
        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contents = new ArrayList<>();

        try{

            String sql
                    = " SELECT * "
                    + " FROM contenuto_nel_magazzino "
                    + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                    + " WHERE TITOLO = ? OR AUTORE = ? OR NUMERO = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);
            ps.setString(i,searchString);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                contenutoNelMagazzino=read(resultSet);
                contents.add(contenutoNelMagazzino);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contents;

    }

    @Override
    public ArrayList<ContenutoNelMagazzino> freeSearchCNMUnblocked(String searchString) {

        PreparedStatement ps;
        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contents = new ArrayList<>();

        try{

            String sql
                    = " SELECT * "
                    + " FROM contenuto_nel_magazzino "
                    + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                    + " WHERE TITOLO = ? OR AUTORE = ? OR NUMERO = ?"
                    + " AND BLOCCATO = 'N' AND DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);
            ps.setString(i,searchString);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                contenutoNelMagazzino=read(resultSet);
                contents.add(contenutoNelMagazzino);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contents;
    }



    @Override
    public ArrayList<ContenutoNelMagazzino> findByCNM(String searchMode, String searchString) {

        PreparedStatement ps;
        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contents = new ArrayList<>();
        ResultSet resultSet;
        String sql;

        try{

            switch (searchMode) {

                case "TITOLO":
                    sql
                        = " SELECT * "
                        + " FROM contenuto_nel_magazzino "
                        + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                        + " WHERE TITOLO = ? AND DELETED = 'N'";

                    ps = conn.prepareStatement(sql);
                    int i = 1;
                    ps.setString(i, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        contenutoNelMagazzino = read(resultSet);
                        contents.add(contenutoNelMagazzino);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "AUTORE":
                    sql
                        = " SELECT * "
                        + " FROM contenuto_nel_magazzino "
                        + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                        + " WHERE AUTORE = ? AND DELETED = 'N'";

                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        contenutoNelMagazzino = read(resultSet);
                        contents.add(contenutoNelMagazzino);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "NUMERO":
                    sql
                        = " SELECT * "
                        + " FROM contenuto_nel_magazzino "
                        + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                        + " WHERE NUMERO = ? AND DELETED = 'N'";

                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        contenutoNelMagazzino = read(resultSet);
                        contents.add(contenutoNelMagazzino);
                    }
                    resultSet.close();
                    ps.close();
                    break;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contents;

    }

    @Override
    public ArrayList<ContenutoNelMagazzino> findByCNMUnblocked(String searchMode, String searchString) {

        PreparedStatement ps;
        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contents = new ArrayList<>();
        String sql;
        ResultSet resultSet;

        try{

            switch (searchMode) {

                case "TITOLO":
                    sql
                            = " SELECT * "
                            + " FROM contenuto_nel_magazzino "
                            + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                            + " WHERE TITOLO = ? AND DELETED = 'N' AND BLOCCATO='N'";

                    ps = conn.prepareStatement(sql);
                    int i = 1;
                    ps.setString(i, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        contenutoNelMagazzino = read(resultSet);
                        contents.add(contenutoNelMagazzino);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "AUTORE":
                    sql
                            = " SELECT * "
                            + " FROM contenuto_nel_magazzino "
                            + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                            + " WHERE AUTORE = ? AND DELETED = 'N' AND BLOCCATO='N'";

                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        contenutoNelMagazzino = read(resultSet);
                        contents.add(contenutoNelMagazzino);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "NUMERO":
                    sql
                            = " SELECT * "
                            + " FROM contenuto_nel_magazzino "
                            + " JOIN fumetto f on f.ISBN = contenuto_nel_magazzino.ISBN_FUMETTO"
                            + " WHERE NUMERO = ? AND DELETED = 'N' AND BLOCCATO='N'";

                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        contenutoNelMagazzino = read(resultSet);
                        contents.add(contenutoNelMagazzino);
                    }
                    resultSet.close();
                    ps.close();
                    break;

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contents;
    }

    public ArrayList<ContenutoNelMagazzino> findRandomContenutoNelMagazzino(ArrayList<Fumetto> fumettoArrayList){

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ContenutoNelMagazzino contenutoNelMagazzino;
        ArrayList<ContenutoNelMagazzino> contents = new ArrayList<>();

        try{

            for(int j=0; j<fumettoArrayList.size(); j++) {
                String sql
                        = " SELECT * "
                        + " FROM contenuto_nel_magazzino WHERE DELETED = 'N'"
                        + " AND ISBN_FUMETTO = ?";

                ps = conn.prepareStatement(sql);
                int i = 1;
                ps.setString(i,fumettoArrayList.get(j).getISBN());
                resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    contenutoNelMagazzino = read(resultSet);
                    contents.add(contenutoNelMagazzino);
                }
            }

            assert resultSet != null;
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contents;

    }

    /* --------------------------------------------------------------------------------------------------------------*/

    @Override
    public Fumetto create(String ISBN, String titolo, String autore, int numero, String formato, String rilegatura, Float prezzo, Float peso, String bloccato) throws DuplicatedObjectException {

        PreparedStatement ps;

        Fumetto fumetto = new Fumetto();
        fumetto.setISBN(ISBN);
        fumetto.setTitolo(titolo);
        fumetto.setAutore(autore);
        fumetto.setNumero(numero);
        fumetto.setFormato(formato);
        fumetto.setRilegatura(rilegatura);
        fumetto.setPrezzo(prezzo);
        fumetto.setPeso(peso);
        fumetto.setBlocked(bloccato);

        try {

            String sql
                    = " SELECT ISBN "
                    + " FROM fumetto "
                    + " WHERE "
                    + " ISBN = ?";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i, fumetto.getISBN());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();
            ps.close();

            if (exists) {
                throw new DuplicatedObjectException("FumettoDAOMySQLJDCBImpl: Tentativo di inserimento di un fumetto già esistente");
            }

            sql
                    = " INSERT INTO fumetto "
                    + " (ISBN, "
                    + " TITOLO, "
                    + " AUTORE, "
                    + " NUMERO, "
                    + " FORMATO,"
                    + " RILEGATURA, "
                    + " PREZZO, "
                    + " PESO, "
                    + " BLOCCATO)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, fumetto.getISBN());
            ps.setString(i++, fumetto.getTitolo());
            ps.setString(i++, fumetto.getAutore());
            ps.setInt(i++, fumetto.getNumero());
            ps.setString(i++, fumetto.getFormato());
            ps.setString(i++, fumetto.getRilegatura());
            ps.setFloat(i++, fumetto.getPrezzo());
            ps.setFloat(i++, fumetto.getPeso());
            ps.setString(i, fumetto.getBlocked());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetto;

    }

    @Override
    public void update(Fumetto fumetto) throws DuplicatedObjectException {

        PreparedStatement ps;

        try{

            String sql
                    = " SELECT ISBN "
                    + " FROM fumetto "
                    + " WHERE "
                    + " ISBN <> ? AND"
                    + " TITOLO = ? AND"
                    + " AUTORE = ? AND"
                    + " NUMERO = ? AND"
                    + " FORMATO = ? AND"
                    + " RILEGATURA = ? AND"
                    + " PREZZO = ? AND"
                    + " PESO = ? AND"
                    + " BLOCCATO = ?";

            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++, fumetto.getISBN());
            ps.setString(i++, fumetto.getTitolo());
            ps.setString(i++, fumetto.getAutore());
            ps.setInt(i++, fumetto.getNumero());
            ps.setString(i++, fumetto.getFormato());
            ps.setString(i++, fumetto.getRilegatura());
            ps.setFloat(i++, fumetto.getPrezzo());
            ps.setFloat(i++, fumetto.getPeso());
            ps.setString(i, fumetto.getBlocked());

            ResultSet resultSet = ps.executeQuery();

            boolean exists;
            exists = resultSet.next();
            resultSet.close();
            ps.close();

            if(exists){
                throw new DuplicatedObjectException("FumettoDAOMySQLJDCBImpl: Tentativo di inserimento di un fumetto già esistente");
            }

            sql
                    = " UPDATE fumetto "
                    + " SET "
                    + " TITOLO = ?,"
                    + " AUTORE = ?,"
                    + " NUMERO = ?,"
                    + " FORMATO = ?,"
                    + " RILEGATURA = ?, "
                    + " PREZZO = ?,"
                    + " PESO = ?,"
                    + " BLOCCATO = ?"
                    + " WHERE "
                    + " ISBN = ?";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, fumetto.getTitolo());
            ps.setString(i++, fumetto.getAutore());
            ps.setInt(i++, fumetto.getNumero());
            ps.setString(i++, fumetto.getFormato());
            ps.setString(i++, fumetto.getRilegatura());
            ps.setFloat(i++, fumetto.getPrezzo());
            ps.setFloat(i++, fumetto.getPeso());
            ps.setString(i++, fumetto.getBlocked());
            ps.setString(i, fumetto.getISBN());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void block(Fumetto fumetto) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE fumetto "
                    + " SET BLOCCATO ='Y' "
                    + " WHERE "
                    + " ISBN = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, fumetto.getISBN());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void unblock(Fumetto fumetto) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE fumetto "
                    + " SET BLOCCATO ='N' "
                    + " WHERE "
                    + " ISBN = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, fumetto.getISBN());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Fumetto showFumetto(String ISBN) {

        PreparedStatement ps;
        Fumetto fumetto = null;

        try {
            String sql
                    = " SELECT ISBN, TITOLO, AUTORE, FORMATO, NUMERO, RILEGATURA, PREZZO, PESO, BLOCCATO, CENTRO_VENDITA_REFERENZIATO, NOME_MAGAZZINO_REF, QUANTITA, DELETED "
                    + " FROM fumetto JOIN contenuto_nel_magazzino ON ISBN = ISBN_FUMETTO "
                    + " JOIN fornito_da ON ISBN = ISBN_FUMETTO_REFERENZIATO"
                    + " WHERE "
                    + " ISBN_FUMETTO = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, ISBN);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                fumetto = read_f(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetto;

    }

    public Fumetto findByFumettoISBNNotYetCreated(String ISBN){

        PreparedStatement ps;
        Fumetto fumetto = null;

        try {
            String sql
                    = " SELECT * "
                    + " FROM fumetto"
                    + " WHERE "
                    + " ISBN = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, ISBN);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                fumetto = read_f(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  fumetto;

    }

    public Fumetto findByFumettoISBN(String ISBN) {

        PreparedStatement ps;
        Fumetto fumetto = null;

        try {
            String sql
                    = " SELECT * "
                    + " FROM fumetto JOIN contenuto_nel_magazzino "
                    + " ON ISBN = ISBN_FUMETTO "
                    + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                    + " WHERE "
                    + " ISBN_FUMETTO = ? AND DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, ISBN);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                fumetto = read_f(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetto;

    }


    @Override
    public ArrayList<Fumetto> findBy(String searchMode, String searchString) {

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();
        String sql;
        ResultSet resultSet;

        try {

            switch(searchMode) {

                case "TITOLO":

                    sql
                        = " SELECT *"
                        + " FROM fumetto JOIN contenuto_nel_magazzino "
                        + " ON ISBN = ISBN_FUMETTO "
                        + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                        + " WHERE "
                        + " TITOLO = ? AND DELETED = 'N'";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        fumetto = read_f(resultSet);
                        fumetti.add(fumetto);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "AUTORE":

                    sql
                        = " SELECT *"
                        + " FROM fumetto JOIN contenuto_nel_magazzino "
                        + " ON ISBN = ISBN_FUMETTO "
                        + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                        + " WHERE "
                        + " AUTORE = ? AND DELETED = 'N'";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        fumetto = read_f(resultSet);
                        fumetti.add(fumetto);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "NUMERO":

                    sql
                        = " SELECT *"
                        + " FROM fumetto JOIN contenuto_nel_magazzino "
                        + " ON ISBN = ISBN_FUMETTO "
                        + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                        + " WHERE "
                        + " NUMERO = ? AND DELETED = 'N'";


                    ps = conn.prepareStatement(sql);
                    ps.setString(1, searchString);

                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        fumetto = read_f(resultSet);
                        fumetti.add(fumetto);
                    }
                    resultSet.close();
                    ps.close();
                    break;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;

    }

    @Override
    public ArrayList<Fumetto> findByUnblocked(String searchMode, String searchString) {

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();
        String sql;
        ResultSet resultSet;

        try {

            switch(searchMode) {
                case "TITOLO":

                    sql
                        = " SELECT *"
                        + " FROM fumetto JOIN contenuto_nel_magazzino "
                        + " ON ISBN = ISBN_FUMETTO "
                        + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                        + " WHERE "
                        + " TITOLO = ? AND DELETED = 'N' AND BLOCCATO ='N'";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        fumetto = read_f(resultSet);
                        fumetti.add(fumetto);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "AUTORE":

                    sql
                        = " SELECT *"
                        + " FROM fumetto JOIN contenuto_nel_magazzino "
                        + " ON ISBN = ISBN_FUMETTO "
                        + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                        + " WHERE "
                        + " AUTORE = ? AND DELETED = 'N' AND BLOCCATO ='N'";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, searchString);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        fumetto = read_f(resultSet);
                        fumetti.add(fumetto);
                    }
                    resultSet.close();
                    ps.close();
                    break;

                case "NUMERO":

                    sql
                        = " SELECT *"
                        + " FROM fumetto JOIN contenuto_nel_magazzino "
                        + " ON ISBN = ISBN_FUMETTO "
                        + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                        + " WHERE "
                        + " NUMERO = ? AND DELETED = 'N' AND BLOCCATO ='N'";


                    ps = conn.prepareStatement(sql);
                    ps.setString(1, searchString);

                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        fumetto = read_f(resultSet);
                        fumetti.add(fumetto);
                    }
                    resultSet.close();
                    ps.close();
                    break;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;

    }

    @Override
    public ArrayList<Fumetto> findAllFumetti() {

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();

        try{

            String sql
                    = " SELECT *"
                    + " FROM fumetto JOIN contenuto_nel_magazzino "
                    + " ON ISBN = ISBN_FUMETTO "
                    + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                    + " WHERE DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fumetto = read_f(resultSet);
                fumetti.add(fumetto);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;

    }

    @Override
    public ArrayList<Fumetto> findAllUnblockedFumetti() {

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();

        try{

            String sql
                    = " SELECT *"
                    + " FROM fumetto JOIN contenuto_nel_magazzino "
                    + " ON ISBN = ISBN_FUMETTO "
                    + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                    + " WHERE BLOCCATO = 'N' AND DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fumetto = read_f(resultSet);
                fumetti.add(fumetto);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;

    }


    @Override
    public ArrayList<Fumetto> freeSearch(String searchString) {

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();

        try {

            String sql
                = " SELECT ISBN, TITOLO, AUTORE, NUMERO, FORMATO, RILEGATURA, PREZZO, PESO"
                + " FROM fumetto JOIN contenuto_nel_magazzino "
                + " ON ISBN = ISBN_FUMETTO "
                + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                + " WHERE TITOLO = ? OR AUTORE = ? OR NUMERO = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);
            ps.setString(i,searchString);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fumetto = read_f(resultSet);
                fumetti.add(fumetto);
            }
            resultSet.close();
            ps.close();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;

    }

    @Override
    public ArrayList<Fumetto> freeSearchUnblocked(String searchString) {

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();

        try {

            String sql
                    = " SELECT ISBN, TITOLO, AUTORE, NUMERO, FORMATO, RILEGATURA, PREZZO, PESO"
                    + " FROM fumetto JOIN contenuto_nel_magazzino "
                    + " ON ISBN = ISBN_FUMETTO "
                    + " JOIN fornito_da fd on fumetto.ISBN = fd.ISBN_FUMETTO_REFERENZIATO"
                    + " WHERE TITOLO = ? OR AUTORE = ? OR NUMERO = ?"
                    + " AND BLOCCATO = 'N' AND DELETED = 'N'";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,searchString);
            ps.setString(i++,searchString);
            ps.setString(i,searchString);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fumetto = read_f(resultSet);
                fumetti.add(fumetto);
            }
            resultSet.close();
            ps.close();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;


    }

    @Override
    public ArrayList<Fumetto> findInCart(String username){

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();

        try {

            String sql
                    = " SELECT ISBN, TITOLO, AUTORE, NUMERO, FORMATO, RILEGATURA, PREZZO, PESO"
                    + " FROM fumetto JOIN ha_nel_carrello hnc on fumetto.ISBN = hnc.ISBN_FUM "
                    + " WHERE USERNAME_UTENTE = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i,username);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fumetto = read_f(resultSet);
                fumetti.add(fumetto);
            }
            resultSet.close();
            ps.close();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;
    }

    public ArrayList<Fumetto> findRandomFumetti(){

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();

        try {
            String sql
                    = " SELECT *"
                    + " FROM fumetto"
                    + " JOIN contenuto_nel_magazzino ON ISBN = ISBN_FUMETTO"
                    + " WHERE DELETED = 'N'"
                    + " ORDER BY RAND()"
                    + " LIMIT 12";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fumetto = read_f(resultSet);
                fumetti.add(fumetto);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;

    }

    public ArrayList<Fumetto> findRandomFumettiUnblocked(){

        PreparedStatement ps;
        Fumetto fumetto;
        ArrayList<Fumetto> fumetti = new ArrayList<>();

        try {
            String sql
                    = " SELECT *"
                    + " FROM fumetto"
                    + " JOIN contenuto_nel_magazzino ON ISBN = ISBN_FUMETTO"
                    + " WHERE DELETED = 'N' AND BLOCCATO = 'N'"
                    + " ORDER BY RAND()"
                    + " LIMIT 12";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                fumetto = read_f(resultSet);
                fumetti.add(fumetto);
            }
            resultSet.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fumetti;

    }


    ContenutoNelMagazzino read(ResultSet rs){

        ContenutoNelMagazzino contenutoNelMagazzino = new ContenutoNelMagazzino();
        Fumetto fumetto = new Fumetto();
        Magazzino magazzino = new Magazzino();

        contenutoNelMagazzino.setFumetto(fumetto);
        contenutoNelMagazzino.setMagazzino(magazzino);

        try{
            contenutoNelMagazzino.getFumetto().setISBN(rs.getString("ISBN_FUMETTO"));
        } catch (SQLException ignored) {
        }
        try{
            contenutoNelMagazzino.getMagazzino().setNomeMagazzino(rs.getString("NOME_MAGAZZINO_REF"));
        } catch (SQLException ignored) {
        }
        try{
            contenutoNelMagazzino.setQuantita(rs.getInt("QUANTITA"));
        } catch (SQLException ignored) {
        }
        try {
            contenutoNelMagazzino.setDeleted(rs.getString("DELETED"));
        }
        catch (SQLException ignored) {
        }

        return contenutoNelMagazzino;

    }

    Fumetto read_f(ResultSet rs){

        Fumetto fumetto = new Fumetto();

        try{
            fumetto.setISBN(rs.getString("ISBN"));
        } catch (SQLException ignored) {
        }
        try{
            fumetto.setTitolo(rs.getString("TITOLO"));
        } catch (SQLException ignored) {
        }
        try{
            fumetto.setAutore(rs.getString("AUTORE"));
        } catch (SQLException ignored) {
        }
        try{
            fumetto.setNumero(rs.getInt("NUMERO"));
        } catch (SQLException ignored) {
        }
        try{
            fumetto.setFormato(rs.getString("FORMATO"));
        } catch (SQLException ignored) {
        }
        try{
            fumetto.setRilegatura(rs.getString("RILEGATURA"));
        } catch (SQLException ignored) {
        }
        try{
            fumetto.setPrezzo(rs.getFloat("PREZZO"));
        }  catch (SQLException ignored) {
        }
        try{
            fumetto.setPeso(rs.getFloat("PESO"));
        }  catch (SQLException ignored) {
        }
        try{
            fumetto.setBlocked(rs.getString("BLOCCATO"));
        } catch (SQLException ignored) {
        }

        return fumetto;
    }

    @Override
    public void delete(Fumetto fumetto) {
        throw new UnsupportedOperationException("Not implement yet");
    }

}
