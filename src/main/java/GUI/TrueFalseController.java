package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.domain.Antwort;
import org.example.domain.AufgabeService;
import org.example.domain.Task;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrueFalseController extends SceneController{

        @FXML
        private MenuButton menuBar;

        @FXML
        private TextArea questionTextArea;

        @FXML
        private VBox answerContainer;

        @FXML
        private TextArea answer1Button;
        @FXML
        private RadioButton rButton11;
        @FXML
        private RadioButton rButton12;
        @FXML
        private TextArea answer2Button;
        @FXML
        private RadioButton rButton21;
        @FXML
        private RadioButton rButton22;

        private final List<TextArea> answerAreas = new ArrayList<>();
        private final List<RadioButton> trueButtons = new ArrayList<>();
        private final List<RadioButton> falseButtons = new ArrayList<>();
        private AufgabeService aufgabe;
        private boolean editMode = false;
        private Task selectedTask;

        public void setAufgabe(AufgabeService aufgabe) {
            //Grundlegende Informationen zur Aufgabe setzen

            this.aufgabe = aufgabe;
        }

    @FXML
    public void initialize() {
        //Fügt die default Textfelder und Buttons zur jeweiligen Liste hinzu

        answerAreas.add(answer1Button);
        answerAreas.add(answer2Button);

        trueButtons.add(rButton11);
        trueButtons.add(rButton21);
        falseButtons.add(rButton12);
        falseButtons.add(rButton22);

        HBox.setHgrow(answer1Button, Priority.ALWAYS);
        HBox.setHgrow(answer2Button, Priority.ALWAYS);

    }

    public void initializeEditMode(Task selectedTask) {
        //Lädt alle Informationen zur Aufgabe aus der Datenbank beim Bearbeiten der Aufgabe

        editMode = true;
            this.selectedTask = selectedTask;

            questionTextArea.setText(selectedTask.getQuestion().getQuestionText());

            List<Antwort> answers = selectedTask.getAnswer();

            for(int i = 2; i < answers.size(); i++) {
                addAnswerArea();
            }
            for(int i=0; i<answers.size(); i++) {
                answerAreas.get(i).setText(answers.get(i).getAntwortText());
                if(answers.get(i).isKorrekt()) {
                    trueButtons.get(i).setSelected(true);
                }
                else {
                    falseButtons.get(i).setSelected(true);
                }
            }
    }

    @FXML
    public void addAnswerArea() {
        // Fügt in der Oberfläche ein neues Aufgabe-Status-Paar ein

        TextArea answerArea = new TextArea();
        answerArea.setPromptText("Antwort "+ (answerAreas.size() + 1));
        answerArea.setPrefHeight(60.0);
        answerArea.setFont(new Font(15.0));
        answerArea.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(answerArea, Priority.ALWAYS);

        RadioButton trueButton = new RadioButton("Richtig");
        trueButton.setFont(new Font(15.0));
        RadioButton falseButton = new RadioButton("Falsch");
        falseButton.setFont(new Font(15.0));

        ToggleGroup group = new ToggleGroup();
        trueButton.setToggleGroup(group);
        falseButton.setToggleGroup(group);

        answerAreas.add(answerArea);
        trueButtons.add(trueButton);
        falseButtons.add(falseButton);

        HBox answerRow = new HBox(10, answerArea, trueButton, falseButton);
        answerRow.setAlignment(Pos.CENTER_LEFT);
        answerContainer.getChildren().add(answerRow);
    }


    @FXML
        public void removeLastAnswerArea() {
            //löscht das letzte Aufgabe-Status-Paar in der Oberfläche einschließlich der gespeicherten Daten

            int childCount = answerContainer.getChildren().size();

            if (childCount > 2) {
                answerContainer.getChildren().remove(childCount - 1);
                answerAreas.removeLast();
                trueButtons.removeLast();
                falseButtons.removeLast();            }
        }


        @FXML
        public void saveAndSwitchToStartPage() throws IOException, SQLException {
            //Speichert alle gesammelten Daten und sendet sie an DB
            //anschließend erfolgt der Wechsel zum Startbildschirm bzw. zur Aufgabenübersicht
            //im editMode

            String question = questionTextArea.getText().trim();

            if (question.isEmpty()) {
                showAlert( "Frage eingeben.");
                return;
            }

            List<String> answers = new ArrayList<>();

            for (int i = 0; i < answerAreas.size(); i++) {
                String answer = answerAreas.get(i).getText().trim();

                if (answer.isEmpty()) {
                    continue;
                }

                boolean isTrueSelected = trueButtons.get(i).isSelected();
                boolean isFalseSelected = falseButtons.get(i).isSelected();

                if (!isTrueSelected && !isFalseSelected) {
                    showAlert("Jede Antwort muss mit 'Richtig' oder 'Falsch' markiert werden.");
                    return;
                }

                answers.add(answer);
                aufgabe.setAnswerPage(answer, isTrueSelected);
            }

            if (answers.isEmpty()) {
                showAlert("Mindestens eine Aussage angeben werden.");
                return;
            }

            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.saveTask(editMode, selectedTask, aufgabe, question, stage);
        }

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
            //Wechsel mit Warnung zur Startseite

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }

    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        //Wechsel mit Warnung zur Aufgabenübersicht

        if(showAlert()){
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }
    }

    @FXML
    public void switchToExamOverview() throws IOException{
            //Wechsel mit Warnung zur Klausurübersicht

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamCollection(stage);
        }
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
            //
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamPage(stage);
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Seite zum Klausur erstellen

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.logout(stage);
        }
    }



}



