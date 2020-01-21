package pl.edu.pk.biblioteka.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Kontroler do wyświetlania statystyk
 */
public class StatPaneController {
    private static final Logger logger = Logger.getLogger(StatPaneController.class.getName());

    @FXML public LineChart<String, Integer> loanChart;
    @FXML public CategoryAxis xAxis;
    @FXML public NumberAxis yAxis;
    @FXML public TabPane statisticsPane;
    @FXML public Tab categoryTab;
    @FXML public BarChart<String, Integer> categoryChart;
    @FXML public CategoryAxis categoryAxis;
    @FXML public NumberAxis scoreAxis;

    @FXML
    public void initialize() {
        yAxis.setLabel("Ilość wypożyczeń");
        xAxis.setLabel("Miesiac");
        xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(
                "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec",
                "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień")));

        ObservableList<XYChart.Series<String, Integer>> data = loanChart.getData();
        XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
        series1.setName("Wypożyczenia za ostatni rok");

        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/stats")
                .build();

        if (httpResponse.isPresent()) {
            CloseableHttpResponse response = httpResponse.get();
            ResponseHandler.getBuilder(response)
                    .setOnSuccess((entity -> {
                        String string = EntityUtils.toString(entity);
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject months = jsonObject.getJSONObject("months");
                        series1.getData().add(new XYChart.Data<>("Styczeń", months.optInt("Jan", 0)));
                        series1.getData().add(new XYChart.Data<>("Luty", months.optInt("Feb", 0)));
                        series1.getData().add(new XYChart.Data<>("Marzec", months.optInt("Mar", 0)));
                        series1.getData().add(new XYChart.Data<>("Kwiecień", months.optInt("Apr", 0)));
                        series1.getData().add(new XYChart.Data<>("Maj", months.optInt("May", 0)));
                        series1.getData().add(new XYChart.Data<>("Czerwiec", months.optInt("Jun", 0)));
                        series1.getData().add(new XYChart.Data<>("Lipiec", months.optInt("Jul", 0)));
                        series1.getData().add(new XYChart.Data<>("Sierpień", months.optInt("Aug", 0)));
                        series1.getData().add(new XYChart.Data<>("Wrzesień", months.optInt("Sep", 0)));
                        series1.getData().add(new XYChart.Data<>("Październik", months.optInt("Oct", 0)));
                        series1.getData().add(new XYChart.Data<>("Listopad", months.optInt("Nov", 0)));
                        series1.getData().add(new XYChart.Data<>("Grudzień", months.optInt("Dec", 0)));
                    }))
                    .setOnFailure(logger::error)
                    .handle();
            data.add(series1);
        }

        statisticsPane.getTabs().remove(categoryTab);
    }
}
