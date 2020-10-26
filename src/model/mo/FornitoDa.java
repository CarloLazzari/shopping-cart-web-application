package model.mo;

public class FornitoDa {

    String ISBNFumettoReferenziato;
    String centroVenditaReferenziato;

    /* N:1 */
    Fumetto fumetto;
    CentroVendita centroVendita;

    public String getISBNFumettoReferenziato() { return ISBNFumettoReferenziato; }
    public String getCentroVenditaReferenziato() { return centroVenditaReferenziato; }
    public CentroVendita getCentroVendita(){ return this.centroVendita;}
    public Fumetto getFumetto(){ return this.fumetto;}

    public void setFumetto(Fumetto fumetto) { this.fumetto = fumetto; }
    public void setCentroVendita(CentroVendita centroVendita) { this.centroVendita = centroVendita; }
    public void setCentroVenditaReferenziato(String centroVenditaReferenziato) { this.centroVenditaReferenziato = centroVenditaReferenziato; }
    public void setISBNFumettoReferenziato(String ISBNFumettoReferenziato) { this.ISBNFumettoReferenziato = ISBNFumettoReferenziato; }

}
