package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.*;

import java.sql.Date;
import java.util.ArrayList;

public interface OrdineDAO {

    public Ordine create(User user,
                         String indirizzoDest,
                         int orderID,
                         Carta carta,
                         String stato,
                         Date data) throws DuplicatedObjectException;

    public ArrayList<Ordine> findAllOrdiniByNomeEffettuante(String Nome_Effettuante);
    public ArrayList<Ordine> findAllOrdini();
    public float calculatePrice(int OrderID);
    public Ordine findByOrderIdentificativo(int OrderID);
    public int selectMaxOrderId();
    public int countOrdersByUsername(String username);
    public void update(Ordine ordine);
    public void delete(Ordine ordine);
    public void updateStatus(Ordine ordine, String stato);

}