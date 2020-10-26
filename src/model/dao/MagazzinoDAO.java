package model.dao;

import model.mo.Magazzino;

import java.util.ArrayList;

public interface MagazzinoDAO {

    public Magazzino findByNomeMagazzino(String nome_magazzino);
    public ArrayList<Magazzino> findAllMagazzini();

}