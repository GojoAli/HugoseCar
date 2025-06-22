<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
  
<jsp:include page="../components/header.jsp">
    <jsp:param name="pageTitle" value="Création d'un Trajet"/>
</jsp:include>

<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>
    <div id="page-content">
        <h1>Créer un trajet</h1>


        <form action="<%= request.getContextPath() %>/trajet/create" method="post" class="container">
            <div class="input_container">
                <label>Point de départ *</label>
                <input type="text" name="start_point" required
                        value="<%= request.getParameter("start_point") != null ? request.getParameter("start_point") : "" %>" />
            </div>

            <div class="input_container">
                <label>Point d'arrivée *</label>
                <input type="text" name="end_point" required
                        value="<%= request.getParameter("end_point") != null ? request.getParameter("end_point") : "" %>" />
            </div>

            <div class="input_container">
                <label>Heure de départ *</label>
                <input type="datetime-local" name="start_hour" required
                        value="<%= request.getParameter("start_hour") != null ? request.getParameter("start_hour") : "" %>" />
            </div>

            <div class="input_container">
                <label>Heure d'arrivée *</label>
                <input type="datetime-local" name="end_hour" required
                    value="<%= request.getParameter("end_hour") != null ? request.getParameter("end_hour") : "" %>" />
            </div>
            
            <div class="input_container">
                <label>Places disponibles *</label>
                <input type="number" name="places_number" min="1" required
                        value="<%= request.getParameter("places_number") != null ? request.getParameter("places_number") : "" %>" />
            </div>

            <input type="submit" class="skew_button_blue" value="Créer le trajet" />
            <a href="<%= request.getContextPath() %>/sessionPage.jsp">Annuler</a>
        </form>
    </div>
</body>
</html>
