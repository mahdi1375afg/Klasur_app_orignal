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

    //Textfelder
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
        this.aufgabe = aufgabe;
    }

    @FXML
    public void initialize() {
        //Setzt Listener auf die Textfelder damit Änderungen sofort erfasst werden

        taskTextField.textProperty().addListener((observable, oldValue, newValue) -> setTask());

        taskSampleSolution.textProperty().addListener((observable, oldValue, newValue) -> setSampleSolution());

    }

    public void initializeNode(Task selectedTask){

        taskTextField.setText(selectedTask.getQuestion().getQuestionText());
        taskSampleSolution.setText(selectedTask.getAnswer().getLast().getAntwortText());

        this.selectedTask = selectedTask;
        editMode = true;

    }

    private void setTask() {
        taskText = taskTextField.getText();

    }

    private void setSampleSolution() {
        sampleSolutionText = taskSampleSolution.getText();
    }

    @FXML
    public void saveTask() throws IOException, SQLException {
        //Speicher die eingegebenen Daten und wechselt zurück zur Startseite

        if(taskText == null || taskText.isEmpty()) {
            showAlert("Sie haben keinen Aufgabentext angegeben.");
            return;
        }
        if(sampleSolutionText == null || sampleSolutionText.isEmpty()) {
            showAlert("Sie haben keine Musterlösung angegeben.");
                return;
        }

        if(!editMode) {
            aufgabe.setAnswerPage(sampleSolutionText, true);
            aufgabe.setTask(taskText);
            aufgabe.save();
        }
        else {
            //ToDo: Aufgabe updaten statt löschen und neu speichern
            aufgabe.setAnswerPage(sampleSolutionText, true);
            aufgabe.setTask(taskText);
            aufgabe.save();
            Task.deleteTask(selectedTask);
        }
        savedSwitchToStartPage();
    }



    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }

    @FXML
    public void savedSwitchToStartPage() throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToStartPage(stage);
    }


    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        if(showAlert()){
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }
    }

    @FXML
    public void switchToExamOverview() throws IOException{
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamCollection(stage);
        }
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamPage(stage);
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.logout(stage);
        }
    }

}
