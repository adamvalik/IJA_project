package ija.model;

import java.util.List;

public interface Environment {

    int getRows();
    int getCols();
    List<Obstacle> getObstacleList();
    List<Robot> getRobotList();

    boolean addRobot(Robot robot);
    boolean containsPosition(Position pos);
    boolean createObstacleAt(int row, int col);
    boolean obstacleAt(int row, int col);
    boolean obstacleAt(Position pos);
    boolean robotAt(Position pos);
    boolean isPositionEmpty(Position pos);
    List<Robot> robots();

}
