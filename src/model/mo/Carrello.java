package model.mo;

public class Carrello {

    private String usernameUtente; 
    private String ISBNFumetto; 
    private int quantita; 

    /* N:1 */
    private User user;
    private Fumetto fumetto;

    public String getUsername_utente() { return usernameUtente;  }
    public String getISBNFumetto() {  return ISBNFumetto;  }
    public int getQuantita() { return quantita; }
    public User getUser() { return user; }
    public Fumetto getFumetto(){ return fumetto;}

    public void setUsername_utente(String username_utente) { this.usernameUtente = username_utente; }
    public void setISBNFumetto(String ISBNFumetto) {  this.ISBNFumetto = ISBNFumetto; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public void setUser(User user) { this.user = user; }
    public void setFumetto(Fumetto fumetto) {  this.fumetto = fumetto;  }

}
