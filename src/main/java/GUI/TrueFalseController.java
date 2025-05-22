package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.domain.AufgabeService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrueFalseController extends SceneController{

        @FXML
        private MenuButton menuBar;

        @FXML
        private TextArea questionTextField;

        @FXML
        private VBox answerContainer;

        @FXML
        private TextField answer1Button;
        @FXML
        private RadioButton rButton11;
        @FXML
        private RadioButton rButton12;
        @FXML
        private TextField answer2Button;
        @FXML
        private RadioButton rButton21;
        @FXML
        private RadioButton rButton22;

        private final List<TextField> answerFields = new ArrayList<>();
        private final List<RadioButton> trueButtons = new ArrayList<>();
        private final List<RadioButton> falseButtons = new ArrayList<>();

        private AufgabeService aufgabe;

        public void setAufgabe(AufgabeService aufgabe) {
            this.aufgabe = aufgabe;
        }

    @FXML
    public void initialize() {
        //Fügt die default Textfelder und Buttons zur jeweiligen Liste hinzu

        answerFields.add(answer1Button);
        answerFields.add(answer2Button);

        trueButtons.add(rButton11);
        trueButtons.add(rButton21);
        falseButtons.add(rButton12);
        falseButtons.add(rButton22);
    }

    @FXML
    public void addAnswerField() {
        // Fügt in der Oberfläche ein neues Aufgabe-Status-Paar ein

        TextField answerField = new TextField();
        answerField.setPromptText("Antwort");
        answerField.setPrefHeight(30.0);
        answerField.setFont(new Font(15.0));
        answerField.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(answerField, Priority.ALWAYS);

        RadioButton trueButton = new RadioButton("Richtig");
        trueButton.setFont(new Font(15.0));
        RadioButton falseButton = new RadioButton("Falsch");
        falseButton.setFont(new Font(15.0));

        ToggleGroup group = new ToggleGroup();
        trueButton.setToggleGroup(group);
        falseButton.setToggleGroup(group);

        answerFields.add(answerField);
        trueButtons.add(trueButton);
        falseButtons.add(falseButton);

        HBox answerRow = new HBox(10, answerField, trueButton, falseButton);
        answerContainer.getChildren().add(answerRow);
    }


    @FXML
        public void removeLastAnswerField() {
            //löscht das letzte Aufgabe-Status-Paar in der Oberfläche einschließlich der gespeicherten Daten

            int childCount = answerContainer.getChildren().size();

            if (childCount > 2) {
                answerContainer.getChildren().remove(childCount - 1);
                answerFields.removeLast();
                trueButtons.removeLast();
                falseButtons.removeLast();            }
        }


        @FXML
        public void saveAndSwitchToStartPage() throws IOException, SQLException {
            //Speichert alle gesammelten Daten und sendet sie an DB --> Wechsel zum Startbildschirm
            //ToDo: Daten an DB senden

            String question = questionTextField.getText().trim();

            if (question.isEmpty()) {
                showAlert("Fehler", "Frage eingeben.");
                return;
            }



            List<String> answers = new ArrayList<>();
            List<Boolean> correctValues = new ArrayList<>();


            for (int i = 0; i < answerFields.size(); i++) {
                String answer = answerFields.get(i).getText().trim();

                if (answer.isEmpty()) {
                    continue;
                }

                boolean isTrueSelected = trueButtons.get(i).isSelected();
                boolean isFalseSelected = falseButtons.get(i).isSelected();

                if (!isTrueSelected && !isFalseSelected) {
                    showAlert("Fehler", "Jede Antwort muss mit 'Richtig' oder 'Falsch' markiert werden.");
                    return;
                }

                answers.add(answer);
                correctValues.add(isTrueSelected); // true = Richtig, false = Falsch
                aufgabe.setAnswerPage(answer, isTrueSelected);
            }


            if (answers.isEmpty()) {
                showAlert("Fehler", "Mindestens eine Aussage angeben werden.");
                return;
            }

            //ToDO: Daten an Datenbank senden
            aufgabe.setTask(question);
            aufgabe.save();

            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }

        private void showAlert(String title, String message) {
            //Zeigt Fehlermeldung mit bestimmten Text an

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }

    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        if(showAlert()){
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }
    }

    @FXML
    public void switchToExamOverview() throws IOException{
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
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.logout(stage);
        }
    }



}



