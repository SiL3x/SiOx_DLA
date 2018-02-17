package models;

import java.util.concurrent.ThreadLocalRandom;

import configuration.Configuration;
import models.Vec.*;

public class Walker {

    private Position position;
    private Configuration configuration;
    private int spawnY;

    public Walker(Configuration configuration) {
        this.configuration = configuration;
        this.position = configuration.getWalkerStart();
    }

    public Walker(Configuration configuration, int front) {
        this.configuration = configuration;
        this.spawnY = front - configuration.getSpawnOffset();
        this.position = configuration.getWalkerStart();

    }

    public void moveRnd(int moveLength) {
        int direction = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        position.move(direction, moveLength);
        position.setX(position.getX() % (configuration.getMeshSize() - 1));
        if (position.getY() > (configuration.getMeshSize() - 2)) position.setY(configuration.getMeshSize() - 2);
        if (position.getY() < 1) position.setY(1);
        if (position.getX() < 1) position.setX(1);
        if (position.getX() > configuration.getMeshSize() - 2) position.setX(configuration.getMeshSize() - 2);
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
