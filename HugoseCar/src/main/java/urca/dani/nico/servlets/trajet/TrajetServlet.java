package urca.dani.nico.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/trajet")
public class TrajetServlet extends HttpServlet {
    private String urlDB, userDB, passDB;

    @Override
    public void init() throws ServletException {
        String host = System.getenv("DB_HOST");
        String db   = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");
        if (host==null||db==null||user==null||pass==null) {
            throw new ServletException("Variables d'environnement manquantes : DB_HOST, DB_NAME, DB_USER, DB_PASSWORD");
        }
        urlDB  = "jdbc:mysql://" + host + ":3306/" + db + "?serverTimezone=UTC";
        userDB = user;
        passDB = pass;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            forwardEdit(req, resp);
        } else {
            forwardList(req, resp);
        }
    }

    private void forwardList(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    List<Object[]> trajets = new ArrayList<>();
    // On joint users pour récupérer name
    String sql = "SELECT t.id, t.driver_id, u.name AS driver_name, "
               + "t.start_point, t.end_point, t.start_hour, t.places_number "
               + "FROM trajet t "
               + "JOIN users u ON t.driver_id = u.id";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getInt("id");
                row[1] = rs.getInt("driver_id");
                row[2] = rs.getString("driver_name");    
                row[3] = rs.getString("start_point");
                row[4] = rs.getString("end_point");
                row[5] = rs.getTimestamp("start_hour").toLocalDateTime();
                row[6] = rs.getInt("places_number");
                trajets.add(row);
            }
        }
        req.setAttribute("trajets", trajets);
        req.getRequestDispatcher("/trajet/trajetList.jsp").forward(req, resp);
    } catch (ClassNotFoundException | SQLException e) {
        throw new ServletException(e);
    }
}


    private void forwardEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String sql = "SELECT * FROM trajet WHERE id = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Object[] row = new Object[6];
                        row[0] = rs.getInt("id");
                        row[1] = rs.getInt("driver_id");
                        row[2] = rs.getString("start_point");
                        row[3] = rs.getString("end_point");
                        row[4] = rs.getTimestamp("start_hour").toLocalDateTime();
                        row[5] = rs.getInt("places_number");
                        req.setAttribute("trajet", row);
                    }
                }
            }
            req.getRequestDispatcher("/trajet/editTrajet.jsp").forward(req, resp);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("delete".equals(action)) {
                deleteTrajet(req);
            } else if ("update".equals(action)) {
                updateTrajet(req);
            } else {
                createTrajet(req);
            }
            resp.sendRedirect(req.getContextPath() + "/trajet");
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }
    }

    private void createTrajet(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO trajet(driver_id, start_point, end_point, start_hour, end_hour, places_number) VALUES(?,?,?,?,?,?)";
        String start = req.getParameter("start_point");
        String end = req.getParameter("end_point");
        String startHr = req.getParameter("start_hour");
        String endHr = req.getParameter("end_hour");
        String places = req.getParameter("places_number");

        String driverId = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("userId".equals(c.getName())) {
                    driverId = c.getValue(); break;
                }
            }
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(driverId));
            ps.setString(2, start);
            ps.setString(3, end);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.parse(startHr, fmt)));
            if (endHr != null && !endHr.isBlank()) ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.parse(endHr, fmt)));
            else ps.setNull(5, Types.TIMESTAMP);
            ps.setInt(6, Integer.parseInt(places));
            ps.executeUpdate();
        }
    }

    private void updateTrajet(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE trajet SET start_point=?, end_point=?, start_hour=?, end_hour=?, places_number=? WHERE id=?";
        int id = Integer.parseInt(req.getParameter("id"));
        String start = req.getParameter("start_point");
        String end = req.getParameter("end_point");
        String startHr = req.getParameter("start_hour");
        String endHr = req.getParameter("end_hour");
        String places = req.getParameter("places_number");

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, start);
            ps.setString(2, end);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(startHr, fmt)));
            if (endHr != null && !endHr.isBlank()) ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.parse(endHr, fmt)));
            else ps.setNull(4, Types.TIMESTAMP);
            ps.setInt(5, Integer.parseInt(places));
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    private void deleteTrajet(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM trajet WHERE id=?";
        int id = Integer.parseInt(req.getParameter("id"));
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
