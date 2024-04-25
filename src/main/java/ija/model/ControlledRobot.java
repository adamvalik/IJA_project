package ija.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

public class ControlledRobot {
    private final double radius = 37.5;
    private DoubleProperty angle = new SimpleDoubleProperty();
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    public ControlledRobot(double x, double y, double angle) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
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

    public void moveForward(double speed) {
        double angleInRadians = Math.toRadians(angle.get());

        x.set(x.get() + speed * Math.cos(angleInRadians));
        y.set(y.get() + speed * Math.sin(angleInRadians));
    }

    public void rotate(double turn) {
        angle.set(angle.get() + turn % 360);
    }

    public Circle getBounds() {
        return new Circle(x.get(), y.get(), radius);
    }
}

