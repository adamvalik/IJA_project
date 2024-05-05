/**
 * @package ija.model
 * @file Environment.java
 * @class Environment
 *
 * The Environment class represents the environment in which the simulation takes place. It holds all
 * the robots, obstacles and implements the collision detection methods.
 *
 * @author Adam Valik
 */

package ija.model;

import ija.other.Collision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the environment in which the simulation takes place
 */
public class Environment {
    private final int width;
    private final int height;
    // lists of robots and obstacles
    private final ObservableList<ControlledRobot> controlledRobots = FXCollections.observableArrayList();
    private final ObservableList<AutonomousRobot> autonomousRobots = FXCollections.observableArrayList();
    private final List<Obstacle> obstacles = new ArrayList<>();

    /**
     * Constructor for the Environment class
     * @param width Width of the environment
     * @param height Height of the environment
     */
    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
    }


    /**
     * Get the controlled robot at the given index
     * @param index Index of the controlled robot
     * @return Controlled robot at the given index
     */
    public ControlledRobot getControlledRobot(int index) {
        return controlledRobots.get(index);
    }

    /**
     * Get the number of controlled robots in the environment
     * @return Number of controlled robots
     */
    public int countControlledRobots() {
        return controlledRobots.size();
    }

    /**
     * Add a controlled robot to the environment
     * @param robot Controlled robot to add
     */
    public void addControlledRobot(ControlledRobot robot) {
        controlledRobots.add(robot);
    }

    /**
     * Get the autonomous robot at the given index
     * @param index Index of the autonomous robot
     * @return Autonomous robot at the given index
     */
    public AutonomousRobot getAutonomousRobot(int index) {
        return autonomousRobots.get(index);
    }

    /**
     * Get the list of autonomous robots
     * @return List of autonomous robots
     */
    public ObservableList<AutonomousRobot> getAutonomousRobots() {
        return autonomousRobots;
    }

    /**
     * Get the number of autonomous robots in the environment
     * @return Number of autonomous robots
     */
    public int countAutonomousRobots() {
        return autonomousRobots.size();
    }

    /**
     * Add an autonomous robot to the environment
     * @param robot Autonomous robot to add
     */
    public void addAutonomousRobot(AutonomousRobot robot) {
        autonomousRobots.add(robot);
    }

    /**
     * Add an obstacle to the environment
     * @param obstacle Obstacle to add
     */
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }


    /**
     * Check if the controlled robot collides with an obstacle or another robot at given coordinates
     * @param robot Controlled robot to check collision for
     * @param x X coordinate of the robot
     * @param y Y coordinate of the robot
     * @return True if the robot collides with an obstacle, false otherwise
     */
    public boolean checkCollisionAt(ControlledRobot robot, double x, double y) {
        boolean result = false;
        Circle c = robot.getCircle(x, y);
        for (Obstacle o : obstacles) {
            if (Collision.checkCollision(c, o.getRectangle())) {
                result = true;
                break;
            }
        }
        if (!result) {
            for (ControlledRobot r : controlledRobots) {
                if (r != robot && Collision.checkCollision(c, r.getCircle())) {
                    result = true;
                    break;
                }
            }
            if (!result) {
                for (AutonomousRobot r : autonomousRobots) {
                    if (Collision.checkCollision(c, r.getCircle())) {
                        result = true;
                        break;
                    }
                }
                if (!result) {
                    result = c.getCenterX() - c.getRadius() < 0 || c.getCenterX() + c.getRadius() > width || c.getCenterY() - c.getRadius() < 0 || c.getCenterY() + c.getRadius() > height;
                }
            }
        }
        return result;
    }

    /**
     * Check if the autonomous robot collides with an obstacle or another robot at given coordinates
     * @param robot Autonomous robot to check collision for
     * @param x X coordinate of the robot
     * @param y Y coordinate of the robot
     * @return True if the robot collides with an obstacle, false otherwise
     */
    public boolean checkCollisionAt(AutonomousRobot robot, double x, double y) {
        boolean result = false;
        Shape detectionBounds = robot.getDetectionBoundsAt(x, y);
        Shape intersect;
        for (Obstacle o : obstacles) {
            intersect = Shape.intersect(detectionBounds, o.getRectangle());
            if (!intersect.getBoundsInLocal().isEmpty()) {
                result = true;
                break;
            }
        }
        if (!result) {
            for (ControlledRobot r : controlledRobots) {
                intersect = Shape.intersect(detectionBounds, r.getCircle());
                if (!intersect.getBoundsInLocal().isEmpty()) {
                    result = true;
                    break;
                }
            }
            if (!result) {
                for (AutonomousRobot r : autonomousRobots) {
                    if (r != robot) {
                        intersect = Shape.intersect(detectionBounds, r.getCircle());
                        if (!intersect.getBoundsInLocal().isEmpty()) {
                            result = true;
                            break;
                        }
                    }
                }
                if (!result) {
                    Bounds bounds = detectionBounds.getBoundsInLocal();
                    result = bounds.getMinX() < 0 || bounds.getMaxX() > width || bounds.getMinY() < 0 || bounds.getMaxY() > height;
                }
            }
        }
        return result;
    }
}