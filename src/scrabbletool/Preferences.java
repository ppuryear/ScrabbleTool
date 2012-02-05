package scrabbletool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * This class provides a static interface for accessing program preferences.
 * Preferences are represented as key-value data, where the keys may be any of
 * the fields in the {@link Preferences.Keys} class.
 * 
 * @author Philip Puryear
 */
public class Preferences {
  private static Properties defaults_ = new Properties();

  /**
   * The game-type preference, the value of which tells the program which XML
   * game descriptor file to parse.
   */
  public static final String GAMETYPE = "gametype";
  static {
    defaults_.setProperty(GAMETYPE, "scrabble");
  }

  /**
   * The UI language.
   */
  public static final String LANGUAGE = "language";
  static {
    defaults_.setProperty(LANGUAGE, "en");
  }

  private static final Path PREFERENCES_FILE;
  static {
    PREFERENCES_FILE = ScrabbleTool.BASE_DIRECTORY.resolve("prefs.xml");
  }

  private static Properties prefs_;

  /**
   * Loads the application preferences from the prefs file.
   * 
   * @throws IOException If there was a problem reading the preferences file.
   */
  static void initialize() throws IOException {
    prefs_ = new Properties(defaults_);

    if (!Files.exists(PREFERENCES_FILE)) {
      // Create a prefs file with the default values.
      save();
    } else {
      InputStream prefsIS = Files.newInputStream(PREFERENCES_FILE);
      prefs_.loadFromXML(prefsIS);
      prefsIS.close();
    }
  }

  /**
   * Retrieves the preference value for the given key.
   * 
   * @param key The preference key.
   * @see Preferences.Keys
   */
  public static String get(String key) {
    return prefs_.getProperty(key);
  }

  /**
   * Sets the given preference to the given value.
   * 
   * @param key The preference key.
   * @param value The value of the preference.
   */
  public static void set(String key, String value) {
    prefs_.setProperty(key, value);
  }

  /**
   * Commits current preferences to disk.
   * 
   * @throws IOException If there is an error writing to the preferences file.
   */
  public static void save() throws IOException {
    OutputStream prefsOS = Files.newOutputStream(PREFERENCES_FILE);
    prefs_.storeToXML(prefsOS, null);
    prefsOS.close();
  }
}
