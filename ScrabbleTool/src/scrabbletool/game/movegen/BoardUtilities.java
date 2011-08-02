package scrabbletool.game.movegen;

import scrabbletool.game.board.Board;

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
   * Finds the boundary (i.e. the first empty column in some direction) of a
   * word on the board. The direction is determined by the {@code dir}
   * parameter: if the left boundary is desired, use {@link #LEFT}; if the right
   * boundary is desired, use {@link #RIGHT}.
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
    while (onBoard(board, col) && board.get(row, col).getTile() != null)
      col += dir;
    return col;
  }

  /**
   * Returns true if and only if the given column is inside the board.
   * 
   * @param board The board to use.
   * @param col The column number to test.
   */
  public static boolean onBoard(Board board, int col) {
    return (col >= 0 && col < board.size());
  }
}
