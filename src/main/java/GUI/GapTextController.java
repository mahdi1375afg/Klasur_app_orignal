package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.AufgabeService;
import org.example.domain.Task;

import java.io.IOException;
import java.sql.SQLException;

public class GapTextController extends SceneController{

    @FXML
    private TextArea textAreaGapText;
    @FXML
    private TextArea textAreaCompleteText;
    @FXML
    private TextArea textAreaTask;

    @FXML
    private MenuButton menuBar;

    private String taskText;
    private String gapText;
    private String completeText;
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

        textAreaTask.textProperty().addListener((observable, oldValue, newValue) -> setTaskText());

        textAreaGapText.textProperty().addListener((observable, oldValue, newValue) -> setGapText());

        textAreaCompleteText.textProperty().addListener((observable, oldValue, newValue) -> setCompleteText());
    }

    public void initializeEditMode(Task selectedTask) {
        //Lädt alle Informationen zur Aufgabe aus der Datenbank beim Bearbeiten der Aufgabe

        textAreaTask.setText(selectedTask.getQuestion().getQuestionText());
        textAreaGapText.setText(selectedTask.getAnswer().getLast().getAntwortText());
        textAreaCompleteText.setText(selectedTask.getAnswer().getLast().getAntwortText2());


        this.selectedTask = selectedTask;
        editMode = true;
    }

    private void setTaskText() { taskText = textAreaTask.getText();}

    private void setGapText(){
        gapText = textAreaGapText.getText();
    }

    private void setCompleteText() {
        completeText = textAreaCompleteText.getText();
    }

    @FXML
    public void saveTask() throws IOException, SQLException {
        //Speichert alle gesammelten Daten und sendet sie an DB
        //anschließend erfolgt der Wechsel zum Startbildschirm bzw. zur Aufgabenübersicht
        //im editMode

        if(taskText == null ||taskText.isEmpty()) {
            showAlert("Sie haben keinen Aufgabentext angegeben.");
            return;
        }
        if(gapText == null || gapText.isEmpty()) {
            showAlert("Sie haben keinen Lückentext angegeben");
            return;
        }

        if(completeText == null || completeText.isEmpty()) {
            showAlert("Sie haben keinen vollständigen Text angegeben.");
            return;
        }
        aufgabe.setAnswerPageMultipleParts(gapText, completeText);

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
        //Wechsel mit Warnung zur Seite zum Klausur generieren

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
