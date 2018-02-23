package main.models;

import java.util.concurrent.ThreadLocalRandom;

import main.configuration.Configuration;

public class Walker {

    private int border=1;
    private Position position;
    private Configuration configuration;
    private int spawnY;

    public Walker(Configuration configuration) {
        this.configuration = configuration;
        this.position = configuration.getWalkerStart();
        this.border = configuration.getKernel().length/2 +1;
    }

    public Walker(Configuration configuration, int front) {
        this.configuration = configuration;
        this.spawnY = front - configuration.getSpawnOffset();
        this.border = configuration.getKernel().length/2 +1;
        int randomX = ThreadLocalRandom.current().nextInt(border, configuration.getMeshSize() - border);
        this.position = new Position(randomX, spawnY);
        //System.out.println("position = " + position);
    }

    public void respawn() {
        int randomX = ThreadLocalRandom.current().nextInt(border, configuration.getMeshSize() - border);
        this.position = new Position(randomX, spawnY);
    }

    public void moveRnd(int moveLength) {
        int direction = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        position.move(direction, moveLength);

        int meshSize = configuration.getMeshSize();

        if (position.getY() > (meshSize - border)) position.setY(meshSize - border);
        if (position.getY() < border) position.setY(border);
        if (position.getX() < border) position.setX(border);
        if (position.getX() > meshSize - border) position.setX(meshSize - border);
    }

    public Position getPosition() {
        return position;
    }

    public void stickTo(Position site) {
        position = calcNearest(site);
    }

    private Position calcNearest(Position site) {
        Vec vec = new Vec(position.getX() - site.getX(), position.getY() - site.getY());
        vec.normalize();
        int newX = (int) Math.round(vec.getX()) + site.getX();
        int newY = (int) Math.round(vec.getY()) + site.getY();
        System.out.println("position = " + position);
        System.out.println("site = " + site);
        System.out.println("Vec = " + vec + "  x = " + (int) Math.round(vec.getX()) + "   y = " + (int) Math.round(vec.getY()));
        position = new Position(newX, newY);
        return position;
    }
}
