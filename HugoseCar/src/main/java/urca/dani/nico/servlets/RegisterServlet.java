package urca.dani.nico.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name        = request.getParameter("name");
        String email       = request.getParameter("email");
        String phoneNumber = request.getParameter("phone_number");
        String rawPassword = request.getParameter("password");

        // 1) validation des champs
        String error = validateInputs(name, email, phoneNumber, rawPassword);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 2) Hash du mot de passe
        String passwordHash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        // 3) Valeur is_certified forcée à false
        boolean certified = false;

        Timestamp createdAt = Timestamp.from(Instant.now());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB)) {
                // Vérifier l'unicité du nom
                String checkSql = "SELECT COUNT(*) FROM users WHERE name = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, name);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            request.setAttribute("error", "Ce nom d'utilisateur est déjà pris.");
                            request.getRequestDispatcher("register.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                // Insertion
                String insertSql =
                    "INSERT INTO users "
                  + "(name, password_hash, email, phone_number, is_certified, created_at) "
                  + "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, passwordHash);
                    insertStmt.setString(3, email);
                    insertStmt.setString(4, phoneNumber);
                    insertStmt.setBoolean(5, certified);
                    insertStmt.setTimestamp(6, createdAt);
                    insertStmt.executeUpdate();
                }
            }

            // Redirection
            response.sendRedirect("login.jsp?registered=true");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur serveur, veuillez réessayer plus tard.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private String validateInputs(String name, String email, String phone, String password) {
        if (name == null || name.isBlank()) {
            return "Le nom d'utilisateur est obligatoire.";
        }
        if (email == null || email.isBlank()) {
            return "L'adresse email est obligatoire.";
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            return "Le format de l'email est invalide.";
        }
        if (password == null || password.isBlank()) {
            return "Le mot de passe est obligatoire.";
        }
        return null;
    }
}
