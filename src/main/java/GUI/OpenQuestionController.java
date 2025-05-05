package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class OpenQuestionController extends SceneController{

    //Textfelder
    @FXML
    private TextField taskTextField;
    @FXML
    private TextField taskSampleSolution;


    @FXML
    private MenuButton menuBar;

    private String taskText = null;
    private String sampleSolutionText = null;

    @FXML
    public void initialize() {
        //wird zu Beginn ausgeführt und sorgt dafür, dass bei Änderungen in den Textfeldern automatisch die entsprechenden Setter aufgerufen werden

        taskTextField.textProperty().addListener((observable, oldValue, newValue) -> setTask());

        taskSampleSolution.textProperty().addListener((observable, oldValue, newValue) -> setSampleSolution());

    }

    private void setTask() {
        taskText = taskTextField.getText();

    }

    private void setSampleSolution() {
        sampleSolutionText = taskSampleSolution.getText();
    }

    public String getTask() {
        return taskText;
    }
    public String getSampleSolution() {
        return sampleSolutionText;
    }


    @FXML
    public void saveTask(ActionEvent event) throws IOException {
        //Speicher die eingegebenen Daten und wechselt zurück zur Startseite

        if(taskText == null) {
            if(!showAlert("Sie haben keinen Aufgabentext angegeben.")){
                return;
            }
        }
        if(sampleSolutionText == null) {
            if(!showAlert("Sie haben keine Musterlösung angegeben.")){
                return;
            }
        }

        //ToDo: Daten an die Anwendungsschicht übergeben


        System.out.println(taskText);
        System.out.println(sampleSolutionText);

        savedSwitchToStartPage(event);
    }



    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }

    @FXML
    public void savedSwitchToStartPage(ActionEvent event) throws IOException {
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }


    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        if(showAlert()){
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }
    }

    @FXML
    public void switchToExamOverview(ActionEvent event) throws IOException{
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
            super.logout(event);
        }
    }

}
