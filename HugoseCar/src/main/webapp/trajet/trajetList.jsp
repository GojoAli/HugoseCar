<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="fr">

<jsp:include page="../components/header.jsp">
    <jsp:param name="pageTitle" value="Liste des Trajets"/>
</jsp:include>

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
            <tr>
            <th>ID</th><th>Conducteur</th><th>Départ</th><th>Arrivée</th><th>Heure de départ</th><th>Places</th><th>Actions</th>
            </tr>
            <%
            if (trajets != null && !trajets.isEmpty()) {
                for (Object[] t : trajets) {
            %>
            <tr>
            <td><%= t[0] %></td>
            <td><%= t[1] %></td>
            <td><%= t[2] %></td>
            <td><%= t[3] %></td>
            <td><%= ((LocalDateTime) t[4]).format(formatter) %></td>
            <td><%= t[5] %></td>
            <td class="actions">
                <a href="<%=request.getContextPath()%>/trajet?action=edit&id=<%= t[0] %>" class="skew_button_blue">Modifier</a>

                <form method="post" action="<%=request.getContextPath()%>/demande">
                    <input type="hidden" name="action" value="create"/>
                    <input type="hidden" name="trajet_id" value="<%= t[0] %>"/>  
                    <input type="submit" class="skew_button_blue" value="Faire une demande de trajet"/>
                </form>
                <a href="<%=request.getContextPath()%>/demande?trajet_id=<%= t[0] %>" class="skew_button_blue">List de demandes</a>
            
                <form method="post" action="<%=request.getContextPath()%>/trajet">
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="id" value="<%= t[0] %>"/>  
                    <input type="submit" class="skew_button_red" value="Supprimer"/>
                </form>
            </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr><td colspan="7" style="text-align:center;">Aucun trajet</td></tr>
            <% } %>
        </table>

    </div>
</body>
</html>
