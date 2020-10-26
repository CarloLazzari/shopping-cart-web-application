package model.mo;

public class CentroVendita {

    private String indirizzo; //address
    private String nomeCentro; //centre Name

    /* 1:N */
    private Magazzino[] magazzini;
    private Fumetto[] fumetti;
    private Ordine[] ordini;

    public String getIndirizzo(){ return indirizzo; }
    public String getNomeCentro(){ return nomeCentro; }
    public Fumetto[] getFumetti(){ return fumetti;}
    public Magazzino[] getMagazzini() { return magazzini;}
    public Ordine[] getOrdini(){ return ordini;}
    public Fumetto getFumetti(int index){ return this.fumetti[index];}
    public Magazzino getMagazzini(int index) { return this.magazzini[index];}
    public Ordine getOrdini(int index){ return this.ordini[index];}


    public void setIndirizzo(String indirizzo){  this.indirizzo = indirizzo; }
    public void setNomecentro(String nome_centro){  this.nomeCentro = nome_centro; }
    public void setFumetti(Fumetto[] fumetti){ this.fumetti = fumetti;}
    public void setMagazzini(Magazzino[] magazzini) {  this.magazzini = magazzini; }
    public void setOrdini(Ordine[] ordini){ this.ordini = ordini;}
    public void setFumetti(int index, Fumetto fumetti){ this.fumetti[index] = fumetti;}
    public void setMagazzini(int index, Magazzino magazzini){ this.magazzini[index] = magazzini;}
    public void setOrdini(int index, Ordine ordini){ this.ordini[index] = ordini;}


}
