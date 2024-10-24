package application.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The AccountController class manages the Accounts page of the Budget Tracker 
 * application, including displaying accounts and handling the 'Add Account' pop-up form.
 */
public class AccountController {

    /** The AnchorPane that contains the account information display. */
    @FXML
    private AnchorPane accountBox;

    /**
     * Displays a pop-up window for adding a new account.
     * The pop-up includes a form for entering account details.
     */
    public void showAccountPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Account");

        GridPane grid = createAccountForm();
        Scene popupScene = new Scene(grid, 400, 225);
        popupScene.getStylesheets().add(getClass().getResource("/css/account.css").toExternalForm());
        popupStage.setResizable(false);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * Hides the Accounts page. (Currently not implemented.)
     */
    public void hide() {
        // Future implementation can go here
    }

    /**
     * Creates the form layout for the 'Add New Account' pop-up.
     *
     * @return A {@link GridPane} containing the form fields for adding a new account.
     */
    public GridPane createAccountForm() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("popup-grid");

        Label nameLabel = new Label("Account Name:");
        nameLabel.getStyleClass().add("popup-label");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter account name");
        nameField.getStyleClass().add("popup-text-field");

        Label dateLabel = new Label("Opening Date:");
        dateLabel.getStyleClass().add("popup-label");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("popup-date-picker");

        Label balanceLabel = new Label("Opening Balance:");
        balanceLabel.getStyleClass().add("popup-label");

        TextField balanceField = new TextField();
        balanceField.setPromptText("Enter opening balance");
        balanceField.getStyleClass().add("popup-text-field");

        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("popup-button");
        saveButton.setOnAction(e -> handleSave(nameField, balanceField));

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("popup-button");
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        HBox buttonBox = new HBox(10, saveButton, cancelButton);

        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, dateLabel, datePicker);
        grid.addRow(2, balanceLabel, balanceField);
        grid.add(buttonBox, 1, 3);

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
