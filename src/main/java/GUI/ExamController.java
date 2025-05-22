package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TextField textFieldExamDate;
    @FXML
    private TextField textFieldExaminer;
    @FXML
    private TextField textFieldNumberPoints;


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

    // Spinner für Aufgabentyp
    @FXML
    private Spinner<Integer> spinnerAmountOpenQuestion;
    @FXML
    private Spinner<Integer> spinnerAmountSingleChoice;
    @FXML
    private Spinner<Integer> spinnerAmountMultipleChoice;
    @FXML
    private Spinner<Integer> SpinnerAmountTrueFalse;
    @FXML
    private Spinner<Integer> SpinnerAmountGapText;
    @FXML
    private Spinner<Integer> spinnerAmountAssign;
    @FXML
    private Spinner<Integer> spinnerAmountRanking;


    @FXML
    private MenuButton menuBar;


    private String examTitle;
    private String examiner;
    private Date examDate;
    private Integer numberPoints;
    private BloomLevel bloomLevel;
    private AntwortType antwortType;
    private QuestionType closeType;

    private int amountOpen;
    private int amountSingleChoice;
    private int amountMultipleChoice;
    private int amountTrueFalse;
    private int amountGapText;
    private int amountAssign;
    private int amountRanking;


    @FXML
    public void initialize() {


        textFieldExamTitle.textProperty().addListener((observable, oldValue, newValue) -> setExamTitle());

        textFieldExamDate.textProperty().addListener((observable, oldValue, newValue) -> setExamDate());

        textFieldExaminer.textProperty().addListener((observable, oldValue, newValue) -> setExaminer());

        textFieldNumberPoints.textProperty().addListener((observable, oldValue, newValue) -> setNumberPoints());

        spinnerAmountOpenQuestion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountSingleChoice.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountMultipleChoice.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        SpinnerAmountTrueFalse.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        SpinnerAmountGapText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountAssign.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountRanking.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));

        //Spinner deaktivieren
        spinnerAmountOpenQuestion.setDisable(true);
        spinnerAmountSingleChoice.setDisable(true);
        spinnerAmountMultipleChoice.setDisable(true);
        SpinnerAmountTrueFalse.setDisable(true);
        SpinnerAmountGapText.setDisable(true);
        spinnerAmountAssign.setDisable(true);
        spinnerAmountRanking.setDisable(true);

        // Spinner aktivieren wenn Aufgabnetyp ausgewählt wird
        rButtonTypOpen.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
            spinnerAmountOpenQuestion.setDisable(!newSelected);
        });
        rButtonTypSingle.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
            spinnerAmountSingleChoice.setDisable(!newSelected);
        });
        rButtonTypMultiple.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
            spinnerAmountMultipleChoice.setDisable(!newSelected);
        });
        rButtonTypTrueFalsch.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
            SpinnerAmountTrueFalse.setDisable(!newSelected);
        });
        rButtonTypGapText.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
            SpinnerAmountGapText.setDisable(!newSelected);
        });
        rButtonTypAssign.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
            spinnerAmountAssign.setDisable(!newSelected);
        });
        rButtonTypRanking.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
            spinnerAmountRanking.setDisable(!newSelected);
        });

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
        examTitle = textFieldExamTitle.getText();
    }

    public void setExaminer() {
        examiner = textFieldExaminer.getText();
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

        if (examiner == null) {
            showAlert("Bitte geben Sie einen Prüfer an!");
            return;
        }

        if (numberPoints == null) {
            showAlert("Bitte geben Sie eine korrekte Punktezahl an!");
            return;
        }

        amountOpen = spinnerAmountOpenQuestion.getValue();
        amountSingleChoice = spinnerAmountSingleChoice.getValue();
        amountMultipleChoice = spinnerAmountMultipleChoice.getValue();
        amountTrueFalse = SpinnerAmountTrueFalse.getValue();
        amountGapText = SpinnerAmountGapText.getValue();
        amountAssign = spinnerAmountAssign.getValue();
        amountRanking = spinnerAmountRanking.getValue();


        System.out.println("Exam Title: " + examTitle);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = formatter.format(examDate);
        System.out.println("Exam Date: " + formattedDate);
        System.out.println("Examiner:" + examiner);
        System.out.println("Number Points: " + numberPoints);

        if (amountOpen > 0) {
            System.out.println("Anzahl offene Aufgaben: " + amountOpen);
        }
        if (amountSingleChoice > 0) {
            System.out.println("Anzahl Single-Choice Aufgaben: " + amountSingleChoice);
        }
        if (amountMultipleChoice > 0) {
            System.out.println("Anzahl Multiple-Choice Aufgaben: " + amountMultipleChoice);
        }
        if (amountTrueFalse > 0) {
            System.out.println(" Anzahl True/False Aufgaben: " + amountTrueFalse);
        }
        if (amountGapText > 0) {
            System.out.println(" Anzahl Lückentext Aufgaben: " + amountTrueFalse);
        }
        if (amountAssign > 0) {
            System.out.println(" Anzahl Zuordnungsaufgaben: " + amountAssign);
        }
        if (amountRanking > 0) {
            System.out.println("Anzahl Ranking Aufgaben: " + amountRanking);
        }

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
    public void switchToTaskOverview(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToTaskOverview(stage);
    }

    @FXML
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToAddTaskPage(stage);
    }

    @FXML
    public void switchToExamOverview() throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamCollection(stage);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.logout(stage);
    }

    @Override
    protected boolean showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Achtung!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getButtonTypes().setAll(ButtonType.OK);

        alert.showAndWait();
        return true;
    }
}
