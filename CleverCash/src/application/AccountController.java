package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountController extends Stage {

    private Set<String> accountNames = new HashSet<>();

    public void start(Stage stage) {
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
        saveButton.setOnAction(e -> {
            if (validateInput(nameField, balanceField)) {
                String name = nameField.getText();
                LocalDate date = datePicker.getValue();
                double balance = Double.parseDouble(balanceField.getText());

                if (accountNames.contains(name)) {
                    showAlert("Error", "Account name already exists.");
                } else {
                    Account account = new Account(name, date, balance);
                    accountNames.add(name);
                    showAlert("Success", "Account created successfully.");
                    stage.close(); // Close the account creation window
                }
            }
        });

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(balanceLabel, 0, 2);
        grid.add(balanceField, 1, 2);
        grid.add(saveButton, 1, 3);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Create New Account");
        stage.show();
    }

    private boolean validateInput(TextField nameField, TextField balanceField) {
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
