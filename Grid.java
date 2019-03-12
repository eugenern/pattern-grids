import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Grid
 * 
 * For now just do the most basic thing possible: a static grid that's drawn all at once
 */
public class Grid {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? " +
                            SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Grid");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    }
}

/**
 * MyPanel
 */
class MyPanel extends JPanel {

    PatternGrid pg = new PatternGrid();

    public MyPanel() {
        pg.fillGrid(100);
    }

    public Dimension getPreferredSize() {
        // hardcoding it here, later will want to improve
        return new Dimension(500, 500);
    }

    public void paintComponent(Graphics g) {
        // Let UI Delegate paint first, which
        // includes background filling since
        // this component is opaque
        super.paintComponent(g);

        pg.paintGrid(g);
    }
}

class PatternGrid {

    private boolean[][] grid;

    public void fillGrid(int size) {
        grid = new boolean[size][size];

        for (int i = 1; i < grid.length; ++i) {
            // examine first i cells of row i (0-indexed)
            // boolean[] pattern = Arrays.copyOf(grid[i], i);
            // boolean inverted = false;
            boolean[] pattern = new boolean[i * 2];
            System.arraycopy(grid[i], 0, pattern, 0, i);
            for (int j = i; j < pattern.length; ++j) {
                pattern[j] = !pattern[j - i];
            }

            // Would much prefer to use System.arraycopy(), but
            // can't access column of grid as an array
            for (int j = i; j < grid.length; ++j) {
                boolean boolVal = pattern[j % pattern.length];
                grid[i][j] = boolVal;
                grid[j][i] = boolVal;
            }
        }
    }

    public void paintGrid(Graphics g) {
        // NOTE: the grid should go from the bottom up
        // false is blue, true is red
        g.setColor(Color.BLUE);
        // again, the size of the display is currently hardcoded
        g.fillRect(0, 0, 500, 500);
        g.setColor(Color.RED);
        for (int i = 1; i < grid.length; ++i) {
            // j can start from i since the grid is symmetrical across y=x
            for (int j = i; j < grid.length; ++j) {
                if (grid[i][j]) {
                    g.fillRect(j * 5, 495 - (i * 5), 5, 5);
                    g.fillRect(i * 5, 495 - (j * 5), 5, 5);
                }
            }
        }
    }
}