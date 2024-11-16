package application.controller;

import application.database.TransactionDatabase;
import application.database.AccountDatabase;
import application.model.TransactionBean;
import application.model.AccountBean;
import application.model.ScheduledTransactionBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;

public class TransactionController {

    private final TransactionDatabase transactionDatabase = new TransactionDatabase();
    private final AccountDatabase accountDatabase = new AccountDatabase();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @FXML
    private TableView<TransactionBean> transactionTableView;
    @FXML
    private TableColumn<TransactionBean, String> accountCol;
    @FXML
    private TableColumn<TransactionBean, String> transactionTypeCol;
    @FXML
    private TableColumn<TransactionBean, LocalDate> transactionDateCol;
    @FXML
    private TableColumn<TransactionBean, String> transactionDescriptionCol;
    @FXML
    private TableColumn<TransactionBean, Double> paymentAmountCol;
    @FXML
    private TableColumn<TransactionBean, Double> depositAmountCol;
    @FXML
    private TableColumn<TransactionBean, Void> deleteTransactionCol;

    @FXML
    private TableView<ScheduledTransactionBean> scheduledTransactionTableView;
    @FXML
    private TableColumn<ScheduledTransactionBean, String> scheduleNameCol;
    @FXML
    private TableColumn<ScheduledTransactionBean, String> scheduledAccountCol;
    @FXML
    private TableColumn<ScheduledTransactionBean, String> scheduledTransactionTypeCol;
    @FXML
    private TableColumn<ScheduledTransactionBean, String> frequencyCol;
    @FXML
    private TableColumn<ScheduledTransactionBean, Integer> dueDateCol;
    @FXML
    private TableColumn<ScheduledTransactionBean, Double> scheduledPaymentAmountCol;
    @FXML
    private TableColumn<ScheduledTransactionBean, Void> deleteScheduledTransactionCol;

    @FXML
    private Button addTransactionButton;
    @FXML
    private Button addScheduledTransactionButton;

    @FXML
    public void initialize() {
        setupTransactionTableView();
        setupScheduledTransactionTableView();
        refreshTransactionTableData();
        refreshScheduledTransactionTableData();

        // Apply font scaling adjustments to the table cells
        applyFontScaling(transactionTableView);
        applyFontScaling(scheduledTransactionTableView);
    }

    // Method to apply font scaling to a TableView
    private void applyFontScaling(TableView<?> tableView) {
        tableView.setStyle("-fx-font-size: 16px;");
    }

