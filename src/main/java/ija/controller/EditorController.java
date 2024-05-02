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

    @FXML
    private Pane noEntities;

    @FXML
    private Button closeExportSuccess;

    @FXML
    private Pane exportSuccess;
    Label angle = new Label();
    Label rotate = new Label();
    Label detection = new Label();
    TextField angleInput = new TextField();
    TextField rotateInput = new TextField();
    TextField detectionInput = new TextField();

    Pane robotSettings = new Pane();


    Button setButton = new Button();


    private Object selectedEntity;

    public List<String> CSV;

    private boolean wasSet = false;

    private String angleValue;
    private String rotateValue;
    private String detectionValue;

    private String settings;
    private Stage gameStage;

    String type = null;

    private double robotSize;
    private double obstacleSize;

    public String raceMode;
    private final double screenWidth = 1200;
    private final double screenHeight = 650;

    private Double currentClickedPositionX;
    private Double currentClickedPositionY;

    Font fontArial = new Font("Arial Black", 18);
    Font fontArialSmall = new Font("Arial", 12);

    private SettingsController settingsController;

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

    public void initialize(String settings, Double robotSize, Double obstacleSize, SettingsController settingsController) {
        this.settings = settings;
        this.robotSize = robotSize;
        this.obstacleSize = obstacleSize;
        this.settingsController = settingsController;
    }

    private void handleStart(MouseEvent event) {

        CSV.add(0, settings);

        try {
            // Load the game view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));
            Parent gameRoot = loader.load();

            // Get the game controller and initiate the game setup
            GameController gameController = loader.getController();

            // Setup the new stage and scene
            Scene gameScene = new Scene(gameRoot);
            gameStage = new Stage();

            StageSetter.initStage(gameStage, "Game", gameScene, false);

            gameController.initialize(gameScene, this, null);
            gameController.loadEnvironment(CSV);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopGame() {
        if (gameStage != null) {
            gameStage.close();
        }
    }

    private void selectEntity(MouseEvent event) {
        selectedEntity = event.getSource();
        angleInput.setText("");
        rotateInput.setText("");
        detectionInput.setText("");
        map.getChildren().removeAll(robotSettings);
    }

    private void displayRobotSettings(Double x, Double y) {

        map.getChildren().removeAll(robotSettings);
        robotSettings.getChildren().removeAll(angle, rotate, detection, angleInput, rotateInput, detectionInput, setButton);

        if (selectedEntity instanceof Circle) {
            type = ((Circle) selectedEntity).getId();

            if(type.equals("player")){
                addToMap(x,y);
                return;
            }

            Circle newCircle = new Circle(x, y, robotSize);
            if(isCircleColliding(newCircle)){
                return;
            }else if(x+robotSize > screenWidth || x-robotSize < 0 || y+robotSize > screenHeight || y-robotSize < 0){
                return;
            }

        } else if (selectedEntity instanceof Rectangle) {
            addToMap(x,y);
            return;
        }

        robotSettings.setStyle("-fx-background-color: #d6d6d6; -fx-border-color: #202020; -fx-effect:  dropshadow(gaussian, #000, 20, 0, 5, 5); -fx-background-radius: 20px; -fx-border-radius: 20px;");


        // Positioning of dialog window according to clicked position on pane
        if((x + 200 > screenWidth) && (y + 250 > screenHeight)){
            robotSettings.setLayoutX(x - 200);
            robotSettings.setLayoutY(y - 200);

        } else if(x + 200 + robotSize/2 > screenWidth){
            robotSettings.setLayoutX(x - 200);
            robotSettings.setLayoutY(y);

        }else if(y + 250 + robotSize/2 > screenHeight){
            robotSettings.setLayoutX(x);
            robotSettings.setLayoutY(y - 200);

        } else {
            robotSettings.setLayoutX(x);
            robotSettings.setLayoutY(y);
        }

        // This attribute is common for controlled and autonomous robot


            // Set button for autonomous settings


            robotSettings.setPrefSize(200, 250);
            map.getChildren().add(robotSettings);
            robotSettings.getChildren().addAll(angle, rotate, detection, angleInput, rotateInput, detectionInput, setButton);
            wasSet = false;


    }

    private void setValues(MouseEvent event){
        if(type != null && type.equals("bot")){

            if(checkInvalidParameters()){
                System.err.println("Invalid parameters for robot.");
                wasSet = false;
                return;

            }else{
                angleValue = angleInput.getText();
                rotateValue = rotateInput.getText();
                detectionValue = detectionInput.getText();
            }
        }

        wasSet = true;
        addToMap(currentClickedPositionX,currentClickedPositionY);

    }

    private void closeSettings(MouseEvent event){
        map.getChildren().remove(robotSettings);
    }


    private void handleMouseClick(MouseEvent event) {
        // Get the x and y coordinates of the click relative to the Pane
        double x = event.getX();
        double y = event.getY();

        currentClickedPositionX = x;
        currentClickedPositionY = y;

        displayRobotSettings(x, y);

    }

    public void addToMap(double x, double y) {

        if (selectedEntity instanceof Circle) {

            if(x+robotSize > screenWidth || x-robotSize < 0 || y+robotSize > screenHeight || y-robotSize < 0){
                return;
            }

            Circle newCircle = new Circle(x, y, robotSize);
            if(isCircleColliding(newCircle)){
                return;
            }
            String type = ((Circle) selectedEntity).getId();
            // Player
            if (type.equals("player")) {



                if(raceMode.equals("off")) {
                    newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/controll.png"))));
                }else{
                    newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/ferrari.png"))));
                }


                CSV.add("controlled_robot,"+ x + "," + y);
                // Bot
            } else{



                Rotate rotate = new Rotate();
                rotate.setAngle(Double.parseDouble(angleValue));
                rotate.setPivotX(x);
                rotate.setPivotY(y);
                newCircle.getTransforms().add(rotate);


                newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/autonom.png"))));


                if(!wasSet) return;

                CSV.add("autonomous_robot,"+ x + "," + y + "," + angleValue + "," + rotateValue + "," + detectionValue);
            }

            newCircle.setLayoutX(0);
            newCircle.setLayoutY(0);

            map.getChildren().add(newCircle);

        } else if (selectedEntity instanceof Rectangle) {

            x -= obstacleSize/2;
            y -= obstacleSize/2;

            // Obstacle is out of bounds
            if(x+obstacleSize > screenWidth || x < 0 || y+obstacleSize > screenHeight || y < 0){
                return;
            }

            Rectangle newRectangle = new Rectangle(x, y, obstacleSize, obstacleSize);

            if(isRectangleColliding(newRectangle)){
                return;
            }

            if(raceMode.equals("off")) {
                newRectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/wall.png"))));
                newRectangle.setStyle("-fx-effect: dropshadow(gaussian, #3BE03C, 20, 0.6, 0, 0);");
            } else{
                newRectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/racetrack.png"))));
            }
            newRectangle.setLayoutX(0);
            newRectangle.setLayoutY(0);



            map.getChildren().add(newRectangle);

            CSV.add("obstacle," + x + "," + y);

        }
    }

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

    private void clearMap(MouseEvent event){
        map.getChildren().clear();
        map.getChildren().add(noEntities);
        map.getChildren().add(exportSuccess);
        noEntities.setVisible(false);
        exportSuccess.setVisible(false);
        CSV.clear();
    }

    private boolean checkInvalidParameters(){

            // Check angle, rotation, detection
            if(angleInput.getText().isEmpty() || rotateInput.getText().isEmpty() || detectionInput.getText().isEmpty()){
                return true;

            } else if(notNum(angleInput.getText()) || notNum(rotateInput.getText()) || notNum(detectionInput.getText()) ){
                return true;

            } else{
                return false;
            }


    }

    private boolean notNum(String str) {

        return !str.matches("\\d+(\\.\\d+)?");
    }



    public void setMode(String mode){
        this.raceMode = mode;

        if(raceMode.equals("off")) {
            player.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/controll.png"))));
            obstacle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/wall.png"))));
            obstacle.setStyle("-fx-effect: dropshadow(gaussian, #3BE03C, 20, 0.6, 0, 0);");
            bot.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/autonom.png"))));
        }else{
            player.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/ferrari.png"))));
            obstacle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/racetrack.png"))));

            bot.setFill(Color.rgb(214,214,214));

        }
    }

    private void exportCSV(MouseEvent event){

        // File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");

        if(!CSV.isEmpty()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File file = fileChooser.showSaveDialog(settingsController.getEditorStage());
            if (file != null) {
                // Export data to CSV
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
                exportSuccess.setVisible(true);
                exportSuccess.toFront();
            } else {
                System.out.println("File save operation cancelled.");

            }
        }else{
            noEntities.setVisible(true);

        }
    }

    public void backToMenu(MouseEvent event) {
        settingsController.closeEditor();
    }

    private void createLabel(Label label, double sizeX, double sizeY, double posX, double posY, String labelName, Font font){
        label.setPrefSize(sizeX, sizeY);
        label.setLayoutX(posX);
        label.setLayoutY(posY);
        label.setText(labelName);
        label.setFont(font);
    }

    private void createInputField(TextField inputField, double posX, double posY){
        inputField.setPrefSize(100,42);
        inputField.setLayoutX(posX);
        inputField.setLayoutY(posY);
    }

    private void createRobotSettings(){

        createLabel(angle, 37, 34, 20, 20, "Angle", fontArialSmall);
        createLabel(rotate, 37, 34, 20, 80, "Rotate", fontArialSmall);
        createLabel(detection, 120, 34, 20, 140, "Detection", fontArialSmall);

        createInputField(angleInput, 80, 20);
        createInputField(rotateInput, 80, 80);
        createInputField(detectionInput, 80, 140);
    }

    private void createHoverEffect(Button button){
        button.setOnMouseEntered(e -> {button.setStyle(button.getStyle() + "-fx-background-color: #FFEE32;");});
        button.setOnMouseExited(e -> {button.setStyle(button.getStyle() + "-fx-background-color: #FFD100;");});
    }




}
