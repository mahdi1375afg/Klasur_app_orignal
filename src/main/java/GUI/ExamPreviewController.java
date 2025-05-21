package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ExamPreviewController extends SceneController {

    @FXML
    private MenuButton menuBar;

    @FXML
    private TextArea textAreaGeneratedExam;
    @FXML
    private TextArea textAreaGeneratedSampleSolution;

    @FXML
    public void initialize() {
        //ToDO: Richtige generierte Daten einfügen

        textAreaGeneratedExam.setEditable(false);
        textAreaGeneratedSampleSolution.setEditable(false);

        textAreaGeneratedExam.setText("Test Klausur");
        textAreaGeneratedSampleSolution.setText("Test Klausur Musterlösung");
    }

    @FXML
    public void generateAndSwitchToStartPage(ActionEvent event) throws IOException {
        //ToDo: Generierte Klausur abspeichern und dem Nutzer als Datei (PDF?) zur Verfügung stellen

        switchToStartPage(event);
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamPage(stage);
    }

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToStartPage(stage);
    }


    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToTaskOverview(stage);
    }

    @FXML
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToAddTaskPage(stage);
    }

    @FXML
    public void switchToExamOverview() throws IOException{
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamCollection(stage);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.logout(stage);
    }
}
