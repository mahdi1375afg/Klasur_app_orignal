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

    //Textfelder
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
        this.aufgabe = aufgabe;
    }

    @FXML
    public void initialize() {
        //Setzt Listener auf die Textfelder damit Änderungen sofort erfasst werden

        textAreaTask.textProperty().addListener((observable, oldValue, newValue) -> setTask());

        textAreaGapText.textProperty().addListener((observable, oldValue, newValue) -> setGapText());

        textAreaCompleteText.textProperty().addListener((observable, oldValue, newValue) -> setCompleteText());
    }

    public void initializeEditMode(Task selectedTask) {

        textAreaTask.setText(selectedTask.getQuestion().getQuestionText());
        textAreaGapText.setText(selectedTask.getAnswer().getLast().getAntwortText());
        textAreaCompleteText.setText(selectedTask.getAnswer().getLast().getAntwortText2());


        this.selectedTask = selectedTask;
        editMode = true;

    }

    private void setTask() {
        taskText = textAreaTask.getText();

    }

    private void setGapText(){
        gapText = textAreaGapText.getText();
    }

    private void setCompleteText() {
        completeText = textAreaCompleteText.getText();
    }

    @FXML
    public void saveTask() throws IOException, SQLException {
        //Speicher die eingegebenen Daten und wechselt zurück zur Startseite

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

        if(!editMode) {
            aufgabe.setAnswerPageMultipleParts(gapText, completeText);
            aufgabe.setTask(taskText);
            aufgabe.save();
        }
        else {
            //ToDo: Aufgabe updaten statt löschen und neu speichern
            aufgabe.setAnswerPageMultipleParts(gapText, completeText);
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
