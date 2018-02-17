import configuration.Configuration;
import graphics.DisplaySites;
import models.Position;
import models.Walker;

import java.util.ArrayList;
import java.util.List;

public class DlaSimulation {

    private List<Walker> walkers;
    private boolean run = true;
    public Configuration configuration;
    private List<Position> sites;

    public DlaSimulation() {
        sites = new ArrayList<>();
        walkers = new ArrayList<>();
        loadConfiguration("test");
        createMesh();
        placeSeed();

        int i = 0;

        while (run) {
            if(walkers.size() == 0) { walkers.add(new Walker(configuration)); }
            if(sites.size() >= 500) {break;}

            moveWalkers();
            calculateSticking();

            i++;
            if(i==1000000) run = false;
        }

        DisplaySites displaySites = new DisplaySites(sites);
    }

    private void calculateSticking() {
        double distance = configuration.getMeshSize()*5;
        Position tempSite = new Position(0, 0);
        for (Walker w : walkers) {
            for (Position site : sites) {
                if (distance >= w.getPosition().distance(site)) {
                    distance = w.getPosition().distance(site);
                    tempSite = site;
                }
            }

            if (distance <= configuration.getStickingDistance()) {
                w.stickTo(tempSite);
                sites.add(w.getPosition());
                //System.out.println("Sticked t20o = " + w.getPosition());
                walkers.clear();
                break;
            }
        }
    }

    private void moveWalkers() {
        for (Walker w : walkers) {
            w.moveRnd(configuration.getMoveLength());
        }
    }

    private void placeSeed() {
        sites.add(configuration.getSeedPosition());
        System.out.println("seed placed at " + configuration.getSeedPosition());
    }

    private void createMesh() {
        System.out.println("mesh created meshSize = " + configuration.getMeshSize());
        return;
    }

    private void loadConfiguration(String name) {
        switch (name) {
            case ("test") : {
                System.out.println("this is a test");
                configuration = new Configuration("test");
                configuration.setMeshSize(100);
                configuration.setMeshResolution(10);
                configuration.setSeedPosition(new Position(50, 90));
                configuration.setWalkerStart(new Position(50, 10));
                configuration.setStickingDistance(3);
                configuration.setMoveLength(1);
            }
        }
    }

    public static void main(String[] args) {
        new DlaSimulation();
    }
}
