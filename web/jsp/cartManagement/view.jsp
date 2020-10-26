<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Carrello" %>
<%@ page import="java.util.List" %>
<%@ page import="model.mo.Fumetto" %><%--

  Created by IntelliJ IDEA.
  User: Carlo
  Date: 28/09/2020
  Time: 01:40
  To change this template use File | Settings | File Templates.
--%>
<% int i =0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Carrello";

    String ISBN = request.getParameter("ISBN");

    List<Carrello> cartItems = (List<Carrello>) request.getAttribute("cartItems");
    List<Fumetto> fumetti = (List<Fumetto>) request.getAttribute("fumetti");
    float totalPrice = (float) request.getAttribute("totalPrice");

%>

<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <title>
        FumettiDB: <%=menuActiveLink%>
    </title>
    <script lang="javascript">

        function addToCart(ISBN){
            const f = document.addToCartForm;
            f.ISBN.value = ISBN;
            f.submit();
        }

        function removeFromCart(ISBN){
            const f = document.removeFromCartForm;
            f.ISBN.value = ISBN;
            f.submit();
        }

        function buy(){
            const f=document.buyForm;
            f.submit();
        }

        function flushCart(){
            const f=document.flushCartForm;
            f.submit();
        }


    </script>
    <style>

        #addToCart, #removeFromCart {
            position:relative;
        }

        #buy {
            float:right;
            padding-bottom: 20px;
        }
        #flushCart {
            padding-left: 15px;
            float:right;
            padding-bottom: 20px;

        }


    </style>
</head>
<body>

    <%@include file="/include/header.inc"%>
    <main>
        <%if(loggedUser!=null){%>
        <h3>Carrello dell'utente: <%=loggedUser.getUsername()%></h3>
        <% } %>
        </br>
        <a href="javascript:flushCart()">
            <img alt="flushCart" id="flushCart" src="images/flush_cart.png" width="100" height="100">
        </a>
        <a href="javascript:buy()">
            <img alt="buy" id="buy" src="images/buy.png" width="100" height="100">
        </a>
        <% if(cartItems.size()==0){%>
        <section>
            Il tuo carrello e' attualmente vuoto.
        </section>
        <% } else {%>
            Prezzo totale: <%=totalPrice%>
        <% } %>
        <% for(i=0; i<cartItems.size(); i++){ %>
            <section>
                <p/>
                    Prodotto: <%=cartItems.get(i).getFumetto().getISBN()%>  (<%=fumetti.get(i).getTitolo()%>, <%=fumetti.get(i).getAutore()%>, <%=fumetti.get(i).getPrezzo()%> Euro) |
                    Quantita: <%=cartItems.get(i).getQuantita()%>

                Aggiungi/rimuovi quantita' al/dal carrello:
                <a href="javascript:addToCart(<%=cartItems.get(i).getFumetto().getISBN()%>)">
                   <img id=addToCart alt="addToCart" src="images/cart_plus.png" width="22" height="22">
                </a>
                <a href="javascript:removeFromCart(<%=cartItems.get(i).getFumetto().getISBN()%>)">
                    <img id=removeFromCart alt="removeFromCart" src="images/remove_cart.png" width="22" height="22">
                </a>
            </section>
        <% } %>

        <form name="buyForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="ConfirmOrderManagement.view"/>
        </form>

        <form name="addToCartForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="controllerAction" value="CartManagement.addQuantity"/>
        </form>

        <form name="removeFromCartForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="controllerAction" value="CartManagement.removeQuantity"/>
        </form>

        <form name="flushCartForm" method="post" action="Dispatcher">
            <input type="hidden" name="flushCart" value=""/>
            <input type="hidden" name="controllerAction" value="CartManagement.flushCart"/>
        </form>

        <div class="clearfix"></div>
        </br>
        <section>
            <form id ="backForm">
                <input type="button" value="Go back!" onclick="history.back()">
            </form>
        </section>
    </main>
    <%@include file="/include/footer.inc"%>
</body>
</html>
