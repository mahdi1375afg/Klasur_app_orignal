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


    @FXML
    public void addAnswerField() {
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
        //Speichert alle gesammelten Daten und sendet sie an DB --> Wechsel zum Startbildschirm
        //ToDo: Daten an DB senden

        String question = textAreaQuestion.getText().trim();

        if (question.isEmpty()) {
            showAlert("Fehler", "Frage eingeben.");
            return;
        }

        List<String> answers = new ArrayList<>();
        List<String> solutions = new ArrayList<>();


        for (int i = 0; i < answerAreas.size(); i++) {
            String answer = answerAreas.get(i).getText().trim();
            String solution = solutionAreas.get(i).getText().trim();


            if (!answer.isEmpty() && !solution.isEmpty()) {
                aufgabe.setAnswerPageMultipleParts(answer, solution);
                answers.add(answer);
                solutions.add(solution);
            }
            else{
                showAlert("Fehler", "Bitte alle Felder ausfüllen ");
                return;
            }
        }

        System.out.println(question);
        System.out.println("Paare:");

        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i);
            String solution = solutions.get(i);

            System.out.println(answer + " " + solution);
        }

        aufgabe.setTask(question);
        aufgabe.save();

        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToStartPage(stage);
    }

    private void showAlert(String title, String message) {
        //Gibt Fehlermeldung mit bestimmten Text aus

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

