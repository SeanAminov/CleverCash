package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Main class is the entry point for the JavaFX application.
 * It sets up the primary stage and loads the initial FXML layout.
 */
public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    
    // Singleton instance of root
    private static AnchorPane root;

    /**
     * Gets the singleton instance of the root AnchorPane.
     * Loads the FXML layout only once, if not already loaded.
     * 
     * @return The singleton instance of the root AnchorPane.
     */
    private static AnchorPane getRoot() {
        if (root == null) {
            try {
                root = FXMLLoader.load(Main.class.getResource("/view/Main.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return root;
    }

    /**
     * Starts the JavaFX application by setting up the primary stage.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane root = getRoot();
            Scene scene = new Scene(root, 1280, 800);

            // Fetch the top bar AnchorPane from the FXML (assuming it’s the first child in root)
            AnchorPane topBar = (AnchorPane) root.getChildren().get(0);

            // Apply the draggable behavior to each child in the top bar
            for (javafx.scene.Node child : topBar.getChildren()) {
                setDraggable(child, primaryStage);
            }

            // Set up specific behavior for the close button (assuming it has fx:id="closeButton")
            ImageView closeButton = (ImageView) root.lookup("#closeButton"); // Assuming close button has fx:id="closeButton"
            if (closeButton != null) {
                closeButton.setOnMouseClicked(event -> {
                    primaryStage.close();
                    event.consume(); // Prevents the click from being processed as a drag
                });
            }

            // Set the stylesheet and other properties
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a node draggable by adding mouse listeners that update the stage position.
     *
     * @param node The node to make draggable.
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

    /**
     * The main method, which serves as the entry point for the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
