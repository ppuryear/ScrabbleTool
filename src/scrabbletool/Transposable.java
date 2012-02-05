package scrabbletool;

/**
 * <p>
 * This interface represents objects that are <em>transposable</em> in the
 * linear algebra sense of exchanging row- and column-indices.
 * </p>
 * <p>
 * Transposition is not destructive: when a {@code Transposable} object is
 * transposed, it produces a 90&deg;-rotated <em>view</em> of itself without
 * modifying its own state. See the {@link #transpose()} documentation for
 * details.
 * </p>
 * <p>
 * Implementors should parameterize {@code Transposable} with their own typename
 * to ensure that {@code transpose()} returns the correct type. For example, an
 * implementing class {@code Foo} would have the following declaration:
 * </p>
 * 
 * <pre>
 * class Foo implements Transposable&lt;Foo&gt; { ...
 * </pre>
 * 
 * @author Philip Puryear
 */
public interface Transposable<T extends Transposable<T>> {
  /**
   * Returns a transposed view of this object
   * <em>without modifying this object's state</em>. This view may or may not be
   * backed by the original object.
   */
  public T transpose();
}
