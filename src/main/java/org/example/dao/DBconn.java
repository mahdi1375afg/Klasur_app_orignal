package org.example.dao;
import java.sql.*;

public class DBconn {

    private static final String url = "jdbc:postgresql://localhost:5432/klasurapp";
    private static final String user = "postgres";
    private static final String password = "1375";

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
        }
        return conn;
    }

    public static void sqlSelect(String table,String column, String value) throws SQLException {

        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement("select * from " + table + " where " + column + " = ?");//? ist platzhalter für value
            ps.setString(1,value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            System.out.println(rs.getInt("id")+rs.getString(column));
        }

    }
}
