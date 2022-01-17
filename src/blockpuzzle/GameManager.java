package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A GameManager is an extended JFrame responsible for managing and visualizing the game.
 */
public class GameManager extends JFrame {
    private final JPanel topPanel = new JPanel();
    private final GridPanel gridPanel = new GridPanel(this);
    private final BlockCombosPanel blockCombosPanel = new BlockCombosPanel();
    private final MouseInteractionManager mouseInteractionManager
            = new MouseInteractionManager(this, gridPanel, blockCombosPanel);

    GameManager() {
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // add panels to frame
        add(topPanel);
        add(gridPanel);
        add(blockCombosPanel);

        // add MouseInteractionListener to panels
        gridPanel.addMouseListener(mouseInteractionManager);
        gridPanel.addMouseMotionListener(mouseInteractionManager);
        blockCombosPanel.addMouseListener(mouseInteractionManager);
        blockCombosPanel.addMouseMotionListener(mouseInteractionManager);


        topPanel.setBackground(Color.BLUE);
        gridPanel.setBackground(Color.YELLOW);
        blockCombosPanel.setBackground(Color.RED);


        // set panels' sizes
        topPanel.setPreferredSize(new Dimension(300, 50));
        topPanel.setMaximumSize(new Dimension(300, 50));
        gridPanel.setPreferredSize(new Dimension(300, 300));
        gridPanel.setMaximumSize(new Dimension(300, 300));
        blockCombosPanel.setPreferredSize(new Dimension(300, 100));
        blockCombosPanel.setMaximumSize(new Dimension(300, 100));

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    /**
     * Starts next round of the game if current round is over, i.e. if
     * player used all of its available BlockCombos.
     * Next round results in three new BlockCombos becoming available.
     */
    void tryNextRound() {
        if (blockCombosPanel.openBlockCombosIsEmpty()) {
            blockCombosPanel.generateNewBlockCombos();
        }
    }


    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.setPreferredSize(new Dimension(300, 450));
        gameManager.setVisible(true);
    }

}
