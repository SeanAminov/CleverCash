package application.controller;

import application.database.AccountDatabase;
import application.model.AccountBean;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDate;

/**
 * The AccountController manages the Accounts page and displays stored accounts
 * using a TableView, allowing users to view, add, and delete accounts.
 */
public class AccountController {

    private final AccountDatabase accountDatabase = new AccountDatabase(); // Database instance

    @FXML
    private AnchorPane accountBox; // Root layout from FXML

    @FXML
    private Button addAccountButton;

    @FXML
    private Button deleteAllButton;

    @FXML
    private TableView<AccountBean> tableView;

    @FXML
    private TableColumn<AccountBean, String> nameCol;

    @FXML
    private TableColumn<AccountBean, LocalDate> dateCol;

    @FXML
    private TableColumn<AccountBean, Double> balanceCol;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        setupAccountTableView();
        refreshTableData();
    }

    /**
     * Sets up the TableView with columns for displaying account information.
     */
    private void setupAccountTableView() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("openingDate"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("openingBalance"));

        // Format the balance to two decimal places
        balanceCol.setCellFactory(column -> new TableCell<AccountBean, Double>() {
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
     * Refreshes the TableView with the latest account data from the database.
     */
    private void refreshTableData() {
        ObservableList<AccountBean> accounts = accountDatabase.getAllAccounts();
        tableView.setItems(accounts);
        tableView.refresh(); // Ensure the table view gets updated
        System.out.println("TableView refreshed with " + accounts.size() + " accounts.");
    }

    /**
     * Handles clearing all accounts from the database and refreshing the table.
     * This method now only deletes the account records without deleting the database file.
     */
    @FXML
    private void handleDeleteAllAccounts() {
        accountDatabase.clearAllAccounts();
        refreshTableData(); // Refresh the TableView after clearing
        showAlert("Success", "All accounts have been cleared.");
    }

    /**
     * Displays a pop-up form to add a new account.
     */
    @FXML
    private void showAccountPopup() {
        Stage popupStage = new Stage();
        popupStage.setResizable(false);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Account");
        VBox form = createAccountForm(popupStage);
        Scene popupScene = new Scene(form, 400, 300);
        popupScene.getStylesheets().add(getClass().getResource("/css/account.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * Creates a form layout for adding a new account.
     *
     * @param popupStage The pop-up stage to close after saving.
     * @return A VBox containing the form fields and buttons.
     */
    private VBox createAccountForm(Stage popupStage) {
        // Create the GridPane for the form
        GridPane grid = new GridPane();
        grid.getStyleClass().add("popup-grid");

        Label nameLabel = new Label("Account Name:");
        nameLabel.getStyleClass().add("popup-label");
        TextField nameField = new TextField();
        nameField.getStyleClass().add("popup-text-field");
        nameField.setPromptText("Enter account name");

        Label dateLabel = new Label("Opening Date:");
        dateLabel.getStyleClass().add("popup-label");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("popup-date-picker");

        Label balanceLabel = new Label("Opening Balance:");
        balanceLabel.getStyleClass().add("popup-label");
        TextField balanceField = new TextField();
        balanceField.getStyleClass().add("popup-text-field");
        balanceField.setPromptText("Enter opening balance");

        // Add elements to the GridPane
        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, dateLabel, datePicker);
        grid.addRow(2, balanceLabel, balanceField);

        // Create the Save and Cancel buttons
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("popup-button");
        saveButton.setOnAction(e -> handleSave(nameField, datePicker, balanceField, popupStage));

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("popup-button");
        cancelButton.setOnAction(e -> popupStage.close());

        // Create an HBox to center the buttons
        HBox buttonBox = new HBox(20, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Add some space between the form and the buttons

        // Use a VBox to position the form and buttons
        VBox vbox = new VBox(10, grid, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        return vbox;
    }

    /**
     * Handles the save operation when a new account is added.
     *
     * @param nameField    The field containing the account name.
     * @param datePicker   The DatePicker for selecting the opening date.
     * @param balanceField The field containing the opening balance.
     * @param popupStage   The pop-up stage to close after saving.
     */
    private void handleSave(TextField nameField, DatePicker datePicker, TextField balanceField, Stage popupStage) {
        String name = nameField.getText().trim();
        LocalDate date = datePicker.getValue();
        String balanceText = balanceField.getText().trim();

        if (name.isEmpty() || date == null || balanceText.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        if (accountDatabase.accountNameExists(name)) {
            showAlert("Error", "An account with this name already exists.");
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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
