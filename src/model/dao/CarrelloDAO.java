    package model.dao;

    import model.mo.Carrello;
    import model.mo.Fumetto;
    import model.mo.User;

    import java.util.ArrayList;

    public interface CarrelloDAO {

        public Carrello addToCart(Fumetto fumetto, User user);
        public Carrello findByISBNAndUsername(String ISBNProdotto, String username);
        public ArrayList<Carrello> viewCart(String username);
        public void removeFromCart(Carrello carrello);
        public void addQuantity(Carrello carrello);
        public void substractQuantity(Carrello carrello);
        public void flushCart(String username);
        public float calculatePrice(String username);
        public int getQuantity(String username, String ISBN);


    }