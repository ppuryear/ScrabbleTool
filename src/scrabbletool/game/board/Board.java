package scrabbletool.game.board;

import java.util.Map;
import scrabbletool.Transposable2DArray;
import scrabbletool.game.Move;

/**
 * A Scrabble board, consisting of a square array of {@link Square}s.
 * 
 * @author Philip Puryear
 */
public class Board extends Transposable2DArray<Square> {
  /**
   * Thrown when the board is given an invalid construction parameter.
   */
  public static class BoardParameterException extends Exception {
    private static final long serialVersionUID = 1L;

    public BoardParameterException(String message) {
      super(message);
    }
  }

  /**
   * Thrown when the board is given a size < 1.
   */
  public static class BoardSizeException extends BoardParameterException {
    private static final long serialVersionUID = 1L;

    public BoardSizeException(int size) {
      super("Cannot create board with size " + size + ". Size must be >= 1.");
    }
  }

  /**
   * Thrown when the board is given a start position that is not inside the
   * board.
   */
  public static class StartPositionException extends BoardParameterException {
    private static final long serialVersionUID = 1L;

    public StartPositionException(int row, int col) {
      super("Cannot create board with start position (" + row + "," + col
            + "). Start position must be inside the board.");
    }
  }

  private int startRow_;
  private int startCol_;

  /**
   * Creates a new board with the given parameters.
   * 
   * @param size The size of this board, i.e. the number of rows and columns.
   * @param startRow The row of the start position.
   * @param startCol The column of the start position.
   * @throws BoardParameterException If one of the supplied parameters is
   *           invalid.
   */
  public Board(int size, int startRow, int startCol) throws BoardParameterException {
    super(size, size);
    if (size < 1)
      throw new BoardSizeException(size);

    if (startRow < 0 || startRow >= size || startCol < 0 || startCol >= size)
      throw new StartPositionException(startRow, startCol);
    startRow_ = startRow;
    startCol_ = startCol;

    // Create a |Square| for each position on this board.
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++)
        super.set(i, j, new Square());
    }
  }

  /**
   * Creates a new board using the given array of {@link Square}s, start row,
   * and start column.
   * 
   * @param squares The array of squares to use.
   * @param startRow The start row to use.
   * @param startCol The start column to use.
   */
  protected Board(Transposable2DArray<Square> squares,
                  int startRow,
                  int startCol) {
    super(squares);
    startRow_ = startRow;
    startCol_ = startCol;
  }

  /**
   * Returns the size of this board.
   */
  public int size() {
    return super.getNumRows();
  }

  /**
   * Returns the row component of the board's start position.
   */
  public int getStartRow() {
    return startRow_;
  }

  /**
   * Returns the column component of the board's start position.
   */
  public int getStartCol() {
    return startCol_;
  }

  /**
   * Plays a move on this board.
   * 
   * @param move The move to play.
   */
  public void playMove(Move move) {
    Board board = move.isAcross() ? this : this.transpose();
    int row = move.getRowOrCol();
    for (Map.Entry<Integer, Tile> mapEntry : move.getTileMap().entrySet()) {
      int col = mapEntry.getKey();
      board.get(row, col).placeTile(mapEntry.getValue());
    }
  }

  @Override
  public Board transpose() {
    return new Board(super.transpose(), startCol_, startRow_);
  }
}
