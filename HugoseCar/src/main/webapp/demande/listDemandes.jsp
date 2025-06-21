<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>

<!DOCTYPE html>
<html lang="fr">

<jsp:include page="../components/header.jsp">
    <jsp:param name="pageTitle" value="Demandes de Trajet"/>
</jsp:include>

<body>
  <jsp:include page="../components/navbar.jsp"></jsp:include>
  <h1>Demandes pour ce trajet</h1>

  <table>
    <thead>
      <tr>
        <th>Demandeur</th>
        <th>Message</th>
        <th>Statut</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <%
        List<Object[]> demandes = (List<Object[]>) request.getAttribute("demandes");
        if (demandes != null && !demandes.isEmpty()) {
          for (Object[] d : demandes) {
            int id = (int) d[0];
            String name = (String) d[1];
            String message = (String) d[2];
            String statut = (String) d[3];
      %>
      <tr>
        <td><%= name %></td>
        <td><%= message %></td>
        <td><%= statut %></td>
        <td class="actions">
          <% if ("EN_ATTENTE".equals(statut)) { %>
            <form method="post" action="<%= request.getContextPath() %>/demande">
              <input type="hidden" name="action" value="updateStatut"/>
              <input type="hidden" name="demande_id" value="<%= id %>"/>
              <input type="hidden" name="statut" value="ACCEPTE"/>
              <button type="submit">Accepter</button>
            </form>
            <form method="post" action="<%= request.getContextPath() %>/demande">
              <input type="hidden" name="action" value="updateStatut"/>
              <input type="hidden" name="demande_id" value="<%= id %>"/>
              <input type="hidden" name="statut" value="REFUSE"/>
              <button type="submit">Refuser</button>
            </form>
          <% } %>
        </td>
      </tr>
      <% }} else { %>
      <tr><td colspan="4">Aucune demande trouvée.</td></tr>
      <% } %>
    </tbody>
  </table>

  <p><a href="<%= request.getContextPath() %>/trajet">← Retour à la liste des trajets</a></p>
</body>
</html>
