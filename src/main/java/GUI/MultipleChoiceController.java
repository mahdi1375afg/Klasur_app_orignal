package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.domain.Antwort;
import org.example.domain.AufgabeService;
import org.example.domain.Task;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceController extends SceneController{

    @FXML
    protected MenuButton menuBar;

    @FXML
    protected TextArea questionTextArea;


    @FXML
    protected VBox answerContainer;

    protected final List<TextArea> answerAreas = new ArrayList<>();
    protected final List<CheckBox> checkBoxes = new ArrayList<>();

    @FXML
    protected TextArea answer1TextArea;
    @FXML
    protected CheckBox checkBox1;
    @FXML
    protected TextArea answer2TextArea;
    @FXML
    protected CheckBox checkBox2;

    protected AufgabeService aufgabe;
    protected Task selectedTask;
    protected boolean editMode = false;

    public void setAufgabe(AufgabeService aufgabe) {
        this.aufgabe = aufgabe;
    }

    @FXML
    public void initialize() {
        answerAreas.add(answer1TextArea);
        checkBoxes.add(checkBox1);

        answerAreas.add(answer2TextArea);
        checkBoxes.add(checkBox2);

        HBox.setHgrow(answer1TextArea, Priority.ALWAYS);
        HBox.setHgrow(answer2TextArea, Priority.ALWAYS);
    }

    public void initializeEditMode(Task selectedTask){
        this.editMode = true;
        this.selectedTask = selectedTask;

        questionTextArea.setText(selectedTask.getQuestion().getQuestionText());

        List<Antwort> answers = selectedTask.getAnswer();

        for (int i = 2; i < answers.size(); i++) {
            addAnswerField();
        }

        for (int i = 0; i < answers.size(); i++) {
            answerAreas.get(i).setText(answers.get(i).getAntwortText());
            checkBoxes.get(i).setSelected(answers.get(i).isKorrekt());
        }
    }


    @FXML
    public void addAnswerField() {

        TextArea answerArea = new TextArea();
        answerArea.setPromptText("Antwort "+ (answerAreas.size() + 1));
        answerArea.setPrefWidth(370.0);
        answerArea.setPrefHeight(60.0);
        answerArea.setFont(new Font(15.0));

        answerArea.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(answerArea, Priority.ALWAYS);

        CheckBox correctCheckBox = new CheckBox("Richtig");
        correctCheckBox.setFont(new Font(15.0));

        answerAreas.add(answerArea);
        checkBoxes.add(correctCheckBox);

        HBox answerRow = new HBox(10, answerArea, correctCheckBox);

        answerContainer.getChildren().add(answerRow);
        answerRow.setAlignment(Pos.CENTER_LEFT);
    }

    @FXML
    public void removeLastAnswerField() {
        int childCount = answerContainer.getChildren().size();

        if (childCount > 2) {
            answerContainer.getChildren().remove(childCount - 1);
            answerAreas.removeLast();
            checkBoxes.removeLast();
        }
    }


    @FXML
    public void saveAndSwitchToStartPage(ActionEvent event) throws IOException, SQLException {

        String question = questionTextArea.getText().trim();

        if (question.isEmpty()) {
            showAlert("Frage eingeben.");
            return;
        }

        List<String> answers = new ArrayList<>();
        List<Integer> correctIndex = new ArrayList<>();
        correctIndex.add(-1);


        for (int i = 0; i < answerAreas.size(); i++) {
            String answer = answerAreas.get(i).getText().trim();


            if (!answer.isEmpty()) {
                answers.add(answer);
            }

            if (checkBoxes.get(i).isSelected()) {
                if(correctIndex.contains(-1)) {
                    correctIndex.remove(correctIndex.indexOf(-1));
                }
                correctIndex.add(i);
            }
        }

        if (answers.size() < 2) {
            showAlert( "Mindestens zwei Antworten angeben.");
            return;
        }

        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i);
            boolean isCorrect = correctIndex.contains(i);
            aufgabe.setAnswerPage(answer, isCorrect);
        }

        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.saveTask(editMode, selectedTask, aufgabe, question, stage);
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

