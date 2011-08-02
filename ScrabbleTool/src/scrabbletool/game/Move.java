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
}
