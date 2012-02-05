package scrabbletool.game.board;

import scrabbletool.game.Letter;

/**
 * A Scrabble tile. Contains a {@link Letter} unless it is {@link Blank}.
 * 
 * @author Philip Puryear
 */
public class Tile implements Comparable<Tile> {
  private Letter letter_;

  /**
   * Creates a new tile with the given face letter.
   * 
   * @param letter The face letter for this tile.
   */
  public Tile(Letter letter) {
    letter_ = letter;
  }

  /**
   * Returns the face letter on this tile.
   */
  public Letter getLetter() {
    return letter_;
  }

  @Override
  public int compareTo(Tile other) {
    if (other instanceof Blank)
      return -1;
    return letter_.compareTo(other.letter_);
  }
}
