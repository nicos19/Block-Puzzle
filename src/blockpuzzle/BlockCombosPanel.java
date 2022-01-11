package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A BlockCombosPanel is an extended JPanel that contains the logical
 * and visual representation of the BLockCombos in the game.
 */
public class BlockCombosPanel extends JPanel {
    private List<BlockCombo> openBlockCombos;

    // this list has always at most one item
    private List<BlockCombo> savedBlockCombo;
    // this list has always at most one item
    private List<BlockCombo> selectedBlockCombo;

    BlockCombosPanel() {
        // add MouseInteractionManager


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
            g.drawRect(10 + i * 55, 10, 50, 50);
        }


    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBlockCombos(g);
    }


}
