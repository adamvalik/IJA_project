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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import java.util.List;
import java.util.Objects;

public class GameController {
    @FXML
    private Button playPauseButton;
    @FXML
    private Pane simulationPane;
    @FXML
    private Button stopButton;
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
    private boolean raceMode;
    private EditorController editor;
    private MenuController menu;
    private AnimationTimer timer;

    public GameController() {
        this.env = new Environment(1200, 650);
    }

    @FXML
    public void initialize() {
        playPauseButton.setOnAction(event -> playPauseSimulation());
        stopButton.setOnAction(event -> stopSimulation());
        movingForward.setOnAction(event -> movingForwardPressed());
        rotatingLeft.setOnAction(event -> rotatingLeftPressed());
        rotatingRight.setOnAction(event -> rotatingRightPressed());
        toggleRobots.setOnAction(event -> toggleRobotsPressed());

        playPauseButton.setFocusTraversable(false);
        stopButton.setFocusTraversable(false);
        movingForward.setFocusTraversable(false);
        rotatingLeft.setFocusTraversable(false);
        rotatingRight.setFocusTraversable(false);
        toggleRobots.setFocusTraversable(false);

        ImageView playView = new ImageView(new Image(getClass().getResourceAsStream("/play.png")));
        prepareButtonView(playView, playPauseButton, 60, 60);
        ImageView pauseView = new ImageView(new Image(getClass().getResourceAsStream("/pause.png")));
        ImageView stopView = new ImageView(new Image(getClass().getResourceAsStream("/stop.png")));
        prepareButtonView(stopView, stopButton, 60, 60);
        ImageView moveView = new ImageView(new Image(getClass().getResourceAsStream("/move.png")));
        prepareButtonView(moveView, movingForward, 60, 60);
        ImageView leftView = new ImageView(new Image(getClass().getResourceAsStream("/left.png")));
        prepareButtonView(leftView, rotatingLeft, 60, 60);
        ImageView rightView = new ImageView(new Image(getClass().getResourceAsStream("/right.png")));
        prepareButtonView(rightView, rotatingRight, 60, 60);

        ImageView playHover = new ImageView(new Image(getClass().getResourceAsStream("/playhover.png")));
        ImageView pauseHover = new ImageView(new Image(getClass().getResourceAsStream("/pausehover.png")));
        ImageView stopHover = new ImageView(new Image(getClass().getResourceAsStream("/stophover.png")));
        ImageView moveHover = new ImageView(new Image(getClass().getResourceAsStream("/movehover.png")));
        ImageView leftHover = new ImageView(new Image(getClass().getResourceAsStream("/lefthover.png")));
        ImageView rightHover = new ImageView(new Image(getClass().getResourceAsStream("/righthover.png")));

        playPauseButton.setOnMouseEntered(event -> playPauseEntered(playHover, pauseHover));
        stopButton.setOnMouseEntered(event -> prepareButtonView(stopHover, stopButton, 60, 60));
        movingForward.setOnMouseEntered(event -> prepareButtonView(moveHover, movingForward, 60, 60));
        rotatingLeft.setOnMouseEntered(event -> prepareButtonView(leftHover, rotatingLeft, 60, 60));
        rotatingRight.setOnMouseEntered(event -> prepareButtonView(rightHover, rotatingRight, 60, 60));

        playPauseButton.setOnMouseExited(event -> playPauseExited(playView, pauseView));
        stopButton.setOnMouseExited(event -> prepareButtonView(stopView, stopButton, 60, 60));
        movingForward.setOnMouseExited(event -> prepareButtonView(moveView, movingForward, 60, 60));
        rotatingLeft.setOnMouseExited(event -> prepareButtonView(leftView, rotatingLeft, 60, 60));
        rotatingRight.setOnMouseExited(event -> prepareButtonView(rightView, rotatingRight, 60, 60));
    }

    public static void prepareButtonView(ImageView buttonView, Button button, int height, int width) {
        buttonView.setFitHeight(height);
        buttonView.setFitWidth(width);
        button.setGraphic(buttonView);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0; -fx-effect: dropshadow(gaussian, black, 10, 0,0,0)");
    }

    private void playPauseEntered(ImageView playHover, ImageView pauseHover) {
        if (simulationRunning) {
            prepareButtonView(pauseHover, playPauseButton, 60, 60);
        }
        else {
            prepareButtonView(playHover, playPauseButton, 60, 60);
        }
    }

    private void playPauseExited(ImageView playView, ImageView pauseView) {
        if (simulationRunning) {
            prepareButtonView(pauseView, playPauseButton, 60, 60);
        }
        else {
            prepareButtonView(playView, playPauseButton, 60, 60);
        }
    }

