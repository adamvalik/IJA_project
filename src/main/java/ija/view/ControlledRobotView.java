package ija.view;

import ija.model.ControlledRobot;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

public class ControlledRobotView {
    private Circle visual;
    private final ControlledRobot robot;
    private final boolean raceModeOn;

    public ControlledRobotView(ControlledRobot robot, Pane parentPane, boolean raceModeOn) {
        this.robot = robot;
        this.raceModeOn = raceModeOn;
        initializeVisualRepresentation(parentPane);
    }

    private void initializeVisualRepresentation(Pane parentPane) {
        visual = robot.getCircle();

        if (raceModeOn) {
            visual.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/formula-1.png"))));
        } else {
            visual.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/controll.jpg"))));
        }

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