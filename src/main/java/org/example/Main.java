package org.example;
import org.example.dao.DBconn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {

        DBconn db = new DBconn();

        try {
            db.sqlSelect("nutzer","vorname", "mahdi");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //System.out.println(name);
    }
}