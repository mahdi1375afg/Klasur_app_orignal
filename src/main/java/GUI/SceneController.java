package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    @FXML
    protected void switchScene(ActionEvent event, String newScene) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        sceneWidth = stage.getScene().getWidth();
        sceneHeight = stage.getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(newScene));
        //stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    protected void switchScene(Stage stage, String newScene) throws IOException {
        double sceneWidth = stage.getScene().getWidth();
        double sceneHeight = stage.getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(newScene));
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel zur Startseite
        switchScene(event, "/GUI/StartPage.fxml");
    }

    @FXML
    public void switchToStartPage(Stage stage) throws IOException {
        switchScene(stage, "/GUI/StartPage.fxml");
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/ExamPage.fxml");
    }

    @FXML
    public void switchToExamPage(Stage stage) throws IOException {
        switchScene(stage, "/GUI/ExamPage.fxml");
    }

    @FXML
    public void switchToExamCollection(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/ExamCollection.fxml");
    }

    @FXML
    public void switchToExamCollection(Stage stage) throws IOException {
        switchScene(stage, "/GUI/ExamCollection.fxml");
    }


    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/TaskOverview.fxml");

    }

    @FXML
    public void switchToTaskOverview(Stage stage) throws IOException {
        switchScene(stage, "/GUI/TaskOverview.fxml");
    }

    @FXML
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/AddTaskPage.fxml");
    }

    @FXML
    public void switchToAddTaskPage(Stage stage) throws IOException {
        switchScene(stage, "/GUI/AddTaskPage.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        System.exit(0);

    }





    public void switchToLoginPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Titelseite zur Anmeldeseite
       switchScene(event, "/GUI/LoginPage.fxml");
    }

    public void switchToSignUpPage(ActionEvent event) throws IOException {
        //Methode sorgt für den Wechsel von Titelseite zur  Registrierungsseite
        switchScene(event, "/GUI/SignUpPage.fxml");
    }
}
