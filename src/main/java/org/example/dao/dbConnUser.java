package org.example.dao;

import org.example.domain.Nutzer;
import org.postgresql.util.PSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbConnUser {

    public static void sqlInsert(String name, String password) throws SQLException {
        DBconn db = new DBconn();

        String sql = "INSERT INTO benutzer (benutzername, passwort) " + "VALUES (?, ?)";

        try (PreparedStatement ps = db.getConn().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);

            int insertCount = ps.executeUpdate();
            //System.out.println("Insert count: " + insertCount);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // oder: throw new RuntimeException(e);
        }
    }

    public static Nutzer getNutzer(String name) throws SQLException {
        String table = "benutzer";
        DBconn db = new DBconn();

        PreparedStatement ps;
        try {
            ps = db.getConn().prepareStatement("SELECT * FROM " + table + " WHERE benutzername = ?");
            ps.setString(1, name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet rs = ps.executeQuery();
        Nutzer nutzer = null;
        if (rs.next()) {
            int id = rs.getInt("id");
            String foundName = rs.getString("benutzername");
            String foundPasswort = rs.getString("passwort");
            nutzer = new Nutzer(id, foundName, foundPasswort);
        }
        rs.close();
        ps.close();
        return nutzer;
    }

    public static boolean getNameUsed(String name) throws SQLException {
        String table = "benutzer";
        DBconn db = new DBconn();


        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = db.getConn().prepareStatement("SELECT * FROM " + table + " WHERE benutzername = ?");
            ps.setString(1, name);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            rs = ps.executeQuery();
            if(rs.next()) {
                if(rs.getString("benutzername").equals(name)) {
                    //System.out.println("Account Exist!");
                    return true;
                } else {
                    System.out.println("Account doesnt Exists!");
                    return false;
                }
            } else {
                System.out.println("Account doesnt Exist!");
                return false;
            }
        } catch (PSQLException e) {
            System.out.println("Account doesnt Exist!");
            return false;
        }
    }
}
