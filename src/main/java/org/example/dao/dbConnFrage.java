package org.example.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbConnFrage {

    private static final String url = "jdbc:postgresql://localhost:5432/klausurapp"; // klasur => klausur
    private static final String user = "postgres";
    private static final String password = "1234"; //1234 - Passwort Jan

    public static Connection getConn()throws SQLException {
        Connection conn = null;
        try {
             conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("connection valid: " + conn.isValid(0));
            }
        }
        catch (SQLException e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static List<Map<String, Object>> sqlSelect(String column, String value) throws SQLException {
        String table = "aufgabe";

        // Liste, die die Ergebnisse speichert
        List<Map<String, Object>> results = new ArrayList<>();

        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement("SELECT * FROM " + table); //"SELECT * FROM " + table + " WHERE " + column + " = ?"
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

    public static void sqlInsert(String name, String aufgabentext, int zeit, String bloomlevel, String type, int modulid, int punkte) throws SQLException {
        String table = "aufgabe";
        String sql = "INSERT INTO aufgabe (name, aufgabetext, zeit, bloomlevel, type, modulid, punkte) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, aufgabentext);
            ps.setInt(3, zeit);
            ps.setObject(4, bloomlevel, java.sql.Types.OTHER);
            ps.setObject(5, type, java.sql.Types.OTHER);
            ps.setInt(6, modulid);
            ps.setInt(7, punkte);

            int insertCount = ps.executeUpdate();
            System.out.println("Insert count: " + insertCount);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // oder: throw new RuntimeException(e);
        }
    }

    public static void sqlUpdate(String table, String[] column,String[] value,String conditionColumn,String conditionValue)throws SQLException {
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
            ps = getConn().prepareStatement(query);
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

    public static void sqlDelete (String table, String column, String value) throws SQLException {
        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement("delete from " + table + " where " + column + " = ?");
            ps.setString(1, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int deleteCount = ps.executeUpdate();
        System.out.println("Delete count: " + deleteCount);
        ps.close();
    }

}



