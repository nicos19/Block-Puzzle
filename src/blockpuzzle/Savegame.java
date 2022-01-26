package blockpuzzle;

import java.io.Serial;
import java.io.Serializable;

/**
 * A Savegame instance describes the state of the last saved
 * game including the best score reached in any game so far.
 */
public class Savegame implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int rotations;
    private final int nextRotation;
    private final boolean gameOver;

    private final int highScore;
    private final int score;

    private final boolean[][] grid;

    private final BlockComboSave[] openCombos = new BlockComboSave[3];

    private BlockComboSave savedCombo;
    private final int remainingRoundsForSavedCombo;


    Savegame(GameManager gameManager, ScorePanel scorePanel,
             GridPanel gridPanel, BlockCombosPanel blockCombosPanel) {
        // from GameManager
        rotations = gameManager.getRotations();
        nextRotation = gameManager.getNextRotation();
        gameOver = gameManager.isGameOver();

        // from ScorePanel
        highScore = scorePanel.getHighScore();
        score = scorePanel.getScore();

        // from GridPanel
        grid = createGridArray(gridPanel.getGrid());

        // from BlockCombosPanel
        setBlockComboSaves(blockCombosPanel);
        remainingRoundsForSavedCombo
                = blockCombosPanel.getRemainingRoundsForSavedCombo();
    }


    /**
     * Creates the 2-dimensional array that represents the Grid's current state.
     * array[y][x] == true if and only if cell at (x, y) is NOT empty
     * @param grid the Grid that shall be converted to an array
     * @return the array representing the Grid
     */
    boolean[][] createGridArray(Grid grid) {
        boolean[][] gridArray = new boolean[grid.getSize()][grid.getSize()];

        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                gridArray[y][x] = !grid.getCellAt(x, y).isEmpty();
            }
        }

        return gridArray;
    }

    /**
     * Sets firstCombo, secondCombo, thirdCombo and savedCombo depending on the
     * state of the given BlockCombosPanel.
     * @param blockCombosPanel the BlockCombosPanel which state shall be saved
     */
    void setBlockComboSaves(BlockCombosPanel blockCombosPanel) {
        SingleContainer<BlockCombo>[] openBlockCombos
                = blockCombosPanel.getOpenBlockCombos();
        openCombos[0] = new BlockComboSave(openBlockCombos[0]);
        openCombos[1] = new BlockComboSave(openBlockCombos[1]);
        openCombos[2] = new BlockComboSave(openBlockCombos[2]);
        savedCombo = new BlockComboSave(blockCombosPanel.getSavedBlockCombo());
    }


    // ----------------------------------------------------------------------------------
    // getter for Savegame fields
    // ----------------------------------------------------------------------------------

    int getRotations() {
        return rotations;
    }

    int getNextRotation() {
        return nextRotation;
    }

    boolean isGameOver() {
        return gameOver;
    }

    int getHighScore() {
        return highScore;
    }

    int getScore() {
        return score;
    }

    boolean[][] getGrid() {
        return grid;
    }

    BlockComboSave[] getOpenCombos() {
        return openCombos;
    }

    BlockComboSave getSavedCombo() {
        return savedCombo;
    }

    int getRemainingRoundsForSavedCombo() {
        return remainingRoundsForSavedCombo;
    }

}
