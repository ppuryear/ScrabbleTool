package scrabbletool.game.board;

import scrabbletool.Transposable;

/**
 * A {@code Square} is a location on the {@link Board}. It may have a
 * {@link Tile} and/or a {@link Modifier}.
 */
public class Square implements Transposable<Square> {
  private Tile _tile;
  private Modifier _modifier;

  /**
   * Creates a new square with no tile and no modifier.
   */
  Square() {
    _tile = null;
    _modifier = null;
  }

  /**
   * Returns the tile on this square, or {@code null} if this square has no tile
   * on it.
   */
  public Tile getTile() {
    return _tile;
  }

  /**
   * Returns the modifier for this square, or {@code null} if this square has no
   * modifier.
   */
  public Modifier getModifier() {
    return _modifier;
  }

  /**
   * Places a tile on this square.
   * 
   * @param tile The tile to place.
   */
  public void placeTile(Tile tile) {
    _tile = tile;
  }

  /**
   * Adds a modifier to this square.
   * 
   * @param modifier The modifier to add.
   */
  public void setModifier(Modifier modifier) {
    _modifier = modifier;
  }

  @Override
  public Square transpose() {
    // The state of a single square doesn't depend on the orientation of the
    // board.
    return this;
  }
}
