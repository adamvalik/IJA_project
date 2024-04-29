package ija.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button newGame;
    @FXML
    private Button loadGame;
    @FXML
    private Button endGame;

    @FXML
    public void initialize() {
        newGame.setOnMouseClicked(this::openSettings);
        loadGame.setOnMouseClicked(this::openFileManager);
        endGame.setOnMouseClicked(this::closeWindow);

    }

    private void openSettings(MouseEvent event) {
        try {
            // Load the game view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("settings-view.fxml"));
            Parent settingsRoot = loader.load();

            // Get the game controller and initiate the game setup
            SettingsController settingsController = loader.getController();

            // Setup the new stage and scene
            Scene settingsScene = new Scene(settingsRoot);
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.setScene(settingsScene);

            settingsController.initialize();

            // Show the new window
            settingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFileManager(MouseEvent event){

    }

    private void closeWindow(MouseEvent event) {
        Scene scene = endGame.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}
