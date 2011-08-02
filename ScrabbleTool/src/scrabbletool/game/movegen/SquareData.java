package scrabbletool.game.movegen;

import java.util.Set;
import java.util.TreeSet;
import scrabbletool.Transposable;
import scrabbletool.game.Letter;

/**
 * This class contains the per-square book-keeping data used by the move
 * generator, namely the {@link Anchor} and {@link CrossSet}.
 * 
 * @author Philip Puryear
 */
public class SquareData implements Transposable<SquareData> {
  /**
   * This class encapsulates the vertical and horizontal anchor bits for a given
   * square.
   */
  public static class Anchor implements Transposable<Anchor> {
    /**
     * A boolean wrapper whose value is mutable.
     */
    private static class MutableBoolean {
      private boolean value_;

      /**
       * Creates a new boolean with the given value.
       * 
       * @param value The value to use.
       */
      public MutableBoolean(boolean value) {
        value_ = value;
      }

      /**
       * Returns the underlying boolean value of this object.
       */
      public boolean getValue() {
        return value_;
      }

      /**
       * Sets the underlying boolean value of this object.
       * 
       * @param value The value to use.
       */
      public void setValue(boolean value) {
        value_ = value;
      }
    }

    private MutableBoolean across_;
    private MutableBoolean down_;

    /**
     * Creates a new anchor object with both across and down bits set to
     * {@code false}.
     */
    public Anchor() {
      this(new MutableBoolean(false), new MutableBoolean(false));
    }

    /**
     * Creates a new anchor object with the given across and down bits.
     * 
     * @param across The across bit to use.
     * @param down The down bit to use.
     */
    private Anchor(MutableBoolean across, MutableBoolean down) {
      across_ = across;
      down_ = down;
    }

    /**
     * Returns true if and only if this square is an across anchor.
     */
    public boolean isAcrossAnchor() {
      return across_.getValue();
    }

    /**
     * Returns true if and only if this square is a down anchor.
     */
    public boolean isDownAnchor() {
      return down_.getValue();
    }

    /**
     * Changes the across-anchor bit on this square according to {@code value}.
     * 
     * @param value True to make this square an across anchor, false otherwise.
     */
    public void setAcrossAnchor(boolean value) {
      across_.setValue(value);
    }

    /**
     * Changes the down-anchor bit on this square according to {@code value}.
     * 
     * @param value True to make this square a down anchor, false otherwise.
     */
    public void setDownAnchor(boolean value) {
      down_.setValue(value);
    }

    @Override
    public Anchor transpose() {
      return new Anchor(down_, across_);
    }
  }

  /**
   * This class encapsulates the vertical and horizontal cross-sets for a given
   * square.
   */
  public static class CrossSet implements Transposable<CrossSet> {
    private Set<Letter> across_;
    private Set<Letter> down_;

    /**
     * Creates a new cross-set container.
     * 
     * @param allowedLetters The initial contents of both the across and down
     *          cross-sets.
     */
    public CrossSet(Set<Letter> allowedLetters) {
      this(new TreeSet<Letter>(allowedLetters),
           new TreeSet<Letter>(allowedLetters));
    }

    /**
     * Creates a new cross-set container using the given sets.
     * 
     * @param across The across set to use.
     * @param down The down set to use.
     */
    private CrossSet(Set<Letter> across, Set<Letter> down) {
      across_ = across;
      down_ = down;
    }

    /**
     * Returns the set of letters that are allowed for across moves on this
     * square.
     */
    public Set<Letter> getAcrossSet() {
      return across_;
    }

    /**
     * Returns the set of letters that are allowed for down moves on this
     * square.
     */
    public Set<Letter> getDownSet() {
      return down_;
    }

    @Override
    public CrossSet transpose() {
      return new CrossSet(down_, across_);
    }
  }

  private Anchor anchor_;
  private CrossSet crossSet_;

  /**
   * Creates a new container.
   * 
   * @param initialCrossSet The initial contents of both the across and down
   *          cross-sets.
   */
  public SquareData(Set<Letter> initialCrossSet) {
    this(new Anchor(), new CrossSet(initialCrossSet));
  }

  /**
   * Creates a new container using the given backing objects.
   * 
   * @param anchor The {@link Anchor} to use.
   * @param crossSet The {@link CrossSet} to use.
   */
  private SquareData(Anchor anchor, CrossSet crossSet) {
    anchor_ = anchor;
    crossSet_ = crossSet;
  }

  /**
   * Returns the {@link Anchor} object for this square.
   */
  public Anchor getAnchor() {
    return anchor_;
  }

  /**
   * Returns the {@link CrossSet} object for this square.
   */
  public CrossSet getCrossSet() {
    return crossSet_;
  }

  @Override
  public SquareData transpose() {
    return new SquareData(anchor_.transpose(), crossSet_.transpose());
  }
}
