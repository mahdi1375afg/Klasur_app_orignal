package org.example.dao;

import org.example.domain.AnswerType;
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

                    AnswerType answerType = AnswerType.fromName(rs.getString("format"));
                    row.put("format", answerType);
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
}
