package org.example.dao;
import org.postgresql.util.PGobject;

import java.sql.*;

public class DBconn {

    private static final String url = "jdbc:postgresql://localhost:5432/klausurapp"; // klasur => klausur
    private static final String user = "postgres";
    private static final String password = "1234"; //1234 - Passwort Jan

    public static Connection getConn()throws SQLException {
        Connection conn = null;
        try {
             conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
        //        System.out.println("connection valid: " + conn.isValid(0));
            }
        }
        catch (SQLException e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static ResultSet  sqlSelect(String table,String column, Object value) throws SQLException {
        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement("select * from " + table + " where " + column + " = ?");//? ist platzhalter für value
            ps.setObject(1,value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet rs = ps.executeQuery();

        return rs;
    }

    public static void sqlInsert(String table, String[] column,Object[] value)throws SQLException {
        PreparedStatement ps;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            // Spezialbehandlung für ENUM-Typen
            if (column[i].equalsIgnoreCase("ist_korrekt")) {
                sb.append("CAST(? AS boolean)");
            } else {
                sb.append("?");
            }

            if (i < value.length - 1) {
                sb.append(",");
            }
        }


        String listColumn = String.join(",", column);
        String query ="insert into " + table + " (" + listColumn + ") values ("+ sb +")";
        System.out.println(query);
        try {
            ps = getConn().prepareStatement(query);
            for(int i = 0; i < value.length; i++) {
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
            int insertCount = ps.executeUpdate();
            //System.out.println("Insert count: " + insertCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.close();
    }


    public static void sqlUpdate(String table, String[] column,Object[] value,String conditionColumn,Object conditionValue)throws SQLException {
        PreparedStatement ps;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < column.length; i++) {
            if (column[i].equalsIgnoreCase("zeit")) {
                sb.append("zeit = CAST(? AS interval)");
            }
                else if (column[i].equalsIgnoreCase("isKorrekt")) {
                    sb.append("isKorrekt = CAST(? AS " + "bloom" + ")");}
                else{
                    sb.append(column[i]).append(" = ?");
                }
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
                if (column[i].equalsIgnoreCase("zeit")) {
                    // Integer-Wert zu einem korrekten Intervall-String umwandeln
                    ps.setString(i + 1, value[i] + " minutes");
                }
                 else if(value[i] instanceof String){
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
            int updatecount = ps.executeUpdate();
            //System.out.println("Update count: " + updatecount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.close();
    }

        public static void sqlDelete (String table, String column, Object value) throws SQLException {
            PreparedStatement ps;
            try {
                ps = getConn().prepareStatement("delete from " + table + " where " + column + " = ?");
                if(value instanceof Integer){
                    ps.setInt(1, (Integer) value);
                } else if (value instanceof String) {
                    ps.setString(1, (String) value);
                } else {
                    throw new SQLException("Invalid data type");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int deleteCount = ps.executeUpdate();
            //System.out.println("Delete count: " + deleteCount);
            ps.close();
        }

    }



