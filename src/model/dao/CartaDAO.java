package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Carta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface CartaDAO {

    public void delete(Carta carta) throws SQLException;
    public Carta findByCardNumber(String numeroCarta);
    public Carta findByCardNumberAndOwner(String numeroCarta, String intestatario);
    public ArrayList<Carta> findByCardOwner(String intestatario) throws SQLException;
    public ArrayList<Carta> findAllCarte();

}
