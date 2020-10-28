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
	String whichUserUsername = (String) request.getAttribute("whichUserUsername");

	ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
	ArrayList<Integer> usersOrdersCount = (ArrayList<Integer>) request.getAttribute("usersOrdersCount");


%>

<html>
	<head>
		<%@include file="/include/htmlHead.inc"%>
		<title>
			FumettiDB: <%=menuActiveLink%>
		</title>
		<style>

			th>a:hover {
				 color:black;
				 text-decoration: underline;
			}

         table {
             border-collapse: collapse;
             width: 100%;
				 table-layout: fixed;
         }

         td, th {
             border: 1px solid #dddddd;
             text-align: left;
				 width: 150px;
             padding: 8px;
         }

			#block, #unblock {
				 float: right;
			}

			#admin{
				 float: right;
			}

		</style>

		<script>
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
			<table id="users">
			   <tr <%if(users.get(i).getAdmin().equals("Y")){%> style="color: blueviolet" <% } %>>
					<th style="width: 100px" ><%=users.get(i).getUsername()%>
						<% if(!(loggedUser.getUsername().equals(users.get(i).getUsername()))){ %>
						<% if(users.get(i).getBlocked().equals("N")) { %>
						<span>
							<a href="javascript:blockUser('<%=users.get(i).getUsername()%>')">
								<img alt="block" id="block" src="images/block_icon.png" width="22" height="22"/>
							</a>
						</span>
						<% } else { %>
						<span>
							<a href="javascript:unblockUser('<%=users.get(i).getUsername()%>')">
								<img alt="unblock" id="unblock" src="images/unblock.png" width="22" height="22"/>
							</a>
						</span>
						<% } %>
						<% } %>
						<%if(users.get(i).getAdmin().equals("Y")){%>
							<img id="admin" alt="adminIcon" src="images/admin.png" width="20" height="20">
						<% } %>
					</th>
					<th style="width: 100px"><%=users.get(i).getFirstname()%></th>
					<th style="width: 100px"><%=users.get(i).getSurname()%></th>
					<th style="width: 150px"><%=users.get(i).getEmail()%></th>
					<th style="width: 50px" ><%=users.get(i).getData()%></th>
					<th style="width: 100px"  ><%=users.get(i).getIndirizzo()%></th>
					<th style="width: 10px" ><%=users.get(i).getBlocked()%></th>
					<th style="width: 50px"  ><a id="viewOrders" href="javascript:viewOrders('<%=users.get(i).getUsername()%>')">#ORDINI:</a> <%=usersOrdersCount.get(i)%></th>
				</tr>


			</table>

		   <% } %>

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
