<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<jsp:include page="components/header.jsp">
    <jsp:param name="pageTitle" value="Connexion"/>
</jsp:include>

<body>
    <jsp:include page="components/navbar.jsp"></jsp:include>
    <div id="page-content">
        <h1>Connexion</h1>

        <form action="login" method="post" class="container">
            <div class="input_container">
                <label for="email">Email :</label>
                <input type="email" name="email" id="email" required>
            </div>

            <div class="input_container">
                <label for="password">Mot de passe :</label>
                <input type="password" name="password" id="password" required>
            </div>

            <input type="submit" class="skew_button_blue" value="Se connecter">

            <a href="register.jsp">S'inscrire sur le site</a>
            <%
                // Affichage d'un message d'erreur si les identifiants sont incorrects
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                    <p style="color:red;"><%= error %></p>
            <%
                }
            %>
        </form>
    </div>
</body>
</html>
