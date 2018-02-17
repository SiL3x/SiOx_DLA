package models;

public class Position {
    private int y;
    private int x;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public String toString() {
        return ("(" + x + "/" + y +")");
    }

    public double distance(Position position) {

        return Math.sqrt((x-position.getX()) * (x-position.getX()) + (y-position.getY()) * (y-position.getY()));
    }

    public void move(int direction, int moveLength) {
        //System.out.println("direction = " + direction);
        //System.out.println("moveLength = " + moveLength);
        if (direction == 0) x += moveLength;
        if (direction == 1) y += moveLength;
        if (direction == 2) x -= moveLength;
        if (direction == 3) y -= moveLength;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
