package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    public void initialize() {
        //Fügt die default Textfelder zur jeweiligen Liste hinzu
        statementAreas.add(statement1TextArea);
        statementAreas.add(statement2TextArea);

        numbers.add(number1TextArea);
        numbers.add(number2TextArea);

    }


    @FXML
    public void addStatementField() {
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
    public void saveAndSwitchToStartPage()  throws IOException {
        //Speichert alle gesammelten Daten und sendet sie an DB --> Wechsel zum Startbildschirm
        //ToDo: Daten an DB senden

        String question = questionTextArea.getText().trim();

        if (question.isEmpty()) {
            showAlert("Fehler", "Frage eingeben.");
            return;
        }

        List<String> statements = new ArrayList<>();

        for (int i = 0; i < statementAreas.size(); i++) {
            String statement = statementAreas.get(i).getText().trim();

            if (!statement.isEmpty()) {
                statements.add(statement);
            }
            else{
                showAlert("Fehler", "Bitte alle Felder ausfüllen ");
                return;
            }
        }

        System.out.println("Frage: "+question);
        System.out.println("Reihenfolge:");

        for (int i = 0; i < statements.size(); i++) {
            String answer = statements.get(i);
            System.out.println((i+1) +"."+answer);
        }

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
