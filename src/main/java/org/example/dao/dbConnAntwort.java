package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbConnAntwort {

    public static List<Map<String, Object>> sqlSelectAntwortId(){
        String table = "antwort";
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT * FROM " + table;

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                results.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Ergebnisse in der Konsole ausgeben
        for (Map<String, Object> result : results) {
            for (Map.Entry<String, Object> eintrag : result.entrySet()) {
                System.out.print(eintrag.getKey() + ": " + eintrag.getValue() + ", ");
            }
            System.out.println();
        }

        return results;
    }

    public static void sqlInsertGeschlosseneAnt(String Antwort, String isCorrect, int antwortId) throws SQLException {
        String sql = "INSERT INTO geschlosseneantwort (antwort, iskorrekt, antwortid) " +
                "VALUES (?, CAST(? AS bloom), ?)";

        try (Connection conn = dbConn.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, Antwort);
            ps.setString(2, isCorrect);
            ps.setInt(3, antwortId);

            ps.executeUpdate();
        }
    }
}
