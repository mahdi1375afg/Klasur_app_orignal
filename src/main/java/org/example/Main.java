package org.example;

import java.sql.SQLException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.domain.*;

public class Main  extends Application {

    public static int id;
    public static void main(String[] args){
        launch(args);

    }

    public static void initUser(int id) throws SQLException {
        Main.id = id;
        Modul.getAllModul();
        Task.getAllTasks(id);
    }

    @Override
    public void start(Stage stage){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/TitlePage.fxml")));
            Scene startScene = new Scene(root);
            stage.setScene(startScene);
            stage.setTitle("Klausurgenerator");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception ignored) {}
    }
}