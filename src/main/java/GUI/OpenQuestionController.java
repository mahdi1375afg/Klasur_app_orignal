package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class OpenQuestionController extends SceneController{

    @FXML
    private TextField taskTextField;
    @FXML
    private TextField taskSampleSolution;
    @FXML
    private Button finsihed;
    @FXML
    private MenuItem startpage;
    @FXML
    private MenuItem taskOverview;
    @FXML
    private MenuItem logout;
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

    private boolean showAlert() {
        //Zeigt Fehlermeldung an, falls die Seite verlassen wird

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Achtung!");
        alert.setHeaderText(null);
        alert.setContentText("Wenn Sie die Seite verlassen, werden Ihre Daten nicht gespeichert!");

        boolean result[] = new boolean[1];

        //Überprüft, welche Schaltfläche der Benutzer gedrückt hat
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result[0] = true;
            } else if (response == ButtonType.CANCEL) {
                result[0] = false;
            }
        });

        return result[0];
    }

    private boolean showAlert(String message) {
        //Zeigt Fehlermeldung an

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Achtung!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        boolean result[] = new boolean[1];

        //Überprüft, welche Schaltfläche der Benutzer gedrückt hat
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result[0] = true;
            } else if (response == ButtonType.CANCEL) {
                result[0] = false;
            }
        });
        return result[0];
    }


    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        showAlert();
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToStartPage(stage);
    }

    @FXML
    public void savedSwitchToStartPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToStartPage(stage);
    }


    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        showAlert();
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToTaskOverview(stage);
    }


    @FXML
    public void logout(ActionEvent event) throws IOException {
        showAlert();
        super.logout(event);
    }

}
