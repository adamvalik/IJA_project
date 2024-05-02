/**
 * @package ija.view
 * @file AutonomousRobotView.java
 * @class AutonomousRobotView
 *
 * The class for visual representation of the autonomous robot.
 *
 * @author Adam Valík
 */
package ija.view;

import ija.model.AutonomousRobot;
import ija.other.ImageSetter;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

/**
 * The class for visual representation of the autonomous robot.
 */
public class AutonomousRobotView {

    /**
     * Creates the visual representation of the autonomous robot
     * @param robot The autonomous robot
     * @param parentPane The pane where the robot will be displayed
     * @param raceModeOn Whether the image should be set to robot or car
     */
    public AutonomousRobotView(AutonomousRobot robot, Pane parentPane, boolean raceModeOn) {
        Circle visual = robot.getCircle();

        // set the correct image to the robot
        if (raceModeOn) {
            visual.setFill(ImageSetter.setImagePattern("/ferrari.png"));
        } else {
            visual.setFill(ImageSetter.setImagePattern("/autonom.png"));
        }

        // bind the visual representation to the robot's properties (javaFX internal observing mechanism)
        visual.centerXProperty().bind(robot.X());
        visual.centerYProperty().bind(robot.Y());

        // rotate the visual representation according to the robot's angle
        Rotate rotate = new Rotate();
        rotate.pivotXProperty().bind(visual.centerXProperty());
        rotate.pivotYProperty().bind(visual.centerYProperty());
        rotate.angleProperty().bind(robot.angle());
        visual.getTransforms().add(rotate);

        // add the visual representation to the parent pane
        parentPane.getChildren().add(visual);
    }
}