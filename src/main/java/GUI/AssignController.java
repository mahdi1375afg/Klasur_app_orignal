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
    private TextArea textFieldQuestion;
    @FXML
    private TextArea  answer1TextField;
    @FXML
    private TextArea  answer2TextField;
    @FXML
    private TextArea solution1TextField;
    @FXML
    private TextArea solution2TextField;

    private final List<TextArea> answerFields = new ArrayList<>();
    private final List<TextArea> solutionFields = new ArrayList<>();

    private AufgabeService aufgabe;

    public void setAufgabe(AufgabeService aufgabe) {
        this.aufgabe = aufgabe;
    }

    @FXML
    public void initialize() {
        //Fügt die default Textfelder zur jeweiligen Liste hinzu
    answerFields.add(answer1TextField);
    answerFields.add(answer2TextField);

    solutionFields.add(solution1TextField);
    solutionFields.add(solution2TextField);

        HBox.setHgrow(answer1TextField, Priority.ALWAYS);
        HBox.setHgrow(answer2TextField, Priority.ALWAYS);

        HBox.setHgrow(solution1TextField, Priority.ALWAYS);
        HBox.setHgrow(solution2TextField, Priority.ALWAYS);
    }


    @FXML
    public void addAnswerField() {
        //Fügt in der Oberfläche ein neues Antwort-Lösung-Paar ein

        TextArea answerField = new TextArea();
        answerField.setPromptText("Antwort " + (answerFields.size() + 1));
        answerField.setPrefWidth(370.0);
        answerField.setPrefHeight(60.0);
        answerField.setFont(new Font(15.0));


        answerField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(answerField, Priority.ALWAYS);

        TextArea solutionField = new TextArea();
        solutionField.setPromptText("Lösung " + (solutionFields.size() + 1));
        solutionField.setPrefWidth(370.0);
        solutionField.setPrefHeight(60.0);
        solutionField.setFont(new Font(15.0));

        answerField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(solutionField, Priority.ALWAYS);

        answerFields.add(answerField);
        solutionFields.add(solutionField);


        HBox answerRow = new HBox(10, answerField, solutionField);

        answerContainer.getChildren().add(answerRow);
    }

    @FXML
    public void removeLastAnswerField() {
        //löscht das letzte Antwort-Lösung-Paar in der Oberfläche einschließlich der gespeicherten Daten

        int childCount = answerContainer.getChildren().size();

        if (childCount > 2) {
            answerContainer.getChildren().remove(childCount - 1);
            answerFields.remove(answerFields.size() - 1);
            solutionFields.remove(solutionFields.size() - 1);
        }
    }


    @FXML
    public void saveAndSwitchToStartPage() throws IOException, SQLException {
        //Speichert alle gesammelten Daten und sendet sie an DB --> Wechsel zum Startbildschirm
        //ToDo: Daten an DB senden

        String question = textFieldQuestion.getText().trim();

        if (question.isEmpty()) {
            showAlert("Fehler", "Frage eingeben.");
            return;
        }

        List<String> answers = new ArrayList<>();
        List<String> solutions = new ArrayList<>();


        for (int i = 0; i < answerFields.size(); i++) {
            String answer = answerFields.get(i).getText().trim();
            String solution = solutionFields.get(i).getText().trim();


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

