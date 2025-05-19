<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
</head>
<body>
    <h1>Connexion</h1>
    <!-- Formulaire de connexion -->
    <form action="login" method="post">
        <label for="email">Email :</label>
        <input type="email" name="email" id="email" required>
        <br/>
        <label for="password">Mot de passe :</label>
        <input type="password" name="password" id="password" required>
        <br/>
        <input type="submit" value="Se connecter">
    </form>

    <p><a href="register.jsp">S'inscrire sur le site</a></p>
    <%
        // Affichage d'un message d'erreur si les identifiants sont incorrects
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
            <p style="color:red;"><%= error %></p>
    <%
        }
    %>
</body>
</html>
