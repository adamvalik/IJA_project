package ija.controller;

import ija.model.AutonomousRobot;
import ija.model.ControlledRobot;
import ija.model.Environment;
import ija.model.Obstacle;
import ija.view.AutonomousRobotView;
import ija.view.ControlledRobotView;
import ija.view.ObstacleView;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.List;

public class GameController {
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Pane simulationPane;
    @FXML
    private Button movingForward;
    @FXML
    private Button rotatingLeft;
    @FXML
    private Button rotatingRight;
    @FXML
    private Button toggleRobots;

    private boolean simulationRunning = false;
    private int currentRobot = 0;

    private Environment env;
    private double robotSpeed;

    public GameController() {
        this.env = new Environment(1180, 650);
    }

    @FXML
    public void initialize() {
        startButton.setOnAction(event -> startSimulation());
        pauseButton.setOnAction(event -> pauseSimulation());

        movingForward.setOnAction(event -> movingForwardPressed());
        rotatingLeft.setOnAction(event -> rotatingLeftPressed());
        rotatingRight.setOnAction(event -> rotatingRightPressed());
        toggleRobots.setOnAction(event -> toggleRobotsPressed());

        startButton.setFocusTraversable(false);
        pauseButton.setFocusTraversable(false);
        movingForward.setFocusTraversable(false);
        rotatingLeft.setFocusTraversable(false);
        rotatingRight.setFocusTraversable(false);
        toggleRobots.setFocusTraversable(false);
    }

    public void initialize(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(this::handleKeyRelease);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update controlled robots
                if (simulationRunning) {
                    for (int i = 0; i < env.countControlledRobots(); i++) {
                        updateMovement(i);
                    }

                    // Update autonomous robots
                    for (int i = 0; i < env.countAutonomousRobots(); i++) {
                        updateAutonomousMovement(i); // A method specifically for autonomous logic
                    }
                }
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
                    AutonomousRobot autonomousRobot = new AutonomousRobot(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]));
                    env.addAutonomousRobot(autonomousRobot);
                    new AutonomousRobotView(autonomousRobot, simulationPane);
                    break;
            }
        }
    }

    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                env.getControlledRobot(0).setMovingForward(true);
                updateMovement(0);
                break;
            case RIGHT:
                env.getControlledRobot(0).setRotatingRight(true);
                updateMovement(0);
                break;
            case LEFT:
                env.getControlledRobot(0).setRotatingLeft(true);
                updateMovement(0);
                break;
            case W:
                env.getControlledRobot(1).setMovingForward(true);
                updateMovement(1);
                break;
            case D:
                env.getControlledRobot(1).setRotatingRight(true);
                updateMovement(1);
                break;
            case A:
                env.getControlledRobot(1).setRotatingLeft(true);
                updateMovement(1);
                break;

            default:
                break;
        }
    }

    // Handle key release to stop movement or rotation
    public void handleKeyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                env.getControlledRobot(0).setMovingForward(false);
                updateMovement(0);
                break;
            case RIGHT:
                env.getControlledRobot(0).setRotatingRight(false);
                updateMovement(0);
                break;
            case LEFT:
                env.getControlledRobot(0).setRotatingLeft(false);
                updateMovement(0);
                break;
            case W:
                env.getControlledRobot(1).setMovingForward(false);
                updateMovement(1);
                break;
            case D:
                env.getControlledRobot(1).setRotatingRight(false);
                updateMovement(1);
                break;
            case A:
                env.getControlledRobot(1).setRotatingLeft(false);
                updateMovement(1);
                break;
            default:
                break;
        }
    }

    private void updateMovement(int index) {
        ControlledRobot controlledRobot = env.getControlledRobot(index);

        double potentialX = controlledRobot.X().get();
        double potentialY = controlledRobot.Y().get();

        if (controlledRobot.isMovingForward()) {
            potentialX += robotSpeed * Math.cos(Math.toRadians(controlledRobot.angle().get()));
            potentialY += robotSpeed * Math.sin(Math.toRadians(controlledRobot.angle().get()));
        }
        // Simulate the movement
        controlledRobot.X().set(potentialX);
        controlledRobot.Y().set(potentialY);

        // Check for collisions
        if (env.checkCollision(controlledRobot)) {
            if (controlledRobot.isMovingForward()) {
                controlledRobot.moveForward(-robotSpeed); // Simple reversal of last move
                if (controlledRobot.isMovingForward()) {
                    controlledRobot.setMovingForward(false);
                }
            }
        } else {
            // If no collision, apply potential movement
            if (controlledRobot.isMovingForward()) {
                controlledRobot.moveForward(robotSpeed);
            }
        }

        // Handle rotation with collision checking
        if (controlledRobot.isRotatingRight()) {
            controlledRobot.rotateRight(); // Rotate right
        } else if (controlledRobot.isRotatingLeft()) {
            controlledRobot.rotateLeft(); // Rotate left
        }
    }

    public void updateAutonomousMovement(int index) {
        AutonomousRobot autonomousRobot = env.getAutonomousRobot(index);

        double potentialX = autonomousRobot.X().get();
        double potentialY = autonomousRobot.Y().get();

        // Simulate the movement
        autonomousRobot.X().set(potentialX);
        autonomousRobot.Y().set(potentialY);

        // Check for collisions
        if (env.checkCollision(autonomousRobot)) {
            autonomousRobot.moveForward(-robotSpeed); // Simple reversal of last move
            autonomousRobot.rotate(); // Rotate to avoid collision
        } else {
            // If no collision, apply potential movement
            autonomousRobot.moveForward(robotSpeed);
        }
    }


    // Implementation of simulation control methods
    public void startSimulation() {
        System.out.println("Simulation started");
        // Additional start logic here
        simulationRunning = true;
    }

    public void pauseSimulation() {
        System.out.println("Simulation paused");
        // Additional pause logic here
        simulationRunning = false;
    }

    public void movingForwardPressed() {
        ControlledRobot robot = env.getControlledRobot(currentRobot);
        robot.setMovingForward(!robot.isMovingForward());
        updateMovement(currentRobot);
    }

    public void rotatingLeftPressed() {
        ControlledRobot robot = env.getControlledRobot(currentRobot);
        robot.setRotatingLeft(!robot.isRotatingLeft());
        updateMovement(currentRobot);
    }

    public void rotatingRightPressed() {
        ControlledRobot robot = env.getControlledRobot(currentRobot);
        robot.setRotatingRight(!robot.isRotatingRight());
        updateMovement(currentRobot);
    }

    public void toggleRobotsPressed() {
        currentRobot = (currentRobot + 1) % env.countControlledRobots();
    }
}
