package scrabbletool.game.board;

import scrabbletool.game.Letter;

/**
 * A blank {@link Tile}. It may have no face letter (if it is unplayed, for
 * instance), or it may have a face letter assigned to it if it is played on the
 * board.
 * 
 * @author Philip Puryear
 */
public class Blank extends Tile {
  /**
   * Constructs a blank tile.
   */
  public Blank() {
    super(null);
  }

  /**
   * Constructs a blank tile with the given face letter.
   * 
   * @param letter The letter to use on this tile.
   */
  public Blank(Letter letter) {
    super(letter);
  }

  @Override
  public int compareTo(Tile other) {
    if (other instanceof Blank)
      return 0;
    return 1;
  }
}
