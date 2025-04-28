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
    //private Parent root;
    private double sceneWidth, sceneHeight;

    private void switchScene(ActionEvent event, String newScene) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        sceneWidth = stage.getScene().getWidth();
        sceneHeight = stage.getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(newScene));
        //stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);

        stage.show();
    }

    public void switchToStartPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel zur Startseite

        switchScene(event, "/GUI/StartPage.fxml");
    }

    public void switchToExamPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/ExamPage.fxml");
    }

    public void switchToExamCollection(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/ExamCollection.fxml");
    }

    public void switchToTaskOverview(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/TaskOverview.fxml");

    }

    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/AddTaskPage.fxml");
    }


}
