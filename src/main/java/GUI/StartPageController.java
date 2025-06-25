package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import org.example.Main;
import org.example.domain.Task;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StartPageController extends SceneController {

    @FXML
    private VBox vBoxPieChartTax;
    @FXML
    private VBox vBoxPieChartModul;

    @FXML
    public void initialize() throws SQLException {
        updateCharts();
    }

    public void updateCharts() throws SQLException {

        vBoxPieChartTax.getChildren().clear();
        vBoxPieChartModul.getChildren().clear();

        Task.getAllTasks(Main.id);

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

            modulData.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
            taxData.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));

            CustomPieChart pieChartTax = new CustomPieChart(taxData, false);
            CustomPieChart pieChartModul = new CustomPieChart(modulData, true);

            pieChartTax.setPrefSize(300, 300);
            pieChartModul.setPrefSize(300, 300);

            pieChartTax.setLabelsVisible(false);
            pieChartTax.setLegendVisible(false);
            pieChartModul.setLabelsVisible(false);
            pieChartModul.setLegendVisible(false);

            pieChartTax.setTitle("Taxonomien");
            pieChartModul.setTitle("Module");

            vBoxPieChartTax.getChildren().add(pieChartTax);
            vBoxPieChartModul.getChildren().add(pieChartModul);
        }
    }
}

