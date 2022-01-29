package blockpuzzle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Color;

/**
 * A MouseInteractionManager is responsible for detecting mouse clicks and movements.
 */
public class MouseInteractionManager implements MouseListener, MouseMotionListener {
    GameManager gameManager;
    ScorePanel scorePanel;
    GridPanel gridPanel;
    BlockCombosPanel blockCombosPanel;

    MouseInteractionManager(GameManager gameM, ScorePanel scoreP,
                            GridPanel gridP, BlockCombosPanel blockCombosP) {
        gameManager = gameM;
        scorePanel = scoreP;
        gridPanel = gridP;
        blockCombosPanel = blockCombosP;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // check if game is over
        if (gameManager.isGameOver()) {
            // click restarts game
            gameManager.restart();
            return;
        }

        // check if player clicked RMT to rotate selected BlockCombo
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (blockCombosPanel.isAnyBlockComboSelected()
                    && gameManager.getRotations() > 0) {
                // rotate selected BlockCombo (if it is rotatable)
                boolean rotate = blockCombosPanel.getSelectedBlockCombo().tryRotate();
                if (rotate) {
                    mouseMoved(e);  // this updates highlighted grid cells if necessary
                }
                gameManager.repaint();
            }
            return;
        }

        // check if player inserts selected BlockCombo
        if (e.getSource() == gridPanel && gridPanel.isMouseOverGrid(e.getPoint())) {
            GridCell clickedCell = gridPanel.getCellUnderMouse(e.getPoint());
            if (blockCombosPanel.isAnyBlockComboSelected()
                    && gridPanel.getGrid().canInsertBlockCombo(
                    clickedCell, blockCombosPanel.getSelectedBlockCombo())) {
                BlockCombo selectedCombo = blockCombosPanel.getSelectedBlockCombo();
                // insert selected BlockCombo in Grid
                gridPanel.getGrid().insertBlockCombo(clickedCell, selectedCombo);
                // remove selected BlockCombo from openBlockCombos/savedBlockCombo
                blockCombosPanel.consumeSelectedBlockCombo();
                // deselect the selected BlockCombo
                blockCombosPanel.deselectBlockCombo();
                // remove highlighting
                gridPanel.clearHighlightedCells();
                // consume one rotation if BlockCombo was rotated
                if (selectedCombo.isRotated()) {
                    gameManager.consumeRotation();
                }
                // check for game over
                gameManager.tryGameOver();
            }
            gameManager.repaint();

            ClearedCellsEffect c = new ClearedCellsEffect(gameManager, gridPanel,
                    scorePanel);
            c.tryClearedCellsEffect();
        }
        // check if player selects or saves any BlockCombo
        else if (e.getSource() == blockCombosPanel
                && e.getButton() == MouseEvent.BUTTON1) {
            blockCombosPanel.trySelect(e);
            blockCombosPanel.trySave(e);
            gameManager.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // check if game is over
        if (gameManager.isGameOver()) {
            // no mouse over effect if game is over
            return;
        }

        // check if player is hovering over the grid
        if (e.getSource() == gridPanel && gridPanel.isMouseOverGrid(e.getPoint())
                && blockCombosPanel.isAnyBlockComboSelected()) {
            GridCell targetCell = gridPanel.getCellUnderMouse(e.getPoint());
            BlockCombo selectedCombo = blockCombosPanel.getSelectedBlockCombo();

            // grid cells that would be covered by selected
            // BlockCombo shall be highlighted
            if (gridPanel.getGrid().canInsertBlockCombo(targetCell, selectedCombo)) {
                // insert possible: highlight green
                gridPanel.calculateHighlightedCells(selectedCombo, targetCell,
                                                    new Color(153, 255, 153));
            }
            else {
                //insert not possible: highlight red
                gridPanel.calculateHighlightedCells(selectedCombo, targetCell,
                                                    new Color(255, 153, 153));
            }

            gameManager.repaint();
        }
        else if (e.getSource() == gridPanel) {
            // mouse is not over grid or no BlockCombo is selected
            // -> do not highlight any grid cells
            gridPanel.clearHighlightedCells();
            gameManager.repaint();
        }
        // check if player is hovering over any open or saved BlockCombo
        else if (e.getSource() == blockCombosPanel) {
            blockCombosPanel.highlightBlockComboAreas(e);
            gameManager.repaint();
        }

    }

}
