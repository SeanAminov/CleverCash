package application.controller;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MainController {

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private AnchorPane mainBox;

    @FXML
    private ImageView menuIcon;

    private Object currentController; // Keep track of the current controller

    // Static instance of MainController
    private static MainController instance;

    // Reference to HomeController
    private HomeController homeController;

    @FXML
    public void initialize() {
        // Set the static instance
        instance = this;

        if (menuIcon != null) {
            menuIcon.setOnMouseClicked(event -> showMenuSlider());
        } else {
            System.out.println("menuIcon is null");
        }
        showHomePage(); // Load the home page by default
    }

    public static MainController getInstance() {
        return instance;
    }

    public HomeController getHomeController() {
        return homeController;
    }

    public AnchorPane getRootPane() {
        return mainRoot;
    }

    public void showMenuSlider() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuBar.fxml"));
            AnchorPane menuPane = loader.load();
            menuPane.setLayoutX(-menuPane.getPrefWidth());
            menuPane.setLayoutY(0);
            mainRoot.getChildren().add(menuPane);
            menuPane.toFront();

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), menuPane);
            slideIn.setToX(0);
            slideIn.play();

            MenuBarController menuBarController = loader.getController();
            menuBarController.setMainController(this);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the menu.", e.getMessage());
        }
    }

    public void hideMenuSlider(AnchorPane menuPane) {
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), menuPane);
        slideOut.setToX(-menuPane.getPrefWidth());
        slideOut.setOnFinished(event -> mainRoot.getChildren().remove(menuPane));
        slideOut.play();
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
