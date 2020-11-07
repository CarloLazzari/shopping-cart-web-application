<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Magazzino" %>
<%@ page import="java.util.List" %>
<%@ page import="model.mo.CentroVendita" %><%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 06/11/2020
  Time: 19:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	int i = 0;
	int j = 0;

	String menuActiveLink = "Centri e Magazzini";
	boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
	User loggedUser = (User) request.getAttribute("loggedUser");

	String applicationMessage = (String) request.getAttribute("applicationMessage");

	List<Magazzino> warehouses = (List<Magazzino>) request.getAttribute("warehouses");
	List<CentroVendita> centres = (List<CentroVendita>) request.getAttribute("centres");


%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="/include/htmlHead.inc"%>
	<title><%=menuActiveLink%>></title>
	<style>
       table {
           border-collapse: collapse;
           table-layout: fixed;
       }

       td, th {
           border: 1px solid #dddddd;
           text-align: left;
           padding: 8px;
           width: 426px;
       }
		 #modifyWarehouse, #modifySellingCentre {
			float: right;
		 }

	</style>
	<script>
       function insertWarehouse(){
           document.newWarehouseForm.submit();
       }

       function insertSellingCentre(){
           document.newSellingCentreForm.submit();
       }

       function modifyWarehouse(nomeMagazzino){
           const f = document.modifyWarehouseForm;
           f.nomeMagazzino.value = nomeMagazzino;
           f.submit();
		 }

       function modifySellingCentre(nomeFornitore){
           const f = document.modifyWarehouseForm;
           f.nomeFornitore.value = nomeFornitore;
           f.submit();
       }
	</script>
</head>

<body>

	<%@include file="/include/header.inc"%>
	<main>
		<section>
			<h3>Magazzini</h3>
		</section>
		<br>

		<%if(warehouses.size()>0){ %>
		<table>
			<tr>
				<th style="color:#ffbc00">Nome Magazzino</th>
				<th style="color:#ffbc00">Centro Vendita di Appartenenza</th>
				<th style="color:#ffbc00">Indirizzo</th>
			</tr>
		</table>
		<%for(i=0; i<warehouses.size(); i++){%>
		<table>
			<tr>
				<th><%=warehouses.get(i).getNomeMagazzino()%>
					<a href="javascript:modifyWarehouse('<%=warehouses.get(i).getNomeMagazzino()%>')">
						<img id="modifyWarehouse" alt="modifyWarehouse" src="images/modify.png" width="22" height="22">
					</a>
				</th>
				<th><%=warehouses.get(i).getCentroVendita().getNomeCentro()%></th>
				<th><%=warehouses.get(i).getIndirizzo()%></th>
			</tr>
		</table>
		<% } %>
		<% } %>
		<br>
		<a href="javascript:insertWarehouse()">
			<img alt="addWarehouse" src="images/add_warehouse.png" width="30" height="30">
		</a>
		<br>
		<br>
	</main>
	<main>
		<section>
			<h3>Centri Vendita</h3>
		</section>
		<br>

		<%if(centres.size()>0){%>
		<table>
			<tr>
				<th style="color:#ffbc00">Nome Centro</th>
				<th style="color:#ffbc00">Indirizzo</th>
			</tr>
		</table>
		<%for(j=0; j<centres.size(); j++){%>
		<table>
			<tr>
				<th><%=centres.get(j).getNomeCentro()%>
					<a href="javascript:modifySellingCentre('<%=centres.get(j).getNomeCentro()%>')">
						<img id="modifySellingCentre" alt="modifySellingCentre" src="images/modify.png" width="22" height="22">
					</a>
				</th>
				<th><%=centres.get(j).getIndirizzo()%></th>
			</tr>
		</table>
		<% } %>
		<% } %>
		<br>
		<a href="javascript:insertSellingCentre()">
			<img alt="addCentre" src="images/add_centre.webp" width="36" height="36">
		</a>
		<br>
		<br>
	</main>

	<section>
		<form name="newWarehouseForm" method="post" action="Dispatcher">
			<input type="hidden" name="whichInsertMode" value="M"/>
			<input type="hidden" name="controllerAction" value="FacilitiesManagement.insertView">
		</form>
		<form name="newSellingCentreForm" method="post" action="Dispatcher">
			<input type="hidden" name="whichInsertMode" value="C"/>
			<input type="hidden" name="controllerAction" value="FacilitiesManagement.insertView">
		</form>
		<form name="modifyWarehouseForm" method="post" action="Dispatcher">
			<input type="hidden" name="whichModifyMode" value="M"/>
			<input type="hidden" name="nomeMagazzino"/>
			<input type="hidden" name="controllerAction" value="FacilitiesManagement.modifyView"/>
		</form>
		<form name="modifySellingCentreForm" method="post" action="Dispatcher">
			<input type="hidden" name="whichModifyMode" value="C"/>
			<input type="hidden" name="nomeFornitore"/>
			<input type="hidden" name="controllerAction" value="FacilitiesManagement.modifyView"/>
		</form>
	</section>
	<%@include file="/include/footer.inc"%>
</body>
</html>
