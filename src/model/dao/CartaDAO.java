package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Carta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface CartaDAO {

    public void Delete(Carta carta) throws SQLException;
    public Carta findByNumeroCarta(String numeroCarta);
    public Carta findByNumeroCartaAndIntestatario(String numeroCarta, String intestatario);
    public ArrayList<Carta> findByCartaIntestatario(String intestatario) throws SQLException;
    public ArrayList<Carta> findAllCarte();

}