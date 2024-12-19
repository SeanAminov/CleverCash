package application.controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private AnchorPane mainBox;

    private Object currentController; // Keep track of the current controller
    private static MainController instance; // Static instance of MainController
    private HomeController homeController; // Reference to HomeController

    @FXML
    public void initialize() {
        instance = this; // Set static instance
        showHomePage(); // Load the home page by default
    }

    public static MainController getInstance() {
        return instance;
    }

    public HomeController getHomeController() {
        return homeController;
    }

    @FXML
    public void closeApp() {
        Platform.exit();
    }

    @FXML
    public void showHomePage() {
        loadPage("/view/Home.fxml");
    }

    @FXML
    public void showAccountPage() {
        loadPage("/view/Accounts.fxml");
    }

    @FXML
    public void showTransactionTypePage() {
        loadPage("/view/TransactionType.fxml");
    }

    @FXML
    public void showTransactionPage() {
        loadPage("/view/Transactions.fxml");
    }

    @FXML
    public void showReportPage() {
        loadPage("/view/Reports.fxml");
    }

    @FXML
    public void showSettingPage() {
        loadPage("/view/Settings.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            // Clear temporary labels if the current controller has them
            if (currentController instanceof HomeController) {
                ((HomeController) currentController).clearTemporaryLabels();
            }

            // Load the new page
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane page = loader.load();

            // Update the current controller reference
            currentController = loader.getController();

            // If the loaded controller is HomeController, store the reference
            if (currentController instanceof HomeController) {
                homeController = (HomeController) currentController;
            }

            // Set the new page in the mainBox
            mainBox.getChildren().clear();
            mainBox.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the page.", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
