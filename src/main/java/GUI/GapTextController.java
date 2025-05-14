package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.AufgabeService;

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


        System.out.println(taskText);
        System.out.println(gapText);
        System.out.println(completeText);

        aufgabe.setAnswerPageMultipleParts(gapText, completeText);
        aufgabe.setTask(taskText);
        aufgabe.save();
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
