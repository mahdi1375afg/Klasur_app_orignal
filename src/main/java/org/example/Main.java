package org.example;

import java.sql.SQLException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.dao.dbConn;
import org.example.domain.*;

public class Main  extends Application {

    public static Integer id;
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

            stage.setOnCloseRequest((WindowEvent we) -> {
                if(Main.id != null) {
                    dbConn.closePool();
                }
            });

        } catch (Exception ignored) {}
            stage.show();
    }
}