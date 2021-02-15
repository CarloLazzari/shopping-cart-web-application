package model.mo;

public class ContenutoNelMagazzino {

    private String ISBNFumetto; 
    private String nomeMagazzinoRef; 
    private String deleted;
    private int quantita;

    /* 1:1  */
    private Fumetto fumetto;
    /* N:1 */
    private Magazzino magazzino;

    public int getQuantita() {  return quantita; }
    public String getISBNProdotto() { return ISBNFumetto; }
    public String getNomeMagazzinoRef() { return nomeMagazzinoRef; }
    public String getDeleted() { return deleted; }
    public Fumetto getFumetto(){ return fumetto;}
    public Magazzino getMagazzino(){ return magazzino;}

    public void setQuantita(int quantita) {  this.quantita = quantita; }
    public void setISBNProdotto(String ISBNProdotto) { this.ISBNFumetto = ISBNFumetto; }
    public void setNomeMagazzinoRef(String nomeMagazzinoRef) {  nomeMagazzinoRef = nomeMagazzinoRef; }
    public void setDeleted(String deleted){ this.deleted = deleted ;}
    public void setFumetto(Fumetto fumetto){ this.fumetto = fumetto;}
    public void setMagazzino(Magazzino magazzino){ this.magazzino = magazzino;}

}
