package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.dao.DBconn;
import org.example.domain.benutzerKonto;

import java.sql.SQLException;

public class Main extends Application {

    public static void main(String[] args) throws SQLException {
        launch(args);
        // Hier wird die Datenbankoperation vor dem Starten der JavaFX-Oberfläche ausgeführt
        DBconn db = new DBconn();


        // Fehlerbehandlung für Insert
        Object []values= {"aufgabe2","was ist primitiv Datentype",4,"anwenden",1,3,"geschlossene aufgabe"};
        String []columns = {"name","aufgabetext","zeit","bloomlevel","modulid","punkte","aufgabetype"};
        try {
            db.sqlInsert("nutzer", columns, values); // Beispiel für SQL Insert
        } catch (SQLException e) {
            e.printStackTrace();}


        // test modul
        /*try {
            db.sqlInsert("modul",columns, values);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            db.sqlSelect("modul","name", "AIN4");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/



        try {
            db.sqlInsert("aufgabe",columns, values);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            db.sqlDelete("nutzer", "vorname", "netan"); // Beispiel für SQL Delete
        } catch (SQLException e) {
            e.printStackTrace(); // Fehlerbehandlung für Delete
        }

        benutzerKonto Nutzer = new benutzerKonto();
        //Nutzer.fragenErstellen(1);
        //Nutzer.fragenfiltern(1);





        //System.out.println(name);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Methode die GUI startet

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/StartPage.fxml"));
            Scene startScene = new Scene(root);
            stage.setScene(startScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
