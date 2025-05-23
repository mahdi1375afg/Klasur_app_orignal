package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
    @FXML
    private TextField textFieldDuration;



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

    @FXML
    private ComboBox<String> modulDropdown;


    private String examTitle;
    private String examiner;
    private LocalDate examDate;
    private Integer numberPoints;
    private Modul modul;
    private int time;
    private List<BloomLevel> bloomLevel = new ArrayList<>();
    private List<AntwortType> antwortType = new ArrayList<>();
    private List<QuestionType> questionType = new ArrayList<>();

    private int amountOpen;
    private int amountSingleChoice;
    private int amountMultipleChoice;
    private int amountTrueFalse;
    private int amountGapText;
    private int amountAssign;
    private int amountRanking;


    @FXML
    public void initialize() throws SQLException {
        modulDropdown.getItems().clear();
        modulDropdown.getItems().addAll(Modul.getAllNames());

        textFieldExamTitle.textProperty().addListener((observable, oldValue, newValue) -> setExamTitle());

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
        String input = textFieldExamDate.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        if (input.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            try {
                LocalDate parsedDate = LocalDate.parse(input, formatter);
                this.examDate = parsedDate;

                // Nur wenn erfolgreich: Rückschreiben formatiert
                textFieldExamDate.setText(parsedDate.format(formatter));
            } catch (DateTimeParseException e) {
                // Optional: Fehlermeldung oder leer lassen
                this.examDate = null;
            }
        } else {
            this.examDate = null;
        }
    }

    public void setTime() {
        String time = textFieldDuration.getText();
        try {
            Integer timeInteger = Integer.parseInt(time);

            if (timeInteger >= 0) {
                this.time = timeInteger;
            }
        } catch (NumberFormatException ignored) {
            this.time = Integer.parseInt(null);
        }
    }

    public void setModul() throws SQLException {
        for(Modul modul : Modul.modules) {
            if(modul.getName().equals(modulDropdown.getValue())) {
                this.modul = modul;
            }
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
        setModul();
        setTime();
        setExamDate();

        if(modul == null) {
            showAlert("Bitte geben sie ein Modul an!");
            return;
        }

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
        System.out.println("Examiner:" + examiner);
        System.out.println("Time: " + time);
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

        Map<QuestionType, Integer> questionTypes = new HashMap<QuestionType, Integer>();

        for(QuestionType questionType : questionType) {
            if(questionType == QuestionType.offen) {
                questionTypes.put(questionType, amountOpen);
            } else if(questionType == QuestionType.singleChoiceFragen) {
                questionTypes.put(questionType, amountSingleChoice);
            } else if(questionType == QuestionType.multipleChoiceFragen) {
                questionTypes.put(questionType, amountMultipleChoice);
            } else if(questionType == QuestionType.wahrOderFalsch) {
                questionTypes.put(questionType, amountTrueFalse);
            } else if(questionType == QuestionType.leerstellen) {
                questionTypes.put(questionType, amountGapText);
            } else if(questionType == QuestionType.zuordnung) {
                questionTypes.put(questionType, amountAssign);
            } else if(questionType == QuestionType.ranking) {
                questionTypes.put(questionType, amountRanking);
            }
        }


        ExamService exam = new ExamService(examTitle,examDate,numberPoints,time,modul,questionTypes,bloomLevel,benutzerKonto.aktuellerBenutzer.getId());

        switch(exam.createKlausur()) {
            case 0:
                System.out.println("Erfolgreich erstellt!");
                super.switchToStartPage(event);
                break;
            case 1:
                showAlert("Nach Filter Modul, sind nicht mehr genug Aufgaben da. Es werden mehr Aufgabe unter diesem Modul gebraucht!");
                break;
            case 2:
                showAlert("Nach Filter Bloom, sind nicht mehr genug Aufgaben da. Es werden mehr Aufgabe unter einem Bloom gebraucht!");
                break;
            case 3:
                showAlert("Nach Filter Typ, sind nicht mehr genug Aufgaben da. Es werden mehr Aufgabe unter einem Typ gebraucht!");
                break;
            case 4:
                showAlert("Kein Task mit passendem Typ gefunden");
                break;
        }


    }


    @FXML
    public void setTaskTaxonomie() {
        if(bloomLevel != null) {
            bloomLevel.clear();
        }

        if (rButtonTaxonomieRemember.isSelected()) {
            bloomLevel.add(BloomLevel.erinnern);
        }
        if (rButtonTaxonomieUnderstand.isSelected()) {
            bloomLevel.add(BloomLevel.verstehen);
        }
        if (rButtonTaxonomieApply.isSelected()) {
            bloomLevel.add(BloomLevel.anwenden);
        }
        if (rButtonTaxonomieAnalyze.isSelected()) {
            bloomLevel.add(BloomLevel.analysieren);
        }
        if (rButtonTaxonomieRate.isSelected()) {
            bloomLevel.add(BloomLevel.bewerten);
        }
        if (rButtonTaxonomieCreate.isSelected()) {
            bloomLevel.add(BloomLevel.erschaffen);
        }
    }

    @FXML
    public void setTaskType() {
        if(antwortType != null) {
            antwortType.clear();
        }
        if(questionType != null) {
            questionType.clear();
        }

        if (rButtonTypOpen.isSelected()) {
            antwortType.add(AntwortType.offeneAntwort);
            questionType.add(QuestionType.offen);
        }
        if (rButtonTypSingle.isSelected()) {
            antwortType.add(AntwortType.geschlosseneAntwort);
            questionType.add(QuestionType.singleChoiceFragen);
        }
        if (rButtonTypMultiple.isSelected()) {
            antwortType.add(AntwortType.geschlosseneAntwort);
            questionType.add(QuestionType.multipleChoiceFragen);
        }
        if (rButtonTypTrueFalsch.isSelected()) {
            antwortType.add(AntwortType.geschlosseneAntwort);
            questionType.add(QuestionType.wahrOderFalsch);
        }
        if (rButtonTypGapText.isSelected()) {
            antwortType.add(AntwortType.geschlosseneAntwort);
            questionType.add(QuestionType.leerstellen);
        }
        if (rButtonTypAssign.isSelected()) {
            antwortType.add(AntwortType.geschlosseneAntwort);
            questionType.add(QuestionType.zuordnung);
        }
        if (rButtonTypRanking.isSelected()) {
            antwortType.add(AntwortType.geschlosseneAntwort);
            questionType.add(QuestionType.ranking);
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
