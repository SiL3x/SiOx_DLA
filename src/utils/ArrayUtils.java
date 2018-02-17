package utils;

import models.Position;

public class ArrayUtils {


    public static int arraySum(int[][] array) {
        int arraySum = 0;
        for (int x = 0; x < array.length; x++) {
            for (int y = 1; y < array.length - 1; y++) {
                arraySum += array[x][y];
            }
        }
        //System.out.println("arraySum = " + arraySum);
        return arraySum;
    }

    public static int arraySum8Neighbours(int[][] array, Position pos) {
        int sum = 0;

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                sum += array[pos.getX() + x][pos.getY() + y];
            }
        }
        //System.out.println(sum);
        return sum;
    }

    public static int[][] setPosition(int[][] array, Position pos) {
        array[pos.getX()][pos.getY()] = 1;
        System.out.println("array[pos.getX()][pos.getY()] = " + array[pos.getX()][pos.getY()]);
        //System.out.println("array = " + array);
        return array;
    }

    public static int getFront(int[][] array, int growthRatio) {
        int row = array.length;
        for (int y = 0; y < array.length; y++) {
            if (sumLine(array, y) >= array.length * growthRatio / 100) {
                row = y;
                break;
            }
        }
        return row;
    }

    private static int sumLine(int[][] array, int y) {
        int out = 0;
        for (int x = 0; x < array.length; x++) {
            out += array[x][y];
        }
        return out;
    }

    public static int[][] arrayAdd(int[][] array1, int[][] array2) {
        if (array1.length != array2.length) {
            System.out.println("Ohhhh Noooo - dimension mismatch");
            return array1;
        }

        int[][] outArray = new int[array1.length][array1.length];

        for (int x = 0; x < array1.length; x++) {
            for (int y = 0; y < array1.length; y++) {
                outArray[x][y] = array1[x][y] + array2[x][y];
            }
        }
        return outArray;
    }
}
