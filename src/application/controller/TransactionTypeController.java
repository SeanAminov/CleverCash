package application.controller;

import application.database.TransactionDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class TransactionTypeController implements Initializable {

    @FXML
    Button addTransactionTypeButton;

    private final TransactionDatabase transactionDatabase = new TransactionDatabase();

    @FXML
    private BarChart<String, Number> transactionTypeBarChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBarChartData();
    }

    private void loadBarChartData() {
        transactionTypeBarChart.getData().clear();

        var transactionTypes = transactionDatabase.getAllTransactionTypes();

        if (transactionTypes.isEmpty()) {
            // Optionally, display a message or leave the chart empty
            return;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        var dataMap = transactionDatabase.getTransactionTypeAmounts();

        for (String type : transactionTypes) {
            double amount = dataMap.getOrDefault(type, 0.0);
            series.getData().add(new XYChart.Data<>(type, amount));
        }

        transactionTypeBarChart.getData().add(series);
    }

    @FXML
    public void showTransactionTypePopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Transaction Type");

        // Create form elements
        Label transactionTypeLabel = new Label("Transaction Type:");
        transactionTypeLabel.getStyleClass().add("popup-label");

        TextField transactionTypeField = new TextField();
        transactionTypeField.setPromptText("Enter new transaction type");
        transactionTypeField.getStyleClass().add("popup-text-field");

        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("popup-button");
        saveButton.setOnAction(e -> {
            String newType = transactionTypeField.getText().trim();
            if (newType.isEmpty()) {
                showAlert("Error", "Transaction type cannot be empty.");
                return;
            }
            if (transactionDatabase.transactionTypeExists(newType)) {
                showAlert("Error", "This transaction type already exists.");
                return;
            }
            transactionDatabase.addTransactionType(newType);
            showAlert("Success", "Transaction type added successfully.");
            loadBarChartData(); // Reload bar chart data
            popupStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("popup-button");
        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.getStyleClass().add("popup-grid");

        grid.addRow(0, transactionTypeLabel, transactionTypeField);

        VBox layout = new VBox(15, grid, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout);
        // Link to the CSS file
        scene.getStylesheets().add(getClass().getResource("/css/transaction.css").toExternalForm());

        popupStage.setScene(scene);
        popupStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}