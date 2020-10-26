<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 27/09/2020
  Time: 20:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% int i;

	boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
	User loggedUser = (User) request.getAttribute("loggedUser");

	String applicationMessage = (String) request.getAttribute("applicationMessage");
	String menuActiveLink = "Users";

	ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
	ArrayList<Integer> usersOrdersCount = (ArrayList<Integer>) request.getAttribute("usersOrdersCount");


%>

<html>
	<head>
		<%@include file="/include/htmlHead.inc"%>
		<title>FumettiDB: <%=menuActiveLink%></title>

		<style>

			p>a:hover {
				 color:black;
				 text-decoration: underline;
			}

		</style>

		<script lang="JavaScript">

			function blockUser(Username){
				const f = document.blockUserForm;
				f.whichUserUsername.value = Username;
				f.submit();
			}

			function unblockUser(Username) {
				const f = document.unblockUserForm;
				f.whichUserUsername.value = Username;
				f.submit();
			}

			function viewOrders(Username) {
				const f = document.viewOrdersForm;
				f.whichUserUsername.value = Username;
				f.submit();
			}

			function mainOnLoadHandler(){
				 document.querySelector("#block").addEventListener("click",blockUser.bind(this));
				 document.querySelector("#unblock").addEventListener("click",unblockUser.bind(this));
				 document.querySelector("#viewOrders").addEventListener("click",viewOrders.bind(this))
			}

         function testBlock(Username) {
             try {
                 blockUser(Username);
             } catch (e) {
                 console.log(e);
             }
         }

         function testUnblock(Username) {
             try {
                 unblockUser(Username);
             } catch (e) {
                 console.log(e);
             }
         }

		</script>

	</head>

	<body>
	 	<%@include file="/include/header.inc"%>
		<main>
			<section id="pageTitle">
				<h1> Utenti </h1>
			</section>
			<% for(i=0; i<users.size(); i++) { %>
			<section id="users">
			   <p <%if(loggedUser.getUsername().equals(users.get(i).getUsername())){%> style="color: blueviolet" <% } %>>
					Username: <%=users.get(i).getUsername()%> -
					Name: <%=users.get(i).getFirstname()%> -
					Surname: <%=users.get(i).getSurname()%> -
					Email: <%=users.get(i).getEmail()%> -
					Date: <%=users.get(i).getData()%> -
					Address: <%=users.get(i).getIndirizzo()%> -
					Blocked: <%=users.get(i).getBlocked()%> -
					<%if(usersOrdersCount.size()>i){%>
					<a id="viewOrders" href="javascript:viewOrders(<%=users.get(i).getUsername()%>)">#ORDINI:</a> <%=usersOrdersCount.get(i)%>
					<% } else {%>
					#ORDINI: 0
					<% } %>

				</p>
				<% if(!(loggedUser.getUsername().equals(users.get(i).getUsername()))){ %>
					<% if(users.get(i).getBlocked().equals("N")) { %>
						<a href="javascript:blockUser(<%=users.get(i).getUsername()%>)">
							<img alt="block" id="block" src="images/block_icon.png" width="22" height="22"/>
						</a>
					<% } else { %>
						<a href="javascript:unblockUser(<%=users.get(i).getUsername()%>)">
							<img alt="unblock" id="unblock" src="images/unblock.png" width="22" height="22"/>
						</a>
					<% } %>
				<% } else {%>
				</br>
				<% } %>
			</section>
			</br>
		   <% } %>
			<br/>

			<form name="blockUserForm" method="post" action="Dispatcher">
				<input type="hidden" name="whichUserUsername"/>
				<input type="hidden" name="controllerAction" value="UsersManagement.block"/>
			</form>

			<form name="unblockUserForm" method="post" action="Dispatcher">
				<input type="hidden" name="whichUserUsername"/>
				<input type="hidden" name="controllerAction" value="UsersManagement.unblock"/>
			</form>

			<form name="viewOrdersForm" method="post" action="Dispatcher">
				<input type="hidden" name="whichUserUsername"/>
				<input type="hidden" name="controllerAction" value="OrdersManagement.view"/>
			</form>

		</main>
		<%@include file="/include/footer.inc"%>
	</body>
</html>