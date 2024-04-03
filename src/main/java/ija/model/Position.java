package ija.model;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getRow() {
        return this.x;
    }
    public int getCol() {
        return this.y;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Position pos)) {
            return false;
        } else {
            return this.x == pos.x && this.y == pos.y;
        }
    }
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        return hash;
    }
    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }
}
