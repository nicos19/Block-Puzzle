package blockpuzzle;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A BlockComboSave is serialized when the game is saved
 * to save the state of a single BlockCombo.
 */
public class BlockComboSave implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private final List<int[]> comboFormation;


    BlockComboSave(SingleContainer<BlockCombo> comboContainer) {
        if (comboContainer.isEmpty()) {
            // no BlockCombo in this SingleContainer
            comboFormation = new ArrayList<>();
        }
        else {
            // save the formation of the non-rotated BlockCombo
            BlockCombo combo = comboContainer.getContent().createCopy();
            combo.resetRotation();
            comboFormation = combo.getComboFormation();
        }
    }

    /**
     * Restores the BlockCombo that is represented by this BlockComboSave.
     * @return the restored BlockCombo
     */
    BlockCombo restoreBlockCombo() {
        return new BlockCombo(comboFormation);
    }

    /**
     * Checks if this BlockComboSave is representing a BlockCombo or not.
     * @return true if comboFormation is empty, false otherwise
     */
    boolean representsBlockCombo() {
        return comboFormation.isEmpty();
    }


}
