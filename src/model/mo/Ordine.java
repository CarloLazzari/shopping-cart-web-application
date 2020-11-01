package model.mo;

import java.sql.Date;
import java.util.GregorianCalendar;

public class Ordine {

    private String indirizzoDestinazione; 
    private String nomeEffettuante; 
    private int orderID;

    private String modalitaPagamento; //payment method
    private String stato; //state
    private java.sql.Date data;

    /* N:1 */
    private User user;
    private CentroVendita centroVendita;
    private Magazzino magazzino;
    private Carta carta;

    /* 1:N */
    private ContenutoNellOrdine[] contenutiNellOrdine;

    /*Getters*/
    public User getUser(){ return this.user;}
    public CentroVendita getCentroVendita(){ return this.centroVendita;}
    public Magazzino getMagazzino(){ return this.magazzino; }
    public Carta getCarta(){ return this.carta;}
    public ContenutoNellOrdine[] getContenutoNellOrdine() { return contenutiNellOrdine; }
    public ContenutoNellOrdine getContenutoNellOrdine(int index){ return contenutiNellOrdine[index];}


    public String getModalitaPagamento(){ return modalitaPagamento;}
    public String getIndirizzoDestinazione(){ return indirizzoDestinazione; }
    public String getNomeEffettuante(){ return nomeEffettuante; }
    public int getOrderID() { return orderID; }
    public String getStato() { return stato; }
    public java.sql.Date getData() {  return data; }

    /*Setters*/
    public void setUser(User user){this.user = user;}
    public void setCentroVendita(CentroVendita centroVendita){ this.centroVendita = centroVendita;}
    public void setMagazzino(Magazzino magazzino){ this.magazzino = magazzino; }
    public void setCarta(Carta carta){ this.carta = carta; }
    public void setContenutoNellOrdine(ContenutoNellOrdine[] contenutiNellOrdine) {  this.contenutiNellOrdine = contenutiNellOrdine;  }
    public void setContenutiNellOrdine(int index, ContenutoNellOrdine contenutiNellOrdine){ this.contenutiNellOrdine[index] = contenutiNellOrdine;}

    public void setModalitaPagamento(String modalitaPagamento){ this.modalitaPagamento = modalitaPagamento;}
    public void setIndirizzoDestinazione(String indirizzoDestinazione) { this.indirizzoDestinazione = indirizzoDestinazione; }
    public void setNomeEffettuante(String nomeEffettuante) { this.nomeEffettuante = nomeEffettuante; }
    public void setStato(String stato) { this.stato = stato; }
    public void setData(java.sql.Date data) {  this.data = data; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

}
