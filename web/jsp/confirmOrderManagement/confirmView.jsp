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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
		<% if(cartItems.size()==0){%>
		<section>
			Il tuo carrello e' attualmente vuoto. Aggiungi prodotti al carrello per effettuare ordini.
		</section>
		<% } else { %>
		<section>
			Prezzo Totale = <%=totalPrice%> Euro
		</section>
		<% } %>
		</br>
		<% for(i=0; i<cartItems.size(); i++) { %>
		<section>
			Prodotto: <%=cartItems.get(i).getFumetto().getISBN()%>  (<%=fumetti.get(i).getTitolo()%>, <%=fumetti.get(i).getAutore()%>, <%=fumetti.get(i).getPrezzo()%> Euro) |
			Quantità presente nel carrello: <%=cartItems.get(i).getQuantita()%>
		</section>
		<% } %>
		</br>

			<% if(cartItems.size()>0){%>
			<form name="confirmOrderForm" action="Dispatcher" method="post">
				<label for="indirizzoSpedizione"></label>
				Inserisci l'indirizzo: <input type="text" id="indirizzoSpedizione" name="indirizzoSpedizione" required maxlength="45"/>
				</br>
				<label for="metodoPagamento"></label>
				Inserisci il numero di carta di credito: <input type="tel" id="metodoPagamento" name="metodoPagamento" maxlength="19" required placeholder="XXXX-XXXX-XXXX-XXXX"/>
				</br>
				</br>
				<input type="submit" class="button" value="Conferma"/>
				<input type="button" name="backButton" class="button" onclick="goback()" value="Annulla"/>
				<input type="hidden" name="controllerAction" value="ConfirmOrderManagement.confirmOrder"/>
			</form>
			<% } %>
        </br>
        </br>

		<form name="backForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="ProductsManagement.view"/>
		</form>

	</main>



    <%@include file="/include/footer.inc"%>

</body>
</html>
