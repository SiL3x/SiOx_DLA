package main;

import main.configuration.Configuration;
import main.graphics.DisplaySites;
import main.models.BondPosition;
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
    private List<BondPosition> bondPositions;

    public DlaSimulation() {
        walkers = new ArrayList<>();
        //loadConfiguration("layer_1");
        loadConfiguration("layer_1");
        createMesh();
        calculateBondPosition();
        substrate = new Substrate(configuration.getMeshSize());
        substrate.createSubstrateFromPoints(configuration.substratePoints);
        placeSeed(configuration.getSeedNumber());

        i = 0;
        int j = 0;
        frontTmp = front;

        while (run) {
            if(walkers.size() == 0) {
                walkers.add(new Walker(configuration, front));
            }
            if(arraySum(mesh) >= 10000) { break; }

            i++;
            j++;

            moveWalkers();
            calculateStickingProbabilityKernel();
            moveGrowthFront();

            if(j==2e6) run = false;
            if (front >= 50) break;
        }

        meshSave = arrayAdd(meshSave, mesh.clone());
        meshSave = arrayAdd(meshSave, substrate.getMeshWithSubstrate());

        DisplaySites displaySites = new DisplaySites(meshSave);
    }

    private void calculateBondPosition() {
        bondPositions = new ArrayList<>();
        int[][] kernel = configuration.getKernel();
        int center  = kernel.length / 2;

        for (int x = 0; x < kernel.length; x++) {
            for (int y = 0; y < kernel.length; y++) {
                if (kernel[x][y] != 0) {
                    bondPositions.add(new BondPosition(x - center, y - center, kernel[x][y]));
                }
            }
        }
    }

    private void placeSeed(int seedNumber) {
        int border = configuration.getKernel().length/2 +1;
        List<Integer> seeds = new ArrayList<Integer>();

        for (int k = 0; k < seedNumber; k++) {
            Walker w = new Walker(configuration);
            boolean inMotion = true;
            while (inMotion) {
                int walkX = w.getPosition().getX();
                int walkY = w.getPosition().getY();
                 if (walkY == (substrate.getValue(walkX) - 1)) {
                     mesh[walkX][walkY] = 1;
                     System.out.println("Seed positioned @ (" + walkX + ", " + walkY + ")");
                     inMotion = false;
                 }
                 if (walkY < (substrate.getValue(walkX) - 20) || walkY >= substrate.getValue(walkX)) {
                     w.respawn(substrate);
                 }
                 w.moveRnd(1);
            }
        }
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

        if (frontTmp != front) {
            front = frontTmp;
            System.out.println("front = " + front);
        }
    }

    private void calculateStickingProbabilityKernel() {
        for (Walker w : walkers) {

            if (w.getPosition().getY() >= (substrate.getValue(w.getPosition().getX()) - configuration.getSurfaceStickDistance())) {
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
    }
/*
    private void calculateStickingTiltedKernel() {
        for (Walker w : walkers) {

            if (w.getPosition().getY() >= (substrate.getValue(w.getPosition().getX()) - configuration.getSurfaceStickDistance())) {



                int halfDiag = (int) (Math.ceil(configuration.getKernel().length) * 1.41 / 2);
                for (int x = w.getPosition().getX() - halfDiag; x < w.getPosition().getX() + halfDiag; x++) {
                    for (int y = w.getPosition().getY() - halfDiag; y < w.getPosition().getY() + halfDiag; y++) {


                    }

                }

                boolean itSticks = sum*sum >=  ThreadLocalRandom.current().nextInt(1, configuration.getStickingProbability()*configuration.getStickingProbability());
                if (itSticks) {
                    mesh[w.getPosition().getX()][w.getPosition().getY()] = 1;
                    walkers.clear();
                    break;
                }
            }
        }
    }
*/
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

    private void moveWalkers() {
        for (Walker w : walkers) {
            w.moveRnd(configuration.getMoveLength());
            if (w.getPosition().getY() < (substrate.getValue(w.getPosition().getX()) - 10) ||
                    w.getPosition().getY() >= substrate.getValue(w.getPosition().getX())) {
                w.respawn(substrate);
            }
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
                //configuration.setSeedPosition(Arrays.asList(new Position(35, 90), new Position(70, 90)));
                configuration.setSeedNumber(6);
                configuration.setWalkerStart(new Position(50, 70));
                configuration.setStickingDistance(3);
                configuration.setMoveLength(1);
                configuration.setGrowthRatio(30); // Value: 0-100
                configuration.setSpawnOffset(5);
                configuration.setStickingProbability(5);
                configuration.setExposure(2000);
                configuration.setKernel(kernel);
                configuration.substratePoints.add(new Position(0, 50));
                configuration.substratePoints.add(new Position(50, 99));
                configuration.substratePoints.add(new Position(100, 50));

                //configuration.substratePoints.add(new Position(0, 90));
                //configuration.substratePoints.add(new Position(100, 90));

                //configuration.substratePoints.add(new Position(0, 90));
                //configuration.substratePoints.add(new Position(100, 90));

                break;
            }

            case "layer_1" : {
                System.out.println("simulate one layer of SiOx on AZO");

                int[][] kernel = {
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0},
                        {0, 0, 0, 2, 1},
                        {0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0}
                };

                configuration = new Configuration("first real try");
                configuration.setMeshSize(100);
                configuration.setMeshResolution(10);
                configuration.setSeedNumber(20);
                configuration.setWalkerStart(new Position(50, 70));
                configuration.setStickingDistance(3);
                configuration.setMoveLength(1);
                configuration.setGrowthRatio(20); // Value: 0-100
                configuration.setSpawnOffset(5);
                configuration.setStickingProbability(7);
                configuration.setExposure(2000);
                configuration.setKernel(kernel);
                configuration.substratePoints.add(new Position(0, 80));
                configuration.substratePoints.add(new Position(20, 90));
                configuration.substratePoints.add(new Position(45, 82));
                configuration.substratePoints.add(new Position(65, 92));
                configuration.substratePoints.add(new Position(83, 85));
                configuration.substratePoints.add(new Position(100, 89));
                configuration.setSurfaceStickDistance(2);

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
