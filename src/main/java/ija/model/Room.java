package ija.model;


import java.util.ArrayList;
import java.util.List;

public class Room implements Environment {
    private final int rows;
    private final int cols;
    private final List<Obstacle> obstacleList = new ArrayList<>();
    private final List<Robot> robotList = new ArrayList<>();


    public Room(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public static Room create(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Neither rows nor columns can be negative");
        }
        return new Room(rows, cols);
    }

    @Override
    public List<Obstacle> getObstacleList() {
        return obstacleList;
    }
    @Override
    public List<Robot> getRobotList() {
        return robotList;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getCols() {
        return this.cols;
    }


    @Override
    public List<Robot> robots() {
        // returns the copy of the list of robots
        return new ArrayList<>(robotList);
    }

    @Override
    public boolean addRobot(Robot robot) {
        if (this.containsPosition(robot.getPosition()) && this.isPositionEmpty(robot.getPosition())) {
            robotList.add(robot);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsPosition(Position pos) {
        return pos.getRow() >= 0 && pos.getRow() < this.rows && pos.getCol() >= 0 && pos.getCol() < this.cols;
    }

    @Override
    public boolean isPositionEmpty(Position pos) {
        return !this.obstacleAt(pos) && !this.robotAt(pos);
    }


    @Override
    public boolean createObstacleAt(int row, int col) {
        // if position is not out of bounds and is empty (there's no obstacle or robot)
        Position pos = new Position(row, col);
        if (this.containsPosition(pos) && this.isPositionEmpty(pos)) {
            // create an obstacle
            obstacleList.add(new Obstacle(pos));
            return true;
        }
        return false;
    }

    @Override
    public boolean obstacleAt(int row, int col) {
        // if pos is not out of bounds and contains an obstacle
        Position pos = new Position(row, col);
        if (containsPosition(pos)) {
            for (Obstacle obstacle : this.getObstacleList()) {
                if (obstacle.getPosition().equals(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean obstacleAt(Position pos) {
        // if pos is not out of bounds and contains an obstacle
        if (this.containsPosition(pos)) {
            for (Obstacle obstacle : this.getObstacleList()) {
                if (obstacle.getPosition().equals(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean robotAt(Position pos) {
        // if pos is not out of bounds and contains a robot
        if (this.containsPosition(pos)) {
            for (Robot robot : this.getRobotList()) {
                if (robot.getPosition().equals(pos)) {
                    return true;
                }
            }
        }
        return false;
    }
}