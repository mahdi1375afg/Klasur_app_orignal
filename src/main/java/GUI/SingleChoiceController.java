package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleChoiceController extends MultipleChoiceController {

    @Override
    @FXML
    public void saveAndSwitchToStartPage(ActionEvent event) throws IOException, SQLException {
        //Speichert alle gesammelten Daten und sendet sie an DB
        //anschließend erfolgt der Wechsel zum Startbildschirm bzw. zur Aufgabenübersicht
        //im editMode

        String question = questionTextArea.getText().trim();

        if (question.isEmpty()) {
            showAlert("Frage eingeben.");
            return;
        }

        List<String> answers = new ArrayList<>();
        int correctIndex = -1;

        for (int i = 0; i < answerAreas.size(); i++) {
            String answer = answerAreas.get(i).getText().trim();

            if (!answer.isEmpty()) {
                answers.add(answer);
            }

            if (checkBoxes.get(i).isSelected()) {
                if (correctIndex != -1) {
                    showAlert("Nur eine Antwort darf als richtig markiert sein.");
                    return;
                }
                correctIndex = i;
            }
        }

        if (answers.size() < 2) {
            showAlert("Mindestens zwei Antworten angeben.");
            return;
        }

        if (correctIndex == -1) {
            showAlert("Eine richtige Antwort auswählen.");
            return;
        }

        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i);
            boolean isCorrect = (i == correctIndex);
            aufgabe.setAnswerPage(answer, isCorrect);
        }

        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.saveTask(editMode, selectedTask, aufgabe, question, stage);
    }
}
