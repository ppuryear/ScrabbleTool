package scrabbletool;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import scrabbletool.game.Game;
import scrabbletool.game.GameFactory;
import scrabbletool.ui.UI;

/**
 * The main runnable class for ScrabbleTool.
 * 
 * @author Philip Puryear
 */
public class ScrabbleTool {
  private static final String DICTIONARY_FOLDER_NAME = "dict";
  private static final String GAMETYPES_FOLDER_NAME = "game";

  /**
   * The base directory of the ScrabbleTool program.
   */
  public static final File BASE_DIRECTORY;

  /**
   * The folder that the dictionaries are stored in.
   */
  public static final File DICTIONARY_FOLDER;

  /**
   * The folder that the game-type descriptor files are stored in.
   */
  public static final File GAMETYPE_FOLDER;

  /**
   * The file extension of the game descriptor file.
   */
  public static final String GAMETYPE_FILE_EXTENSION = ".xml";

  static {
    BASE_DIRECTORY = getBaseDirectory();
    DICTIONARY_FOLDER = FileUtilities.getDescendant(BASE_DIRECTORY,
                                                    DICTIONARY_FOLDER_NAME);
    GAMETYPE_FOLDER = FileUtilities.getDescendant(BASE_DIRECTORY,
                                                  GAMETYPES_FOLDER_NAME);
  }

  /**
   * The program entry point for ScrabbleTool.
   */
  public static void main(String[] args) {
    // Initialize the preferences object.
    try {
      Preferences.initialize();
    } catch (IOException e) {
      // Bail if we can't read our preferences file.
      e.printStackTrace();
      return;
    }

    // Find the game descriptor file.
    String gametypeFilename = Preferences.getPreference(Preferences.Keys.GAMETYPE)
                              + GAMETYPE_FILE_EXTENSION;
    File gameTypeFile = FileUtilities.getDescendant(GAMETYPE_FOLDER,
                                                    gametypeFilename);

    // Instantiate a new game.
    Game game = null;
    try {
      game = GameFactory.newGame(gameTypeFile);
    } catch (Exception e) {
      // Bail if there is a problem.
      // TODO: Handle errors more gracefully here.
      e.printStackTrace();
      return;
    }
    game.newGame();

    // Build the UI.
    UI.createUI(game);
  }

  /**
   * Returns the program's working directory.
   */
  private static File getBaseDirectory() {
    File baseDir = null;
    try {
      URL fileURL = ScrabbleTool.class.getProtectionDomain()
                                      .getCodeSource()
                                      .getLocation();
      URI fileURI = fileURL.toURI();
      baseDir = new File(fileURI);
    } catch (URISyntaxException e) {
      // We should never get here since we let Java supply the URI.
      e.printStackTrace();
    }
    return baseDir;
  }
}
