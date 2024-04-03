package ija.model;

public class Obstacle {
    private final Position pos;
    public Obstacle(Position pos) {
        this.pos = pos;
    }
    public Position getPosition() {
        return pos;
    }
}