package application.controller;

import application.database.AccountDatabase;
import application.model.AccountBean;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The AccountController manages the Accounts page and displays stored accounts
 * using a TableView, allowing users to view, add, and delete accounts individually.
 */
public class AccountController {

    private final AccountDatabase accountDatabase = new AccountDatabase();

    @FXML
    private AnchorPane accountBox;

    @FXML
    private Button addAccountButton;

    @FXML
    private TableView<AccountBean> tableView;

    @FXML
    private TableColumn<AccountBean, String> nameCol;

    @FXML
    private TableColumn<AccountBean, LocalDate> dateCol;

    @FXML
    private TableColumn<AccountBean, Double> balanceCol;

    @FXML
    private TableColumn<AccountBean, Void> deleteCol;

    // Define a DateTimeFormatter with the desired pattern
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @FXML
    public void initialize() {
        setupAccountTableView();
        refreshTableData();
        applyFontScaling(tableView);  // Apply font scaling to the account table
    }

    // Method to apply font scaling to a TableView
    private void applyFontScaling(TableView<?> tableView) {
        tableView.setStyle("-fx-font-size: 16px;");
    }

    private void setupAccountTableView() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("openingDate"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("openingBalance"));

        // Set font size for each cell in nameCol, dateCol, and balanceCol
        nameCol.setCellFactory(column -> createStyledCellForString());
        dateCol.setCellFactory(column -> createStyledCellForDate());
        balanceCol.setCellFactory(column -> createStyledCellForDouble());

        // Set up delete button with an icon in each row of deleteCol
        deleteCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();

            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("/application/icons/delete.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitHeight(40); // 2.5 times larger
                deleteIconView.setFitWidth(40);  // 2.5 times larger
                deleteButton.setGraphic(deleteIconView);

                deleteButton.setOnAction(event -> {
                    AccountBean account = getTableView().getItems().get(getIndex());
                    handleDeleteAccount(account);
                });

                deleteButton.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    setStyle("-fx-alignment: CENTER;"); // Center the content of the cell
                }
            }
        });
    }

    private TableCell<AccountBean, String> createStyledCellForString() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
                setStyle("-fx-font-size: 32px;");
            }
        };
    }

    private TableCell<AccountBean, LocalDate> createStyledCellForDate() {
        return new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Format the LocalDate to "MMM. dd, yyyy"
                    setText(item.format(dateFormatter));
                }
                setStyle("-fx-font-size: 32px;");
            }
        };
    }

    private TableCell<AccountBean, Double> createStyledCellForDouble() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
                setStyle("-fx-font-size: 32px;");
            }
        };
    }

    private void refreshTableData() {
        ObservableList<AccountBean> accounts = accountDatabase.getAllAccounts();
        tableView.setItems(accounts);
        tableView.refresh();
        System.out.println("TableView refreshed with " + accounts.size() + " accounts.");
    }

    private void handleDeleteAccount(AccountBean account) {
        boolean confirmation = showConfirmation("Confirm Deletion", "Are you sure you want to delete the account: " + account.getName() + "?");
        if (confirmation) {
            accountDatabase.deleteAccount(account.getName());
            refreshTableData();
            showAlert("Success", "Account deleted successfully!");
        }
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showAccountPopup() {
        Stage popupStage = new Stage();
        popupStage.setResizable(false);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add New Account");

        // Always create a new VBox instance each time the popup is shown
        VBox form = createAccountForm(popupStage);

        Scene popupScene = new Scene(form, 400, 300);
        popupScene.getStylesheets().add(getClass().getResource("/css/account.css").toExternalForm());

        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private VBox createAccountForm(Stage popupStage) {
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

        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, dateLabel, datePicker);
        grid.addRow(2, balanceLabel, balanceField);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSave(nameField, datePicker, balanceField, popupStage));
        saveButton.getStyleClass().add("popup-button");

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());
        cancelButton.getStyleClass().add("popup-button");

        HBox buttonBox = new HBox(20, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        VBox vbox = new VBox(10, grid, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        return vbox;
    }

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
            refreshTableData();
            showAlert("Success", "Account added successfully!");
            popupStage.close();
        } catch (NumberFormatException e) {
            showAlert("Error", "Opening balance must be a valid number.");
        }
    }
}
