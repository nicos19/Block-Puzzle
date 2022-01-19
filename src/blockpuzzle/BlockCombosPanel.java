package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A BlockCombosPanel is an extended JPanel that contains the logical and visual
 * representation of the (not yet inserted) BlockCombos in the game.
 */
public class BlockCombosPanel extends JPanel {
    private final GameManager gameManager;

    private final SingleContainer<BlockCombo>[] openBlockCombos = new SingleContainer[3];
    private final SingleContainer<BlockCombo> savedBlockCombo = new SingleContainer<>();

    // -1: no selection, 0/1/2: selected openBlockCombos[0/1/2], 3: selected saved combo
    private int selectedBlockCombo = -1;

    // how many rounds has the player to use the saved BlockCombo
    private int remainingRoundsForSavedCombo = 3;


    BlockCombosPanel(GameManager gameM) {
        gameManager = gameM;

        openBlockCombos[0] = new SingleContainer<>();
        openBlockCombos[1] = new SingleContainer<>();
        openBlockCombos[2] = new SingleContainer<>();

        // starts the game by creating the initial three BlockCombos
        generateNewBlockCombos();
    }

    /**
     * Checks if player has any available (open) BlockCombos remaining.
     * @return true if all SingleContainers of openBlockCombos is empty,
     * false otherwise
     */
    boolean openBlockCombosIsEmpty() {
        return openBlockCombos[0].isEmpty() && openBlockCombos[1].isEmpty()
                && openBlockCombos[2].isEmpty();
    }

    /**
     * Generates randomly three new BlockCombos which the player must use next.
     * This is considered as the start of a new round.
     */
    void generateNewBlockCombos() {
        openBlockCombos[0].store(BlockComboCreator.createRandomCombo());
        openBlockCombos[1].store(BlockComboCreator.createRandomCombo());
        openBlockCombos[2].store(BlockComboCreator.createRandomCombo());

        // if a BlockCombo is saved, then its remaining rounds to use are reduced by one
        if (!savedBlockCombo.isEmpty()) {
            remainingRoundsForSavedCombo -= 1;
        }
    }

    /**
     * Checks if any BlockCombo is currently selected.
     * @return true if any BlockCombo is selected, false otherwise
     */
    boolean isAnyBlockComboSelected() {
        return selectedBlockCombo != -1;
    }

    /**
     * Gets the currently selected BlockCombo.
     * Throws IllegalStateException if no BlockCombo is selected.
     * @return the selected BlockCombo
     */
    BlockCombo getSelectedBlockCombo() {
        if (selectedBlockCombo >= 0 && selectedBlockCombo < 3) {
            // some open BlockCombo selected
            return openBlockCombos[selectedBlockCombo].getContent();
        }
        else if (selectedBlockCombo == 3) {
            // saved BlockCombo selected
            return savedBlockCombo.getContent();
        }
        else {
            throw new IllegalStateException("getSelectedBlockCombo() should only " +
                    "be called if any BlockCombo is selected.");
        }
    }

    /**
     * Saves the currently selected BlockCombo by storing in savedBlockCombo.
     * The saved BlockCombo is then removed from openBlockCombos and deselected.
     * Does nothing if savedBlockCombo is not empty.
     */
    void saveSelectedBlockCombo() {
        if (savedBlockCombo.isEmpty()) {
            savedBlockCombo.store(openBlockCombos[selectedBlockCombo].getContent());
            openBlockCombos[selectedBlockCombo].clear();
            deselectBlockCombo();
            remainingRoundsForSavedCombo = 3;
        }
    }

    /**
     * Removes the given BlockCombo from openBlockCombos or savedBlockCombo,
     * depending on given index.
     * Lets GameManager start next round if player used all available BlockCombos.
     * Throws IllegalArgumentException if no BlockCombo is selected.
     */
    void consumeSelectedBlockCombo() {
        if (selectedBlockCombo >= 0 && selectedBlockCombo < 3) {
            openBlockCombos[selectedBlockCombo].clear();
        }
        else if (selectedBlockCombo == 3) {
            savedBlockCombo.clear();
        }
        else {
            throw new IllegalArgumentException("consumeSelectedBlockCombo() should " +
                    "not be called if no BlockCombo is selected.");
        }

        // GameManager starts next round if necessary
        gameManager.tryNextRound();
    }

