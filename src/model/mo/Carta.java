
package model.mo;
import java.util.Date;

public class Carta {

    private String numeroCarta; //cardNumber
    private String intestatario; //who owns the cart
    private Date dataScadenza; //expire date

    /* N:1 */
    private User user;
    /* 1:N */
    private Ordine[] ordini; //orders

    public User getUser(){ return this.user;}
    public Ordine[] getOrdini() { return this.ordini; }
    public Ordine getOrdini(int index) { return this.ordini[index];}
    public String getNumeroCarta(){
        return numeroCarta;
    }
    public String getIntestatario(){
        return intestatario;
    }
    public Date getDataScadenza(){ return dataScadenza; }

    public void setUser(User user){ this.user = user;}
    public void setOrdini(Ordine[] ordini) { this.ordini = ordini;  }
    public void setOrdini(Ordine ordini, int index) { this.ordini[index] = ordini;  }
    public void setDataScadenza(Date dataScadenza){
        this.dataScadenza = dataScadenza;
    }
    public void setNumeroCarta(String numeroCarta){
        this.numeroCarta = numeroCarta;
    }
    public void setIntestatario(String intestatario){
        this.intestatario = intestatario;
    }

}