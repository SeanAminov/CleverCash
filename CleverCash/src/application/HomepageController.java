package application;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomepageController extends Application {

    private final static String title = "Budget Tracker Application";
    private BorderPane root; // Use this as the main layout

    private static Button createButton(String name, String iconPath, double prefW, double prefH) {
        Button button = new Button();

        ImageView icon = new ImageView(new Image(HomepageController.class.getResourceAsStream(iconPath)));
        icon.setFitWidth(30);
        icon.setFitHeight(30);

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(icon, new Label(name));
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        button.setGraphic(hbox);
        button.setPrefWidth(prefW);
        button.setPrefHeight(prefH);
        button.setBackground(new Background(new BackgroundFill(Color.web("#AAAAAA"), CornerRadii.EMPTY, null)));

        return button;
    }

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane(); // Main layout

        // Create Buttons with Icons
        Button home = createButton("Home", "/application/resources/home.png", 300, 50);
        Button accounts = createButton("Accounts", "/application/resources/accounts.png", 300, 50);
        Button transactions = createButton("Transactions", "/application/resources/transactions.png", 300, 50);
        Button reports = createButton("Reports", "/application/resources/reports.png", 300, 50);
        Button settings = createButton("Settings", "/application/resources/settings.png", 300, 50);

        // Set action to switch content to the Accounts page
        accounts.setOnAction(e -> showAccountsPage());

        // Search Box with Icon
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(250);
        searchField.setPrefHeight(50);

        ImageView searchIcon = new ImageView(new Image(getClass().getResourceAsStream("/application/resources/search.png")));
        searchIcon.setFitWidth(30);
        searchIcon.setFitHeight(30);

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchIcon, searchField);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 0, 10, 0));

        // VBox for Buttons
        VBox vbox = new VBox(20);
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(searchBox, home, accounts, transactions, reports, settings);
        vbox.setBackground(new Background(new BackgroundFill(Color.web("#AAAAAA"), CornerRadii.EMPTY, null)));

        root.setLeft(vbox); // Set the button menu on the left

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();
    }

    // Method to switch the content to the Accounts page
    private void showAccountsPage() {
        VBox accountsLayout = new VBox(20);
        accountsLayout.setPadding(new Insets(20));
        accountsLayout.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        Button addAccountButton = new Button("Add Account");
        addAccountButton.setMinWidth(250); // Increased minimum width to prevent truncation
        addAccountButton.setPrefSize(250, 50); // Increased preferred size
        addAccountButton.setOnAction(e -> showAddAccountPopup()); // Opens the pop-up

        accountsLayout.getChildren().add(addAccountButton); // Add the button to the layout

        root.setCenter(accountsLayout); // Replace the center of the root with the accounts page
    }

    // Method to show the Add Account form in a pop-up window
    private void showAddAccountPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        popupStage.setTitle("Add New Account");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label nameLabel = new Label("Account Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter account name");

        Label dateLabel = new Label("Opening Date:");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now()); // Set today's date by default

        Label balanceLabel = new Label("Opening Balance:");
        TextField balanceField = new TextField();
        balanceField.setPromptText("Enter opening balance");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (validateInput(nameField, balanceField)) {
                showAlert("Success", "Account created successfully!");
                popupStage.close(); // Close the pop-up after saving
            }
        });

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1); // Add the DatePicker to the grid
        grid.add(balanceLabel, 0, 2);
        grid.add(balanceField, 1, 2);
        grid.add(saveButton, 1, 3);

        Scene popupScene = new Scene(grid, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }


    // Validation method to ensure input is correct
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

    // Utility method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
