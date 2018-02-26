package main.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

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

            System.out.println("drawing array.length = " + array.length);

            for (int x = 0; x < array.length; x++) {
                for (int y = 0; y < array.length; y++) {
                    if (array[x][y] >0) graph2.draw(new Ellipse2D.Double(x*5, y*5, 5, 5));
                }
            }
        }

        private Shape cirle(int i) {
            Shape drawCircle = new Ellipse2D.Double(250, i*10, 20, 20);
            return drawCircle;
        }
    }
}
