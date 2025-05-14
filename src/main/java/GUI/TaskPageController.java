package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

import javafx.stage.Stage;
import org.example.domain.*;

public class TaskPageController extends SceneController {

    //Textfelder
    @FXML
    private TextField textFieldTaskTitle;
    @FXML
    private TextField textFieldNewModul;
    @FXML
    private TextField textFieldNumberPoints;
    @FXML
    TextField textFieldDuration;

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

    @FXML
    private Button buttonAddModul;

    private String taskTitleText ;
    private String modulTitleText;
    private Integer numberPointsInteger;
    private AntwortType antwortType;
    private BloomLevel bloomLevel;
    private Integer durationNumber;
    private CloseType closeType;

    @FXML
    private ComboBox<String> modulDropdown;

    @FXML
    public void initialize() {
        //wird zu Beginn ausgeführt und sorgt dafür, dass bei Änderungen in den Textfeldern automatisch die entsprechenden Setter aufgerufen werden

        modulDropdown.getItems().addAll(Modul.getAllModul());

        textFieldTaskTitle.textProperty().addListener((observable, oldValue, newValue) -> setTaskTitle());

        modulDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setModulTitle());

        textFieldNumberPoints.textProperty().addListener((observable, oldValue, newValue) -> setNumberPoints());

        textFieldDuration.textProperty().addListener((observable, oldValue, newValue) -> setDurationNumber());
    }

    public String getTextFieldTaskTitle() {
        return taskTitleText;
    }

    public void setTaskTitle() {
        taskTitleText = textFieldTaskTitle.getText();
    }

    public void setModulTitle() {
        modulTitleText = modulDropdown.getValue();
    }

    public void setNewModul() {
        //ToDo: Neues Modul in DB einfügen
        modulTitleText = textFieldNewModul.getText();

    }

    public String getModulTitle() {
        return modulTitleText;
    }

    public void setNumberPoints() {
        String points = textFieldNumberPoints.getText();
        try {
            Integer pointsInteger = Integer.parseInt(points);

            if (pointsInteger >= 0) {
                numberPointsInteger = pointsInteger;
            }
        } catch (NumberFormatException ignored) {

        }
    }

    public Integer getTextFieldNumberPoints() {
        return numberPointsInteger;
    }

    public void setDurationNumber() {
        String temp = textFieldDuration.getText();
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
         //    ToDo: Title, Modul, Typ. Taxonomie und Punkte an Anwendungsschicht schicken

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

        /*
        System.out.println(getTextFieldTaskTitle());
        System.out.println(getModulTitle());
        System.out.println(getTextFieldNumberPoints());
        System.out.println(getDurationNumber());
        System.out.println(getAntwortType());
        System.out.println(getTaskTaxonomie());
         */

        //Aufgabe Objekt erstellen und alle Informationen die wir hier bekommen einfüllen
        AufgabeService aufgabe = new AufgabeService();
        aufgabe.setTaskPageData(getTextFieldTaskTitle(), getTextFieldNumberPoints(),getDurationNumber(),getAntwortType(),getTaskTaxonomie(),getModulTitle(),closeType);


        if(antwortType == AntwortType.offeneAntwort){
            switchScene(event, "/GUI/OpenQuestionPage.fxml");
        }
        else if (antwortType == AntwortType.geschlosseneAntwort && closeType == CloseType.singleChoiceFragen){
            switchScene(event, "/GUI/SingleChoicePage.fxml");
        }
        else if (antwortType == AntwortType.geschlosseneAntwort && closeType == CloseType.wahrOderFalsch){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/TrueFalsePage.fxml"));
            Parent root = loader.load();

            TrueFalseController controller = loader.getController();
            controller.setAufgabe(aufgabe);  //Aufgabe weitergabe

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            double sceneWidth = stage.getScene().getWidth();
            double sceneHeight = stage.getScene().getHeight();

            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            stage.setScene(scene);
            stage.show();
        }
        else if(antwortType == AntwortType.geschlosseneAntwort && closeType == CloseType.multipleChoiceFragen){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/MultipleChoicePage.fxml"));
            Parent root = loader.load();

            MultipleChoiceController controller = loader.getController(); //Change das zu jeweils Controller
            controller.setAufgabe(aufgabe);  //Aufgabe weitergabe

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            double sceneWidth = stage.getScene().getWidth();
            double sceneHeight = stage.getScene().getHeight();

            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            stage.setScene(scene);
            stage.show();
        }
        else if(antwortType == AntwortType.geschlosseneAntwort && closeType == CloseType.zuordnung){
            switchScene(event, "/GUI/AssignPage.fxml");
        }
        else if(antwortType == AntwortType.geschlosseneAntwort && closeType == CloseType.leerstellen){
            switchScene(event, "/GUI/GapText.fxml");
        }
        else if(antwortType == AntwortType.geschlosseneAntwort && closeType == CloseType.ranking){
            switchScene(event, "/GUI/RankingPage.fxml");
        }
        else {
            super.switchToStartPage(event);
        }
    }

    @FXML
    public void setTaskType() {

        if (rButtonTypOpen.isSelected()) {
            antwortType = AntwortType.offeneAntwort;
        } else if (rButtonTypSingle.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = CloseType.singleChoiceFragen;
        } else if (rButtonTypMultiple.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = CloseType.multipleChoiceFragen;
        } else if (rButtonTypTrueFalsch.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = CloseType.wahrOderFalsch;
        } else if (rButtonTypGapText.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = CloseType.leerstellen;
        } else if (rButtonTypAssign.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = CloseType.zuordnung;
        } else if (rButtonTypRanking.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = CloseType.ranking;
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
    public void switchToExamOverview() throws IOException{
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
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.logout(stage);
    }
}


