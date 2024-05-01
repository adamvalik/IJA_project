package ija.view;

import ija.model.Obstacle;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class ObstacleView {
    private Obstacle obstacle;
    private Rectangle rectangle;
    private final boolean raceModeOn;

    public ObstacleView(Obstacle obstacle, Pane parentPane, boolean raceModeOn) {
        this.obstacle = obstacle;
        this.raceModeOn = raceModeOn;
        initializeView(parentPane);
    }

    private void initializeView(Pane parentPane) {
        // Create a rectangle to visually represent the obstacle
        rectangle = obstacle.getRectangle();
        if (raceModeOn) {
            rectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/racetrack.png"))));
        } else {
            rectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/wall.png"))));
            DropShadow shadow = new DropShadow();
            shadow.setRadius(20);
            shadow.setSpread(0.6);
            shadow.setColor(Color.rgb(59, 224, 60));
            rectangle.setEffect(shadow);
        }
        // Add the rectangle to the parent pane
        parentPane.getChildren().add(rectangle);
    }

    public void updateView(double radius) {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(radius);
        shadow.setColor(Color.rgb(59, 224, 60));
        shadow.setSpread(0.6);
        rectangle.setEffect(shadow);
    }
}
