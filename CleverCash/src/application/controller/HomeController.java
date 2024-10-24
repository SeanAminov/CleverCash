package application.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class HomeController {

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private PieChart pieChart;

    @FXML
    public void initialize() {
        setupLineChart();
        setupPieChart();
    }

    private void setupLineChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expense Trends");

        // Add data points to the series
        series.getData().add(new XYChart.Data<>("Jan.", 0));
        series.getData().add(new XYChart.Data<>("Feb.", 0));
        series.getData().add(new XYChart.Data<>("Mar.", 0));
        series.getData().add(new XYChart.Data<>("Apr.", 0));
        series.getData().add(new XYChart.Data<>("May", 0));
        series.getData().add(new XYChart.Data<>("Jun.", 0));
        series.getData().add(new XYChart.Data<>("Jul.", 0));
        series.getData().add(new XYChart.Data<>("Aug.", 0));
        series.getData().add(new XYChart.Data<>("Sept.", 0));
        series.getData().add(new XYChart.Data<>("Oct.", 0));
        series.getData().add(new XYChart.Data<>("Nov.", 0));
        series.getData().add(new XYChart.Data<>("Dec.", 0));

        lineChart.getData().add(series);	
    }

    private void setupPieChart() {
        PieChart.Data income = new PieChart.Data("Income", 0);
        PieChart.Data expense = new PieChart.Data("Expenses", 0);

        pieChart.getData().addAll(income, expense);
    }
}
