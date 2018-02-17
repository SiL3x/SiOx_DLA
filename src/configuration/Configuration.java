package configuration;

import models.Position;

public class Configuration {
    private final String configName;
    private int meshSizeX;
    private int meshSizeY;
    private int meshResolution;
    private int meshSize;
    private Position seedPosition;
    private Position walkerStart;
    private int moveLength;
    private double stickingDistance;

    public Configuration(String configName) {
        this.configName = configName;
    }

    public void setMeshSize(int i) {
        meshSizeX = i;
        meshSizeY = i;
        meshSize = i;
    }

    public void setMeshResolution(int i) {
        meshResolution = 1;
    }

    public int getMeshSize() {
        return meshSize;
    }


    public Position getSeedPosition() {
        return seedPosition;
    }

    public void setSeedPosition(Position seedPosition) {
        this.seedPosition = seedPosition;
    }

    public Position getWalkerStart() {
        return walkerStart;
    }

    public void setWalkerStart(Position walkerStart) {
        this.walkerStart = walkerStart;
    }

    public int getMoveLength() {
        return moveLength;
    }

    public double getStickingDistance() {
        return stickingDistance;
    }

    public void setStickingDistance(double distance) {
        this.stickingDistance = distance;
    }

    public void setMoveLength(int moveLength) {
        this.moveLength = moveLength;
    }
}