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
        this.front = meshSize - 1;
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
    }

    private void setHighestPoint() {
        highestPoint = meshSize;
        for (int value : values) {
            if (value < highestPoint) highestPoint = value;
        }
        System.out.println("highestPoint = " + highestPoint);
    }

    public int getFront(int[][] mesh, int growthRatio) {

        int frontTmp = front;

        for (int y = (meshSize - highestPoint); y < front ; y++) {

            int sum = 0;

            for (int x = 0; x < meshSize; x++) {
                sum += mesh[x][values[x] + y - meshSize];
            }

            if (sum > 0) {
                System.out.println("sum = " + sum + "   y = " + y);
            }

            if (sum >= (meshSize * growthRatio / 100D)) {
                System.out.println("sum = " + sum + " y = " + y + "  -> break! -> frontTmp : " + (frontTmp - y));
                front = y;
                //System.out.println("front new = " + front);
                moveFrontBy(frontTmp - y + 1);
                System.out.println("substrate = " + this);
                setHighestPoint();
                return  front - 1;
            }
        }
        return  front;
    }

    public void moveFrontBy(int lines) {
        if (highestPoint - lines >= meshSize) {
            System.out.println("Mesh Boundary reached");
            return;
        }

        System.out.println("moved front = " + front);

        for (int i = 0; i < values.length; i++) {
            values[i] -= lines;
        }
    }

    public int getValue(int x) {
        return values[x];
    }

    public void setFront(int front) {
        this.front = front;
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


    public int getHighestPoint() {
        return highestPoint;
    }
}
