package ija.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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

    public boolean checkCollisionAt(ControlledRobot robot, double x, double y) {
        Circle c = robot.getCircle(x, y);
        for (Obstacle o : obstacles) {
            if (Collision.checkCollision(c, o.getRectangle())) {
                return true;
            }
        }
        for (ControlledRobot r : controlledRobots) {
            if (r != robot && Collision.checkCollision(c, r.getCircle())) {
                return true;
            }
        }
        for (AutonomousRobot r : autonomousRobots) {
            if (Collision.checkCollision(c, r.getCircle())) {
                return true;
            }
        }
        return c.getCenterX() - c.getRadius() < 0 || c.getCenterX() + c.getRadius() > width || c.getCenterY() - c.getRadius() < 0 || c.getCenterY() + c.getRadius() > height;
    }

    public boolean checkCollisionAt(AutonomousRobot robot, double x, double y) {
        Shape detectionBounds = robot.getDetectionBoundsAt(x, y);
        Shape intersect;
        for (Obstacle o : obstacles) {
            intersect = Shape.intersect(detectionBounds, o.getRectangle());
            if (!intersect.getBoundsInLocal().isEmpty()) {
                return true;
            }
        }
        for (ControlledRobot r : controlledRobots) {
            intersect = Shape.intersect(detectionBounds, r.getCircle());
            if (!intersect.getBoundsInLocal().isEmpty()) {
                return true;
            }
        }
        for (AutonomousRobot r : autonomousRobots) {
            if (r != robot) {
                intersect = Shape.intersect(detectionBounds, r.getCircle());
                if (!intersect.getBoundsInLocal().isEmpty()) {
                    return true;
                }
            }
        }
        Bounds bounds = detectionBounds.getBoundsInLocal();
        return bounds.getMinX() < 0 || bounds.getMaxX() > width || bounds.getMinY() < 0 || bounds.getMaxY() > height;
    }
}