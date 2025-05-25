// Sprache: java
package org.example.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initDatabase() {
        // Passe den Pfad zur SQL-Datei an. Doppelte Backslashes werden in Strings benötigt.
        String sqlFile = "C:\\Users\\mahdi\\OneDrive\\4er_Semester-DESKTOP-5LIBHVM\\git_branch_jar\\Klasur_app_orignal\\src\\main\\resources\\klausur_app_sql.sql";
        String runScript = "RUNSCRIPT FROM '" + sqlFile + "'";
        try (Connection conn = DBconn.getConn();
             Statement stmt = conn.createStatement()) {
            stmt.execute(runScript);
            System.out.println("SQL-Skript wurde erfolgreich ausgef\u00FChrt.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}