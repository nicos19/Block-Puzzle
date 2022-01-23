package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A BlockCombosPanel is an extended JPanel that contains the logical and visual
 * representation of the (not yet inserted) BlockCombos in the game.
 */
public class BlockCombosPanel extends JPanel {
    private final GameManager gameManager;

    private final SingleContainer<BlockCombo>[] openBlockCombos = new SingleContainer[3];
    private final SingleContainer<BlockCombo> savedBlockCombo = new SingleContainer<>();

    // -1: no highlighting, 0/1/2: highlight openBlockCombos[0/1/2],
    // 3: highlight saved combo, 4: highlight empty area for saved combos
    private int highlightedComboArea = -1;

    // -1: no selection, 0/1/2: selected openBlockCombos[0/1/2], 3: selected saved combo
    private int selectedBlockCombo = -1;

    // how many rounds has the player to use the saved BlockCombo
    private final int maximumRemainingRoundsForSavedCombo = 4;
    private int remainingRoundsForSavedCombo = maximumRemainingRoundsForSavedCombo;


    BlockCombosPanel(GameManager gameM) {
        gameManager = gameM;

        openBlockCombos[0] = new SingleContainer<>();
        openBlockCombos[1] = new SingleContainer<>();
        openBlockCombos[2] = new SingleContainer<>();

        // starts the game by creating the initial three BlockCombos
        generateNewBlockCombos();
    }

    /**
     * Resets the BlockCombosPanel to its initial state.
     * All open or saved BlockCombos are removed.
     */
    void reset() {
        // clear openBlockCombos
        for (SingleContainer<BlockCombo> container : openBlockCombos) {
            container.clear();
        }
        // clear savedBlockCombo
        savedBlockCombo.clear();
        remainingRoundsForSavedCombo = maximumRemainingRoundsForSavedCombo;
    }

