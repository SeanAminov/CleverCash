package application.controller;

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

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

	@FXML 
	private AnchorPane menuBox;

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
     */
    @FXML
    public void showMenuSlider() {
    	//needs to be implemented
    }

    /**
     * Loads a page specified by the FXML file path.
     *
     * @param fxmlPath The path to the FXML file.
     */
    private void loadPage(String fxmlPath) {
        URL url = getClass().getResource(fxmlPath);
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
 // Page display methods
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
}
