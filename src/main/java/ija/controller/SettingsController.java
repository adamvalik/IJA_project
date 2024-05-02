/**
 * @package ija.controller
 * @file SettingsController.java
 * @class SettingsController
 *
 * Controller for settings window.
 *
 * @author Dominik Horut
 */
package ija.controller;

import ija.other.ImageSetter;
import ija.other.StageSetter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for settings window.
 */
public class SettingsController {

    /**
     * User input field for setting robot speed
     */
    @FXML
    private TextField robotSpeed;

    /**
     * User input field for setting obstacle size
     */
    @FXML
    private TextField obstacleSize;

    /**
     * User input field for setting robot size
     */
    @FXML
    private TextField robotSize;

    /**
     * Button for switching between robot mode and race mode
     */
    @FXML
    private Button mode;

    /**
     * Button to confirm settings
     */
    @FXML
    private Button setter;
    private String racingModeValue = "off";

    /**
     * / Contains all information received from settings.
     */
    private String csvHeader;
    private MenuController menu;
    private Stage editorStage;


    /**
     * Initializes the controller by binding all buttons in settings to appropriate methods
     * and sets style of certain buttons.
     */
    @FXML
    public void initialize() {
        mode.setOnMouseClicked(this::switchMode);
        setter.setOnMouseClicked(this::setSettings);


        setter.setOnMouseEntered(e -> {setter.setStyle(setter.getStyle() + "-fx-background-color: #FFEE32;");});
        setter.setOnMouseExited(e -> {setter.setStyle(setter.getStyle() + "-fx-background-color: #FFD100;");});

        ImageView robot = ImageSetter.setImageView("/robotmode.png");
        GameController.prepareButtonView(robot, mode, 60, 60);
        mode.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
    }

    public void initialize(MenuController menu) {
        this.menu = menu;
    }

    /**
     * Toggles between normal (robot) mode and racing mode.
     * Sets chosen mode and also changes style of settings window.
     * @param event Mouse event which triggered mode switch.
     */
    private void switchMode(MouseEvent event) {

        if(racingModeValue.equals("on")) {
            racingModeValue = "off";

        } else {
            racingModeValue = "on";
        }

        // Racing mode is ON
        if (racingModeValue.equals("on")) {

            ImageView formula = ImageSetter.setImageView("/ferrari.png");
            GameController.prepareButtonView(formula, mode, 60, 60);
            mode.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");

            racingModeValue = "on";
        // Racing mode is OFF
        } else {

            ImageView robot = ImageSetter.setImageView("/robotmode.png");
            GameController.prepareButtonView(robot, mode, 60, 60);
            mode.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");

            racingModeValue = "off";
        }
    }

    /**
     * Sets game settings to be passed to a game controller or exported.
     * @param event Mouse event which triggerred settings set.
     */
    private void setSettings(MouseEvent event) {

        if(parametersCorrect()) {
            csvHeader = "settings," + robotSpeed.getText() + "," + robotSize.getText() + "," + obstacleSize.getText() + "," + racingModeValue;
            System.out.println(csvHeader);
            startEditor();

        } else {
            System.err.println("Inserted values are not valid.");
        }
    }

    /**
     * Starts the game editor.
     * Sets up everything and launches editor.
     */
    private void startEditor() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("editor-view.fxml"));
            Parent editorRoot = loader.load();

            /// Get the game controller and initiate the game setup
            EditorController editorController = loader.getController();
            editorController.initialize(csvHeader, Double.parseDouble(robotSize.getText()), Double.parseDouble(obstacleSize.getText()), this);
            editorController.setMode(racingModeValue);

            /// Setting up the new stage and scene
            Scene editorScene = new Scene(editorRoot);
            editorStage = new Stage();
            StageSetter.initStage(editorStage, "Game Editor", editorScene, false);

            menu.closeSettings();

        } catch (IOException e) {
            System.err.println("Error while opening editor window.");
        }
    }

    /**
     * Closes the editor window.
     */
    public void closeEditor() {
        if (editorStage != null) {
            editorStage.close();
        }
    }

    /**
     * Getter for editor stage.
     * @return Editor stage
     */
    public Stage getEditorStage() {
        return editorStage;
    }

    /**
     * Checks if all parameters inserted into settings are correct.
     * @return True if correct, false otherwise.
     */
    private boolean parametersCorrect() {

        /// Check if all fields are filled
        if(robotSpeed.getText().isEmpty() || robotSize.getText().isEmpty() || obstacleSize.getText().isEmpty()) {
            return false;

        /// Check if all fields are numbers
        } else if(notNum(robotSpeed.getText()) || notNum(robotSize.getText()) || notNum(obstacleSize.getText()) ) {
            return false;

        } else {
            return true;
        }
    }

    /**
     * Checks if string is a number.
     * @param str String to be checked.
     * @return True if string is not a number, false otherwise.
     */
    private boolean notNum(String str) {
        return !str.matches("\\d+(\\.\\d+)?");
    }

}
