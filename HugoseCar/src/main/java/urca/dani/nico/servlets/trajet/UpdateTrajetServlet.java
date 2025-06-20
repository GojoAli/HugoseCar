package urca.dani.nico.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/trajet/update")
public class UpdateTrajetServlet extends HttpServlet {
    private String urlDB, userDB, passDB;
    @Override
    public void init() throws ServletException { /* mêmes init */ }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // afficher le formulaire pré-rempli
        int id = Integer.parseInt(req.getParameter("id"));
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM trajet WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    req.setAttribute("trajet", rs);
                    req.getRequestDispatcher("/WEB-INF/updateTrajet.jsp").forward(req, resp);
                } else resp.sendRedirect(req.getContextPath() + "/trajet/list");
            }
        } catch (Exception e) {
            resp.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // mêmes validations que Create…
        int id = Integer.parseInt(req.getParameter("id"));
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE trajet SET driver_id=?, start_point=?, end_point=?, start_hour=?, end_hour=?, places_number=? WHERE id=?")) {
            // set les 6 premiers params + ps.setInt(7, id);
            ps.executeUpdate();
            resp.sendRedirect(req.getContextPath() + "/trajet/list");
        } catch (Exception e) {
            req.setAttribute("error", "Erreur de mise à jour.");
            req.getRequestDispatcher("/WEB-INF/updateTrajet.jsp").forward(req, resp);
        }
    }
}
