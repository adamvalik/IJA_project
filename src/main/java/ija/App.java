/**
 * @package ija
 * @file App.java
 * @class App
 *
 * Main class of the application, responsible for starting the menu window
 *
 * @author Adam Valík
 * @author Dominik Horut
 */
package ija;

import ija.controller.MenuController;
import ija.other.StageSetter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the application
 */
public class App extends Application {
    /**
     * Starts the application by loading the menu window
     * @param primaryStage Stage of the application
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("menu-view.fxml"));
        Parent root = loader.load();

        MenuController menuController = loader.getController();
        menuController.initialize(primaryStage);
        // add the robots to the menu background
        menuController.robots();

        Scene scene = new Scene(root);

        StageSetter.initStage(primaryStage, "2D Robot Simulation", scene, false);
        primaryStage.show();
    }

    /**
     * Main method of the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}