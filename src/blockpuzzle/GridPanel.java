package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A GridPanel is an extended JPanel that contains
 * the logical and visual representation of a Grid.
 */
public class GridPanel extends JPanel {
    private final GameManager gameManager;

    private final Grid grid;
    private final Map<GridCell, Color> highlightedCells = new HashMap<>();

    // x-coordinate of the grid's upper left corner (in pixels)
    private final int posX = 11;
    // y-coordinate of the grid's upper left corner (in pixels)
    private final int posY = 11;

    // a cell has cellSize * cellSize pixels
    private final int cellSize = 31;
    private final Rectangle gridArea;

    GridPanel(GameManager gameM) {
        gameManager = gameM;

        grid = new Grid(gameM);
        gridArea = new Rectangle(posX, posY,
                                grid.getSize() * cellSize,
                                grid.getSize() * cellSize);
    }

    /**
     * Restores the GridPanel based on the game state represented by given savegame.
     * @param savegame the Savegame representing the game state to be restored
     */
    void restorePanel(Savegame savegame) {
        // fill cells that are marked as filled in savegame's grid
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                if (savegame.getGrid()[y][x]) {
                    // cell at (x, y) shall be full
                    grid.getCellAt(x, y).fill();
                }
            }
        }
    }

    /**
     * Resets the GridPanel (and so clears the Grid) to its initial state.
     */
    void reset() {
        grid.clear();
    }

    /**
     * Gets the grid of this GridPanel.
     * @return the grid.
     */
    Grid getGrid() {
        return grid;
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
     * Calculates all GridCells that shall be highlighted and remembers them in
     * highlightedCells.
     * A GridCell shall be highlighted if and only if it would be filled if
     * given BlockCombo is inserted in given targetCell.
     * @param combo the BlockCombo that triggers the highlighting
     * @param targetCell the GridCell corresponding with combo's start block
     */
    void calculateHighlightedCells(BlockCombo combo, GridCell targetCell, Color color) {
        highlightedCells.clear();

        for (int[] block : combo.getComboFormation()) {
            // get position that shall be highlighted
            int[] highlightedPosition = {targetCell.getPosX() + block[0],
                                         targetCell.getPosY() + block[1]};
            // check if highlightedPosition is inside the grid
            if (!grid.positionOutOfBounds(highlightedPosition[0],
                                          highlightedPosition[1])) {
                // remember the GridCell corresponding to highlightedPosition
                highlightedCells.put(grid.getCellAt(highlightedPosition[0],
                                                    highlightedPosition[1]),
                                     color);
            }
        }
    }

    /**
     * Clears the list of highlightedCells.
     */
    void clearHighlightedCells() {
        highlightedCells.clear();
    }

    /**
     * Draws the grid and its content (i.e. its inserted blocks).
     * @param g the Graphics object given by paintComponent()
     */
    private void drawGrid(Graphics g) {
        // draw empty grid
        g.setColor(Color.BLACK);
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
        g.setColor(Color.GRAY);
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                if (!grid.getCells()[y][x].isEmpty()) {
                    colorCell(g, grid.getCells()[y][x]);
                }
                else {
                    // paint empty cells in light gray
                    g.setColor(new Color(230, 230, 230));
                    colorCell(g, grid.getCells()[y][x]);
                    g.setColor(Color.GRAY);
                }
            }
        }

        // show effect for recently cleared cells
        switch(grid.getRecentlyClearedTimer()) {
            case 3:
                for (GridCell clearedCell : grid.getRecentlyClearedCells()) {
                    //g.setColor(new Color(155, 155, 155));
                    g.setColor(new Color(0, 153, 0));
                    colorCell(g, clearedCell);
                    g.setColor(Color.GRAY);
                }
                break;
            case 2:
                for (GridCell clearedCell : grid.getRecentlyClearedCells()) {
                    g.setColor(new Color(180, 180, 180));
                    colorCell(g, clearedCell);
                    g.setColor(Color.GRAY);
                }
                break;
            case 1:
                for (GridCell clearedCell : grid.getRecentlyClearedCells()) {
                    g.setColor(new Color(205, 205, 205));
                    colorCell(g, clearedCell);
                    g.setColor(Color.GRAY);
                }
                break;
        }

        // highlight cells
        for (GridCell cell : highlightedCells.keySet()) {
            if (cell.isEmpty()) {
                g.setColor(highlightedCells.get(cell));
                colorCell(g, cell);
            }
            else {
                g.setColor(highlightedCells.get(cell).darker().darker());
                colorCell(g, cell);
            }
        }

        // if game is over, draw "GAME OVER" writing
        if(gameManager.isGameOver()) {
            drawGameOverWriting(g);
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

    /**
     * Draws a "GAME OVER" writing.
     * @param g the Graphics object given by paintComponent() / drawGrid()
     */
    private void drawGameOverWriting(Graphics g) {
        // fade the Grid
        g.setColor(new Color(255, 255, 255, 170));
        g.fillRect(posX - 1, posY - 1,
                (cellSize - 1) * grid.getSize() + grid.getSize() + 2,
                (cellSize - 1) * grid.getSize() + grid.getSize() + 2);
        g.setColor(new Color(20, 20, 20));

        // draw "GAME OVER"
        g.setFont(new Font("Monospaced", Font.BOLD, 105));
        g.drawString("GAME", posX + 13, posY + 109);
        g.drawString("OVER", posX + 13, posY + 202);

        // tell player that click restarts game
        g.setFont(new Font("Monospaced", Font.BOLD, 26));
        g.drawString("Click to Restart", posX + 12, posY + 270);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

}
