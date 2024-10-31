package application.controller;

import application.database.TransactionDatabase;
import application.database.AccountDatabase;
import application.model.TransactionBean;
import application.model.ScheduledTransactionBean;
import application.model.AccountBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDate;

public class TransactionController {

    private final TransactionDatabase transactionDatabase = new TransactionDatabase();
    private final AccountDatabase accountDatabase = new AccountDatabase(); // To get account names

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
    private Button addTransactionButton;

    @FXML
    private Button addScheduledTransactionButton;

    @FXML
    private Button clearTransactionsButton;

    @FXML
    private Button clearScheduledTransactionsButton;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        setupTransactionTableView();
        setupScheduledTransactionTableView();
        refreshTransactionTableData();
        refreshScheduledTransactionTableData();
    }

    /**
     * Sets up the Transaction TableView with columns for displaying transaction information.
     */
    private void setupTransactionTableView() {
        accountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountCol.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Format payment and deposit amounts to two decimal places
        paymentAmountCol.setCellFactory(column -> new TableCell<TransactionBean, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });

        depositAmountCol.setCellFactory(column -> new TableCell<TransactionBean, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
    }

    /**
     * Sets up the Scheduled Transaction TableView with columns for displaying scheduled transaction information.
     */
    private void setupScheduledTransactionTableView() {
        scheduleNameCol.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        scheduledAccountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        scheduledTransactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        scheduledPaymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        // Format payment amount to two decimal places
        scheduledPaymentAmountCol.setCellFactory(column -> new TableCell<ScheduledTransactionBean, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
    }

    /**
     * Refreshes the Transaction TableView with the latest data from the database.
     */
    private void refreshTransactionTableData() {
        ObservableList<TransactionBean> transactions = transactionDatabase.getAllTransactions();
        transactionTableView.setItems(transactions);
        transactionTableView.refresh();
        System.out.println("Transaction TableView refreshed with " + transactions.size() + " transactions.");
    }

    /**
     * Refreshes the Scheduled Transaction TableView with the latest data from the database.
     */
    private void refreshScheduledTransactionTableData() {
        ObservableList<ScheduledTransactionBean> scheduledTransactions = transactionDatabase.getAllScheduledTransactions();
        scheduledTransactionTableView.setItems(scheduledTransactions);
        scheduledTransactionTableView.refresh();
        System.out.println("Scheduled Transaction TableView refreshed with " + scheduledTransactions.size() + " scheduled transactions.");
    }

    /**
     * Handles adding a new transaction.
     */
    @FXML
    private void handleAddTransaction() {
        Stage popupStage = new Stage();
        popupStage.setResizable(false);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Transaction");
        VBox form = createTransactionForm(popupStage);
        Scene popupScene = new Scene(form, 500, 400);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * Handles adding a new scheduled transaction.
     */
    @FXML
    private void handleAddScheduledTransaction() {
        Stage popupStage = new Stage();
        popupStage.setResizable(false);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Scheduled Transaction");
        VBox form = createScheduledTransactionForm(popupStage);
        Scene popupScene = new Scene(form, 500, 400);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * Creates a form layout for adding a new transaction.
     *
     * @param popupStage The pop-up stage to close after saving.
     * @return A VBox containing the form fields and buttons.
     */
    private VBox createTransactionForm(Stage popupStage) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        Label accountLabel = new Label("Account:");
        ComboBox<String> accountComboBox = new ComboBox<>();
        accountComboBox.getItems().addAll(getAccountNames());
        accountComboBox.getSelectionModel().selectFirst();

        Label transactionTypeLabel = new Label("Transaction Type:");
        ComboBox<String> transactionTypeComboBox = new ComboBox<>();
        // Assuming you have a method to get transaction types
        transactionTypeComboBox.getItems().addAll(getTransactionTypes());
        transactionTypeComboBox.getSelectionModel().selectFirst();

        Label dateLabel = new Label("Transaction Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label descriptionLabel = new Label("Transaction Description:");
        TextField descriptionField = new TextField();

        Label paymentAmountLabel = new Label("Payment Amount:");
        TextField paymentAmountField = new TextField();

        Label depositAmountLabel = new Label("Deposit Amount:");
        TextField depositAmountField = new TextField();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSaveTransaction(accountComboBox, transactionTypeComboBox, datePicker,
                descriptionField, paymentAmountField, depositAmountField, popupStage));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttonBox = new HBox(20, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        grid.addRow(0, accountLabel, accountComboBox);
        grid.addRow(1, transactionTypeLabel, transactionTypeComboBox);
        grid.addRow(2, dateLabel, datePicker);
        grid.addRow(3, descriptionLabel, descriptionField);
        grid.addRow(4, paymentAmountLabel, paymentAmountField);
        grid.addRow(5, depositAmountLabel, depositAmountField);

        VBox vbox = new VBox(10, grid, buttonBox);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    /**
     * Handles saving a new transaction.
     */
    private void handleSaveTransaction(ComboBox<String> accountComboBox, ComboBox<String> transactionTypeComboBox,
                                       DatePicker datePicker, TextField descriptionField,
                                       TextField paymentAmountField, TextField depositAmountField, Stage popupStage) {

        String account = accountComboBox.getValue();
        String transactionType = transactionTypeComboBox.getValue();
        LocalDate transactionDate = datePicker.getValue();
        String description = descriptionField.getText().trim();
        String paymentText = paymentAmountField.getText().trim();
        String depositText = depositAmountField.getText().trim();

        if (account == null || transactionType == null || transactionDate == null ||
                description.isEmpty() || (paymentText.isEmpty() && depositText.isEmpty())) {
            showAlert("Error", "All required fields must be filled.");
            return;
        }

        Double paymentAmount = null;
        Double depositAmount = null;

        try {
            if (!paymentText.isEmpty()) {
                paymentAmount = Double.parseDouble(paymentText);
            }
            if (!depositText.isEmpty()) {
                depositAmount = Double.parseDouble(depositText);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Payment and Deposit amounts must be valid numbers.");
            return;
        }

        TransactionBean transaction = new TransactionBean(account, transactionType, transactionDate,
                description, paymentAmount, depositAmount);
        transactionDatabase.addTransaction(transaction);
        refreshTransactionTableData();
        showAlert("Success", "Transaction added successfully!");
        popupStage.close();
    }

    /**
     * Creates a form layout for adding a new scheduled transaction.
     *
     * @param popupStage The pop-up stage to close after saving.
     * @return A VBox containing the form fields and buttons.
     */
    private VBox createScheduledTransactionForm(Stage popupStage) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        Label scheduleNameLabel = new Label("Schedule Name:");
        TextField scheduleNameField = new TextField();

        Label accountLabel = new Label("Account:");
        ComboBox<String> accountComboBox = new ComboBox<>();
        accountComboBox.getItems().addAll(getAccountNames());
        accountComboBox.getSelectionModel().selectFirst();

        Label transactionTypeLabel = new Label("Transaction Type:");
        ComboBox<String> transactionTypeComboBox = new ComboBox<>();
        transactionTypeComboBox.getItems().addAll(getTransactionTypes());
        transactionTypeComboBox.getSelectionModel().selectFirst();

        Label frequencyLabel = new Label("Frequency:");
        ComboBox<String> frequencyComboBox = new ComboBox<>();
        frequencyComboBox.getItems().add("Monthly"); // Hard-coded as per specification
        frequencyComboBox.getSelectionModel().selectFirst();

        Label dueDateLabel = new Label("Due Date (Day of Month):");
        TextField dueDateField = new TextField();

        Label paymentAmountLabel = new Label("Payment Amount:");
        TextField paymentAmountField = new TextField();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSaveScheduledTransaction(scheduleNameField, accountComboBox,
                transactionTypeComboBox, frequencyComboBox, dueDateField, paymentAmountField, popupStage));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttonBox = new HBox(20, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        grid.addRow(0, scheduleNameLabel, scheduleNameField);
        grid.addRow(1, accountLabel, accountComboBox);
        grid.addRow(2, transactionTypeLabel, transactionTypeComboBox);
        grid.addRow(3, frequencyLabel, frequencyComboBox);
        grid.addRow(4, dueDateLabel, dueDateField);
        grid.addRow(5, paymentAmountLabel, paymentAmountField);

        VBox vbox = new VBox(10, grid, buttonBox);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    /**
     * Handles saving a new scheduled transaction.
     */
    private void handleSaveScheduledTransaction(TextField scheduleNameField, ComboBox<String> accountComboBox,
                                                ComboBox<String> transactionTypeComboBox, ComboBox<String> frequencyComboBox,
                                                TextField dueDateField, TextField paymentAmountField, Stage popupStage) {

        String scheduleName = scheduleNameField.getText().trim();
        String account = accountComboBox.getValue();
        String transactionType = transactionTypeComboBox.getValue();
        String frequency = frequencyComboBox.getValue();
        String dueDateText = dueDateField.getText().trim();
        String paymentText = paymentAmountField.getText().trim();

        if (scheduleName.isEmpty() || account == null || transactionType == null || frequency == null ||
                dueDateText.isEmpty() || paymentText.isEmpty()) {
            showAlert("Error", "All required fields must be filled.");
            return;
        }

        if (transactionDatabase.scheduledTransactionNameExists(scheduleName)) {
            showAlert("Error", "A scheduled transaction with this name already exists.");
            return;
        }

        int dueDate;
        Double paymentAmount;

        try {
            dueDate = Integer.parseInt(dueDateText);
            paymentAmount = Double.parseDouble(paymentText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Due Date and Payment Amount must be valid numbers.");
            return;
        }

        ScheduledTransactionBean scheduledTransaction = new ScheduledTransactionBean(scheduleName, account,
                transactionType, frequency, dueDate, paymentAmount);
        transactionDatabase.addScheduledTransaction(scheduledTransaction);
        refreshScheduledTransactionTableData();
        showAlert("Success", "Scheduled transaction added successfully!");
        popupStage.close();
    }

    /**
     * Handles clearing all transactions.
     */
    @FXML
    private void handleClearTransactions() {
        transactionDatabase.clearAllTransactions();
        refreshTransactionTableData();
        showAlert("Success", "All transactions have been cleared.");
    }

    /**
     * Handles clearing all scheduled transactions.
     */
    @FXML
    private void handleClearScheduledTransactions() {
        transactionDatabase.clearAllScheduledTransactions();
        refreshScheduledTransactionTableData();
        showAlert("Success", "All scheduled transactions have been cleared.");
    }

    /**
     * Retrieves a list of account names from the AccountDatabase.
     */
    private ObservableList<String> getAccountNames() {
        ObservableList<AccountBean> accounts = accountDatabase.getAllAccounts();
        ObservableList<String> accountNames = FXCollections.observableArrayList();
        for (AccountBean account : accounts) {
            accountNames.add(account.getName());
        }
        return accountNames;
    }

    /**
     * Retrieves a list of transaction types.
     * For simplicity, returns a hard-coded list as transaction types are not defined elsewhere.
     */
    private ObservableList<String> getTransactionTypes() {
        // Replace with actual data retrieval if transaction types are stored in the database
        return FXCollections.observableArrayList("House", "Car", "Kids", "Education");
    }

    /**
     * Displays an alert dialog with the given title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
