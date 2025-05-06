package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.stage.Stage;
import org.example.domain.AntwortType;
import org.example.domain.BloomLevel;
import org.example.domain.Nutzer;
import org.example.domain.benutzerKonto;

public class TaskPageController extends SceneController {

    //Textfelder
    @FXML
    private TextField taskTitle;
    @FXML
    private TextField modulTitle;
    @FXML
    private TextField numberPoints;
    @FXML
    TextField duration;

    //RadioButtons für Aufgabentyp
    @FXML
    private RadioButton rButtonTypOpen;
    @FXML
    private RadioButton rButtonTypSingle;
    @FXML
    private RadioButton rButtonTypMultiple;
    @FXML
    private RadioButton rButtonTypTrueFalsch;
    @FXML
    private RadioButton rButtonTypGapText;
    @FXML
    private RadioButton rButtonTypAssign;
    @FXML
    private RadioButton rButtonTypRanking;

    //RadioButtons für Taxonomie
    @FXML
    private RadioButton rButtonTaxonomieRemember;
    @FXML
    private RadioButton rButtonTaxonomieUnderstand;
    @FXML
    private RadioButton rButtonTaxonomieApply;
    @FXML
    private RadioButton rButtonTaxonomieAnalyze;
    @FXML
    private RadioButton rButtonTaxonomieRate;
    @FXML
    private RadioButton rButtonTaxonomieCreate;

    @FXML
    private MenuButton menuBar;

    private String taskTitleText = null;
    private String modulTitleText = null;
    private Integer numberPointsInteger = null;
    private AntwortType antwortType = null;
    private BloomLevel bloomLevel = null;
    private Integer durationNumber = null;

    @FXML
    public void initialize() {
        //wird zu Beginn ausgeführt und sorgt dafür, dass bei Änderungen in den Textfeldern automatisch die entsprechenden Setter aufgerufen werden

        taskTitle.textProperty().addListener((observable, oldValue, newValue) -> setTaskTitle());

        modulTitle.textProperty().addListener((observable, oldValue, newValue) -> setModulTitle());

        numberPoints.textProperty().addListener((observable, oldValue, newValue) -> setNumberPoints());

        duration.textProperty().addListener((observable, oldValue, newValue) -> setDurationNumber());
    }

    public String getTaskTitle() {
        return taskTitleText;
    }

    public void setTaskTitle() {
        taskTitleText = taskTitle.getText();
    }

    public void setModulTitle() {
        modulTitleText = modulTitle.getText();
    }

    public String getModulTitle() {

        return modulTitleText;
    }

    public void setNumberPoints() {
        String points = numberPoints.getText();
        try {
            Integer pointsInteger = Integer.parseInt(points);

            if (pointsInteger >= 0) {
                numberPointsInteger = pointsInteger;
            }
        } catch (NumberFormatException ignored) {

        }
    }

    public Integer getNumberPoints() {
        return numberPointsInteger;
    }

    public void setDurationNumber() {
        String temp = duration.getText();
        try {
            Integer tempInteger = Integer.parseInt(temp);

            if (tempInteger >= 0) {
                durationNumber = tempInteger;
            }
        } catch (NumberFormatException ignored) {}
    }
    public Integer getDurationNumber() {
        return durationNumber;
    }

    @FXML
    public void switchToAddTaskPageContent(ActionEvent event) throws IOException, SQLException {
        //Schickt alle gesammelten Informationen beim Seitenwechsel an Anwendungsschicht
         //ToDo: Title, Modul, Typ. Taxonomie und Punkte an Anwendungsschicht schicken
         //ToDO: --> Schnittstelle definieren

        if (taskTitleText == null || taskTitleText.trim().isEmpty()) {
            showAlert(null, "Bitte geben Sie einen Title an!");
            return;
        }

        if (modulTitleText == null || modulTitleText.trim().isEmpty()) {
            showAlert(null, "Bitte geben Sie ein Modul an!");
            return;
        }

        if (numberPointsInteger == null) {
            showAlert("Fehler", "Bitte geben Sie eine Punktezahl an!");
            return;
        }

        if(durationNumber == null){
            showAlert("Fehler", "Bitte geben Sie die Dauer an!");
            return;
        }
        if (antwortType == null) {
            showAlert("Fehler", "Bitte geben Sie einen Aufgabentyp an!");
            return;
        }

        if (bloomLevel == null) {
            showAlert("Fehler", "Bitte geben Sie eine Taxonomie an!");
            return;
        }

        System.out.println(getTaskTitle());
        System.out.println(getModulTitle());
        System.out.println(getNumberPoints());
        System.out.println(getDurationNumber());
        System.out.println(getAntwortType());
        System.out.println(getTaskTaxonomie());
        //ToDo: Daten an die nächste Seite weitergeben
        //Damit das Funktioniert muss vorher in DB dieser Befehl ausgeführt werden:
        //INSERT INTO benutzer (benutzername, passwort)
        //VALUES ('testuser', 'geheimespasswort');
        // Achso ja es muss ein User mit id 1 existieren
        benutzerKonto konto = new benutzerKonto();

        if(antwortType == AntwortType.offeneAntwort){
            switchScene(event, "/GUI/OpenQuestionPage.fxml");
        }
        else if(antwortType == AntwortType.geschlosseneAntwort){
            switchScene(event, "/GUI/TrueFalsePage.fxml");
        }
        //ToDo: SceneSwitch abhängig vom Aufgabentyp machen
        else {
            super.switchToStartPage(event);
        }
    }

    @FXML
    public void setTaskType() {
        //ToDo: Enum-AntwortType anpassen, dass es mehr Fragetypen (nicht nur zwei) gibt

        if (rButtonTypOpen.isSelected()) {
            antwortType = AntwortType.offeneAntwort;
        } else if (rButtonTypSingle.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
        } else if (rButtonTypMultiple.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
        } else if (rButtonTypTrueFalsch.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
        } else if (rButtonTypGapText.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
        } else if (rButtonTypAssign.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
        } else if (rButtonTypRanking.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
        }
    }

    public String getAntwortType() {
        return antwortType.getName();
    }

    public String getTaskTaxonomie() {
        return bloomLevel.getKategorie();
    }

    @FXML
    public void setTaskTaxonomie() {

        if (rButtonTaxonomieRemember.isSelected()) {
            bloomLevel = BloomLevel.erinnern;
        } else if (rButtonTaxonomieUnderstand.isSelected()) {
            bloomLevel = BloomLevel.verstehen;
        } else if (rButtonTaxonomieApply.isSelected()) {
            bloomLevel = BloomLevel.anwenden;
        } else if (rButtonTaxonomieAnalyze.isSelected()) {
            bloomLevel = BloomLevel.analysieren;
        } else if (rButtonTaxonomieRate.isSelected()) {
            bloomLevel = BloomLevel.bewerten;
        } else if (rButtonTaxonomieCreate.isSelected()) {
            bloomLevel = BloomLevel.erschaffen;
        }
    }

    private void showAlert(String title, String message) {
        //Zeigt Fehlermeldung an, falls ein Feld leer ist

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    public void switchToExamOverview(ActionEvent event) throws IOException{
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamCollection(stage);
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamPage(stage);
    }


        @FXML
    public void logout(ActionEvent event) throws IOException {
        super.logout(event);
    }
}


