package graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.geom.*;

@SuppressWarnings("serial")

public class MyFrame extends JFrame {

    public static void main(String[] args) {

        new MyFrame();

    }

    public MyFrame() {
        this.setSize(500, 500);
        this.setTitle("MyFrame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new DrawStuff(), BorderLayout.CENTER);
        this.setVisible(true);

    }

    private class DrawStuff extends JComponent {

        public void paint(Graphics g) {
            Graphics2D graph2 = (Graphics2D) g;
            graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graph2.setBackground(Color.BLACK);

            Shape drawLine = new Line2D.Float(20, 90, 55, 250);

            Shape drawArc2D = new Arc2D.Double(5, 150, 100, 100, 45, 180, Arc2D.OPEN);
            Shape drawArc2D2 = new Arc2D.Double(5, 200, 100, 100, 45, 180, Arc2D.CHORD);
            Shape drawArc2D3 = new Arc2D.Double(5, 250, 100, 100, 45, 180, Arc2D.PIE);

            graph2.setPaint(Color.RED);


            for (int i=0; i<10; i++) {
                graph2.draw(cirle(i));
            }
            //graph2.draw(drawLine);
            //graph2.draw(drawArc2D);
            //graph2.draw(drawArc2D2);
            //graph2.draw(drawArc2D3);

        }

        private Shape cirle(int i) {
            Shape drawCircle = new Ellipse2D.Double(250, i*10, 20, 20);
            return drawCircle;
        }
    }
}
