package blockpuzzle;

import java.io.*;

/**
 * A SaveManager manages the saving and loading of the current/saved game state.
 */
public class SaveManager {
    GameManager gameManager;
    ScorePanel scorePanel;
    GridPanel gridPanel;
    BlockCombosPanel blockCombosPanel;

    SaveManager(GameManager gm, ScorePanel sp, GridPanel gp, BlockCombosPanel bcp) {
        gameManager = gm;
        scorePanel = sp;
        gridPanel = gp;
        blockCombosPanel = bcp;
    }

    /**
     * Creates a new Savegame based on current state of the game.
     * @return the created Savegame
     */
    Savegame createSavegame() {
        return new Savegame(gameManager, scorePanel, gridPanel, blockCombosPanel);
    }

    /**
     * Saves the current game state by creating a Savegame Obbject and serializing it.
     */
    void saveGameState() {
        // create directory for Savegames (if it does not already exist)
        File targetDirectory = new File("Savegames");
        targetDirectory.mkdir();  // does nothing if directory already exists

        // create file for Savegame
        File file = new File("Savegames" + File.separator
                + "Savegame.save");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("An error occurred while creating the Savegame file.");
            e.printStackTrace();
        }

        // create Savegame instance
        Savegame savegame = createSavegame();

        // serialize Savegame
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    fileOutputStream);
            objectOutputStream.writeObject(savegame);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (Exception e) {
            System.err.println("An error occurred while serializing the Savegame.");
            e.printStackTrace();
        }
    }

    /**
     * Restores the game state represented by the given savegame.
     * @param savegame the Savegame representing the game state to be restored
     */
    void restoreGameState(Savegame savegame) {
        gameManager.restoreGameManager(savegame);
        scorePanel.restorePanel(savegame);
        gridPanel.restorePanel(savegame);
        blockCombosPanel.restorePanel(savegame);
    }

    /**
     * Loads a previously saved Savegame by deserializing the content of the Savegame's file.
     * @return the loaded Savegame if deserialization successful, null otherwise
     */
    Savegame loadSavegame() {
        Savegame loadedSavegame = null;

        File savegameFile = new File(
                "Savegames" + File.separator + "Savegame.save");

        if (!savegameFile.exists()) {
            // no savegame file found -> abort loading
            return loadedSavegame;
        }

        try {
            FileInputStream fileInputStream
                    = new FileInputStream(savegameFile.getPath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            loadedSavegame = (Savegame) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e) {
            System.err.println("An error occurred while deserializing Savegame file.");
            e.printStackTrace();
        }

        return loadedSavegame;
    }


}
