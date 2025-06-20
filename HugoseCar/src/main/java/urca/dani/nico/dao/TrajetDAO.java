package urca.dani.nico.dao;

import urca.dani.nico.models.Trajet;
import java.sql.*;
import java.util.*;

public class TrajetDAO {
    private final String url;
    private final String user;
    private final String pass;

    public TrajetDAO(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public void create(Trajet trajet) throws SQLException {
        String sql = "INSERT INTO trajet (driver_id, start_point, end_point, start_hour, end_hour, places_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trajet.getDriverId());
            ps.setString(2, trajet.getStartPoint());
            ps.setString(3, trajet.getEndPoint());
            ps.setTimestamp(4, Timestamp.valueOf(trajet.getStartHour()));
            if (trajet.getEndHour() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(trajet.getEndHour()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setInt(6, trajet.getPlacesNumber());
            ps.executeUpdate();
        }
    }

    public List<Trajet> findAll() throws SQLException {
        List<Trajet> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM trajet")) {
            while (rs.next()) {
                Trajet t = mapResultSetToTrajet(rs);
                list.add(t);
            }
        }
        return list;
    }

    public Trajet findById(int id) throws SQLException {
        String sql = "SELECT * FROM trajet WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTrajet(rs);
                } else {
                    return null;
                }
            }
        }
    }

    public void update(Trajet trajet) throws SQLException {
        String sql = "UPDATE trajet SET driver_id = ?, start_point = ?, end_point = ?, start_hour = ?, end_hour = ?, places_number = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trajet.getDriverId());
            ps.setString(2, trajet.getStartPoint());
            ps.setString(3, trajet.getEndPoint());
            ps.setTimestamp(4, Timestamp.valueOf(trajet.getStartHour()));
            if (trajet.getEndHour() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(trajet.getEndHour()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setInt(6, trajet.getPlacesNumber());
            ps.setInt(7, trajet.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM trajet WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Trajet mapResultSetToTrajet(ResultSet rs) throws SQLException {
        Trajet t = new Trajet();
        t.setId(rs.getInt("id"));
        t.setDriverId(rs.getInt("driver_id"));
        t.setStartPoint(rs.getString("start_point"));
        t.setEndPoint(rs.getString("end_point"));
        t.setStartHour(rs.getTimestamp("start_hour").toLocalDateTime());
        Timestamp tsEnd = rs.getTimestamp("end_hour");
        if (tsEnd != null) t.setEndHour(tsEnd.toLocalDateTime());
        t.setPlacesNumber(rs.getInt("places_number"));
        return t;
    }
}