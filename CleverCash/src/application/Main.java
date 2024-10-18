package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private final static String title = "Budget Tracker Application";

    private static Button createButton(String name, String iconPath, double prefW, double prefH) {
        Button button = new Button();
        
        // Create an ImageView for the icon
        ImageView icon = new ImageView(new Image(Main.class.getResourceAsStream(iconPath)));
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
        try {
            // Create buttons
        	
            Button home = createButton("Home", "/application/resources/home.png", 300, 50);
            Button accounts = createButton("Accounts", "/application/resources/accounts.png", 300, 50);
            Button transactions = createButton("Transactions", "/application/resources/transactions.png", 300, 50);
            Button reports = createButton("Reports", "/application/resources/reports.png", 300, 50);
            Button settings = createButton("Settings", "/application/resources/settings.png", 300, 50);

            // Create input box and icon
            TextField searchField = new TextField();
            searchField.setPromptText("Search...");
            searchField.setPrefWidth(250);
            searchField.setPrefHeight(50);
            
            ImageView searchIcon = new ImageView(new Image(Main.class.getResourceAsStream("/application/resources/search.png")));
            searchIcon.setFitWidth(30);
            searchIcon.setFitHeight(30);
            
            HBox searchBox = new HBox(10);
            searchBox.getChildren().addAll(searchIcon, searchField);
            searchBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            searchBox.setPadding(new Insets(0, 0, 10, 0)); // Padding between search box and buttons

            // VBox for buttons
            VBox vbox = new VBox(20);
            vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
            vbox.setPadding(new Insets(20));
            vbox.getChildren().addAll(searchBox, home, accounts, transactions, reports, settings);
            vbox.setBackground(new Background(new BackgroundFill(Color.web("#AAAAAA"), CornerRadii.EMPTY, null)));

            // Set up the layout
            BorderPane root = new BorderPane();
            root.setLeft(vbox);
            
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}