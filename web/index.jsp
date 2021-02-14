
<%--
  Created by IntelliJ IDEA.
  User: Carlo
  Date: 17/09/2020
  Time: 00:25
  To change this template use File | Settings | File Templates.
--%>
<%@page session="false"%>

<%--
  request.getContextPath()- returns root path of your application, while ../ - returns parent directory of a file.
--%>

<%String contextPath=request.getContextPath();%>
<!DOCTYPE HTML>
<html lang="it-IT">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="refresh" content="0; url=<%=contextPath%>/Dispatcher">
	<script type="text/javascript">
       function onLoadHandler() {
           window.location.href = "<%=contextPath%>/Dispatcher";
       }
       window.addEventListener("load", onLoadHandler);
	</script>
	<title>Page Redirection</title>
</head>
<body>
If you are not redirected automatically, follow the <a href='<%=contextPath%>/Dispatcher'>link</a>
</body>
</html>
