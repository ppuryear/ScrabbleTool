package scrabbletool.game.movegen;

import java.util.Map;
import java.util.Set;
import scrabbletool.Transposable2DArray;
import scrabbletool.gaddag.Arc;
import scrabbletool.gaddag.GADDAG;
import scrabbletool.game.Alphabet;
import scrabbletool.game.Letter;
import scrabbletool.game.Move;
import scrabbletool.game.board.Board;
import scrabbletool.game.board.BoardUtilities;
import scrabbletool.game.board.Tile;

/**
 * This class manages the book-keeping data needed by the move generator.
 * 
 * @author Philip Puryear
 */
class DataManager {
  private Transposable2DArray<SquareData> squareData_;
  private Board board_;
  private GADDAG gaddag_;
  private AnchorUpdater anchorUpdater_;
  private CrossSetUpdater crossSetUpdater_;

  /**
   * Creates a new data manager.
   * 
   * @param game The game to be played.
   */
  public DataManager(Board board, GADDAG gaddag, Alphabet alphabet) {
    board_ = board;
    gaddag_ = gaddag;

    // Instantiate an array of |SquareData|.
    int boardSize = board_.size();
    squareData_ = new Transposable2DArray<SquareData>(boardSize, boardSize);
    for (int i = 0; i < boardSize - 1; i++) {
      for (int j = 0; j < boardSize - 1; j++)
        squareData_.set(i, j, new SquareData(alphabet.getLetters()));
    }
    anchorUpdater_ = new AnchorUpdater();
    crossSetUpdater_ = new CrossSetUpdater();
  }

  /**
   * Updates the move generation data, given that the specified move has been
   * played on the board.
   * 
   * @param move The move that was played.
   */
  public void update(Move move) {
    anchorUpdater_.update(move);
    crossSetUpdater_.update(move);
  }

  /**
   * Transposes all of the move generation data.
   */
  public void transpose() {
    squareData_ = squareData_.transpose();
    board_ = board_.transpose();
  }

  /**
   * Returns the {@link SquareData} at the given row and column.
   * 
   * @param row The row index.
   * @param col The column index.
   */
  public SquareData getSquareData(int row, int col) {
    return squareData_.get(row, col);
  }

  /**
   * Anchor management logic is factored into this subclass.
   */
  private class AnchorUpdater {
    /**
     * Updates the anchor data for this game, given that the specified move has
     * been played on the board.
     * 
     * @param move The move that was played.
     */
    public void update(Move move) {
      // If the move is down, simply transpose the board so we can pretend that
      // it's across.
      if (!move.isAcross())
        transpose();

      int row = move.getRowOrCol();
      int col = 0;
      for (Map.Entry<Integer, Tile> mapEntry : move.getTileMap().entrySet()) {
        // Update the anchors for the current tile, the tile above, and the tile
        // below.
        col = mapEntry.getKey();
        updateAnchors(row, col);
        if (row > 0)
          updateAnchors(row - 1, col);
        if (row < board_.size() - 1)
          updateAnchors(row + 1, col);
      }

      // Update the anchors on the square immediately left of the play.
      int wordLeftBound = BoardUtilities.findWordBoundary(board_, row, col,
                                                          BoardUtilities.LEFT) - 1;
      if (wordLeftBound >= 0)
        updateAnchors(row, wordLeftBound);

      // Update the anchors on the square immediately right of the play.
      int wordRightBound = BoardUtilities.findWordBoundary(board_, row, col,
                                                           BoardUtilities.RIGHT) + 1;
      if (wordRightBound < board_.size())
        updateAnchors(row, wordRightBound);

      // Transpose the board back to its original orientation, if necessary.
      if (!move.isAcross())
        transpose();
    }

    /**
     * Updates the anchors on the specified square.
     * 
     * @param row The row index.
     * @param col The column index.
     */
    private void updateAnchors(int row, int col) {
      Tile tileAbove = row > 0 ? board_.get(row - 1, col).getTile() : null;
      Tile tileLeft = col > 0 ? board_.get(row, col - 1).getTile() : null;

      SquareData.Anchor anchor = squareData_.get(row, col).getAnchor();
      if (board_.get(row, col).getTile() != null) {
        // If there is a tile on this square, then we are only an across anchor
        // if the square immediately left of us is empty.
        anchor.setAcrossAnchor(tileLeft == null);

        // Similarly, we are only an down anchor if the square immediately
        // above us is empty.
        anchor.setDownAnchor(tileAbove == null);
      } else {
        // If there is no tile on this square, then we need to take all of the
        // surrounding tiles into account.
        Tile tileBelow = null;
        if (row < board_.size() - 1)
          tileBelow = board_.get(row + 1, col).getTile();

        Tile tileRight = null;
        if (col < board_.size() - 1)
          tileRight = board_.get(row, col + 1).getTile();

        // We are only an across anchor if there is a tile either above or below
        // us AND there is no tile either to the left or the right.
        anchor.setAcrossAnchor((tileAbove != null || tileBelow != null)
                               && (tileLeft == null && tileRight == null));

        // The down anchor is set according to the transpose of the above logic.
        anchor.setDownAnchor((tileLeft != null || tileRight != null)
                             && (tileAbove == null && tileBelow == null));
      }
    }
  }