    /**
     * Selects a BlockCombo in openBlockCombos if
     * player clicked on any painted BlockCombo.
     * @param e the MouseEvent invoked by player's click
     */
    void trySelect(MouseEvent e) {
        // check if player tries to select an open BlockCombo
        for (int i = 0; i < openBlockCombos.length; i++) {
            Rectangle selectionArea = new Rectangle(
                    15 + i * 65, 30, 55, 55);
            if (selectionArea.contains(e.getPoint())) {
                // player tries to select BlockCombo in openBlockCombos[i]
                if (!openBlockCombos[i].isEmpty()) {
                    selectBlockCombo(i);
                }
            }
        }

        // check if player tries to select a saved BlockCombo
        Rectangle selectionArea = new Rectangle(
                15 + 3 * 65 + 20, 30, 55, 55);
        if (selectionArea.contains(e.getPoint())) {
            // player tries to select BlockCombo in savedBlockCombo
            if (!savedBlockCombo.isEmpty()) {
                selectBlockCombo(3);
            }
        }
    }

    /**
     * Selects the BlockCombo represented by given index.
     * Former selected BlockCombo resets its rotation.
     * @param index 0/1/2 represents the BlockCombos in openBlockCombos[0/1/2],
     *              3 represents the BlockCombo in savedBlockCombo
     */
    void selectBlockCombo(int index) {
        if (selectedBlockCombo != -1) {
            // reset rotation of old selected BlockCombo
            getSelectedBlockCombo().resetRotation();
        }

        selectedBlockCombo = index;
    }

    /**
     * Deselects the currently selected BlockCombo.
     */
    void deselectBlockCombo() {
        selectedBlockCombo = -1;
    }

    /**
     * Draws the open and saved block combos.
     * @param g the Graphics object given by paintComponent()
     */
    private void drawBlockCombos(Graphics g) {
        // draw areas for three open BlockCombos
        for (int i = 0; i < 3; i++) {
            g.drawRect(15 + i * 65, 30, 55, 55);
        }
        // draw area for saved BlockCombo
        g.drawRect(15 + 3 * 65 + 20, 30, 55, 55);

        // draw open BlockCombos
        for (int i = 0; i < openBlockCombos.length; i++) {
            if (!openBlockCombos[i].isEmpty()) {
                BlockCombo combo = openBlockCombos[i].getContent();
                int[] initialPosition = {39 + i * 65, 54};
                drawSingleBlockCombo(g, combo, initialPosition);
            }
        }

        // draw saved BlockCombo (if saved any)
        if (!savedBlockCombo.isEmpty()) {
            int[] initialPosition = {39 + 3 * 65 + 20, 54};
            drawSingleBlockCombo(g, savedBlockCombo.getContent(), initialPosition);
        }

        // draw remainingRemainingRoundsForSavedCombo
        g.drawString(String.valueOf(remainingRoundsForSavedCombo), 255, 27);

        // draw number of remaining rotations
        g.drawString("Rotations: " + gameManager.getRotations(), 15, 20);
        if (isAnyBlockComboSelected() && getSelectedBlockCombo().isRotated()) {
            g.setColor(Color.RED);
            g.drawString("-1", 100, 20);
            g.setColor(Color.BLACK);
        }

        // draw selection
        drawSelection(g);
    }

    /**
     * Draws the given BlockCombo to this BlockCombosPanel.
     * @param g the Graphics object given by paintComponent()
     * @param combo the BlockCombo to be drawn
     * @param initialPosition the position for the BlockCombo's start block
     *                        (without offset)
     */
    private void drawSingleBlockCombo(Graphics g, BlockCombo combo,
                                      int[] initialPosition) {
        // calculate offset so that combo is drawn centrally
        double[] offsetInBlocks = combo.getDrawOffset();
        int[] offsetInPixels = {(int)(offsetInBlocks[0] * 9),
                                (int)(offsetInBlocks[1] * 9)};

        // draw all blocks of combo
        for (int[] block : combo.getComboFormation()) {
            g.fillRect(initialPosition[0] + block[0] * 9 + offsetInPixels[0],
                       initialPosition[1] + block[1] * 9 + offsetInPixels[1],
                       8, 8);
        }
    }

    /**
     * Highlights the screen area of the selected BlockCombo if any is selected.
     * @param g the Graphics object given by paintComponent()
     */
    private void drawSelection(Graphics g) {
        g.setColor(Color.GREEN);

        switch(selectedBlockCombo) {
            case -1:
                // no selection
                break;
            case 3:
                // saved BlockCombo selected
                g.drawRect(15 + 3 * 65 + 20, 30, 55, 55);
                g.drawRect(14 + 3 * 65 + 20, 29, 57, 57);
                break;
            default:
                // some open BlockCombo selected
                g.drawRect(15 + selectedBlockCombo * 65, 30, 55, 55);
                g.drawRect(14 + selectedBlockCombo * 65, 29, 57, 57);
        }

        g.setColor(Color.BLACK);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBlockCombos(g);
    }


}
