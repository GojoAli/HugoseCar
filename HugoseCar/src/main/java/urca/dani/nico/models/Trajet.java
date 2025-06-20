package urca.dani.nico.models;

import java.time.LocalDateTime;

public class Trajet {
    private int id;
    private int driverId;
    private String startPoint;
    private String endPoint;
    private LocalDateTime startHour;
    private LocalDateTime endHour;
    private int placesNumber;
    private LocalDateTime createdAt;

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }

    public String getEndPoint() { return endPoint; }
    public void setEndPoint(String endPoint) { this.endPoint = endPoint; }

    public LocalDateTime getStartHour() { return startHour; }
    public void setStartHour(LocalDateTime startHour) { this.startHour = startHour; }

    public LocalDateTime getEndHour() { return endHour; }
    public void setEndHour(LocalDateTime endHour) { this.endHour = endHour; }

    public int getPlacesNumber() { return placesNumber; }
    public void setPlacesNumber(int placesNumber) { this.placesNumber = placesNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
