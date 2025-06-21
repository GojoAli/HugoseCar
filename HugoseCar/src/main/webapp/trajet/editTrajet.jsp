<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html lang="fr">

<jsp:include page="../components/header.jsp">
    <jsp:param name="pageTitle" value="Modification de Trajet"/>
</jsp:include>

<body>
       <jsp:include page="../components/navbar.jsp"></jsp:include>
  <h1 style="text-align:center;">Modifier le trajet</h1>

  <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
  <% } %>

  <%
    Object[] trajet = (Object[]) request.getAttribute("trajet");
    int id = (Integer) trajet[0];
    int driverId = (Integer) trajet[1];
    String startPoint = (String) trajet[2];
    String endPoint = (String) trajet[3];
    LocalDateTime startHour = (LocalDateTime) trajet[4];
    int placesNumber = (Integer) trajet[5];
  %>

  <form action="<%= request.getContextPath() %>/trajet" method="post">
    <input type="hidden" name="action" value="update" />
    <input type="hidden" name="id" value="<%= id %>" />

    <label>Conducteur ID *</label>
    <input type="number" name="driver_id" required
           value="<%= request.getParameter("driver_id") != null ? request.getParameter("driver_id") : driverId %>" disabled />

    <label>Point de départ *</label>
    <input type="text" name="start_point" required
           value="<%= request.getParameter("start_point") != null ? request.getParameter("start_point") : startPoint %>" />

    <label>Point d'arrivée *</label>
    <input type="text" name="end_point" required
           value="<%= request.getParameter("end_point") != null ? request.getParameter("end_point") : endPoint %>" />

    <label>Heure de départ *</label>
    <input type="datetime-local" name="start_hour" required
           value="<%= request.getParameter("start_hour") != null ? request.getParameter("start_hour") : startHour.toString().replace(' ', 'T') %>" />

    <label>Heure d'arrivée</label>
    <input type="datetime-local" name="end_hour"
           value="<%= request.getParameter("end_hour") != null ? request.getParameter("end_hour") : "" %>" />

    <label>Places disponibles *</label>
    <input type="number" name="places_number" min="1" required
           value="<%= request.getParameter("places_number") != null ? request.getParameter("places_number") : placesNumber %>" />

    <div class="actions">
      <button type="submit">Mettre à jour</button>
      <a href="<%= request.getContextPath() %>/trajet">Annuler</a>
    </div>
  </form>
</body>
</html>
