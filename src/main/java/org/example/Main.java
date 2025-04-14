package org.example;
import org.example.dao.DBconn;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        DBconn db = new DBconn();
        
        try {
            db.getConn();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello world!");
    }
}