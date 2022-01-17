package blockpuzzle;

import java.util.List;

/**
 * A BlockCombo describes a set of blocks with a particular formation and
 * represents a game object that can be inserted into a grid.
 */
public class BlockCombo {

    /** each comboFormation.get(i) = [i_x, i_y] represents one block
     * [i_x, i_y] is the block's distance to the start block
     * each comboFormation has a start block, this start block is
     * represented by the entry [i_x, i_y] = [0, 0]
     */
    private List<int[]> comboFormation;
    private int rotation = 0;

    BlockCombo(List<int[]> formation) {
        comboFormation = formation;
    }

    /**
     * Gets the formation of the BlockCombo.
     * @return the comboFormation
     */
    List<int[]> getComboFormation() {
        return comboFormation;
    }

    /**
     * Rotates the BlockCombo 90° to the right.
     */
    void rotate() {
        // calculate for all blocks in formation the new distance to start block
        for (int i = 0; i < comboFormation.size(); i++) {
            int[] newDistance = {-comboFormation.get(i)[1],
                    comboFormation.get(i)[0]};
            comboFormation.set(i, newDistance);
        }

        // set new rotation
        rotation += 90;
        if (rotation == 360) {
            // BlockCombo is back in original formation
            rotation = 0;
        }
    }

    /**
     * Rotates the BlockCombo back in its original formation.
     */
    void resetRotation() {
        // rotate as long as original formation is not restored yet
        while (rotation != 0) {
            rotate();
        }
    }

    /**
     * Gets the offset (in blocks) for drawing this BlockCombo into BlockCombosPanel.
     * offset [off_x, off_y] means that the BlockCombo must be shifted off_x blocks
     * in x-direction and off_y blocks in y-direction.
     * @return the calculated offset
     */
    double[] getDrawOffset() {
        int blocksLeftOfStartBlock = 0;
        int blocksRightOfStartBlock = 0;
        int blocksAboveStartBlock = 0;
        int blocksUnderStartBlock = 0;

        for (int[] block : comboFormation) {
            if (block[0] > 0 && block[0] > blocksRightOfStartBlock) {
                blocksRightOfStartBlock += 1;
            }
            else if (block[0] < 0 && block[0] < blocksLeftOfStartBlock) {
                blocksLeftOfStartBlock -= 1;
            }
            if (block[1] > 0 && block[1] > blocksUnderStartBlock) {
                blocksUnderStartBlock += 1;
            }
            else if (block[1] < 0 && block[1] < blocksAboveStartBlock) {
                blocksAboveStartBlock -= 1;
            }
        }

        return new double[] {
                (double)(blocksLeftOfStartBlock + blocksRightOfStartBlock) / -2,
                (double)(blocksAboveStartBlock + blocksUnderStartBlock) / -2
        };
    }


}
