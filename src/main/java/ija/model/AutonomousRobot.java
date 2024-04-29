package ija.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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

    public Circle getBounds() {
        return new Circle(x.get(), y.get(), radius);
    }

    public Circle getBoundsAt(double x, double y) {
        return new Circle(x, y, radius);
    }


    /**
     * @param x X coordinates of the center of the robot
     * @param y Y coordinates of the center of the robot
     * @return
     */
    public Rectangle getDetectionBoundsAt(double x, double y) {
        double width = detectionRadius;
        double height = 2 * radius;
        Rectangle detectionArea = new Rectangle(x, y - radius, width, height);

        Rotate rotate = new Rotate();
        rotate.setAngle(angle.get());
        rotate.setPivotX(x);
        rotate.setPivotY(y);

        detectionArea.setFill(Color.RED);

        detectionArea.getTransforms().add(rotate);

        return detectionArea;
    }

    public Rectangle getDetectionBounds() {
        double width = detectionRadius;
        double height = 2 * radius;
        Rectangle detectionArea = new Rectangle(x.get(), y.get() - radius, width, height);

        Rotate rotate = new Rotate();
        rotate.setAngle(angle.get());
        rotate.setPivotX(x.get());
        rotate.setPivotY(y.get());

        detectionArea.setFill(Color.RED);

        detectionArea.getTransforms().add(rotate);

        return detectionArea;
    }

}

