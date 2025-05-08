package org.example.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbConnFrage {

    public static List<Map<String, Object>> sqlSelect(String column, String value) throws SQLException {
        String table = "aufgabe";
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
            row.put("aufgabetext", rs.getString("aufgabetext"));
            row.put("zeit", rs.getInt("zeit"));
            row.put("bloomlevel", rs.getObject("bloomlevel"));
            row.put("type", rs.getObject("type"));
            row.put("modulid", rs.getInt("modulid"));
            row.put("punkte", rs.getInt("punkte"));
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

    public static void sqlInsert(String name, String aufgabentext, int zeit, String format, int punkte, String taxonomie, int benutzer_id) throws SQLException {
        String table = "aufgabe";
        DBconn db = new DBconn();
        String sql = "INSERT INTO aufgabe (name, aufgabentext, zeit, format, punkte, taxonomie, benutzer_id) " + "VALUES (?, ?, ?::interval, ?, ?, ?::taxonomie_stufe, ?)";

        try (PreparedStatement ps = db.getConn().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, aufgabentext);

            String intervalString = zeit + " minutes";
            ps.setString(3, intervalString);

            ps.setString(4, format);
            ps.setInt(5, punkte);
            ps.setObject(6, taxonomie);
            ps.setInt(7, benutzer_id);

            int insertCount = ps.executeUpdate();
            System.out.println("Insert count: " + insertCount);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // oder: throw new RuntimeException(e);
        }
    }

    public int getId(String name, String aufgabentext, int zeit, String format, int punkte, String taxonomie, int benutzerId) throws SQLException {
        DBconn db = new DBconn();
        String sql = "SELECT id FROM aufgabe WHERE name = ? AND aufgabentext = ? AND zeit = ? AND format = ? AND punkte = ? AND taxonomie = ? AND benutzer_id = ?";

        try (PreparedStatement ps = db.getConn().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, aufgabentext);

            String intervalString = zeit + " minutes";
            ps.setString(3, intervalString);

            ps.setString(4, format);
            ps.setInt(5, punkte);
            ps.setObject(6, taxonomie);
            ps.setInt(7, benutzerId);

            int aufgabenId = ps.executeUpdate();
            return aufgabenId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // oder: throw new RuntimeException(e);
        }
    }

    public static void sqlUpdate(String[] column,String[] value,String conditionColumn,String conditionValue)throws SQLException {
        String table = "aufgabe";
        DBconn db = new DBconn();
        PreparedStatement ps;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < column.length; i++) {

            sb.append(column[i]).append(" = ?");
            if (i < column.length - 1) {
                sb.append(", ");
            }
        }

        String listColumn = sb.toString();
        String query = "update " + table + " set " + listColumn + " where " + conditionColumn + " = ?";
        System.out.println(query);
        try {
            ps = db.getConn().prepareStatement(query);
            for (int i = 0; i < value.length; i++) {
                ps.setString(i + 1, value[i]);
            }
            if (conditionColumn.equals("id")) {
                ps.setInt(value.length + 1, Integer.parseInt(conditionValue));
            } else {
                ps.setString(value.length + 1, conditionValue);
            }
            int updatecount = ps.executeUpdate();
            System.out.println("Update count: " + updatecount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.close();
    }

    public static void sqlDelete (String column, String value) throws SQLException {
        String table = "aufgabe";
        DBconn db = new DBconn();
        PreparedStatement ps;
        try {
            ps = db.getConn().prepareStatement("delete from " + table + " where " + column + " = ?");
            ps.setString(1, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int deleteCount = ps.executeUpdate();
        System.out.println("Delete count: " + deleteCount);
        ps.close();
    }
}



