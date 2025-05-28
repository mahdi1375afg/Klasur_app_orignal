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

    private String taskTitleText ;
    private String modulTitleText;
    private Integer numberPointsInteger;
    private AntwortType antwortType;
    private BloomLevel bloomLevel;
    private Integer durationNumber;
    private QuestionType closeType;
    private boolean isEditMode = false;
    private Task selectedTask;
    @FXML
    private ComboBox<String> modulDropdown;

    @FXML
    public void initialize() throws SQLException {
        //wird zu Beginn ausgeführt und sorgt dafür, dass bei Änderungen in den Textfeldern automatisch die entsprechenden Setter aufgerufen werden

        modulDropdown.getItems().clear();
        modulDropdown.getItems().addAll(Modul.getAllNames());

        textFieldTaskTitle.textProperty().addListener((observable, oldValue, newValue) -> setTaskTitle());

        modulDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setModulTitle());

        textFieldNumberPoints.textProperty().addListener((observable, oldValue, newValue) -> setNumberPoints());

        textFieldDuration.textProperty().addListener((observable, oldValue, newValue) -> setDurationNumber());
    }

    public void initializeEditMode(Task aufgabe) {
        //Methode mit der die Inhalte einer bereits vorhandenen Aufgabe übertragen werden können
        this.isEditMode = true;
        selectedTask = aufgabe;

        textFieldTaskTitle.setText(aufgabe.getQuestion().getName());
        textFieldNumberPoints.setText(String.valueOf(aufgabe.getQuestion().getPoints()));
        textFieldDuration.setText(String.valueOf((aufgabe).getQuestion().getTime()));

        modulDropdown.setValue(aufgabe.getModul().getName());

        switch (aufgabe.getAnswer().getFirst().getTyp().getName()) {
            case "offen" -> rButtonTypOpen.setSelected(true);
            case "Single-Choice" -> rButtonTypSingle.setSelected(true);
            case "Multiple-Choice" -> rButtonTypMultiple.setSelected(true);
            case "Wahr/Falsch" -> rButtonTypTrueFalsch.setSelected(true);
            case "Lückentext" -> rButtonTypGapText.setSelected(true);
            case "Zuordnung" -> rButtonTypAssign.setSelected(true);
            case "Ranking" -> rButtonTypRanking.setSelected(true);
        }
        setTaskType();

        switch (aufgabe.getQuestion().getTaxonomie().getKategorie()) {
            case "Erinnern" -> rButtonTaxonomieRemember.setSelected(true);
            case "Verstehen" -> rButtonTaxonomieUnderstand.setSelected(true);
            case "Anwenden" -> rButtonTaxonomieApply.setSelected(true);
            case "Analysieren" -> rButtonTaxonomieAnalyze.setSelected(true);
            case "Bewerten" -> rButtonTaxonomieRate.setSelected(true);
            case "Erschaffen" -> rButtonTaxonomieCreate.setSelected(true);
        }
        setTaskTaxonomie();
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

    public void setNewModul() throws SQLException {
        modulTitleText = textFieldNewModul.getText();
        benutzerKonto konto = new benutzerKonto();

        if (modulTitleText.isEmpty()) {
            showAlert("Fehler", "Bitte geben Sie einen Modulnamen ein.");
            return;
        }

        if (Modul.getAllNames().contains(modulTitleText)) {
            showAlert("Hinweis", "Dieses Modul existiert bereits.");
            return;
        }
        konto.createModul(modulTitleText);
        modulDropdown.getItems().setAll(Modul.getAllNames());
        textFieldNewModul.clear();
    }

    public String getModulTitle() {
        return modulTitleText;
    }

    public void setNumberPoints() {
        String points = textFieldNumberPoints.getText();
        try {
            int pointsInteger = Integer.parseInt(points);

            if (pointsInteger >= 0 && pointsInteger < 1000) {
                numberPointsInteger = pointsInteger;
            }
            else{
                numberPointsInteger = null;
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
            int tempInteger = Integer.parseInt(temp);

            if (tempInteger >= 0 && tempInteger < 1000) {
                durationNumber = tempInteger;
            }
            else{
                durationNumber = null;
            }
        } catch (NumberFormatException ignored) {}
    }

    public Integer getDurationNumber() {
        return durationNumber;
    }

    @FXML
    public void switchToAddTaskPageContent(ActionEvent event) throws IOException, SQLException {
        //Schickt alle gesammelten Informationen beim Seitenwechsel an Anwendungsschicht

        if (taskTitleText == null || taskTitleText.trim().isEmpty()) {
            showAlert(null, "Bitte geben Sie einen Namen an!");
            return;
        }

        if (modulTitleText == null || modulTitleText.trim().isEmpty()) {
            showAlert(null, "Bitte geben Sie ein Modul an!");
            return;
        }

        if (numberPointsInteger == null) {
            showAlert("Fehler", "Bitte geben Sie eine korrekte Punktezahl an!");
            return;
        }

        if (durationNumber == null) {
            showAlert("Fehler", "Bitte geben Sie eine korrekte Dauer an!");
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

        //Aufgabe Objekt erstellen und alle Informationen die wir hier bekommen einfüllen
        AufgabeService aufgabe = new AufgabeService();
        aufgabe.setTaskPageData(getTextFieldTaskTitle(), getTextFieldNumberPoints(), getDurationNumber(), getAntwortType(), getTaskTaxonomie(), getModulTitle(), closeType);

        if (!isEditMode) {
            if (antwortType == AntwortType.offeneAntwort) {
                OpenQuestionController controller = switchSceneAndGetController(event, "/GUI/OpenQuestionPage.fxml");
                controller.setAufgabe(aufgabe);
            } else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.singleChoiceFragen) {
                SingleChoiceController controller = switchSceneAndGetController(event, "/GUI/SingleChoicePage.fxml");
                controller.setAufgabe(aufgabe);
            } else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.wahrOderFalsch) {
                TrueFalseController controller = switchSceneAndGetController(event, "/GUI/TrueFalsePage.fxml");
                controller.setAufgabe(aufgabe);
            } else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.multipleChoiceFragen) {
                MultipleChoiceController controller = switchSceneAndGetController(event, "/GUI/MultipleChoicePage.fxml");
                controller.setAufgabe(aufgabe);
            } else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.zuordnung) {
                AssignController controller = switchSceneAndGetController(event, "/GUI/AssignPage.fxml");
                controller.setAufgabe(aufgabe);
            } else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.leerstellen) {
                GapTextController controller = switchSceneAndGetController(event, "/GUI/GapText.fxml");
                controller.setAufgabe(aufgabe);
            } else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.ranking) {
                RankingController controller = switchSceneAndGetController(event, "/GUI/RankingPage.fxml");
                controller.setAufgabe(aufgabe);
            } else {
                super.switchToStartPage(event);
            }
        }
        else{
            //ToDo:Bearbeiten für alle Aufgaben

            if (antwortType == AntwortType.offeneAntwort) {
                OpenQuestionController controller = switchSceneAndGetController(event, "/GUI/OpenQuestionPage.fxml");
                controller.initializeEditMode(selectedTask);
                controller.setAufgabe(aufgabe);
            }
            else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.leerstellen) {
                GapTextController controller = switchSceneAndGetController(event, "/GUI/GapText.fxml");
                controller.initializeEditMode(selectedTask);
                controller.setAufgabe(aufgabe);
            }
            else if (antwortType == AntwortType.geschlosseneAntwort && closeType == QuestionType.singleChoiceFragen) {
                SingleChoiceController controller = switchSceneAndGetController(event, "/GUI/SingleChoicePage.fxml");
                controller.initializeEditMode(selectedTask);
                controller.setAufgabe(aufgabe);
            }

        }
    }

    private <T> T switchSceneAndGetController(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        T controller = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double sceneWidth = stage.getScene().getWidth();
        double sceneHeight = stage.getScene().getHeight();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();

        return controller;
    }


    @FXML
    public void setTaskType() {

        if (rButtonTypOpen.isSelected()) {
            antwortType = AntwortType.offeneAntwort;
        } else if (rButtonTypSingle.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = QuestionType.singleChoiceFragen;
        } else if (rButtonTypMultiple.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = QuestionType.multipleChoiceFragen;
        } else if (rButtonTypTrueFalsch.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = QuestionType.wahrOderFalsch;
        } else if (rButtonTypGapText.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = QuestionType.leerstellen;
        } else if (rButtonTypAssign.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = QuestionType.zuordnung;
        } else if (rButtonTypRanking.isSelected()) {
            antwortType = AntwortType.geschlosseneAntwort;
            closeType = QuestionType.ranking;
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


