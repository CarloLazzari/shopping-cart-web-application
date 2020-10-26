package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;
import model.mo.ContenutoNelMagazzino;
import model.mo.FornitoDa;
import model.mo.Fumetto;

import java.util.ArrayList;

public interface FornitoDaDAO {

    public FornitoDa create(Fumetto fumetto, CentroVendita centroVendita) throws DuplicatedObjectException;
    public FornitoDa findByFumettoISBNandCentroVenditaRef(String ISBN, String nomeCentro);
    public FornitoDa update(FornitoDa fornitoDa);
    public FornitoDa delete(FornitoDa fornitoDa);
    public ArrayList<FornitoDa> findAllFornitoDa();
    public ArrayList<FornitoDa> findAllFornitoDaUnblocked();
    public ArrayList<FornitoDa> findInCart(String username);

    public ArrayList<FornitoDa> freeSearch(String searchString);
    public ArrayList<FornitoDa> freeSearchUnblocked(String searchString);
    public ArrayList<FornitoDa> findBy(String searchMode, String searchString);
    public ArrayList<FornitoDa> findByUnblocked(String searchMode, String searchString);

    /**/

}
