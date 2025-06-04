package org.example.dao;

import org.example.domain.AntwortType;
import org.example.domain.BloomLevel;
import org.postgresql.util.PGInterval;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbConnExam {

    public static List<Map<String, Object>> sqlSelect(int id) throws SQLException {
        String sql = "SELECT * FROM klausur WHERE benutzer_id = ?";
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("name", rs.getString("name"));
                    row.put("datum", rs.getObject("datum"));
                    row.put("gesamtpunkte", rs.getObject("gesamtpunkte"));
                    row.put("benutzer_id", rs.getInt("benutzer_id"));

                    results.add(row);
                }
            }
        }

        // Debug-Ausgabe
        for (Map<String, Object> result : results) {
            for (Map.Entry<String, Object> entry : result.entrySet()) {
                System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
            }
            System.out.println();
        }

        return results;
    }

    public int getId(String name, LocalDate date, int totalPoints, int id) throws SQLException {
        String sql = "INSERT INTO klausur (name, datum, gesamtpunkte, benutzer_id) " + "VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setObject(2, date);
            ps.setInt(3, totalPoints);
            ps.setInt(4, id);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("ID konnte nicht ermittelt werden.");
                }
            }
        }
    }
}
