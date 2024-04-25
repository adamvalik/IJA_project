package ija.model;

import java.awt.*;

public class Obstacle {
    private double x;
    private double y;
    private final double width = 75;
    private final double height = 75;

    public Obstacle(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }
}
