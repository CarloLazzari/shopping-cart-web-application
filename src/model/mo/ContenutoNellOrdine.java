package model.mo;

public class ContenutoNellOrdine {

    private String ISBNFumettoRef; //ISBN of the comic
    private int IDOrdineRef; //Order ID
    private int quantita; //quantity
    private String dispatcher;
    private String magazzinoRef; //warehouse that will ship the order

    /* N:1 */
    private Ordine ordine;
    private Fumetto fumetto;
    private CentroVendita centroVendita;
    private Magazzino magazzino;

    /* Getters */
    public String getDispatcher() { return dispatcher; }
    public String getMagazzinoRef() { return magazzinoRef; }
    public String getISBNFumettoRef() { return ISBNFumettoRef; }
    public int getIDOrdineRef() {  return IDOrdineRef; }
    public int getQuantita() { return quantita; }
    public Ordine getOrdine() { return ordine; }
    public Fumetto getFumetto() { return fumetto;  }
    public CentroVendita getCentroVendita() {  return centroVendita; }
    public Magazzino getMagazzino() { return magazzino; }

    /* Setters */
    public void setIDOrdineRef(int IDOrdineRef) {  this.IDOrdineRef = IDOrdineRef;  }
    public void setISBNFumettoRef(String ISBNFumettoRef) { this.ISBNFumettoRef = ISBNFumettoRef; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public void setOrdine(Ordine ordine){ this.ordine = ordine;}
    public void setFumetto(Fumetto fumetto){ this.fumetto = fumetto;}
    public void setCentroVendita(CentroVendita centroVendita) {  this.centroVendita = centroVendita;  }
    public void setMagazzino(Magazzino magazzino) { this.magazzino = magazzino; }

    public void setDispatcher(String dispatcher) { this.dispatcher = dispatcher; }
    public void setMagazzinoRef(String magazzinoRef) { this.magazzinoRef = magazzinoRef; }
    //public Fumetto getFumetti(int index){ return this.fumetti[index];}
    //public void setFumetti(Fumetto[] fumetti){ this.fumetti = fumetti;}

}