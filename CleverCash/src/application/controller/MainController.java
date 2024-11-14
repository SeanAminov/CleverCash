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
    private AnchorPane mainRoot; // Main root container for stacking panes

    @FXML
    private AnchorPane mainBox; // Content box to load different pages

    @FXML
    private ImageView menuIcon; // Reference to the menu icon ImageView

    /**
     * Initializes the controller by setting up the menu icon click event and loading the home page.
     */
    @FXML
    public void initialize() {
        // Attach click event to menuIcon programmatically
        if (menuIcon != null) {
            menuIcon.setOnMouseClicked(event -> showMenuSlider());
        } else {
            System.out.println("menuIcon is null"); // For debugging
        }
        // Load the default home page
        showHomePage();
    }

    /**
     * Displays the sliding menu by loading the MenuBar.fxml file and animating it into view.
     */
    public void showMenuSlider() {
        System.out.println("showMenuSlider called"); // For debugging

        try {
            // Load the sliding menu pane from MenuBar.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuBar.fxml"));
            AnchorPane menuPane = loader.load();

            // Configure the menu pane to slide in from the left
            menuPane.setLayoutX(-menuPane.getPrefWidth());
            menuPane.setLayoutY(0);

            // Add the menu pane to the main root
            mainRoot.getChildren().add(menuPane);
            menuPane.toFront();

            // Animate the menu pane sliding in
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), menuPane);
            slideIn.setToX(0);
            slideIn.play();

            // Set up for the MenuBarController to use mainController's hideMenuSlider
            MenuBarController menuBarController = loader.getController();
            menuBarController.setMainController(this);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the menu.", e.getMessage());
        }
    }

    /**
     * Hides the sliding menu by animating it out of view and removing it from the main root pane.
     */
    public void hideMenuSlider(AnchorPane menuPane) {
        // Animate the menu sliding out of view
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), menuPane);
        slideOut.setToX(-menuPane.getPrefWidth());
        slideOut.setOnFinished(event -> {
            // Remove the menuPane from the root pane after the animation
            mainRoot.getChildren().remove(menuPane);
        });
        slideOut.play();
    }

    /**
     * Closes the application when the close button is clicked.
     */
    @FXML
    public void closeApp() {
        Platform.exit();
    }

    /**
     * Loads and displays the home page content in mainBox.
     */
    @FXML
    public void showHomePage() {
        loadPage("/view/Home.fxml");
    }

    /**
     * Loads and displays the accounts page content in mainBox.
     */
    @FXML
    public void showAccountPage() {
        loadPage("/view/Accounts.fxml");
    }

    /**
     * Loads and displays the transaction type page content in mainBox.
     */
    @FXML
    public void showTransactionTypePage() {
        loadPage("/view/TransactionType.fxml");
    }

    /**
     * Loads and displays the transactions page content in mainBox.
     */
    @FXML
    public void showTransactionPage() {
        loadPage("/view/Transactions.fxml");
    }

    /**
     * Loads and displays the reports page content in mainBox.
     */
    @FXML
    public void showReportPage() {
        loadPage("/view/Reports.fxml");
    }

    /**
     * Loads and displays the settings page content in mainBox.
     */
    @FXML
    public void showSettingPage() {
        loadPage("/view/Settings.fxml");
    }

    /**
     * Helper method to load an FXML page into the mainBox.
     *
     * @param fxmlPath Path to the FXML file to load
     */
    private void loadPage(String fxmlPath) {
        try {
            AnchorPane page = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainBox.getChildren().clear();
            mainBox.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the page.", e.getMessage());
        }
    }

    /**
     * Shows a simple alert dialog for displaying errors.
     *
     * @param title Title of the alert
     * @param header Header text of the alert
     * @param content Content message of the alert
     */
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
