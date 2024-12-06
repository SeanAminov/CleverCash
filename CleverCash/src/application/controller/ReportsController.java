package application.controller;

import application.database.AccountDatabase;
import application.database.TransactionDatabase;
import application.model.TransactionBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The ReportsController handles the "Report of Transactions by an Account" and 
 * "Report of Transactions by a Transaction Type".
 * It includes two ComboBoxes for selecting the Account and Transaction Type,
 * and two TableViews for displaying the filtered transactions.
 */
public class ReportsController {

    @FXML
    private ComboBox<String> accountComboBox;

    @FXML
    private ComboBox<String> transactionTypeComboBox;

    @FXML
    private TableView<TransactionBean> accountTransactionsTable;

    @FXML
    private TableColumn<TransactionBean, String> accTransAccountCol;

    @FXML
    private TableColumn<TransactionBean, LocalDate> accTransDateCol;

    @FXML
    private TableColumn<TransactionBean, String> accTransDescriptionCol;

    @FXML
    private TableColumn<TransactionBean, Double> accPaymentAmountCol;

    @FXML
    private TableColumn<TransactionBean, Double> accDepositAmountCol;

    @FXML
    private TableView<TransactionBean> typeTransactionsTable;

    @FXML
    private TableColumn<TransactionBean, String> typeTransTypeCol;

    @FXML
    private TableColumn<TransactionBean, LocalDate> typeTransDateCol;

    @FXML
    private TableColumn<TransactionBean, String> typeTransDescriptionCol;

    @FXML
    private TableColumn<TransactionBean, Double> typePaymentAmountCol;

    @FXML
    private TableColumn<TransactionBean, Double> typeDepositAmountCol;

    private final AccountDatabase accountDatabase = new AccountDatabase();
    private final TransactionDatabase transactionDatabase = new TransactionDatabase();

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    public void initialize() {
        // Populate ComboBoxes with data
        populateComboBoxes();

        // Initialize TableViews
        initializeAccountTransactionsTable();
        initializeTypeTransactionsTable();

        // Add listeners to ComboBoxes to load data upon selection
        accountComboBox.valueProperty().addListener((obs, oldVal, newVal) -> loadAccountTransactions(newVal));
        transactionTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> loadTypeTransactions(newVal));

