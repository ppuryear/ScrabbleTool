package scrabbletool.gaddag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import scrabbletool.game.Alphabet;
import scrabbletool.game.Letter;

/**
 * This class is responsible for the creation of new {@link GADDAG}s via the
 * {@link #newGADDAG} method.
 * 
 * @author Philip Puryear
 */
public class GADDAGFactory {
  /**
   * Thrown when a word in the dictionary has a length < 2.
   */
  public static class WordSizeException extends Exception {
    private static final long serialVersionUID = 1L;

    public WordSizeException(List<Letter> word) {
      super("Cannot add " + word + " to dictionary. Word length must be >= 2.");
    }
  }

  /**
   * Instantiates a new GADDAG.
   * 
   * @param dictFile A text file containing a list of words to add, separated by
   *          newlines.
   * @param alphabet The alphabet used by the dictionary.
   * @return The newly-created GADDAG.
   * @throws IOException If there is a problem reading the dictionary file.
   * @throws WordSizeException If the dictionary file contains a word whose
   *           length is less than 2.
   */
  public static GADDAG newGADDAG(File dictFile, Alphabet alphabet) throws IOException,
                                                                  WordSizeException {
    List<String> words = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new FileReader(dictFile));
    while (reader.ready())
      words.add(reader.readLine());
    reader.close();

    GADDAG gaddag = new GADDAG();
    for (int i = 0; i < words.size(); i++) {
      List<Letter> letterList = letterListFromString(words.get(i), alphabet);
      if (letterList.size() < 2)
        throw new WordSizeException(letterList);
      gaddag.addWord(letterList);
    }
    return gaddag;
  }

  /**
   * Creates a list of {@link Letter}s from a dictionary string using the given
   * alphabet.
   * 
   * @param word The dictionary string.
   * @param alphabet The alphabet to use.
   */
  private static List<Letter> letterListFromString(String word,
                                                   Alphabet alphabet) {
    // TODO: This currently just extracts ASCII characters.
    List<Letter> letterList = new ArrayList<Letter>(word.length());
    for (char c : word.toCharArray()) {
      Letter letter = alphabet.getLetter(String.valueOf(c));
      if (letter != null)
        letterList.add(letter);
    }
    return letterList;
  }
}
