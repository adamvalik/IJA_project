package ija.view;

import ija.model.AutonomousRobot;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

import java.util.Random;

public class AutonomousRobotView {
    private Circle visual;
    private final AutonomousRobot robot;
    private boolean menu;

    public AutonomousRobotView(AutonomousRobot robot, Pane parentPane, boolean raceModeOn, boolean menu) {
        this.robot = robot;
        this.menu = menu;
        initializeVisualRepresentation(parentPane);
    }

    private void initializeVisualRepresentation(Pane parentPane) {
        visual = robot.getCircle();

        if (menu) {
            Random rand = new Random();
            if (rand.nextBoolean()) {
                visual.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/ferrari.png"))));
            } else {
                visual.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/autonom.png"))));
            }
        } else {
            visual.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/autonom.png"))));
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