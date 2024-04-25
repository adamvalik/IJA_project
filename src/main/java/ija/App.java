package ija;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ija.controller.GameController;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file using the class loader
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("app-view.fxml"));
        Parent root = loader.load();

        // Get the controller instance
        GameController gameController = loader.getController();

        // Set up the scene
        Scene scene = new Scene(root);

        // Set the scene for the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Your Application Title");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}