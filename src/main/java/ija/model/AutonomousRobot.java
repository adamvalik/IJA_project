package ija.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

public class AutonomousRobot {
    private final double radius;
    private final double detectionRadius;
    private final double turningAngle;
    private DoubleProperty angle = new SimpleDoubleProperty();
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    public AutonomousRobot(double x, double y, double angle, double turningAngle, double detectionRadius) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
        this.detectionRadius = 37.5 + detectionRadius;
        this.turningAngle = turningAngle;
        this.radius = 37.5;
    }

    public AutonomousRobot(double x, double y, double angle, double turningAngle, double detectionRadius, double radius) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
        this.detectionRadius = radius + detectionRadius;
        this.turningAngle = turningAngle;
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

    public double getDetectionRadius() {
        return detectionRadius;
    }

    public void moveForward(double speed) {
        double angleInRadians = Math.toRadians(angle.get());

        x.set(x.get() + speed * Math.cos(angleInRadians));
        y.set(y.get() + speed * Math.sin(angleInRadians));
    }

    public void rotate() {
        angle.set(angle.get() + turningAngle % 360);
    }

    //TODO: detectionRadius cannot be less then radius
    public Circle getBounds() {
        return new Circle(x.get(), y.get(), radius);
    }

    public Circle getDetectionBounds() {
        return new Circle(x.get(), y.get(), detectionRadius);
    }
}

