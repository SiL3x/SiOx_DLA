package main;

import main.configuration.Configuration;
import main.graphics.DisplaySites;
import main.models.Position;
import main.models.Substrate;
import main.models.Walker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static main.utils.ArrayUtils.*;

public class DlaSimulation {

    private int frontTmp;
    private List<Walker> walkers;
    private boolean run = true;
    public Configuration configuration;

    private int[][] mesh;
    private int[][] meshSave;
    private int front;
    private int i;
    private Substrate substrate;

    public DlaSimulation() {
        walkers = new ArrayList<>();
        loadConfiguration("test");
        createMesh();
        substrate = new Substrate(configuration.getMeshSize());
        substrate.createSubstrateFromPoints(configuration.substratePoints);
        substrate.setFront(90);
        placeSeed();

        i = 0;
        int j = 0;
        frontTmp = front;

        while (run) {
            if(walkers.size() == 0) {
                //System.out.println("front = " + front);
                walkers.add(new Walker(configuration, front));
            }
            if(arraySum(mesh) >= 10000) { break; }

            i++;
            j++;

            moveWalkers();
            calculateStickingProbabilityKernel();
            //calculateStickingKernel();

            //moveGrowthFrontByExposure();
            moveGrowthFront();

            /*
            if ( frontTmp != front) {
                System.out.println("front = " + front + "\ti = " + i + "\tratio = " + (1.0*i / (frontTmp - front)));
                //frontTmp = front;
                i = 0;
            }
            */

            if(j==1e6) run = false;
            if (front <= 12) break;
            if (substrate.getHighestPoint() < 15) break;
        }

        meshSave = arrayAdd(meshSave, mesh.clone());

        DisplaySites displaySites = new DisplaySites(meshSave);
    }

    private void moveGrowthFrontByExposure() {
        if (i == configuration.getExposure() ||
                sumLine(mesh, front) >= (configuration.getGrowthRatio() * configuration.getMeshSize()) / 100.0) {
            front--;
            i = 0;
        }
    }

    private void moveGrowthFront() {
        frontTmp = substrate.getFront(mesh, configuration.getGrowthRatio());
        //System.out.println("frontTmp = " + frontTmp);

        if (frontTmp != front) {
            //substrate.moveFrontBy(front - frontTmp);
            //System.out.println("substrate = " + substrate);
            System.out.println("front = " + front + "  frontTmp = " + frontTmp);
            front = frontTmp;
        }


        //System.out.println("front = " + front);
        //meshSave = arrayAdd(meshSave, mesh.clone());
        //mesh = eraseBelow(mesh, front+3);
    }



    private int[][] eraseBelow(int[][] array, int line) {
        for (int y = line; y < array.length; y++) {
            for (int x = 0; x < array.length; x++) {
                array[x][y] = 0;
            }
        }
        return array;
    }

    private void calculateSticking() {
        double distance = configuration.getMeshSize()*5;
        Position tempSite = new Position(0, 0);
        for (Walker w : walkers) {
            if (arraySum8Neighbours(mesh, w.getPosition()) > 0) {
                mesh[w.getPosition().getX()][w.getPosition().getY()] = 1;
                walkers.clear();
                break;
            }
        }
    }

    private void calculateStickingKernel() {
        for (Walker w : walkers) {
            final int[][] subArray = getSubArray(w.getPosition());
            final int sum = subArrayMultSum(subArray, configuration.getKernel());

            if (sum > 0) {
                mesh[w.getPosition().getX()][w.getPosition().getY()] = 1;
                walkers.clear();
                break;
            }
        }
    }

    private void calculateStickingProbabilityKernel() {
        for (Walker w : walkers) {
            final int[][] subArray = getSubArray(w.getPosition());
            final int sum = subArrayMultSum(subArray, configuration.getKernel());

            boolean itSticks = sum*sum >=
                    ThreadLocalRandom.current().nextInt(1, configuration.getStickingProbability()*configuration.getStickingProbability());
            if (itSticks) {
                mesh[w.getPosition().getX()][w.getPosition().getY()] = 1;
                walkers.clear();
                break;
            }
        }
    }

