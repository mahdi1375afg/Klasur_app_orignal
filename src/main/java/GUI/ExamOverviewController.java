package GUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class ExamOverviewController extends SceneController implements Initializable {

    @FXML
    private MenuButton menuBar;


    @FXML
    private TableColumn<ObservableList<String>, String> nameColumn;
    @FXML
    private TableColumn<ObservableList<String>, String> modulColumn;
    @FXML
    private TableColumn<ObservableList<String>, String> pointsColumn;
    @FXML
    private javafx.scene.control.TableView<ObservableList<String>> tableView;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        nameColumn.setResizable(true);
        nameColumn.setMaxWidth(Double.MAX_VALUE);
        modulColumn.setResizable(true);
        modulColumn.setMaxWidth(Double.MAX_VALUE);
        pointsColumn.setResizable(true);
        pointsColumn.setMaxWidth(Double.MAX_VALUE);

        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.333));//0.3, da jede der 3 Spalten 1/3 des Platzes bekommt
        modulColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.333));
        pointsColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.333));

        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFirst()));
        modulColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get(1)));
        pointsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get(2)));

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addRightClickMenu();

        loadExams();
    }

    private void addRightClickMenu() {
        //fügt ein Menü ein, dass durch einen Rechtsklick verschiedenen Optionen anbietet

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Klausur löschen");
        MenuItem downloadItem = new MenuItem("Klausur exportieren");
        MenuItem openItem = new MenuItem("Klausur anzeigen");

        openItem.setOnAction(event -> {
            for (ObservableList<String> exam : tableView.getSelectionModel().getSelectedItems()) {
                try {
                    Desktop.getDesktop().open(new File("target/GeneratedExams/" + exam.getFirst()));
                } catch (IOException ignored) {}
            }
        });


        deleteItem.setOnAction(event -> {
            ObservableList<ObservableList<String>> selectedExams = tableView.getSelectionModel().getSelectedItems();

            if (selectedExams.isEmpty())
                return;

            boolean confirm = showAlert("Möchten Sie die Auswahl wirklich löschen?");

            if (confirm) {
                for(ObservableList<String> exam: selectedExams){
                    String pdfName = exam.getFirst();
                    File pdfFile = new File("target/GeneratedExams/" + pdfName);
                    if (pdfFile.exists()) {
                        pdfFile.delete();
                    }

                }
                loadExams();
            }

        });

        downloadItem.setOnAction(event -> {
            ObservableList<ObservableList<String>> selectedExams = tableView.getSelectionModel().getSelectedItems();

            if (selectedExams.isEmpty())
                return;

            for(ObservableList<String> exam: selectedExams) {
                String pdfName = exam.getFirst();
                File sourcePdfFile = new File("target/GeneratedExams/" + pdfName);
                String userHome = System.getProperty("user.home");
                Path downloadsPath = Paths.get(userHome, "Downloads");

                Path destinationPath = downloadsPath.resolve(sourcePdfFile.getName());
                Path uniquePath = getUniqueDestinationPath(destinationPath);

                try {
                    Files.copy(sourcePdfFile.toPath(), uniquePath);
                } catch (IOException ignored) {
                }
            }
        });

        tableView.setContextMenu(rightClickMenu);
        rightClickMenu.getItems().addAll(openItem, downloadItem, deleteItem);


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


    private void loadExams() {
        //lädt alle bereits generierten Klausuren aus dem entsprechenden Ordner

        javafx.collections.ObservableList<ObservableList<String>> data = javafx.collections.FXCollections.observableArrayList();

        File dir = new File("target/GeneratedExams/");
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".pdf"));
        if (files == null) return;

        for (File pdf : files) {
            try (PDDocument document = PDDocument.load(pdf)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);

                String modul = extractModul(text);
                String punkte = extractPoints(text);
                String name = pdf.getName();

                javafx.collections.ObservableList<String> row = javafx.collections.FXCollections.observableArrayList();
                row.add(name);
                row.add(modul);
                row.add(punkte);

                data.add(row);
            } catch (Exception ignored) {}
        }

        tableView.setItems(data);
    }



    private String extractModul(String text) {
        //extrahiert den Namen des Moduls aus dem PDF

        String[] lines = text.split("\n");
        boolean foundMarker = false;

        for (String line : lines) {
            if (foundMarker && !line.trim().isEmpty()) {
                return line.trim();
            }

            if (line.contains("Klausur zur Vorlesung")) {
                foundMarker = true;
            }
        }

        return "Unbekannt";
    }


    private String extractPoints(String text) {
        //extrahiert die Anzahl der Punkte aus dem PDF

        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(von (\\d+) möglichen\\)");
        java.util.regex.Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        return "n/a";
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamPage(stage);
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
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToAddTaskPage(stage);
    }

    @FXML
    public void switchToExamOverview() throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.switchToExamCollection(stage);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        super.logout(stage);
    }
}
