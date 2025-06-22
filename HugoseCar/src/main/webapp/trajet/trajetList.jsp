<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<!DOCTYPE html>
<html lang="fr">

<jsp:include page="../components/header.jsp">
    <jsp:param name="pageTitle" value="Liste des Trajets"/>
</jsp:include>

<%
    // Récupérer l'id du conducteur connecté depuis le cookie "userId"
    String userIdConnected = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("userId".equals(c.getName())) {
                userIdConnected = c.getValue();
                break;
            }
        }
    }
%>

<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div id="page-content">
        <h1 style="text-align:center;">Gestion des trajets</h1>
        <div class="top">
            <a href="trajet/createTrajet.jsp" class="skew_button_green">Nouveau trajet</a>
        </div>

        <%
            List<Object[]> trajets = (List<Object[]>) request.getAttribute("trajets");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        %>
        <table class="container">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Conducteur</th>
                    <th>Départ</th>
                    <th>Arrivée</th>
                    <th>Heure de départ</th>
                    <th>Places</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <% if (trajets != null && !trajets.isEmpty()) {
                   for (Object[] t : trajets) {
                       int id = (int) t[0];
                       int driverId = (int) t[1]; 
                       String driverName = (String) t[2];
                       String start = (String) t[3];
                       String end = (String) t[4];
                       LocalDateTime startHour = (LocalDateTime) t[5];
                       int places = (int) t[6];
                       boolean isOwner = userIdConnected != null && Integer.parseInt(userIdConnected) == driverId;
            %>
                <tr>
                    <td><%= id %></td>
                    <td><%= driverName %></td>
                    <td><%= start %></td>
                    <td><%= end %></td>
                    <td><%= startHour.format(formatter) %></td>
                    <td><%= places %></td>
                    <td class="actions">
                        <% if (isOwner) { %>
                        <a href="<%= request.getContextPath() %>/trajet?action=edit&trajet_id=<%= id %>" class="skew_button_blue">Modifier</a>
                        <% } %>
                        <form method="post" action="<%= request.getContextPath() %>/demande" style="display:inline">
                            <input type="hidden" name="action" value="create"/>
                            <input type="hidden" name="trajet_id" value="<%= id %>"/>
                            <button type="submit" class="skew_button_blue">Demander</button>
                        </form>
                        <% if (isOwner) { %>
                        <a href="<%= request.getContextPath() %>/demande?trajet_id=<%= id %>" class="skew_button_blue">Voir demandes</a>
                        <% } %>
                        <% if (isOwner) { %>
                        <form method="post" action="<%= request.getContextPath() %>/trajet" style="display:inline">
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="trajet_id" value="<%= id %>"/>
                            <button type="submit" class="skew_button_red">Supprimer</button>
                        </form>
                        <% } %>
                    </td>
                </tr>
            <%   }
               } else { %>
                <tr><td colspan="7" style="text-align:center;">Aucun trajet</td></tr>
            <% } %>
            </tbody>
        </table>

    </div>
</body>
</html>
