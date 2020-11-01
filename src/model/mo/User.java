package model.mo;

import java.sql.Date;

public class User {

    private String username;
    private String password;
    private String firstname;
    private String surname;
    private Date dataNascita;
    private String email;
    private String indirizzo;
    private String blocked;
    public String isAdmin;

    /* 1:N */
    private Carta[] carte;
    private Ordine[] ordini;
    private Carrello[] carrello;

    @Override
    public String toString(){ return this.dataNascita  + "-" + this.email + "-" + this.username + "-" + this.password + "-" + this .firstname + "-" + this.surname + "-" + this.indirizzo + "-" + this.blocked; }

    /* Getters */
    public Date getData(){ return dataNascita;  }
    public String getUsername() { return username; }
    public String getPassword() { return password;  }
    public String getFirstname() { return firstname;  }
    public String getSurname() {  return surname;  }
    public String getEmail(){  return email;  }
    public String getIndirizzo(){  return indirizzo;  }
    public String getBlocked() { return blocked;  }
    public String getAdmin() { return isAdmin; }

    public Carta[] getCarte(){ return carte ;}
    public Ordine[] getOrdini(){ return ordini;}
    public Carrello[] getCarrello() { return carrello;}
    public Carta getCarte(int index){ return this.carte[index];}
    public Ordine getOrdini(int index){return this.ordini[index];}
    public Carrello getCarrello(int index){return carrello[index];}

    /* Setters */
    public void setCarte(Carta[] carte){ this.carte = carte;}
    public void setOrdini(Ordine[] ordini){ this.ordini = ordini;}
    public void setCarrello(Carrello[] carrello){this.carrello = carrello;}
    public void setCarte(int index, Carta carte) { this.carte[index]=carte;}
    public void setOrdini(int index, Ordine ordini){ this.ordini[index]=ordini;}
    public void setCarrello(int index, Carrello carrello){this.carrello[index]= carrello;}

    public void setData(Date dataNascita){  this.dataNascita = dataNascita; }
    public void setEmail(String email){  this.email = email; }
    public void setIndirizzo(String indirizzo){   this.indirizzo = indirizzo; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) {this.password = password;}
    public void setFirstname(String firstname) { this.firstname = firstname;}
    public void setSurname(String surname) { this.surname = surname; }
    public void setBlocked(String blocked) { this.blocked = blocked; }
    public void setAdmin(String admin) { isAdmin = admin;}

}
