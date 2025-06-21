<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
  
<jsp:include page="../components/header.jsp">
    <jsp:param name="pageTitle" value="Création d'un Trajet"/>
</jsp:include>

<body>
  <jsp:include page="../components/navbar.jsp"></jsp:include>
  <h1>Créer un trajet</h1>

  <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
  <% }
  else {%>

<div> you you <%= request.getAttribute("error") %> </div>

<% }

  %>

  <form action="<%= request.getContextPath() %>/trajet/create" method="post">
    <label>Point de départ *</label>
    <input type="text" name="start_point" required
           value="<%= request.getParameter("start_point") != null ? request.getParameter("start_point") : "" %>" />

    <label>Point d'arrivée *</label>
    <input type="text" name="end_point" required
           value="<%= request.getParameter("end_point") != null ? request.getParameter("end_point") : "" %>" />

    <label>Heure de départ *</label>
    <input type="datetime-local" name="start_hour" required
           value="<%= request.getParameter("start_hour") != null ? request.getParameter("start_hour") : "" %>" />

    <label>Heure d'arrivée *</label>
    <input type="datetime-local" name="end_hour" required
           value="<%= request.getParameter("end_hour") != null ? request.getParameter("end_hour") : "" %>" />

    <label>Places disponibles *</label>
    <input type="number" name="places_number" min="1" required
           value="<%= request.getParameter("places_number") != null ? request.getParameter("places_number") : "" %>" />

    <div class="actions">
      <button type="submit">Créer</button>
      <a href="<%= request.getContextPath() %>/sessionPage.jsp">Annuler</a>
    </div>
  </form>
</body>
</html>
