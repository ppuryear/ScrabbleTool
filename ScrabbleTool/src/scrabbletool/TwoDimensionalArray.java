package scrabbletool;

/**
 * A two-dimensional array.
 * 
 * @author Philip Puryear
 */
public class TwoDimensionalArray<T> {
  private Object[][] array_;
  private int numRows_;
  private int numCols_;

  /**
   * Constructs a new 2D-array with the given number of rows and columns.
   * 
   * @param rows The number of rows.
   * @param cols The number of columns.
   */
  public TwoDimensionalArray(int rows, int cols) {
    numRows_ = rows;
    numCols_ = cols;
    array_ = new Object[rows][cols];
  }

  /**
   * Constructs a 2D-array backed by another 2D-array.
   */
  protected TwoDimensionalArray(TwoDimensionalArray<T> other) {
    array_ = other.array_;
    numRows_ = other.numRows_;
    numCols_ = other.numCols_;
  }

  /**
   * Returns the element at the specified row and column in the array.
   * 
   * @param row The row of the element to return.
   * @param col The column of the element to return.
   */
  public T get(int row, int col) {
    @SuppressWarnings("unchecked")
    T element = (T) array_[row][col];
    return element;
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
    @SuppressWarnings("unchecked")
    T oldElement = (T) array_[row][col];
    array_[row][col] = element;
    return oldElement;
  }

  /**
   * Returns the number of rows in this array.
   */
  public int getNumRows() {
    return numRows_;
  }

  /**
   * Returns the number of columns in this array.
   */
  public int getNumCols() {
    return numCols_;
  }
}
