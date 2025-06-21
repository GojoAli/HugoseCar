package urca.dani.nico.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/trajet/create")
public class CreateTrajetServlet extends HttpServlet {
    private String urlDB;
    private String userDB;
    private String passDB;

    @Override
    public void init() throws ServletException {
        String host = System.getenv("DB_HOST");
        String db   = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");
        if (host == null || db == null || user == null || pass == null) {
            throw new ServletException("Variables d'environnement manquantes : DB_HOST, DB_NAME, DB_USER, DB_PASSWORD");
        }
        urlDB  = "jdbc:mysql://" + host + ":3306/" + db + "?serverTimezone=UTC";
        userDB = user;
        passDB = pass;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
     
        String start    = request.getParameter("start_point");
        String end      = request.getParameter("end_point");
        String startHr  = request.getParameter("start_hour");
        String endHr  = request.getParameter("end_hour");
        String places   = request.getParameter("places_number");


        // Récupération de l'ID conducteur depuis le cookie
        String driverId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("userId".equals(c.getName())) {
                    driverId = c.getValue();
                    break;
                }
            }
        }

        if (driverId == null || driverId.isBlank() || start == null || start.isBlank()
         || end == null || end.isBlank() || startHr == null || startHr.isBlank()
         || places == null || places.isBlank()) {
            request.setAttribute("error", "Tous les champs obligatoires doivent être remplis.");
            request.getRequestDispatcher("/trajet/createTrajet.jsp").forward(request, response);
            return;
        }

        String sql = "INSERT INTO trajet(driver_id, start_point, end_point, start_hour, end_hour, places_number) VALUES(?,?,?,?,?,?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, Integer.parseInt(driverId));
                ps.setString(2, start);
                ps.setString(3, end);

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime dt = LocalDateTime.parse(startHr, fmt);
                ps.setTimestamp(4, Timestamp.valueOf(dt));

                LocalDateTime dt2 = LocalDateTime.parse(endHr, fmt);
                ps.setTimestamp(5, Timestamp.valueOf(dt2));

                ps.setInt(6, Integer.parseInt(places));
                ps.executeUpdate();
                request.setAttribute("error", "ça marche.");
                //response.sendRedirect(request.getContextPath() + "/trajet/createTrajet.jsp");
                request.getRequestDispatcher("/trajet/createTrajet.jsp").forward(request, response);
                return;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de la création du trajet." + e.getMessage());
            request.getRequestDispatcher("/trajet/createTrajet.jsp").forward(request, response);
        }
    }
}