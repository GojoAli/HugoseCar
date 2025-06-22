package urca.dani.nico.models;

import java.io.IOException;
import java.sql.*;

public class User {
    private int id;
    private String email;
    private String name;
    private String phoneNumber;

    public User(int id, String email, String name, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
}