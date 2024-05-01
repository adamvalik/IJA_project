package ija.model;

import javafx.beans.property.IntegerProperty;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private final double x; // [x,y] represents the top left corner of the square
    private final double y;
    private final double side;

    public Obstacle(double x, double y) {
        this.x = x;
        this.y = y;
        this.side = 75;
    }

    public Obstacle(double x, double y, double side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSideLen() {
        return side;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, side, side);
    }
}
