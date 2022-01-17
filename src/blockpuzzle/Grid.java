package blockpuzzle;

import java.util.ArrayList;
import java.util.List;

/**
 * A Grid is a play field for the game consisting of GridCells.
 */
public class Grid {
    private GameManager gameManager;
    private final int size = 9;
    private final GridCell[][] cells;

    Grid(GameManager manager) {
        gameManager = manager;
        cells = new GridCell[size][size];

        // fill Grid with GridCells
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                cells[y][x] = new GridCell(x, y);
            }
        }
    }

    /**
     * Gets the size of this Grid.
     * @return the size
     */
    int getSize() {
        return size;
    }

    /**
     * Gets the GridCell at given position.
     * @param x the x-position of the GridCell
     * @param y the y-position of the GridCell
     * @return the GridCell at (x, y)
     */
    GridCell getCellAt(int x, int y) {
        return cells[y][x];
    }

    /**
     * Gets the GridCells contained by this Grid.
     * @return the cells
     */
    GridCell[][] getCells() {
        return cells;
    }

    /**
     * Identifies all rows and columns of the Grid whose
     * GridCells are all full and clears them.
     */
    void clearFullRowsAndColumns() {
        List<Integer> fullRows = new ArrayList<>();
        List<Integer> fullColumns = new ArrayList<>();

        // find full rows
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (cells[y][x].isEmpty()) {
                    // empty cell found
                    break;
                }
            }
            // row y is full
            fullRows.add(y);
        }

        // find full columns
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (cells[y][x].isEmpty()) {
                    // empty cell found
                    break;
                }
            }
            // column x is full
            fullColumns.add(x);
        }

        // clear full rows
        for (Integer row : fullRows) {
            clearRow(row);
        }

        // clear full columns
        for (Integer column : fullColumns) {
            clearColumn(column);
        }

    }

    /**
     * Clears all GridCells in given row of the Grid.
     * @param y the row
     */
    private void clearRow(int y) {
        for (int x = 0; x < size; x++) {
            cells[y][x].clear();
        }
    }

    /**
     * Clears all GridCells in given column of the Grid.
     * @param x the column
     */
    private void clearColumn(int x) {
        for (int y = 0; y < size; y++) {
            cells[y][x].clear();
        }
    }

    /**
     * Inserts given BlockCombo into the Grid, so that BlockCombo's
     * start block is inserted in given GridCell.
     * @param cell the GridCell for the start block
     * @param combo the BlockCombo to be inserted
     */
    void insertBlockCombo(GridCell cell, BlockCombo combo) {
        for (int[] block : combo.getComboFormation()) {
            // find target cell for block
            GridCell targetCell = getCellAt(cell.getPosX() + block[0],
                                            cell.getPosY() + block[1]);
            // fill target cell
            targetCell.fill();
        }

        // GameManager starts next round if necessary
        gameManager.tryNextRound();
    }

    /**
     * Checks if given BlockCombo can be inserted into the grid, so that
     * BlockCombo's start block is inserted in given GridCell.
     * @param cell the GridCell for the start block
     * @param combo the BlockCombo to be inserted
     * @return true if BlockCombo can be inserted, false otherwise
     */
    boolean canInsertBlockCombo(GridCell cell, BlockCombo combo) {
        for (int[] block : combo.getComboFormation()) {
            // find position of target cell for block
            int targetCellX = cell.getPosX() + block[0];
            int targetCellY = cell.getPosY() + block[1];
            if (positionOutOfBounds(targetCellX, targetCellY)) {
                // block would be placed out of bounds of the Grid
                return false;
            }

            // check if target cell is empty
            GridCell targetCell = getCellAt(targetCellX, targetCellY);
            if (!targetCell.isEmpty()) {
                // block's target cell is non-empty
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the given position is outside the Grid.
     * @param posX x-coordinate of the questioned position
     * @param posY y-coordinate of the questioned position
     * @return true if the position is outside the Grid, false otherwise
     */
    private boolean positionOutOfBounds(int posX, int posY) {
        return posX < 0 || posX >= size || posY < 0 || posY >= size;
    }


}
