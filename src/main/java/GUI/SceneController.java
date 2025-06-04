package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.Main;
import org.example.dao.dbConn;
import org.example.domain.AufgabeService;
import org.example.domain.Task;
import org.example.domain.benutzerKonto;
import java.io.IOException;
import java.sql.SQLException;

public class SceneController {

    @FXML
    protected void switchScene(ActionEvent event, String newScene) throws IOException {
        //steuert Wechsel zwischen verschiedenen Scenes und sorgt dafür, dass Fenstergröße beibehalten wird

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double sceneWidth = stage.getScene().getWidth();
        double sceneHeight = stage.getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(newScene));
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchScene(Stage stage, String newScene) throws IOException {
        //steuert Wechsel zwischen verschiedenen Scenes und sorgt dafür, dass Fenstergröße beibehalten wird

        double sceneWidth = stage.getScene().getWidth();
        double sceneHeight = stage.getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(newScene));
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException, SQLException {
        //sorgt für den Wechsel zur Startseite

        switchScene(event, "/GUI/StartPage.fxml");
    }

    @FXML
    public void switchToStartPage(Stage stage) throws IOException {
        //sorgt für den Wechsel zur Startseite

        switchScene(stage, "/GUI/StartPage.fxml");
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        //sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(event, "/GUI/ExamPage.fxml");
    }

    @FXML
    public void switchToExamPage(Stage stage) throws IOException {
        //sorgt für den Wechsel von Startseite zur Klausurerstellungsseite

        switchScene(stage, "/GUI/ExamPage.fxml");
    }

    @FXML
    public void switchToExamCollection(ActionEvent event) throws IOException {
        //sorgt für den Wechsel von Startseite zur Klausurerübersichtsseite

        switchScene(event, "/GUI/ExamCollection.fxml");
    }

    @FXML
    public void switchToExamCollection(Stage stage) throws IOException {
        //sorgt für den Wechsel von Startseite zur Klausurerübersichtsseite

        switchScene(stage, "/GUI/ExamCollection.fxml");
    }


    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException {
        //sorgt für den Wechsel von Startseite zur Aufgabenübersicht

        switchScene(event, "/GUI/TaskOverview.fxml");
    }

    @FXML
    public void switchToTaskOverview(Stage stage) throws IOException {
        //sorgt für den Wechsel von Startseite zur Aufgabenübersicht

        switchScene(stage, "/GUI/TaskOverview.fxml");
    }

    @FXML
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        //sorgt für den Wechsel von Startseite zur Seite zum Aufgabe hinzufügen

        switchScene(event, "/GUI/AddTaskPage.fxml");
    }

    @FXML
    public void switchToAddTaskPage(Stage stage) throws IOException {
        //sorgt für den Wechsel von Startseite zur Seite zum Aufgabe hinzufügen

        switchScene(stage, "/GUI/AddTaskPage.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        //meldet den aktuellen Nutzer ab und wechselt zu Anmeldeseite

        benutzerKonto konto = new benutzerKonto();
        konto.abmelden();
        dbConn.closePool();
        switchToTitlePage(event);
    }

    @FXML
    public void logout(Stage stage) throws IOException {
        //meldet den aktuellen Nutzer ab und wechselt zu Anmeldeseite

        benutzerKonto konto = new benutzerKonto();
        konto.abmelden();
        dbConn.closePool();
        switchToTitlePage(stage);
    }

    @FXML
    public void switchToLoginPage(ActionEvent event) throws IOException, SQLException {
        //sorgt für den Wechsel zur Anmeldeseite
       switchScene(event, "/GUI/LoginPage.fxml");
    }

    @FXML
    public void switchToSignUpPage(ActionEvent event) throws IOException {
        //sorgt für den Wechsel zur Registrierungsseite
        switchScene(event, "/GUI/SignUpPage.fxml");
    }

    @FXML
    protected void switchToTitlePage(ActionEvent event) throws IOException {
        //sorgt für den Wechsel zur Anfangsseite
        switchScene(event, "/GUI/TitlePage.fxml");
    }

    @FXML
    protected void switchToTitlePage(Stage stage) throws IOException {
        //Methode sorgt für den Wechsel zur Startseite
        switchScene(stage, "/GUI/TitlePage.fxml");
    }

    protected boolean showAlert() {
        //Zeigt Fehlermeldung an, wenn Daten beim Seitenwechsel verloren gehen würden

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Achtung!");
        alert.setHeaderText(null);
        alert.setContentText("Wenn Sie die Seite verlassen, werden Ihre Daten nicht gespeichert!");

        boolean[] result = new boolean[1];

        //Überprüft, welche Schaltfläche der Benutzer gedrückt hat
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result[0] = true;
            }
        });

        return result[0];
    }

    protected boolean showAlert(String message) {
        //Zeigt Fehlermeldung mit beliebigem Text an

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Fehler!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        boolean[] result = new boolean[1];

        //Überprüft, welche Schaltfläche der Benutzer gedrückt hat
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result[0] = true;
            }
        });
        return result[0];
    }

    public void saveTask(boolean editMode, Task selectedTask, AufgabeService aufgabe, String question, Stage stage) throws SQLException, IOException {
        //speichert eine Aufgabe nach dem Erstellen in der Datenbank ab

        if(!editMode){
            aufgabe.setTask(question);
            aufgabe.save();
            switchToStartPage(stage);
        }
        else{
            //ToDo: Aufgabe updaten statt löschen und neu speichern
            aufgabe.setTask(question);
            aufgabe.save();
            Task.deleteTask(selectedTask);
            Task.getAllTasks(Main.id);
            switchToTaskOverview(stage);
        }
    }

}
