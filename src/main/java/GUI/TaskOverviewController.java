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
import java.util.Optional;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskOverviewController extends SceneController implements Initializable {

    @FXML
    private MenuButton menuBar;

    @FXML
    private TableView<Object[]> tableView;

    @FXML
    private TableColumn<Object[], String> nameColumn;
    @FXML
    private TableColumn<Object[], String> modulColumn;
    @FXML
    private TableColumn<Object[], String> taxonomieColumn;
    @FXML
    private TableColumn<Object[], String> typColumn;
    @FXML
    private TableColumn<Object[], Integer> punkteColumn;



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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setzt die Attribute so dass die Ansicht passend mit skaliert

        testTable();

        nameColumn.setResizable(true);
        nameColumn.setMaxWidth(Double.MAX_VALUE);
        modulColumn.setResizable(true);
        modulColumn.setMaxWidth(Double.MAX_VALUE);
        taxonomieColumn.setResizable(true);
        taxonomieColumn.setMaxWidth(Double.MAX_VALUE);
        typColumn.setResizable(true);
        typColumn.setMaxWidth(Double.MAX_VALUE);
        punkteColumn.setResizable(true);
        punkteColumn.setMaxWidth(Double.MAX_VALUE);

        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));//0.2, da jede der 5 Spaltem 1/5 des Platzes bekommt
        modulColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        taxonomieColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        typColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        punkteColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); addRightClickMenu();
    }

    private void testTable(){
        //Methode zum Testen der Sortierfunktion der Tabelle

        ObservableList<Object[]> data = FXCollections.observableArrayList(
                new Object[]{"Aufgabe 1", "Mathematik", "Wissen", "Multiple Choice", 10},
                new Object[]{"Aufgabe 2", "Informatik", "Anwenden", "Kurzantwort", 8},
                new Object[]{"Aufgabe 3", "Biologie", "Verstehen", "Wahlaufgabe", 12},
                new Object[]{"Aufgabe 4", "Chemie", "Analysieren", "Multiple Choice", 15}
        );

        tableView.setItems(data);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[0]));
        modulColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[1]));
        taxonomieColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[2]));
        typColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[3]));
        punkteColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((Integer) cellData.getValue()[4]).asObject());
    }

    private void addRightClickMenu() {
        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem deleteTaskItem = new MenuItem("Aufgabe löschen");

        deleteTaskItem.setOnAction(event -> {
            ObservableList<Object[]> selectedTasks = tableView.getSelectionModel().getSelectedItems();

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
