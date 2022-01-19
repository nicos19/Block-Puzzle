package blockpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Creates a new BlockCombo which is a copy of this BlockCombo.
     * @return the new BlockCombo
     */
    BlockCombo createCopy() {
        List<int[]> formationCopy = new ArrayList<>();
        for (int[] block : comboFormation) {
            int[] blockCopy = {block[0], block[1]};
            formationCopy.add(blockCopy);
        }
        return new BlockCombo(formationCopy);
    }

    /**
     * Prints a string representation of the comboFormation to the standard output.
     */
    void printFormation() {
        for (int[] block : comboFormation) {
            System.out.print(block[0] + "|" + block[1] + "  ");
        }
        System.out.println();
    }

    /**
     * Checks if this BlockCombo is equivalent to the other given BlockCombos.
     * Two BlockCombos are equivalent if and only if the comboFormations of both
     * BlockCombos contain the same elements (not considering the elements' order).
     * @param other the BlockCombo to be compared
     * @return true if the BlockCombos are equivalent, false otherwise
     */
    boolean equivalent(BlockCombo other) {
        if (comboFormation.size() != other.getComboFormation().size()) {
            return false;
        }

        // check if each block in this.comboFormation has its
        // equivalent otherBlock in other.comboFormation
        for (int[] block : comboFormation) {
            boolean blockFound = false;
            for (int[] otherBlock : other.getComboFormation()) {
                if (Arrays.equals(block, otherBlock)) {
                    blockFound = true;
                    break;
                }
            }
            if (!blockFound) {
                // this has a block that other has not
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if this BlockCombo can be rotated. Non-rotatable
     * BlockCombos would not change by executing a rotation.
     * @return true if BlockCombo can be rotated
     */
    boolean isRotatable() {
        // simulate rotation for this BlockCombo
        BlockCombo rotatedCombo = createCopy();
        rotatedCombo.rotate();

        // find block b of rotatedCombo so that if b is start block of rotatedCombo:
        // rotatedComboWithBStartBlock.equivalent(this) == true

        for (int i = 0; i < comboFormation.size(); i++) {
            int[] b = rotatedCombo.getComboFormation().get(i);
            // get adjusted formation with b as start block
            List<int[]> formationBStartBlock = new ArrayList<>();
            for (int j = 0; j < comboFormation.size(); j++) {
                int[] adjustedBlock = {rotatedCombo.getComboFormation().get(j)[0] -b[0],
                                       rotatedCombo.getComboFormation().get(j)[1] -b[1]};
                formationBStartBlock.add(adjustedBlock);
            }

            BlockCombo rotatedComboWithBStartBlock =
                    new BlockCombo(formationBStartBlock);

            if (rotatedComboWithBStartBlock.equivalent(this)) {
                // rotatedCombo is equivalent to non-rotated combo
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if this BlockCombo is rotated or in its original orientation.
     * @return true if rotation != 0, false otherwise
     */
    boolean isRotated() {
        return rotation != 0;
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
