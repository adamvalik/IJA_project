/**
 * @package ija.other
 * @file Collision.java
 * @class Collision
 *
 * Class for calculating the collision of two shapes.
 *
 * @author Adam Valik
 */

package ija.other;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Collision of two shapes.
 */
public class Collision {

    /**
     * Calculates the collision of two circles.
     * @param c1 Circle 1
     * @param c2 Circle 2
     * @return True if the circles collide, false otherwise
     */
    public static boolean checkCollision(Circle c1, Circle c2) {
        // distance between the centers of the circles is less than the sum of the radii
        return Math.sqrt(Math.pow(c2.getCenterX() - c1.getCenterX(), 2) + Math.pow(c2.getCenterY() - c1.getCenterY(), 2)) <= c1.getRadius() + c2.getRadius();
    }

    /**
     * Calculates the collision of a circle and a rectangle.
     * @param c Circle
     * @param r Rectangle
     * @return True if the circle and the rectangle collide, false otherwise
     */
    public static boolean checkCollision(Circle c, Rectangle r) {
        double closestX = Math.max(r.getX(), Math.min(c.getCenterX(), r.getX() + r.getWidth()));
        double closestY = Math.max(r.getY(), Math.min(c.getCenterY(), r.getY() + r.getHeight()));
        double distance = Math.sqrt(Math.pow(c.getCenterX() - closestX, 2) + Math.pow(c.getCenterY() - closestY, 2));
        // distance between the center of the circle and the closest point on the rectangle is less than the radius of the circle
        return distance < c.getRadius();
    }
}
