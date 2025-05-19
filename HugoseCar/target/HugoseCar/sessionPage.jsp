<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Page de session</title>
</head>
<body>
<%
    // Récupération du nom d'utilisateur stocké dans la session
    String username = (String) session.getAttribute("username");
    if(username != null) {
%>
    <h1>Bonjour, <%= username %> !</h1>
    <p>Vous êtes connecté.</p>
<%
    } else {
%>
    <h1>Accès non autorisé</h1>
    <p>Veuillez vous connecter <a href="login.jsp">ici</a>.</p>
<%
    }
%>
</body>
</html>