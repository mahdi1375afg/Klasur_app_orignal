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
        rs.close();
    }

    public static void sqlInsert(String table, String[] column,String[] value)throws SQLException {
        PreparedStatement ps;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++){

            sb.append("?,");
            if (i == value.length -1){
                sb.deleteCharAt(sb.length()-1);
            }
        }

        String listColumn = String.join(",", column);
        try {
            ps = getConn().prepareStatement("insert into " + table + " (" + listColumn + ") values ("+ sb +")");
            for(int i = 0; i < value.length; i++) {
                ps.setString(i + 1, value[i]);
            }
            int insertCount = ps.executeUpdate();
            System.out.println("Insert count: " + insertCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.close();
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



