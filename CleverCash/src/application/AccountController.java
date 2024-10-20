package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;

/**
 * The AccountController class manages the Accounts page of the Budget Tracker 
 * application, including displaying accounts and handling the 'Add Account' pop-up form.
 */
public class AccountController {

    /**
     * Displays the Accounts page within the provided {@link BorderPane} layout.
     *
     * @param root The BorderPane layout where the Accounts page will be displayed.
     */
    public void show(BorderPane root) {
        VBox accountsLayout = new VBox(20);
        accountsLayout.setPadding(new Insets(20));
        accountsLayout.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        Button addAccountButton = new Button("Add Account");
        addAccountButton.setPrefSize(250, 50);
        addAccountButton.setOnAction(e -> showPopUp());

        accountsLayout.getChildren().add(addAccountButton);
        root.setCenter(accountsLayout);
    }

    /**
     * Hides the Accounts page. (Currently not implemented.)
     */
    public void hide() {
        // Future implementation can go here
    }

    /**
     * Displays the 'Add New Account' pop-up window.
     */
    public void showPopUp() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Account");

        GridPane grid = createAccountForm();
        Scene popupScene = new Scene(grid, 300, 200);
        popupStage.setResizable(false);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * Creates the form layout for the 'Add New Account' pop-up.
     *
     * @return A {@link GridPane} containing the form fields for adding a new account.
     */
    private GridPane createAccountForm() {
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
        saveButton.setOnAction(e -> handleSave(nameField, balanceField));

        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, dateLabel, datePicker);
        grid.addRow(2, balanceLabel, balanceField);
        grid.addRow(3, new Label(), saveButton);

        return grid;
    }

    /**
     * Handles the save operation when the user submits the 'Add New Account' form.
     *
     * @param nameField    The {@link TextField} containing the account name.
     * @param balanceField The {@link TextField} containing the opening balance.
     */
    private void handleSave(TextField nameField, TextField balanceField) {
        if (isValidInput(nameField, balanceField)) {
            showAlert("Success", "Account created successfully!");
        }
    }

    /**
     * Validates the input fields of the 'Add New Account' form.
     *
     * @param nameField    The {@link TextField} containing the account name.
     * @param balanceField The {@link TextField} containing the opening balance.
     * @return {@code true} if both fields are valid, {@code false} otherwise.
     */
    private boolean isValidInput(TextField nameField, TextField balanceField) {
        if (nameField.getText().isEmpty() || balanceField.getText().isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return false;
        }
        try {
            Double.parseDouble(balanceField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Opening balance must be a valid number.");
            return false;
        }
        return true;
    }

    /**
     * Displays an alert dialog with the given title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to be displayed in the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
