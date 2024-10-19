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

public class HomepageController extends Application {

    private final static String title = "Budget Tracker Application";
    private BorderPane root;

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

        accounts.setOnAction(e -> showAccountsPage());

        return new VBox(20, searchBox, home, accounts, transactions, reports, settings);
    }

    private void showAccountsPage() {
        VBox accountsLayout = new VBox(20);
        accountsLayout.setPadding(new Insets(20));
        accountsLayout.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        Button addAccountButton = new Button("Add Account");
        addAccountButton.setPrefSize(250, 50);
        addAccountButton.setOnAction(e -> new AccountController().show());

        accountsLayout.getChildren().add(addAccountButton);
        root.setCenter(accountsLayout);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
