<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Fumetto" %>
<%@ page import="model.mo.FornitoDa" %>
<%@ page import="model.mo.ContenutoNelMagazzino" %><%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 27/09/2020
  Time: 12:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%

   boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
   User loggedUser = (User) request.getAttribute("loggedUser");

   String applicationMessage = (String) request.getAttribute("applicationMessage");
   String menuActiveLink = "View Details";

   Fumetto fumetto = (Fumetto) request.getAttribute("fumetto");
   FornitoDa fornitoDa = (FornitoDa) request.getAttribute("fornitoDa");
   ContenutoNelMagazzino contenutoNelMagazzino = (ContenutoNelMagazzino) request.getAttribute("contenutoNelMagazzino");

   String disponibilita = (contenutoNelMagazzino.getQuantita()>0) ? "Y" : "N";

%>
<html>
<head>

   <%@include file="/include/htmlHead.inc"%>
   <title>
      FumettiDB: <%=menuActiveLink%>
   </title>
   <style>

      div>a {
         color: #424242 !important;
      }
      a:hover {
         text-decoration: underline;
      }

      .field {
         margin: 5px 0;
      }

      label {
         float: left;
         width: 56px;
         font-size: 0.8em;
         margin-right: 10px;
         padding-top: 3px;
         text-align: left;
      }

      p {
         width: 50%;
         border: none;
         border-radius: 4px;
         padding: 3px;
         background-color: #e8eeef;
         color:#8a97a0;
         box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
      }

      #prodotto {
         float: right;
      }

      #immagine {
         position: relative;
      }
      #immagine img {
         position: absolute;
         right: 20px;
      }

   </style>
   <script lang="JavaScript">

      function goback() {
         document.backForm.submit();
      }

      function mainOnLoadHandler() {
         document.backForm.backButton.addEventListener("click", goback);
      }

      function addToCart(ISBN){
         const f = document.addToCartForm;
         f.ISBN.value = ISBN;
         f.submit();
      }

   </script>
</head>
<body>
   <%@include file="/include/header.inc"%>
   <main>
      <section id="pageTitle">
         <h1>
             Fumetti: visualizza dettagli
         </h1>
      </section>

      <div style="margin-right: 50px; text-align: right; color:#ffbc00; font-family: 'Open Sans','Helvetica Neue',Helvetica,Arial,sans-serif">
      <%if(contenutoNelMagazzino.getQuantita()==0){ %>
      <strong>NON DISPONIBILE</strong>
      <% } %>
      </div>
      <div id="immagine" style="margin-top: 5px">
         <img id="prodotto" alt="<%=fumetto.getISBN()%>" src="productImages/<%=fumetto.getISBN()%>.jpg" width="200" height="286">
      </div>

      <section id="Product">
         <div class="field clearfix">
            <p> <strong>Titolo:</strong> <%=fumetto.getTitolo()%> </p>
         </div>
         <div class="field clearfix">
            <p> <strong>Autore/i:</strong> <%=fumetto.getAutore()%> </p>
         </div>
         <div class="field clearfix">
            <p> <strong>Numero:</strong> <%=fumetto.getNumero()%> </p>
         </div>
         <div class="field clearfix">
            <p> <strong>Formato:</strong> <%=fumetto.getFormato()%>cm </p>
         </div>
         <div class="field clearfix">
            <p> <strong>Rilegatura:</strong> <%=fumetto.getRilegatura()%> </p>
         </div>
         <div class="field clearfix">
            <p> <strong>Prezzo:</strong> <%=fumetto.getPrezzo()%> &euro;</p>
         </div>
         <div class="field clearfix">
            <p> <strong>Peso:</strong> <%=fumetto.getPeso()%>kg </p>
         </div>
         <div class="field clearfix">
            <p> <strong>Fornitore:</strong> <%=fornitoDa.getCentroVendita().getNomeCentro()%></p>
         </div>
         <%if((loggedUser!=null)&&(loggedUser.getAdmin().equals("Y"))) { %>
         <div class="field clearfix">
            <p> <strong>Magazzino:</strong> <%=contenutoNelMagazzino.getMagazzino().getNomeMagazzino()%></p>
         </div>
         <div class="field clearfix">
            <p><strong>Quantit&agrave in magazzino:</strong> <%=contenutoNelMagazzino.getQuantita()%></p>
         </div>
         <div>
            <p><strong>Disponibile:</strong> <%=disponibilita%></p>
         </div>
         <div class="field clearfix">
            <p> <strong>Bloccato:</strong> <%=fumetto.getBlocked()%></p>
         </div>
         <div class="field clearfix">
            <p> <strong>Cancellato:</strong> <%=contenutoNelMagazzino.getDeleted()%></p>
         </div>
         <% } %>
         <div class="field clearfix">
            <a href="http://www.google.com/search?q=<%=fumetto.getTitolo()%>+<%=fumetto.getAutore()%>"><strong>Leggi di più</strong></a>
         </div>
      </section>

      <%if(loggedOn){%>
      <%if(contenutoNelMagazzino.getQuantita()>0){%>
      Aggiungi al carrello:
      <span>
         <a href="javascript:addToCart(<%=fumetto.getISBN()%>)">
            <img id=addToCart alt="addToCart" src="images/cart_plus.png" width="25" height="25">
         </a>
      </span>
      <%}%>
      <% } %>

      <br>
      <br>

      <input type="button" name="backButton" class="button" onclick="goback()" value="Go back"/>

      <form name="backForm" method="post" action="Dispatcher">
         <input type="hidden" name="controllerAction" value="ProductsManagement.view"/>
      </form>

      <form name="addToCartForm" method="post" action="Dispatcher">
         <input type="hidden" name="ISBN"/>
         <input type="hidden" name="controllerAction" value="ProductsManagement.addToCart"/>
      </form>

   </main>
<%@include file="/include/footer.inc"%>
</body>
</html>
