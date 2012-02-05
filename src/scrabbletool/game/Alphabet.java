package scrabbletool.game;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Scrabble alphabet consists of all {@link Letter}s allowed in valid words.
 * 
 * @author Philip Puryear
 */
public class Alphabet {
  private TreeSet<Letter> letters_;

  /**
   * Creates a new, empty alphabet.
   */
  Alphabet() {
    letters_ = new TreeSet<Letter>();
  }

  /**
   * Returns the set of all {@link Letter}s in this alphabet.
   */
  public Set<Letter> getLetters() {
    return Collections.unmodifiableSet(letters_);
  }

  /**
   * Returns the {@link Letter} in this alphabet that has the given textual
   * representation, or {@code null} if no such letter exists.
   * 
   * @param text The textual representation of the letter.
   */
  public Letter getLetter(String text) {
    Letter dummyLetter = new Letter(text, 0);
    Letter mapLetter = letters_.ceiling(dummyLetter);
    if (!mapLetter.getText().equals(text))
      return null;
    return mapLetter;
  }

  /**
   * Adds the given letter to this alphabet.
   * 
   * @param letter The letter to add.
   */
  void addLetter(Letter letter) {
    letters_.add(letter);
  }
}
