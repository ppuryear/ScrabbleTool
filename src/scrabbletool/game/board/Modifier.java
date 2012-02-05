package scrabbletool.game.board;

/**
 * A Scrabble score modifier. Each modifier has a <em>magnitude</em>, an integer
 * whose specific meaning depends on the subclass. (For instance, the magnitude
 * of a {@link WordScoreModifier} refers to the word score multiplication
 * factor.)
 * 
 * @author Philip Puryear
 */
public abstract class Modifier {
  private int magnitude_;

  /**
   * Constructs a new modifier with the given magnitude.
   * 
   * @param magnitude The magnitude of this modifier.
   */
  public Modifier(int magnitude) {
    magnitude_ = magnitude;
  }

  /**
   * Returns the magnitude of this modifier.
   */
  public int getMagnitude() {
    return magnitude_;
  }
}
