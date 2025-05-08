package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.domain.AufgabeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrueFalseController extends SceneController{

        @FXML
        private MenuButton menuBar;

        @FXML
        private TextField questionTextField;

        @FXML
        private VBox answerContainer;

        private final List<TextField> answerFields = new ArrayList<>();
        private final List<RadioButton> trueButtons = new ArrayList<>();
        private final List<RadioButton> falseButtons = new ArrayList<>();

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

        private AufgabeService aufgabe;

        public void setAufgabe(AufgabeService aufgabe) {
            this.aufgabe = aufgabe;
             System.out.println("Aufgabe gesetzt in nächsten Controller!");
        }

    @FXML
    public void initialize() {
        answerFields.add(answer1Button);
        answerFields.add(answer2Button);

        trueButtons.add(rButton11);
        trueButtons.add(rButton21);
        falseButtons.add(rButton12);
        falseButtons.add(rButton22);
    }

        @FXML
        public void addAnswerField() {

            TextField answerField = new TextField();
            answerField.setPromptText("Antwort");
            answerField.setPrefWidth(370.0);
            answerField.setPrefHeight(30.0);
            answerField.setFont(new Font(15.0));

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
            // Fügt HBox  VBox answerContainer hinzu
            answerContainer.getChildren().add(answerRow);
        }

        @FXML
        public void removeLastAnswerField() {
            int childCount = answerContainer.getChildren().size();

            if (childCount > 2) {
                answerContainer.getChildren().remove(childCount - 1);
                answerFields.remove(answerFields.size() - 1);
                trueButtons.remove(trueButtons.size() - 1);
                falseButtons.remove(falseButtons.size() - 1);            }
        }


        @FXML
        public void saveandswitchToStartPage(ActionEvent event)  throws IOException {

            String question = questionTextField.getText().trim();

            if (question.isEmpty()) {
                showAlert("Fehler", "Frage eingeben.");
                return;
            }

            aufgabe.setTask(question);

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


            if (answers.size() < 1) {
                showAlert("Fehler", "Mindestens eine Aussage angeben werden.");
                return;
            }


            System.out.println(question);
            for (int i = 0; i < answers.size(); i++) {
                String answer = answers.get(i);
                boolean isTrue = correctValues.get(i);

                if (isTrue) {
                    System.out.println((i) +  answer + " (richtig)");
                } else {
                    System.out.println((i)  + answer + " (falsch)");
                }
            }

            //ToDO: Daten an Datenbank senden
            aufgabe.save();

            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }

        private void showAlert(String title, String message) {
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
        public void switchToTaskOverview(ActionEvent event) throws IOException {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }

        @FXML
        public void switchToExamPage(ActionEvent event) throws IOException {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamPage(stage);
        }

        @FXML
        public void switchToExamOverview(ActionEvent event) throws IOException {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamCollection(stage);
        }

        @FXML
        public void logout(ActionEvent event) throws IOException {
            super.logout(event);
        }

    }



