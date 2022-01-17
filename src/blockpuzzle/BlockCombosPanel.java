package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A BlockCombosPanel is an extended JPanel that contains the logical and visual
 * representation of the (not yet inserted) BlockCombos in the game.
 */
public class BlockCombosPanel extends JPanel {
    private final List<BlockCombo> openBlockCombos = new ArrayList<>();

    // this list has always at most one item
    private final List<BlockCombo> savedBlockCombo = new ArrayList<>();
    // how many rounds has the player to use the saved BlockCombo
    private int remainingRoundsForSavedCombo = 3;

    // this list has always at most one item
    private final List<BlockCombo> selectedBlockCombo = new ArrayList<>();

    BlockCombosPanel() {
        // starts the game by creating the initial three BlockCombos
        generateNewBlockCombos();

        // add MouseInteractionManager

    }

    /**
     * Checks if player has any available (open) BlockCombos remaining.
     * @return true if openBlockCombos is empty, false otherwise
     */
    boolean openBlockCombosIsEmpty() {
        return openBlockCombos.isEmpty();
    }

    /**
     * Generates randomly three new BlockCombos which the player must use next.
     * This is considered as the start of a new round.
     */
    void generateNewBlockCombos() {
        openBlockCombos.add(BlockComboCreator.createRandomCombo());
        openBlockCombos.add(BlockComboCreator.createRandomCombo());
        openBlockCombos.add(BlockComboCreator.createRandomCombo());

        // ff a BlockCombo is saved, then its remaining rounds to use are reduced by one
        if (!savedBlockCombo.isEmpty()) {
            remainingRoundsForSavedCombo -= 1;
        }
    }

    /**
     * Saves the given BlockCombo by adding to savedBlockCombo.
     * The saved BlockCombo is then removed from openBlockCombos.
     * Does nothing if savedBlockCombo is not empty.
     * @param combo the BlockCombo to be saved
     */
    void saveBlockCombo(BlockCombo combo) {
        if (savedBlockCombo.isEmpty()) {
            savedBlockCombo.add(combo);
            openBlockCombos.remove(combo);
            remainingRoundsForSavedCombo = 3;
        }
    }

    /**
     * Selects the given BlockCombo by adding to selectedBlockCombo.
     * If selectedBlockCombo is not empty, the given BlockCombo
     * replaces the item in selectedBlockCombo.
     * @param combo the BlockCombo to be selected
     */
    void selectBlockCombo(BlockCombo combo) {
        // remove former selected BlockCombo form list
        if (!selectedBlockCombo.isEmpty()) {
            selectedBlockCombo.clear();
        }

        // add combo to list
        selectedBlockCombo.add(combo);
    }

    /**
     * Deselects the currently selected BlockCombo
     * by clearing selectedBlockCombo.
     */
    void deselectBlockCombo() {
        selectedBlockCombo.clear();
    }

    /**
     * Draws the open and saved block combos.
     * @param g the Graphics object given by paintComponent()
     */
    private void drawBlockCombos(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawRect(15 + i * 65, 30, 55, 55);
        }
        g.drawRect(15 + 3 * 65 + 20, 30, 55, 55);

        g.drawString("Rotations: 23", 15, 20);

        g.setColor(Color.BLACK);
        g.fillRect(21, 36, 8, 8);
        g.fillRect(21, 45, 8, 8);
        g.fillRect(21, 54, 8, 8);
        g.fillRect(21, 63, 8, 8);
        g.fillRect(21, 72, 8, 8);

        g.fillRect(30, 54, 8, 8);
        g.fillRect(39, 54, 8, 8);
        g.fillRect(48, 54, 8, 8);
        g.fillRect(57, 54, 8, 8);

        for (int i = 0; i < openBlockCombos.size(); i++) {
            BlockCombo combo = openBlockCombos.get(i);
            //int[] initialPosition =
            //drawSingleBlockCombo(g, );
        }

    }

    private void drawSingleBlockCombo(Graphics g, BlockCombo combo,
                                      int[] initialPosition) {

    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBlockCombos(g);
    }


}