  /**
   * Cross-set management logic is factored into this subclass.
   */
  private class CrossSetUpdater {
    private Arc arc_;
    private int row_;
    private int col_;

    /**
     * Updates the cross-set data for this game, given that the specified move
     * has been played on the board.
     * 
     * @param move The move that was played.
     */
    public void update(Move move) {
      // If the move is across, then we'll be looking at column word-formations
      // first, so transpose the board.
      if (move.isAcross())
        transpose();

      int row = move.getRowOrCol();
      int col = 0;
      for (Map.Entry<Integer, Tile> mapEntry : move.getTileMap().entrySet()) {
        // Update the cross-sets for the word formed on this column.
        col = mapEntry.getKey();
        update(col, row);
      }

      // We now need to look at the row word-formation (or column, if this is a
      // down move), so transpose again.
      transpose();

      // Update the cross-sets for the word formed on this row.
      update(row, col);

      // Transpose the board back to its original state.
      if (!move.isAcross())
        transpose();
    }

    /**
     * Updates the cross-sets for the word-boundary squares of the given word.
     * 
     * @param row The row that this word is located on.
     * @param letterPos Any internal column of the word.
     */
    private void update(int row, int letterPos) {
      arc_ = gaddag_.getRootArc();
      row_ = row;
      int wordEnd = BoardUtilities.findWordBoundary(board_, row, letterPos,
                                                    BoardUtilities.RIGHT);
      col_ = wordEnd;

      // Travel along the GADDAG to the left word boundary.
      traverseToWordBoundary(BoardUtilities.LEFT);

      // Find the cross-set at the left boundary.
      if (col_ >= 0)
        computeCrossSet(BoardUtilities.LEFT);

      // Switch to the suffix sub-graph.
      arc_ = arc_.getDestination().getArc(GADDAG.DELIMITER);

      // Find the cross-set at the right boundary.
      col_ = wordEnd + 1;
      if (col_ < board_.size())
        computeCrossSet(BoardUtilities.RIGHT);
    }

    /**
     * Computes the cross set at the current location in the specified
     * direction.
     * 
     * @param direction The direction of the cross-set. Either
     *          {@link BoardUtilities#LEFT} or {@link BoardUtilities#RIGHT}.
     */
    private void computeCrossSet(int direction) {
      Set<Letter> crossSet = squareData_.get(row_, col_).getCrossSet()
                                        .getDownSet();
      crossSet.clear();

      // If there is no tile immediately to the (left or right, according to
      // |direction|), then the cross-set on this tile is equal to the
      // letter-set on the current arc.
      if (!board_.isValidPosition(row_, col_ + direction)
          || board_.get(row_, col_ + direction).getTile() == null) {
        crossSet.addAll(arc_.getLetterSet());
      } else {
        // Otherwise, we must try every possible letter and see if we can reach
        // the /next/ word boundary by following the GADDAG. If we can, then the
        // letter we tried is part of the cross-set.
        Arc oldArc = arc_;
        int oldCol = col_;
        for (Letter letter : arc_.getLetterSet()) {
          arc_ = gaddag_.getArc(arc_.getDestination(), letter);
          traverseToWordBoundary(direction);
          if (arc_ != null)
            crossSet.add(letter);
          col_ = oldCol;
          arc_ = oldArc;
        }
      }
    }

    /**
     * Using the letters on the board, traverses the GADDAG until an empty
     * square is reached. If the GADDAG does not contain a path for the letters,
     * then this function will set {@code arc_} to {@code null} and return
     * early.
     * 
     * @param dir The direction to traverse on the board.
     */
    private void traverseToWordBoundary(int dir) {
      while (board_.isValidPosition(row_, col_) && arc_ != null) {
        Tile tile = board_.get(row_, col_).getTile();
        if (tile == null)
          break;
        arc_ = gaddag_.getArc(arc_.getDestination(), tile.getLetter());
        col_ += dir;
      }
    }
  }
}
