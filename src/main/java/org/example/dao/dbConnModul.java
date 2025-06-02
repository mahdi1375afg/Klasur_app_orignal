package org.example.dao;

import java.sql.*;
import java.util.*;

public class dbConnModul {

    public static List<Map<String, Object>> sqlSelectModul() throws SQLException {
        String table = "modul";
        List<Map<String, Object>> results = new ArrayList<>();

        String sql = "SELECT * FROM " + table;

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("beschreibung", rs.getString("beschreibung"));
                results.add(row);
            }
        }

        return results;
    }

    public static HashMap<Integer, String> sqlGetAllModul() throws SQLException {
        String table = "modul";
        HashMap<Integer, String> results = new HashMap<>();

        String sql = "SELECT * FROM " + table;

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.put(rs.getInt("id"), rs.getString("name"));
            }
        }

        // Debug-Ausgabe
        for (Map.Entry<Integer, String> eintrag : results.entrySet()) {
            System.out.println(eintrag.getKey() + ": " + eintrag.getValue() + ", ");
        }
        return results;
    }
}
