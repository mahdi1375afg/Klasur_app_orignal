package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class AssignController extends SceneController{

    @FXML
    private MenuButton menuBar;

    @FXML
    private VBox answerContainer;

    @FXML
    private TextArea textAreaQuestion;
    @FXML
    private TextArea answer1TextArea;
    @FXML
    private TextArea answer2TextArea;
    @FXML
    private TextArea solution1TextArea;
    @FXML
    private TextArea solution2TextArea;

    private final List<TextArea> answerAreas = new ArrayList<>();
    private final List<TextArea> solutionAreas = new ArrayList<>();

    private AufgabeService aufgabe;
    private boolean editMode = false;
    private Task selectedTask;

    public void setAufgabe(AufgabeService aufgabe) {
        this.aufgabe = aufgabe;
    }

    @FXML
    public void initialize() {
        //Fügt die default Textfelder zur jeweiligen Liste hinzu
    answerAreas.add(answer1TextArea);
    answerAreas.add(answer2TextArea);

    solutionAreas.add(solution1TextArea);
    solutionAreas.add(solution2TextArea);

        HBox.setHgrow(answer1TextArea, Priority.ALWAYS);
        HBox.setHgrow(answer2TextArea, Priority.ALWAYS);

        HBox.setHgrow(solution1TextArea, Priority.ALWAYS);
        HBox.setHgrow(solution2TextArea, Priority.ALWAYS);
    }

    public void setEditMode(Task selectedTask){
        editMode = true;
        this.selectedTask = selectedTask;

        textAreaQuestion.setText(selectedTask.getQuestion().getQuestionText());

        List<Antwort> answers = selectedTask.getAnswer();

        for(int i=2; i<answers.size(); i++){
            addAnswerAreas();
        }

        for(int i=0; i<solutionAreas.size(); i++){
            answerAreas.get(i).setText(answers.get(i).getAntwortText());
            solutionAreas.get(i).setText(answers.get(i).getAntwortText2());
        }
    }

    @FXML
    public void addAnswerAreas() {
        //Fügt in der Oberfläche ein neues Antwort-Lösung-Paar ein

        TextArea answerArea = new TextArea();
        answerArea.setPromptText("Antwort " + (answerAreas.size() + 1));
        answerArea.setPrefWidth(370.0);
        answerArea.setPrefHeight(60.0);
        answerArea.setFont(new Font(15.0));


        answerArea.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(answerArea, Priority.ALWAYS);

        TextArea solutionArea = new TextArea();
        solutionArea.setPromptText("Lösung " + (solutionAreas.size() + 1));
        solutionArea.setPrefWidth(370.0);
        solutionArea.setPrefHeight(60.0);
        solutionArea.setFont(new Font(15.0));

        answerArea.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(solutionArea, Priority.ALWAYS);

        answerAreas.add(answerArea);
        solutionAreas.add(solutionArea);


        HBox answerRow = new HBox(10, answerArea, solutionArea);

        answerContainer.getChildren().add(answerRow);
    }

    @FXML
    public void removeLastAnswerField() {
        //löscht das letzte Antwort-Lösung-Paar in der Oberfläche einschließlich der gespeicherten Daten

        int childCount = answerContainer.getChildren().size();

        if (childCount > 2) {
            answerContainer.getChildren().remove(childCount - 1);
            answerAreas.removeLast();
            solutionAreas.removeLast();
        }
    }


    @FXML
    public void saveAndSwitchToStartPage() throws IOException, SQLException {
        //Speichert alle gesammelten Daten und sendet sie an DB → Wechsel zum Startbildschirm

        String question = textAreaQuestion.getText().trim();

        if (question.isEmpty()) {
            showAlert("Frage eingeben.");
            return;
        }

        for (int i = 0; i < answerAreas.size(); i++) {
            String answer = answerAreas.get(i).getText().trim();
            String solution = solutionAreas.get(i).getText().trim();


            if (!answer.isEmpty() && !solution.isEmpty()) {
                aufgabe.setAnswerPageMultipleParts(answer, solution);
            }
            else{
                showAlert("Bitte alle Felder ausfüllen ");
                return;
            }
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

