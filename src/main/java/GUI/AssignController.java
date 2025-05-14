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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignController extends SceneController{

    @FXML
    private MenuButton menuBar;

    @FXML
    private VBox answerContainer;

    @FXML
    private TextField textFieldQuestion;
    @FXML
    private TextField  answer1TextField;
    @FXML
    private TextField  answer2TextField;
    @FXML
    private TextField solution1TextField;
    @FXML
    private TextField solution2TextField;

    private final List<TextField> answerFields = new ArrayList<>();
    private final List<TextField> solutionFields = new ArrayList<>();

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
    }


    @FXML
    public void addAnswerField() {
        //Fügt in der Oberfläche ein neues Antwort-Lösung-Paar ein

        TextField answerField = new TextField();
        answerField.setPromptText("Antwort");
        answerField.setPrefWidth(370.0);
        answerField.setPrefHeight(60.0);
        answerField.setFont(new Font(15.0));

        TextField solutionField = new TextField();
        answerField.setPromptText("Lösung");
        answerField.setPrefWidth(370.0);
        answerField.setPrefHeight(60.0);
        answerField.setFont(new Font(15.0));


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

