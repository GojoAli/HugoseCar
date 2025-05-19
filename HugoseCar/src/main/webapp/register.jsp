<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>Inscription</title>
  <style>
    .error { color: red; margin-bottom: 1em; }
    form { max-width: 400px; margin: auto; }
    label { display: block; margin-top: 0.5em; }
    input { width: 100%; padding: 0.4em; box-sizing: border-box; }
    .actions { margin-top: 1em; }
  </style>
</head>
<body>
  <h1>Créer un compte</h1>

  <!-- Message d'erreur -->
  <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
  <% } %>

  <form action="<%= request.getContextPath() %>/register" method="post">
    <label for="name">Nom d'utilisateur *</label>
    <input type="text" id="name" name="name"
           value="<%= request.getParameter("name") != null ? request.getParameter("name") : "" %>"
           required />

    <label for="email">Email *</label>
    <input type="email" id="email" name="email"
           value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
           required />

    <label for="phone_number">Numéro de téléphone</label>
    <input type="tel" id="phone_number" name="phone_number"
           value="<%= request.getParameter("phone_number") != null ? request.getParameter("phone_number") : "" %>" />

    <label for="password">Mot de passe *</label>
    <input type="password" id="password" name="password" required />

    <div class="actions">
      <button type="submit">S’inscrire</button>
      <a href="<%= request.getContextPath() %>/login.jsp">Retour à la connexion</a>
    </div>
  </form>
</body>
</html>
