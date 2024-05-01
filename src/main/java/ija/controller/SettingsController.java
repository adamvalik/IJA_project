package ija.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private TextField robotSpeed;

    @FXML
    private TextField obstacleSize;

    @FXML
    private TextField robotSize;

    @FXML
    private ToggleButton racingMode;
    @FXML
    private Button setter;
    private String racingModeValue = "off";

    private String csvHeader;
    private MenuController menu;
    private Stage editorStage;

    @FXML
    public void initialize() {
        racingMode.setOnMouseClicked(this::setRacingMode);
        setter.setOnMouseClicked(this::setSettings);
    }

    public void initialize(MenuController menu) {
        this.menu = menu;
    }

    private void setRacingMode(MouseEvent event) {
        if (racingMode.isSelected()) {
            // Toggle is ON
            racingMode.setText("ON");
            racingModeValue = "on";
        } else {
            // Toggle is OFF
            racingMode.setText("OFF");
            racingModeValue = "off";
        }
    }

    private void setSettings(MouseEvent event){
        if(checkInvalidParameters()){
            System.out.println("Nelze");

        }else {
            csvHeader = "settings," + robotSpeed.getText() + "," + robotSize.getText() + "," + obstacleSize.getText() + "," + racingModeValue;
            System.out.println(csvHeader);
            startEditor();
        }
    }

    private void startEditor(){
        try {
            // Load the game view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("editor-view.fxml"));
            Parent editorRoot = loader.load();

            // Get the game controller and initiate the game setup
            EditorController editorController = loader.getController();
            editorController.initialize(csvHeader, Double.parseDouble(robotSize.getText()), Double.parseDouble(obstacleSize.getText()), this);
            editorController.setMode(racingModeValue);
            // Setup the new stage and scene
            Scene editorScene = new Scene(editorRoot);
            editorStage = new Stage();
            editorStage.setTitle("Game Editor");
            editorStage.setScene(editorScene);
            editorStage.setResizable(false);
            editorController.initialize();

            menu.closeSettings();
            editorStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeEditor() {
        if (editorStage != null) {
            editorStage.close();
        }
    }

    public Stage getEditorStage() {
        return editorStage;
    }

    private boolean checkInvalidParameters(){

        // Check angle, rotation, detection
        if(robotSpeed.getText().isEmpty() || robotSize.getText().isEmpty() || obstacleSize.getText().isEmpty()){
            return true;

        } else if(!isNum(robotSpeed.getText()) || !isNum(robotSize.getText()) || !isNum(obstacleSize.getText()) ){
            return true;

        } else{
            return false;
        }
    }

    private boolean isNum(String str) {

        return str.matches("\\d+(\\.\\d+)?");
    }


}
