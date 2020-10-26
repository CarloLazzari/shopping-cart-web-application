package model.mo;

public class Magazzino {

    private String nomeMagazzino; //warehouse name
    private String nomeCentroRef; //the centre at which this warehouse belongs to
    public String indirizzo; //address

    /* N:1 */
    private CentroVendita centroVendita;
    /* 1:N */
    private ContenutoNelMagazzino[] contenutiNelMagazzino;

    public CentroVendita getCentroVendita(){return centroVendita;}
    public ContenutoNelMagazzino[] getContenutiNelMagazzino() { return contenutiNelMagazzino;}
    public ContenutoNelMagazzino getContenutiNelMagazzino(int index ) { return contenutiNelMagazzino[index];}
    public String getNomeMagazzino() {  return nomeMagazzino;  }
    public String getNomeCentroRef() { return nomeCentroRef; }
    public String getIndirizzo() { return indirizzo;  }

    public void setNomeMagazzino(String nomeMagazzino) {  this.nomeMagazzino = nomeMagazzino;    }
    public void setNomeCentroRef(String nomeCentroRef) { this.nomeCentroRef = nomeCentroRef;    }
    public void setCentroVendita(CentroVendita centroVendita) { this.centroVendita = centroVendita; }
    public void setContenutiNelMagazzino(ContenutoNelMagazzino[] contenutiNelMagazzino) { this.contenutiNelMagazzino = contenutiNelMagazzino;}
    public void setContenutiNelMagazzino(int index, ContenutoNelMagazzino contenutiNelMagazzino ) { this.contenutiNelMagazzino[index] = contenutiNelMagazzino;}
    public void setIndirizzo(String indirizzof) { this.indirizzo = indirizzo; }

}