package org.example.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconn {

    private static final String url = "jdbc:postgresql://localhost:5432/klasurapp";
    private static final String user = "postgres";
    private static final String password = "1375";

    public static Connection getConn()throws SQLException {
        Connection conn = null;
        try {
             conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database" );
            }
        }
        catch (SQLException e) {
            System.out.println("Connection failed");
        }
        return conn;
    }
}
