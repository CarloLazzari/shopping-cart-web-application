
<%@page session="false"%>
<%@page import="model.mo.User"%>
<%@ page import="model.mo.Fumetto" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.ContenutoNelMagazzino" %>
<%@ page import="model.mo.FornitoDa" %>

<%int i;

    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";

    ArrayList<Fumetto> fumettoArrayList = (ArrayList<Fumetto>) request.getAttribute("fumettoArrayList");
    ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = (ArrayList<ContenutoNelMagazzino>) request.getAttribute("contenutoNelMagazzinoArrayList");
    ArrayList<FornitoDa> fornitoDaArrayList = (ArrayList<FornitoDa>) request.getAttribute("fornitoDaArrayList");

    int whichHalf = (int) request.getAttribute("whichHalf");


%>

<!DOCTYPE html>
<html>
    <head>
        <script>
            function modifyProduct(ISBN){
                const f = document.modifyForm;
                f.ISBN.value = ISBN;
                f.submit();
            }

            function blockProduct(ISBN){
                const f = document.blockForm;
                f.ISBN.value = ISBN;
                f.submit();
            }

            function unblockProduct(ISBN){
                const f = document.unblockForm;
                f.ISBN.value = ISBN;
                f.submit();
            }

            function deleteProduct(ISBN){
                const f = document.deleteForm;
                f.ISBN.value = ISBN;
                f.submit();
            }

            function addToCart(ISBN){
                const f = document.addToCartForm;
                f.ISBN.value = ISBN;
                f.submit();
            }

            function viewDetails(ISBN){
                const f = document.viewDetailsForm;
                f.ISBN.value = ISBN;
                f.submit();
            }

            function arrow(value){
                const f = document.promoPageForm;
                f.whichHalf.value = value;
                f.submit();
            }

        </script>

        <style>

            #fumetti {
                margin-top: 30px;
                margin-left: 110px;
            }

            #prodotti {
                text-align: center;
                float:left;
                height: 150px;
                width: 162px;
                margin: 10px 10px 10px 10px;

            }

            .titolo, .autore, .numero {
                font-size: 0.8em;
                text-align: center;
            }

            .additionalInfo {
                font-size: 0.5em;
            }

            #rightArrow {
                float: right;
            }

            #leftArrow {
                float: left;
                margin-top: 30px;
            }

            img[target=addToCart] {
                margin-left: 12px;
            }

            img[target=addToCart], [target=viewDetails], [target=block], [target=unblock], [target=modify], [target=delete] {
                float:left;
            }

        </style>
        <title>
            FumettiDB: <%=menuActiveLink%>
        </title>
        <%@include file="/include/htmlHead.inc"%>
    </head>
    <body>
        <img id="a" alt="astolfo" src="images/astolfo.png" width="150" height="148">
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
            </br>
            </br>
        </main>
        <main>
            <h3>Vetrina Home-Page</h3>
            <section>
                Oggi consigliamo questi prodotti!
            </section>

            <%if(whichHalf==0) { %>

                <section id="fumetti" class="clearfix">
                    <% for(i=0; i<(fumettoArrayList.size()/2); i++) {%>
                    <article id ="prodotti">
                        <%if(loggedOn){%>
                        <a href="javascript:addToCart(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="addToCart" target="addToCart" src="images/cart_plus.png" width="20" height="20"/>
                        </a>
                        <%}%>
                        <a href="javascript:viewDetails(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="viewDetails" target="viewDetails" src="images/details.png" width="20" height="20"/>
                        </a>
                        <%if((loggedOn)&&loggedUser.getAdmin().equals("Y")){%>
                        <a href="javascript:blockProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="block" target="block" src="images/block_icon.png" width="22" height="22"/>
                        </a>

                        <a href="javascript:unblockProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="block" target="unblock" src="images/unblock.png" width="22" height="22"/>
                        </a>

                        <a href="javascript:modifyProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="modify" target="modify" src="images/modify.png" width="22" height="22"/>
                        </a>

                        <a href="javascript:deleteProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="delete" target="delete" src="images/trashcan.png" width="22" height="22"/>
                        </a>

                        <% } %>
                        </br>

                        <section class="titolo"> <%=fumettoArrayList.get(i).getTitolo()%> - <%=fumettoArrayList.get(i).getNumero()%></section>
                        <section class="autore"> <%=fumettoArrayList.get(i).getAutore()%></section>
                        <section class="numero"> <%=fumettoArrayList.get(i).getPrezzo()%> Euro</section>
                        <section class="additionalInfo"><%=fornitoDaArrayList.get(i).getCentroVendita().getNomeCentro()%></section>
                        <section class="additionalInfo"><%=contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino()%></section>
                    </article>
                    <% } %>

                    <a href="javascript:arrow(1)">
                        <img id="rightArrow" alt="rightArrow" src="images/right_arrow.png" height="115" width="100">
                    </a>
                </section>

            <% } else if (whichHalf==1) { %>

                <a href="javascript:arrow(0)">
                    <img id="leftArrow" alt="leftArrow" src="images/left_arrow.png" height="115" width="100">
                </a>

                <section id="fumetti" class="clearfix">

                    <% for(i=11; i>5; i--) {%>
                    <article id ="prodotti">
                        <%if(loggedOn){%>
                        <a href="javascript:addToCart(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="addToCart" target="addToCart" src="images/cart_plus.png" width="20" height="20"/>
                        </a>
                        <%}%>
                        <a href="javascript:viewDetails(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="viewDetails" target="viewDetails" src="images/details.png" width="20" height="20"/>
                        </a>
                        <%if((loggedOn)&&loggedUser.getAdmin().equals("Y")){%>
                        <a href="javascript:blockProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="block" target="block" src="images/block_icon.png" width="22" height="22"/>
                        </a>

                        <a href="javascript:unblockProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="block" target="unblock" src="images/unblock.png" width="22" height="22"/>
                        </a>

                        <a href="javascript:modifyProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="modify" target="modify" src="images/modify.png" width="22" height="22"/>
                        </a>

                        <a href="javascript:deleteProduct(<%=fumettoArrayList.get(i).getISBN()%>)">
                            <img alt="delete" target="delete" src="images/trashcan.png" width="22" height="22"/>
                        </a>

                        <% } %>
                        </br>

                        <section class="titolo"> <%=fumettoArrayList.get(i).getTitolo()%> - <%=fumettoArrayList.get(i).getNumero()%></section>
                        <section class="autore"> <%=fumettoArrayList.get(i).getAutore()%></section>
                        <section class="numero"> <%=fumettoArrayList.get(i).getPrezzo()%> Euro</section>
                        <section class="additionalInfo"><%=fornitoDaArrayList.get(i).getCentroVendita().getNomeCentro()%></section>
                        <section class="additionalInfo"><%=contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino()%></section>
                    </article>
                    <% } %>


                </section>

            <% } %>

            </br>
        </main>

        <form name="addToCartForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="controllerAction" value="ProductsManagement.addToCart"/>
        </form>
        <form name="blockForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="controllerAction" value="ProductsManagement.block"/>
        </form>
        <form name="unblockForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="controllerAction" value="ProductsManagement.unblock"/>
        </form>
        <form name="deleteForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="nomeMagazzino" value="QD Magazzino">
            <input type="hidden" name="controllerAction" value="ProductsManagement.delete"/>
        </form>
        <form name="modifyForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="nomeFornitore" value="Quinta Dimensione"/>
            <input type="hidden" name="nomeMagazzino" value="QD Magazzino"/>
            <input type="hidden" name="controllerAction" value="ProductsManagement.modifyView"/>
        </form>
        <form name="viewDetailsForm" method="post" action="Dispatcher">
            <input type="hidden" name="ISBN"/>
            <input type="hidden" name="controllerAction" value="ProductsManagement.viewDetails"/>
        </form>
        <form name="searchForm" method="post" action="Dispatcher">
            <input type="hidden" name="searchString"/>
            <input type="hidden" name="searchMode"/>
            <input type="hidden" name="controllerAction" value="ProductsManagement.view"/>
        </form>

        <form name="promoPageForm" method="post" action="Dispatcher">
            <input type="hidden" name="whichHalf"/>
            <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
        </form>

        <%@include file="/include/footer.inc"%>
    </body>
</html>
