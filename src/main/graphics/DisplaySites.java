package main.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class DisplaySites extends JFrame {

    private int[][] array;

    public DisplaySites(int[][] array) {
        this.array = array;
        this.setSize(500, 550);
        this.setTitle("MyFrame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new DisplaySites.DrawStuff(), BorderLayout.CENTER);
        this.setVisible(true);
    }

    private class DrawStuff extends JComponent {

        public void paint(Graphics g) {
            Graphics2D graph2 = (Graphics2D) g;
            graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graph2.setBackground(Color.BLACK);

            graph2.setPaint(Color.BLACK);
            Rectangle2D.Double background = new Rectangle2D.Double(0, 0, 500, 500);
            graph2.fill(background);
            graph2.draw(background);

            Ellipse2D.Double circle1 = new Ellipse2D.Double(0, 0, 5, 5);
            graph2.draw(circle1);

            graph2.setPaint(Color.WHITE);

            for (int x = 0; x < array.length; x++) {
                for (int y = 0; y < array.length; y++) {
                    if (array[x][y] >0) {
                        Ellipse2D.Double circle = new Ellipse2D.Double(x*5, y*5, 5, 5);
                        graph2.fill(circle);
                        graph2.draw(circle);
                    }
                }
            }
        }

        private Shape cirle(int i) {
            Shape drawCircle = new Ellipse2D.Double(250, i*10, 20, 20);
            return drawCircle;
        }
    }
}
