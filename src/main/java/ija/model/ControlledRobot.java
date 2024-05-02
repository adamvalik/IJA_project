/**
 * @package ija.model
 * @file ControlledRobot.java
 * @class ControlledRobot
 *
 * The ControlledRobot class represents a robot that can be controlled by the user in a simulation, extends
 * the Robot class. The robot can move forward, backward, rotate left and right.
 *
 * @author Adam Valik
 */

package ija.model;

/**
 * Represents a robot that can be controlled by the user
 */
public class ControlledRobot extends Robot {
    // flags for movement and rotation
    private boolean movingForward = false;
    private boolean movingBackward = false;
    private boolean rotatingRight = false;
    private boolean rotatingLeft = false;
    private final int rotationSensitivity = 5;

    /**
     * @param x X coordinate of the robot
     * @param y Y coordinate of the robot
     * @param radius Radius of the robot
     */
    public ControlledRobot(double x, double y, double radius) {
        super(x, y, 0, radius);
    }

    /**
     * Check if the robot is moving forward
     * @return True if it is, false otherwise
     */
    public boolean isMovingForward() {
        return movingForward;
    }

    /**
     * Set the robot to move forward
     * @param movingForward True if the robot should move forward, false otherwise
     */
    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
    }

    /**
     * Check if the robot is moving backward
     * @return True if it is, false otherwise
     */
    public boolean isMovingBackward() {
        return movingBackward;
    }

    /**
     * Set the robot to move backward
     * @param movingBackward True if the robot should move backward, false otherwise
     */
    public void setMovingBackward(boolean movingBackward) {
        this.movingBackward = movingBackward;
    }

    /**
     * Check if the robot is rotating right
     * @return True if it is, false otherwise
     */
    public boolean isRotatingRight() {
        return rotatingRight;
    }

    /**
     * Set the robot to rotate right
     * @param rotatingRight True if the robot should rotate right, false otherwise
     */
    public void setRotatingRight(boolean rotatingRight) {
        this.rotatingRight = rotatingRight;
    }

    /**
     * Check if the robot is rotating left
     * @return True if it is, false otherwise
     */
    public boolean isRotatingLeft() {
        return rotatingLeft;
    }

    /**
     * Set the robot to rotate left
     * @param rotatingLeft True if the robot should rotate left, false otherwise
     */
    public void setRotatingLeft(boolean rotatingLeft) {
        this.rotatingLeft = rotatingLeft;
    }

    /**
     * Rotate the robot to the right
     */
    public void rotateRight() {
        angle.set(angle.get() + rotationSensitivity % 360);
    }

    /**
     * Rotate the robot to the left
     */
    public void rotateLeft() {
        angle.set(angle.get() - rotationSensitivity % 360);
    }
}

