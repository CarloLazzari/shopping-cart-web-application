package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface ContenutoNellOrdineDAO {

    public ContenutoNellOrdine create(Ordine ordine, Fumetto fumetto, int quantita, CentroVendita centroVendita, Magazzino magazzino);
    public ContenutoNellOrdine findByOrderIDAndISBN(int Ordine_ID_Ref, String ISBN);
    public ArrayList<ContenutoNellOrdine> findAllContenutoNellOrdine();
    public ArrayList<ContenutoNellOrdine> findByOrderIDCNO(int OrderID);
    public ArrayList<Float> calculatePriceCNO();
    public ArrayList<Float> calculatePriceCNOforWhichOrderID(int OrderID);
    public ArrayList<Float> calculatePriceForWhichUsername(String username);
    public void delete(ContenutoNellOrdine contenutoNellOrdine);
    public void update(ContenutoNellOrdine contenutoNellOrdine);

}
