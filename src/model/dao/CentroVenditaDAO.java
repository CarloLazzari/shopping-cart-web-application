package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.CentroVendita;

import java.util.ArrayList;

public interface CentroVenditaDAO {

    public CentroVendita create(String nomeCentro, String indirizzo) throws DuplicatedObjectException;
    public CentroVendita findByNomeCentro(String nomeCentro);
    public void update(CentroVendita centroVendita) throws DuplicatedObjectException;
    public void delete(CentroVendita centroVendita);
    public ArrayList<CentroVendita> findAllCentri();
    
}