    private void setupTransactionTableView() {
        accountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));

        transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionDateCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
                setStyle("-fx-font-size: 16px;");
            }
        });

        paymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        paymentAmountCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
                setStyle("-fx-font-size: 16px;");
            }
        });

        depositAmountCol.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));
        depositAmountCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
                setStyle("-fx-font-size: 16px;");
            }
        });

        // Add delete icon to each row in Transaction Table
        deleteTransactionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();

            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("/application/icons/delete.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitHeight(20);
                deleteIconView.setFitWidth(20);
                deleteButton.setGraphic(deleteIconView);
                deleteButton.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
                deleteButton.setOnAction(event -> {
                    TransactionBean transaction = getTableView().getItems().get(getIndex());
                    if (showConfirmation("Confirm Deletion", "Are you sure you want to delete this transaction?")) {
                        transactionDatabase.deleteTransaction(transaction);
                        refreshTransactionTableData();
                        showAlert("Success", "Transaction deleted successfully!");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
                setStyle("-fx-alignment: CENTER;");
            }
        });
    }

    private void setupScheduledTransactionTableView() {
        scheduleNameCol.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        scheduledAccountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        scheduledTransactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        scheduledPaymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        scheduledPaymentAmountCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
                setStyle("-fx-font-size: 16px;");
            }
        });

        // Add delete icon to each row in Scheduled Transaction Table
        deleteScheduledTransactionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();

            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("/application/icons/delete.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitHeight(20);
                deleteIconView.setFitWidth(20);
                deleteButton.setGraphic(deleteIconView);
                deleteButton.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
                deleteButton.setOnAction(event -> {
                    ScheduledTransactionBean scheduledTransaction = getTableView().getItems().get(getIndex());
                    if (showConfirmation("Confirm Deletion", "Are you sure you want to delete this scheduled transaction?")) {
                        transactionDatabase.deleteScheduledTransaction(scheduledTransaction);
                        refreshScheduledTransactionTableData();
                        showAlert("Success", "Scheduled transaction deleted successfully!");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
                setStyle("-fx-alignment: CENTER;");
            }
        });
    }

    @FXML
    private void handleAddTransaction() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Transaction");

        VBox form = createTransactionForm(popupStage);
        Scene popupScene = new Scene(form, 500, 400);

        // Link to the CSS file
        popupScene.getStylesheets().add(getClass().getResource("/css/transaction.css").toExternalForm());

        popupStage.setScene(popupScene);
        popupStage.show();
    }

    @FXML
    private void handleAddScheduledTransaction() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Scheduled Transaction");
        VBox form = createScheduledTransactionForm(popupStage);
        Scene popupScene = new Scene(form, 500, 400);

        // Link to the CSS file
        popupScene.getStylesheets().add(getClass().getResource("/css/transaction.css").toExternalForm());

        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private VBox createTransactionForm(Stage popupStage) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.getStyleClass().add("popup-grid");

        Label accountLabel = new Label("Account:");
        accountLabel.getStyleClass().add("popup-label");

        ComboBox<String> accountComboBox = new ComboBox<>();
        accountComboBox.getItems().addAll(getAccountNames());
        accountComboBox.getSelectionModel().selectFirst();
        accountComboBox.getStyleClass().add("popup-text-field");

        Label transactionTypeLabel = new Label("Transaction Type:");
        transactionTypeLabel.getStyleClass().add("popup-label");

        ComboBox<String> transactionTypeComboBox = new ComboBox<>();
        transactionTypeComboBox.setItems(transactionDatabase.getAllTransactionTypes());
        transactionTypeComboBox.getSelectionModel().selectFirst();
        transactionTypeComboBox.getStyleClass().add("popup-text-field");

        Label dateLabel = new Label("Transaction Date:");
        dateLabel.getStyleClass().add("popup-label");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("popup-date-picker");

        Label descriptionLabel = new Label("Description:");
        descriptionLabel.getStyleClass().add("popup-label");

        TextField descriptionField = new TextField();
        descriptionField.getStyleClass().add("popup-text-field");

        Label paymentLabel = new Label("Payment Amount:");
        paymentLabel.getStyleClass().add("popup-label");

        TextField paymentField = new TextField();
        paymentField.getStyleClass().add("popup-text-field");

        Label depositLabel = new Label("Deposit Amount:");
        depositLabel.getStyleClass().add("popup-label");

        TextField depositField = new TextField();
        depositField.getStyleClass().add("popup-text-field");

        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("popup-button");
        saveButton.setOnAction(e -> {
            try {
                if (accountComboBox.getValue() == null || transactionTypeComboBox.getValue() == null ||
                        descriptionField.getText().trim().isEmpty() ||
                        (paymentField.getText().isEmpty() && depositField.getText().isEmpty())) {
                    showAlert("Error", "All fields must be filled in, including 'Description' and either 'Payment Amount' or 'Deposit Amount'.");
                    return;
                }

                double paymentAmount = paymentField.getText().isEmpty() ? 0.0 : Double.parseDouble(paymentField.getText());
                double depositAmount = depositField.getText().isEmpty() ? 0.0 : Double.parseDouble(depositField.getText());

                transactionDatabase.addTransaction(new TransactionBean(
                        accountComboBox.getValue(),
                        transactionTypeComboBox.getValue(),
                        datePicker.getValue(),
                        descriptionField.getText(),
                        paymentAmount,
                        depositAmount
                ));

                refreshTransactionTableData();
                showAlert("Success", "Transaction saved successfully!");
                popupStage.close();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numeric values for payment and deposit amounts.");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("popup-button");
        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        grid.addRow(0, accountLabel, accountComboBox);
        grid.addRow(1, transactionTypeLabel, transactionTypeComboBox);
        grid.addRow(2, dateLabel, datePicker);
        grid.addRow(3, descriptionLabel, descriptionField);
        grid.addRow(4, paymentLabel, paymentField);
        grid.addRow(5, depositLabel, depositField);

        VBox vbox = new VBox(15, grid, buttonBox);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    private VBox createScheduledTransactionForm(Stage popupStage) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.getStyleClass().add("popup-grid");

        Label scheduleNameLabel = new Label("Schedule Name:");
        scheduleNameLabel.getStyleClass().add("popup-label");

        TextField scheduleNameField = new TextField();
        scheduleNameField.getStyleClass().add("popup-text-field");

        Label accountLabel = new Label("Account:");
        accountLabel.getStyleClass().add("popup-label");

        ComboBox<String> accountComboBox = new ComboBox<>(FXCollections.observableArrayList(getAccountNames()));
        accountComboBox.getSelectionModel().selectFirst();
        accountComboBox.getStyleClass().add("popup-text-field");

        Label transactionTypeLabel = new Label("Transaction Type:");
        transactionTypeLabel.getStyleClass().add("popup-label");

        ComboBox<String> transactionTypeComboBox = new ComboBox<>();
        transactionTypeComboBox.setItems(transactionDatabase.getAllTransactionTypes());
        transactionTypeComboBox.getSelectionModel().selectFirst();
        transactionTypeComboBox.getStyleClass().add("popup-text-field");

        Label frequencyLabel = new Label("Frequency:");
        frequencyLabel.getStyleClass().add("popup-label");

        ComboBox<String> frequencyComboBox = new ComboBox<>(FXCollections.observableArrayList("Monthly"));
        frequencyComboBox.getSelectionModel().selectFirst();
        frequencyComboBox.getStyleClass().add("popup-text-field");

        Label dueDateLabel = new Label("Due Date (Day of Month):");
        dueDateLabel.getStyleClass().add("popup-label");

        TextField dueDateField = new TextField();
        dueDateField.setPromptText("1-31");
        dueDateField.getStyleClass().add("popup-text-field");

        Label paymentAmountLabel = new Label("Payment Amount:");
        paymentAmountLabel.getStyleClass().add("popup-label");

        TextField paymentAmountField = new TextField();
        paymentAmountField.getStyleClass().add("popup-text-field");

        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("popup-button");
        saveButton.setOnAction(e -> handleSaveScheduledTransaction(scheduleNameField, accountComboBox, transactionTypeComboBox, frequencyComboBox, dueDateField, paymentAmountField, popupStage));

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("popup-button");
        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttonBox = new HBox(20, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        grid.addRow(0, scheduleNameLabel, scheduleNameField);
        grid.addRow(1, accountLabel, accountComboBox);
        grid.addRow(2, transactionTypeLabel, transactionTypeComboBox);
        grid.addRow(3, frequencyLabel, frequencyComboBox);
        grid.addRow(4, dueDateLabel, dueDateField);
        grid.addRow(5, paymentAmountLabel, paymentAmountField);

        VBox formLayout = new VBox(10, grid, buttonBox);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));

        return formLayout;
    }

    private void handleSaveScheduledTransaction(TextField scheduleNameField, ComboBox<String> accountComboBox, ComboBox<String> transactionTypeComboBox, ComboBox<String> frequencyComboBox, TextField dueDateField, TextField paymentAmountField, Stage popupStage) {

        String scheduleName = scheduleNameField.getText().trim();
        String account = accountComboBox.getValue();
        String transactionType = transactionTypeComboBox.getValue();
        String frequency = frequencyComboBox.getValue();
        String dueDateText = dueDateField.getText().trim();
        String paymentText = paymentAmountField.getText().trim();

        if (scheduleName.isEmpty() || account == null || transactionType == null || frequency == null || dueDateText.isEmpty() || paymentText.isEmpty()) {
            showAlert("Error", "All fields must be filled out.");
            return;
        }

        try {
            int dueDate = Integer.parseInt(dueDateText);
            if (dueDate < 1 || dueDate > 31) {
                showAlert("Error", "Due Date must be between 1 and 31.");
                return;
            }

            double paymentAmount = Double.parseDouble(paymentText);
            ScheduledTransactionBean scheduledTransaction = new ScheduledTransactionBean(scheduleName, account, transactionType, frequency, dueDate, paymentAmount);

            try {
                transactionDatabase.addScheduledTransaction(scheduledTransaction);
                refreshScheduledTransactionTableData();
                showAlert("Success", "Scheduled transaction added successfully!");
                popupStage.close();
            } catch (SQLException e) {
                if (e.getMessage().contains("UNIQUE constraint failed")) {
                    showAlert("Duplicate Schedule Name", "A scheduled transaction with the name '" + scheduledTransaction.getScheduleName() + "' already exists. Please use a unique name.");
                } else {
                    e.printStackTrace();
                    showAlert("Error Adding Scheduled Transaction", "An unexpected error occurred. Could not add the scheduled transaction. Please try again.");
                }
                scheduleNameField.requestFocus();
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Due Date must be a valid integer, and Payment Amount must be a valid number.");
        }
    }

    private ObservableList<String> getAccountNames() {
        return FXCollections.observableArrayList(accountDatabase.getAllAccounts().stream().map(AccountBean::getName).toList());
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshTransactionTableData() {
        transactionTableView.setItems(transactionDatabase.getAllTransactions());
    }

    private void refreshScheduledTransactionTableData() {
        ObservableList<ScheduledTransactionBean> scheduledTransactions = transactionDatabase.getAllScheduledTransactions();
        scheduledTransactions.sort((a, b) -> Integer.compare(a.getDueDate(), b.getDueDate()));
        scheduledTransactionTableView.setItems(scheduledTransactions);
    }
}
