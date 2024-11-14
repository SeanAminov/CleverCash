package application.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MenuBarController {

    @FXML
    private AnchorPane menuPane;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void closeMenu() {
        if (mainController != null && menuPane != null) {
            mainController.hideMenuSlider(menuPane);
        }
    }

    @FXML
    private void handleHomeAction() {
        if (mainController != null) {
            mainController.showHomePage();
            closeMenu();
        }
    }
    
    // Implement similar methods for Accounts, Transactions, etc.
}
