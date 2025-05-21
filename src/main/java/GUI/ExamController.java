package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.domain.AntwortType;
import org.example.domain.BloomLevel;
import org.example.domain.QuestionType;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamController extends SceneController {

    //Textfelder
    @FXML
    private TextField textFieldExamTitle;
    @FXML
    private TextField textFieldNumberPoints;
    @FXML
    private TextField textFieldExamDate;


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


    private String examTitle;
    private Date examDate;
    private Integer numberPoints;
    private BloomLevel bloomLevel;
    private AntwortType antwortType;
    private QuestionType closeType;

    @FXML
    public void initialize() {


        textFieldExamTitle.textProperty().addListener((observable, oldValue, newValue) -> setExamTitle());

        textFieldNumberPoints.textProperty().addListener((observable, oldValue, newValue) -> setNumberPoints());

        textFieldExamDate.textProperty().addListener((observable, oldValue, newValue) -> setExamDate());
    }

    public void setExamDate() {
        String input = textFieldExamDate.getText();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        formatter.setLenient(false); // kein "32.01.2025" o.ä.

        try {
            Date parsedDate = formatter.parse(input);
            this.examDate = parsedDate;
        } catch (ParseException e) {
            examDate = null;
        }
    }



    public void setNumberPoints() {
        String points = textFieldNumberPoints.getText();
        try {
            Integer pointsInteger = Integer.parseInt(points);

            if (pointsInteger >= 0) {
                numberPoints = pointsInteger;
            }
        } catch (NumberFormatException ignored) {
            numberPoints = null;
        }
    }

    public void setExamTitle() {
        //ToDO: Daten an DB schicken um die Aufgabe zu erstellen
        //ToDo: Wechsel zur richtigen nächsten Seite

        examTitle = textFieldExamTitle.getText();
    }

    @FXML
    public void generateExam(ActionEvent event) throws SQLException, IOException {
        if (examTitle == null || examTitle.trim().isEmpty()) {
            showAlert("Bitte geben Sie einen korrekten Titel an!");
            return;
        }

        if (examDate == null) {
            showAlert("Bitte geben Sie ein korrektes Datum an!");
            return;
        }

        if (numberPoints == null) {
            showAlert("Bitte geben Sie eine korrekte Punktezahl an!");
            return;
        }

        System.out.println("Exam Title: " + examTitle);
        System.out.println("Number Points: " + numberPoints);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = formatter.format(examDate);
        System.out.println("Exam Date: " + formattedDate);

        super.switchToStartPage(event);

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
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToAddTaskPage(stage);
    }

    @FXML
    public void switchToExamOverview() throws IOException{
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamCollection(stage);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.logout(stage);
    }
}
