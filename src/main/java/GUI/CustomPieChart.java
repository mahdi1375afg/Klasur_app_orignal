package GUI;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.example.domain.Modul;
import org.example.domain.Task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomPieChart extends PieChart {

    private final List<Color> colorPalette = new ArrayList<>();

    public CustomPieChart(ObservableList<Data> data, boolean setRightClickMenu) {
        //erstellt das chart, sowie die Legende mit benutzerdefinierten Farben
        super(data);
        setLegendSide(Side.RIGHT);
        setClockwise(true);

        sceneProperty().addListener((obs, oldScene, newScene) -> {
            generateColorPalette(getData().size());
            applyCustomColors();
            createCustomScrollableLegend(setRightClickMenu);
        });
    }

    private void createCustomScrollableLegend(boolean setRightClickMenu) {
        //erstellt eine passende scrollbare Legende für das chart

        Node oldLegend = lookup(".chart-legend");
        if (oldLegend == null) return;

        oldLegend.setVisible(false);
        oldLegend.setManaged(false);

        Node parentNode = oldLegend.getParent();
        if (parentNode == null) return;

        Node grandParentNode = parentNode.getParent();

        if (!(grandParentNode instanceof VBox pane)) return;

        VBox legendBox = new VBox(5);
        legendBox.setPadding(new Insets(5));
        legendBox.setStyle("-fx-background-color: transparent;");

        ObservableList<Data> dataList = getData();
        for (int i = 0; i < dataList.size(); i++) {
            Data d = dataList.get(i);
            HBox item = new HBox(5);
            item.setAlignment(Pos.CENTER_LEFT);

            Rectangle colorBox = new Rectangle(12, 12);
            Color color = colorPalette.get(i);
            colorBox.setFill(color);
            colorBox.setStroke(Paint.valueOf("transparent"));

            Label label = new Label(d.getName());
            label.setStyle("-fx-font-size: 12px;");

            item.getChildren().addAll(colorBox, label);

            item.setOnMouseEntered(e -> highlightSegment(d, item));
            item.setOnMouseExited(e -> resetSegment(d, item));

            if (setRightClickMenu) {
                addRightClickMenu(item, d);
            }

            legendBox.getChildren().add(item);
        }

        ScrollPane scrollPane = new ScrollPane(legendBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefViewportHeight(300);

        pane.getChildren().add(scrollPane);
    }

    private void addRightClickMenu(HBox item, Data data) {
        //fügt Menü ein, dass bei Rechtsklick geöffnet wird

        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Löschen");
        deleteItem.setOnAction(event -> {

            boolean confirm = showAlert();
            if (confirm) {
                List<Task> deletableTasks = new ArrayList<>();
                for (Task task : Task.tasks) {
                    if (task.getModul().getName().equals(data.getName())) {
                        deletableTasks.add(task);
                    }
                }

                for (Task task : deletableTasks) {
                    try {
                        Task.deleteTask(task);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                Modul deletableModul = null;
                for(Modul modul : Modul.modules) {
                    if(modul.getName().equals(data.getName())) {
                        deletableModul = modul;
                    }
                }
                try {
                    assert deletableModul != null;
                    Modul.deleteModul(deletableModul);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //TODO: Löschen auch aktualisierung der PieCharts und Legenden
                System.out.println("Deleted: " + data.getName());
            }
        });

        MenuItem printItem = new MenuItem("Exportieren");
        printItem.setOnAction(e -> {
            //ToDo: Druckfunktion einfügen
        });

        contextMenu.getItems().addAll(deleteItem, printItem);

        item.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(item, e.getScreenX(), e.getScreenY());
            } else if (e.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
            }
        });
    }
    private void highlightSegment(Data d, HBox item) {
        // Alle zurücksetzen
        for (Data datum : getData()) {
            if (datum.getNode() != null) {
                datum.getNode().setScaleX(1.0);
                datum.getNode().setScaleY(1.0);
                datum.getNode().setOpacity(1.0);
            }
        }
        item.setStyle("-fx-background-color: lightgray; -fx-cursor: hand;");
        if (d.getNode() != null) {
            d.getNode().setScaleX(1.1);
            d.getNode().setScaleY(1.1);
            d.getNode().setOpacity(0.8);
        }
    }

    private void resetSegment(Data d, HBox item) {
        item.setStyle("-fx-background-color: transparent;");
        if (d.getNode() != null) {
            d.getNode().setScaleX(1.0);
            d.getNode().setScaleY(1.0);
            d.getNode().setOpacity(1.0);
        }
    }


    private void generateColorPalette(int size) {
        //erstellt eine individuelle Farbpalette für die Diagramme

        colorPalette.clear();
        for (int i = 0; i < size; i++) {
            double hue = 360.0 * i / size;
            Color color = Color.hsb(hue, 0.7, 0.9);
            colorPalette.add(color);
        }
    }

    private void applyCustomColors() {
        //setzt die individuellen Farben für die einzelnen Segmente des Charts

        ObservableList<Data> dataList = getData();
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.get(i);
            Color color = colorPalette.get(i);
            String colorStr = toHexString(color);
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + colorStr + ";");
            }
        }
    }

    private String toHexString(Color color) {
        //wandelt die Farbwerte eines Color-Objekts in Hex-Werte um

        return String.format("#%02X%02X%02X",
                (int)(color.getRed()*255),
                (int)(color.getGreen()*255),
                (int)(color.getBlue()*255));
    }

    protected boolean showAlert() {
        //Zeigt Fehlermeldung an, wenn Daten beim Seitenwechsel verloren gehen würden

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Achtung!");
        alert.setHeaderText(null);
        alert.setContentText("Es werden alle zugehörigen Aufgaben gelöscht.");

        boolean[] result = new boolean[1];

        //Überprüft, welche Schaltfläche der Benutzer gedrückt hat
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result[0] = true;
            }
        });

        return result[0];
    }
}
