<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html lang="fr">

<jsp:include page="../components/header.jsp">
    <jsp:param name="pageTitle" value="Modification de Trajet"/>
</jsp:include>

<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div id="page-content">

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

        <form action="<%= request.getContextPath() %>/trajet" method="post" class="container">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id" value="<%= id %>" />

            <div class="input_container">
                <label>Conducteur ID *</label>
                <input type="number" name="driver_id" required
                    value="<%= request.getParameter("driver_id") != null ? request.getParameter("driver_id") : driverId %>" disabled />
            </div>

            <div class="input_container">
                <label>Point de départ *</label>
                <input type="text" name="start_point" required
                    value="<%= request.getParameter("start_point") != null ? request.getParameter("start_point") : startPoint %>" />
            </div>

            <div class="input_container">            
                <label>Point d'arrivée *</label>
                <input type="text" name="end_point" required
                    value="<%= request.getParameter("end_point") != null ? request.getParameter("end_point") : endPoint %>" />
            </div>

            <div class="input_container">
                <label>Heure de départ *</label>
                <input type="datetime-local" name="start_hour" required
                    value="<%= request.getParameter("start_hour") != null ? request.getParameter("start_hour") : startHour.toString().replace(' ', 'T') %>" />
            </div>

            <div class="input_container">
                <label>Heure d'arrivée</label>
                <input type="datetime-local" name="end_hour"
                    value="<%= request.getParameter("end_hour") != null ? request.getParameter("end_hour") : "" %>" />
            </div>

            <div class="input_container">
                <label>Places disponibles *</label>
                <input type="number" name="places_number" min="1" required
                    value="<%= request.getParameter("places_number") != null ? request.getParameter("places_number") : placesNumber %>" />
            </div>

            <input type="submit" class="skew_button_blue" value="Mettre à jour"/>
            <a href="<%= request.getContextPath() %>/trajet" class="skew_button_red">Annuler</a>
        </form>

    </div>
</body>
</html>
