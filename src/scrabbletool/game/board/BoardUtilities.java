package scrabbletool.game.board;

/**
 * Useful board-related functions for use in move generation.
 * 
 * @author Philip Puryear
 */
public class BoardUtilities {
  /**
   * The leftward direction.
   * 
   * @see #findWordBoundary(Board, int, int, int)
   */
  public static final int LEFT = -1;

  /**
   * The rightward direction.
   * 
   * @see #findWordBoundary(Board, int, int, int)
   */
  public static final int RIGHT = 1;

  /**
   * Finds the boundary of a word on the board. The direction is determined by
   * the {@code dir} parameter: if the left boundary is desired, use
   * {@link #LEFT}; if the right boundary is desired, use {@link #RIGHT}.
   * 
   * @param board The board to use.
   * @param row The row that the word is located on.
   * @param internalPos Any column located inside the word bounds.
   * @param dir (See above.)
   * @return The column number of the boundary.
   */
  public static int findWordBoundary(Board board,
                                     int row,
                                     int internalPos,
                                     int dir) {
    int col = internalPos;
    while (board.isValidPosition(row, col)
           && board.get(row, col).getTile() != null)
      col += dir;
    return col - dir;
  }
}
