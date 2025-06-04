package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.AufgabeService;
import org.example.domain.Task;

import java.io.IOException;
import java.sql.SQLException;

public class OpenQuestionController extends SceneController{

    @FXML
    private TextArea taskTextField;
    @FXML
    private TextArea taskSampleSolution;

    @FXML
    private MenuButton menuBar;

    private String taskText = null;
    private String sampleSolutionText = null;
    private AufgabeService aufgabe;
    private Task selectedTask;
    private boolean editMode = false;

    public void setAufgabe(AufgabeService aufgabe) {
        //Grundlegende Informationen zur Aufgabe setzen

        this.aufgabe = aufgabe;
    }

    @FXML
    public void initialize() {
        //Setzt Listener auf die Textfelder damit Änderungen sofort erfasst werden

        taskTextField.textProperty().addListener((observable, oldValue, newValue) -> setTask());

        taskSampleSolution.textProperty().addListener((observable, oldValue, newValue) -> setSampleSolution());
    }

    public void initializeEditMode(Task selectedTask){
        //Lädt alle Informationen zur Aufgabe aus der Datenbank beim Bearbeiten der Aufgabe

        taskTextField.setText(selectedTask.getQuestion().getQuestionText());
        taskSampleSolution.setText(selectedTask.getAnswer().getLast().getAntwortText());

        this.selectedTask = selectedTask;
        editMode = true;
    }

    private void setTask() { taskText = taskTextField.getText();}

    private void setSampleSolution() {
        sampleSolutionText = taskSampleSolution.getText();
    }

    @FXML
    public void saveTask() throws IOException, SQLException {
        //Speichert alle gesammelten Daten und sendet sie an DB
        //anschließend erfolgt der Wechsel zum Startbildschirm bzw. zur Aufgabenübersicht
        //im editMode

        if(taskText == null || taskText.isEmpty()) {
            showAlert("Sie haben keinen Aufgabentext angegeben.");
            return;
        }
        if(sampleSolutionText == null || sampleSolutionText.isEmpty()) {
            showAlert("Sie haben keine Musterlösung angegeben.");
                return;
        }

        aufgabe.setAnswerPage(sampleSolutionText, true);
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.saveTask(editMode, selectedTask, aufgabe, taskText, stage);
    }



    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Startseite

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }

    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        //Wechsel mit Warnung zur Aufgabenübersicht

        if(showAlert()){
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }
    }

    @FXML
    public void switchToExamOverview() throws IOException{
        //Wechsel mit Warnung zur Klausurübersicht

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamCollection(stage);
        }
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Seite zum Klausur erstellen

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamPage(stage);
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        //Abmelden mit Warnung

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.logout(stage);
        }
    }

}
