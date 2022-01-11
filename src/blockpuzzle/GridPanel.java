package blockpuzzle;

import javax.swing.*;
import java.awt.*;

/**
 * A GridPanel is an extended JPanel that contains the visual representation of a Grid.
 */
public class GridPanel extends JPanel {
    private Grid grid;
    private int posX;  // x-coordinate of the grid's upper left corner (in pixels)
    private int posY;  // y-coordinate of the grid's upper left corner (in pixels)
    private final int cellSize = 50;

    GridPanel() {
        // add MouseInteractionManager

    }

    private void drawGrid() {

    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid();
    }

}
