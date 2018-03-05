package main.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Substrate {

    int[] values;
    int meshSize;
    int highestPoint;
    int front;
    int spread;
    int min;
    int max;
    double[] slopes;

    public Substrate(int meshSize) {
        this.meshSize = meshSize;
        front = 0;
        values = new int[meshSize];

    }

    public void createSubstrateFromPoints(List<Position> points) {
        final int size = points.size();
        slopes = new double[points.get(points.size() - 1).getX()];
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

            for (int x = points.get(i - 1).getX(); x < points.get(i).getX(); x++) {
                slopes[x] = slope;
            }

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

    public double getSubstrateNormal(int x) {
        double normal = Math.atan(slopes[x]); // in radians
        return normal;
    }

    public int getHighestPoint() {
        return highestPoint;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    public int[][] getMeshWithSubstrate() {
        int[][] outMesh = new int[meshSize][meshSize];

        for (int x = 0; x < meshSize; x++) {
            outMesh[x][values[x]] = 1;
        }

        return outMesh;
    }
}
