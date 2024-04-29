package ija.model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Collision {
    public static boolean checkCollision(Circle c1, Circle c2) {
        return Math.sqrt(Math.pow(c2.getCenterX() - c1.getCenterX(), 2) + Math.pow(c2.getCenterY() - c1.getCenterY(), 2)) <= c1.getRadius() + c2.getRadius();
    }

    public static boolean checkCollision(Circle c, Rectangle r) {
        double closestX = Math.max(r.getX(), Math.min(c.getCenterX(), r.getX() + r.getWidth()));
        double closestY = Math.max(r.getY(), Math.min(c.getCenterY(), r.getY() + r.getHeight()));
        double distance = Math.sqrt(Math.pow(c.getCenterX() - closestX, 2) + Math.pow(c.getCenterY() - closestY, 2));
        return distance < c.getRadius();
    }

    public static boolean checkCollision(Rectangle rectangle1, Rectangle rectangle2) {
        return rectangle1.getBoundsInParent().intersects(rectangle2.getBoundsInParent());
    }

}
