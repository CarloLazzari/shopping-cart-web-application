
<%@page session="false"%>
<%@page import="model.mo.User"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";

%>

<!DOCTYPE html>
<html>
    <head>
        <title>
            FumettiDB: <%=menuActiveLink%>
        </title>
        <%@include file="/include/htmlHead.inc"%>
    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main>

            <% if(loggedOn) {%>
            Benvenuto, <%if(loggedUser.getAdmin().equals("Y")){%>amministratore <%}%><%=loggedUser.getFirstname()%> <%=loggedUser.getSurname()%>. <br/>
            Clicca sulla voce "Prodotti" per navigare nella sezione prodotti. Potrai aggiungere prodotti al carrello e effettuare ordini.
            <% if(loggedUser.getAdmin().equals("Y")) %> Clicca sulla voce "Utenti" per gestire gli utenti.
            <%} else {%>
            Benvenuto.
            Clicca sulla voce "Prodotti" per navigare nella sezione prodotti. Fai il login per aggiungere prodotti al carrello e effettuare ordini.
            <% } %>

        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>