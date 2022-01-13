package blockpuzzle;

import java.util.ArrayList;
import java.util.List;

/**
 * The BlockComboCreator class provides static
 * methods to create all legal BlockCombos.
 *
 * The size AxB_C of a BlockCombo states that the BlockCombo
 * has A blocks in x-direction and B blocks in y-direction
 * with a total of C blocks.
 */
public class BlockComboCreator {

    /**
     * Creates a BlockCombo consisting of a single block.
     * @return the created BlockCombo
     */
    static BlockCombo create_1x1_1_Combo() {
        int[] block0 = {0, 0};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a BlockCombo with size 1x2_2.
     * @return the created BlockCombo
     */
    static BlockCombo create_1x2_2_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {0, 1};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);

        return new BlockCombo(comboFormation);
    }

    static BlockCombo create_1x3_3_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {0, 1};
        int[] block2 = {0, 2};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);

        return new BlockCombo(comboFormation);
    }

    static BlockCombo create_1x4_4_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {0, 1};
        int[] block2 = {0, 2};
        int[] block3 = {0, 3};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);

        return new BlockCombo(comboFormation);
    }

    static BlockCombo create_1x5_5_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {0, 1};
        int[] block2 = {0, 2};
        int[] block3 = {0, 3};
        int[] block4 = {0, 4};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);
        comboFormation.add(block4);

        return new BlockCombo(comboFormation);
    }

    static BlockCombo create_2x2_3_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 0};
        int[] block2 = {0, 1};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);

        return new BlockCombo(comboFormation);
    }

    static BlockCombo create_2x2_4_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 0};
        int[] block2 = {0, 1};
        int[] block3 = {1, 1};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);

        return new BlockCombo(comboFormation);
    }

}
