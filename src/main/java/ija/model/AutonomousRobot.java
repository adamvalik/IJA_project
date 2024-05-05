/**
 * @package ija.model
 * @file AutonomousRobot.java
 * @class AutonomousRobot
 *
 * The AutonomousRobot class represents an autonomous robot in a simulation, extends the Robot class.
 * The robot can rotate by a given angle and has a detection area represented by a triangle pointing in
 * the direction of the robot from its center (long as the given detection radius).
 *
 * @author Adam Valik
 */

package ija.model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Represents an autonomous robot in the simulation
 */
public class AutonomousRobot extends Robot {
    private final double detectionRadius; // radius of the detection area
    private final double turningAngle; // angle by which the robot turns when colliding

    /**
     * Constructor for the AutonomousRobot class
     * @param x X coordinate of the robot
     * @param y Y coordinate of the robot
     * @param angle Initial angle of the robot in degrees
     * @param turningAngle Angle by which the robot turns when colliding
     * @param detection Radius of the detection area
     * @param radius Radius of the robot
     */
    public AutonomousRobot(double x, double y, double angle, double turningAngle, double detection, double radius) {
        super(x, y, angle, radius);
        this.detectionRadius = radius + detection;
        this.turningAngle = turningAngle;
    }

    /**
     * Rotate the robot by the given turning angle
     */
    public void rotate() {
        angle.set((angle.get() + turningAngle) % 360);
    }

    /**
     * Get the detection area of the robot at the given coordinates
     * @param x Potential x coordinate of the robot
     * @param y Potential y coordinate of the robot
     * @return Circle union with the detection area represented by a triangle
     */
    public Shape getDetectionBoundsAt(double x, double y) {
        Circle robot = getCircle(x, y);
        // detection area represented by a triangle pointing in the direction of the robot from its center
        Polygon detectionArea = new Polygon();
        detectionArea.getPoints().addAll(new Double[] {
                x, y,                                // center
                x + detectionRadius, y - radius + 1, // top right
                x + detectionRadius, y + radius - 1  // bottom right
        });
        // rotate the detection area by the angle of the robot
        Rotate rotate = new Rotate();
        rotate.setAngle(angle.get());
        rotate.setPivotX(x);
        rotate.setPivotY(y);
        detectionArea.getTransforms().add(rotate);

        // return the union of the robot and the detection area
        return Shape.union(robot, detectionArea);
    }
}