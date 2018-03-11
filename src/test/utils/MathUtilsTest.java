package test.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static main.utils.MathUtils.pointLineDistance;

public class MathUtilsTest {

    private final double DELTA = 1e-3;

    @Test
    public void distance1() {
        Point p1 = new Point(-1, 0);
        Point p2 = new Point(1, 0 );
        Point p3 = new Point(0, 1);

        Assert.assertEquals(1, pointLineDistance(p3, p1, p2), DELTA);
    }
}
