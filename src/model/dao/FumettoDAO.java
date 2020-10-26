package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;
import model.mo.Fumetto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface FumettoDAO {

    public void update(Fumetto fumetto) throws DuplicatedObjectException;
    public void block(Fumetto fumetto) throws SQLException;
    public void unblock(Fumetto fumetto) throws SQLException;
    public void delete(Fumetto fumetto);
    public Fumetto create(String ISBN, String titolo, String autore, int numero, String formato, String rilegatura, Float prezzo, Float peso, String bloccato) throws DuplicatedObjectException;
    public Fumetto showFumetto(String ISBN);
    public Fumetto findByFumettoISBN(String ISBN);
    public ArrayList<Fumetto> findBy(String modalitaRicerca, String searchString);
    public ArrayList<Fumetto> findByUnblocked(String modalitaRicerca, String searchString);
    public ArrayList<Fumetto> findAllFumetti();
    public ArrayList<Fumetto> findAllUnblockedFumetti();
    public ArrayList<Fumetto> freeSearch(String searchString);
    public ArrayList<Fumetto> freeSearchUnblocked(String searchString);
    public ArrayList<Fumetto> findInCart(String username);

}