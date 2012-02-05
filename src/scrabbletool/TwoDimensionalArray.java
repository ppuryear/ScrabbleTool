package scrabbletool;

/**
 * A two-dimensional array.
 * 
 * @author Philip Puryear
 */
public class TwoDimensionalArray<T> {
  /**
   * Thrown when a user attempts to create a sub-array with invalid bounds.
   */
  public static class InvalidBoundsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with the given parameters.
     * 
     * @param numRows The number of rows in the parent array.
     * @param numCols The number of columns in the parent array.
     * @param rowMin The user-supplied row lower-bound (inclusive).
     * @param rowMax The user-supplied row upper-bound (exclusive).
     * @param colMin The user-supplied column lower-bound (inclusive).
     * @param colMax The user-supplied column upper-bound (exclusive).
     */
    public InvalidBoundsException(int numRows,
                                  int numCols,
                                  int rowMin,
                                  int rowMax,
                                  int colMin,
                                  int colMax) {
      super("Error: Cannot create sub-array with bounds "
            + boundsToString(rowMin, rowMax, colMin, colMax)
            + ". Bounds must lie inside the range "
            + boundsToString(0, numRows, 0, numCols) + ".");
    }

    /**
     * Creates a string representation of a set of 2D-array bounds.
     */
    private static String boundsToString(int rowMin,
                                         int rowMax,
                                         int colMin,
                                         int colMax) {
      return "{rows " + rowMin + " to " + rowMax + ", cols" + colMin + " to "
             + colMax + "}";
    }
  }

  private Object[][] array_;
  private int rowMin_;
  private int rowMax_;
  private int colMin_;
  private int colMax_;

  /**
   * Constructs a new 2D-array with the given number of rows and columns.
   * 
   * @param rows The number of rows.
   * @param cols The number of columns.
   */
  public TwoDimensionalArray(int rows, int cols) {
    this(new Object[rows][cols], 0, rows, 0, cols);
  }

  /**
   * Constructs a 2D-array backed by another 2D-array.
   */
  protected TwoDimensionalArray(TwoDimensionalArray<T> other) {
    this(other.array_, other.colMin_, other.colMax_, other.rowMin_,
         other.rowMax_);
  }

  /**
   * Creates a 2D-array with the given member data.
   */
  private TwoDimensionalArray(Object[][] array,
                              int rowMin,
                              int rowMax,
                              int colMin,
                              int colMax) {
    array_ = array;
    rowMin_ = rowMin;
    rowMax_ = rowMax;
    colMin_ = colMin;
    colMax_ = colMax;
  }

  /**
   * Returns the element at the specified row and column in the array.
   * 
   * @param row The row of the element to return.
   * @param col The column of the element to return.
   */
  public T get(int row, int col) {
    @SuppressWarnings("unchecked")
    T element = (T) array_[row - rowMin_][col - colMin_];
    return element;
  }

  /**
   * Returns a new array with the given index bounds. (The array returned is
   * backed by the original array.)
   * 
   * @param rowMin The row lower bound (inclusive).
   * @param rowMax The row upper bound (exclusive).
   * @param colMin The column lower bound (inclusive).
   * @param colMax The column upper bound (exclusive).
   */
  public TwoDimensionalArray<T> subArray(int rowMin,
                                         int rowMax,
                                         int colMin,
                                         int colMax) {
    if (!isValidPosition(rowMin, colMin)
        || !isValidPosition(rowMax - 1, colMax - 1) || rowMin >= rowMax
        || colMin >= colMax)
      throw new InvalidBoundsException(getNumRows(), getNumCols(), rowMin,
                                       rowMax, colMin, colMax);
    return new TwoDimensionalArray<T>(array_, rowMin, rowMax, colMin, colMax);
  }

  /**
   * Replaces the element at the specified row and column with the specified
   * element.
   * 
   * @param row The row of the element to replace.
   * @param col The column of the element to replace.
   * @param element The element to be stored.
   * @return The element previously stored at the specified location.
   */
  public T set(int row, int col, T element) {
    T oldElement = get(row, col);
    array_[row - rowMin_][col - colMin_] = element;
    return oldElement;
  }

  /**
   * Returns the number of rows in this array.
   */
  public int getNumRows() {
    return rowMax_ - rowMin_;
  }

  /**
   * Returns the number of columns in this array.
   */
  public int getNumCols() {
    return colMax_ - colMin_;
  }

  /**
   * Returns true if and only if the given row and column constitute a valid
   * array coordinate.
   * 
   * @param row The row component.
   * @param col The column component.
   */
  public boolean isValidPosition(int row, int col) {
    return (row >= 0 && row < getNumRows()) && (col >= 0 && col < getNumCols());
  }
}
