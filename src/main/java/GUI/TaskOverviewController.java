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

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); addRightClickMenu();
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


        rightClickMenu.getItems().addAll(editTaskItem, duplicateTask, deleteTaskItem);

        tableView.setOnMouseClicked(click -> {
            if (click.getButton() == MouseButton.SECONDARY) {
                rightClickMenu.show(tableView, click.getScreenX(), click.getScreenY());
            } else {
                rightClickMenu.hide();
            }
        });
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
