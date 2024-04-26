package ija.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

public class ControlledRobot {
    private final double radius;
    private DoubleProperty angle = new SimpleDoubleProperty();
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private boolean movingForward = false;
    private boolean rotatingRight = false;
    private boolean rotatingLeft = false;

    public ControlledRobot(double x, double y, double angle) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
        this.radius = 37.5;
    }

    public ControlledRobot(double x, double y, double angle, double radius) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
        this.radius = radius;
    }

    public DoubleProperty X() {
        return x;
    }

    public DoubleProperty Y() {
        return y;
    }

    public DoubleProperty angle() {
        return angle;
    }

    public double getRadius() {
        return radius;
    }

    public boolean isMovingForward() {
        return movingForward;
    }

    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
    }

    public boolean isRotatingRight() {
        return rotatingRight;
    }

    public void setRotatingRight(boolean rotatingRight) {
        this.rotatingRight = rotatingRight;
    }

    public boolean isRotatingLeft() {
        return rotatingLeft;
    }

    public void setRotatingLeft(boolean rotatingLeft) {
        this.rotatingLeft = rotatingLeft;
    }

    public void moveForward(double speed) {
        double angleInRadians = Math.toRadians(angle.get());

        x.set(x.get() + speed * Math.cos(angleInRadians));
        y.set(y.get() + speed * Math.sin(angleInRadians));
    }

    //TODO: 3 degrees is a small enough angle to simulate a smooth rotation, test this value and adjust accordingly
    public void rotateRight() {
        angle.set(angle.get() + 3 % 360);
    }

    public void rotateLeft() {
        angle.set(angle.get() - 3 % 360);
    }

    public Circle getBounds() {
        return new Circle(x.get(), y.get(), radius);
    }
}

