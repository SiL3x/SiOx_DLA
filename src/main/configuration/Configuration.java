package main.configuration;

import main.models.Position;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private final String configName;
    private int meshSizeX;
    private int meshSizeY;
    private int meshResolution;
    private int meshSize;
    private List<Position> seedPosition;
    private Position walkerStart;
    private int moveLength;
    private double stickingDistance;

    private int growthRatio;

    private int spawnOffset;
    private int stickingProbability;
    private int exposure;
    private int[][] kernel;
    public List<Position> substratePoints;

    public Configuration(String configName) {
        this.configName = configName;
        substratePoints = new ArrayList<>();
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


    public List<Position> getSeedPosition() {
        return seedPosition;
    }

    public void setSeedPosition(List<Position> seedPosition) {
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

    public int getGrowthRatio() {
        return growthRatio;
    }

    public void setGrowthRatio(int growthRatio) {
        this.growthRatio = growthRatio;
    }

    public int getSpawnOffset() {
        return spawnOffset;
    }

    public void setSpawnOffset(int spawnOffset) {
        this.spawnOffset = spawnOffset;
    }

    public int getStickingProbability() {
        return stickingProbability;
    }

    public void setStickingProbability(int stickingProbability) {
        this.stickingProbability = stickingProbability;
    }

    public int getExposure() {
        return exposure;
    }

    public void setExposure(int exposure) {
        this.exposure = exposure;
    }

    public void setKernel(int[][] kernel) {
        this.kernel = kernel;
    }

    public int[][] getKernel() {
        return kernel;
    }
}
