package ija.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditorController {

    @FXML
    private Circle player;

    @FXML
    private Circle bot;

    @FXML
    private Rectangle obstacle;

    @FXML
    private Pane map;

    @FXML
    private Button start;

    @FXML
    private Button clear;

    @FXML
    private TextField speedInput;
    Label angle = new Label();
    Label rotate = new Label();
    Label detection = new Label();
    TextField angleInput = new TextField();
    TextField rotateInput = new TextField();
    TextField detectionInput = new TextField();

    Button setButton = new Button();

    private Object selectedEntity;

    public List<String> CSV = new ArrayList<>();

    private boolean wasSet = false;

    private String angleValue;
    private String rotateValue;
    private String detectionValue;

    String type = null;

    private final double offset = 37.5;
    private final double screenWidth = 1180;
    private final double screenHeight = 650;

    @FXML
    public void initialize() {
        player.setOnMouseClicked(this::handleElementClick);
        bot.setOnMouseClicked(this::handleElementClick);
        obstacle.setOnMouseClicked(this::handleElementClick);
        map.setOnMouseClicked(this::handleMouseClick);
        clear.setOnMouseClicked(this::clearMap);
        setButton.setOnMouseClicked(this::setValues);
        CSV = new ArrayList<>();
        start.setOnMouseClicked(this::handleStart);
    }

    private void handleStart(MouseEvent event) {
        if (speedInput.getText().isEmpty() || !isNum(speedInput.getText())) {
            return;
        }
        CSV.add("settings," + speedInput.getText());

        try {
            // Load the game view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));
            Parent gameRoot = loader.load();

            // Get the game controller and initiate the game setup
            GameController gameController = loader.getController();

            // Setup the new stage and scene
            Scene gameScene = new Scene(gameRoot);
            Stage gameStage = new Stage();
            gameStage.setTitle("Game Window");
            gameStage.setScene(gameScene);

            gameController.initialize(gameScene);
            gameController.loadEnvironment(CSV);

            // Show the new window
            gameStage.show();
        } catch (IOException e) {
            System.out.println("magore");  // Handle exceptions properly in real applications
        }
    }



    private void handleElementClick(MouseEvent event) {
        selectedEntity = event.getSource();
        angleInput.setText("");
        rotateInput.setText("");
        detectionInput.setText("");
        map.getChildren().removeAll(angle, rotate, detection, angleInput, rotateInput, detectionInput, setButton);


        if(selectedEntity instanceof Circle) {
            type = ((Circle) selectedEntity).getId();
        } else if(selectedEntity instanceof Rectangle) {
            return;
        }

        // Angle label
        angle.setPrefSize(37,34);
        angle.setLayoutX(339);
        angle.setLayoutY(672);
        angle.setText("Angle");

        // Angle input
        angleInput.setPrefSize(101,42);
        angleInput.setLayoutX(307);
        angleInput.setLayoutY(705);

        // Set button
        setButton.setPrefSize(66,42);
        setButton.setLayoutX(750);
        setButton.setLayoutY(705);
        setButton.setText("SET");

        if(type.equals("player")){
            map.getChildren().addAll(angle, angleInput, setButton);
            wasSet = false;
            return;
        }

        if(type.equals("bot")){
            // Rotate label
            rotate.setPrefSize(37,34);
            rotate.setLayoutX(500);
            rotate.setLayoutY(672);
            rotate.setText("Rotate");

            // Detection label
            detection.setPrefSize(120,34);
            detection.setLayoutX(627);
            detection.setLayoutY(672);
            detection.setText("Obstacle detection");

            // Rotate input
            rotateInput.setPrefSize(101,42);
            rotateInput.setLayoutX(468);
            rotateInput.setLayoutY(705);

            // Detection input
            detectionInput.setPrefSize(101,42);
            detectionInput.setLayoutX(627);
            detectionInput.setLayoutY(705);
        }

        map.getChildren().addAll(angle, rotate, detection, angleInput, rotateInput, detectionInput, setButton);
        wasSet = false;
    }

    private void setValues(MouseEvent event){
        if(type.equals("bot")){

            if(checkInvalidParameters("bot")){
                System.out.println("NELZE PICO BOT");
                System.out.println(angleInput.getText());
                wasSet = false;
                return;

            }else{
                angleValue = angleInput.getText();
                rotateValue = rotateInput.getText();
                detectionValue = detectionInput.getText();
            }
        } else if(type.equals("player")){

            if(checkInvalidParameters("player")){
                System.out.println("NELZE PICO PLAYER");
                wasSet = false;
                return;

            }else{
                angleValue = angleInput.getText();
            }
        }
        wasSet = true;
    }


    private void handleMouseClick(MouseEvent event) {
        // Get the x and y coordinates of the click relative to the Pane
        double x = event.getX();
        double y = event.getY();
        addToMap(x,y);

    }



    // Optionally, you can provide a method to retrieve the clicked element
    public Object getSelectedEntity() {
        return selectedEntity;
    }

    public void addToMap(double x, double y) {

        boolean checkSquares;
        if(x+offset > screenWidth || x-offset < 0 || y+offset > screenHeight || y-offset < 0){
            return;
        }

        Node newEntity = null;
        if (selectedEntity instanceof Circle) {

            checkSquares = true;
            if(isColliding(x, y, offset, checkSquares)){
                return;
            }

            String type = ((Circle) selectedEntity).getId();
            // Player
            if (type.equals("player")) {
                newEntity = new Circle(offset);
                ((Circle) newEntity).setFill(Color.BLUE);

                if(!wasSet) return;

                CSV.add("controlled_robot,"+ x + "," + y + "," + angleValue);
                // Bot
            } else{
                newEntity = new Circle(offset);
                ((Circle) newEntity).setFill(Color.RED);

                if(!wasSet) return;

                CSV.add("autonomous_robot,"+ x + "," + y + "," + angleValue + "," + rotateValue + "," + detectionValue);
            }

        } else if (selectedEntity instanceof Rectangle) {

            checkSquares = false;
            if(isColliding(x, y, offset, checkSquares)){
                return;
            }

            newEntity = new Rectangle(offset*2, offset*2);
            ((Rectangle) newEntity).setFill(Color.YELLOW);
            x -= offset;
            y -= offset;

            CSV.add("obstacle," + x + "," + y);

        }

        newEntity.setLayoutX(x);
        newEntity.setLayoutY(y);
        // Add the new entity to the Pane
        map.getChildren().add(newEntity);
    }

    public boolean isColliding(double centerX, double centerY, double radius, boolean checkSquares) {
        for (Node entity : map.getChildren()) {
            if (entity instanceof Circle) {
                Circle circle = (Circle) entity;

                double dx = Math.abs(centerX - circle.getLayoutX());
                double dy = Math.abs(centerY - circle.getLayoutY());
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < radius * 2) {
                    return true;
                }
            } else if (entity instanceof Rectangle && checkSquares) {
                Rectangle rectangle = (Rectangle) entity;

                // Calculate closest point on the rectangle to the circle
                double closestX = clamp(centerX, rectangle.getLayoutX(), rectangle.getLayoutX() + rectangle.getWidth());
                double closestY = clamp(centerY, rectangle.getLayoutY(), rectangle.getLayoutY() + rectangle.getHeight());

                // Calculate distance between circle center and closest point
                double dx = centerX - closestX;
                double dy = centerY - closestY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < radius) {
                    return true;
                }
            }

        }
        return false;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }


    private void clearMap(MouseEvent event){
        map.getChildren().clear();
        CSV.clear();
    }

    private boolean checkInvalidParameters(String type){
        if(type.equals("bot")){

            // Check angle, rotation, detection
            if(angleInput.getText().isEmpty() || rotateInput.getText().isEmpty() || detectionInput.getText().isEmpty()){
                return true;

            } else if(!isNum(angleInput.getText()) || !isNum(rotateInput.getText()) || !isNum(detectionInput.getText()) ){
                return true;

            } else if(!isInRange(angleInput.getText())){
                return true;

            } else{
                return false;
            }

            // Check only angle
        } else if(type.equals("player")){
            if(angleInput.getText().isEmpty() || !isNum(angleInput.getText()) || !isInRange(angleInput.getText())){
                return true;

            }else{
                return false;
            }
        }
        return false;
    }

    private boolean isNum(String str) {

        return str.matches("\\d+(\\.\\d+)?");
    }

    private boolean isInRange(String str) {
        double number = Double.parseDouble(str);
        return number >= 0 && number <= 359;
    }





}
