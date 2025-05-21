package org.example.dao;

import org.postgresql.util.PGobject;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBconn {

    private static final String url = "jdbc:postgresql://localhost:5432/klausurapp";
    private static final String user = "postgres";
    private static final String password = "1234";


    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

        public static List<Map<String, Object>> sqlSelect(String table, String column, Object value) throws SQLException {
        String query = "SELECT * FROM " + table + " WHERE " + column + " = ?";
        List<Map<String, Object>> results = new ArrayList<>();

        try (
                Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setObject(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        }
        return results;
    }


    public static void sqlInsert(Connection conn, String table, String[] column, Object[] value) throws SQLException {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            if (column[i].equalsIgnoreCase("ist_korrekt")) {
                placeholders.append("CAST(? AS boolean)");
            } else {
                placeholders.append("?");
            }
            if (i < value.length - 1) {
                placeholders.append(",");
            }
        }

        String listColumn = String.join(",", column);
        String query = "INSERT INTO " + table + " (" + listColumn + ") VALUES (" + placeholders + ")";
        System.out.println(query);

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            for (int i = 0; i < value.length; i++) {
                if (value[i] instanceof String) {
                    ps.setString(i + 1, (String) value[i]);
                } else if (value[i] instanceof Integer) {
                    ps.setInt(i + 1, (Integer) value[i]);
                } else if (value[i] instanceof Boolean) {
                    ps.setBoolean(i + 1, (Boolean) value[i]);
                } else if (value[i] instanceof PGobject) {
                    ps.setObject(i + 1, value[i]);
                } else {
                    throw new SQLException("Invalid data type: " + value[i].getClass());
                }
            }

            ps.executeUpdate();
        }
    }

    public static void sqlUpdate(String table, String[] column, Object[] value, String conditionColumn, Object conditionValue) throws SQLException {
        StringBuilder setClause = new StringBuilder();
        for (int i = 0; i < column.length; i++) {
            if (column[i].equalsIgnoreCase("zeit")) {
                setClause.append("zeit = CAST(? AS interval)");
            } else if (column[i].equalsIgnoreCase("isKorrekt")) {
                setClause.append("isKorrekt = CAST(? AS bloom)");
            } else {
                setClause.append(column[i]).append(" = ?");
            }
            if (i < column.length - 1) {
                setClause.append(", ");
            }
        }

        String query = "UPDATE " + table + " SET " + setClause + " WHERE " + conditionColumn + " = ?";
        System.out.println(query);

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            for (int i = 0; i < value.length; i++) {
                if (column[i].equalsIgnoreCase("zeit")) {
                    ps.setString(i + 1, value[i] + " minutes");
                } else if (value[i] instanceof String) {
                    ps.setString(i + 1, (String) value[i]);
                } else if (value[i] instanceof Integer) {
                    ps.setInt(i + 1, (Integer) value[i]);
                } else {
                    throw new SQLException("Invalid data type");
                }
            }

            if (conditionValue instanceof Integer) {
                ps.setInt(value.length + 1, (Integer) conditionValue);
            } else if (conditionValue instanceof String) {
                ps.setString(value.length + 1, (String) conditionValue);
            } else {
                throw new SQLException("Invalid data type for conditionValue");
            }

            ps.executeUpdate();
        }
    }

    public static void sqlDelete(String table, String column, Object value) throws SQLException {
        String query = "DELETE FROM " + table + " WHERE " + column + " = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            if (value instanceof Integer) {
                ps.setInt(1, (Integer) value);
            } else if (value instanceof String) {
                ps.setString(1, (String) value);
            } else {
                throw new SQLException("Invalid data type");
            }

            ps.executeUpdate();
        }
    }
}
