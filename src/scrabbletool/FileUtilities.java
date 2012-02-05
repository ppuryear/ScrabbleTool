package scrabbletool;

import java.io.File;

/**
 * Provides convenience methods for dealing with files.
 * 
 * @author Philip Puryear
 */
public class FileUtilities {
  /**
   * Returns the descendant {@link File} of the given file. The descendant is
   * found by traversing the file system along {@code relativePath}, starting at
   * {@code parent}.
   * 
   * @param parent The parent file.
   * @param relativePath The path from {@code parent} to the desired file.
   * @return The descendant file.
   */
  public static File getDescendant(File parent, String... relativePath) {
    File intermediate = parent;
    for (String child : relativePath) {
      intermediate = new File(intermediate, child);
    }
    return intermediate;
  }
}
