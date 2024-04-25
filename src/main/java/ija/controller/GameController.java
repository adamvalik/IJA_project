package ija.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
public class GameController {

    @FXML
    private Circle circle;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void handleKeyPress(KeyEvent event) {
        System.out.println("Key Pressed: " + event.getCode());
        if (event.getCode() == KeyCode.LEFT) {
            // Move the AnchorPane to the left by 50 pixels
            double newX = anchorPane.getLayoutX() - 50;
            anchorPane.setLayoutX(newX);
        } else if (event.getCode() == KeyCode.RIGHT) {
            // Move the AnchorPane to the right by 50 pixels
            double newX = anchorPane.getLayoutX() + 50;
            anchorPane.setLayoutX(newX);
        } else if (event.getCode() == KeyCode.UP) {
            // Move the AnchorPane up by 50 pixels
            double newY = anchorPane.getLayoutY() - 50;
            anchorPane.setLayoutY(newY);
        } else if (event.getCode() == KeyCode.DOWN) {
            // Move the AnchorPane down by 50 pixels
            double newY = anchorPane.getLayoutY() + 50;
            anchorPane.setLayoutY(newY);
        }
    }
}
