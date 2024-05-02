/**
 * @package ija.view
 * @file ControlledRobotView.java
 * @class ControlledRobotView
 *
 * The class for visual representation of the controlled robot.
 *
 * @author Adam Valík
 */

package ija.view;

import ija.model.ControlledRobot;
import ija.other.ImageSetter;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

/**
 * The class for visual representation of the controlled robot
 */
public class ControlledRobotView {

    /**
     * Creates the visual representation of the controlled robot
     * @param robot The controlled robot
     * @param parentPane The pane where the robot will be displayed
     * @param raceModeOn Whether the image should be set to robot or car
     * @param ferrari Whether the formula car image should be set to Ferrari or Red Bull
     */
    public ControlledRobotView(ControlledRobot robot, Pane parentPane, boolean raceModeOn, boolean ferrari) {
        Circle visual = robot.getCircle();

        // set the correct image to the robot
        if (raceModeOn) {
            if (ferrari) {
                visual.setFill(ImageSetter.setImagePattern("/ferrari.png"));
            } else {
                visual.setFill(ImageSetter.setImagePattern("/redbull.png"));
            }
        } else {
            visual.setFill(ImageSetter.setImagePattern("/control.png"));
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