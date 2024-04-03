package ija.controller;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class GameController {
    public void moveControlledRobot(KeyEvent e) {
        switch (e.getCode()) {
            case T -> {
                // Turn robot
                System.out.println("OTOCKA 45 stupnu vole");
            }
            case SPACE -> {
                System.out.println("Jedeme dopredu kurvaaa");
            }
        }
    }
}
