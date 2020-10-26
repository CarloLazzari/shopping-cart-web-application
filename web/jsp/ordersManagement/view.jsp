<%@ page import="model.mo.Ordine" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>


<%

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
			Ordini
		</title>
		<style>


			#changeState {
				 position: relative;
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
						<%if((loggedOn)&&(loggedUser.getAdmin().equals(("Y")))&&(whichUserUsername!=null)) { %>
						Ordini effettuati dall'utente <%=whichUserUsername%>.
						<% } else if((loggedOn)&&(loggedUser.getAdmin().equals(("Y")))){%>
						Ordini effettuati da tutti gli utenti.
						<%} else if(loggedOn) {%>
						Ordini effettuati dall'utente <%=loggedUser.getUsername()%>.
						<%} %>
					</h3>
					<% } %>
				</section>
				</br>
					<% for(int i=0; i<ordini.size(); i++) { %>
						<section>
							<p>
								<%=ordini.get(i).getOrderID()%> -
								<%=ordini.get(i).getData()%> -
								<%=ordini.get(i).getIndirizzoDestinazione()%> -
								<%=ordini.get(i).getUser().getUsername()%> -
								<%=ordini.get(i).getCarta().getNumeroCarta()%> -
								<%=ordini.get(i).getStato()%> -
								(Prezzo totale = <%=prices.get(i)%> Euro)
							</p>
							<% if((loggedOn)&&(Objects.requireNonNull(loggedUser).getAdmin().equals(("Y")))){%>

							<label for="changeState">
								<select id="changeState">
									<option value="In preparazione">In preparazione</option>
									<option value="Spedito">Spedito</option>
									<option value="Completato">Completato</option>
								</select>
							</label>

							<script>
								var x = document.getElementById("changeState");
							</script>
							<span style="margin-top: 2px">
							<a href="javascript:changeState(x.value,<%=ordini.get(i).getOrderID()%>)">
								<img alt="changeState" src="images/edit_icon.png" width="22" height="22">
							</a>
							</span>
							<% } %>

							<span style="margin-top: 2px">
							<a href="javascript:viewDetails(<%=ordini.get(i).getOrderID()%>)">
								<img alt="viewDetails" src="images/view_details.png" width="22" height="22">
							</a>
							</span>
						</section>
						<br/>


					<%	} %>
					<div class="clearfix"></div>

					</br>
					<section>
						<form name="changeStateForm" method="post" action="Dispatcher">
							<input type="hidden" name="ORDER_ID"/>
							<input type="hidden" name="STATO"/>
							<input type="hidden" name="controllerAction" value="OrdersManagement.modifyStatus"/>
						</form>
						<form name="viewDetailsForm" method="post" action="Dispatcher">
							<input type="hidden" name="ORDER_ID"/>
							<input type="hidden" name="controllerAction" value="OrdersManagement.viewDetails"/>
						</form>
					</section>

			</main>

		<%@include file="/include/footer.inc"%>
	</body>
</html>