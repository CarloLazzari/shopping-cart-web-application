package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;
import model.mo.Magazzino;

import java.util.ArrayList;

public interface MagazzinoDAO {

    public Magazzino create(CentroVendita centroVendita, String nomeMagazzino, String indirizzo) throws DuplicatedObjectException;
    public void update(Magazzino magazzino) throws DuplicatedObjectException;
    public void delete(Magazzino magazzino);
    public Magazzino findByNomeMagazzino(String nome_magazzino);
    public ArrayList<Magazzino> findAllMagazzini();

}
