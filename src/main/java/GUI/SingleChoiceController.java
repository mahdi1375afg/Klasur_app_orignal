package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class SingleChoiceController extends SceneController{

    @FXML
    private MenuButton menuBar;

    @FXML
    private TextField questionTextField;

    @FXML
    private VBox answerContainer;

    private final List<TextField> answerFields = new ArrayList<>();
    private final List<CheckBox> checkBoxes = new ArrayList<>();

    @FXML
    private TextField answer1Button;
    @FXML
    private CheckBox checkBox1;
    @FXML
    private TextField answer2Button;
    @FXML
    private CheckBox checkBox2;

    @FXML
    public void initialize() {
        answerFields.add(answer1Button);
        checkBoxes.add(checkBox1);

        answerFields.add(answer2Button);
        checkBoxes.add(checkBox2);
    }


    @FXML
    public void addAnswerField() {

        TextField answerField = new TextField();
        answerField.setPromptText("Antwort");
        answerField.setPrefWidth(401.0);
        answerField.setPrefHeight(31.0);
        answerField.setFont(new Font(15.0));

        CheckBox correctCheckBox = new CheckBox("Richtig");
        correctCheckBox.setFont(new Font(15.0));


        answerFields.add(answerField);
        checkBoxes.add(correctCheckBox);


        HBox answerRow = new HBox(10, answerField, correctCheckBox);

        // Fügt HBox  VBox answerContainer hinzu
        answerContainer.getChildren().add(answerRow);
    }

    @FXML
    public void removeLastAnswerField() {
        int childCount = answerContainer.getChildren().size();

        if (childCount > 2) {
            answerContainer.getChildren().remove(childCount - 1);
            answerFields.remove(answerFields.size() - 1);
            checkBoxes.remove(checkBoxes.size() - 1);
        }
    }


    @FXML
    public void saveandswitchToStartPage(ActionEvent event)  throws IOException {

        String question = questionTextField.getText().trim();

        if (question.isEmpty()) {
            showAlert("Fehler", "Frage eingeben.");
            return;
        }

        List<String> answers = new ArrayList<>();
        int correctIndex = -1;


        for (int i = 0; i < answerFields.size(); i++) {
            String answer = answerFields.get(i).getText().trim();


            if (!answer.isEmpty()) {
                answers.add(answer);

                if (checkBoxes.get(i).isSelected()) {

                    if (correctIndex != -1) {
                        showAlert("Fehler", "Nur eine Antwort darf als richtig markiert sein.");
                        return;
                    }
                    correctIndex = i;
                }
            }
        }


        if (answers.size() < 2) {
            showAlert("Fehler", "Mindestens zwei Antworten angeben.");
            return;
        }


        if (correctIndex == -1) {
            showAlert("Fehler", "Eine richtige Antwort auswählen.");
            return;
        }


        System.out.println(question);
        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i);
            boolean isCorrect = (i == correctIndex);

            if (isCorrect) {
                System.out.println((i) +  answer + " (richtig)");
            } else {
                System.out.println((i)  + answer);
            }
        }


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

