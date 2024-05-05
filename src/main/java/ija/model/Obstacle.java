/**
 * @package ija.model
 * @file Obstacle.java
 * @class Obstacle
 *
 * The Obstacle class represents a static obstacle in the simulation. It is represented by a square.
 *
 * @author Adam Valik
 */

package ija.model;

import javafx.scene.shape.Rectangle;

/**
 * Represents a static obstacle in the simulation
 */
public class Obstacle {
    private final double x; // [x,y] represents the top left corner of the square
    private final double y;
    private final double side; // side length of the square

    /**
     * Constructor for the Obstacle class
     * @param x X coordinate of the obstacle
     * @param y Y coordinate of the obstacle
     * @param side Side length of the obstacle
     */
    public Obstacle(double x, double y, double side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    /**
     * Getter for the X coordinate of the obstacle
     * @return X coordinate of the obstacle
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for the Y coordinate of the obstacle
     * @return Y coordinate of the obstacle
     */
    public double getY() {
        return y;
    }

    /**
     * Get the rectangle representation of the obstacle
     * @return The representation of the obstacle as a square
     */
    public Rectangle getRectangle() {
        return new Rectangle(x, y, side, side);
    }
}
