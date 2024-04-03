package ija.model;

public class AutonomousRobot implements Robot {
    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public int getCurrAngle() {
        return 0;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public Position nextPosition() {
        return null;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean move() {
        return false;
    }

    @Override
    public void turn() {

    }

    @Override
    public void turn(int n) {

    }

    @Override
    public void turnToAngle(int angle) {

    }
}
