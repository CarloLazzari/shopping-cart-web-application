package model.dao;

import model.mo.CentroVendita;

import java.util.ArrayList;

public interface CentroVenditaDAO {

    public CentroVendita findByNomeCentro(String nome_centro);
    public ArrayList<CentroVendita> findAllCentri();
    public CentroVendita create(String nome_centro, String indirizzo);
    public void delete(CentroVendita centroVendita);
    public void update(CentroVendita centroVendita);

}