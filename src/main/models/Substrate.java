package main.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Substrate {

    int[] values;
    int meshSize;
    int highestPoint;
    int front;

    public Substrate(int meshSize) {
        this.meshSize = meshSize;
        values = new int[meshSize];

    }

    public int[] getSubstrate() {
        return values;
    }

    public void createSubstrateFromPoints(List<Position> points) {
        final int size = points.size();
        double slope;

        if ( points.get(size - 1).getX() != meshSize ) {
            System.out.println("ERROR: X value of the last point must match the mesh size!");
            return;
        }
        if ( points.get(0).getX() != 0) {
            System.out.println("ERROR: X value of the first point must be 0!");
            return;
        }

        for (int i = 1; i < size; i++) {
            slope = 1D * (points.get(i - 1).getY() - points.get(i).getY()) / (points.get(i - 1).getX() - points.get(i).getX());
            System.out.println("slope = " + slope);

            int k = 0;
            for (int j = points.get(i - 1).getX(); j < points.get(i).getX(); j++) {
                values[j] = (int) Math.round(slope * k + points.get(i - 1).getY());
                k++;
            }
        }

        setHighestPoint();
    }

    private void setHighestPoint() {
        highestPoint = 0;
        for (int value : values) {
            if (value > highestPoint) highestPoint = value;
        }
    }

    public void getFront(int[][] mesh, int growthRatio) {

        for (int y = front; y >= highestPoint; y--) {
            int sum = 0;
            for (int x = 0; x < mesh.length; x++) {
                sum += mesh[x][values[x]];
            }
            if (sum >= (meshSize * growthRatio / 100D)) {
                front = y;
                break;
            }
        }
    }

    public void moveFrontBy(int lines) {
        if (highestPoint + lines >= meshSize) {
            System.out.println("Mesh Boundary reached");
            return;
        }

        front += lines;

        for (int i = 0; i < values.length; i++) {
            values[i] += lines;
        }
    }

    public int getValue(int x) {
        return values[x];
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    public static void main(String[] args) {
        Substrate substrate = new Substrate(20);
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(10, 5));
        positions.add(new Position(20, 2));
        substrate.createSubstrateFromPoints(positions);
        System.out.println(substrate);
        substrate.moveFrontBy(2);
        System.out.println(substrate);
    }
}
