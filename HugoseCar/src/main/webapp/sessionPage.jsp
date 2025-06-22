<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<!DOCTYPE html>
<html>

<jsp:include page="components/header.jsp">
    <jsp:param name="pageTitle" value="Profil"/>
</jsp:include>

<body>
    <jsp:include page="components/navbar.jsp"></jsp:include>
    <div id="page-content">
        <%
            // Récupération du nom d'utilisateur stocké dans la session
            String email = (String) session.getAttribute("email");
            if(email != null) {
        %>
            <h1>Bonjour!</h1>
            <p>Vous êtes connecté.</p>
        <%
            } else {
        %>
            <h1>Accès non autorisé</h1>
            <p>Veuillez vous connecter <a href="login.jsp">ici</a>.</p>
        <%
            }
        %>

        <div class="nav">
            <a href="<%= request.getContextPath() %>/trajet">Voir la liste des trajets</a>
            <a href="<%= request.getContextPath() %>/trajet?action=create">Créer un trajet</a>
            <a href="<%= request.getContextPath() %>/logout">Déconnexion</a>
        </div>

    </div>

</body>
</html>