package blockpuzzle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A MouseInteractionManager is responsible for detecting mouse clicks and movements.
 */
public class MouseInteractionManager implements MouseListener, MouseMotionListener {
    GameManager gameManager;
    GridPanel gridPanel;
    BlockCombosPanel blockCombosPanel;

    MouseInteractionManager(GameManager gameM,
                            GridPanel gridP, BlockCombosPanel blockCombosP) {
        gameManager = gameM;
        gridPanel = gridP;
        blockCombosPanel = blockCombosP;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == gridPanel) {
            if (gridPanel.isMouseOverGrid(e.getPoint())) {
                GridCell clickedCell = gridPanel.getCellUnderMouse(e.getPoint());
                clickedCell.fill();
                gameManager.repaint();
            }
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

    }
}
