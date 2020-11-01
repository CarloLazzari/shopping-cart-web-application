package model.mo;

public class Fumetto {

    private int numero;
    private float prezzo;
    private float peso; 
    private String formato; 
    private String autore;
    private String ISBN;
    private String titolo; 
    private String rilegatura; 
    private String blocked;

    /* 1:N */
    private Carrello[] carrello;
    private ContenutoNelMagazzino[] contenutoNelMagazzino;
    private ContenutoNellOrdine[] contenutoNellOrdine;
    private FornitoDa[] fornitoDa;

    /* Getters */
    public int getNumero() { return numero;  }
    public float getPrezzo() { return prezzo; }
    public String getISBN() { return ISBN;  }
    public String getFormato() { return formato;  }
    public float getPeso() {  return peso; }
    public String getAutore() { return autore;  }
    public String getTitolo() {  return titolo;}
    public String getRilegatura() { return rilegatura;}
    public String getBlocked(){ return blocked;};
  
    public Carrello[] getCarrello() { return carrello;}
    public ContenutoNelMagazzino[] getContenutoNelMagazzino(){ return this.contenutoNelMagazzino;}
    public ContenutoNellOrdine[] getContenutoNellOrdine() { return this.contenutoNellOrdine; }
    public FornitoDa[] getFornitoDa(){ return this.fornitoDa; }
    public Carrello getCarrello(int index) { return carrello[index];}
    public ContenutoNelMagazzino getContenutoNelMagazzino(int index){ return  this.contenutoNelMagazzino[index];}
    public ContenutoNellOrdine getContenutoNellOrdine(int index){ return  this.contenutoNellOrdine[index];}
    public FornitoDa getFornitoDa(int index){ return this.fornitoDa[index];}


    /* Setters */
    public void setNumero(int numero) {  this.numero = numero; }
    public void setPrezzo(float prezzo) {  this.prezzo = prezzo;  }
    public void setISBN(String ISBN) { this.ISBN = ISBN;  }
    public void setFormato(String formato) {   this.formato = formato;  }
    public void setPeso(float peso) {  this.peso = peso;  }
    public void setAutore(String autore) { this.autore = autore;  }
    public void setTitolo(String titolo) { this.titolo = titolo;  }
    public void setRilegatura(String rilegatura) {this.rilegatura = rilegatura;  }
    //public void setNomeFornitore(String nome_fornitore) {  this.nomeFornitore = nome_fornitore;  }
    public void setBlocked(String blocked){ this.blocked = blocked;}

    public void setCarrello(Carrello[] carrello){ this.carrello = carrello;}
    public void setContenutoNelMagazzino(ContenutoNelMagazzino[] contenutoNelMagazzino){ this.contenutoNelMagazzino = contenutoNelMagazzino;}
    public void setContenutoNellOrdine(ContenutoNellOrdine[] contenutoNellOrdine){ this.contenutoNellOrdine = contenutoNellOrdine;}
    public void setFornitoDa(FornitoDa[] fornitoDa){ this.fornitoDa = fornitoDa; }
    public void setCarrello(int index, Carrello carrello){ this.carrello[index] = carrello;}
    public void setContenutoNelMagazzino(int index, ContenutoNelMagazzino contenutoNelMagazzino){ this.contenutoNelMagazzino[index] = contenutoNelMagazzino;}
    public void setContenutoNellOrdine(int index, ContenutoNellOrdine contenutoNellOrdine){ this.contenutoNellOrdine[index] = contenutoNellOrdine;}
    public void setFornitoDa(int index, FornitoDa fornitoDa){this.fornitoDa[index] = fornitoDa;}

}
