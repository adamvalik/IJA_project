package ija;

import ija.controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("menu-view.fxml"));
        Parent root = loader.load();

        MenuController menuController = loader.getController();
        menuController.robots();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("2D Robot Simulation");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}