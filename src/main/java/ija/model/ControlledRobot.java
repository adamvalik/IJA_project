package ija.model;

import java.util.HashSet;
import java.util.Set;

public class ControlledRobot implements Robot {
    private final Environment env;
    private Position pos;
    private int currAngle;
    private final Set<Observer> observers = new HashSet<>();


    private ControlledRobot(Environment env, Position pos) {
        this.env = env;
        this.pos = pos;
        this.currAngle = 0;
    }
    private ControlledRobot(Environment env, Position pos, int angle) {
        this.env = env;
        this.pos = pos;
        this.currAngle = angle;
    }

    public static ControlledRobot create(Environment env, Position pos) {
        ControlledRobot robot = new ControlledRobot(env, pos);
        if (env.addRobot(robot)) {
            return robot;
        } else {
            return null;
        }
    }

    @Override
    public int getCurrAngle() {
        return currAngle;
    }

    @Override
    public Position getPosition() {
        return pos;
    }


    @Override
    public Position nextPosition() {
        /* 3x3 room:        (angle 0 is upwards)
                [0,0][0,1][0,2]
                [1,0][1,1][1,2]
                [2,0][2,1][2,2]
        */
        int rowDelta = 0; // Default value
        int colDelta = 0; // Default value

        switch (currAngle) {
            case 0 -> rowDelta = -1;
            case 45 -> {
                rowDelta = -1;
                colDelta = 1;
            }
            case 90 -> colDelta = 1;
            case 135 -> {
                rowDelta = 1;
                colDelta = 1;
            }
            case 180 -> rowDelta = 1;
            case 225 -> {
                rowDelta = 1;
                colDelta = -1;
            }
            case 270 -> colDelta = -1;
            case 315 -> {
                rowDelta = -1;
                colDelta = -1;
            }
            default -> {
            }
        }
        return new Position(this.getPosition().getRow() + rowDelta, this.getPosition().getCol() + colDelta);
    }

    @Override
    public boolean canMove() {
        // if the next position exists (is part of the environment) and is empty
        Position nextPos = nextPosition();
        return env.containsPosition(nextPos) && env.isPositionEmpty(nextPos);
    }


    @Override
    public boolean move() {
        if (this.canMove()) {
            pos = nextPosition();
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void turn() {
        // clockwise rotation of 45 degrees
        currAngle = (currAngle + 45) % 360;
        notifyObservers();
    }

    @Override
    public void turn(int n) {
        // n times clockwise rotation of 45 degrees
        currAngle = (currAngle + 45 * n) % 360;
        notifyObservers();
    }

    @Override
    public void turnToAngle(int angle) {
        // set the angle to the given value
        if (angle % 45 != 0) {
            throw new IllegalArgumentException("Angle must be a multiple of 45 degrees.");
        }
        currAngle = angle;
        notifyObservers();
    }


    public void addObserver(Observable.Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observable.Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers() {
        this.observers.forEach((observer) -> {
            observer.update(this);
        });
    }
}