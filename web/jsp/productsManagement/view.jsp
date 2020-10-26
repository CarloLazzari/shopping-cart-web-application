
<%@page session="false"%>
<%@page import="model.mo.User"%>
<%@ page import="model.mo.Fumetto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page import="model.mo.ContenutoNelMagazzino" %>
<%@ page import="model.mo.FornitoDa" %>

<% int i;

    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");

    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Products";
    String searchString =  request.getParameter("searchString");
    String modalitaRicerca = request.getParameter("modalitaRicerca");
    String ISBN =  request.getParameter("ISBN");

    List<Fumetto> fumetti = (List<Fumetto>) request.getAttribute("fumetti");
    List<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = (List<ContenutoNelMagazzino>) request.getAttribute("contenutoNelMagazzinoArrayList");
    List<FornitoDa> fornitoDaArrayList = (List<FornitoDa>) request.getAttribute("fornitoDaArrayList");
    //List<File> imageUrlList = (List<File>) request.getAttribute("imageUrlList");

%>

<!DOCTYPE html>
<html>

	<head>
		<%@include file="/include/htmlHead.inc"%>
		<title>
			Fumetti
		</title>
		<style>

        #newProduct {
            float: right;
				margin-bottom: 10px;
        }


			#products article{
          float: left;
          width: 250px;
			 height: 500px;
          border-width: 1px;
          border-style: solid;
          border-radius: 10px;
          border-color: #a6a6f5;
          padding: 10px 8px 10px 20px;
          margin: 0 18px 16px 0;
          background: linear-gradient(to right, #ffffff, #e7f8ff);
          box-shadow: 0 3px 2px #424e4e;
			}

			#delete, #block, #addToCart, #viewDetails, #viewDetails, #unblock, #modify {
				float:left;
			}

			input[id=searchString] {
				 background-color: white;
				 background-position: 10px 10px;
				 background-repeat: no-repeat;
				 padding-left: 5px;
			}

			#fumetti {
				 margin-top: 30px;
				 margin-left: 80px;
			}

			#prodotti {
				 text-align: center;
				 float:left;
				 height: 300px;
				 width: 150px;
				 margin: 10px 10px 10px 10px;

			}

        .titolo, .autore, .numero {
            font-size: 0.8em;
				text-align: center;
        }

		  .additionalInfo {
				font-size: 0.5em;
		  }


		</style>
			<script lang="JavaScript">

			function insertProduct(){
				document.insertProductForm.submit();
			}

			function search(searchString,modalitaRicerca){
				const f = document.searchForm;
				f.searchString.value = searchString;
				f.modalitaRicerca.value = modalitaRicerca;
				f.submit();
			}

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

			function mainOnLoadHandler(){
				document.querySelector("#newProductImage").addEventListener("click",insertProduct);
				document.querySelector("#addToCart").addEventListener("click",addToCart);
				document.querySelector("#block").addEventListener("click",blockProduct);
				document.querySelector("#unblock").addEventListener("click",unblockProduct);
				document.querySelector("#delete").addEventListener("click",deleteProduct);
				document.querySelector("#modify").addEventListener("click",modifyProduct);
			}

			const searchInput = $('input[name=searchString]', '#search').val()
			const optionInput = $('input[name=modalitaRicerca]:checked', '#modalitaRicercaForm').val()

		</script>
	</head>
		<body>
				<%@include file="/include/header.inc"%>
				<main>
					<%if((loggedOn)&&loggedUser.getAdmin().equals("Y")){%>
					<aside>
						<a href="javascript:insertProduct()">
							<img id="newProduct" alt="newProduct" src="images/add_book_icon.png" width="50" height="50" >
						</a>
					</aside>
					<%}%>
					<section id="pageTitle">
							<h1> Prodotti </h1>
					</section>

					<form id="search">
						<label for="searchString">
							<input type="text" id="searchString" name="searchString" maxlength="20"> </br>
						</label>
					</form>
					<form id="modalitaRicercaForm" style="margin-top: 3px;">
						<label>
							<input type="radio" name="modalitaRicerca" value="Titolo"/>Titolo
							<input type="radio" name="modalitaRicerca" value="Autore"/>Autore
							<input type="radio" name="modalitaRicerca" value="Numero"/>Numero
						</label>
					</form>

					<section style="margin-top: 3px;">
					<a href="javascript:search(searchInput,optionInput)">
						<img alt="search" id="searchImage" src="images/search.png" width="22" height="22">
					</a>
					</section>

					<section id="fumetti" class="clearfix">
						<% for(i=0; i<fumetti.size(); i++) {%>
						<article id ="prodotti">
							<%if(loggedOn){%>
							<a href="javascript:addToCart(<%=fumetti.get(i).getISBN()%>)">
								<img alt="addToCart" id="addToCart" src="images/cart_plus.png" width="20" height="20"/>
							</a>
							<%}%>
							<a href="javascript:viewDetails(<%=fumetti.get(i).getISBN()%>)">
								<img alt="viewDetails" id="viewDetails" src="images/details.png" width="20" height="20"/>
							</a>
							<%if((loggedOn)&&loggedUser.getAdmin().equals("Y")){%>
							<a href="javascript:blockProduct(<%=fumetti.get(i).getISBN()%>)">
								<img alt="block" id="block" src="images/block_icon.png" width="22" height="22"/>
							</a>

							<a href="javascript:unblockProduct(<%=fumetti.get(i).getISBN()%>)">
								<img alt="block" id="unblock" src="images/unblock.png" width="22" height="22"/>
							</a>

							<a href="javascript:modifyProduct(<%=fumetti.get(i).getISBN()%>)">
								<img alt="modify" id="modify" src="images/modify.png" width="22" height="22"/>
							</a>

							<a href="javascript:deleteProduct(<%=fumetti.get(i).getISBN()%>)">
								<img alt="delete" id="delete" src="images/trashcan.png" width="22" height="22"/>
							</a>

							<% } %>
							</br>

							<section class="titolo"> <%=fumetti.get(i).getTitolo()%> - <%=fumetti.get(i).getNumero()%></section>
							<section class="autore"> <%=fumetti.get(i).getAutore()%></section>
							<section class="numero"> <%=fumetti.get(i).getPrezzo()%> Euro</section>
							<section class="additionalInfo"><%=fornitoDaArrayList.get(i).getCentroVendita().getNomeCentro()%></section>
							<section class="additionalInfo"><%=contenutoNelMagazzinoArrayList.get(i).getMagazzino().getNomeMagazzino()%></section>
						</article>
							<% } %>
					</section>

					<form name="insertProductForm" method="post" action="Dispatcher">
						<input type="hidden" name=""/>
						<input type="hidden" name="controllerAction" value="ProductsManagement.insertView"/>
					</form>
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
						<input type="hidden" name="modalitaRicerca"/>
						<input type="hidden" name="searchString"/>
						<input type="hidden" name="controllerAction" value="ProductsManagement.view"/>
					</form>


				</main>
				<%@include file="/include/footer.inc"%>
		</body>
</html>