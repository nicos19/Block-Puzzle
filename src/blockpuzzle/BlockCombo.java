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


}
