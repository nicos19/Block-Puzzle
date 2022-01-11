package blockpuzzle;

/**
 * A Grid is a play field for the game consisting of GridCells.
 */
public class Grid {
    private final int size = 9;
    private GridCell[][] cells;

    Grid() {
        cells = new GridCell[size][size];

        // fill Grid with GridCells
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                cells[y][x] = new GridCell(x, y);
            }
        }
    }

}
