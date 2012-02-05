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
    if (this.letter_ == null) {
      if (other.letter_ == null)
        return 0;
      else
        return 1;
    } else {
      if (other.letter_ == null)
        return -1;
      else
        return letter_.compareTo(other.letter_);
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (!(other instanceof Tile))
      return false;

    Tile otherTile = (Tile) other;
    if (this.letter_ == null) {
      if (otherTile.letter_ == null)
        return true;
    } else {
      if (otherTile.letter_ != null)
        return letter_.equals(otherTile.letter_);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (letter_ == null)
      return 0;
    return letter_.hashCode();
  }
}
