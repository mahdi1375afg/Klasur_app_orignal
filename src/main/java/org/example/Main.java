package org.example;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.dao.DBInitializer;
import org.example.domain.*;

public class Main  extends Application {
    public static void main(String[] args) throws SQLException {
        DBInitializer db = new DBInitializer();
        db.initDatabase();
        launch(args);

    }

    public static void initUser(int id) throws SQLException {
        Modul.getAllModul();
        Task.getAllTasks(id);
    }

    public static void terminateUser() throws SQLException {
        Modul.modules.clear();
        Task.tasks.clear();
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