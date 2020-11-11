<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Magazzino" %>
<%@ page import="model.mo.CentroVendita" %><%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 06/11/2020
  Time: 17:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%

	String menuActiveLink = "Modifica magazzino";
	String applicationMessage = (String) request.getAttribute("applicationMessage");
	boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
	User loggedUser = (User) request.getAttribute("loggedUser");

	CentroVendita centroVendita = (CentroVendita) request.getAttribute("nomeFornitore");

	String action = (centroVendita!=null) ? "modify" : "insert";

%>
<html>
<head>
	<%@include file="/include/htmlHead.inc"%>
	<title><%=menuActiveLink%>></title>
	<style>
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

       input[type="text"], input[type="nomeFornitore"], input[type="indirizzo"] {
           border: none;
           border-radius: 4px;
           padding: 3px;
           background-color: #e8eeef;
           color:#8a97a0;
           box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
       }

       input[type="text"]:focus, input[type="indirizzo"]:focus, input[type="nomeFornitore"]:focus {
           background: #d2d9dd;
           outline-color: #0c74f8;
       }
	</style>
	<script>
       const status = '<%=action%>';

       function submitSellingCentre() {
           let f;
           f = document.insModForm;
           f.controllerAction.value = "FacilitiesManagement."+status;
           f.submit();
       }

       function goback() {
           document.backForm.submit();
       }

       function mainOnLoadHandler() {
           document.insModForm.addEventListener("submit", submitSellingCentre);
           document.insModForm.backButton.addEventListener("click", goback);
       }

	</script>
</head>
<body>
	<%@include file="/include/header.inc"%>
	<main>
		<section>
			<h3>Centro Vendita: <%=(action.equals("modify")) ? "Modifica Centro Vendita" : "Nuovo Centro Vendita"%></h3>
		</section>
		<br>

		<section id="insModFormSection">
			<form name="insModForm" action="Dispatcher" method="post">
				<div class="field clearfix">
					<label for="nomeFornitore">Fornitore</label>
					<input type="text" id="nomeFornitore" name="nomeFornitore"
							 value="<%=(action.equals("modify")) ? centroVendita.getNomeCentro() : ""%>"
							 required size="20" maxlength="45" <%if(action.equals("modify")){%>readonly<%}%>/>
				</div>
				<div class="field clearfix">
					<label for="indirizzo">Indirizzo</label>
					<input type="text" id="indirizzo" name="indirizzo"
							 value="<%=(action.equals("modify")) ? centroVendita.getIndirizzo() : ""%>"
							 required size="20" maxlength="45"/>
				</div>
				<br>
				<label>&#160;</label>
				<input type="submit" class="button" value="Conferma"/>
				<input type="button" name="backButton" class="button" onclick="goback()" value="Annulla"/>

				<input type="hidden" name="controllerAction" value="FacilitiesManagement.view">

				<%if (action.equals("modify")) {%>
				<input type="hidden" name="nomeFornitore" value="<%=centroVendita.getNomeCentro()%>"/>
				<input type="hidden" name="whichModifyMode" value="C"/>
				<% } else {%>
				<input type="hidden" name="whichInsertMode" value="C"/>
				<% } %>
			</form>
		</section>

		<form name="backForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="FacilitiesManagement.view"/>
		</form>

	</main>
	<%@include file="/include/footer.inc"%>
</body>
</html>
