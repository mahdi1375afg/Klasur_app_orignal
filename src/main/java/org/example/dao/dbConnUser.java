package org.example.dao;

import org.example.domain.User;
import org.postgresql.util.PSQLException;

import java.sql.*;

public class dbConnUser {

    public static void sqlInsert(String name, String password) throws SQLException {
        String sql = "INSERT INTO benutzer (benutzername, passwort) VALUES (?, ?)";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, password);

            ps.executeUpdate();
        }
    }

    public static User getNutzer(String name) throws SQLException {
        String sql = "SELECT * FROM benutzer WHERE benutzername = ?";
        User user = null;

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String foundName = rs.getString("benutzername");
                    String foundPasswort = rs.getString("passwort");
                    user = new User(id, foundName, foundPasswort);
                }
            }
        }
        return user;
    }

    public static boolean getNameUsed(String name) throws SQLException {
        String sql = "SELECT benutzername FROM benutzer WHERE benutzername = ?";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Wenn ein Ergebnis da ist, ist der Name schon verwendet
            }
        } catch (PSQLException e) {
            // Optional: hier kannst du Logging machen
            return false;
        }
    }
}
