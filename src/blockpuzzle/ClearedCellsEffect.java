package blockpuzzle;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ClearedCellsEffect class is used to invoke the visual
 * effect of recently cleared cells in the Grid of the game.
 */
public class ClearedCellsEffect implements ActionListener {
    GameManager gameManager;
    ScorePanel scorePanel;
    GridPanel gridPanel;

    ClearedCellsEffect(GameManager gameM, GridPanel gridP, ScorePanel scoreP) {
        gameManager = gameM;
        scorePanel = scoreP;
        gridPanel = gridP;
    }

    /**
     * Tells the gridPanel to activate an effect to show that
     * the player just cleared some full rows/columns.
     */
    void tryClearedCellsEffect() {
        if (gridPanel.getGrid().getRecentlyClearedCells().isEmpty()) {
            // no cells recently cleared -> no effect
            return;
        }

        if (gridPanel.getGrid().getRecentlyClearedTimer() > 0) {
            // show effect
            gameManager.repaint();

            // set timer to invoke next level of effect
            gridPanel.getGrid().decreaseRecentlyClearedTimer();
            Timer t = new Timer(150, this);
            t.setRepeats(false);
            t.start();
        }
        else if (gridPanel.getGrid().getRecentlyClearedTimer() == 0) {
            // recentlyClearedTimer is 0 -> effect shall end
            gridPanel.getGrid().clearRecentlyClearedCells();
            // remove recently added score note in ScorePanel
            scorePanel.resetRecentlyAddedScore();
            gameManager.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tryClearedCellsEffect();
    }
}
