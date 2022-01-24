package blockpuzzle;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private int highScore = 0;
    private int score = 0;


    /**
     * Resets the score (not the highScore).
     */
    void reset() {
        score = 0;
    }

    /**
     * Increases the score by given number.
     * If score is afterwards larger than highScore, highScore is set to score.
     * @param number the number added to the score
     */
    void increaseScoreBy(int number) {
        score += number;
        if (score > highScore) {
            highScore = score;
        }
    }

    /**
     * Draws the current score and the highScore.
     * @param g the Graphics object given by paintComponent()
     */
    private void drawScores(Graphics g) {
        Color standardColor = Color.GRAY;
        g.setColor(standardColor);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // draw highScore
        g.drawString("Best: " + highScore, 15, 24);

        // draw current score
        g.setFont(new Font("Monospaced", Font.PLAIN, 20));
        g.drawString("Score: " + score, 14, 48);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawScores(g);
    }

}
