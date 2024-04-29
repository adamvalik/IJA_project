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
    private Button playPauseButton;
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

    private final Environment env;
    private double robotSpeed;
    private double robotRadius;
    private double obstacleSize;

    public GameController() {
        this.env = new Environment(1180, 650);
    }

    @FXML
    public void initialize() {
        playPauseButton.setOnAction(event -> playPauseSimulation());
        movingForward.setOnAction(event -> movingForwardPressed());
        rotatingLeft.setOnAction(event -> rotatingLeftPressed());
        rotatingRight.setOnAction(event -> rotatingRightPressed());
        toggleRobots.setOnAction(event -> toggleRobotsPressed());

        playPauseButton.setFocusTraversable(false);
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
                if (simulationRunning) {
                    for (int i = 0; i < env.countControlledRobots(); i++) {
                        updateMovement(i);
                    }
                    for (int i = 0; i < env.countAutonomousRobots(); i++) {
                        updateAutonomousMovement(i);
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
                    robotRadius = Double.parseDouble(parts[2]);
                    obstacleSize = Double.parseDouble(parts[3]);
                    break;
                case "controlled_robot":
                    ControlledRobot robot = new ControlledRobot(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), robotRadius);
                    env.addControlledRobot(robot);
                    new ControlledRobotView(robot, simulationPane);
                    break;
                case "obstacle":
                    Obstacle obstacle = new Obstacle(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), obstacleSize);
                    env.addObstacle(obstacle);
                    new ObstacleView(obstacle, simulationPane);
                    break;
                case "autonomous_robot":
                    AutonomousRobot autonomousRobot = new AutonomousRobot(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), robotRadius);
                    env.addAutonomousRobot(autonomousRobot);
                    new AutonomousRobotView(autonomousRobot, simulationPane);
                    break;
            }
        }
    }

    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                if (env.countControlledRobots() > 0) {
                    if (!env.getControlledRobot(0).isMovingForward()) {
                        System.out.println("Player 1 moving forward");
                    }
                    env.getControlledRobot(0).setMovingForward(true);
                }
                break;
            case DOWN:
                if (env.countControlledRobots() > 0) {
                    if (!env.getControlledRobot(0).isMovingBackward()) {
                        System.out.println("Player 1 moving backward");
                    }
                    env.getControlledRobot(0).setMovingBackward(true);
                }
                break;
            case RIGHT:
                if (env.countControlledRobots() > 0) {
                    if (!env.getControlledRobot(0).isRotatingRight()) {
                        System.out.println("Player 1 rotating right");
                    }
                    env.getControlledRobot(0).setRotatingRight(true);
                }
                break;
            case LEFT:
                if (env.countControlledRobots() > 0) {
                    if (!env.getControlledRobot(0).isRotatingLeft()) {
                        System.out.println("Player 1 rotating left");
                    }
                    env.getControlledRobot(0).setRotatingLeft(true);
                }
                break;
            case W:
                if (env.countControlledRobots() > 1) {
                    if (!env.getControlledRobot(1).isMovingForward()) {
                        System.out.println("Player 2 moving forward");
                    }
                    env.getControlledRobot(1).setMovingForward(true);
                }
                break;
            case S:
                if (env.countControlledRobots() > 1) {
                    if (!env.getControlledRobot(1).isMovingBackward()) {
                        System.out.println("Player 2 moving backward");
                    }
                    env.getControlledRobot(1).setMovingBackward(true);
                }
                break;
            case D:
                if (env.countControlledRobots() > 1) {
                    if (!env.getControlledRobot(1).isRotatingRight()) {
                        System.out.println("Player 2 rotating right");
                    }
                    env.getControlledRobot(1).setRotatingRight(true);
                }
                break;
            case A:
                if (env.countControlledRobots() > 1) {
                    if (!env.getControlledRobot(1).isRotatingLeft()) {
                        System.out.println("Player 2 rotating left");
                    }
                    env.getControlledRobot(1).setRotatingLeft(true);
                }
                break;
            case SPACE:
                playPauseSimulation();
                break;

            default:
                break;
        }
    }

    public void handleKeyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                if (env.countControlledRobots() > 0) {
                    env.getControlledRobot(0).setMovingForward(false);
                }
                break;
            case DOWN:
                if (env.countControlledRobots() > 0) {
                    env.getControlledRobot(0).setMovingBackward(false);
                }
                break;
            case RIGHT:
                if (env.countControlledRobots() > 0) {
                    env.getControlledRobot(0).setRotatingRight(false);
                }
                break;
            case LEFT:
                if (env.countControlledRobots() > 0) {
                    env.getControlledRobot(0).setRotatingLeft(false);
                }
                break;
            case W:
                if (env.countControlledRobots() > 1) {
                    env.getControlledRobot(1).setMovingForward(false);
                }
                break;
            case S:
                if (env.countControlledRobots() > 1) {
                    env.getControlledRobot(1).setMovingBackward(false);
                }
                break;
            case D:
                if (env.countControlledRobots() > 1) {
                    env.getControlledRobot(1).setRotatingRight(false);
                }
                break;
            case A:
                if (env.countControlledRobots() > 1) {
                    env.getControlledRobot(1).setRotatingLeft(false);
                }
                break;
            default:
                break;
        }
    }

    private void updateMovement(int index) {
        ControlledRobot controlledRobot = env.getControlledRobot(index);

        if (controlledRobot.isMovingForward() || controlledRobot.isMovingBackward()) {
            double potentialX, potentialY;
            if (controlledRobot.isMovingForward()) {
                potentialX = controlledRobot.X().get() + robotSpeed * Math.cos(Math.toRadians(controlledRobot.angle().get()));
                potentialY = controlledRobot.Y().get() + robotSpeed * Math.sin(Math.toRadians(controlledRobot.angle().get()));
            } else {
                potentialX = controlledRobot.X().get() - robotSpeed * Math.cos(Math.toRadians(controlledRobot.angle().get()));
                potentialY = controlledRobot.Y().get() - robotSpeed * Math.sin(Math.toRadians(controlledRobot.angle().get()));
            }

            // Perform a lookahead collision check before actually moving.
            if (!env.checkCollisionAt(controlledRobot, potentialX, potentialY)) {
                // No collision predicted, so it's safe to move forward.
                controlledRobot.X().set(potentialX);
                controlledRobot.Y().set(potentialY);
            } else {
                System.out.println("Collision of controlled robot " + index);
                // Collision predicted: stop movement and handle collision.
                if (controlledRobot.isMovingForward()) {
                    controlledRobot.setMovingForward(false);
                }

                if (controlledRobot.isMovingBackward()) {
                    controlledRobot.setMovingBackward(false);
                }
            }
        }

        // Handle rotation only if not moving forward to avoid complex movement scenarios.
        if (controlledRobot.isRotatingRight()) {
            controlledRobot.rotateRight();
        } else if (controlledRobot.isRotatingLeft()) {
            controlledRobot.rotateLeft();
        }
    }

    public void updateAutonomousMovement(int index) {
        AutonomousRobot autonomousRobot = env.getAutonomousRobot(index);

        double potentialX = autonomousRobot.X().get() + robotSpeed * Math.cos(Math.toRadians(autonomousRobot.angle().get()));
        double potentialY = autonomousRobot.Y().get() + robotSpeed * Math.sin(Math.toRadians(autonomousRobot.angle().get()));

        if (!env.checkCollisionAt(autonomousRobot, potentialX, potentialY)) {
            autonomousRobot.X().set(potentialX);
            autonomousRobot.Y().set(potentialY);
        } else {
            System.out.println("Collision of autonomous robot " + index);
            autonomousRobot.rotate();
        }
    }

    public void playPauseSimulation() {
        if (simulationRunning) {
            System.out.println("Simulation paused");
        } else {
            System.out.println("Simulation resumed");
        }
        simulationRunning = !simulationRunning;
        playPauseButton.setText(simulationRunning ? "Pause" : "Play");
    }

    public void movingForwardPressed() {
        ControlledRobot robot = env.getControlledRobot(currentRobot);
        robot.setMovingForward(!robot.isMovingForward());
        if (robot.isMovingForward()) {
            System.out.println("Controlled robot " + currentRobot + " moving forward");
        }
        else {
            System.out.println("Controlled robot " + currentRobot + " stopped moving forward");
        }
    }

    public void rotatingLeftPressed() {
        ControlledRobot robot = env.getControlledRobot(currentRobot);
        robot.setRotatingLeft(!robot.isRotatingLeft());
        if (robot.isRotatingLeft()) {
            System.out.println("Controlled robot " + currentRobot + " rotating left");
        }
        else {
            System.out.println("Controlled robot " + currentRobot + " stopped rotating left");
        }
    }

    public void rotatingRightPressed() {
        ControlledRobot robot = env.getControlledRobot(currentRobot);
        robot.setRotatingRight(!robot.isRotatingRight());
        if (robot.isRotatingRight()) {
            System.out.println("Controlled robot " + currentRobot + " rotating right");
        }
        else {
            System.out.println("Controlled robot " + currentRobot + " stopped rotating right");
        }
    }

    public void toggleRobotsPressed() {
        currentRobot = (currentRobot + 1) % env.countControlledRobots();
        System.out.println("Current controlled robot: " + currentRobot);
    }
}
