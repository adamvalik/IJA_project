package ija.view;

import ija.model.AutonomousRobot;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class AutonomousRobotView {
    private Circle visual;
    private final AutonomousRobot robot;

    public AutonomousRobotView(AutonomousRobot robot, Pane parentPane, boolean raceModeOn) {
        this.robot = robot;
        initializeVisualRepresentation(parentPane);
    }

    private void initializeVisualRepresentation(Pane parentPane) {
        visual = robot.getCircle();

        visual.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/robot.jpg"))));

        visual.centerXProperty().bind(robot.X());
        visual.centerYProperty().bind(robot.Y());

        Rotate rotate = new Rotate();
        rotate.pivotXProperty().bind(visual.centerXProperty());
        rotate.pivotYProperty().bind(visual.centerYProperty());
        rotate.angleProperty().bind(robot.angle());

        visual.getTransforms().add(rotate);




        parentPane.getChildren().add(visual);
    }


}