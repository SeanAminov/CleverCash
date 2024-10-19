package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AccountController {

    public void show() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Account");

        GridPane grid = createAccountForm();
        Scene popupScene = new Scene(grid, 300, 200);

        popupStage.setScene(popupScene);
        popupStage.show();
    }

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

    private void handleSave(TextField nameField, TextField balanceField) {
        if (isValidInput(nameField, balanceField)) {
            showAlert("Success", "Account created successfully!");
        }
    }

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
