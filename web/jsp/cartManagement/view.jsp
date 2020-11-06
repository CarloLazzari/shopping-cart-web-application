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
<% int i;
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

		function goback() {
			document.backForm.submit();
		}

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

		function viewDetails(ISBN){
			const f = document.viewDetailsForm;
			f.ISBN.value = ISBN;
			f.submit();
		}

	</script>
	<style>

		#addToCart, #removeFromCart {
			float: right;
		}

		#buy {
			float:right;
			margin-top: 8px;

		}
		#flushCart {
			padding-left: 15px;
			float:right;
			padding-bottom: 20px;

		}

		table {
			border-collapse: collapse;
			width: 80%;
		}

		td, th {
			border: 1px solid #dddddd;
			text-align: left;
			width: 100px;
			padding: 8px;
		}

		th>a:hover {
			color:black;
			text-decoration: underline;
		}

		#cartImages {
			position: relative;
		}
		#cartImages img {
			bottom: -40px;
			right: 20px;
		}

	</style>
</head>
<body>

	<%@include file="/include/header.inc"%>
	<main>

		<%if(loggedUser!=null){%>
			<h3>Carrello dell'utente: <%=loggedUser.getUsername()%></h3>
		<% } %>
		<br>
		<section>
			Cliccando sul pulsante "buy" verrai reindirizzato alla pagina dell'ordine. Cliccando sull'icona del carrello puoi svuotare quest'ultimo.
		</section>
		<br>
		<div id="cartImages">
			<a href="javascript:flushCart()">
 				<img alt="flushCart" id="flushCart" src="images/clear_shopping_cart_2.png" width="100" height="100">
			</a>
			<a href="javascript:buy()">
				<img alt="buy" id="buy" src="images/buy.png" width="100" height="100">
			</a>
		</div>
	  	<% if(cartItems.size()==0){%>
		<section>
	 		Il tuo carrello &egrave attualmente vuoto.
		</section>
	  	<% } else {%>
		Prezzo totale: <%=totalPrice%> &euro;
		<br>
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

		<% for(i=0; i<cartItems.size(); i++){ %>
		<table>
		<tr>
			<th><a href="javascript:viewDetails(<%=cartItems.get(i).getFumetto().getISBN()%>)"><%=cartItems.get(i).getFumetto().getISBN()%></a></th>
				<th><%=fumetti.get(i).getTitolo()%> - <%=fumetti.get(i).getNumero()%></th>
				<th><%=fumetti.get(i).getAutore()%></th>
				<th><%=fumetti.get(i).getPrezzo()%> &euro;</th>
				<th><%=cartItems.get(i).getQuantita()%>
				<a href="javascript:removeFromCart(<%=cartItems.get(i).getFumetto().getISBN()%>)">
					<img id=removeFromCart alt="removeFromCart" src="images/remove_cart.png" width="30" height="30">
				</a>
				<a href="javascript:addToCart(<%=cartItems.get(i).getFumetto().getISBN()%>)">
					<img id=addToCart alt="addToCart" src="images/cart_plus.png" width="30" height="30">
				</a>
			</th>
		</table>
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

		<form name="backForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="ProductsManagement.view"/>
		</form>

		<form name="viewDetailsForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="ProductsManagement.viewDetails"/>
			<input type="hidden" name="ISBN"/>
			<input type="hidden" name="nomeFornitore" value="Quinta Dimensione"/>
			<input type="hidden" name="nomeMagazzino" value="QD Magazzino"/>
		</form>

		<div class="clearfix"></div>
		<br>

		<input type="button" name="backButton" class="button" onclick="goback()" value="Go back"/>

	</main>
	<%@include file="/include/footer.inc"%>
</body>
</html>
