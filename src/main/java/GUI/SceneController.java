package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    //Klasse die Wechsel zwischen Verschiedenen "Bildschirmen" verwaltet

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToStartPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von der Klausurerstellung zu Startseite

        Parent root = FXMLLoader.load(getClass().getResource("/GUI/StartPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToExamPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        Parent root = FXMLLoader.load(getClass().getResource("/GUI/ExamPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToExamCollection(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        Parent root = FXMLLoader.load(getClass().getResource("/GUI/ExamCollection.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTaskOverview(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        Parent root = FXMLLoader.load(getClass().getResource("/GUI/TaskOverview.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        Parent root = FXMLLoader.load(getClass().getResource("/GUI/AddTaskPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
