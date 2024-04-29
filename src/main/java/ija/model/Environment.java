package ija.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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

    private boolean checkEnvironmentBounds(double x, double y, double radius) {
        return x - radius < 0 || x + radius > width || y - radius < 0 || y + radius > height;
    }

    private boolean checkCollision(Circle c1, Circle c2) {
        return Math.sqrt(Math.pow(c2.getCenterX() - c1.getCenterX(), 2) + Math.pow(c2.getCenterY() - c1.getCenterY(), 2)) <= c1.getRadius() + c2.getRadius();
    }

    private boolean checkCollision(Circle c, Rectangle r) {
        double closestX = Math.max(r.getX(), Math.min(c.getCenterX(), r.getX() + r.getWidth()));
        double closestY = Math.max(r.getY(), Math.min(c.getCenterY(), r.getY() + r.getHeight()));
        double distance = Math.sqrt(Math.pow(c.getCenterX() - closestX, 2) + Math.pow(c.getCenterY() - closestY, 2));
        return distance < c.getRadius();
    }

    private boolean checkCollision(Rectangle rectangle1, Rectangle rectangle2) {
        return rectangle1.getBoundsInParent().intersects(rectangle2.getBoundsInParent());
    }

    public boolean checkCollisionAt(ControlledRobot robot, double x, double y) {
        Circle c = robot.getCircle(x, y);
        for (Obstacle o : obstacles) {
            if (checkCollision(c, o.getRectangle())) {
                return true;
            }
        }
        for (ControlledRobot r : controlledRobots) {
            if (r != robot && checkCollision(c, r.getCircle())) {
                return true;
            }
        }
        for (AutonomousRobot r : autonomousRobots) {
            if (checkCollision(c, r.getCircle())) {
                return true;
            }
        }
        return checkEnvironmentBounds(x, y, robot.getRadius());
    }

    public boolean checkCollisionAt(AutonomousRobot robot, double x, double y) {
        Circle c = robot.getCircle(x, y);
        for (Obstacle o : obstacles) {
            if (checkCollision(c, o.getRectangle())) {
                return true;
            }
        }
        for (ControlledRobot r : controlledRobots) {
            if (checkCollision(c, r.getCircle())) {
                return true;
            }
        }
        for (AutonomousRobot r : autonomousRobots) {
            if (r != robot && checkCollision(c, r.getCircle())) {
                return true;
            }
        }
        return checkEnvironmentBounds(x, y, robot.getRadius());
    }

}