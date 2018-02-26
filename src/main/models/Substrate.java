package main.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Substrate {

    int[] values;
    int meshSize;
    int highestPoint;
    int front;
    int spread;
    int min;
    int max;

    public Substrate(int meshSize) {
        this.meshSize = meshSize;
        front = 0;
        values = new int[meshSize];

    }

    public void createSubstrateFromPoints(List<Position> points) {
        final int size = points.size();
        double slope;

        if (points.get(size - 1).getX() != meshSize ) {
            System.out.println("ERROR: X value of the last point must match the mesh size!");
            return;
        }
        if (points.get(0).getX() != 0) {
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
        spread = calculateSpreadAndSetFront();
        System.out.println(this);
    }

    private void setHighestPoint() {
        highestPoint = meshSize;
        for (int value : values) {
            if (value < highestPoint) highestPoint = value;
        }
    }

    public int getFront(int[][] mesh, int growthRatio) {

        for (int y = max - spread; y > front; y--) {

            int sum = 0;

            for (int x = 0; x < meshSize; x++) {
                sum += mesh[x][values[x] - y];
            }

            if (sum >= (meshSize * growthRatio / 100D)) {
                front = y;
                return  front;
            }
        }
        return  front;
    }

    public int getValue(int x) {
        return values[x] - front;
    }

    public int calculateSpreadAndSetFront() {
        min = meshSize;
        max = 0;
        for (int x = 0; x < meshSize; x++) {
            if (values[x] < min) min = values[x];
            if (values[x] > max) max = values[x];
        }
        return max - min;
    }

    public int getHighestPoint() {
        return highestPoint;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    public static void main(String[] args) {
        int[][] mesh = {
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
        };

        System.out.println("mesh.length = " + mesh.length);

        Substrate substrate = new Substrate(10);
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 10));
        //positions.add(new Position(10, 15));
        positions.add(new Position(10, 10));
        substrate.setHighestPoint();

        substrate.createSubstrateFromPoints(positions);
        System.out.println(substrate);
        substrate.getFront(mesh, 15);
        //substrate.moveFrontBy(2);
        System.out.println(substrate);
        substrate.getFront(mesh, 15);
        System.out.println(substrate);
    }
}
