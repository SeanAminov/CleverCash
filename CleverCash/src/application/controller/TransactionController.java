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
import javafx.scene.Node;
import javafx.event.EventHandler;

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
    private TextField searchTransactionField;
    @FXML
    private TextField searchScheduledTransactionField;

    @FXML
    public void initialize() {
        setupTransactionTableView();
        setupScheduledTransactionTableView();
        refreshTransactionTableData();
        refreshScheduledTransactionTableData();

        // Apply font scaling adjustments to the table cells
        applyFontScaling(transactionTableView);
        applyFontScaling(scheduledTransactionTableView);

        // Add double-click event handlers to tables
        addDoubleClickEvent(transactionTableView, this::handleEditTransaction);
        addDoubleClickEvent(scheduledTransactionTableView, this::handleEditScheduledTransaction);

        // Add listeners to search fields
        searchTransactionField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTransactions(newValue);
        });

        searchScheduledTransactionField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterScheduledTransactions(newValue);
        });
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
        transactionDateCol.setCellFactory(column -> createDateCell());

        paymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        paymentAmountCol.setCellFactory(column -> createAmountCell());

        depositAmountCol.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));
        depositAmountCol.setCellFactory(column -> createAmountCell());

        // Add delete icon to each row in Transaction Table
        deleteTransactionCol.setCellFactory(col -> createDeleteButtonCell(
            (TransactionBean transaction) -> {
                if (showConfirmation("Confirm Deletion", "Are you sure you want to delete this transaction?")) {
                    transactionDatabase.deleteTransaction(transaction);
                    refreshTransactionTableData();
                    showAlert("Success", "Transaction deleted successfully!");
                }
            }
        ));
    }

    private void setupScheduledTransactionTableView() {
        scheduleNameCol.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        scheduledAccountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        scheduledTransactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        scheduledPaymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        scheduledPaymentAmountCol.setCellFactory(column -> createAmountCell());

        // Add delete icon to each row in Scheduled Transaction Table
        deleteScheduledTransactionCol.setCellFactory(col -> createDeleteButtonCell(
            (ScheduledTransactionBean scheduledTransaction) -> {
                if (showConfirmation("Confirm Deletion", "Are you sure you want to delete this scheduled transaction?")) {
                    transactionDatabase.deleteScheduledTransaction(scheduledTransaction);
                    refreshScheduledTransactionTableData();
                    showAlert("Success", "Scheduled transaction deleted successfully!");
                }
            }
        ));
    }

    @FXML
    private void handleAddTransaction() {
        showTransactionForm(null); // null indicates new transaction
    }

    @FXML
    private void handleAddScheduledTransaction() {
        showScheduledTransactionForm(null); // null indicates new scheduled transaction
    }

    private void showTransactionForm(TransactionBean transaction) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(transaction == null ? "Add Transaction" : "Edit Transaction");

        VBox form = createTransactionForm(popupStage, transaction);
        Scene popupScene = new Scene(form, 500, 400);

        // Link to the CSS file
        popupScene.getStylesheets().add(getClass().getResource("/css/transaction.css").toExternalForm());

        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private void showScheduledTransactionForm(ScheduledTransactionBean scheduledTransaction) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(scheduledTransaction == null ? "Add Scheduled Transaction" : "Edit Scheduled Transaction");

        VBox form = createScheduledTransactionForm(popupStage, scheduledTransaction);
        Scene popupScene = new Scene(form, 500, 400);

        // Link to the CSS file
        popupScene.getStylesheets().add(getClass().getResource("/css/transaction.css").toExternalForm());

        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private VBox createTransactionForm(Stage popupStage, TransactionBean transaction) {
        GridPane grid = createFormGrid();

        ComboBox<String> accountComboBox = createComboBox(getAccountNames());
        ComboBox<String> transactionTypeComboBox = createComboBox(transactionDatabase.getAllTransactionTypes());
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField descriptionField = new TextField();
        TextField paymentField = new TextField();
        TextField depositField = new TextField();

        if (transaction != null) {
            // Editing existing transaction, pre-fill fields
            accountComboBox.setValue(transaction.getAccount());
            transactionTypeComboBox.setValue(transaction.getTransactionType());
            datePicker.setValue(transaction.getTransactionDate());
            descriptionField.setText(transaction.getTransactionDescription());
            paymentField.setText(transaction.getPaymentAmount() != null ? transaction.getPaymentAmount().toString() : "");
            depositField.setText(transaction.getDepositAmount() != null ? transaction.getDepositAmount().toString() : "");
        }

        addFormRow(grid, 0, "Account:", accountComboBox);
        addFormRow(grid, 1, "Transaction Type:", transactionTypeComboBox);
        addFormRow(grid, 2, "Transaction Date:", datePicker);
        addFormRow(grid, 3, "Description:", descriptionField);
        addFormRow(grid, 4, "Payment Amount:", paymentField);
        addFormRow(grid, 5, "Deposit Amount:", depositField);

        Button saveButton = createButton("Save", e -> {
            try {
                if (accountComboBox.getValue() == null || transactionTypeComboBox.getValue() == null ||
                        descriptionField.getText().trim().isEmpty() ||
                        (paymentField.getText().isEmpty() && depositField.getText().isEmpty())) {
                    showAlert("Error", "All fields must be filled in, including 'Description' and either 'Payment Amount' or 'Deposit Amount'.");
                    return;
                }

                double paymentAmount = paymentField.getText().isEmpty() ? 0.0 : Double.parseDouble(paymentField.getText());
                double depositAmount = depositField.getText().isEmpty() ? 0.0 : Double.parseDouble(depositField.getText());

                if (transaction == null) {
                    // New transaction
                    transactionDatabase.addTransaction(new TransactionBean(
                            accountComboBox.getValue(),
                            transactionTypeComboBox.getValue(),
                            datePicker.getValue(),
                            descriptionField.getText(),
                            paymentAmount,
                            depositAmount
                    ));
                    showAlert("Success", "Transaction saved successfully!");
                } else {
                    // Editing existing transaction
                    TransactionBean updatedTransaction = new TransactionBean(
                            transaction.getId(),
                            accountComboBox.getValue(),
                            transactionTypeComboBox.getValue(),
                            datePicker.getValue(),
                            descriptionField.getText(),
                            paymentAmount,
                            depositAmount
                    );
                    transactionDatabase.updateTransaction(transaction, updatedTransaction);
                    showAlert("Success", "Transaction updated successfully!");
                }

                refreshTransactionTableData();
                popupStage.close();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numeric values for payment and deposit amounts.");
            }
        });

        Button cancelButton = createButton("Cancel", e -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(15, grid, buttonBox);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    private VBox createScheduledTransactionForm(Stage popupStage, ScheduledTransactionBean scheduledTransaction) {
        GridPane grid = createFormGrid();

        TextField scheduleNameField = new TextField();
        ComboBox<String> accountComboBox = createComboBox(getAccountNames());
        ComboBox<String> transactionTypeComboBox = createComboBox(transactionDatabase.getAllTransactionTypes());
        ComboBox<String> frequencyComboBox = createComboBox(FXCollections.observableArrayList("Monthly"));
        TextField dueDateField = new TextField();
        TextField paymentAmountField = new TextField();

        dueDateField.setPromptText("1-31");

        if (scheduledTransaction != null) {
            // Editing existing scheduled transaction, pre-fill fields
            scheduleNameField.setText(scheduledTransaction.getScheduleName());
            accountComboBox.setValue(scheduledTransaction.getAccount());
            transactionTypeComboBox.setValue(scheduledTransaction.getTransactionType());
            frequencyComboBox.setValue(scheduledTransaction.getFrequency());
            dueDateField.setText(Integer.toString(scheduledTransaction.getDueDate()));
            paymentAmountField.setText(scheduledTransaction.getPaymentAmount().toString());
        }

        addFormRow(grid, 0, "Schedule Name:", scheduleNameField);
        addFormRow(grid, 1, "Account:", accountComboBox);
        addFormRow(grid, 2, "Transaction Type:", transactionTypeComboBox);
        addFormRow(grid, 3, "Frequency:", frequencyComboBox);
        addFormRow(grid, 4, "Due Date (Day of Month):", dueDateField);
        addFormRow(grid, 5, "Payment Amount:", paymentAmountField);

        Button saveButton = createButton("Save", e -> handleSaveScheduledTransaction(scheduleNameField, accountComboBox, transactionTypeComboBox, frequencyComboBox, dueDateField, paymentAmountField, popupStage, scheduledTransaction));

        Button cancelButton = createButton("Cancel", e -> popupStage.close());

        HBox buttonBox = new HBox(20, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox formLayout = new VBox(10, grid, buttonBox);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));

        return formLayout;
    }

    private void handleSaveScheduledTransaction(TextField scheduleNameField, ComboBox<String> accountComboBox, ComboBox<String> transactionTypeComboBox, ComboBox<String> frequencyComboBox, TextField dueDateField, TextField paymentAmountField, Stage popupStage, ScheduledTransactionBean originalScheduledTransaction) {

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
                if (originalScheduledTransaction != null) {
                    // Editing existing scheduled transaction
                    boolean scheduleNameChanged = !scheduleName.equals(originalScheduledTransaction.getScheduleName());
                    if (scheduleNameChanged && transactionDatabase.scheduledTransactionNameExists(scheduleName)) {
                        showAlert("Error", "A scheduled transaction with this name already exists.");
                        return;
                    }
                    transactionDatabase.updateScheduledTransaction(originalScheduledTransaction, scheduledTransaction);
                    showAlert("Success", "Scheduled transaction updated successfully!");
                } else {
                    // Adding new scheduled transaction
                    if (transactionDatabase.scheduledTransactionNameExists(scheduleName)) {
                        showAlert("Error", "A scheduled transaction with this name already exists.");
                        return;
                    }
                    transactionDatabase.addScheduledTransaction(scheduledTransaction);
                    showAlert("Success", "Scheduled transaction added successfully!");
                }
                refreshScheduledTransactionTableData();
                popupStage.close();
            } catch (SQLException e) {
                if (e.getMessage().contains("UNIQUE constraint failed")) {
                    showAlert("Duplicate Schedule Name", "A scheduled transaction with the name '" + scheduledTransaction.getScheduleName() + "' already exists. Please use a unique name.");
                } else {
                    e.printStackTrace();
                    showAlert("Error", "An unexpected error occurred. Please try again.");
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

    private void handleEditTransaction(TransactionBean transaction) {
        showTransactionForm(transaction);
    }

    private void handleEditScheduledTransaction(ScheduledTransactionBean scheduledTransaction) {
        showScheduledTransactionForm(scheduledTransaction);
    }

    private void filterTransactions(String searchText) {
        ObservableList<TransactionBean> allTransactions = transactionDatabase.getAllTransactions();
        if (searchText == null || searchText.isEmpty()) {
            transactionTableView.setItems(allTransactions);
        } else {
            ObservableList<TransactionBean> filteredTransactions = FXCollections.observableArrayList();
            for (TransactionBean transaction : allTransactions) {
                if (transaction.getTransactionDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredTransactions.add(transaction);
                }
            }
            transactionTableView.setItems(filteredTransactions);
        }
    }

    private void filterScheduledTransactions(String searchText) {
        ObservableList<ScheduledTransactionBean> allScheduledTransactions = transactionDatabase.getAllScheduledTransactions();
        if (searchText == null || searchText.isEmpty()) {
            scheduledTransactionTableView.setItems(allScheduledTransactions);
        } else {
            ObservableList<ScheduledTransactionBean> filteredScheduledTransactions = FXCollections.observableArrayList();
            for (ScheduledTransactionBean scheduledTransaction : allScheduledTransactions) {
                if (scheduledTransaction.getScheduleName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredScheduledTransactions.add(scheduledTransaction);
                }
            }
            scheduledTransactionTableView.setItems(filteredScheduledTransactions);
        }
    }

    // Helper methods to reduce code duplication

    private <T> void addDoubleClickEvent(TableView<T> tableView, java.util.function.Consumer<T> handler) {
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    T rowData = row.getItem();
                    handler.accept(rowData);
                }
            });
            return row;
        });
    }

    private <S> TableCell<S, LocalDate> createDateCell() {
        return new TableCell<S, LocalDate>() {
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
        };
    }

    private <S> TableCell<S, Double> createAmountCell() {
        return new TableCell<S, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item == 0.0) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
                setStyle("-fx-font-size: 16px;");
            }
        };
    }

    private <T> TableCell<T, Void> createDeleteButtonCell(java.util.function.Consumer<T> deleteHandler) {
        return new TableCell<T, Void>() {
            private final Button deleteButton = new Button();

            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("/application/icons/delete.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitHeight(20);
                deleteIconView.setFitWidth(20);
                deleteButton.setGraphic(deleteIconView);
                deleteButton.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
                deleteButton.setOnAction(event -> {
                    T item = getTableView().getItems().get(getIndex());
                    deleteHandler.accept(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.getStyleClass().add("popup-grid");
        return grid;
    }

    private void addFormRow(GridPane grid, int rowIndex, String labelText, Node field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("popup-label");
        if (field instanceof Control) {
            field.getStyleClass().add("popup-text-field");
        }
        grid.addRow(rowIndex, label, field);
    }

    private ComboBox<String> createComboBox(ObservableList<String> items) {
        ComboBox<String> comboBox = new ComboBox<>(items);
        comboBox.getSelectionModel().selectFirst();
        comboBox.getStyleClass().add("popup-text-field");
        return comboBox;
    }

    private Button createButton(String text, EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.getStyleClass().add("popup-button");
        button.setOnAction(handler);
        return button;
    }
}
