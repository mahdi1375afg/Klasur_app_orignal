package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
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

public class RankingController extends SceneController {

    @FXML
    private MenuButton menuBar;

    @FXML
    private VBox answerContainer;

    @FXML
    private TextArea number1TextArea;
    @FXML
    private TextArea statement1TextArea;
    @FXML
    private TextArea number2TextArea;
    @FXML
    private TextArea statement2TextArea;
    @FXML
    private TextArea questionTextArea;

    private final List<TextArea> statementAreas = new ArrayList<>();
    private final List<TextArea> numbers = new ArrayList<>();

    private AufgabeService aufgabe;
    Task selectedTask;
    boolean editMode;

    public void setAufgabe(AufgabeService aufgabe) {
        this.aufgabe = aufgabe;
    }

    @FXML
    public void initialize() {
        //Fügt die default Textfelder zur jeweiligen Liste hinzu
        statementAreas.add(statement1TextArea);
        statementAreas.add(statement2TextArea);

        numbers.add(number1TextArea);
        numbers.add(number2TextArea);

    }

    public void initializeEditMode(Task selectedTask) {
        this.editMode = true;
        this.selectedTask = selectedTask;

        questionTextArea.setText(selectedTask.getQuestion().getQuestionText());
        List<Antwort> answers = selectedTask.getAnswer();

        for(int i=2; i<answers.size(); i++){
            addStatementArea();
        }

        for(int i=0; i<answers.size(); i++){
            statementAreas.get(i).setText(answers.get(i).getAntwortText());
        }
    }


    @FXML
    public void addStatementArea() {
        //Fügt in der Oberfläche ein neues Antwort-Lösung-Paar ein

        TextArea numberArea = new TextArea();
        String numberStatement = Integer.toString(statementAreas.size()+1);
        numberArea.setText(numberStatement);
        numberArea.setPrefWidth(60.0);
        numberArea.setPrefHeight(34.0);
        numberArea.setMaxWidth(60.0);
        numberArea.setFont(new Font(15.0));
        numberArea.setEditable(false);

        TextArea statementArea = new TextArea();
        statementArea.setPromptText("Aussage "+ (numbers.size()+1));
        statementArea.setPrefWidth(515.0);
        statementArea.setPrefHeight(34.0);
        statementArea.setFont(new Font(15.0));

        HBox.setHgrow(numberArea, Priority.NEVER);
        HBox.setHgrow(statementArea, Priority.ALWAYS);

        statementAreas.add(statementArea);
        numbers.add(numberArea);

        HBox answerRow = new HBox(10, numberArea, statementArea);

        answerContainer.getChildren().add(answerRow);
    }

    @FXML
    public void removeLastAnswerField() {
        //löscht das letzte Antwort-Lösung-Paar in der Oberfläche einschließlich der gespeicherten Daten

        int childCount = answerContainer.getChildren().size();

        if (childCount > 2) {
            answerContainer.getChildren().remove(childCount - 1);
            statementAreas.removeLast();
            numbers.removeLast();
        }
    }

    @FXML
    public void saveAndSwitchToStartPage() throws IOException, SQLException {
        //Speichert alle gesammelten Daten und sendet sie an DB → Wechsel zum Startbildschirm

        String question = questionTextArea.getText().trim();

        if (question.isEmpty()) {
            showAlert("Frage eingeben.");
            return;
        }

        List<String> statements = new ArrayList<>();

        for (TextArea statementArea : statementAreas) {
            String statement = statementArea.getText().trim();

            if (!statement.isEmpty()) {
                statements.add(statement);
            } else {
                showAlert("Bitte alle Felder ausfüllen ");
                return;
            }
        }

        for (int i = 0; i < statements.size(); i++) {
            String answer = statements.get(i);
            aufgabe.setAnswerPageRanking(answer, i+1);
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
