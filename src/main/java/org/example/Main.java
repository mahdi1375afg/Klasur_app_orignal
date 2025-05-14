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

        initUser();
        launch(args);

    }

    public static void initUser() throws SQLException {
        Modul.getAllModul();
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