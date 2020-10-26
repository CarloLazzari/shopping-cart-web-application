
<meta charset="utf-8"/>

<!-- Linking styles -->
<link rel="stylesheet" href="css/mainpage.css" type="text/css" media="screen">
<title>FumettiDB: <%=menuActiveLink%></title>
<script>

  let applicationMessage;
  <%if (applicationMessage != null) {%>
    applicationMessage="<%=applicationMessage%>";
  <%}%>
  function onLoadHandler() {
    <%if(!loggedOn) { %>
       headerOnLoadHandler();
    <% } %>
    try { mainOnLoadHandler(); } catch (e) {}
    if (applicationMessage!==undefined) alert(applicationMessage);
  }
  window.addEventListener("load", onLoadHandler);

</script>