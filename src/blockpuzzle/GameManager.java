package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A GameManager is an extended JFrame responsible for managing and visualizing the game.
 */
public class GameManager extends JFrame {
    private final ScorePanel scorePanel = new ScorePanel();
    private final GridPanel gridPanel = new GridPanel(this);
    private final BlockCombosPanel blockCombosPanel = new BlockCombosPanel(this);
    private final MouseInteractionManager mouseInteractionManager
            = new MouseInteractionManager(this, gridPanel, blockCombosPanel);
    private final SaveManager saveManager
            = new SaveManager(this, scorePanel, gridPanel, blockCombosPanel);

    // how many BlockCombos can the player rotate
    private final int initialRotations = 3;
    private int rotations = initialRotations;

    // when nextRotation reaches 100, player gets a new rotation
    private int nextRotation = 0;

    private boolean gameOver = false;

    GameManager() {
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // add panels to frame
        add(scorePanel);
        add(gridPanel);
        add(blockCombosPanel);

        // add MouseInteractionManagers to panels
        gridPanel.addMouseListener(mouseInteractionManager);
        gridPanel.addMouseMotionListener(mouseInteractionManager);
        blockCombosPanel.addMouseListener(mouseInteractionManager);
        blockCombosPanel.addMouseMotionListener(mouseInteractionManager);

        // set panel background colors
        Color backgroundColor = new Color(20, 20, 20);
        scorePanel.setBackground(backgroundColor);
        gridPanel.setBackground(backgroundColor);
        blockCombosPanel.setBackground(backgroundColor);

        // set panels' sizes
        scorePanel.setPreferredSize(new Dimension(300, 50));
        scorePanel.setMaximumSize(new Dimension(300, 50));
        gridPanel.setPreferredSize(new Dimension(300, 300));
        gridPanel.setMaximumSize(new Dimension(300, 300));
        blockCombosPanel.setPreferredSize(new Dimension(300, 100));
        blockCombosPanel.setMaximumSize(new Dimension(300, 100));

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    /**
     * Saves the state of the current game.
     */
    void saveGame() {
        saveManager.saveGameState();
    }

    /**
     * Restores the state of a previously saved game if a savegame file exists.
     */
    void restoreGame() {
        Savegame savegame = saveManager.loadSavegame();
        if (savegame != null) {
            // a Savegame has been loaded -> restore saved game state
            saveManager.restoreGameState(savegame);
        }
    }

    /**
     * Starts a new game.
     */
    void restart() {
        rotations = initialRotations;
        nextRotation = 0;
        gameOver = false;

        // reset Panels
        scorePanel.reset();
        gridPanel.reset();
        blockCombosPanel.reset();

        // generate initial BlockCombos
        blockCombosPanel.generateNewBlockCombos();
    }

    /**
     * Restores the GameManager based on the game
     * state represented by the given savegame.
     * @param savegame the Savegame representing the game state to be restored
     */
    void restoreGameManager(Savegame savegame) {
        rotations = savegame.getRotations();
        nextRotation = savegame.getNextRotation();
        gameOver = savegame.isGameOver();
    }

    /**
     * Starts next round of the game if current round is over, i.e. if
     * player used all of its available BlockCombos.
     * Next round results in three new BlockCombos becoming available.
     * Checks for game over after BlockCombo generating.
     */
    void tryNextRound() {
        if (blockCombosPanel.openBlockCombosIsEmpty()
                && !blockCombosPanel.hasUrgentSavedCombo()) {
            // start next round
            blockCombosPanel.generateNewBlockCombos();
            repaint();
        }

        // check for game over
        tryGameOver();
    }

    /**
     * Checks if the game is over and remembers if it is.
     */
    void tryGameOver() {
        if (checkForGameOver()) {
            gameOver = true;
        }
    }

    /**
     * Checks if the game is over, i.e. if the player has BlockCombos available that must
     * be used before the next round starts and no such BlockCombo can be inserted.
     * @return true if the game is over, false otherwise
     */
    private boolean checkForGameOver() {
        SingleContainer<BlockCombo>[] openCombos = blockCombosPanel.getOpenBlockCombos();
        SingleContainer<BlockCombo> savedCombo = blockCombosPanel.getSavedBlockCombo();

        if (blockCombosPanel.openBlockCombosIsEmpty()) {
            // no open BlockCombos
            if (!savedCombo.isEmpty()
                    && blockCombosPanel.getRemainingRoundsForSavedCombo() == 0) {
                // there is a saved BlockCombo that must be used this round
                if (!gridPanel.getGrid().canInsertBlockCombo(savedCombo.getContent(),
                        rotations > 0)) {
                    // savedCombo cannot be inserted
                    return true;
                }
            }
            return false;
        }
        else {
            // there is at least one open BlockCombo
            for (SingleContainer<BlockCombo> container : openCombos) {
                if (!container.isEmpty()) {
                    if (gridPanel.getGrid().canInsertBlockCombo(
                            container.getContent(), rotations > 0)) {
                        // BlockCombo in container can be inserted
                        return false;
                    }
                }
            }

            // no open BlockCombo can be inserted

            if (savedCombo.isEmpty()) {
                // no saved BlockCombo (-> one open BlockCombo could be saved)
                // GAME OVER if there are multiple open BlockCombos
                return blockCombosPanel.getNumberOfOpenBlockCombos() > 1;
            }
            else {
                // GAME OVER if saved BlockCombo cannot be inserted, too
                return !gridPanel.getGrid().canInsertBlockCombo(savedCombo.getContent(),
                        rotations > 0);
            }
        }
    }

    /**
     * Checks if the game was declared as over.
     * @return true if game is over, false otherwise
     */
    boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the number of rotations.
     * @return the rotations
     */
    int getRotations() {
        return rotations;
    }

    /**
     * Gets nextRotation.
     * @return nextRotation
     */
    int getNextRotation() {
        return nextRotation;
    }

    /**
     * Increases the number of available rotations by one.
     */
    void addRotation() {
        rotations += 1;
    }

    /**
     * Updates nextRotation depending on the number of recently cleared rows and columns.
     * If nextRotation reaches 100, the player gets a new rotation.
     * @param clearedRowsAndColumns the number of recently cleared rows and columns
     */
    void updateNextRotation(int clearedRowsAndColumns) {
        // nextRotation increases if player cleared at least two rows and columns
        nextRotation += (clearedRowsAndColumns - 1) * 20;

        // add rotation if necessary
        while (nextRotation >= 100) {
            addRotation();
            nextRotation -= 100;
        }
        // now: nextRotation < 100
    }

    /**
     * Decreases the number of available rotations by one.
     * Throws IllegalStateException if no rotations are available.
     */
    void consumeRotation() {
        if (rotations == 0) {
            throw new IllegalStateException(
                    "useRotation() should not be called if rotations == 0.");
        }
        rotations -= 1;
    }

    /**
     * Updates the score of the current game depending on how many cells and
     * rows/columns have just been cleared.
     * scoreToAdd = 10 * numberOfClearedCells * numberOfClearedRowsAndColumns
     * Unlocks new rotation if nextRotation exceeds 100
     * @param clearedRows the list of rows which have just been cleared
     * @param clearedColumns the list of columns which have just been cleared
     */
    void updateScore(List<Integer> clearedRows, List<Integer> clearedColumns) {
        int gridSize = gridPanel.getGrid().getSize();

        int numberOfClearedRowsAndColumns = clearedRows.size() + clearedColumns.size();
        int numberOfClearedCells =
                gridSize * clearedRows.size()
                        + (gridSize - clearedRows.size()) * clearedColumns.size();

        int scoreToAdd = 10 * numberOfClearedCells * numberOfClearedRowsAndColumns;

        // add rotation if necessary
        updateNextRotation(numberOfClearedRowsAndColumns);

        // increase the score
        scorePanel.increaseScoreBy(scoreToAdd);
    }


    public static void main(String[] args) {
        // start game
        GameManager gameManager = new GameManager();
        gameManager.setPreferredSize(new Dimension(300, 450));
        gameManager.setVisible(true);

        // try loading and restoring savegame
        gameManager.restoreGame();

        // before application exit
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                // save current game state
                gameManager.saveGame();
            }
        });

    }

}
