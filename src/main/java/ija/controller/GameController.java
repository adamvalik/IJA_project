package ija.controller;

import ija.model.ControlledRobot;
import ija.model.Environment;
import ija.model.Obstacle;
import ija.view.ControlledRobotView;
import ija.view.ObstacleView;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.List;

public class GameController {
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Pane simulationPane;

    private Environment env;

    private boolean movingForward = false;
    private boolean rotatingRight = false;
    private boolean rotatingLeft = false;

    private double robotSpeed;


    public GameController() {
        this.env = new Environment();
    }

    @FXML
    public void initialize() {
        startButton.setOnAction(event -> startSimulation());
        pauseButton.setOnAction(event -> pauseSimulation());
        stopButton.setOnAction(event -> stopSimulation());
        startButton.setFocusTraversable(false);
        pauseButton.setFocusTraversable(false);
        stopButton.setFocusTraversable(false);
    }

    public void initialize(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(this::handleKeyRelease);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateMovement(); // Call updateMovement at each frame
            }
        }.start();


    }

    public void loadEnvironment(List<String> CSVFile) {
        for (String line : CSVFile) {
            String[] parts = line.split(",");
            switch (parts[0]) {
                case "settings":
                    robotSpeed = Double.parseDouble(parts[1]);
                    break;
                case "controlled_robot":
                    ControlledRobot robot = new ControlledRobot(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                    env.addControlledRobot(robot);
                    new ControlledRobotView(robot, simulationPane);
                    break;
                case "obstacle":
                    Obstacle obstacle = new Obstacle(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                    env.addObstacle(obstacle);
                    new ObstacleView(obstacle, simulationPane);
                    break;
                case "autonomous_robot":
                    // Add autonomous robot
                    break;
            }
        }
    }

    // Handle key press to start movement or rotation
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                movingForward = true;
                break;
            case RIGHT:
                rotatingRight = true;
                break;
            case LEFT:
                rotatingLeft = true;
                break;
            case SPACE:
                // Toggle start/stop might require a separate mechanism
                break;
            default:
                break;
        }
        updateMovement();
    }

    // Handle key release to stop movement or rotation
    public void handleKeyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                movingForward = false;
                break;
            case RIGHT:
                rotatingRight = false;
                break;
            case LEFT:
                rotatingLeft = false;
                break;
            default:
                break;
        }
        updateMovement();
    }

    private void updateMovement() {
        ControlledRobot controlledRobot = env.getControlledRobots().get(0); // Assume a single controlled robot for simplicity

        // Determine new position without actually moving the robot
        double potentialX = controlledRobot.X().get();
        double potentialY = controlledRobot.Y().get();

        if (movingForward) {
            potentialX += robotSpeed * Math.cos(Math.toRadians(controlledRobot.angle().get()));
            potentialY += robotSpeed * Math.sin(Math.toRadians(controlledRobot.angle().get()));
        }
        // Simulate the movement
        controlledRobot.X().set(potentialX);
        controlledRobot.Y().set(potentialY);

        // Check for collisions
        if (env.checkCollision(controlledRobot)) {
            // Handle collision - e.g., stop the robot, reverse slightly, etc.
            if (movingForward ) {
                controlledRobot.moveForward(-robotSpeed); // Simple reversal of last move
                if (movingForward) {
                    movingForward = false;
                }
            }
        } else {
            // If no collision, apply potential movement
            if (movingForward) {
                controlledRobot.moveForward(robotSpeed);
            }
        }

        // Handle rotation with collision checking
        if (rotatingRight) {
            controlledRobot.rotate(3); // Rotate right
        } else if (rotatingLeft) {
            controlledRobot.rotate(-3); // Rotate left
        }

    }




    // Implementation of simulation control methods
    public void startSimulation() {
        System.out.println("Simulation started");
        // Additional start logic here
    }

    public void pauseSimulation() {
        System.out.println("Simulation paused");
        // Additional pause logic here
    }

    public void stopSimulation() {
        System.out.println("Simulation stopped");
        // Additional stop/reset logic here
    }
}
