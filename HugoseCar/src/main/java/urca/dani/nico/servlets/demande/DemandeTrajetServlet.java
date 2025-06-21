package urca.dani.nico.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/demande")
public class DemandeTrajetServlet extends HttpServlet {
    private String urlDB, userDB, passDB;

    @Override
    public void init() throws ServletException {
        String host = System.getenv("DB_HOST");
        String db = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");
        if (host == null || db == null || user == null || pass == null) {
            throw new ServletException("Variables d'environnement manquantes.");
        }
        urlDB = "jdbc:mysql://" + host + ":3306/" + db + "?serverTimezone=UTC";
        userDB = user;
        passDB = pass;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String trajetIdStr = req.getParameter("trajet_id");
        if (trajetIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/trajet");
            return;
        }

        int trajetId = Integer.parseInt(trajetIdStr);
        List<Object[]> demandes = new ArrayList<>();
        String sql = "SELECT d.id, u.name, d.message, d.statut FROM demande_trajet d JOIN users u ON d.user_id = u.id WHERE d.trajet_id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, trajetId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = new Object[4];
                        row[0] = rs.getInt("id");
                        row[1] = rs.getString("name");
                        row[2] = rs.getString("message");
                        row[3] = rs.getString("statut");
                        demandes.add(row);
                    }
                }
            }
            req.setAttribute("demandes", demandes);
            req.getRequestDispatcher("/demande/listDemandes.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "create":
                    createDemande(req);
                    break;
                case "cancel":
                    cancelDemande(req);
                    break;
                case "updateStatut":
                    updateStatut(req);
                    break;
            }
            resp.sendRedirect(req.getContextPath() + "/trajet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void createDemande(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String trajetIdStr = req.getParameter("trajet_id");
        String message = req.getParameter("message");

        String userId = null;
        for (Cookie c : req.getCookies()) {
            if ("userId".equals(c.getName())) {
                userId = c.getValue();
                break;
            }
        }
        if (trajetIdStr == null || userId == null) return;

        int trajetId = Integer.parseInt(trajetIdStr);
        String sql = "INSERT INTO demande_trajet (trajet_id, user_id, message) VALUES (?, ?, ?)";

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trajetId);
            ps.setInt(2, Integer.parseInt(userId));
            ps.setString(3, message != null ? message : "");
            ps.executeUpdate();
        }
    }

    private void cancelDemande(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String demandeIdStr = req.getParameter("demande_id");
        if (demandeIdStr == null) return;

        int demandeId = Integer.parseInt(demandeIdStr);
        String sql = "DELETE FROM demande_trajet WHERE id=?";

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, demandeId);
            ps.executeUpdate();
        }
    }

    private void updateStatut(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String demandeIdStr = req.getParameter("demande_id");
        String statut = req.getParameter("statut");
        if (demandeIdStr == null || statut == null) return;

        int demandeId = Integer.parseInt(demandeIdStr);
        String sql = "UPDATE demande_trajet SET statut=? WHERE id=?";

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, demandeId);
            ps.executeUpdate();
        }
    }
}
