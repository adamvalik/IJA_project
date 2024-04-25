package ija.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Environment {
    private final int width = 1180;
    private final int height = 650;
    private ObservableList<ControlledRobot> controlledRobots;
    private ObservableList<AutonomousRobot> autonomousRobots;
    private List<Obstacle> obstacles;

    public Environment() {
        this.controlledRobots = FXCollections.observableArrayList();
        this.obstacles = new ArrayList<>();
    }

    public ObservableList<ControlledRobot> getControlledRobots() {
        return controlledRobots;
    }

    public void addControlledRobot(ControlledRobot robot) {
            controlledRobots.add(robot);
    }

    public void addAutonomousRobot(AutonomousRobot robot) {
            autonomousRobots.add(robot);
    }

    public ObservableList<AutonomousRobot> getAutonomousRobots() {
        return autonomousRobots;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public boolean checkCollision(ControlledRobot robot) {
        for (Obstacle obstacle : obstacles) {
            // Directly using the Rectangle intersects method if getBounds() returns a Rectangle
            if (robot.getBounds().intersects(obstacle.getBounds().getX(), obstacle.getBounds().getY(), obstacle.getBounds().getWidth(), obstacle.getBounds().getHeight())) {
                return true; // Collision detected
            }
        }

        double robotX = robot.X().get();
        double robotY = robot.Y().get();
        double robotRadius = robot.getRadius(); // Assuming the robot has a method getRadius() that returns its size

        // Check left boundary
        if (robotX - robotRadius < 0) return true;

        // Check right boundary
        if (robotX + robotRadius > width) return true;

        // Check top boundary
        if (robotY - robotRadius < 0) return true;

        // Check bottom boundary
        if (robotY + robotRadius > height) return true;


        return false; // No collision
    }

}
