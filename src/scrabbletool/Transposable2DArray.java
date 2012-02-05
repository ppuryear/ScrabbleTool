package scrabbletool;

/**
 * <p>
 * A {@link TwoDimensionalArray} that is {@link Transposable}.
 * </p>
 * <p>
 * In addition to the array itself, the elements stored by this array must be
 * {@code Transposable} in order to account for situations where the array
 * elements' states are related to the orientation of the outer array (as in,
 * for instance, nested 2D-arrays).
 * </p>
 * 
 * @author Philip Puryear
 */
public class Transposable2DArray<T extends Transposable<T>> extends
        TwoDimensionalArray<T> implements Transposable<Transposable2DArray<T>> {
  private TwoDimensionalArray<T> transposedArray_;

  /**
   * Constructs a new array with the given number of rows and columns.
   * 
   * @param rows The number of rows.
   * @param cols The number of columns.
   */
  public Transposable2DArray(int rows, int cols) {
    super(rows, cols);
    transposedArray_ = new TwoDimensionalArray<T>(cols, rows);
  }

  /**
   * Constructs a 2D-array backed by another 2D-array.
   */
  protected Transposable2DArray(Transposable2DArray<T> other) {
    this(other, other.transposedArray_);
  }

  /**
   * Constructs a new array with the given row- and column-major-ordered arrays.
   * 
   * @param rowArray The underlying array.
   * @param colArray A transposed version of {@code rowArray}.
   */
  private Transposable2DArray(TwoDimensionalArray<T> rowArray,
                              TwoDimensionalArray<T> colArray) {
    super(rowArray);
    transposedArray_ = colArray;
  }

  @Override
  public Transposable2DArray<T> subArray(int rowMin,
                                         int rowMax,
                                         int colMin,
                                         int colMax) {
    TwoDimensionalArray<T> rowSubArray = super.subArray(rowMin, rowMax, colMin,
                                                        colMax);
    TwoDimensionalArray<T> colSubArray = transposedArray_.subArray(colMin,
                                                                   colMax,
                                                                   rowMin,
                                                                   rowMax);
    return new Transposable2DArray<T>(rowSubArray, colSubArray);
  }

  @Override
  public T set(int row, int col, T element) {
    // We need to place this element into both the row- and column-major-ordered
    // arrays.
    transposedArray_.set(col, row, element.transpose());
    return super.set(row, col, element);
  }

  /**
   * Returns a transposed view of this array, backed by this object. This array
   * is not modified by this operation.
   */
  @Override
  public Transposable2DArray<T> transpose() {
    return new Transposable2DArray<T>(transposedArray_, this);
  }
}
