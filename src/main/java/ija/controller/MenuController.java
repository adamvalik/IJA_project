package ija.controller;

import ija.model.AutonomousRobot;
import ija.model.Collision;
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

public class MenuController {
    @FXML
    private Button newGame;
    @FXML
    private Button loadGame;
    @FXML
    private Button endGame;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Text text;

    private Environment env;

    private Stage settingsStage;
    private Stage primaryStage;
    private Stage gameStage;

    @FXML
    public void initialize() {
        newGame.setOnMouseClicked(this::openSettings);
        loadGame.setOnMouseClicked(this::importCSV);
        endGame.setOnMouseClicked(this::closeWindow);

        newGame.setFocusTraversable(false);
        loadGame.setFocusTraversable(false);
        endGame.setFocusTraversable(false);
    }

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
        for (int i = 0; i < 8; i++) {
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
        new AutonomousRobotView(robot, menuPane, false);
    }

    private void openSettings(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("settings-view.fxml"));
            Parent settingsRoot = loader.load();

            SettingsController settingsController = loader.getController();
            settingsController.initialize(this);

            Scene settingsScene = new Scene(settingsRoot);
            settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.setScene(settingsScene);

            settingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSettings() {
        if (settingsStage != null) {
            settingsStage.close();
        }
    }

    private void importCSV(MouseEvent event){
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
                while ((line = reader.readLine()) != null) {
                    importedData.add(line);
                }
                // Handle the imported data as needed
                this.openGame(importedData);
                System.out.println("CSV file imported successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File open operation cancelled.");
        }
    }

    private void openGame(List<String> CSV) {
        try {
            // Load the game view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));
            Parent gameRoot = loader.load();

            // Get the game controller and initiate the game setup
            GameController gameController = loader.getController();

            // Setup the new stage and scene
            Scene gameScene = new Scene(gameRoot);
            gameStage = new Stage();
            gameStage.setTitle("Game Window");
            gameStage.setScene(gameScene);

            gameController.initialize(gameScene, null, this);
            gameController.loadEnvironment(CSV);

            gameStage.setResizable(false);


            // Show the new window
            gameStage.show();
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
