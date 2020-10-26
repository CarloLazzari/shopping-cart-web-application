<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Fumetto" %><%--
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
    String menuActiveLink = "View Details";

    Fumetto fumetto = (Fumetto) request.getAttribute("fumetto");


%>
<html>
<head>

  <%@include file="/include/htmlHead.inc"%>
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

        p {
            border: none;
            border-radius: 4px;
            padding: 3px;
            background-color: #e8eeef;
            color:#8a97a0;
            box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
        }

    </style>

    <script language="JavaScript">

        function goback() {
            document.backForm.submit();
        }

        function mainOnLoadHandler() {
            document.backForm.backButton.addEventListener("click", goback);

        }



    </script>

</head>
<body>
   <%@include file="/include/header.inc"%>
    <main>
        <section id="pageTitle">
            <h1>
                Fumetti: visualizza dettagli
            </h1>
        </section>
        <section id="Product">
                <div class="field clearfix">
                    <p> Titolo: <%=fumetto.getTitolo()%> </p>
                </div>
                <div class="field clearfix">
                    <p> Autore: <%=fumetto.getAutore()%> </p>
                </div>
                <div class="field clearfix">
                    <p> Numero: <%=fumetto.getNumero()%> </p>
                </div>
                <div class="field clearfix">
                    <p> Formato: <%=fumetto.getFormato()%> </p>
                </div>
                <div class="field clearfix">
                    <p> Rilegatura: <%=fumetto.getRilegatura()%> </p>
                </div>
                <div class="field clearfix">
                    <p> Prezzo: <%=fumetto.getPrezzo()%> Euro </p>
                </div>
                <div class="field clearfix">
                    <p> Peso: <%=fumetto.getPeso()%>kg </p>
                </div>
                <form id ="backForm">
                    <input type="button" value="Indietro" onclick="history.back()">
                </form>
        </section>

    </main>
<%@include file="/include/footer.inc"%>
</body>
</html>
