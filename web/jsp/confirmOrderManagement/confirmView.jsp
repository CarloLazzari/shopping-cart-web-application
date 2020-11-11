<%@ page import="model.mo.Carrello" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.List" %>
<%@ page import="model.mo.Fumetto" %><%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 28/09/2020
  Time: 01:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>

<% int i=0;

	boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
	User loggedUser = (User) request.getAttribute("loggedUser");
	String applicationMessage = (String) request.getAttribute("applicationMessage");
	String menuActiveLink = "Compra";

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
	<style>

		table {
	  		border-collapse: collapse;
	  		width: 100%;
		}

		td, th {
			border: 1px solid #dddddd;
		  	text-align: left;
		  	width: 100px;
		  	padding: 8px;
		}

	</style>
	<script lang="JavaScript">

		function submitOrder(){
		    let f;
		    f = document.confirmOrderForm;
		    f.submit();
		}

      function goback() {
          document.backForm.submit();
      }

		function mainOnLoadHandler(){
		    document.confirmOrderForm.addEventListener("submit",submitOrder);
          document.confirmOrderForm.backButton.addEventListener("click", goback);
		}

	</script>

</head>
<body>
	<%@include file="/include/header.inc"%>
	<main>
		<section id="pageTitle">
			<h3>Conferma dell'ordine</h3>
		</section>
		<% if(cartItems.size()==0){%>
			<section>
				Il tuo carrello e' attualmente vuoto. Aggiungi prodotti al carrello per effettuare ordini.
			</section>
		<% } else { %>

			<br>
			<table>
				<tr>
					<th style="color:#ffbc00">ISBN</th>
					<th style="color:#ffbc00">Titolo</th>
					<th style="color:#ffbc00">Autore</th>
					<th style="color:#ffbc00">Prezzo</th>
					<th style="color:#ffbc00">Quantit&agrave</th>
				</tr>
			</table>
		<% } %>

		<% for(i=0; i<cartItems.size(); i++) { %>
			<table>
				<tr>
					<th><%=cartItems.get(i).getFumetto().getISBN()%></th>
					<th><%=fumetti.get(i).getTitolo()%> - <%=fumetti.get(i).getNumero()%></th>
					<th><%=fumetti.get(i).getAutore()%></th>
					<th><%=fumetti.get(i).getPrezzo()%> &euro;</th>
					<th><%=cartItems.get(i).getQuantita()%></th>
				</tr>
			</table>
		<% } %>
		<br>

			<% if(cartItems.size()>0){%>
			<section>
				Prezzo Totale = <%=totalPrice%> &euro;
			</section>
			<form name="confirmOrderForm" action="Dispatcher" method="post">
				<label for="indirizzoSpedizione"></label>
				Inserisci l'indirizzo: <input type="text" id="indirizzoSpedizione" name="indirizzoSpedizione" required maxlength="45"/>
				<br>
				<label for="metodoPagamento"></label>
				Inserisci il numero di carta di credito: <input type="tel" id="metodoPagamento" name="metodoPagamento" maxlength="19" required placeholder="XXXX-XXXX-XXXX-XXXX"/>
				<br>
				<br>
				<input type="submit" class="button" value="Conferma"/>
				<input type="button" name="backButton" class="button" onclick="goback()" value="Annulla"/>
				<input type="hidden" name="controllerAction" value="ConfirmOrderManagement.confirmOrder"/>
			</form>
			<% } %>
        <br>

		<form name="backForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="ProductsManagement.view"/>
		</form>

	</main>

    <%@include file="/include/footer.inc"%>

</body>
</html>
