package blockpuzzle;

/**
 * A GridCell represents a single cell in a Grid.
 */
public class GridCell {
    private final int posX;
    private final int posY;
    private boolean empty = true;

    GridCell(int positionX, int positionY) {
        posX = positionX;
        posY = positionY;
    }

    /**
     * Gets the x-coordinate of this GridCell.
     * @return posX
     */
    int getPosX() {
        return posX;
    }

    /**
     * Gets the y-coordinate of this GridCell.
     * @return posY
     */
    int getPosY() {
        return posY;
    }

    /**
     * Checks whether this GridCell is empty or not
     * @return true if empty == true, false otherwise
     */
    boolean isEmpty() {
        return empty;
    }

    /**
     * Sets empty = true.
     */
    void clear() {
        empty = true;
    }

    /**
     * Sets empty = false.
     * Throws IllegalStateException if GridCell is not empty.
     */
    void fill() {
        if (!empty) {
            throw new IllegalStateException("fill() must not be called " +
                    "when GridCell is not empty.");
        }
        empty = false;
    }



}
