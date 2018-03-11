package main.utils;

import java.awt.*;
import java.awt.geom.Line2D;

public class MathUtils {

    public static double distance(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static double pointLineDistance(Point  a, Point r1, Point r2) {
        Line2D.Double line = new Line2D.Double(r1, r2);
        return line.ptLineDist(a);
    }
}
