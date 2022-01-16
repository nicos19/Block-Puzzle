package blockpuzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     * Creates randomly a BlockCombo using the create_..._Combo() methods of this class.
     * @return the created BlockCombo
     */
    static BlockCombo createRandomCombo() {
        Random r = new Random();

        return switch (r.nextInt(14)) {
            case 0 -> create_1x1_1_Combo();
            case 1 -> create_1x2_2_Combo();
            case 2 -> create_1x3_3_Combo();
            case 3 -> create_1x4_4_Combo();
            case 4 -> create_1x5_5_Combo();
            case 5 -> create_2x2_3_Combo();
            case 6 -> create_2x2_4_Combo();
            case 7 -> create_2x3_4_Combo();
            case 8 -> create_3x3_5_Combo();
            case 9 -> create_diagonal_2_Combo();
            case 10 -> create_diagonal_3_Combo();
            case 11 -> create_T_4_Combo();
            case 12 -> create_T_5_Combo();
            default ->
                    // create some rare BlockCombo
                    switch (r.nextInt(4)) {
                        case 0 -> create_diagonal_4_Combo();
                        case 1 -> create_Plus_5_Combo();
                        case 2 -> create_X_5_Combo();
                        default -> create_Circle_4_Combo();
                    };
        };
    }

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

    static BlockCombo create_2x3_4_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 0};
        int[] block2 = {0, 1};
        int[] block3 = {0, 2};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);

        return new BlockCombo(comboFormation);
    }

    static BlockCombo create_3x3_5_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 0};
        int[] block2 = {2, 0};
        int[] block3 = {0, 1};
        int[] block4 = {0, 2};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);
        comboFormation.add(block4);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a BlockCombo with 2 blocks which are arranged diagonally.
     * @return the created BlockCombo
     */
    static BlockCombo create_diagonal_2_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 1};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a BlockCombo with 3 blocks which are arranged diagonally.
     * @return the created BlockCombo
     */
    static BlockCombo create_diagonal_3_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 1};
        int[] block2 = {2, 2};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a BlockCombo with 4 blocks which are arranged diagonally.
     * @return the created BlockCombo
     */
    static BlockCombo create_diagonal_4_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 1};
        int[] block2 = {2, 2};
        int[] block3 = {3, 3};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a T-shaped BlockCombo with 4 blocks.
     * @return the created BlockCombo
     */
    static BlockCombo create_T_4_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 0};
        int[] block2 = {2, 0};
        int[] block3 = {1, 1};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a T-shaped BlockCombo with 5 blocks.
     * @return the created BlockCombo
     */
    static BlockCombo create_T_5_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {1, 0};
        int[] block2 = {2, 0};
        int[] block3 = {1, 1};
        int[] block4 = {1, 2};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);
        comboFormation.add(block4);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a Plus-Symbol-shaped BlockCombo with 5 blocks.
     * @return the created BlockCombo
     */
    static BlockCombo create_Plus_5_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {0, -1};
        int[] block2 = {0, 1};
        int[] block3 = {-1, 0};
        int[] block4 = {1, 0};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);
        comboFormation.add(block4);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates an X-shaped BlockCombo with 5 blocks.
     * @return the created BlockCombo
     */
    static BlockCombo create_X_5_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {-1, -1};
        int[] block2 = {1, -1};
        int[] block3 = {-1, 1};
        int[] block4 = {1, 1};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);
        comboFormation.add(block4);

        return new BlockCombo(comboFormation);
    }

    /**
     * Creates a Circle-shaped BlockCombo with 4 blocks.
     * @return the created BlockCombo
     */
    static BlockCombo create_Circle_4_Combo() {
        int[] block0 = {0, 0};
        int[] block1 = {-1, 1};
        int[] block2 = {1, 1};
        int[] block3 = {0, 2};

        List<int[]> comboFormation = new ArrayList<>();
        comboFormation.add(block0);
        comboFormation.add(block1);
        comboFormation.add(block2);
        comboFormation.add(block3);

        return new BlockCombo(comboFormation);
    }

}
