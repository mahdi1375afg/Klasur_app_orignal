package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.dao.DBconn;
import org.example.domain.*;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        //launch(args);
        // Hier wird die Datenbankoperation vor dem Starten der JavaFX-Oberfläche ausgeführt
        DBconn db = new DBconn();

        //Fragen hinzufügen
        benutzerKonto mahdi = new benutzerKonto();
        /*
        Modul modul = new Modul(1,"AIN4","allgemein Informatik");

        Fragen frage = new Fragen(2,"wie groß ist eine Elephant", BloomLevel.verstehen,modul,3, FragenArt.GeschlosseneFrage);
        System.out.println(frage.getFragenArt());
        mahdi.antwortErstellen(frage);
        */

        //Fragen bearbeiten
        Antwort antwort = new Antwort(2,"Elephant ist 4m groß", true, 1, antwortType.geschlosseneAntwort);
         ///mahdi.antwortBearbeiten(antwort);

        //Fragen löschen
        mahdi.antwortloeaschen(antwort);



        // Fehlerbehandlung für Insert
       /* Object []values= {"aufgabe2","was ist primitiv Datentype",4,"anwenden",1,3,"geschlossene aufgabe"};
        String []columns = {"name","aufgabetext","zeit","bloomlevel","modulid","punkte","aufgabetype"};
        try {
            db.sqlInsert("nutzer", columns, values); // Beispiel für SQL Insert
        } catch (SQLException e) {
            e.printStackTrace();}
        */

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




        benutzerKonto Nutzer = new benutzerKonto();
        //Nutzer.fragenErstellen(1);
        //Nutzer.fragenfiltern(1);





        //System.out.println(name);
    }

   /* @Override
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
    }*/
}
