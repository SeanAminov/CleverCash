package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The HomepageController class represents the main interface for the 
 * Budget Tracker Application. It provides navigation between different 
 * sections such as Home, Accounts, Transactions, Reports, and Settings.
 */
public class HomepageController extends Application {

    /** Title of the application window. */
    private final static String title = "Budget Tracker Application";
    
    /** Main layout container for the application. */
    private BorderPane root;

    /** Instance of AccountController to manage accounts-related actions. */
    private AccountController aController = new AccountController();

    /**
     * Creates a button with a given name, icon, and size.
     * 
     * @param name    The label text for the button.
     * @param iconPath The path to the icon image.
     * @param prefW   The preferred width of the button.
     * @param prefH   The preferred height of the button.
     * @return A Button object with the specified properties.
     */
    private static Button createButton(String name, String iconPath, double prefW, double prefH) {
        Button button = new Button();
        ImageView icon = new ImageView(new Image(HomepageController.class.getResourceAsStream(iconPath)));
        icon.setFitWidth(30);
        icon.setFitHeight(30);

        HBox hbox = new HBox(10, icon, new Label(name));
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        button.setGraphic(hbox);
        button.setPrefSize(prefW, prefH);

        return button;
    }

    /**
     * Starts the JavaFX application and initializes the primary stage.
     * 
     * @param primaryStage The main application window.
     */
    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();

        VBox vbox = new VBox(20, createMenuButtons());
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setPadding(new Insets(10));
        vbox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, null)));

        root.setLeft(vbox);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();
    }

    /**
     * Creates the menu buttons with event handlers and returns them as a VBox.
     * 
     * @return A VBox containing the search field and menu buttons.
     */
    private VBox createMenuButtons() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefSize(250, 50);

        ImageView searchIcon = new ImageView(new Image(getClass().getResourceAsStream("/application/resources/search.png")));
        searchIcon.setFitWidth(30);
        searchIcon.setFitHeight(30);

        HBox searchBox = new HBox(10, searchIcon, searchField);
        searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        VBox.setMargin(searchBox, new Insets(10));

        Button home = createButton("Home", "/application/resources/home.png", 300, 50);
        Button accounts = createButton("Accounts", "/application/resources/accounts.png", 300, 50);
        Button transactions = createButton("Transactions", "/application/resources/transactions.png", 300, 50);
        Button reports = createButton("Reports", "/application/resources/reports.png", 300, 50);
        Button settings = createButton("Settings", "/application/resources/settings.png", 300, 50);

        home.setOnAction(e -> showHomePage());
        accounts.setOnAction(e -> showAccountsPage());
        transactions.setOnAction(e -> showTransactionsPage());
        reports.setOnAction(e -> showReportsPage());
        settings.setOnAction(e -> showSettingsPage());

        return new VBox(20, searchBox, home, accounts, transactions, reports, settings);
    }

    /**
     * Displays the Accounts page by clearing the center of the layout and
     * calling the AccountController to show its content.
     */
    private void showAccountsPage() {
        root.setCenter(new VBox()); 
        aController.show(root); 
    }

    /**
     * Displays the Home page by clearing the center of the layout.
     */
    private void showHomePage() {
        root.setCenter(new VBox());
        // WILL ADD MORE LATER(filler) //
    }

    /**
     * Displays the Transactions page by clearing the center of the layout.
     */
    private void showTransactionsPage() {
        root.setCenter(new VBox());
    }

    /**
     * Displays the Reports page by clearing the center of the layout.
     */
    private void showReportsPage() {
        root.setCenter(new VBox());
    }

    /**
     * Displays the Settings page by clearing the center of the layout.
     */
    private void showSettingsPage() {
        root.setCenter(new VBox());
    }

    /**
     * The main entry point for the JavaFX application.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
