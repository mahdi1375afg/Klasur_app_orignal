package GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import org.example.domain.Task;
import org.example.domain.benutzerKonto;

import java.sql.SQLException;
import java.util.Optional;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        //ToDo: Richtige ID eintragen & Format der Aufgabe richtig ausgeben
        ObservableList<Task> tasks = FXCollections.observableArrayList(Task.tasks);
        tableView.setItems(tasks);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestion().getName()));
        modulColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModul().getName()));
        taxonomieColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestion().getTaxonomie().name()));
        typColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestion().getFormat().name()));
        pointsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuestion().getPoints()).asObject());

    }


    private void addRightClickMenu() {
        //ToDo: Mit DB verbinden und richtige Aufgaben löschen
        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem deleteTaskItem = new MenuItem("Aufgabe löschen");

        deleteTaskItem.setOnAction(event -> {
            ObservableList<Task> selectedTasks = tableView.getSelectionModel().getSelectedItems();

            if (selectedTasks.isEmpty())
                return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Löschen bestätigen");
            alert.setHeaderText("Aufgabe löschen");
            alert.setContentText("Möchten Sie die ausgewählte Aufgabe wirklich löschen?");

            Optional<ButtonType> userResponse = alert.showAndWait();
            if (userResponse.isPresent() && userResponse.get() == ButtonType.OK) {
                tableView.getItems().removeAll(selectedTasks);
            }

        });

        rightClickMenu.getItems().add(deleteTaskItem);

        tableView.setOnMouseClicked(click -> {
            if (click.getButton() == MouseButton.SECONDARY) {
                rightClickMenu.show(tableView, click.getScreenX(), click.getScreenY());
            } else {
                rightClickMenu.hide();
            }
        });
    }

}
