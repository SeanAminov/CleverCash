package application.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;

/**
 * The MainController class handles the main navigation and management of different pages
 * within the application, allowing users to switch between various views such as Home,
 * Accounts, Transactions, Reports, and Settings.
 */
public class MainController {

    /** The currently displayed pane in the main view. */
    @FXML
    private AnchorPane currentPane;

    /** The main container for displaying the application's content. */
    @FXML
    private AnchorPane mainBox;

    /**
     * Initializes the MainController by showing the home page when the application starts.
     */
    @FXML
    public void initialize() {
        showHomePage();
    }

    /**
     * Closes the application.
     * This method is triggered when the user chooses to exit the application.
     */
    @FXML
    public void closeApp() {
        Platform.exit();
    }

    /**
     * Displays the menu slider by loading the MenuBar.fxml file.
     * Removes the current pane if it exists before adding the new one.
     */
    @FXML
    public void showMenuSlider() {
        URL url = getClass().getResource("/view/MenuBar.fxml");

        try {
            if (currentPane != null) {
                mainBox.getChildren().remove(currentPane);
            }

            currentPane = FXMLLoader.load(url);
            mainBox.getChildren().add(currentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the home page by loading the Home.fxml file.
     * Sets the layout position of the home page and updates the current pane.
     */
    @FXML
    public void showHomePage() {
        URL url = getClass().getResource("/view/Home.fxml");

        try {
            if (currentPane != null) {
                mainBox.getChildren().remove(currentPane);
            }

            currentPane = FXMLLoader.load(url);
            currentPane.setLayoutX(71.0);
            currentPane.setLayoutY(57.0);
            mainBox.getChildren().add(currentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the accounts page by loading the Accounts.fxml file.
     * Sets the layout position of the accounts page and updates the current pane.
     */
    @FXML
    public void showAccountPage() {
        URL url = getClass().getResource("/view/Accounts.fxml");

        try {
            if (currentPane != null) {
                mainBox.getChildren().remove(currentPane);
            }

            currentPane = FXMLLoader.load(url);
            currentPane.setLayoutX(71.0);
            currentPane.setLayoutY(57.0);
            mainBox.getChildren().add(currentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the transactions page by loading the Transactions.fxml file.
     * Sets the layout position of the transactions page and updates the current pane.
     */
    @FXML
    public void showTransactionPage() {
        URL url = getClass().getResource("/view/Transactions.fxml");

        try {
            if (currentPane != null) {
                mainBox.getChildren().remove(currentPane);
            }

            currentPane = FXMLLoader.load(url);
            currentPane.setLayoutX(71.0);
            currentPane.setLayoutY(57.0);
            mainBox.getChildren().add(currentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the reports page by loading the Reports.fxml file.
     * Sets the layout position of the reports page and updates the current pane.
     */
    @FXML
    public void showReportPage() {
        URL url = getClass().getResource("/view/Reports.fxml");

        try {
            if (currentPane != null) {
                mainBox.getChildren().remove(currentPane);
            }

            currentPane = FXMLLoader.load(url);
            currentPane.setLayoutX(71.0);
            currentPane.setLayoutY(57.0);
            mainBox.getChildren().add(currentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the settings page by loading the Settings.fxml file.
     * Sets the layout position of the settings page and updates the current pane.
     */
    @FXML
    public void showSettingPage() {
        URL url = getClass().getResource("/view/Settings.fxml");

        try {
            if (currentPane != null) {
                mainBox.getChildren().remove(currentPane);
            }

            currentPane = FXMLLoader.load(url);
            currentPane.setLayoutX(71.0);
            currentPane.setLayoutY(57.0);
            mainBox.getChildren().add(currentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
