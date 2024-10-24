package application.controller;

import application.database.AccountDatabase;
import application.model.AccountBean;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * The AccountController manages the Accounts page and displays stored accounts
 * using a TableView, allowing users to view, add, and delete accounts.
 */
public class AccountController {

    private final AccountDatabase accountDatabase = new AccountDatabase(); // Database instance
    private final TableView<AccountBean> tableView = new TableView<>(); // Initialize TableView

    /**
     * Displays the Accounts page with the "Add Account" and "Delete All" buttons
     * and the TableView below them.
     *
     * @param root The main layout of the application.
     */
    public void show(BorderPane root) {
        VBox accountsLayout = new VBox(10);
        accountsLayout.setPadding(new Insets(20));
        accountsLayout.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        HBox buttonBox = new HBox(10); // Layout for buttons
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Add Account button
        Button addAccountButton = new Button("Add Account");
        addAccountButton.setPrefSize(120, 40);
        addAccountButton.setOnAction(e -> showAccountPopup());

        // Delete All Accounts button
        Button deleteAllButton = new Button("Delete All Accounts");
        deleteAllButton.setPrefSize(160, 40);
        deleteAllButton.setOnAction(e -> handleDeleteAllAccounts());

        // Add both buttons to the HBox
        buttonBox.getChildren().addAll(addAccountButton, deleteAllButton);

        // Set up the TableView and refresh it with data
        setupAccountTableView();
        refreshTableData();

        // Ensure the TableView expands to fill the available space
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Add the buttons and TableView to the VBox
        accountsLayout.getChildren().addAll(buttonBox, tableView);

        // Set the VBox as the center content of the root layout
        root.setCenter(accountsLayout);
    }

    /**
     * Sets up the TableView with columns for displaying account information.
     */
    private void setupAccountTableView() {
        TableColumn<AccountBean, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<AccountBean, String> dateCol = new TableColumn<>("Opening Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("openingDate"));

        TableColumn<AccountBean, Double> balanceCol = new TableColumn<>("Opening Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("openingBalance"));

        tableView.getColumns().clear();
        tableView.getColumns().addAll(nameCol, dateCol, balanceCol);
    }

    /**
     * Refreshes the TableView with the latest account data from the database.
     */
    private void refreshTableData() {
        ObservableList<AccountBean> accounts = accountDatabase.getAllAccounts();
        tableView.setItems(accounts);
        tableView.refresh(); // Ensure the table view gets updated
        System.out.println("TableView refreshed with " + accounts.size() + " accounts.");
    }

    /**
     * Handles deleting all accounts from the database and refreshing the table.
     */
    private void handleDeleteAllAccounts() {
        accountDatabase.deleteAllAccounts();
        refreshTableData(); // Refresh the TableView after deletion
        showAlert("Success", "All accounts have been deleted.");
    }

    /**
     * Displays a pop-up form to add a new account.
     */
    public void showAccountPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Account");

        GridPane form = createAccountForm(popupStage);
        Scene popupScene = new Scene(form, 400, 300);

        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * Creates a form layout for adding a new account.
     *
     * @param popupStage The pop-up stage to close after saving.
     * @return A GridPane containing the form fields.
     */
    private GridPane createAccountForm(Stage popupStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label nameLabel = new Label("Account Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter account name");

        Label dateLabel = new Label("Opening Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label balanceLabel = new Label("Opening Balance:");
        TextField balanceField = new TextField();
        balanceField.setPromptText("Enter opening balance");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSave(nameField, datePicker, balanceField, popupStage));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, dateLabel, datePicker);
        grid.addRow(2, balanceLabel, balanceField);
        grid.add(buttonBox, 1, 3);

        return grid;
    }

    /**
     * Handles the save operation when a new account is added.
     *
     * @param nameField   The field containing the account name.
     * @param datePicker  The DatePicker for selecting the opening date.
     * @param balanceField The field containing the opening balance.
     * @param popupStage  The pop-up stage to close after saving.
     */
    private void handleSave(TextField nameField, DatePicker datePicker, TextField balanceField, Stage popupStage) {
        String name = nameField.getText();
        LocalDate date = datePicker.getValue();
        String balanceText = balanceField.getText();

        if (name.isEmpty() || date == null || balanceText.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        try {
            double balance = Double.parseDouble(balanceText);
            AccountBean newAccount = new AccountBean(name, date, balance);
            accountDatabase.addAccount(newAccount);
            refreshTableData(); // Refresh the TableView
            showAlert("Success", "Account added successfully!");
            popupStage.close(); // Close the pop-up window
        } catch (NumberFormatException e) {
            showAlert("Error", "Opening balance must be a valid number.");
        }
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
        alert.setContentText(message);
        alert.showAndWait();
    }
}
