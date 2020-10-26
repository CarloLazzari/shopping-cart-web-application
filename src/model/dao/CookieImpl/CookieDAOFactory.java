package model.dao.CookieImpl;

import model.dao.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class CookieDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            this.request=(HttpServletRequest) factoryParameters.get("request");
            this.response=(HttpServletResponse) factoryParameters.get("response");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /* Non ho concetto di transazione con i cookie*/
    @Override
    public void commitTransaction() {}

    @Override
    public void rollbackTransaction() {}

    @Override
    public void closeTransaction() {}

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOCookieImpl(request,response);
    }

    @Override
    public CartaDAO getCartaDAO() {
        return null;
    }

    @Override
    public CentroVenditaDAO getCentroVenditaDAO() {
        return null;
    }

    @Override
    public FumettoDAO getFumettoDAO() {
        return null;
    }

    @Override
    public ContenutoNelMagazzinoDAO getContenutoNelMagazzinoDAO() {
        return null;
    }

    @Override
    public MagazzinoDAO getMagazzinoDAO() {
        return null;
    }

    @Override
    public OrdineDAO getOrdineDao() {
        return null;
    }

    @Override
    public ContenutoNellOrdineDAO getContenutoNellOrdineDAO() {
        return null;
    }

    @Override
    public CarrelloDAO getCarrelloDAO() {
        return null;
    }

    @Override
    public FornitoDaDAO getFornitoDaDAO() { return null; }


}