package GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import org.example.domain.*;

import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskOverviewController extends SceneController implements Initializable {

    @FXML
    private MenuButton menuBar;

    @FXML
    private TableView<Task> tableView;

    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> modulColumn;
    @FXML
    private TableColumn<Task, String> taxonomieColumn;
    @FXML
    private TableColumn<Task, String> typColumn;
    @FXML
    private TableColumn<Task, Integer> pointsColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setzt die Attribute, sodass die Ansicht passend mit skaliert

        loadData();

        nameColumn.setResizable(true);
        nameColumn.setMaxWidth(Double.MAX_VALUE);
        modulColumn.setResizable(true);
        modulColumn.setMaxWidth(Double.MAX_VALUE);
        taxonomieColumn.setResizable(true);
        taxonomieColumn.setMaxWidth(Double.MAX_VALUE);
        typColumn.setResizable(true);
        typColumn.setMaxWidth(Double.MAX_VALUE);
        pointsColumn.setResizable(true);
        pointsColumn.setMaxWidth(Double.MAX_VALUE);

        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));//0.2, da jede der 5 Spalten 1/5 des Platzes bekommt
        modulColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        taxonomieColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        typColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        pointsColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addRightClickMenu();
    }

    private void loadData() {
        //lädt die Aufgaben aus der Datenbank für die Übersicht

        ObservableList<Task> tasks = FXCollections.observableArrayList(Task.tasks);
        tableView.setItems(tasks);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestion().getName()));
        modulColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModul().getName()));
        taxonomieColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestion().getTaxonomie().name()));
        typColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAnswer().getFirst().getTyp().getName()));
        pointsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuestion().getPoints()).asObject());

    }


    private void addRightClickMenu() {
        //fügt ein Menü ein, dass durch einen Rechtsklick verschiedenen Optionen anbietet

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem deleteTaskItem = new MenuItem("Aufgabe löschen");
        MenuItem editTaskItem = new MenuItem("Aufgabe bearbeiten");
        MenuItem duplicateTask = new MenuItem("Aufgabe duplizieren");
        MenuItem printTaskItem = new MenuItem("Aufgabe exportieren");

        deleteTaskItem.setOnAction(event -> {
            ObservableList<Task> selectedTasks = tableView.getSelectionModel().getSelectedItems();

            if (selectedTasks.isEmpty())
                return;

            boolean confirm = showAlert("Möchten Sie die ausgewählte Aufgabe wirklich löschen?");

            if (confirm) {
                for(Task task : selectedTasks) {
                    try {
                        Task.deleteTask(task);
                    } catch (SQLException e) {
                        showAlert("Fehler beim Löschen der Aufgabe.");
                    }
                }
                tableView.getItems().removeAll(selectedTasks);
            }
        });

        editTaskItem.setOnAction(event -> {
            Task selectedTask = tableView.getSelectionModel().getSelectedItem();
            if (selectedTask == null) return;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AddTaskPage.fxml"));
                Parent root = loader.load();

                TaskPageController controller = loader.getController();

                controller.initializeEditMode(selectedTask);

                Stage stage = (Stage) tableView.getScene().getWindow();
                Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                stage.show();

                } catch (IOException ignored) {
                    showErrorAlert("Fehler beim Öffnen der Aufgabe.");
            }
        });

        duplicateTask.setOnAction(event -> {
            Task selectedTask = tableView.getSelectionModel().getSelectedItem();
            if (selectedTask == null) return;

            AufgabeService aufgabeService = new AufgabeService();
            try {
                aufgabeService.duplicateTask(selectedTask);
                loadData(); // Tabelle neu laden
            } catch (SQLException e) {
                showErrorAlert("Fehler beim Duplizieren der Aufgabe.");
            }
        });

        printTaskItem.setOnAction(event -> {
            ObservableList<Task> selectedTasks = tableView.getSelectionModel().getSelectedItems();
            if (selectedTasks.isEmpty())
                return;
            //ToDo: Druckfunktion für eine Aufgabe aufrufen bzw. die Auswahl an Aufgaben
            // Normale Aufgabe
            Document document = new Document(PageSize.A4);
            String outputDir = "target/GeneratedExams/Tasks/";
            new File(outputDir).mkdirs();
            String filePath = outputDir + selectedTasks.getFirst().getQuestion().getName() + ".pdf";
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            document.open();
            // Musterlösung der Aufgabe
            Document musterloesung = new Document(PageSize.A4);
            String outputDirLoesung = outputDir + "Loesungen/";
            new File(outputDirLoesung).mkdirs();
            String filePathLoesung = outputDirLoesung + selectedTasks.getFirst().getQuestion().getName() + "_Loesung.pdf";
            try {
                PdfWriter writerLoesung = PdfWriter.getInstance(musterloesung, new FileOutputStream(filePathLoesung));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            musterloesung.open();

            Font headerFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font questionHeaderFont = new Font(Font.HELVETICA, 14);
            questionHeaderFont.setColor(Color.blue);
            Font questionFont = new Font(Font.HELVETICA, 12);
            Font answerFont = new Font(Font.HELVETICA, 10);
            Font answerFont2 = new Font(Font.HELVETICA, 8, Font.ITALIC);
            Font punktFont = new Font(Font.HELVETICA, 10, Font.ITALIC);

            int aufgabeNummer = 1;
            for(Task task : selectedTasks) {
                Paragraph taskHeadParagraph = new Paragraph();
                taskHeadParagraph.add(new Chunk("Aufgabe " + aufgabeNummer + " (" + task.getQuestion().getPoints() + " Punkte)", questionHeaderFont));
                taskHeadParagraph.setSpacingBefore(10);
                taskHeadParagraph.setSpacingAfter(2);
                document.add(taskHeadParagraph);
                musterloesung.add(taskHeadParagraph);

                Paragraph taskParagraph = new Paragraph();
                taskParagraph.add(new Chunk(task.getQuestion().getQuestionText(), questionFont));
                taskParagraph.setSpacingBefore(15);
                taskParagraph.setSpacingAfter(2);
                document.add(taskParagraph);
                musterloesung.add(taskParagraph);

                QuestionType typ = task.getAnswer().getFirst().getTyp();


                if(typ == QuestionType.multipleChoiceFragen || typ == QuestionType.singleChoiceFragen || typ == QuestionType.wahrOderFalsch) {
                    // Multiple Choice + Single Choice + Wahr/Falsch
                    for(int i = 0; i < task.getAnswer().size(); i++) {
                        String antwortText = task.getAnswer().get(i).getAntwortText();
                        boolean right = task.getAnswer().get(i).isKorrekt();
                        Paragraph antwortParagraph = new Paragraph("[  ]  " + antwortText, answerFont);
                        Paragraph antwortParagraphRight = new Paragraph("[ X ]  " + antwortText, answerFont);
                        antwortParagraph.setIndentationLeft(20f);
                        antwortParagraph.setSpacingAfter(2f);
                        antwortParagraphRight.setIndentationLeft(20f);
                        antwortParagraphRight.setSpacingAfter(2f);
                        document.add(antwortParagraph);
                        if(right) {
                            musterloesung.add(antwortParagraphRight);
                        } else {
                            musterloesung.add(antwortParagraph);
                        }
                    }

                } else if(typ == QuestionType.ranking) {
                    // Ranking + Zuordnung
                    for (int i = 0; i < task.getAnswer().size(); i++) {
                        String antwortText = task.getAnswer().get(i).getAntwortText();
                        int position = task.getAnswer().get(i).getAntwortRanking();
                        Paragraph antwortParagraph = new Paragraph("[   ] " + antwortText, answerFont);
                        Paragraph antwortParagraphRight = new Paragraph("[" + position + "] " + antwortText, answerFont);
                        antwortParagraph.setIndentationLeft(20f);
                        antwortParagraph.setSpacingAfter(2f);
                        antwortParagraphRight.setIndentationLeft(20f);
                        antwortParagraphRight.setSpacingAfter(2f);
                        document.add(antwortParagraph);
                        musterloesung.add(antwortParagraphRight);
                    }
                } else if(typ == QuestionType.zuordnung) {
                    // Zuordnung

                    List<String> leftItems = new ArrayList<>();
                    List<String> rightItems = new ArrayList<>();

                    for (Antwort answer : task.getAnswer()) {
                        leftItems.add(answer.getAntwortText());
                        rightItems.add(answer.getAntwortText2());
                    }

                    // Zufällige Reihenfolge der rechten Seite
                    List<String> shuffledRightItems = new ArrayList<>(rightItems);
                    Collections.shuffle(shuffledRightItems);

                    PdfPTable table = new PdfPTable(2);
                    table.setWidths(new int[]{1, 1});
                    table.setSpacingBefore(10f);
                    table.setSpacingAfter(10f);

                    char letter = 'a';
                    for (int i = 0; i < leftItems.size(); i++) {
                        PdfPCell cellLeft = new PdfPCell(new Phrase((i + 1) + ". " + leftItems.get(i), answerFont));
                        PdfPCell cellRight = new PdfPCell(new Phrase((char)(letter + i) + ". " + shuffledRightItems.get(i), answerFont));
                        cellLeft.setPadding(5f);
                        cellRight.setPadding(5f);

                        table.addCell(cellLeft);
                        table.addCell(cellRight);
                    }

                    document.add(table);

                    // Tabelle für Musterlösung (korrekte Paare)
                    PdfPTable loesungstabelle = new PdfPTable(2);
                    loesungstabelle.setWidths(new int[]{1, 1});
                    loesungstabelle.setSpacingBefore(10f);
                    loesungstabelle.setSpacingAfter(10f);

                    for (int i = 0; i < leftItems.size(); i++) {
                        PdfPCell cellLeft = new PdfPCell(new Phrase((i + 1) + ". " + leftItems.get(i), answerFont));
                        PdfPCell cellRight = new PdfPCell(new Phrase((char)(letter + i) + ". " + rightItems.get(i), answerFont));
                        cellLeft.setPadding(5f);
                        cellRight.setPadding(5f);

                        loesungstabelle.addCell(cellLeft);
                        loesungstabelle.addCell(cellRight);
                    }

                    musterloesung.add(loesungstabelle);
                } else if(typ == QuestionType.leerstellen) {
                    // Keine Ahnung ob das stimmt, hier muss noch dran gearbeitet werden
                    String vollerText = task.getAnswer().getFirst().getAntwortText2();
                    String lueckenText = task.getAnswer().getFirst().getAntwortText();

                    vollerText = vollerText.replaceAll("[.,;!?]", "").toLowerCase();
                    lueckenText = lueckenText.replaceAll("[.,;!?]", "").toLowerCase();

                    String[] vollerWoerter = vollerText.split("\\s+");
                    String[] lueckenWoerter = lueckenText.split("\\s+");

                    List<String> gefundeneLuecken = new ArrayList<>();

                    for (int i = 0; i < Math.min(vollerWoerter.length, lueckenWoerter.length); i++) {
                        if (lueckenWoerter[i].equals("_")) {
                            gefundeneLuecken.add(vollerWoerter[i]);
                        }
                    }

                    Paragraph Lueckentext = new Paragraph(task.getAnswer().getFirst().getAntwortText(),answerFont);

                    Paragraph Worte = new Paragraph(" ", answerFont2);
                    for(String wort : gefundeneLuecken) {
                        Worte.add(wort + " ");
                    }
                    System.out.println("gefundeneLuecken:");
                    for (String wort : gefundeneLuecken) {
                        System.out.println("'" + wort + "'");
                    }

                    document.add(Lueckentext);
                    System.out.println("Worte: " + Worte);
                    document.add(Worte);

                    Paragraph VollerText = new Paragraph(task.getAnswer().getFirst().getAntwortText2(),answerFont);
                    musterloesung.add(VollerText);
                }

                document.add(new Paragraph(" "));
                musterloesung.add(new Paragraph(" "));

                aufgabeNummer++;
            }

            document.close();
            musterloesung.close();
            System.out.println("PDF wurde erstellt");

            File sourcePdfFile = new File(outputDir + selectedTasks.getFirst().getQuestion().getName() + ".pdf");
            File sourcePdfFileSolution = new File(outputDirLoesung + selectedTasks.getFirst().getQuestion().getName() + "_Loesung.pdf");
            String userHome = System.getProperty("user.home");
            Path downloadsPath = Paths.get(userHome, "Downloads");

            Path destinationPath = downloadsPath.resolve(sourcePdfFile.getName());
            Path uniquePath = getUniqueDestinationPath(destinationPath);
            Path destinationPathSolution = downloadsPath.resolve(sourcePdfFileSolution.getName());
            Path uniquePathSolution = getUniqueDestinationPath(destinationPathSolution);


            try {
                Files.copy(sourcePdfFile.toPath(), uniquePath);
                Files.copy(sourcePdfFileSolution.toPath(), uniquePathSolution);
            } catch (IOException ignored) {}

        });


        rightClickMenu.getItems().addAll(editTaskItem, duplicateTask, printTaskItem, deleteTaskItem);

        tableView.setOnMouseClicked(click -> {
            if (click.getButton() == MouseButton.SECONDARY) {
                rightClickMenu.show(tableView, click.getScreenX(), click.getScreenY());
            } else {
                rightClickMenu.hide();
            }
        });
    }


    private Path getUniqueDestinationPath(Path destinationPath) {
        //erstellt einen einzigartigen Namen für das PDF

        Path parentDir = destinationPath.getParent();
        String fileName = destinationPath.getFileName().toString();

        String baseName;
        String extension;

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        } else {
            baseName = fileName;
            extension = "";
        }

        int counter = 1;
        Path newPath = destinationPath;

        while (Files.exists(newPath)) {
            String newFileName = String.format("%s(%d)%s", baseName, counter++, extension);
            newPath = parentDir.resolve(newFileName);
        }

        return newPath;
    }


    @Override
    protected boolean showAlert(String message) {
        //Zeigt Fehlermeldung mit beliebigem Text an

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Achtung!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        AtomicBoolean result = new AtomicBoolean(false);

        //Überprüft, welche Schaltfläche der Benutzer gedrückt hat
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result.set(true);
            }
        });
        return result.get();
    }


    private void showErrorAlert(String message){
        //zeigt Alert ohne Auswahlmöglichkeiten

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setContentText(message);
        alert.show();
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
    public void switchToExamOverview() throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamCollection(stage);
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamPage(stage);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.logout(stage);
    }

    @FXML
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToAddTaskPage(stage);
    }
}
