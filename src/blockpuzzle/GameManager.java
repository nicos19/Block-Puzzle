package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A GameManager is an extended JFrame responsible for managing and visualizing the game.
 */
public class GameManager extends JFrame {
    private final JPanel topPanel = new JPanel();
    private final GridPanel gridPanel = new GridPanel(this);
    private final BlockCombosPanel blockCombosPanel = new BlockCombosPanel(this);
    private final MouseInteractionManager mouseInteractionManager
            = new MouseInteractionManager(this, gridPanel, blockCombosPanel);

    // how many BlockCombos can the player rotate
    private final int initialRotations = 3;
    private int rotations = initialRotations;

    private boolean gameOver = false;

    GameManager() {
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // add panels to frame
        add(topPanel);
        add(gridPanel);
        add(blockCombosPanel);

        // add MouseInteractionManagers to panels
        gridPanel.addMouseListener(mouseInteractionManager);
        gridPanel.addMouseMotionListener(mouseInteractionManager);
        blockCombosPanel.addMouseListener(mouseInteractionManager);
        blockCombosPanel.addMouseMotionListener(mouseInteractionManager);

        // set panel background colors
        Color backgroundColor = new Color(20, 20, 20);
        topPanel.setBackground(backgroundColor);
        gridPanel.setBackground(backgroundColor);
        blockCombosPanel.setBackground(backgroundColor);

        // set panels' sizes
        topPanel.setPreferredSize(new Dimension(300, 50));
        topPanel.setMaximumSize(new Dimension(300, 50));
        gridPanel.setPreferredSize(new Dimension(300, 300));
        gridPanel.setMaximumSize(new Dimension(300, 300));
        blockCombosPanel.setPreferredSize(new Dimension(300, 100));
        blockCombosPanel.setMaximumSize(new Dimension(300, 100));

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    /**
     * Starts a new game.
     */
    void restart() {
        rotations = initialRotations;
        gameOver = false;

        // reset Panels
        //topPanel.reset()
        gridPanel.reset();
        blockCombosPanel.reset();

        // generate initial BlockCombos
        blockCombosPanel.generateNewBlockCombos();
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
        List<BlockCombo> mustUseBlockCombos = blockCombosPanel.getMustUseBlockCombos();
        if (mustUseBlockCombos.isEmpty()) {
            // player has no BlockCombos that must be used this round
            // GameManager starts next round
            return false;
        }

        for (BlockCombo combo : mustUseBlockCombos) {
            // check if combo can be inserted into Grid
            if (gridPanel.getGrid().canInsertBlockCombo(combo)) {
                return false;
            }

            // if rotations available: check if rotated combo can be inserted into Grid
            if (rotations > 0) {
                BlockCombo comboRotated = combo.createCopy();
                for (int i = 0; i < 3; i++) {
                    comboRotated.rotate();
                    if (gridPanel.getGrid().canInsertBlockCombo(comboRotated)) {
                        return false;
                    }
                }
            }
        }

        if (mustUseBlockCombos.size() == 1
                && blockCombosPanel.savedBlockComboIsEmpty()) {
            // case: there is exactly one open BlockCombo, and no BlockCombo is saved
            // player can save the open BlockCombo to continue game
            return false;
        }

        // no BlockCombo insertable -> Game Over
        return true;
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
     * Increases the number of available rotations by one.
     */
    void addRotation() {
        rotations += 1;
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


    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.setPreferredSize(new Dimension(300, 450));
        gameManager.setVisible(true);
    }

}
