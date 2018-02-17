import configuration.Configuration;
import graphics.DisplaySites;
import models.Position;
import models.Walker;

import java.util.ArrayList;
import java.util.List;

import static utils.ArrayUtils.arraySum;
import static utils.ArrayUtils.arraySum8Neighbours;
import static utils.ArrayUtils.setPosition;

public class DlaSimulation {

    private List<Walker> walkers;
    private boolean run = true;
    public Configuration configuration;
    //private List<Position> sites;
    private int[][] mesh;

    public DlaSimulation() {
        //sites = new ArrayList<>();
        walkers = new ArrayList<>();
        loadConfiguration("test");
        createMesh();
        placeSeed();

        int i = 0;

        while (run) {
            if(walkers.size() == 0) { walkers.add(new Walker(configuration)); }
            if(arraySum(mesh) >= 200) { break; }

            moveWalkers();
            calculateSticking();

            i++;
            //if(i==10000) run = false;
        }

        DisplaySites displaySites = new DisplaySites(mesh);
    }

    private void calculateSticking() {
        double distance = configuration.getMeshSize()*5;
        Position tempSite = new Position(0, 0);
        for (Walker w : walkers) {
            if (arraySum8Neighbours(mesh, w.getPosition()) > 0) {
                mesh[w.getPosition().getX()][w.getPosition().getY()] = 1;
                //System.out.println("Sticked @ " + w.getPosition());
                walkers.clear();
                break;
            }
        }
    }

    private void moveWalkers() {
        for (Walker w : walkers) {
            w.moveRnd(configuration.getMoveLength());
            //System.out.println("w.getPosition() = " + w.getPosition());
        }
    }

    private void placeSeed() {
        setPosition(mesh, configuration.getSeedPosition());
        System.out.println("seed placed at " + configuration.getSeedPosition());
    }

    private void createMesh() {
        System.out.println("mesh created meshSize = " + configuration.getMeshSize());
        mesh = new int[configuration.getMeshSize()][configuration.getMeshSize()];
    }

    private void loadConfiguration(String name) {
        switch (name) {
            case ("test") : {
                System.out.println("this is a test");
                configuration = new Configuration("test");
                configuration.setMeshSize(100);
                configuration.setMeshResolution(10);
                configuration.setSeedPosition(new Position(50, 90));
                configuration.setWalkerStart(new Position(50, 70));
                configuration.setStickingDistance(3);
                configuration.setMoveLength(1);
            }
        }
    }

    public static void main(String[] args) {
        new DlaSimulation();
    }
}
