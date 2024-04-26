package ija.view;

import ija.model.Obstacle;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class ObstacleView {
    private Obstacle obstacle;
    private Rectangle rectangle;

    public ObstacleView(Obstacle obstacle, Pane parentPane) {
        this.obstacle = obstacle;
        initializeView(parentPane);
    }

    private void initializeView(Pane parentPane) {
        // Create a rectangle to visually represent the obstacle
        rectangle = obstacle.getBounds();
        rectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/jaj.jpg"))));

        // Add the rectangle to the parent pane
        parentPane.getChildren().add(rectangle);
    }
}
