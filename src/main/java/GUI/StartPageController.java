package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import org.example.domain.Task;

import java.util.HashMap;
import java.util.Map;

public class StartPageController extends SceneController {

    @FXML
    private VBox vBoxPieChartTax;   // Container im FXML, z.B. VBox oder Pane, wo PieChart rein soll
    @FXML
    private VBox vBoxPieChartModul;

    @FXML
    public void initialize() {
        //erstellt Diagramme zur Übersicht auf der Startseite

        ObservableList<Task> tasks = FXCollections.observableArrayList(Task.tasks);
        Map<String, Integer> modulCounter = new HashMap<>();
        Map<String, Integer> taxonomieCounter = new HashMap<>();

        if (!tasks.isEmpty()) {

            for (Task task : tasks) {
                String modul = task.getModul().getName();
                String taxonomie = task.getQuestion().getTaxonomie().name();

                modulCounter.put(modul, modulCounter.getOrDefault(modul, 0) + 1);
                taxonomieCounter.put(taxonomie, taxonomieCounter.getOrDefault(taxonomie, 0) + 1);
            }

            ObservableList<PieChart.Data> modulData = FXCollections.observableArrayList();
            ObservableList<PieChart.Data> taxData = FXCollections.observableArrayList();

            modulCounter.forEach((key, value) -> modulData.add(new PieChart.Data(key, value)));
            taxonomieCounter.forEach((key, value) -> taxData.add(new PieChart.Data(key, value)));

            CustomPieChart pieChartTax = new CustomPieChart(taxData);
            CustomPieChart pieChartModul = new CustomPieChart(modulData);

            pieChartTax.setPrefSize(300, 300);
            pieChartModul.setPrefSize(300, 300);

            pieChartTax.setLabelsVisible(false);
            pieChartTax.setLegendVisible(false);
            pieChartModul.setLabelsVisible(false);
            pieChartModul.setLegendVisible(false);

            pieChartTax.setTitle("Taxonomien");
            pieChartModul.setTitle("Modul");

            vBoxPieChartTax.getChildren().add(pieChartTax);
            vBoxPieChartModul.getChildren().add(pieChartModul);
        }
    }
}
