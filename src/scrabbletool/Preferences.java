package scrabbletool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class provides a static interface for accessing program preferences.
 * Preferences are represented as key-value data, where the keys may be any of
 * the fields in the {@link Preferences.Keys} class.
 * 
 * @author Philip Puryear
 */
public class Preferences {
  /**
   * A container class for preference keys.
   * 
   * @see Preferences#getPreference(String)
   */
  public static class Keys {
    /**
     * The game-type preference, the value of which tells the program which XML
     * game descriptor file to parse.
     */
    public static final String GAMETYPE = "gametype";
    /**
     * The UI language.
     */
    public static final String LANGUAGE = "language";
  }

  private static final String PREFERENCES_FILENAME = "preferences";
  private static Properties preferences_;

  /**
   * Loads the preferences from the file {@code <program_dir>/preferences}.
   * 
   * @throws IOException If there was a problem reading the preferences file.
   */
  static void initialize() throws IOException {
    preferences_ = new Properties();
    File prefsFile = FileUtilities.getDescendant(ScrabbleTool.BASE_DIRECTORY,
                                                 PREFERENCES_FILENAME);
    FileInputStream prefsFIS = new FileInputStream(prefsFile);
    preferences_.load(prefsFIS);
  }

  /**
   * Retrieves the preference value for the given key.
   * 
   * @param key The preference key.
   * @see Preferences.Keys
   */
  public static String getPreference(String key) {
    return preferences_.getProperty(key);
  }
}
