/**
 * @package ija.view
 * @file ObstacleView.java
 * @class ObstacleView
 *
 * The class for visual representation of the obstacle robot.
 *
 * @author Adam Valík
 */

package ija.view;

import ija.model.Obstacle;
import ija.other.ImageSetter;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The class for visual representation of the obstacle robot
 */
public class ObstacleView {

    /**
     * Creates the visual representation of the obstacle
     * @param obstacle The obstacle
     * @param parentPane The pane where the obstacle will be displayed
     * @param raceModeOn Whether the image should be set to racetrack or wall
     */
    public ObstacleView(Obstacle obstacle, Pane parentPane, boolean raceModeOn) {
        Rectangle rectangle = obstacle.getRectangle();

        // set the correct image to the obstacle
        if (raceModeOn) {
            rectangle.setFill(ImageSetter.setImagePattern("/racetrack.png"));
        } else {
            rectangle.setFill(ImageSetter.setImagePattern("/wall.png"));
            // add a shadow effect to the wall
            DropShadow shadow = new DropShadow();
            shadow.setRadius(20);
            shadow.setSpread(0.6);
            shadow.setColor(Color.rgb(59, 224, 60)); // toxic green
            rectangle.setEffect(shadow);
        }

        // add the visual representation to the parent pane
        parentPane.getChildren().add(rectangle);
    }
}
