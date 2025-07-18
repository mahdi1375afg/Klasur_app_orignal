package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.*;

import java.io.IOException;
import java.sql.SQLException;
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
    private Integer time;
    private final List<BloomLevel> bloomLevel = new ArrayList<>();
    private final List<AnswerType> answerType = new ArrayList<>();
    private final List<QuestionType> questionType = new ArrayList<>();


    @FXML
    public void initialize() throws SQLException {
        //ToDo: Spinner Max-Wert mit DB und Taxonomie anpassen
        modulDropdown.getItems().clear();
        modulDropdown.getItems().addAll(Modul.getAllNames());

        textFieldExamTitle.textProperty().addListener((observable, oldValue, newValue) -> setExamTitle());
        textFieldExaminer.textProperty().addListener((observable, oldValue, newValue) -> setExaminer());
        textFieldNumberPoints.textProperty().addListener((observable, oldValue, newValue) -> setNumberPoints());

        // Alle Spinner initial mit min=0, max=10, start=0
        spinnerAmountOpenQuestion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountSingleChoice.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountMultipleChoice.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        SpinnerAmountTrueFalse.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        SpinnerAmountGapText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountAssign.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinnerAmountRanking.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));

        // Alle Spinner deaktivieren
        spinnerAmountOpenQuestion.setDisable(true);
        spinnerAmountSingleChoice.setDisable(true);
        spinnerAmountMultipleChoice.setDisable(true);
        SpinnerAmountTrueFalse.setDisable(true);
        SpinnerAmountGapText.setDisable(true);
        spinnerAmountAssign.setDisable(true);
        spinnerAmountRanking.setDisable(true);

        // RadioButton-Listener mit Logik zur Spinner-Aktivierung
        setupSpinnerWithRadio(rButtonTypOpen, spinnerAmountOpenQuestion);
        setupSpinnerWithRadio(rButtonTypSingle, spinnerAmountSingleChoice);
        setupSpinnerWithRadio(rButtonTypMultiple, spinnerAmountMultipleChoice);
        setupSpinnerWithRadio(rButtonTypTrueFalsch, SpinnerAmountTrueFalse);
        setupSpinnerWithRadio(rButtonTypGapText, SpinnerAmountGapText);
        setupSpinnerWithRadio(rButtonTypAssign, spinnerAmountAssign);
        setupSpinnerWithRadio(rButtonTypRanking, spinnerAmountRanking);
    }


    private void setupSpinnerWithRadio(RadioButton radio, Spinner<Integer> spinner) {
        radio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                spinner.setDisable(false);
                spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
            } else {
                spinner.setDisable(true);
                spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
            }
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
            int timeInteger = Integer.parseInt(time);

            if (timeInteger >= 0) {
                this.time = timeInteger;
            }
        } catch (NumberFormatException ignored) {
            this.time = null;
        }
    }

    public void setModul(){
        for(Modul modul : Modul.modules) {
            if(modul.getName().equals(modulDropdown.getValue())) {
                this.modul = modul;
            }
        }
    }



    public void setNumberPoints() {
        String points = textFieldNumberPoints.getText();
        try {
            int pointsInteger = Integer.parseInt(points);

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

        int amountOpen = spinnerAmountOpenQuestion.getValue();
        int amountSingleChoice = spinnerAmountSingleChoice.getValue();
        int amountMultipleChoice = spinnerAmountMultipleChoice.getValue();
        int amountTrueFalse = SpinnerAmountTrueFalse.getValue();
        int amountGapText = SpinnerAmountGapText.getValue();
        int amountAssign = spinnerAmountAssign.getValue();
        int amountRanking = spinnerAmountRanking.getValue();


        System.out.println("Exam Title: " + examTitle);
        System.out.println("Examiner:" + examiner);
        System.out.println("Time: " + time);
        System.out.println("Number Points: " + numberPoints);
        Map<QuestionType, Integer> questionTypes = new HashMap<>();

        if (amountOpen > 0) {
            System.out.println("Anzahl offene Aufgaben: " + amountOpen);
            questionTypes.put(QuestionType.offen, amountOpen);
        }
        if (amountSingleChoice > 0) {
            System.out.println("Anzahl Single-Choice Aufgaben: " + amountSingleChoice);
            questionTypes.put(QuestionType.singleChoiceFragen, amountSingleChoice);
        }
        if (amountMultipleChoice > 0) {
            System.out.println("Anzahl Multiple-Choice Aufgaben: " + amountMultipleChoice);
            System.out.println(amountOpen);
            questionTypes.put(QuestionType.multipleChoiceFragen, amountMultipleChoice);
        }
        if (amountTrueFalse > 0) {
            System.out.println(" Anzahl True/False Aufgaben: " + amountTrueFalse);
            questionTypes.put(QuestionType.wahrOderFalsch, amountTrueFalse);
        }
        if (amountGapText > 0) {
            System.out.println(" Anzahl Lückentext Aufgaben: " + amountGapText);
            questionTypes.put(QuestionType.leerstellen, amountGapText);
        }
        if (amountAssign > 0) {
            System.out.println(" Anzahl Zuordnungsaufgaben: " + amountAssign);
            questionTypes.put(QuestionType.zuordnung, amountAssign);
        }
        if (amountRanking > 0) {
            System.out.println("Anzahl Ranking Aufgaben: " + amountRanking);
            questionTypes.put(QuestionType.ranking, amountRanking);
        }


        ExamService exam = new ExamService(examTitle, examiner,examDate,numberPoints,time,modul,questionTypes,bloomLevel, UserAccount.aktuellerBenutzer.getId());

        switch(exam.createKlausur()) {
            case 0:
                System.out.println("Erfolgreich erstellt!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ExamPreview.fxml"));
                Parent root = loader.load();

                ExamPreviewController controller = loader.getController();
                controller.setExamParams(exam);
                controller.loadPDFs();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double sceneWidth = stage.getScene().getWidth();
                double sceneHeight = stage.getScene().getHeight();

                Scene scene = new Scene(root, sceneWidth, sceneHeight);
                stage.setScene(scene);
                stage.show();

                break;
            case 1:
                showAlert("Nicht genügend Aufgaben für das Modul " + modul.getName() + " vorhanden. Bitte fügen Sie weitere Aufgaben hinzu!");
                break;
            case 2:
                showAlert("Nicht genügend Aufgaben für die gewählten Taxonomien vorhanden. Bitte fügen Sie weitere Aufgaben hinzu!");
                break;
            case 3:
                showAlert("Nicht genügend Aufgaben für die gewählten Aufgabentypen vorhanden. Bitte fügen Sie weitere Aufgaben hinzu!");
                break;
            case 4:
                showAlert("Keine passenden Aufgaben vorhanden. Bitte wählen Sie eine anderer Konfiguration zum Erstellen aus!");
                break;
            case 5:
                showAlert("Keine passenden Aufgaben vorhanden. Bitte füge mehr Aufgaben unter den gewählten Bloom hinzu!");
                break;
        }
    }


    @FXML
    public void setTaskTaxonomie() {

        bloomLevel.clear();

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

    public void setExamParams(ExamService exam) {
        if (exam == null) return;

        // Titel
        textFieldExamTitle.setText(exam.getName());

        // Prüfer
        textFieldExaminer.setText(exam.getPruefer());

        // Datum als String setzen
        if (exam.getDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            textFieldExamDate.setText(exam.getDate().format(formatter));
        } else {
            textFieldExamDate.setText("");
        }

        // Anzahl Punkte
        if (exam.getTotalPoints() != null) {
            textFieldNumberPoints.setText(String.valueOf(exam.getTotalPoints()));
        } else {
            textFieldNumberPoints.setText("");
        }

        // Dauer (Zeit)
        if (exam.getTotalTime() != null) {
            textFieldDuration.setText(String.valueOf(exam.getTotalTime()));
        } else {
            textFieldDuration.setText("");
        }

        // Modul Dropdown setzen
        if (exam.getModul() != null) {
            modulDropdown.setValue(exam.getModul().getName());
            this.modul = exam.getModul();
        }

        // Frage-Typen und Spinner befüllen
        Map<QuestionType, Integer> questionTypes = exam.getQuestionType();
        if (questionTypes != null) {
            // Zuerst alle RadioButtons und Spinner zurücksetzen
            rButtonTypOpen.setSelected(false);
            spinnerAmountOpenQuestion.setDisable(true);
            spinnerAmountOpenQuestion.getValueFactory().setValue(0);

            rButtonTypSingle.setSelected(false);
            spinnerAmountSingleChoice.setDisable(true);
            spinnerAmountSingleChoice.getValueFactory().setValue(0);

            rButtonTypMultiple.setSelected(false);
            spinnerAmountMultipleChoice.setDisable(true);
            spinnerAmountMultipleChoice.getValueFactory().setValue(0);

            rButtonTypTrueFalsch.setSelected(false);
            SpinnerAmountTrueFalse.setDisable(true);
            SpinnerAmountTrueFalse.getValueFactory().setValue(0);

            rButtonTypGapText.setSelected(false);
            SpinnerAmountGapText.setDisable(true);
            SpinnerAmountGapText.getValueFactory().setValue(0);

            rButtonTypAssign.setSelected(false);
            spinnerAmountAssign.setDisable(true);
            spinnerAmountAssign.getValueFactory().setValue(0);

            rButtonTypRanking.setSelected(false);
            spinnerAmountRanking.setDisable(true);
            spinnerAmountRanking.getValueFactory().setValue(0);

            // Jetzt entsprechend der übergebenen Werte aktivieren und setzen
            for (Map.Entry<QuestionType, Integer> entry : questionTypes.entrySet()) {
                QuestionType type = entry.getKey();
                int amount = entry.getValue();

                switch (type) {
                    case offen -> {
                        rButtonTypOpen.setSelected(true);
                        spinnerAmountOpenQuestion.setDisable(false);
                        spinnerAmountOpenQuestion.getValueFactory().setValue(amount);
                    }
                    case singleChoiceFragen -> {
                        rButtonTypSingle.setSelected(true);
                        spinnerAmountSingleChoice.setDisable(false);
                        spinnerAmountSingleChoice.getValueFactory().setValue(amount);
                    }
                    case multipleChoiceFragen -> {
                        rButtonTypMultiple.setSelected(true);
                        spinnerAmountMultipleChoice.setDisable(false);
                        spinnerAmountMultipleChoice.getValueFactory().setValue(amount);
                    }
                    case wahrOderFalsch -> {
                        rButtonTypTrueFalsch.setSelected(true);
                        SpinnerAmountTrueFalse.setDisable(false);
                        SpinnerAmountTrueFalse.getValueFactory().setValue(amount);
                    }
                    case leerstellen -> {
                        rButtonTypGapText.setSelected(true);
                        SpinnerAmountGapText.setDisable(false);
                        SpinnerAmountGapText.getValueFactory().setValue(amount);
                    }
                    case zuordnung -> {
                        rButtonTypAssign.setSelected(true);
                        spinnerAmountAssign.setDisable(false);
                        spinnerAmountAssign.getValueFactory().setValue(amount);
                    }
                    case ranking -> {
                        rButtonTypRanking.setSelected(true);
                        spinnerAmountRanking.setDisable(false);
                        spinnerAmountRanking.getValueFactory().setValue(amount);
                    }
                }
            }
        }

        // BloomLevel aus dem ExamService übernehmen und UI setzen
        bloomLevel.clear();
        if (exam.getBloomLevels() != null) {
            bloomLevel.addAll(exam.getBloomLevels());

            rButtonTaxonomieRemember.setSelected(bloomLevel.contains(BloomLevel.erinnern));
            rButtonTaxonomieUnderstand.setSelected(bloomLevel.contains(BloomLevel.verstehen));
            rButtonTaxonomieApply.setSelected(bloomLevel.contains(BloomLevel.anwenden));
            rButtonTaxonomieAnalyze.setSelected(bloomLevel.contains(BloomLevel.analysieren));
            rButtonTaxonomieRate.setSelected(bloomLevel.contains(BloomLevel.bewerten));
            rButtonTaxonomieCreate.setSelected(bloomLevel.contains(BloomLevel.erschaffen));
        } else {
            rButtonTaxonomieRemember.setSelected(false);
            rButtonTaxonomieUnderstand.setSelected(false);
            rButtonTaxonomieApply.setSelected(false);
            rButtonTaxonomieAnalyze.setSelected(false);
            rButtonTaxonomieRate.setSelected(false);
            rButtonTaxonomieCreate.setSelected(false);
        }
    }


    @FXML
    public void setTaskType() {

        answerType.clear();
        questionType.clear();

        if (rButtonTypOpen.isSelected()) {
            if(spinnerAmountOpenQuestion.getValue() <= 0) {
                showAlert("Aufgabetyp ausgewählt aber keine Anzahl!");
            }
            answerType.add(AnswerType.offeneAntwort);
            questionType.add(QuestionType.offen);
        }
        if (rButtonTypSingle.isSelected()) {
            if(spinnerAmountSingleChoice.getValue() <= 0) {
                showAlert("Aufgabetyp ausgewählt aber keine Anzahl!");
            }
            answerType.add(AnswerType.geschlosseneAntwort);
            questionType.add(QuestionType.singleChoiceFragen);
        }
        if (rButtonTypMultiple.isSelected()) {
            if(spinnerAmountMultipleChoice.getValue() <= 0) {
                showAlert("Aufgabetyp ausgewählt aber keine Anzahl!");
            }
            answerType.add(AnswerType.geschlosseneAntwort);
            questionType.add(QuestionType.multipleChoiceFragen);
        }
        if (rButtonTypTrueFalsch.isSelected()) {
            if(SpinnerAmountTrueFalse.getValue() <= 0) {
                showAlert("Aufgabetyp ausgewählt aber keine Anzahl!");
            }
            answerType.add(AnswerType.geschlosseneAntwort);
            questionType.add(QuestionType.wahrOderFalsch);
        }
        if (rButtonTypGapText.isSelected()) {
            if(SpinnerAmountGapText.getValue() <= 0) {
                showAlert("Aufgabetyp ausgewählt aber keine Anzahl!");
            }
            answerType.add(AnswerType.geschlosseneAntwort);
            questionType.add(QuestionType.leerstellen);
        }
        if (rButtonTypAssign.isSelected()) {
            if(spinnerAmountAssign.getValue() <= 0) {
                showAlert("Aufgabetyp ausgewählt aber keine Anzahl!");
            }
            answerType.add(AnswerType.geschlosseneAntwort);
            questionType.add(QuestionType.zuordnung);
        }
        if (rButtonTypRanking.isSelected()) {
            if(spinnerAmountRanking.getValue() <= 0) {
                showAlert("Aufgabetyp ausgewählt aber keine Anzahl!");
            }
            answerType.add(AnswerType.geschlosseneAntwort);
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
}
