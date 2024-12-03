package application.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import application.database.AccountDatabase;
import application.database.TransactionDatabase;
import application.model.ScheduledTransactionBean;
import application.model.TransactionBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeController {

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Label accountsLabel;

    @FXML
    private Label transactionsLabel;

    @FXML
    private Label scheduledTransactionsLabel;

    @FXML
    private Label lineChartDataLabel;

    @FXML
    private Label barChartDataLabel;

    @FXML
    private Label pieChartDataLabel;

    // Buttons in the Quick Access section
    @FXML
    private Button addAccountButton;

    @FXML
    private Button addTransactionButton;

    @FXML
    private Button addScheduledTransactionButton;

    private final AccountDatabase accountDatabase = new AccountDatabase();
    private final TransactionDatabase transactionDatabase = new TransactionDatabase();

    private final Map<Integer, Double> monthlyExpenses = new HashMap<>();
    private double monthlyIncome = 0.0;
    private double monthlyExpensesTotal = 0.0;

    // Controller instances
    private AccountController accountController;
    private TransactionController transactionController;

    // Variables to hold temporary labels or data
    private XYChart.Data<String, Number> selectedLineDataPoint;
    private XYChart.Data<String, Number> selectedBarDataPoint;
    private PieChart.Data selectedPieData;

    @FXML
    public void initialize() {
        setupLineChart();
        setupPieChart();
        setupBarChart();
        updateSummaryLabels();
        updatePieChartData();
        updateExpenseTrends();
        loadBarChartData();

        // Load controllers directly
        loadAccountController();
        loadTransactionController();

        // Hide the labels initially
        lineChartDataLabel.setVisible(false);
        barChartDataLabel.setVisible(false);
        pieChartDataLabel.setVisible(false);
    }

    private void loadAccountController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Accounts.fxml"));
            loader.load();
            accountController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTransactionController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Transactions.fxml"));
            loader.load();
            transactionController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupLineChart() {
        lineChart.getData().clear();
        lineChart.setTitle("Expense Trends");
    }

    private void setupPieChart() {
        pieChart.getData().clear();
        pieChart.setTitle("Monthly Income Versus Expenses");
    }

    private void setupBarChart() {
        barChart.getData().clear();
        barChart.setTitle("Expenses by Transaction Type");
    }

    public void updateSummaryLabels() {
        try {
            int accountCount = accountDatabase.getAllAccounts().size();
            int transactionCount = transactionDatabase.getAllTransactions().size();
            int scheduledTransactionCount = transactionDatabase.getAllScheduledTransactions().size();

            accountsLabel.setText(String.valueOf(accountCount));
            transactionsLabel.setText(String.valueOf(transactionCount));
            scheduledTransactionsLabel.setText(String.valueOf(scheduledTransactionCount));
        } catch (Exception e) {
            accountsLabel.setText("Error");
            transactionsLabel.setText("Error");
            scheduledTransactionsLabel.setText("Error");
            e.printStackTrace();
        }
    }

    public void updatePieChartData() {
        try {
            monthlyIncome = 0.0;
            monthlyExpensesTotal = 0.0;

            LocalDate today = LocalDate.now();
            int currentMonth = today.getMonthValue();
            int currentYear = today.getYear();
            int currentDay = today.getDayOfMonth();

            List<TransactionBean> transactions = transactionDatabase.getAllTransactions();
            List<ScheduledTransactionBean> scheduledTransactions = transactionDatabase.getAllScheduledTransactions();

            // Process regular transactions for the current month
            for (TransactionBean transaction : transactions) {
                if (transaction.getTransactionDate().getMonthValue() == currentMonth && transaction.getTransactionDate().getYear() == currentYear) {
                    if (transaction.getPaymentAmount() > 0) {
                        monthlyExpensesTotal += transaction.getPaymentAmount();
                    }
                    if (transaction.getDepositAmount() > 0) {
                        monthlyIncome += transaction.getDepositAmount();
                    }
                }
            }

            // Process scheduled transactions for the current month
            for (ScheduledTransactionBean scheduledTransaction : scheduledTransactions) {
                int dueDate = scheduledTransaction.getDueDate();
                if (dueDate > 31) continue;

                if (dueDate <= currentDay) {
                    if (scheduledTransaction.getPaymentAmount() > 0) {
                        monthlyExpensesTotal += scheduledTransaction.getPaymentAmount();
                    }
                }
            }

            pieChart.getData().clear();
            PieChart.Data incomeData = new PieChart.Data("Income", monthlyIncome);
            PieChart.Data expenseData = new PieChart.Data("Expenses", monthlyExpensesTotal);

            pieChart.getData().addAll(incomeData, expenseData);

            pieChart.getData().forEach(data -> {
                if (data.getName().equals("Income")) {
                    data.getNode().setStyle("-fx-pie-color: green;");
                } else if (data.getName().equals("Expenses")) {
                    data.getNode().setStyle("-fx-pie-color: red;");
                }

                // Add event handler for toggling label display
                data.getNode().setOnMouseClicked(event -> {
                    if (selectedPieData != null && selectedPieData == data) {
                        pieChartDataLabel.setVisible(false);
                        selectedPieData = null;
                    } else {
                        selectedPieData = data;
                        String name = data.getName();
                        double value = data.getPieValue();
                        pieChartDataLabel.setText(name + ": $" + String.format("%.2f", value));
                        pieChartDataLabel.setVisible(true);
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateExpenseTrends() {
        try {
            monthlyExpenses.clear();

            List<TransactionBean> transactions = transactionDatabase.getAllTransactions();
            List<ScheduledTransactionBean> scheduledTransactions = transactionDatabase.getAllScheduledTransactions();

            LocalDate today = LocalDate.now();
            int currentYear = today.getYear();
            int currentMonth = today.getMonthValue();
            int currentDay = today.getDayOfMonth();

            // Define starting month based on when transactions started
            int startingMonth = 1; // Default to January
            if (currentYear == 2023) {
                startingMonth = 10; // Started transactions in October 2023
            }

            // Initialize monthlyExpenses with 0.0 for each month from Jan to Dec
            for (int month = 1; month <= 12; month++) {
                monthlyExpenses.put(month, 0.0);
            }

            // Process regular transactions from startingMonth onwards
            for (TransactionBean transaction : transactions) {
                LocalDate transactionDate = transaction.getTransactionDate();
                int transactionMonth = transactionDate.getMonthValue();
                int transactionYear = transactionDate.getYear();

                if (transactionYear == currentYear && transactionMonth >= startingMonth) {
                    double amount = transaction.getPaymentAmount() - transaction.getDepositAmount();
                    monthlyExpenses.merge(transactionMonth, amount, Double::sum);
                }
            }

            // Process scheduled transactions for current and future months only
            int scheduledStartMonth = Math.max(currentMonth, startingMonth);
            for (ScheduledTransactionBean scheduledTransaction : scheduledTransactions) {
                int dueDate = scheduledTransaction.getDueDate();
                if (dueDate > 31) continue;

                double paymentAmount = scheduledTransaction.getPaymentAmount();

                for (int month = scheduledStartMonth; month <= 12; month++) {
                    if (month == currentMonth) {
                        if (dueDate <= currentDay) {
                            monthlyExpenses.merge(month, paymentAmount, Double::sum);
                        }
                    } else {
                        monthlyExpenses.merge(month, paymentAmount, Double::sum);
                    }
                }
            }

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Expense Trends");

            for (int month = 1; month <= 12; month++) {
                double totalForMonth = monthlyExpenses.getOrDefault(month, 0.0);
                final int finalMonth = month;
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(getMonthName(finalMonth), totalForMonth);

                series.getData().add(dataPoint);
            }

            lineChart.getData().clear();
            lineChart.getData().add(series);

            // Update data point selection listener
            addLineChartDataPointHandlers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMonthName(int month) {
        return java.time.Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    private void addLineChartDataPointHandlers() {
        Platform.runLater(() -> {
            for (XYChart.Series<String, Number> series : lineChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    Node node = data.getNode();
                    node.setOnMouseClicked(event -> {
                        if (selectedLineDataPoint != null && selectedLineDataPoint == data) {
                            // Hide label
                            lineChartDataLabel.setVisible(false);
                            selectedLineDataPoint = null;
                        } else {
                            // Show label
                            selectedLineDataPoint = data;
                            String month = data.getXValue();
                            Number amount = data.getYValue();
                            lineChartDataLabel.setText("Month: " + month + ", Amount: $" + String.format("%.2f", amount.doubleValue()));
                            lineChartDataLabel.setVisible(true);
                        }
                    });
                }
            }
        });
    }

    private void loadBarChartData() {
        barChart.getData().clear();

        var transactionTypes = transactionDatabase.getAllTransactionTypes();

        if (transactionTypes.isEmpty()) {
            // Optionally, display a message or leave the chart empty
            return;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        var dataMap = transactionDatabase.getTransactionTypeAmounts();

        for (String type : transactionTypes) {
            double amount = dataMap.getOrDefault(type, 0.0);
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(type, amount);
            series.getData().add(dataPoint);

            // Add listener to ensure the node is available before setting the event handler
            dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setOnMouseClicked(event -> {
                        if (selectedBarDataPoint != null && selectedBarDataPoint == dataPoint) {
                            barChartDataLabel.setVisible(false);
                            selectedBarDataPoint = null;
                        } else {
                            selectedBarDataPoint = dataPoint;
                            String typeName = dataPoint.getXValue();
                            Number value = dataPoint.getYValue();
                            barChartDataLabel.setText(typeName + ": $" + String.format("%.2f", value.doubleValue()));
                            barChartDataLabel.setVisible(true);
                        }
                    });
                }
            });
        }

        barChart.getData().add(series);
    }

    /**
     * Clears temporary labels or data, such as the small table point displays
     * when navigating to a new page.
     */
    public void clearTemporaryLabels() {
        if (selectedLineDataPoint != null) {
            selectedLineDataPoint = null;
        }
        if (selectedBarDataPoint != null) {
            selectedBarDataPoint = null;
        }
        if (selectedPieData != null) {
            selectedPieData = null;
        }
        lineChartDataLabel.setText("");
        lineChartDataLabel.setVisible(false);
        barChartDataLabel.setText("");
        barChartDataLabel.setVisible(false);
        pieChartDataLabel.setText("");
        pieChartDataLabel.setVisible(false);
    }

    // Methods to handle Quick Access button actions

    @FXML
    private void handleAddAccount() {
        if (accountController != null) {
            accountController.showAccountPopup();
            accountController.getPopupStage().setOnHidden(event -> updateSummaryLabels());
        } else {
            System.err.println("AccountController is not initialized");
        }
    }

    @FXML
    private void handleAddTransaction() {
        if (transactionController != null) {
            transactionController.handleAddTransaction();
            transactionController.getPopupStage().setOnHidden(event -> {
                updateSummaryLabels();
                updatePieChartData();
                updateExpenseTrends();
                loadBarChartData();
            });
        } else {
            System.err.println("TransactionController is not initialized");
        }
    }

    @FXML
    private void handleAddScheduledTransaction() {
        if (transactionController != null) {
            transactionController.handleAddScheduledTransaction();
            transactionController.getPopupStage().setOnHidden(event -> {
                updateSummaryLabels();
                updateExpenseTrends();
                loadBarChartData();
            });
        } else {
            System.err.println("TransactionController is not initialized");
        }
    }
}
