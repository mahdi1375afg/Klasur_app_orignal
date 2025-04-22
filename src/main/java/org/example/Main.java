package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.dao.DBconn;

import java.sql.SQLException;

public class Main extends Application {

    public static void main(String[] args) throws SQLException {
        // Hier wird die Datenbankoperation vor dem Starten der JavaFX-Oberfläche ausgeführt
        String[] values = {"netan", "drake"};
        String[] columns = {"vorname", "name"};
        DBconn db = new DBconn();

        try {
            db.sqlInsert("nutzer", columns, values); // Beispiel für SQL Insert
        } catch (SQLException e) {
            e.printStackTrace(); // Fehlerbehandlung für Insert
        }

        try {
            db.sqlDelete("nutzer", "vorname", "netan"); // Beispiel für SQL Delete
        } catch (SQLException e) {
            e.printStackTrace(); // Fehlerbehandlung für Delete
        }

        launch(args);
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
