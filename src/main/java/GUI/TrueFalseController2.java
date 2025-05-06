package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;

public class TrueFalseController2 extends SceneController {


    //ToDo: Fehlermeldungen bei erneuter Aufgabe richtig setzten und bei neuer Aufgabe button = null setzen
    //ToDo: Daten besser speicherm?
    //ToDo: Code aufräumen
    //Textfelder
    @FXML
    private TextField textFieldTaskText;
    @FXML
    private TextField textFieldTask;
    @FXML
    private MenuButton menuBar;
    @FXML
    private RadioButton rButtonTrue;
    @FXML
    private RadioButton rButtonFalse;

    private String task = null;
    private String taskText = null;
    private ArrayList<String> tasks = new ArrayList<>();
    private Boolean answerStatus = null;

    public void initialize() {
        //wird zu Beginn ausgeführt und sorgt dafür, dass bei Änderungen in den Textfeldern automatisch die entsprechenden Setter aufgerufen werden

        textFieldTask.textProperty().addListener((observable, oldValue, newValue) -> setTask());
    }


    public void setTaskText() {
        taskText = textFieldTaskText.getText();
    }

    public String getTaskText(){
        return taskText;
    }

    public void setTask() {
        task = textFieldTask.getText();
    }

    public String getTask(){
        return task;
    }

    public void addTask(String task) {
        tasks.add(task);
    }

    @FXML
    private void setAnswerStatus() {
        if(rButtonTrue.isSelected()){
            answerStatus = true;
        }
        else if(rButtonFalse.isSelected()){
            answerStatus = false;
        }
    }


    @FXML
    private Boolean getAnswerStatus() {
        return answerStatus;
    }

    @FXML
    public void setTaskTextPage2(String text) {
        taskText = text;
        textFieldTaskText.setText(text);
        textFieldTaskText.setEditable(false); // verhindert Bearbeitung
        textFieldTaskText.setFocusTraversable(false); // optional, nimmt Fokus weg
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
        //ToDo: Daten speichern und nicht auf dem Terminal ausgeben
        //ToDo: Fehlerbehandlung bei leerer Eingabe
        if(task == null || taskText == null){
            if(!showAlert()) {
                return;
            }
        }

        addTask(task);
        System.out.println(tasks.toString());
        System.out.println(getTaskText());
        System.out.println(getAnswerStatus());

        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToStartPage(stage);

    }

    @FXML
    public void switchToAddAnotherTask(ActionEvent event) throws IOException {
        if (task == null || taskText == null) {
            if (!showAlert()) {
                return;
            }
        }

        addTask(task);
        System.out.println(tasks.toString());
        System.out.println(getTaskText());
        System.out.println(getAnswerStatus());

        answerStatus = null;

        // Neue Seite laden mit Zugriff auf Controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/TrueFalsePage2.fxml"));
        Parent root = loader.load();

        // Zugriff auf den Controller der neuen Seite
        TrueFalseController2 controller2 = loader.getController();
        controller2.setTaskTextPage2(getTaskText());  // <-- Text wird jetzt korrekt gesetzt

        // Szene anzeigen
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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

