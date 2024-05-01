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

        player.setOnMouseClicked(this::handleElementClick);
        bot.setOnMouseClicked(this::handleElementClick);
        obstacle.setOnMouseClicked(this::handleElementClick);
        map.setOnMouseClicked(this::handleMouseClick);
        clear.setOnMouseClicked(this::clearMap);
        CSV = new ArrayList<>();
        start.setOnMouseClicked(this::handleStart);
        export.setOnMouseClicked(this::exportCSV);
        back.setOnMouseClicked(this::backToMenu);
        noEntities.setVisible(false);
        exportSuccess.setVisible(false);
        noEntities.toFront();
        exportSuccess.toFront();


        setButton.setOnMouseClicked(event -> {
            setValues(event);
            closeSettings(event);
        });

        closeNoEntities.setOnMouseClicked(event -> {
            noEntities.setVisible(false);
        });

        closeExportSuccess.setOnMouseClicked(event -> {
            exportSuccess.setVisible(false);
        });

        clear.setFocusTraversable(false);
        start.setFocusTraversable(false);
        export.setFocusTraversable(false);
        back.setFocusTraversable(false);

        ImageView trash = new ImageView(new Image(getClass().getResourceAsStream("/clear.png")));
        ImageView trashHover = new ImageView(new Image(getClass().getResourceAsStream("/clearhover.png")));
        GameController.prepareButtonView(trash, clear, 75, 100);


        // Hover effect for buttons
        start.setOnMouseEntered(e -> {start.setStyle(start.getStyle() + "-fx-background-color: #FFEE32;");});
        start.setOnMouseExited(e -> {start.setStyle(start.getStyle() + "-fx-background-color: #FFD100;");});

        clear.setOnMouseEntered(e -> {clear.setStyle(clear.getStyle() + "-fx-background-color: #FFEE32;");});
        clear.setOnMouseExited(e -> {clear.setStyle(clear.getStyle() + "-fx-background-color: #FFD100;");});

        export.setOnMouseEntered(e -> {export.setStyle(export.getStyle() + "-fx-background-color: #FFEE32;");});
        export.setOnMouseExited(e -> {export.setStyle(export.getStyle() + "-fx-background-color: #FFD100;");});

        back.setOnMouseEntered(e -> {back.setStyle(back.getStyle() + "-fx-background-color: #FFEE32;");});
        back.setOnMouseExited(e -> {back.setStyle(back.getStyle() + "-fx-background-color: #FFD100;");});

        closeNoEntities.setOnMouseEntered(e -> {closeNoEntities.setStyle(closeNoEntities.getStyle() + "-fx-background-color: #FFEE32;");});
        closeNoEntities.setOnMouseExited(e -> {closeNoEntities.setStyle(closeNoEntities.getStyle() + "-fx-background-color: #FFD100;");});

        closeExportSuccess.setOnMouseEntered(e -> {closeExportSuccess.setStyle(closeExportSuccess.getStyle() + "-fx-background-color: #FFEE32;");});
        closeExportSuccess.setOnMouseExited(e -> {closeExportSuccess.setStyle(closeExportSuccess.getStyle() + "-fx-background-color: #FFD100;");});

        clear.setOnMouseEntered(e -> {GameController.prepareButtonView(trashHover, clear, 75, 100);});
        clear.setOnMouseExited(e -> {GameController.prepareButtonView(trash, clear, 75, 100);});

        setButton.setOnMouseEntered(e -> {setButton.setStyle("-fx-background-color: #FFEE32; -fx-border-color: #202020; -fx-border-radius: 17px; -fx-background-radius: 20px; -fx-border-width: 3px; -fx-border-insets: -1px");
            ;});
        setButton.setOnMouseExited(e -> {setButton.setStyle("-fx-background-color: #FFD100; -fx-border-color: #202020; -fx-border-radius: 17px; -fx-background-radius: 20px; -fx-border-width: 3px; -fx-border-insets: -1px");
            ;});

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
            gameStage.setTitle("Game Window");
            gameStage.setScene(gameScene);

            gameController.initialize(gameScene, this, null);
            gameController.loadEnvironment(CSV);

            gameStage.setResizable(false);


            // Show the new window
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopGame() {
        if (gameStage != null) {
            gameStage.close();
        }
    }

    private void handleElementClick(MouseEvent event) {
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
        // Angle label
        angle.setPrefSize(37,34);
        angle.setLayoutX(20);
        angle.setLayoutY(20);
        angle.setText("Angle");
        angle.setFont(fontArialSmall);

        // Angle input
        angleInput.setPrefSize(101,42);
        angleInput.setLayoutX(80);
        angleInput.setLayoutY(20);

            // Rotate label
            rotate.setPrefSize(37,34);
            rotate.setLayoutX(20);
            rotate.setLayoutY(80);
            rotate.setText("Rotate");
        rotate.setFont(fontArialSmall);

            // Detection label
            detection.setPrefSize(120,34);
            detection.setLayoutX(20);
            detection.setLayoutY(140);
            detection.setText("Detection");
        detection.setFont(fontArialSmall);

            // Rotate input
            rotateInput.setPrefSize(101,42);
            rotateInput.setLayoutX(80);
            rotateInput.setLayoutY(80);

            // Detection input
            detectionInput.setPrefSize(101,42);
            detectionInput.setLayoutX(80);
            detectionInput.setLayoutY(140);

            // Set button for autonomous settings
            setButton.setPrefSize(100,42);
            setButton.setLayoutX(50);
            setButton.setLayoutY(190);
            setButton.setText("SET");
            setButton.setStyle("-fx-background-color: #FFD100; -fx-border-color: #202020; -fx-border-radius: 17px; -fx-background-radius: 20px; -fx-border-width: 3px; -fx-border-insets: -1px");
            setButton.setFont(fontArial);

            robotSettings.setPrefSize(200, 250);
            map.getChildren().add(robotSettings);
            robotSettings.getChildren().addAll(angle, rotate, detection, angleInput, rotateInput, detectionInput, setButton);
            wasSet = false;


    }

    private void setValues(MouseEvent event){
        if(type != null && type.equals("bot")){

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

    public Object getSelectedEntity() {
        return selectedEntity;
    }

    public void addToMap(double x, double y) {

        Node newEntity = null;

        if (selectedEntity instanceof Circle) {

            if(x+robotSize > screenWidth || x-robotSize < 0 || y+robotSize > screenHeight || y-robotSize < 0){
                return;
            }
            String type = ((Circle) selectedEntity).getId();
            // Player
            if (type.equals("player")) {

                Circle newCircle = new Circle(x, y, robotSize);
                if(isCircleColliding(newCircle)){
                    return;
                }

                if(raceMode.equals("off")) {
                    newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/controll.png"))));
                }else{
                    newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/ferrari.png"))));
                }
                newCircle.setLayoutX(0);
                newCircle.setLayoutY(0);

                map.getChildren().add(newCircle);

                CSV.add("controlled_robot,"+ x + "," + y);
                // Bot
            } else{

                Circle newCircle = new Circle(x, y, robotSize);

                Rotate rotate = new Rotate();
                rotate.setAngle(Double.parseDouble(angleValue));
                rotate.setPivotX(x);
                rotate.setPivotY(y);
                newCircle.getTransforms().add(rotate);


                newCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/autonom.png"))));
                newCircle.setLayoutX(0);
                newCircle.setLayoutY(0);

                if(isCircleColliding(newCircle)){
                    return;
                }
                if(!wasSet) return;
                map.getChildren().add(newCircle);
                CSV.add("autonomous_robot,"+ x + "," + y + "," + angleValue + "," + rotateValue + "," + detectionValue);
            }

        } else if (selectedEntity instanceof Rectangle) {

            x -= obstacleSize/2;
            y -= obstacleSize/2;

            // Obstacle is out of bounds
            if(x+obstacleSize > screenWidth || x < 0 || y+obstacleSize > screenHeight || y < 0){
                return;
            }

            Rectangle newRectangle = new Rectangle(x, y, obstacleSize, obstacleSize);

            if(raceMode.equals("off")) {
                newRectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/wall.png"))));
                newRectangle.setStyle("-fx-effect: dropshadow(gaussian, #3BE03C, 20, 0.6, 0, 0);");
            } else{
                newRectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/racetrack.png"))));
            }
            newRectangle.setLayoutX(0);
            newRectangle.setLayoutY(0);

            if(isRectangleColliding(newRectangle)){
                return;
            }

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

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }


    private void clearMap(MouseEvent event){
        map.getChildren().clear();
        map.getChildren().add(noEntities);
        map.getChildren().add(exportSuccess);
        noEntities.setVisible(false);
        exportSuccess.setVisible(false);
        CSV.clear();
    }

    private boolean checkInvalidParameters(String type){
        if(type.equals("bot")){

            // Check angle, rotation, detection
            if(angleInput.getText().isEmpty() || rotateInput.getText().isEmpty() || detectionInput.getText().isEmpty()){
                return true;

            } else if(!isNum(angleInput.getText()) || !isNum(rotateInput.getText()) || !isNum(detectionInput.getText()) ){
                return true;

            } else{
                return false;
            }
        }
        return false;
    }

    private boolean isNum(String str) {

        return str.matches("\\d+(\\.\\d+)?");
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
            // Show file save dialog
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
            System.out.println("SEtting errror window");

            noEntities.setVisible(true);

        }
    }

    public void backToMenu(MouseEvent event) {
        settingsController.closeEditor();
    }


}