    private int[][] getSubArray(Position position) {
        int size = configuration.getKernel().length;
        int px = position.getX();
        int py = position.getY();
        int[][] outArray = new int[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                outArray[x][y] += mesh[px - size/2 + x][py - size/2 + y];
            }
        }
        return outArray;
    }

    private void calculateStickingProbability() {
        for (Walker w : walkers) {
            boolean itSticks = arraySum8Neighbours(mesh, w.getPosition())*arraySum8Neighbours(mesh, w.getPosition()) >=
                    ThreadLocalRandom.current().nextInt(1, configuration.getStickingProbability()*configuration.getStickingProbability());
            if (itSticks) {
                mesh[w.getPosition().getX()][w.getPosition().getY()] = 1;
                System.out.println("Sticked @ " + w.getPosition());
                walkers.clear();
                break;
            }
        }
    }

    private void moveWalkers() {
        for (Walker w : walkers) {
            w.moveRnd(configuration.getMoveLength());
            if (w.getPosition().getY() < (substrate.getValue(w.getPosition().getX()) - 10) ||
                    w.getPosition().getY() >= substrate.getValue(w.getPosition().getX())) {
                w.respawn(substrate);
                //System.out.println("respawned - position = " + w.getPosition());
            }
            //System.out.println("w.getPosition() = " + w.getPosition());
        }
    }

    private void placeSeed() {
        for (Position p : configuration.getSeedPosition()) {
            setPosition(mesh, p);
        }
        front = configuration.getSeedPosition().get(0).getY();
        System.out.println("seed placed at " + configuration.getSeedPosition());
    }

    private void createMesh() {
        System.out.println("mesh created meshSize = " + configuration.getMeshSize());
        mesh = new int[configuration.getMeshSize()][configuration.getMeshSize()];
        meshSave = mesh.clone();
    }

    private void loadConfiguration(String name) {
        switch (name) {
            case "test" : {
                System.out.println("this is a test");

                int[][] kernel = {
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0},
                        {0, 0, 0, 2, 1},
                        {0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0}
                }; 

                configuration = new Configuration("test");
                configuration.setMeshSize(100);
                configuration.setMeshResolution(10);
                configuration.setSeedPosition(Arrays.asList(new Position(35, 90), new Position(70, 90)));
                configuration.setWalkerStart(new Position(50, 70));
                configuration.setStickingDistance(3);
                configuration.setMoveLength(1);
                configuration.setGrowthRatio(1); // Value: 0-100
                configuration.setSpawnOffset(5);
                configuration.setStickingProbability(3);
                configuration.setExposure(2000);
                configuration.setKernel(kernel);
                //configuration.substratePoints.add(new Position(0, 70));
                //configuration.substratePoints.add(new Position(50, 100));
                //configuration.substratePoints.add(new Position(100, 70));

                configuration.substratePoints.add(new Position(0, 90));
                configuration.substratePoints.add(new Position(100, 90));

                //configuration.substratePoints.add(new Position(0, 90));
                //configuration.substratePoints.add(new Position(100, 90));

                break;
            }

            case "big_one" : {
                System.out.println("this is a big one");
                configuration = new Configuration("big_one");
                configuration.setMeshSize(500);
                configuration.setMeshResolution(10);
                configuration.setSeedPosition(Arrays.asList(new Position(35, 90), new Position(70, 90)));
                configuration.setWalkerStart(new Position(50, 70));
                configuration.setStickingDistance(3);
                configuration.setMoveLength(1);
                configuration.setGrowthRatio(10); // Value: 0-100
                configuration.setSpawnOffset(5);
                configuration.setStickingProbability(5);
                configuration.setExposure(50000);
                break;
            }
        }
    }

    public static void main(String[] args) {
        new DlaSimulation();
    }
}
