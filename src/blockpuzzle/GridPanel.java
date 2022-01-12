package blockpuzzle;

import javax.swing.*;
import java.awt.*;

/**
 * A GridPanel is an extended JPanel that contains
 * the logical and visual representation of a Grid.
 */
public class GridPanel extends JPanel {
    private Grid grid = new Grid();
    private int posX = 11;  // x-coordinate of the grid's upper left corner (in pixels)
    private int posY = 11;  // y-coordinate of the grid's upper left corner (in pixels)

    // a cell has cellSize * cellSize pixels
    private final int cellSize = 31;
    private final Rectangle gridArea;

    GridPanel() {
        gridArea = new Rectangle(posX, posY,
                                grid.getSize() * cellSize,
                                grid.getSize() * cellSize);

        // add MouseInteractionManager

    }

    /**
     * Checks if the given mouse position is over the drawn grid
     * @param mousePos the mouse position to be checked
     * @return true if gridArea contains mousePos
     */
    boolean isMouseOverGrid(Point mousePos) {
        return gridArea.contains(mousePos);
    }

    /**
     * Gets the GridCell under the given mouse position.
     * @param mousePos the mouse position to be checked
     * @return the GridCell corresponding to mousePos
     */
    GridCell getCellUnderMouse(Point mousePos) {
        int cellX = (mousePos.x - posX) / cellSize;
        int cellY = (mousePos.y - posY) / cellSize;

        return grid.getCellAt(cellX, cellY);
    }


    /**
     * Draws the grid and its content (i.e. its inserted blocks).
     * @param g the Graphics object given by paintComponent()
     */
    private void drawGrid(Graphics g) {
        // draw empty grid
        g.drawRect(posX - 1, posY - 1,
                   (cellSize - 1) * grid.getSize() + grid.getSize() + 1,
                   (cellSize - 1) * grid.getSize() + grid.getSize() + 1);
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                g.drawRect(posX + x * cellSize, posY + y * cellSize,
                              cellSize - 1, cellSize - 1);
            }
        }

        // fill grid where cells are non-empty
        g.setColor(Color.GREEN);
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                if (!grid.getCells()[y][x].isEmpty()) {
                    colorCell(g, grid.getCells()[y][x]);
                }
            }
        }

    }

    /**
     * Colors the rectangle on the GridPanel that represents the given GridCell.
     * @param g the Graphics object given by paintComponent() / drawGrid()
     * @param cell the GridCell to be colored
     */
    private void colorCell(Graphics g, GridCell cell) {
        g.fillRect(posX + 1 + cell.getPosX() * cellSize,
                   posY + 1 + cell.getPosY() * cellSize,
                   cellSize - 2,
                   cellSize - 2);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

}
