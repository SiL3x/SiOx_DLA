package main.models;

public class Vec {
    private double y;
    private double x;

    public Vec(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec positionDifference(Position position1, Position position2) {
        x = position1.getX() - position2.getX();
        y = position1.getY() - position2.getY();
        return  this;
    }

    public void normalize() {
        double norm = Math.sqrt(x*x + y*y);
        x = x / norm;
        y = y / norm;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }


    public String toString() {
        return "("+x+"/"+y+")";
    }
}
