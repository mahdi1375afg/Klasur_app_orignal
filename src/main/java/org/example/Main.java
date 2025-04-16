package org.example;
import org.example.dao.DBconn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException {
        String []values = {"netan","drake","gmail@outlook"};
        String []columns = {"vorname","name","email"};

        DBconn db = new DBconn();



        try {
            db.sqlInsert("nutzer",columns, values);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            db.sqlDelete("nutzer","vorname", "netan");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        //System.out.println(name);
    }
}