    /**
     * Gets the list of all BlockCombos that must be used in the current
     * round of the game, i.e. before new BlockCombos are generated.
     * @return the BlockCombo to be used in this round
     */
    List<BlockCombo> getMustUseBlockCombos() {
        List<BlockCombo> mustUseCombos = new ArrayList<>();

        // get all open BlockCombos
        for (SingleContainer<BlockCombo> container : openBlockCombos) {
            if (!container.isEmpty()) {
                mustUseCombos.add(container.getContent());
            }
        }

        // get saved BlockCombo if it must be used this round
        if (!savedBlockCombo.isEmpty() && remainingRoundsForSavedCombo == 0) {
            mustUseCombos.add(savedBlockCombo.getContent());
        }

        return mustUseCombos;
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
     * Checks if there is any BlockCombo saved.
     * @return true if savedBlockCombo is empty, false otherwise
     */
    boolean savedBlockComboIsEmpty() {
        return savedBlockCombo.isEmpty();
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
     * Checks if player saved previously a BlockCombo which must be used in this round.
     * @return true if there is a saved BlockCombo with 0 remaining rounds to use
     */
    boolean hasUrgentSavedCombo() {
        return !savedBlockCombo.isEmpty() && remainingRoundsForSavedCombo == 0;
    }

    /**
     * Saves the currently selected BlockCombo by storing in savedBlockCombo.
     * The saved BlockCombo is then removed from openBlockCombos and deselected.
     * Lets GameManager start next round if openBlockCombos is empty after saving.
     * Does nothing if savedBlockCombo is not empty.
     */
    void saveSelectedBlockCombo() {
        if (savedBlockCombo.isEmpty()) {
            // save BlockCombo
            savedBlockCombo.store(openBlockCombos[selectedBlockCombo].getContent());
            remainingRoundsForSavedCombo = maximumRemainingRoundsForSavedCombo;

            // reset rotation of saved BlockCombo
            getSelectedBlockCombo().resetRotation();

            // remove saved BlockCombo from openBlockCombos and deselect it
            openBlockCombos[selectedBlockCombo].clear();
            deselectBlockCombo();

            highlightedComboArea = 3;  // remember that mouse is over saved BlockCombo

            // GameManager starts next round if necessary
            gameManager.tryNextRound();
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
     * Highlights an area of an open or saved BlockCombo if player is hovering over one.
     * @param e the MouseEvent invoked by player's click
     */
    void highlightBlockComboAreas(MouseEvent e) {
        highlightedComboArea = -1;

        // check if player hovers over open BlockCombos
        for (int i = 0; i < openBlockCombos.length; i++) {
            Rectangle selectionArea = new Rectangle(
                    15 + i * 65, 30, 55, 55);
            if (selectionArea.contains(e.getPoint())) {
                // player hovers over BlockCombo in openBlockCombos[i]
                if (!openBlockCombos[i].isEmpty() && selectedBlockCombo != i) {
                    highlightedComboArea = i;
                }
            }
        }

        // check if player hovers over saved BlockCombo
        Rectangle selectionArea = new Rectangle(
                15 + 3 * 65 + 20, 30, 55, 55);
        if (selectionArea.contains(e.getPoint())) {
            if (!savedBlockCombo.isEmpty() && selectedBlockCombo != 3) {
                // player hovers over BlockCombo in savedBlockCombo
                highlightedComboArea = 3;
            }
            else if (savedBlockCombo.isEmpty() && isAnyBlockComboSelected()) {
                // player hovers over empty savedBlockCombo area
                highlightedComboArea = 4;
            }
        }
    }

    /**
     * Saves the currently selected BlockCombo if player clicked on screen
     * area for saved BlockCombos and no BlockCombo is saved yet.
     * @param e the MouseEvent invoked by player's click
     */
    void trySave(MouseEvent e) {
        Rectangle selectionArea = new Rectangle(
                15 + 3 * 65 + 20, 30, 55, 55);
        if (selectionArea.contains(e.getPoint())) {
            // player clicked in savedBlockCombo area
            if (savedBlockCombo.isEmpty() && selectedBlockCombo >= 0) {
                // some BlockCombo is selected and no BlockCombo is currently saved
                // player wants to save the selected BlockCombo
                saveSelectedBlockCombo();
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

        // set new selected BlockCombo
        selectedBlockCombo = index;
        highlightedComboArea = -1;
    }

    /**
     * Deselects the currently selected BlockCombo and resets its rotation.
     */
    void deselectBlockCombo() {
        selectedBlockCombo = -1;
    }

    /**
     * Draws the open and saved block combos.
     * @param g the Graphics object given by paintComponent()
     */
    private void drawBlockCombos(Graphics g) {
        Color standardColor = Color.GRAY;
        g.setColor(standardColor);

        // draw areas for three open BlockCombos
        for (int i = 0; i < 3; i++) {
            g.drawRect(15 + i * 65, 30, 55, 55);
        }
        // draw area for saved BlockCombo
        g.drawRect(15 + 3 * 65 + 20, 30, 55, 55);

        // draw highlighting for these four areas
        drawComboAreaHighlighting(g);
        g.setColor(standardColor);

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
            // highlight area for saved BlockCombo if remainingRounds == 0
            if (remainingRoundsForSavedCombo == 0) {
                g.setColor(new Color(200, 0, 0));
                g.drawRect(15 + 3 * 65 + 20, 30, 55, 55);
                g.setColor(standardColor);
            }
        }

        // draw remainingRemainingRoundsForSavedCombo (if any combo saved)
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        if (!savedBlockCombo.isEmpty()) {
            if (remainingRoundsForSavedCombo != maximumRemainingRoundsForSavedCombo)  {
                if (remainingRoundsForSavedCombo == 0) {
                    g.setColor(new Color(200, 0, 0));
                }
                g.drawString(String.valueOf(remainingRoundsForSavedCombo), 255, 26);
                g.setColor(standardColor);
            }
        }

        // draw number of remaining rotations
        g.drawString("Rotations: " + gameManager.getRotations(), 15, 16);
        if (isAnyBlockComboSelected() && getSelectedBlockCombo().isRotated()) {
            g.setColor(Color.RED);
            g.drawString(" -1", 100, 16);
            g.setColor(standardColor);
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
        g.setColor(new Color(50, 170, 10));

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
    }

    /**
     * Highlights the screen area of either one of the open BlockCombos or of the
     * saved BlockCombo. This Highlighting marks that the highlighted BlockCombo
     * can be selected or (for empty area of saved BlockCombo) that the currently
     * selected BlockCombo can be saved.
     * @param g the Graphics object given by paintComponent()
     */
    private void drawComboAreaHighlighting(Graphics g) {
        g.setColor(g.getColor().darker().darker().darker());

        switch(highlightedComboArea) {
            case -1:
                // no highlighting
                break;
            case 3:
                if (gameManager.isGameOver()) {
                    highlightedComboArea = -1;
                    break;
                }
                // saved BlockCombo is highlighted
                g.fillRect(16 + 3 * 65 + 20, 31, 54, 54);
                break;
            case 4:
                // empty area for saved BlockCombos is highlighted
                g.fillRect(16 + 3 * 65 + 20, 31, 54, 54);
                g.setColor(g.getColor().brighter().brighter().brighter());
                g.drawString("Save", 15 + 3 * 65 + 20 + 15, 61);
                break;
            default:
                // one of the open BlockCombos is highlighted
                g.fillRect(16 + highlightedComboArea * 65, 31,
                           54, 54);
                break;
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBlockCombos(g);
    }


}
