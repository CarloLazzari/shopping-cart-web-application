<%@ page import="model.mo.User" %>
<%@ page import="model.mo.ContenutoNellOrdine" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 19/10/2020
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%int i;

	boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
	User loggedUser = (User) request.getAttribute("loggedUser");
	String applicationMessage = (String) request.getAttribute("applicationMessage");
	String menuActiveLink = "Dettagli dell'ordine";
	String whichUserUsername = (String) request.getAttribute("whichUserUsername");
	float prezzo = (float) request.getAttribute("prezzo");
	List<Float> prices = (List<Float>) request.getAttribute("prices");

	List<ContenutoNellOrdine> contenutoNellOrdineList = (List<ContenutoNellOrdine>) request.getAttribute("contenutoNellOrdine");

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

		function mainOnLoadHandler() {
			document.backForm.backButton.addEventListener("click", goback);
		}

		function viewDetails(ISBN){
			const f = document.viewDetailsForm;
			f.ISBN.value = ISBN;
			f.submit();
		}

	</script>
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

		th>a:hover {
			color:black;
			text-decoration: underline;
		}

	</style>

</head>
<body>
	<%@include file="/include/header.inc"%>
	<main>
		<section id="pageTitle">
			<h3>Dettagli dell'ordine <%=contenutoNellOrdineList.get(0).getOrdine().getOrderID()%></h3>
		</section>

		Prezzo totale = <%=prezzo%> &euro;
		</br>
		</br>

		<table>
			<tr>
				<th style="color:#ffbc00">ISBN</th>
				<th style="color:#ffbc00">Quantità acquistata</th>
				<th style="color:#ffbc00">Magazzino</th>
				<th style="color:#ffbc00">Centro vendita</th>
				<th style="color:#ffbc00">Prezzo</th>
			</tr>
		</table>

		<%for(i=0; i<contenutoNellOrdineList.size(); i++) { %>
		<table>
			<tr>
				<th><a href="javascript:viewDetails(<%=contenutoNellOrdineList.get(i).getFumetto().getISBN()%>)"> <%=contenutoNellOrdineList.get(i).getFumetto().getISBN()%></a></th>
				<th><%=contenutoNellOrdineList.get(i).getQuantita()%></th>
				<th><%=contenutoNellOrdineList.get(i).getMagazzino().getNomeMagazzino()%></th>
				<th><%=contenutoNellOrdineList.get(i).getCentroVendita().getNomeCentro()%></th>
				<%if(prices.size()>i){%>
				<th><%=prices.get(i)%>&nbsp€</th>
				<% } %>
			</tr>
		</table>

		<% } %>
		</br>

		<input type="button" name="backButton" class="button" onclick="goback()" value="Go back"/>

		<form name="backForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="OrdersManagement.view"/>
		</form>

		<form name="viewDetailsForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="ProductsManagement.viewDetails"/>
			<input type="hidden" name="ISBN"/>
			<input type="hidden" name="nomeFornitore" value="Quinta Dimensione"/>
			<input type="hidden" name="nomeMagazzino" value="QD Magazzino"/>
		</form>

	</main>
	<%@include file="/include/footer.inc"%>
</body>
</html>
