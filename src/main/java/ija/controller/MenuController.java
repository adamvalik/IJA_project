package ija.controller;

import ija.model.AutonomousRobot;
import ija.model.Environment;
import ija.view.AutonomousRobotView;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller for the main menu window.
 */
public class MenuController {

    @FXML
    private Button newGame;
    @FXML
    private Button loadGame;
    @FXML
    private Button endGame;

    @FXML
    private AnchorPane menuPane;

    /**
     * Logo of game in menu.
     */
    @FXML
    private Text text;
    private Environment env;

    // All stages user can get to from menu
    private Stage settingsStage;
    private Stage primaryStage;
    private Stage gameStage;

    /**
     * Initializes the menu controller by binding buttons to methods
     */
    @FXML
    public void initialize() {
        newGame.setOnMouseClicked(this::openSettings);
        loadGame.setOnMouseClicked(this::importCSV);
        endGame.setOnMouseClicked(this::closeWindow);

        newGame.setFocusTraversable(false);
        loadGame.setFocusTraversable(false);
        endGame.setFocusTraversable(false);
    }

    /**
     * Sets primary stage
     * @param primaryStage Primary stage to be set.
     */
    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void robots() {
        this.env = new Environment(1180, 650);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < env.countAutonomousRobots(); i++) {
                    AutonomousRobot auto = env.getAutonomousRobots().get(i);
                    double x = auto.X().get();
                    double y = auto.Y().get();
                    double radius = auto.getRadius();
                    double potentialX = x + 5 * Math.cos(Math.toRadians(auto.angle().get()));
                    double potentialY = y + 5 * Math.sin(Math.toRadians(auto.angle().get()));

                    if (potentialX - radius < 0 || potentialX + radius > 772 || potentialY - radius < 0 || potentialY + radius > 522) {
                        auto.rotate();
                    } else {
                        auto.X().set(potentialX);
                        auto.Y().set(potentialY);
                    }
                }
            }
        }.start();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            setRobotBackground(random.nextInt(732)+20, random.nextInt(482)+20, random.nextInt(359), random.nextInt(359));
        }

        newGame.toFront();
        loadGame.toFront();
        endGame.toFront();
        text.toFront();

        newGame.setOnMouseEntered(e -> {newGame.setStyle(newGame.getStyle() + "-fx-background-color: #FFEE32;");});
        newGame.setOnMouseExited(e -> {newGame.setStyle(newGame.getStyle() + "-fx-background-color: #FFD100;");});

        loadGame.setOnMouseEntered(e -> {loadGame.setStyle(loadGame.getStyle() + "-fx-background-color: #FFEE32;");});
        loadGame.setOnMouseExited(e -> {loadGame.setStyle(loadGame.getStyle() + "-fx-background-color: #FFD100;");});

        endGame.setOnMouseEntered(e -> {endGame.setStyle(endGame.getStyle() + "-fx-background-color: #FFEE32;");});
        endGame.setOnMouseExited(e -> {endGame.setStyle(endGame.getStyle() + "-fx-background-color: #FFD100;");});
    }

    private void setRobotBackground(double x, double y, double angle, double turn) {
        AutonomousRobot robot = new AutonomousRobot(x, y, angle, turn, 0, 20);
        env.addAutonomousRobot(robot);
        new AutonomousRobotView(robot, menuPane, false, true);
    }

    /**
     * Opens settings window.
     * @param event Mouse event which triggered opening of settings.
     */
    private void openSettings(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("settings-view.fxml"));
            Parent settingsRoot = loader.load();

            // Prepare game controller
            SettingsController settingsController = loader.getController();
            settingsController.initialize(this);

            // Set up the new stage and scene
            Scene settingsScene = new Scene(settingsRoot);
            settingsStage = new Stage();
            StageSetter.initStage(settingsStage, "Settings", settingsScene, false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSettings() {
        if (settingsStage != null) {
            settingsStage.close();
        }
    }

    /**
     * Imports CSV file with game data from users filesystem.
     * @param event Mouse event which triggered import of CSV file.
     */
    private void importCSV(MouseEvent event) {

        // File chooser setup
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show open file dialog
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {

            // Read data from CSV
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                List<String> importedData = new ArrayList<>();

                // Reading the whole file into imported data list
                while ((line = reader.readLine()) != null) {
                    importedData.add(line);
                }

                this.openGame(importedData);
                System.out.println("CSV file imported successfully.");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("File open operation cancelled.");
        }
    }

    /**
     * Opens the game with imported game data given in CSV file.
     * @param CSV File containing game data.
     */
    private void openGame(List<String> CSV) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));
            Parent gameRoot = loader.load();

            // Prepare game controller
            GameController gameController = loader.getController();

            // Set up the new stage and scene
            Scene gameScene = new Scene(gameRoot);
            gameStage = new Stage();

            StageSetter.initStage(gameStage, "Game Window", gameScene, false);
            gameController.initialize(gameScene, null, this);

            // Loading game from CSV file
            gameController.loadEnvironment(CSV);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopGame() {
        if (gameStage != null) {
            gameStage.close();
        }
    }

    private void closeWindow(MouseEvent event) {
        Scene scene = endGame.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
