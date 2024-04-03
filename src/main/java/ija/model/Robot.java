package ija.model;

public interface Robot extends Observable {

    int getCurrAngle();
    Position getPosition();
    Position nextPosition();
    boolean canMove();
    boolean move();
    void turn();
    void turn(int n);
    void turnToAngle(int angle);
}