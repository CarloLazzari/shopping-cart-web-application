<%@ page import="model.mo.Ordine" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<% int i = 0;

  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  User loggedUser = (User) request.getAttribute("loggedUser");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
  String menuActiveLink = "Ordini";
  String whichUserUsername = (String) request.getAttribute("whichUserUsername");

  List<Ordine> ordini = (List<Ordine>) request.getAttribute("ordini");
  List<Float> prices = (List<Float>) request.getAttribute("prices");

%>

<!DOCTYPE html>
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

			#changeState {
				 position: relative;
			}

			#changeStateImage, #viewDetailsImage {
				 float: right;
			}

			#changeStateImage {
				 padding-top: 0px;
				 margin-top: 2px;
			}

		</style>

		<script lang="javascript">

			function changeState(Stato,OrderID){
			    const f = document.changeStateForm;
             f.STATO.value = Stato;
			    f.ORDER_ID.value = OrderID;
			    f.submit();
			}

			function viewDetails(OrderID) {
			    const f = document.viewDetailsForm;
			    f.ORDER_ID.value = OrderID;
			    f.submit();
			}

		</script>

	</head>
		<body>
			<%@include file="/include/header.inc"%>
			<main>
				<section id="pageTitle">
					<% if(loggedUser!=null){%>
					<h3>
						<%if((loggedOn)&&(loggedUser.getAdmin().equals(("Y")))&&(!(whichUserUsername.equals("")))){ %>
							<%if(prices.size()==0) {%>
								Non sono ancora stati effettuati ordini dall'utente <%=whichUserUsername%>.
							<% } else { %>
								Ordini effettuati dall'utente <%=whichUserUsername%>.
							<% } %>
						<% } else if((loggedOn)&&(loggedUser.getAdmin().equals(("Y")))){%>
						Ordini effettuati da tutti gli utenti.
						<% } else if(loggedOn) { %>
						Ordini effettuati dall'utente <%=loggedUser.getUsername()%>.
						<% } %>
					</h3>
					<% } %>
				</section>

				<%if((loggedOn)&&(loggedUser!=null)&&(loggedUser.getAdmin().equals("Y"))&&(prices.size()!=0)){%>
				<label for="changeState">Seleziona lo stato che vuoi assegnare all'ordine: </label>
					<select id="changeState">
						<option value="In preparazione">In preparazione</option>
						<option value="Spedito">Spedito</option>
						<option value="Completato">Completato</option>
					</select>
				<% } %>

				<script lang="javascript">

                function search(){
                    let s = document.getElementById("searchString").value;
                    const f = document.viewUsersOrdersForm;
                    f.whichUserUsername.value = s;
                    f.submit();
                }

				</script>

				<section>
					<label>Digitare il nome dell'utente di cui si vogliono visualizzare gli ordini:</label>
					<label for="searchString">
						<input type="text" id="searchString" name="searchString" maxlength="20">
					</label>
					<a onclick="window.search()">
						<img style="margin-top: -5px" alt="search" id="searchImage" src="images/search.png" width="22" height="22">
					</a>
				</section>

				</br>
				</br>

				<%if(ordini.size()>0){%>
				<table>
					<tr>
						<th style="color:#ffbc00">ID Ordine</th>
						<th style="color:#ffbc00">Data</th>
						<th style="color:#ffbc00">Indirizzo Spedizione</th>
						<th style="color:#ffbc00">Effettuante</th>
						<th style="color:#ffbc00">Metodo pagamento</th>
						<th style="color:#ffbc00">Stato</th>
						<th style="color:#ffbc00">Prezzo</th>
					</tr>
				</table>
				<%}%>

				<% for( i=0; i<ordini.size(); i++) { %>
				<table>
					<tr>
						<th><%=ordini.get(i).getOrderID()%>
						<% if((loggedOn)&&(Objects.requireNonNull(loggedUser).getAdmin().equals(("Y")))){%>
						<script>
							 var x = document.getElementById("changeState");
						</script>
						<span style="margin-top: 2px">
							<a href="javascript:changeState(x.value,<%=ordini.get(i).getOrderID()%>)">
								<img alt="changeState" id="changeStateImage" src="images/pencil.svg" width="18" height="18">
							</a>
						</span>
							<% } %>

						<span style="margin-top: 2px">
							<a href="javascript:viewDetails(<%=ordini.get(i).getOrderID()%>)">
								<img alt="viewDetails" id="viewDetailsImage" src="images/view_details.png" width="22" height="22">
							</a>
						</span>
						</th>
						<th><%=ordini.get(i).getData()%></th>
						<th><%=ordini.get(i).getIndirizzoDestinazione()%></th>
						<th><%=ordini.get(i).getUser().getUsername()%></th>
						<th><%=ordini.get(i).getCarta().getNumeroCarta()%></th>
						<th><%=ordini.get(i).getStato()%></th>
						<th><%=prices.get(i)%> Euro</th>
					</tr>

				</table>
				<%	} %>

				<div class="clearfix"></div>

				</br>
				<section>
					<form name="changeStateForm" method="post" action="Dispatcher">
						<input type="hidden" name="ORDER_ID"/>
						<input type="hidden" name="STATO"/>
						<%if(whichUserUsername!=null) { %>
						<input type="hidden" name="whichUserUsername" value="<%=whichUserUsername%>"/>
						<% } %>
						<input type="hidden" name="controllerAction" value="OrdersManagement.modifyStatus"/>
					</form>
					<form name="viewDetailsForm" method="post" action="Dispatcher">
						<input type="hidden" name="ORDER_ID"/>
						<input type="hidden" name="controllerAction" value="OrdersManagement.viewDetails"/>
					</form>
					<form name="viewUsersOrdersForm" method="post" action="Dispatcher">
						<input type="hidden" name="whichUserUsername"/>
						<input type="hidden" name="controllerAction" value="OrdersManagement.view"/>
					</form>
				</section>

			</main>

		<%@include file="/include/footer.inc"%>
	</body>
</html>
