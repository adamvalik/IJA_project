package ija.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Environment {
    private final int width;
    private final int height;
    private ObservableList<ControlledRobot> controlledRobots;
    private ObservableList<AutonomousRobot> autonomousRobots;
    private List<Obstacle> obstacles;

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.controlledRobots = FXCollections.observableArrayList();
        this.autonomousRobots = FXCollections.observableArrayList();
        this.obstacles = new ArrayList<>();
    }

    public ControlledRobot getControlledRobot(int index) {
        return controlledRobots.get(index);
    }

    public ObservableList<ControlledRobot> getControlledRobots() {
        return controlledRobots;
    }

    public int countControlledRobots() {
        return controlledRobots.size();
    }

    public void addControlledRobot(ControlledRobot robot) {
        controlledRobots.add(robot);
    }

    public AutonomousRobot getAutonomousRobot(int index) {
        return autonomousRobots.get(index);
    }

    public ObservableList<AutonomousRobot> getAutonomousRobots() {
        return autonomousRobots;
    }

    public int countAutonomousRobots() {
        return autonomousRobots.size();
    }

    public void addAutonomousRobot(AutonomousRobot robot) {
        autonomousRobots.add(robot);
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public boolean checkCollision(ControlledRobot robot) {
        // check for obstacles
        for (Obstacle obstacle : obstacles) {
            if (robot.getBounds().intersects(obstacle.getX(), obstacle.getY(), obstacle.getSideLen(), obstacle.getSideLen())) {
                return true;
            }
        }

        // check for other controlled robots
        for (ControlledRobot otherRobot : controlledRobots) {
            if (robot != otherRobot && robot.getBounds().intersects(otherRobot.getBounds().getBoundsInLocal())) {
                return true;
            }
        }

        // check for autonomous robots
        for (AutonomousRobot autonomousRobot : autonomousRobots) {
            if (robot.getBounds().intersects(autonomousRobot.getBounds().getBoundsInLocal())) {
                return true;
            }
        }

        if (checkEnvironmentBounds(robot.X().get(), robot.Y().get(), robot.getRadius())) {
            return true;
        }

        return false; // no collision
    }

    public boolean checkCollision(AutonomousRobot robot) {
        // check for obstacles
        for (Obstacle obstacle : obstacles) {
            if (robot.getDetectionBounds().intersects(obstacle.getX(), obstacle.getY(), obstacle.getSideLen(), obstacle.getSideLen())) {
                return true;
            }
        }

        // check for other controlled robots
        for (ControlledRobot controlledRobot : controlledRobots) {
            if (robot.getDetectionBounds().intersects(controlledRobot.getBounds().getBoundsInLocal())) {
                return true;
            }
        }

        // check for autonomous robots
        for (AutonomousRobot otherRobot : autonomousRobots) {
            if (robot != otherRobot && robot.getDetectionBounds().intersects(otherRobot.getBounds().getBoundsInLocal())) {
                return true;
            }
        }

        if (checkEnvironmentBounds(robot.X().get(), robot.Y().get(), robot.getDetectionRadius())) {
            return true;
        }

        return false; // no collision
    }

    private boolean checkEnvironmentBounds(double x, double y, double radius) {
        return x - radius < 0 || x + radius > width || y - radius < 0 || y + radius > height;
    }
}

