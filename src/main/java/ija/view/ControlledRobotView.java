package ija.view;

import ija.model.ControlledRobot;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class ControlledRobotView {
    private Circle visual;
    private final ControlledRobot robot;
    private Line direction;

    public ControlledRobotView(ControlledRobot robot, Pane parentPane) {
        this.robot = robot;
        initializeVisualRepresentation(parentPane);
    }

    private void initializeVisualRepresentation(Pane parentPane) {
        // Create a visual representation, e.g., a circle or an image
        visual = new Circle(robot.getRadius());
        // fill the circle with an image djk.jpg in resources
        visual.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/djk.jpg"))));

        // Bind the circle's center to the robot's position
        visual.centerXProperty().bind(robot.X());
        visual.centerYProperty().bind(robot.Y());

        // Create a rotation transform and bind its angle to the robot's direction property
        Rotate rotate = new Rotate();
        // Bind the pivot points to the circle's center
        rotate.pivotXProperty().bind(visual.centerXProperty());
        rotate.pivotYProperty().bind(visual.centerYProperty());
        // Assuming the robot class has a directionProperty() that returns a DoubleProperty
        rotate.angleProperty().bind(robot.angle());

        // Add the rotation transform to the circle
        visual.getTransforms().add(rotate);



        parentPane.getChildren().add(visual);
    }


}