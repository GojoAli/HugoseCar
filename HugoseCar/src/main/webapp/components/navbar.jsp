<%
    // Récupération du nom d'utilisateur stocké dans la session
    String email = (String) session.getAttribute("email");
%>

<div class="nav">
    <a href="<%= request.getContextPath() %>/" class="left">
        <img src="<%= request.getContextPath() %>/img/logo.png" alt="HugoseCar Logo" class="logo">
        <span style="display: flex; justify-content: center; align-items: center; font-size: 18px; font-weight: bold;">HugoseCar</span>
    </a>
    <div class="right">
        <% if (email == null) { %>
        <a href="login.jsp">Connexion</a>
        <a href="register.jsp">S'inscrire</a>
        <%
            } else {
        %>
        <a href="sessionPage.jsp">Profil</a>
        <a href="<%= request.getContextPath() %>/logout">Deconnexion</a>
        <%
            }
        %>
    </div>
</div>
<div>