    public void initialize(Scene scene, EditorController editor, MenuController menu) {
        this.menu = menu;
        this.editor = editor;
        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(this::handleKeyRelease);

        this.timer = new AnimationTimer() {
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
        };
        timer.start();
    }

    public void loadEnvironment(List<String> CSVFile) {
        for (String line : CSVFile) {
            String[] parts = line.split(",");
            switch (parts[0]) {
                case "settings":
                    robotSpeed = Double.parseDouble(parts[1]);
                    robotRadius = Double.parseDouble(parts[2]);
                    obstacleSize = Double.parseDouble(parts[3]);
                    if (Objects.equals(parts[4], "on")) {
                        raceMode = true;
                        toggleRobots.setVisible(false);
                    }
                    else if (Objects.equals(parts[4], "off")) {
                        raceMode = false;
                        ImageView toggleView = new ImageView(new Image(getClass().getResourceAsStream("/toggle.png")));
                        prepareButtonView(toggleView, toggleRobots, 60, 120);
                        ImageView toggleHover = new ImageView(new Image(getClass().getResourceAsStream("/togglehover.png")));
                        toggleRobots.setOnMouseEntered(event -> prepareButtonView(toggleHover, toggleRobots, 60, 120));
                        toggleRobots.setOnMouseExited(event -> prepareButtonView(toggleView, toggleRobots, 60, 120));
                    }
                    break;
                case "controlled_robot":
                    ControlledRobot robot = new ControlledRobot(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), robotRadius);
                    env.addControlledRobot(robot);
                    if (env.countControlledRobots() == 1 && raceMode) {
                        new ControlledRobotView(robot, simulationPane, true, true);
                    }
                    else {
                        new ControlledRobotView(robot, simulationPane, raceMode, false);
                    }
                    break;
                case "obstacle":
                    Obstacle obstacle = new Obstacle(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), obstacleSize);
                    env.addObstacle(obstacle);
                    new ObstacleView(obstacle, simulationPane, raceMode);
                    break;
                case "autonomous_robot":
                    AutonomousRobot autonomousRobot = new AutonomousRobot(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), robotRadius);
                    env.addAutonomousRobot(autonomousRobot);
                    new AutonomousRobotView(autonomousRobot, simulationPane, raceMode, false);
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
            case ESCAPE:
                stopSimulation();
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
                controlledRobot.moveForward(robotSpeed * (controlledRobot.isMovingForward() ? 1 : -1));
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
            System.out.println("Autonomous robot " + index + " rotated to angle " + autonomousRobot.angle().get());
        }
    }

    public void playPauseSimulation() {
        simulationRunning = !simulationRunning;
        if (simulationRunning) {
            System.out.println("Simulation resumed");
            ImageView pauseView = new ImageView(new Image(getClass().getResourceAsStream("/pause.png")));
            prepareButtonView(pauseView, playPauseButton, 60, 60);

        } else {
            System.out.println("Simulation paused");
            ImageView playView = new ImageView(new Image(getClass().getResourceAsStream("/play.png")));
            prepareButtonView(playView, playPauseButton, 60, 60);

        }
    }

    public void stopSimulation() {
        simulationRunning = false;
        timer.stop();
        System.out.println("Simulation stopped");
        if (menu != null) {
            menu.stopGame();
        }
        else if (editor != null) {
            editor.stopGame();
        }
    }

    public void movingForwardPressed() {
        if (env.countControlledRobots() > 0) {
            ControlledRobot robot = env.getControlledRobot(currentRobot);
            robot.setMovingForward(!robot.isMovingForward());
            if (robot.isMovingForward()) {
                System.out.println("Controlled robot " + currentRobot + " moving forward");
            } else {
                System.out.println("Controlled robot " + currentRobot + " stopped moving forward");
            }
        }
    }

    public void rotatingLeftPressed() {
        if (env.countControlledRobots() > 0) {
            ControlledRobot robot = env.getControlledRobot(currentRobot);
            robot.setRotatingLeft(!robot.isRotatingLeft());
            if (robot.isRotatingLeft()) {
                System.out.println("Controlled robot " + currentRobot + " rotating left");
            } else {
                System.out.println("Controlled robot " + currentRobot + " stopped rotating left");
            }
        }
    }

    public void rotatingRightPressed() {
        if (env.countControlledRobots() > 0) {
            ControlledRobot robot = env.getControlledRobot(currentRobot);
            robot.setRotatingRight(!robot.isRotatingRight());
            if (robot.isRotatingRight()) {
                System.out.println("Controlled robot " + currentRobot + " rotating right");
            } else {
                System.out.println("Controlled robot " + currentRobot + " stopped rotating right");
            }
        }
    }

    public void toggleRobotsPressed() {
        if (env.countControlledRobots() > 1) {
            currentRobot = (currentRobot + 1) % env.countControlledRobots();
            System.out.println("Current controlled robot: " + currentRobot);
        }
    }
}
