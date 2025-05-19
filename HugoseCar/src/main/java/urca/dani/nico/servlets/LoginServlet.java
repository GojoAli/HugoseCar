package urca.dani.nico.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;




@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    // Paramètres de connexion à votre base de données MySQL
    private final String urlDB = "jdbc:mysql://localhost:3306/hugosecar";
    private final String userDB = "root";
    private final String passDB = "taTu3MA+";


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des informations saisies par l'utilisateur
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Vérification des identifiants via la méthode authenticate
        if (authenticate(username, password)) {
            // Création d'une session et stockage du nom d'utilisateur
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            // Redirection vers la page de session
            response.sendRedirect("sessionPage.jsp");
        } else {
           
            // Si l'authentification échoue, renvoi sur la page de connexion avec un message d'erreur
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }


    /**
     * Vérifie que l'utilisateur et le mot de passe existent dans la base de données.
     * @param username Identifiant saisi par l'utilisateur.
     * @param password Mot de passe saisi par l'utilisateur.
     * @return true si l'utilisateur est authentifié, false sinon.
     */
    private boolean authenticate(String username, String password) {
        boolean isValid = false;
        try {
            // Chargement du driver JDBC (souvent automatique avec JDBC 4+)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Ouverture de la connexion à la base de données
            try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB)) {
                String sql = "SELECT COUNT(*) FROM user WHERE name = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            isValid = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

}
