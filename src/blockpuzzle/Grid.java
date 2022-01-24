package blockpuzzle;

import java.util.ArrayList;
import java.util.List;

/**
 * A Grid is a play field for the game consisting of GridCells.
 */
public class Grid {
    private final GameManager gameManager;
    private final int size = 9;
    private final GridCell[][] cells;
    private final List<GridCell> recentlyClearedCells = new ArrayList<>();
    private int recentlyClearedTimer = 0;

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
     * Clears all GridCells of this Grid.
     */
    void clear() {
      for (int x = 0; x < size; x++) {
          for (int y = 0; y < size; y++) {
              cells[y][x].clear();
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
     * Gets the list of recentlyClearedCells.
     * @return the recentlyClearedCells
     */
    List<GridCell> getRecentlyClearedCells() {
        return recentlyClearedCells;
    }

    /**
     * Gets the recentlyClearedTimer value.
     * @return the recentlyClearedTimer
     */
    int getRecentlyClearedTimer() {
        return recentlyClearedTimer;
    }

    /**
     * Decreases the value of recentlyClearedTimer by one.
     */
    void decreaseRecentlyClearedTimer() {
        recentlyClearedTimer -= 1;
    }

    /**
     * Clears the list of recentlyClearedCells.
     */
    void clearRecentlyClearedCells() {
        recentlyClearedCells.clear();
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
            boolean rowYFull = true;
            for (int x = 0; x < size; x++) {
                if (cells[y][x].isEmpty()) {
                    // empty cell found
                    rowYFull = false;
                    break;
                }
            }
            if (rowYFull) {
                // row y is full
                fullRows.add(y);
            }
        }

        // find full columns
        for (int x = 0; x < size; x++) {
            boolean columnXFull = true;
            for (int y = 0; y < size; y++) {
                if (cells[y][x].isEmpty()) {
                    // empty cell found
                    columnXFull = false;
                    break;
                }
            }
            if (columnXFull) {
                // column x is full
                fullColumns.add(x);
            }
        }

        // clear full rows
        for (Integer row : fullRows) {
            clearRow(row);
        }

        // clear full columns
        for (Integer column : fullColumns) {
            clearColumn(column);
        }

        if (!fullRows.isEmpty() || !fullColumns.isEmpty()) {
            // remember that cells have just been cleared
            recentlyClearedTimer = 4;

            // update score
            gameManager.updateScore(fullRows, fullColumns);
        }
    }

    /**
     * Clears all GridCells in given row of the Grid.
     * @param y the row
     */
    private void clearRow(int y) {
        for (int x = 0; x < size; x++) {
            // clear cell
            cells[y][x].clear();

            // remember cell as recently cleared
            if (!recentlyClearedCells.contains(cells[y][x])) {
                recentlyClearedCells.add(cells[y][x]);
            }
        }
    }

    /**
     * Clears all GridCells in given column of the Grid.
     * @param x the column
     */
    private void clearColumn(int x) {
        for (int y = 0; y < size; y++) {
            // clear cell
            cells[y][x].clear();

            // remember cell as recently cleared
            if (!recentlyClearedCells.contains(cells[y][x])) {
                recentlyClearedCells.add(cells[y][x]);
            }
        }
    }

    /**
     * Inserts given BlockCombo into the Grid, so that BlockCombo's
     * start block is inserted in given GridCell.
     * Clears all full rows and columns afterwards.
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
        clearFullRowsAndColumns();
    }

    /**
     * Checks if given BlockCombo can be inserted into the Grid, so that
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
     * Checks if given BlockCombo can be inserted anywhere into the Grid.
     * @param combo the BlockCombo to be inserted
     * @return true if BlockCombo can be inserted anywhere, false otherwise
     */
    boolean canInsertBlockCombo(BlockCombo combo) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (canInsertBlockCombo(cells[y][x], combo)) {
                    // combo can be inserted at cells[y][x]
                    return true;
                }
            }
        }

        // combo cannot be inserted anywhere
        return false;
    }

    /**
     * Checks if the given position is outside the Grid.
     * @param posX x-coordinate of the questioned position
     * @param posY y-coordinate of the questioned position
     * @return true if the position is outside the Grid, false otherwise
     */
    boolean positionOutOfBounds(int posX, int posY) {
        return posX < 0 || posX >= size || posY < 0 || posY >= size;
    }

}
