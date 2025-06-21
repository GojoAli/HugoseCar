package urca.dani.nico.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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
            throw new ServletException(
                "Variables d'environnement manquantes : "
              + "DB_HOST, DB_NAME, DB_USER, DB_PASSWORD"
            );
        }
        urlDB  = "jdbc:mysql://" + host + ":3306/" + db + "?serverTimezone=UTC";
        userDB = user;
        passDB = pass;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email      = request.getParameter("email");
        String rawPassword = request.getParameter("password");

        if (email == null || email.isBlank()
         || rawPassword == null || rawPassword.isBlank()) {
            request.setAttribute("error", "Email et mot de passe sont obligatoires.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (authenticateByEmail(email, rawPassword)) {
            HttpSession session = request.getSession();


            // Récupération de l'ID utilisateur
            int userId = fetchUserIdByEmail(email);

            // Création du cookie de connexion
            Cookie cookie = new Cookie("userId", String.valueOf(userId));
            cookie.setHttpOnly(true);
            cookie.setMaxAge(7 * 24 * 3600); // 7 jours
            response.addCookie(cookie);

            session.setAttribute("email", email);
            response.sendRedirect("sessionPage.jsp");
        } else {
            request.setAttribute("error", "Identifiants invalides.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    /**
     * Récupère le hash associé à l'email et vérifie le mot de passe.
     */
    private boolean authenticateByEmail(String email, String rawPassword) {
        String sql = "SELECT password_hash FROM users WHERE email = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("password_hash");
                        return BCrypt.checkpw(rawPassword, storedHash);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // à remplacer par un logger en prod
        }
        return false;
    }


        private int fetchUserIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
