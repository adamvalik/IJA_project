/**
 * @package ija.model
 * @file Robot.java
 * @class Robot
 *
 * The super class for all robots in the simulation. Holds common properties and methods.
 *
 * @author Adam Valik
 */

package ija.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

/**
 * The common super class for all robots in the simulation.
 */
abstract public class Robot {
    /**
     * Radius of the robot
     */
    protected double radius;
    /**
     * X coordinate of the center of the robot
     */
    protected final DoubleProperty x = new SimpleDoubleProperty();
    /**
     * Y coordinate of the center of the robot
     */
    protected final DoubleProperty y = new SimpleDoubleProperty();
    /**
     * Angle of the robot in degrees
     */
    protected final DoubleProperty angle = new SimpleDoubleProperty();

    /**
     * Common constructor for the Robot class
     * @param x X coordinate of the robot
     * @param y Y coordinate of the robot
     * @param angle Initial angle of the robot in degrees
     * @param radius Radius of the robot
     */
    protected Robot(double x, double y, double angle, double radius) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
        this.radius = radius;
    }

    /**
     * Getter for the X coordinate of the robot
     * @return X coordinate of the robot
     */
    public DoubleProperty X() {
        return x;
    }

    /**
     * Getter for the Y coordinate of the robot
     * @return Y coordinate of the robot
     */
    public DoubleProperty Y() {
        return y;
    }

    /**
     * Getter for the angle of the robot
     * @return Angle of the robot in degrees
     */
    public DoubleProperty angle() {
        return angle;
    }

    /**
     * Getter for the radius of the robot
     * @return Radius of the robot
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Move the robot forward by the given speed
     * @param speed Speed of the robot
     */
    public void moveForward(double speed) {
        // transfer the angle in degrees to radians
        double angleInRadians = Math.toRadians(angle.get());

        // trigonometry to calculate the new position
        x.set(x.get() + speed * Math.cos(angleInRadians)); // x += dx
        y.set(y.get() + speed * Math.sin(angleInRadians)); // y += dy
    }

    /**
     * Get robot's object representation
     * @return Circle representing the robot
     */
    public Circle getCircle() {
        return new Circle(x.get(), y.get(), radius);
    }

    /**
     * Get robot's object representation at the given position
     * @param x X coordinate of the robot
     * @param y Y coordinate of the robot
     * @return Circle representing the robot
     */
    public Circle getCircle(double x, double y) {
        return new Circle(x, y, radius);
    }
}
