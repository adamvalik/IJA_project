<h1><img height="40" src="icon.png" alt="robot icon">&nbsp;&nbsp;Ultimate Robot/Racing Game</h1> 

Task: 2D mobile robot simulator for Java Course (IJA) at FIT BUT  
Authors: Adam Valík (xvalik05), Dominik Horut (xhorut01)

## Description
The project simulates environment designed for the movement and interaction of robots within a bounded, rectangular space. The simulation includes robots with circular bodies capable of movement and collision detection. The environment also contains static obstacles composed of multiple squares.
### Autonomous robots
These robots travel straight until they detect an obstacle within a predefined distance. Upon detection, they turn by a specified angle and continue moving.
### Controlled robots
Operated via controls from a GUI or from keyboard, these robots can move forward, backward and also turn while moving or rotate in the place. They automatically stop if an obstacle is detected ahead. 
Keyboard controls: robot1 - arrow keys, robot2 - WASD keys
GUI controls: any number of controlled robots, move forward, rotate left, rotate right, change robot
### Simulation
The simulation can be paused and resumed at any time. To reset the simulation, press the stop button, which will return to the editor/menu and start the simulation again.
### Editor
The editor allows the user to create a new environment by placing obstacles, controlled robots and autonomous robots with their parameters. The editor also allows the user to save the environment to a CSV file which can be loaded later straight from the menu.
### Racing mode
The second mode is for two players and is focused on formula1 racing. It allows two controlled robots visualized as formula cars and obstacles, create a track in editor or load one of the prepared maps in ``data`` directory and challenge a friend to race.

## Installation
The project uses Java SE 17 along with JavaFX 17. To build the project, you also need to have Maven installed.
Run:
```bash
mvn package
```
to build the project. This will also generate the documentation, which can be found in ``target/site/apidocs/index.html`` The resulting JAR file will be in the ``target`` directory.
Run:
```bash
mvn javafx:run
```
to run the project.

If you are running the macOS Sonoma, you may encounter problems with the ``javafx:run`` command. In that case, ensure that you have javafx in ``lib`` directory and run the following command:
```bash
java --module-path lib/javafx --add-modules javafx.controls,javafx.fxml -jar target/IJA_project-1.0.jar
```