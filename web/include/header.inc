
<script>
    function headerOnLoadHandler() {
        const usernameTextField = document.querySelector("#username");
        const usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
        const passwordTextField = document.querySelector("#password");
        const passwordTextFieldMsg = "La password \xE8 obbligatoria.";

        if (usernameTextField !== undefined && passwordTextField !== undefined ) {
            usernameTextField.setCustomValidity(usernameTextFieldMsg);
            usernameTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
            });
            passwordTextField.setCustomValidity(passwordTextFieldMsg);
            passwordTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
            });
        }
    }
</script>

<style>

    #logout {
        margin-left: 5px;
        margin-bottom: 10px;
    }

    #fumetti {
        margin-top: 30px;
        margin-left: 80px;
    }

</style>

<header class="clearfix">

    <h1 class="logo">
        FumettiDB  <%if(loggedOn){ %>
        <h3 style="color:white; padding-left: 15px; ">Utente loggato: <%=loggedUser.getUsername()%></h3>
        <% } %>
    </h1>


    <form name ="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
    </form>

    <nav>
        <ul>
            <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                <a href = "Dispatcher?controllerAction=HomeManagement.view">Home</a>
            </li>

            <li <%=menuActiveLink.equals("Prodotti")?"class=\"active\"":""%>>
            <a href = "Dispatcher?controllerAction=ProductsManagement.view">Prodotti</a>
            </li>

            <%if(loggedOn){%>
            <li <%=menuActiveLink.equals("Carrello")?"class=\"active\"":""%>>
            <a href = "Dispatcher?controllerAction=CartManagement.view">Carrello</a>
            </li>
            <% } %>

            <%if(loggedOn){%>
            <li <%=menuActiveLink.equals("Ordini")?"class=\"active\"":""%>>
            <a href = "Dispatcher?controllerAction=OrdersManagement.view">Ordini</a>
            </li>
            <% } %>

            <%if(loggedOn){%>
            <li <%=menuActiveLink.equals("Compra")?"class=\"active\"":""%>>
            <a href = "Dispatcher?controllerAction=ConfirmOrderManagement.view">Compra</a>
            </li>
            <% } %>

            <%if((loggedOn)&&(loggedUser.getAdmin().equals("Y"))){%>
            <li <%=menuActiveLink.equals("Utenti")?"class=\"active\"":""%>>
            <a href = "Dispatcher?controllerAction=UsersManagement.view">Utenti</a>
            </li>
            <% } %>
        </ul>
        <a id ="logout" href = "javascript:logoutForm.submit()">Logout </a>
        <br/>
    </nav>

    <%if(!loggedOn){%>
        <section id="login" class = "clearfix">
            <form name="logonForm" action="Dispatcher" method="post">
                <label for="username">Username</label>
                <input type="text" id="username" name="USERNAME" max="20" required>
                <label for="password">Password</label>
                <input type="password" id="password" name="PASSWORD" maxlength="20" required>
                <input type="hidden" name="controllerAction" value="HomeManagement.logon"/>
                <input type="submit" value="OK">
            </form>
        </section>
    <%}%>

</header>