package model.mo;

public class ContenutoNelMagazzino {

    private String ISBNFumetto; //ISBN of the comic
    private String nomeMagazzinoRef; //ISBN of the warehouse it is contained into
    private String deleted;
    private int quantita; //quantity


    /* 1:1  */
    private Fumetto fumetto;
    //private Fumetto[] fumetti;
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
    //public Fumetto[] getFumetti() { return fumetti;  }
    //public Fumetto getFumetti(int index){ return this.fumetti[index];}
    //public void setFumetti(Fumetto[] fumetti){ this.fumetti = fumetti;}
    //public void setFumetti(int index, Fumetto fumetto){ this.fumetti[index] = fumetto;}

}