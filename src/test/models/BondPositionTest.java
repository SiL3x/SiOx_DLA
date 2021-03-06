package test.models;

import main.models.BondPosition;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class BondPositionTest {
    private final double DELTA = 1e-3;

    @Test
    public void giveBackXY() {
        BondPosition bondPosition = new BondPosition(0, -1, 1);
        Assert.assertEquals(0, bondPosition.getX(), DELTA);
        Assert.assertEquals(-1, bondPosition.getY(), DELTA);
    }

    @Test
    public void tiltBy0() {
        BondPosition bondPosition = new BondPosition(0, -1, 1);
        bondPosition.tilt(Math.toRadians(0));
        Assert.assertEquals(0, bondPosition.getX(), DELTA);
        Assert.assertEquals(-1, bondPosition.getY(), DELTA);
    }

    @Test
    public void tiltBy45degrees() {
        BondPosition bondPosition = new BondPosition(0, -1, 1);
        bondPosition.tilt(Math.toRadians(45));
        Assert.assertEquals(0.7071, bondPosition.getX(), DELTA);
        Assert.assertEquals(-0.7071, bondPosition.getY(), DELTA);
        bondPosition.tilt(Math.toRadians(0));
        System.out.println(bondPosition.getX() + "  " + bondPosition.getY() );
    }

    @Test
    public void tiltByMinus45degrees() {
        BondPosition bondPosition = new BondPosition(0, -1, 1);
        bondPosition.tilt(Math.toRadians(-45));
        Assert.assertEquals(-0.7071, bondPosition.getX(), DELTA);
        Assert.assertEquals(-0.7071, bondPosition.getY(), DELTA);
        bondPosition.tilt(Math.toRadians(0));
    }

}
