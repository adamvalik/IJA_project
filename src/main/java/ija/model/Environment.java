package ija.model;

import java.util.List;

public interface Environment {

    boolean addRobot(Robot robot);
    boolean containsPosition(Position pos);
    boolean createObstacleAt(int row, int col);
    boolean obstacleAt(int row, int col);
    boolean obstacleAt(Position pos);
    boolean robotAt(Position pos);
    boolean isPositionEmpty(Position pos);

    int rows();

    int cols();

    List<Robot> robots();

}
