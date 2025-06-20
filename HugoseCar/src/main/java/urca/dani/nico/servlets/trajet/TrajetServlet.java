package urca.dani.nico.servlets;

import urca.dani.nico.dao.TrajetDAO;
import urca.dani.nico.models.Trajet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/trajet")
public class TrajetServlet extends HttpServlet {
    private TrajetDAO dao;

    @Override
    public void init() throws ServletException {
        String url = "jdbc:mysql://" + System.getenv("DB_HOST") + ":3306/" + System.getenv("DB_NAME") + "?serverTimezone=UTC";
        dao = new TrajetDAO(url, System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Trajet t = dao.findById(id);
                req.setAttribute("trajet", t);
                req.getRequestDispatcher("/WEB-INF/editTrajet.jsp").forward(req, resp);
            } else {
                List<Trajet> trajets = dao.findAll();
                req.setAttribute("trajets", trajets);
                req.getRequestDispatcher("/WEB-INF/trajetList.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.delete(id);
            } else {
                Trajet t = readTrajetFromRequest(req);
                if ("update".equals(action)) {
                    t.setId(Integer.parseInt(req.getParameter("id")));
                    dao.update(t);
                } else {
                    dao.create(t);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/trajet");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Trajet readTrajetFromRequest(HttpServletRequest req) {
        Trajet t = new Trajet();
        t.setDriverId(Integer.parseInt(req.getParameter("driver_id")));
        t.setStartPoint(req.getParameter("start_point"));
        t.setEndPoint(req.getParameter("end_point"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        t.setStartHour(LocalDateTime.parse(req.getParameter("start_hour"), fmt));
        String end = req.getParameter("end_hour");
        if (end != null && !end.isBlank()) t.setEndHour(LocalDateTime.parse(end, fmt));
        t.setPlacesNumber(Integer.parseInt(req.getParameter("places_number")));
        return t;
    }
}