        // Add double-click event handlers to TableViews
        addDoubleClickEvent(accountTransactionsTable, this::showTransactionDetailsPopup);
        addDoubleClickEvent(typeTransactionsTable, this::showTransactionDetailsPopup);
    }

    /**
     * Populates the Account and Transaction Type ComboBoxes with data from the databases.
     */
    private void populateComboBoxes() {
        // Populate Account ComboBox
        ObservableList<String> accountNames = FXCollections.observableArrayList(
                accountDatabase.getAllAccounts().stream()
                        .map(account -> account.getName())
                        .toList()
        );
        accountComboBox.setItems(accountNames);
        accountComboBox.setPromptText("Select Account");

        // Populate Transaction Type ComboBox
        ObservableList<String> transactionTypes = transactionDatabase.getAllTransactionTypes();
        transactionTypeComboBox.setItems(transactionTypes);
        transactionTypeComboBox.setPromptText("Select Transaction Type");
    }

    /**
     * Initializes the Account Transactions TableView with appropriate columns.
     */
    private void initializeAccountTransactionsTable() {
        // Set up cell value factories using PropertyValueFactory
        accTransAccountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        accTransDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        accTransDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        accPaymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        accDepositAmountCol.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Optional: Format the date and amounts
        accTransDateCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });

        accPaymentAmountCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null || amount == 0.0) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        accDepositAmountCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null || amount == 0.0) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        // Initialize with empty data
        accountTransactionsTable.setItems(FXCollections.observableArrayList());
    }

    /**
     * Initializes the Transaction Type Transactions TableView with appropriate columns.
     */
    private void initializeTypeTransactionsTable() {
        // Set up cell value factories using PropertyValueFactory
        typeTransTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        typeTransDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        typeTransDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        typePaymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        typeDepositAmountCol.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Optional: Format the date and amounts
        typeTransDateCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });

        typePaymentAmountCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null || amount == 0.0) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        typeDepositAmountCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null || amount == 0.0) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        // Initialize with empty data
        typeTransactionsTable.setItems(FXCollections.observableArrayList());
    }

    /**
     * Loads and displays transactions associated with the selected account.
     *
     * @param accountName The name of the selected account.
     */
    private void loadAccountTransactions(String accountName) {
        if (accountName == null || accountName.isEmpty()) {
            accountTransactionsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        // Fetch all transactions for the selected account
        ObservableList<TransactionBean> allTransactions = transactionDatabase.getAllTransactions();
        ObservableList<TransactionBean> filteredTransactions = FXCollections.observableArrayList(
                allTransactions.stream()
                        .filter(transaction -> transaction.getAccount().equals(accountName))
                        .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                        .toList()
        );

        accountTransactionsTable.setItems(filteredTransactions);
    }

    /**
     * Loads and displays transactions associated with the selected transaction type.
     *
     * @param transactionType The selected transaction type.
     */
    private void loadTypeTransactions(String transactionType) {
        if (transactionType == null || transactionType.isEmpty()) {
            typeTransactionsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        // Fetch all transactions for the selected transaction type
        ObservableList<TransactionBean> allTransactions = transactionDatabase.getAllTransactions();
        ObservableList<TransactionBean> filteredTransactions = FXCollections.observableArrayList(
                allTransactions.stream()
                        .filter(transaction -> transaction.getTransactionType().equals(transactionType))
                        .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                        .toList()
        );

        typeTransactionsTable.setItems(filteredTransactions);
    }

    /**
     * Adds a double-click event handler to a TableView to show transaction details in a popup.
     *
     * @param tableView The TableView to add the event to.
     * @param handler   The handler to execute on double-click.
     * @param <T>       The type of the items in the TableView.
     */
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

    /**
     * Shows a read-only popup window with detailed information about the selected transaction.
     *
     * @param transaction The transaction to display details for.
     */
    private void showTransactionDetailsPopup(TransactionBean transaction) {
        // Create a new stage for the popup
        Stage detailStage = new Stage();
        detailStage.initOwner(accountTransactionsTable.getScene().getWindow());
        detailStage.setTitle("Transaction Details (Read-Only)");

        // Create a GridPane layout for the details
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        // Create and configure labels and text fields
        Label accountLabel = new Label("Account:");
        TextField accountField = new TextField(transaction.getAccount());
        accountField.setEditable(false);

        Label typeLabel = new Label("Transaction Type:");
        TextField typeField = new TextField(transaction.getTransactionType());
        typeField.setEditable(false);

        Label dateLabel = new Label("Date:");
        TextField dateField = new TextField(dateFormatter.format(transaction.getTransactionDate()));
        dateField.setEditable(false);

        Label descLabel = new Label("Description:");
        TextField descField = new TextField(transaction.getTransactionDescription());
        descField.setEditable(false);

        Label paymentLabel = new Label("Payment Amount:");
        TextField paymentField = new TextField(
                transaction.getPaymentAmount() == null ? "" : String.format("%.2f", transaction.getPaymentAmount()));
        paymentField.setEditable(false);

        Label depositLabel = new Label("Deposit Amount:");
        TextField depositField = new TextField(
                transaction.getDepositAmount() == null ? "" : String.format("%.2f", transaction.getDepositAmount()));
        depositField.setEditable(false);

        // Add all components to the grid
        grid.add(accountLabel, 0, 0);
        grid.add(accountField, 1, 0);
        grid.add(typeLabel, 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(dateLabel, 0, 2);
        grid.add(dateField, 1, 2);
        grid.add(descLabel, 0, 3);
        grid.add(descField, 1, 3);
        grid.add(paymentLabel, 0, 4);
        grid.add(paymentField, 1, 4);
        grid.add(depositLabel, 0, 5);
        grid.add(depositField, 1, 5);

        // Create a Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> detailStage.close());

        // Arrange components in a VBox
        VBox vbox = new VBox(10, grid, backButton);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setPadding(new javafx.geometry.Insets(20));

        // Set the scene and show the popup
        Scene scene = new Scene(vbox, 400, 400);
        detailStage.setScene(scene);
        detailStage.showAndWait();
    }
}
