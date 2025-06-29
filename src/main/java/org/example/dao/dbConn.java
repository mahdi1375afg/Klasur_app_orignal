package org.example.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.postgresql.util.PGobject;

import java.sql.*;
import java.util.*;

public class dbConn {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/klausurapp");
        config.setUsername("postgres");
        config.setPassword("1234");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);      // 30 Sekunden
        config.setConnectionTimeout(10000); // 10 Sekunden
        config.setMaxLifetime(1800000);     // 30 Minuten

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
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

    public static void sqlDelete(String table, String column1, Object value1, String column2, Object value2) throws SQLException {
        String query = "DELETE FROM " + table + " WHERE " + column1 + " = ? AND " + column2 + " = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            if (value1 instanceof Integer) {
                ps.setInt(1, (Integer) value1);
            } else if (value1 instanceof String) {
                ps.setString(1, (String) value1);
            } else {
                throw new SQLException("Invalid data type for value1");
            }

            if (value2 instanceof Integer) {
                ps.setInt(2, (Integer) value2);
            } else if (value2 instanceof String) {
                ps.setString(2, (String) value2);
            } else {
                throw new SQLException("Invalid data type for value2");
            }

            ps.executeUpdate();
        }
    }
}
