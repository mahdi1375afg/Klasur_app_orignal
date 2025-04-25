package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class dbConnAntwort {
    public static List<Map<String, Object>> sqlSelectAntwortId() throws SQLException {
        String table = "antwort";
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

            results.add(row);
        }
        rs.close();

        // Ergebnisse in der Konsole ausgeben
        for (Map<String, Object> result : results) {
            for (Map.Entry<String, Object> Eintrag : result.entrySet()) {
                System.out.print(Eintrag.getKey() + ": " + Eintrag.getValue() + ", ");
            }
            System.out.println(); // Neue Zeile für jeden Datensatz
        }
        return results;
    }

    public static void sqlInsertGeschlosseneAnt(String Antwort,String iskorrekt,int antwortId) throws SQLException {

        String sql = "INSERT INTO geschlosseneantwort (antwort,iskorrekt,antwortid) " + "VALUES (?, CAST(? AS bloom ), ?)";
        DBconn db = new DBconn();
        try (PreparedStatement ps = db.getConn().prepareStatement(sql)) {
            ps.setString(1, Antwort);
            ps.setString(2, iskorrekt);
            ps.setInt(3, antwortId);

            int insertCount = ps.executeUpdate();
            System.out.println("Insert count: " + insertCount);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // oder: throw new RuntimeException(e);
        }
    }
}
