package model.dao.CookieImpl;

import model.dao.UserDAO;
import model.mo.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


public class UserDAOCookieImpl implements UserDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public User create(String username, String password, String nome, String cognome, String email, String dataNascita, String indirizzo, String blocked, String isAdmin) {

        User loggedUser = new User();
        loggedUser.setUsername(username);
        loggedUser.setFirstname(nome);
        loggedUser.setSurname(cognome);
        loggedUser.setAdmin(isAdmin);

        Cookie cookie;

        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;
    }

    @Override
    public void update(User loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser",encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void delete(User loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser","");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public User findLoggedUser() {

        Cookie[] cookies = request.getCookies();
        User loggedUser = null;

        if(cookies != null){
            for(int i=0; i<cookies.length && loggedUser==null; i++){
                if(cookies[i].getName().equals("loggedUser")){
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;
    }

    private String encode(User loggedUser) {

        String encoddedLoggedUser;
        encoddedLoggedUser = loggedUser.getUsername()+"#"+loggedUser.getFirstname()+"#"+loggedUser.getSurname()+"#"+loggedUser.getAdmin();
        return encoddedLoggedUser;
    }

    private User decode(String encoddedLoggedUser) {

        User loggedUser = new User();

        String[] values = encoddedLoggedUser.split("#");
        loggedUser.setUsername(values[0]);
        loggedUser.setFirstname(values[1]);
        loggedUser.setSurname(values[2]);
        loggedUser.setAdmin(values[3]);

        return loggedUser;

    }

    @Override
    public void block(User user) { throw new UnsupportedOperationException("Not implement yet"); }

    @Override
    public void unblock(User user) { throw new UnsupportedOperationException("Not implement yet"); }

    @Override
    public User findByUsername(String username) { throw new UnsupportedOperationException("Not implement yet"); }

    @Override
    public ArrayList<Integer> countOrderByUsername() { throw new UnsupportedOperationException("Not implement yet"); }

    @Override
    public ArrayList<User> findAllUsers() {
        throw new UnsupportedOperationException("Not implement yet");
    }

    @Override
    public int countOrders(String username) { throw new UnsupportedOperationException("Not implement yet"); }

}