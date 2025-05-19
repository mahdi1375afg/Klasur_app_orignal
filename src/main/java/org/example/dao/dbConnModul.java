package org.example.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbConnModul {

    public static List<Map<String, Object>> sqlSelectModul() throws SQLException {
        String table = "modul";
        DBconn db = new DBconn();

        // Liste, die die Ergebnisse speichert
        List<Map<String, Object>> results = new ArrayList<>();

        PreparedStatement ps;
        try {
            ps = db.getConn().prepareStatement("SELECT * FROM " + table); //"SELECT * FROM " + table + " WHERE " + column + " = ?"
            //ps.setString(1, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet rs = ps.executeQuery();

        // Alle Ergebnisse in der Liste speichern
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", rs.getInt("id"));
            row.put("name", rs.getString("name"));
            row.put("beschreibung", rs.getString("beschreibung"));

            results.add(row);
        }
        rs.close();

        return results;
    }

    public static HashMap<Integer, String> sqlGetAllModul() throws SQLException {
        String table = "modul";
        DBconn db = new DBconn();

        // Liste, die die Ergebnisse speichert
        HashMap<Integer, String> results = new HashMap<>();

        PreparedStatement ps;
        try {
            ps = db.getConn().prepareStatement("SELECT * FROM " + table); //"SELECT * FROM " + table + " WHERE " + column + " = ?"
            //ps.setString(1, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet rs = ps.executeQuery();

        // Alle Ergebnisse in der Liste speichern
        while (rs.next()) {
            results.put(rs.getInt("id"), rs.getString("name"));
        }
        rs.close();

        for (Map.Entry<Integer, String> eintrag : results.entrySet()) {
            System.out.println(eintrag.getKey() + ": " + eintrag.getValue() + ", ");
        }
        return results;
    }
}
