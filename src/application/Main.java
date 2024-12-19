package application;

import application.services.Alerts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Main class is the entry point for the JavaFX application.
 */
public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Starts the JavaFX application by setting up the primary stage.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main FXML file
            AnchorPane root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Scene scene = new Scene(root, 1280, 800);

            // Fetch the full top bar AnchorPane using fx:id
            Separator topBar = (Separator) root.lookup("#topBar");
            if (topBar != null) {
                // Apply draggable behavior to the full top bar
                setDraggable(topBar, primaryStage);
            }

            // Configure the stage
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED); // Removes default OS window decorations
            primaryStage.show();

            // Notify user of scheduled transactions due today
            Alerts alerts = new Alerts();
            alerts.showDueTodayNotifications();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a node draggable by adding mouse listeners that update the stage position.
     *
     * @param node  The node to make draggable.
     * @param stage The primary stage to move.
     */
    private void setDraggable(javafx.scene.Node node, Stage stage) {
        node.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        node.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
