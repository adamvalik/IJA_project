/**
 * @package ija.controller
 * @file EditorController.java
 * @class EditorController
 *
 * Controller for editor window. Allows user to create a map for the game.
 *
 * @author Dominik Horut
 */

package ija.controller;

import ija.model.Collision;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Controller for editor window. Allows user to create a map for the game.
 */
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
    private Button export;

    @FXML
    private Button back;

    @FXML
    private TextField speedInput;

    @FXML
    private AnchorPane window;

    @FXML
    private Button closeNoEntities;

    /**
     * Pop-up window to inform user about exporting empty map.
      */
    @FXML
    private Pane noEntities;

    @FXML
    private Button closeExportSuccess;

    /**
     * Pop-up window to inform user about successful export of map.
     */
    @FXML
    private Pane exportSuccess;

    // Robot settings window components.
    Label angle = new Label();
    Label rotate = new Label();
    Label detection = new Label();
    TextField angleInput = new TextField();
    TextField rotateInput = new TextField();
    TextField detectionInput = new TextField();

    Pane robotSettings = new Pane();

    Button setButton = new Button();

    /**
     * Entity user wants to add to game map.
     */
    private Object selectedEntity;

    /**
     * Contains information about entities on the map for game controller.
     */
    public List<String> CSV;

    /**
     * Flag for checking if autonomous robot settings were set.
     */
    private boolean wasSet = false;

    // Autonomous robot parameters
    private String angleValue;
    private String rotateValue;
    private String detectionValue;

    // General settings of a game
    private String settings;
    private Stage gameStage;

    String type = null;

    private double robotSize;
    private double obstacleSize;

    public String raceMode;

    // Editor window size
    private final double screenWidth = 1200;
    private final double screenHeight = 650;


    private Double currentClickedPositionX;
    private Double currentClickedPositionY;

    Font fontArial = new Font("Arial Black", 18);
    Font fontArialSmall = new Font("Arial", 12);

    private SettingsController settingsController;

    /**
     * Initializes the editor window, binds buttons to functions and sets up their style.
     */
    @FXML
    public void initialize() {

        player.setOnMouseClicked(this::selectEntity);
        bot.setOnMouseClicked(this::selectEntity);
        obstacle.setOnMouseClicked(this::selectEntity);
        map.setOnMouseClicked(this::handleMouseClick);
        clear.setOnMouseClicked(this::clearMap);
        start.setOnMouseClicked(this::handleStart);
        export.setOnMouseClicked(this::exportCSV);
        back.setOnMouseClicked(this::backToMenu);
        noEntities.setVisible(false);
        exportSuccess.setVisible(false);

        CSV = new ArrayList<>();

        createRobotSettings();

        setButton.setOnMouseClicked(event -> {
            setValues(event);
            closeSettings(event);
        });

        // Pop up windows settings
        noEntities.toFront();
        closeNoEntities.setOnMouseClicked(event -> {
            noEntities.setVisible(false);
        });

        exportSuccess.toFront();
        closeExportSuccess.setOnMouseClicked(event -> {
            exportSuccess.setVisible(false);
        });

        // Disable focus on buttons
        clear.setFocusTraversable(false);
        start.setFocusTraversable(false);
        export.setFocusTraversable(false);
        back.setFocusTraversable(false);
        closeNoEntities.setFocusTraversable(false);
        closeExportSuccess.setFocusTraversable(false);

        ImageView trash = new ImageView(new Image(getClass().getResourceAsStream("/clear.png")));
        ImageView trashHover = new ImageView(new Image(getClass().getResourceAsStream("/clearhover.png")));
        GameController.prepareButtonView(trash, clear, 75, 100);

        // Hover effect for buttons
        createHoverEffect(start);
        createHoverEffect(clear);
        createHoverEffect(export);
        createHoverEffect(back);
        createHoverEffect(closeNoEntities);
        createHoverEffect(closeExportSuccess);

        clear.setOnMouseEntered(e -> {GameController.prepareButtonView(trashHover, clear, 75, 100);});
        clear.setOnMouseExited(e -> {GameController.prepareButtonView(trash, clear, 75, 100);});

        setButton.setPrefSize(100,42);
        setButton.setLayoutX(50);
        setButton.setLayoutY(190);
        setButton.setText("SET");
        setButton.setFont(fontArial);

        setButton.setStyle("-fx-background-color: #FFD100; -fx-border-color: #202020; -fx-border-radius: 17px; -fx-background-radius: 20px; -fx-border-width: 3px; -fx-border-insets: -1px");

        setButton.setOnMouseEntered(e -> {
            setButton.setStyle("-fx-background-color: #FFEE32; -fx-border-color: #202020; -fx-border-radius: 17px; -fx-background-radius: 20px; -fx-border-width: 3px; -fx-border-insets: -1px");
        });
        setButton.setOnMouseExited(e -> {
            setButton.setStyle("-fx-background-color: #FFD100; -fx-border-color: #202020; -fx-border-radius: 17px; -fx-background-radius: 20px; -fx-border-width: 3px; -fx-border-insets: -1px");
        });

    }

    /**
     * Initializes the editor window with given settings.
     * @param settings General settings of game.
     * @param robotSize global size of robots.
     * @param obstacleSize global size of obstacles.
     * @param settingsController Settings controller.
     */
    public void initialize(String settings, Double robotSize, Double obstacleSize, SettingsController settingsController) {
        this.settings = settings;
        this.robotSize = robotSize;
        this.obstacleSize = obstacleSize;
        this.settingsController = settingsController;
    }

    /**
     * Starts the game with the current map settings and entities layout.
     * @param event Mouse event which triggered game start.
     */
    private void handleStart(MouseEvent event) {

        CSV.add(0, settings);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));
            Parent gameRoot = loader.load();

            // Get the game controller and initiate the game setup
            GameController gameController = loader.getController();

            // Set up the new stage and scene
            Scene gameScene = new Scene(gameRoot);
            gameStage = new Stage();
            StageSetter.initStage(gameStage, "Game", gameScene, false);

            gameController.initialize(gameScene, this, null);
            gameController.loadEnvironment(CSV);

        } catch (IOException e) {
            System.err.println("Error when loading game.");
        }
    }

    /**
     * Closes game window.
     */
    public void stopGame() {
        if (gameStage != null) {
            gameStage.close();
        }
    }

    /**
     * Sets clicked entity from entity list as selected.
     * @param event Mouse event which triggered entity selection.
     */
    private void selectEntity(MouseEvent event) {
        selectedEntity = event.getSource();

        // Delete previous autonomous robot settings
        angleInput.setText("");
        rotateInput.setText("");
        detectionInput.setText("");

        map.getChildren().removeAll(robotSettings);
    }

    /**
     * Displays robot settings window to allow user to set autonomous robot new parameters.
     * @param x X coordinate of where robot should be placed.
     * @param y Y coordinate of where robot should be placed.
     */
    private void displayRobotSettings(Double x, Double y) {

        //Close previous settings window, if any opened.
        map.getChildren().removeAll(robotSettings);
        robotSettings.getChildren().removeAll(angle, rotate, detection, angleInput, rotateInput, detectionInput, setButton);

        // Check, which entity will be placed to map.
        if (selectedEntity instanceof Circle) {
            type = ((Circle) selectedEntity).getId();

            // No need to display settings
            if(type.equals("player")) {
                addToMap(x,y);
                return;
            }

            Circle newCircle = new Circle(x, y, robotSize);

            // Check if robot is not colliding with other entities on map or is not placed outside the map.
            if(isCircleColliding(newCircle)) {
                return;
            } else if(x+robotSize > screenWidth || x-robotSize < 0 || y+robotSize > screenHeight || y-robotSize < 0) {
                return;
            }

            // No need to display settings
        } else if (selectedEntity instanceof Rectangle) {
            addToMap(x,y);
            return;
        }

        robotSettings.setStyle("-fx-background-color: #d6d6d6; -fx-border-color: #202020; -fx-effect:  dropshadow(gaussian, #000, 20, 0, 5, 5); -fx-background-radius: 20px; -fx-border-radius: 20px;");

        // Positioning of dialog window according to clicked position on pane
        // Window overflows at bottom-right corner
        if((x + 200 > screenWidth) && (y + 250 > screenHeight)) {
            robotSettings.setLayoutX(x - 200);
            robotSettings.setLayoutY(y - 200);

        // Window overflows at right edge
        } else if(x + 200 + robotSize/2 > screenWidth) {
            robotSettings.setLayoutX(x - 200);
            robotSettings.setLayoutY(y);

        // Window overflows at bottom edge
        } else if(y + 250 + robotSize/2 > screenHeight) {
            robotSettings.setLayoutX(x);
            robotSettings.setLayoutY(y - 200);

        // Window does not overflow
        } else {
            robotSettings.setLayoutX(x);
            robotSettings.setLayoutY(y);
        }

        robotSettings.setPrefSize(200, 250);
        map.getChildren().add(robotSettings);
        robotSettings.getChildren().addAll(angle, rotate, detection, angleInput, rotateInput, detectionInput, setButton);

        // Set the flag to false until setting will be set
        wasSet = false;
    }

    /**
     * Sets values of autonomous robot parameters and checks if they are valid.
     * @param event Mouse event which triggered setting values.
     */
    private void setValues(MouseEvent event) {
        if(type != null && type.equals("bot")) {

            if(checkInvalidParameters()) {
                System.err.println("Invalid parameters for robot.");
                wasSet = false;
                return;

            } else {
                angleValue = angleInput.getText();
                rotateValue = rotateInput.getText();
                detectionValue = detectionInput.getText();
            }
        }

        // Flag is set to true to allow adding robot to map
        wasSet = true;

        // Robot can be added to map
        addToMap(currentClickedPositionX,currentClickedPositionY);
    }

    /**
     * Closes autonomous robot settings window.
     * @param event Mouse event which triggered closing settings.
     */
    private void closeSettings(MouseEvent event){
        map.getChildren().remove(robotSettings);
    }

    /**
     * Handles the user click on map.
     * @param event Mouse event which triggered click on map.
     */
    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        currentClickedPositionX = x;
        currentClickedPositionY = y;

        displayRobotSettings(x, y);
    }

    /**
     * Adds entity to map and displays it.
     * @param x X coordinate where entity will be displayed.
     * @param y Y coordinate where entity will be displayed.
     */
    public void addToMap(double x, double y) {

        if (selectedEntity instanceof Circle) {

            // Check if robot is not colliding with other entities on map or is not placed outside the map.
            if(x+robotSize > screenWidth || x-robotSize < 0 || y+robotSize > screenHeight || y-robotSize < 0){
                return;
            }
            Circle newCircle = new Circle(x, y, robotSize);

            if(isCircleColliding(newCircle)){
                return;
            }

            String type = ((Circle) selectedEntity).getId();

            // Player to be added
            if (type.equals("player")) {

                // Choosing style for player depending on mode
                if(raceMode.equals("off")) {
                    newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/controll.png"))));
                } else {
                    newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/ferrari.png"))));
                }

                CSV.add("controlled_robot,"+ x + "," + y);
            // Autonomous robot to be added
            } else{
                // Rotating robot by desired angle
                Rotate rotate = new Rotate();
                rotate.setAngle(Double.parseDouble(angleValue));
                rotate.setPivotX(x);
                rotate.setPivotY(y);
                newCircle.getTransforms().add(rotate);

                // Setting style
                newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/autonom.png"))));

                // Check that robot parameters were set
                if(!wasSet) return;

                CSV.add("autonomous_robot,"+ x + "," + y + "," + angleValue + "," + rotateValue + "," + detectionValue);
            }

            // Adding controlled or autonomous robot to map
            newCircle.setLayoutX(0);
            newCircle.setLayoutY(0);
            map.getChildren().add(newCircle);

        } else if (selectedEntity instanceof Rectangle) {

            // Getting square center
            x -= obstacleSize/2;
            y -= obstacleSize/2;

            // Check if obstacle is not colliding with other entities on map or is not placed outside the map.
            if(x+obstacleSize > screenWidth || x < 0 || y+obstacleSize > screenHeight || y < 0) {
                return;
            }

            Rectangle newRectangle = new Rectangle(x, y, obstacleSize, obstacleSize);

            if(isRectangleColliding(newRectangle)) {
                return;
            }

            // Setting obstacle style depending on mode
            if(raceMode.equals("off")) {
                newRectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/wall.png"))));
                newRectangle.setStyle("-fx-effect: dropshadow(gaussian, #3BE03C, 20, 0.6, 0, 0);");
            } else {
                newRectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/racetrack.png"))));
            }

            // Adding obstacle to map
            newRectangle.setLayoutX(0);
            newRectangle.setLayoutY(0);
            map.getChildren().add(newRectangle);

            CSV.add("obstacle," + x + "," + y);

        }
    }

    /**
     * Checks if new robot is colliding with other entities on map.
     * @param newCircle Robot to be checked.
     * @return True if robot is colliding, false otherwise.
     */
    public boolean isCircleColliding(Circle newCircle) {
        for (Node entity : map.getChildren()) {
            if (entity instanceof Circle) {

                if (Collision.checkCollision(newCircle, (Circle) entity)) {
                    return true;
                }
            } else if (entity instanceof Rectangle) {
                if (Collision.checkCollision(newCircle, (Rectangle) entity)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Checks if new obstacle is colliding with other entities on map.
     * @param newRectangle Obstacle to be checked.
     * @return True if obstacle is colliding, false otherwise.
     */
    public boolean isRectangleColliding(Rectangle newRectangle) {
        for (Node entity : map.getChildren()) {
            if (entity instanceof Circle) {
                if (Collision.checkCollision((Circle) entity, newRectangle)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Clears the map of all entities.
     * @param event Mouse event which triggered map clearing.
     */
    private void clearMap(MouseEvent event) {
        map.getChildren().clear();
        map.getChildren().add(noEntities);
        map.getChildren().add(exportSuccess);
        noEntities.setVisible(false);
        exportSuccess.setVisible(false);
        CSV.clear();
    }

    /**
     * Checks if parameters for autonomous robot are valid.
     * @return True if parameters are invalid, false otherwise.
     */
    private boolean checkInvalidParameters(){

        // Check that all parameters are filled
        if(angleInput.getText().isEmpty() || rotateInput.getText().isEmpty() || detectionInput.getText().isEmpty()) {
            return true;

        // Check that all parameters are numbers
        } else if(notNum(angleInput.getText()) || notNum(rotateInput.getText()) || notNum(detectionInput.getText()) ) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * Checks if string is not a number.
     * @param str String to be checked.
     * @return True if string is not a number, false otherwise.
     */
    private boolean notNum(String str) { return !str.matches("\\d+(\\.\\d+)?"); }

    /**
     * Sets visuals of editor window according to mode.
     * @param mode Mode of the game (robot or racing).
     */
    public void setMode(String mode) {
        this.raceMode = mode;

        // Robot mode set
        if(raceMode.equals("off")) {
            player.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/controll.png"))));
            obstacle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/wall.png"))));
            obstacle.setStyle("-fx-effect: dropshadow(gaussian, #3BE03C, 20, 0.6, 0, 0);");
            bot.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/autonom.png"))));

        // Racing mode set
        } else {
            player.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/ferrari.png"))));
            obstacle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/racetrack.png"))));
            bot.setFill(Color.rgb(214,214,214));
        }
    }

    /**
     * Exports map to CSV file.
     * @param event Mouse event which triggered map export.
     */
    private void exportCSV(MouseEvent event){

        // File chooser opening
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");

        // Ensure that map is not empty
        if(!CSV.isEmpty()) {

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(settingsController.getEditorStage());

            if (file != null) {

                // Write the map settings into .csv file line after line
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(settings);
                    writer.newLine();

                    for (String line : CSV) {
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("CSV file exported successfully.");
                // Telling user that map was successfully exported
                exportSuccess.setVisible(true);
                exportSuccess.toFront();
            } else {
                System.out.println("File save operation cancelled.");
            }
        // Telling user that map is empty and cannot be exported
        } else {
            noEntities.setVisible(true);
        }
    }

    /**
     * Closes editor window and returns to main menu.
     * @param event Mouse event which triggered back to menu.
     */
    public void backToMenu(MouseEvent event) {
        settingsController.closeEditor();
    }

    /**
     * Creates label with given parameters in robot settings window.
     * @param label Label to be created.
     * @param sizeX Width of label.
     * @param sizeY Height of label.
     * @param posX X coordinate of label.
     * @param posY Y coordinate of label.
     * @param labelName Name of label.
     * @param font Font of label.
     */
    private void createLabel(Label label, double sizeX, double sizeY, double posX, double posY, String labelName, Font font){
        label.setPrefSize(sizeX, sizeY);
        label.setLayoutX(posX);
        label.setLayoutY(posY);
        label.setText(labelName);
        label.setFont(font);
    }

    /**
     * Creates input field with given parameters in robot settings window.
     * @param inputField Input field to be created.
     * @param posX X coordinate of input field.
     * @param posY Y coordinate of input field.
     */
    private void createInputField(TextField inputField, double posX, double posY){
        inputField.setPrefSize(100,42);
        inputField.setLayoutX(posX);
        inputField.setLayoutY(posY);
    }

    /**
     * Creates robot settings window.
     */
    private void createRobotSettings(){

        createLabel(angle, 37, 34, 20, 20, "Angle", fontArialSmall);
        createLabel(rotate, 37, 34, 20, 80, "Rotate", fontArialSmall);
        createLabel(detection, 120, 34, 20, 140, "Detection", fontArialSmall);

        createInputField(angleInput, 80, 20);
        createInputField(rotateInput, 80, 80);
        createInputField(detectionInput, 80, 140);
    }

    /**
     * Creates hover effect for button.
     * @param button Button to set hover effect to.
     */
    private void createHoverEffect(Button button){
        button.setOnMouseEntered(e -> {button.setStyle(button.getStyle() + "-fx-background-color: #FFEE32;");});
        button.setOnMouseExited(e -> {button.setStyle(button.getStyle() + "-fx-background-color: #FFD100;");});
    }
}
