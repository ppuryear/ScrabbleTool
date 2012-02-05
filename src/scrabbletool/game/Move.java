package scrabbletool.game;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import scrabbletool.game.board.Tile;

/**
 * A Scrabble move, consisting of a set of tiles to be placed at specified board
 * positions.
 * 
 * @author Philip Puryear
 */
public class Move {
  private boolean across_;
  private int rowOrCol_;
  private Map<Integer, Tile> tileMap_;

  /**
   * Creates a new move with no tiles.
   * 
   * @param across True if the move is <em>across</em>, false if the move is
   *          <em>down</em>.
   * @param rowOrCol The move's row if it is an <em>across</em> move, or the
   *          move's column if it is a <em>down</em> move.
   */
  public Move(boolean across, int rowOrCol) {
    across_ = across;
    rowOrCol_ = rowOrCol;
    tileMap_ = new TreeMap<Integer, Tile>();
  }

  /**
   * Returns true if this move is <em>across</em>, or false if it is
   * <em>down</em>.
   */
  public boolean isAcross() {
    return across_;
  }

  /**
   * Returns this move's row if it is an <em>across</em> move, or this move's
   * column if it is a <em>down</em> move.
   */
  public int getRowOrCol() {
    return rowOrCol_;
  }

  /**
   * Returns a map which maps column numbers (if the move is across, otherwise
   * row numbers) to the {@link Tile}s to be played at those positions.
   */
  public Map<Integer, Tile> getTileMap() {
    return Collections.unmodifiableMap(tileMap_);
  }

  /**
   * Adds a tile to this move at the specified column (if the move is across,
   * otherwise row).
   * 
   * @param tile The tile to add.
   * @param pos The column or row on which to place the tile.
   */
  public void addTile(Tile tile, int pos) {
    tileMap_.put(pos, tile);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (!(other instanceof Move))
      return false;

    Move otherMove = (Move) other;

    // Special case: if both tile maps are empty, then both moves are equal (an
    // empty tile map represents a "no-op" move).
    if (this.tileMap_.isEmpty() && otherMove.tileMap_.isEmpty())
      return true;

    // Special case: if both moves involve only one tile, then only the location
    // of that tile matters (the across/down distinction is irrelevant).
    if (this.tileMap_.size() == 1 && otherMove.tileMap_.size() == 1)
      return singleTileMoveEquals(otherMove);

    // General case: compare all members.
    if (this.across_ != otherMove.across_)
      return false;
    if (this.rowOrCol_ != otherMove.rowOrCol_)
      return false;
    if (!this.tileMap_.equals(otherMove.tileMap_))
      return false;
    return true;
  }

  /**
   * Returns true if and only if the given single-tile move represents the same
   * move as this single-tile move.
   */
  private boolean singleTileMoveEquals(Move other) {
    Map.Entry<Integer, Tile> mapEntry = tileMap_.entrySet().iterator().next();
    int thisRow = across_ ? rowOrCol_ : mapEntry.getKey();
    int thisCol = across_ ? mapEntry.getKey() : rowOrCol_;
    Tile thisTile = mapEntry.getValue();

    mapEntry = other.tileMap_.entrySet().iterator().next();
    int otherRow = other.across_ ? other.rowOrCol_ : mapEntry.getKey();
    int otherCol = other.across_ ? mapEntry.getKey() : other.rowOrCol_;
    Tile otherTile = mapEntry.getValue();

    if (!thisTile.equals(otherTile))
      return false;
    if (thisRow != otherRow)
      return false;
    if (thisCol != otherCol)
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = rowOrCol_ ^ tileMap_.hashCode();
    if (!across_)
      return -hashCode;
    return hashCode;
  }
}
