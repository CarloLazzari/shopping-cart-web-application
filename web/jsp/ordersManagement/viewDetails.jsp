<%@ page import="model.mo.User" %>
<%@ page import="model.mo.ContenutoNellOrdine" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 19/10/2020
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
	<title>Dettagli dell'ordine</title>
	<style>

	</style>
	<script lang="javascript">

	</script>
</head>
<body>
	<%@include file="/include/header.inc"%>
	<main>
		<section id="pageTitle">
			Dettagli dell'ordine <%=contenutoNellOrdineList.get(1).getOrdine().getOrderID()%>
		</section>

		Prezzo totale = <%=prezzo%> Euro

		<%for(i=0; i<contenutoNellOrdineList.size(); i++) { %>
			<section>
				<p>
					ISBN: <%=contenutoNellOrdineList.get(i).getFumetto().getISBN()%> -
					Quantità: <%=contenutoNellOrdineList.get(i).getQuantita()%> -
					Magazzino: <%=contenutoNellOrdineList.get(i).getMagazzino().getNomeMagazzino()%> -
					Centro vendita: <%=contenutoNellOrdineList.get(i).getCentroVendita().getNomeCentro()%> -
					<%if(prices.size()>i){%>
					Prezzo totale: <%=prices.get(i)%>
					<% } %>
				</p>
			</section>

		<% } %>
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
