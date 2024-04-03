package ija;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Robot Room Adventure");

        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawRoom(gc); // Method to draw the initial state of the room

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawRoom(GraphicsContext gc) {
        // Implement drawing logic here
        gc.fillText("Welcome to Robot Room!", 150, 200);
        // Draw robots and obstacles
    }

    public static void main(String[] args) {
        launch(args);
    }
}
