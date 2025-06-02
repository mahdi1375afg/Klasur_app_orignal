package org.example.dao;

import org.example.domain.AntwortType;
import org.example.domain.BloomLevel;
import org.postgresql.util.PGInterval;

import java.sql.*;
import java.util.*;

public class dbConnFrage {

    public static List<Map<String, Object>> sqlSelect(int id) throws SQLException {
        String sql = "SELECT * FROM aufgabe WHERE benutzer_id = ?";
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("name", rs.getString("name"));
                    row.put("aufgabentext", rs.getString("aufgabentext"));

                    PGInterval interval = (PGInterval) rs.getObject("zeit");
                    int minuten = interval.getHours() * 60 + interval.getMinutes();
                    if (interval.getSeconds() >= 30) {
                        minuten += 1;
                    }
                    row.put("zeit", minuten);

                    AntwortType antwortType = AntwortType.fromName(rs.getString("format"));
                    row.put("format", antwortType);
                    row.put("punkte", rs.getObject("punkte"));

                    String oldTaxonomie = rs.getString("taxonomie");
                    BloomLevel taxonomie = BloomLevel.fromKategorie(oldTaxonomie);
                    row.put("taxonomie", taxonomie);

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

    public static void sqlInsert(String name, String aufgabentext, int zeit, String format, int punkte, String taxonomie, int benutzer_id) throws SQLException {
        String sql = "INSERT INTO aufgabe (name, aufgabentext, zeit, format, punkte, taxonomie, benutzer_id) " +
                "VALUES (?, ?, ?::interval, ?, ?, ?::taxonomie_stufe, ?)";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, aufgabentext);
            ps.setString(3, zeit + " minutes");
            ps.setString(4, format);
            ps.setInt(5, punkte);
            ps.setObject(6, taxonomie);
            ps.setInt(7, benutzer_id);

            ps.executeUpdate();
        }
    }

    public int getId(String name, String aufgabentext, int zeit, String format, int punkte, String taxonomie, int benutzerId) throws SQLException {
        String sql = "INSERT INTO aufgabe (name, aufgabentext, zeit, format, punkte, taxonomie, benutzer_id) " +
                "VALUES (?, ?, ?::interval, ?, ?, ?::taxonomie_stufe, ?)";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, aufgabentext);
            ps.setObject(3, zeit + " minutes", Types.OTHER);
            ps.setString(4, format);
            ps.setInt(5, punkte);
            ps.setObject(6, taxonomie);
            ps.setInt(7, benutzerId);

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

    public static void sqlUpdate(String[] column, String[] value, String conditionColumn, String conditionValue) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < column.length; i++) {
            sb.append(column[i]).append(" = ?");
            if (i < column.length - 1) sb.append(", ");
        }

        String sql = "UPDATE aufgabe SET " + sb + " WHERE " + conditionColumn + " = ?";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < value.length; i++) {
                ps.setString(i + 1, value[i]);
            }

            if ("id".equals(conditionColumn)) {
                ps.setInt(value.length + 1, Integer.parseInt(conditionValue));
            } else {
                ps.setString(value.length + 1, conditionValue);
            }

            ps.executeUpdate();
        }
    }

    public static void sqlDelete(String column, String value) throws SQLException {
        String sql = "DELETE FROM aufgabe WHERE " + column + " = ?";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.executeUpdate();
        }
    }
}
