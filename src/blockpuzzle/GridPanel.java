package blockpuzzle;

import javax.swing.*;
import java.awt.*;

/**
 * A GridPanel is an extended JPanel that contains
 * the logical and visual representation of a Grid.
 */
public class GridPanel extends JPanel {
    private Grid grid = new Grid();
    private int posX;  // x-coordinate of the grid's upper left corner (in pixels)
    private int posY;  // y-coordinate of the grid's upper left corner (in pixels)
    private final int cellSize = 30;

    GridPanel() {
        // add MouseInteractionManager

    }

    /**
     * Draws the grid and its content (i.e. its inserted blocks).
     * @param g the Graphics object given by paintComponent()
     */
    private void drawGrid(Graphics g) {
        // draw empty grid
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                g.drawRect(15 + x * cellSize,
                           15 + y * cellSize,
                              cellSize, cellSize);
            }
        }

    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

}
