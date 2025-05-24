package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import org.example.domain.Task;

import java.util.HashMap;
import java.util.Map;

public class StartPageController extends SceneController{


    @FXML
    private PieChart pieChartTax;
    @FXML
    private PieChart pieChartModul;

    @FXML
    public void initialize(){
        //Erstellt Pie charts mit Übersicht über die Datenbank
        //ToDo: Schriftart kleiner machen o.ä. um skalieren zu können bei vielen Modulen

        ObservableList<Task> tasks = FXCollections.observableArrayList(Task.tasks);
        Map<String, Integer> modulCounter = new HashMap<>();
        Map<String, Integer> taxonomieCounter = new HashMap<>();


        for (Task task : tasks) {
            String modul = task.getModul().getName();
            String taxonomie = task.getQuestion().getTaxonomie().name();

            modulCounter.put(modul ,modulCounter.getOrDefault(modul, 0)+1);
            taxonomieCounter.put(taxonomie,taxonomieCounter.getOrDefault(taxonomie, 0)+1);
        }

        ObservableList<PieChart.Data> modulData = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> taxData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : modulCounter.entrySet()) {
            modulData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        for (Map.Entry<String, Integer> entry : taxonomieCounter.entrySet()) {
            taxData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChartTax.setData(taxData);
        pieChartModul.setData(modulData);

        pieChartTax.setTitle("Taxonomie");
        pieChartModul.setTitle("Module");

        pieChartTax.setData(taxData);
        pieChartModul.setData(modulData);

        pieChartModul.setLabelsVisible(false);
        pieChartModul.setLegendVisible(true);
        pieChartTax.setLabelsVisible(false);
        pieChartTax.setLegendVisible(true);
    }

}

