<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="urca.dani.nico.models.Trajet" %>
<%
    Trajet t = (Trajet) request.getAttribute("trajet");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Modifier un trajet</title>
</head>
<body>
<h1>Modifier le trajet</h1>

<form method="post" action="trajet">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="<%= t.getId() %>">

    <label>Conducteur ID: <input type="number" name="driver_id" value="<%= t.getDriverId() %>" required></label><br>
    <label>Départ: <input type="text" name="start_point" value="<%= t.getStartPoint() %>" required></label><br>
    <label>Arrivée: <input type="text" name="end_point" value="<%= t.getEndPoint() %>" required></label><br>
    <label>Heure de départ:
        <input type="datetime-local" name="start_hour"
               value="<%= t.getStartHour().toString().replace(" ", "T") %>" required>
    </label><br>
    <label>Heure d’arrivée:
        <input type="datetime-local" name="end_hour"
               value="<%= (t.getEndHour() != null) ? t.getEndHour().toString().replace(" ", "T") : "" %>">
    </label><br>
    <label>Places: <input type="number" name="places_number" value="<%= t.getPlacesNumber() %>" required></label><br>

    <input type="submit" value="Mettre à jour">
</form>

<a href="trajet">← Retour à la liste</a>
</body>
</html>
