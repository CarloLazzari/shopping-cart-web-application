package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.ContenutoNelMagazzino;
import model.mo.Fumetto;
import model.mo.Magazzino;

import java.util.ArrayList;

public interface ContenutoNelMagazzinoDAO {

    /* Creo il fumetto nel sito e nello stesso momento lo inserisco nel magazzino */
    /* Ovvero inserendolo in ContenutoNelMagazzino */
    /* Dopo posso modificare gli attributi del fumetto nel sito, bloccarlo, e gestire la quantità */
    public ContenutoNelMagazzino create(Fumetto fumetto, Magazzino magazzino, int quantita, String deleted) throws DuplicatedObjectException;
    public ContenutoNelMagazzino findByFumettoISBNRefAndMagazzinoRef(String ISBNProdotto, String Magazzino);
    public ArrayList<ContenutoNelMagazzino> findAllContenutoNelMagazzino();
    public ArrayList<ContenutoNelMagazzino> findAllContenutoNelMagazzinoUnblocked();
    public void updateWarehouse(ContenutoNelMagazzino contenutoNelMagazzino) throws DuplicatedObjectException;
    public void deleteFromWarehouse(ContenutoNelMagazzino contenutoNelMagazzino);
    public void undeleteFromWarehouse(ContenutoNelMagazzino contenutoNelMagazzino);
    public void addQuantityToWarehouse(ContenutoNelMagazzino contenutoNelMagazzino);
    public void removeQuantityFromWarehouse(ContenutoNelMagazzino contenutoNelMagazzino);
    public int getAvailability(String ISBNProdotto, String Magazzino);

    public ArrayList<ContenutoNelMagazzino> findInCartCNM(String username);
    public ArrayList<ContenutoNelMagazzino> freeSearchCNM(String searchString);
    public ArrayList<ContenutoNelMagazzino> freeSearchCNMUnblocked(String searchString);
    public ArrayList<ContenutoNelMagazzino> findByCNM(String modalitaRicerca, String searchString);
    public ArrayList<ContenutoNelMagazzino> findByCNMUnblocked(String modalitaRicerca, String searchString);


}