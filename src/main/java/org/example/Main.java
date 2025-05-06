package org.example;
import org.example.dao.DBconn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.dao.DBconn;
import org.example.domain.*;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main  extends Application {
    public static void main(String[] args) throws SQLException {
        String[] values = {"netan", "drake", "gmail@outlook"};
        String[] columns = {"vorname", "name", "email"};

        launch(args);


      /*  DBconn db = new DBconn();



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
    */
        Fragen frage = new Fragen(1, "Was ist Java?", BloomLevel.anwenden,new Modul(1,"AIN","ain") , 1, FragenArt.GeschlosseneFrage);
        Antwort antwort = new Antwort(2, "Java ist eine Programmiersprache", true, 1,AntwortType.geschlosseneAntwort);
        LocalDateTime time = LocalDateTime.now();
        benutzerKonto konto = new benutzerKonto(1, "netan", "drake", time, true);

        konto.antwortloeaschen(antwort);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //System.out.println(name);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/TitlePage.fxml"));
            Scene startScene = new Scene(root);
            stage.setScene(startScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}