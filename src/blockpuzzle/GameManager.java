package blockpuzzle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A GameManager is an extended JFrame responsible for managing and visualizing the game.
 */
public class GameManager extends JFrame {
    private JPanel topPanel = new JPanel();
    private GridPanel gridPanel = new GridPanel();
    private BlockCombosPanel blockCombosPanel = new BlockCombosPanel();

    GameManager() {
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // add panels to frame
        add(topPanel);
        add(gridPanel);
        add(blockCombosPanel);

        topPanel.setBackground(Color.BLUE);
        gridPanel.setBackground(Color.YELLOW);
        blockCombosPanel.setBackground(Color.RED);

        topPanel.setPreferredSize(new Dimension(300, 50));
        topPanel.setMaximumSize(new Dimension(300, 50));
        gridPanel.setPreferredSize(new Dimension(300, 300));
        gridPanel.setMaximumSize(new Dimension(300, 300));
        blockCombosPanel.setPreferredSize(new Dimension(300, 100));
        blockCombosPanel.setMaximumSize(new Dimension(300, 100));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }


    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.setSize(300, 450);
        gameManager.setVisible(true);
    }

}
