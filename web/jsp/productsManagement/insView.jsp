<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Fumetto" %>
<%@ page import="model.mo.ContenutoNelMagazzino" %>
<%@ page import="model.mo.FornitoDa" %><%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 27/09/2020
  Time: 12:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");

    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "New Product Insert";
    String ISBN =  (String) request.getParameter("ISBN");

    Fumetto fumetto = (Fumetto) request.getAttribute("fumetto");
    ContenutoNelMagazzino contenutoNelMagazzino = (ContenutoNelMagazzino) request.getAttribute("contenutoNelMagazzino");
    FornitoDa fornitoDa = (FornitoDa) request.getAttribute("fornitoDa");
    String action=(fumetto != null) ? "modify" : "insert";


%>
<html>
<head>

	<%@include file="/include/htmlHead.inc"%>
	<title>
		FumettiDB: <%=menuActiveLink%>
	</title>
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

		input[type="text"], input[type="titolo"], input[type="autore"], input[type="numero"], input[type="formato"], input[type="rilegatura"], input[type="prezzo"], input[type="peso"], input[type="bloccato"], input[type="nomeFornitore"], input[type="nomeMagazzino"] {
			border: none;
			border-radius: 4px;
			padding: 3px;
			background-color: #e8eeef;
			color:#8a97a0;
			box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
		}

		input[type="text"]:focus, input[type="titolo"]:focus, input[type="autore"]:focus, input[type="numero"]:focus, input[type="formato"]:focus, input[type="rilegatura"]:focus, input[type="prezzo"]:focus, input[type="peso"]:focus, input[type="bloccato"]:focus, input[type="nomeFornitore"]:focus, input[type="nomeMagazzino"]:focus {
			background: #d2d9dd;
			outline-color: #0c74f8;
		}

		#addToWarehouse, #removeFromWarehouse {

			margin: 5px;
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

		const status = '<%=action%>';

		function submitProduct() {
			let f;
			f = document.insModForm;
			f.controllerAction.value = "ProductsManagement."+status;
			f.submit();
		}

		function addToWarehouse(ISBN){
			const f = document.addToWarehouseForm;
			f.ISBN.value = ISBN;
			f.submit();
		}

		function removeFromWarehouse(ISBN){
			const f = document.removeFromWarehouseForm;
			f.ISBN.value = ISBN;
			f.submit();
		}

		function goback() {
			document.backForm.submit();
		}

		function mainOnLoadHandler() {
			document.insModForm.addEventListener("submit", submitProduct);
			document.insModForm.backButton.addEventListener("click", goback);
		}

	</script>
