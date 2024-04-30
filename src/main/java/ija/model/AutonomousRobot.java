package ija.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;


public class AutonomousRobot {
    private final double radius;
    private final double detectionRadius;
    private final double turningAngle;
    private DoubleProperty angle = new SimpleDoubleProperty();
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    public AutonomousRobot(double x, double y, double angle, double turningAngle, double detection) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
        this.radius = 37.5;
        this.detectionRadius = 37.5 + detection;
        this.turningAngle = turningAngle;
    }

    public AutonomousRobot(double x, double y, double angle, double turningAngle, double detection, double radius) {
        this.x.set(x);
        this.y.set(y);
        this.angle.set(angle);
        this.radius = radius;
        this.detectionRadius = radius + detection;
        this.turningAngle = turningAngle;
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

    public Circle getCircle() {
        return new Circle(x.get(), y.get(), radius);
    }

    public Circle getCircle(double x, double y) {
        return new Circle(x, y, radius);
    }

    public Shape getDetectionBoundsAt(double x, double y) {
        Circle robot = getCircle(x, y);
        Polygon detectionArea = new Polygon();
        detectionArea.getPoints().addAll(new Double[] {
                x, y,
                x+detectionRadius, y-radius+1,
                x+detectionRadius, y+radius-1
        });

        Rotate rotate = new Rotate();
        rotate.setAngle(angle.get());
        rotate.setPivotX(x);
        rotate.setPivotY(y);

        detectionArea.setFill(Color.GRAY);

        detectionArea.getTransforms().add(rotate);

        return Shape.union(robot, detectionArea);
    }
}