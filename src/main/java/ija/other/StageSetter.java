/**
 * @package ija.controller
 * @file StageSetter.java
 * @class StageSetter
 *
 * Class for setting new stage.
 *
 * @author Dominik Horut
 */
package ija.other;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class for setting up the new stage.
 */
public class StageSetter {
    /**
     * Default constructor for StageSetter.
     */
    public StageSetter() {}

    /**
     * Initializes the stage, sets all important parameters and displays the stage
     * @param stage Stage to be set.
     * @param windowTitle Window title to be displayed.
     * @param scene Scene to be displayed in the stage.
     * @param resizable Make the window resizable or not.
     */
    public static void initStage(Stage stage, String windowTitle, Scene scene, boolean resizable) {
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.show();
    }
}
