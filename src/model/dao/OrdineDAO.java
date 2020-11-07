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

    public Ordine findByOrderID(int OrderID);
    public ArrayList<Ordine> findAllOrdiniByUsername(String username);
    public ArrayList<Ordine> findAllOrdini();
    public float calculatePrice(int OrderID);
    public void update(Ordine ordine);
    public void delete(Ordine ordine);
    public void updateStatus(Ordine ordine, String stato);
    public int selectMaxOrderID();

}
