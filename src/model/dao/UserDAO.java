package model.dao;

import model.mo.User;
import java.util.ArrayList;

public interface UserDAO {

    public void block(User user);
    public void unblock(User user);
    public void update(User user);
    public void delete(User user);
    public User create(String username, String password, String nome, String cognome, String email, String dataNascita, String indirizzo, String blocked, String isAdmin);
    public User findLoggedUser(); /* Da implementare nei cookie */
    public User findByUsername(String username); /* Da implementare nei DAO e cookie(?)*/
    public ArrayList<Integer> countOrderByUsername();
    public ArrayList<User> findAllUsers(); /* Da implementare nei DAO e cookie(?)*/
    public int countOrders(String username);



}