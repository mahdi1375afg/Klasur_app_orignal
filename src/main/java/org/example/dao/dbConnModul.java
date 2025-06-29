package org.example.dao;

import java.sql.*;
import java.util.*;

public class dbConnModul {
    public static HashMap<Integer, String> sqlGetAllModul(int id) throws SQLException {
        String table = "modul";
        HashMap<Integer, String> results = new HashMap<>();

        String sql = "SELECT * FROM " + table + " WHERE benutzer_id = " + id;

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