</head>
<body>
	<%@include file="/include/header.inc"%>
	<main>
		<section id="pageTitle">
			<h1>
				 Fumetti: <%=(action.equals("modify")) ? "Modifica Fumetto" : "Nuovo Fumetto"%>
			</h1>
		</section>

		<%if(action.equals("modify")){%>
		<div id="immagine" style="margin-top: 5px">
			<img id="prodotto" alt="<%=fumetto.getISBN()%>" src="productImages/<%=fumetto.getISBN()%>.jpg" width="269" height="385">
		</div>
		<%}%>

		<section id="insModFormSection">
			<form name="insModForm" action="Dispatcher" method="post">
				<div class="field clearfix">
					<label for="ISBN">ISBN</label>
					<input type="text" id="ISBN" name="ISBN"
							value="<%=(action.equals("modify")) ? fumetto.getISBN() : ""%>"
							required size="20" maxlength="45" <%if(action.equals("modify")){%>readonly<%}%>/>
				</div>
				<div class="field clearfix">
					<label for="Titolo">Titolo</label>
					<input type="text" id="Titolo" name="Titolo"
							value="<%=(action.equals("modify")) ? fumetto.getTitolo() : ""%>"
							required size="20" maxlength="45"/>
				</div>
				<div class="field clearfix">
					<label for="Autore">Autore</label>
					<input type="text" id="Autore" name="Autore"
							value="<%=(action.equals("modify")) ? fumetto.getAutore() : ""%>"
							required size="20" maxlength="45"/>
				</div>
				<div class="field clearfix">
				   <label for="Numero">Numero</label>
				   <input type="text" id="Numero" name="Numero"
							value="<%=(action.equals("modify")) ? fumetto.getNumero() : ""%>"
							required size="20" maxlength="3"/>
				</div>
				<div class="field clearfix">
				   <label for="Formato">Formato</label>
				   <input type="text" id="Formato" name="Formato"
							value="<%=(action.equals("modify")) ? fumetto.getFormato() : ""%>"
							required size="20" maxlength="45"/>
				</div>
				<div class="field clearfix">
				   <label for="Rilegatura">Rilegatura</label>
				   <input type="text" id="Rilegatura" name="Rilegatura"
							value="<%=(action.equals("modify")) ? fumetto.getRilegatura() : ""%>"
							required size="20" maxlength="45"/>
				</div>
				<div class="field clearfix">
				   <label for="Prezzo">Prezzo</label>
				   <input type="text" id="Prezzo" name="Prezzo"
							value="<%=(action.equals("modify")) ? fumetto.getPrezzo() : ""%>"
							required size="20" maxlength="45"/>
				</div>
				<div class="field clearfix">
				   <label for="Peso">Peso</label>
				   <input type="text" id="Peso" name="Peso"
							value="<%=(action.equals("modify")) ? fumetto.getPeso() : ""%>"
							required size="20" maxlength="45"/>
				</div>
				<div class="field clearfix">
				   <label for="Quantita">Quantità</label>
				   <input type="text" id="Quantita" name="Quantita"
							value="<%=(action.equals("modify")) ? contenutoNelMagazzino.getQuantita() : ""%>"
							required size="20" maxlength="3"/>
				</div>
				<div class="field clearfix">
				   <label for="Deleted">Deleted</label>
				   <input type="text" id="Deleted" name="Deleted"
							value="<%=(action.equals("modify")) ? contenutoNelMagazzino.getDeleted() : ""%>"
							required size="20" maxlength="1"/>
				</div>
				<div class="field clearfix">
				   <label for="Bloccato">Bloccato</label>
				   <input type="text" id="Bloccato" name="Bloccato"
							value="<%=(action.equals("modify")) ? fumetto.getBlocked() : ""%>"
							required size="20" maxlength="1"/>
				</div>
				<div class="field clearfix">
				   <label for="nomeFornitore">Fornitore</label>
				   <input type="text" id="nomeFornitore" name="nomeFornitore"
							value="<%=(action.equals("modify")) ? fornitoDa.getCentroVendita().getNomeCentro() : ""%>"
							required size="20" maxlength="45" <%if(action.equals("modify")){%>readonly<%}%>/>
				</div>
				<div class="field clearfix">
				   <label for="nomeMagazzino">Magazzino</label>
				   <input type="text" id="nomeMagazzino" name="nomeMagazzino"
							value="<%=(action.equals("modify")) ? contenutoNelMagazzino.getMagazzino().getNomeMagazzino() : ""%>"
							required size="20" maxlength="45" <%if(action.equals("modify")){%>readonly<%}%>/>
				</div>
				<label>&#160;</label>
				<input type="submit" class="button" value="Conferma"/>
				<input type="button" name="backButton" class="button" onclick="goback()" value="Annulla"/>
				<input type="hidden" name="controllerAction" value="ProductsManagement.view">
				<%if (action.equals("modify")) {%>
				<input type="hidden" name="ISBN" value="<%=fumetto.getISBN()%>"/>
				<input type="hidden" name="nomeFornitore" value="<%=fornitoDa.getCentroVendita().getNomeCentro()%>"/>
				<input type="hidden" name="nomeMagazzino" value="<%=contenutoNelMagazzino.getMagazzino().getNomeMagazzino()%>"/>
				<% } %>
			</form>
		</section>

		<br>

		<%if (action.equals("modify")) {%>
		<section class="field clearfix">
			<p>Aggiungi quantità al magazzino: </p>
			<a href="javascript:addToWarehouse(<%=fumetto.getISBN()%>)">
				<img id="addToWarehouse" alt="addToWareHouse" src="images/add_book_icon.png" width="30" height="30">
			</a>
			<p>Rimuovi quantità dal magazzino: </p>
			<a href="javascript:removeFromWarehouse(<%=fumetto.getISBN()%>)">
				<img id="removeFromWarehouse" alt="removeFromWareHouse" src="images/remove_book.png" width="30" height="30">
			</a>
		</section>
		<% } %>

		<form name="backForm" method="post" action="Dispatcher">
			<input type="hidden" name="controllerAction" value="ProductsManagement.view"/>
		</form>

		<form name="addToWarehouseForm" method="post" action="Dispatcher">
			<input type="hidden" name="ISBN"/>
			<input type="hidden" name="nomeMagazzino" value="QD Magazzino"/>
			<input type="hidden" name="nomeFornitore" value="Quinta Dimensione"/>
			<input type="hidden" name="controllerAction" value="ProductsManagement.addQuantityToWarehouse"/>
		</form>
		<form name="removeFromWarehouseForm" method="post" action="Dispatcher">
			<input type="hidden" name="ISBN"/>
			<input type="hidden" name="nomeMagazzino" value="QD Magazzino"/>
			<input type="hidden" name="nomeFornitore" value="Quinta Dimensione"/>
			<input type="hidden" name="controllerAction" value="ProductsManagement.removeQuantityFromWarehouse"/>
		</form>

    </main>
    <%@include file="/include/footer.inc"%>
</body>
</html>
