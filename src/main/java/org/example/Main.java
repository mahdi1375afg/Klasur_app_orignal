package org.example;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.domain.*;

public class Main  extends Application {
    public static void main(String[] args) throws SQLException {
        launch(args);
        //Nutzer konto = new Nutzer(1,"max","1234");
        //benutzerKonto konto2 = new benutzerKonto();
        //int id = konto2.fragenErstellen("Was ist Java?", "Java ist eine Programmiersprache", 1, "Multiple Choice", 1, "Anwenden", konto.getId());

        //konto2.fragenBearbeiten(1);
        // Hol alle Modul
        //konto2.deleteFragetable(3);
        /* Test Code
        Frage frage = new Frage(1, "Was ist Java?", BloomLevel.anwenden,new Modul(1,"AIN","ain") , 1, FragenArt.GeschlosseneFrage);
        Antwort antwort = new Antwort(2, "Java ist eine Programmiersprache", true, 1,AntwortType.geschlosseneAntwort);
        benutzerKonto konto = new benutzerKonto();
        konto.antwortloeaschen(antwort);
         */
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