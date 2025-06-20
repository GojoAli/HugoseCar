<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="urca.dani.nico.models.Trajet" %>
<%@ page import="java.util.List" %>
<%
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des trajets</title>
</head>
<body>
<h1>Liste des trajets</h1>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Départ</th>
        <th>Arrivée</th>
        <th>Date départ</th>
        <th>Date arrivée</th>
        <th>Places</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="t" items="${trajets}">
        <tr>
            <td>${t.id}</td>
            <td>${t.startPoint}</td>
            <td>${t.endPoint}</td>
            <td>${t.startHour}</td>
            <td>${t.endHour}</td>
            <td>${t.placesNumber}</td>
            <td>
                <a href="trajet?action=edit&id=${t.id}">Modifier</a>
                <form method="post" action="trajet" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${t.id}">
                    <input type="submit" value="Supprimer">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<h2>Ajouter un trajet</h2>
<form method="post" action="trajet">
    <label>Conducteur ID: <input type="number" name="driver_id" required></label><br>
    <label>Départ: <input type="text" name="start_point" required></label><br>
    <label>Arrivée: <input type="text" name="end_point" required></label><br>
    <label>Heure de départ: <input type="datetime-local" name="start_hour" required></label><br>
    <label>Heure d’arrivée: <input type="datetime-local" name="end_hour"></label><br>
    <label>Places: <input type="number" name="places_number" required></label><br>
    <input type="submit" value="Créer">
</form>

</body>
</html>
