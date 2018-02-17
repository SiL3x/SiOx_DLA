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

}
