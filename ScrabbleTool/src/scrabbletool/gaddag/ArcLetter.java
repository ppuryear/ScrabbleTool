package scrabbletool.gaddag;

import scrabbletool.game.Letter;

/**
 * An {@code ArcLetter} is a symbol that is mapped to unique outgoing arcs in
 * the GADDAG. An arc-letter may be either any Scrabble {@link Letter} or the
 * {@link Delimiter} symbol.
 * 
 * @author Philip Puryear
 */
class ArcLetter implements Comparable<ArcLetter> {
  private Letter _letter;

  /**
   * Constructs a new arc letter for the given Scrabble letter.
   * 
   * @param letter
   */
  public ArcLetter(Letter letter) {
    _letter = letter;
  }

  /**
   * Returns the underlying Scrabble letter for this arc letter.
   */
  public Letter getLetter() {
    return _letter;
  }

  /**
   * Compares this arc letter to another. The comparison is such that the
   * {@link Delimiter} symbol is greater than any other letter (and equal to
   * itself).
   */
  @Override
  public int compareTo(ArcLetter other) {
    if (other instanceof Delimiter)
      return -1;
    return _letter.compareTo(other._letter);
  }
}
