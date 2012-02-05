package scrabbletool.game.board;

/**
 * A Scrabble score modifier. Each modifier has a <em>magnitude</em> and a
 * {@link Type}.
 * 
 * @author Philip Puryear
 */
public class Modifier {
  /**
   * This class enumerates all modifier types.
   * 
   * @see Type#LETTER_SCORE
   * @see Type#WORD_SCORE
   */
  public static enum Type {
    /**
     * A letter score modifier.
     */
    LETTER_SCORE,

    /**
     * A word score modifier.
     */
    WORD_SCORE
  }

  private Type type_;
  private int magnitude_;

  /**
   * Constructs a new modifier.
   * 
   * @param type The {@link Type} of modifier.
   * @param magnitude The magnitude of this modifier.
   */
  public Modifier(Type type, int magnitude) {
    type_ = type;
    magnitude_ = magnitude;
  }

  /**
   * Returns the type of this modifier.
   */
  public Type getType() {
    return type_;
  }

  /**
   * Returns the magnitude of this modifier.
   */
  public int getMagnitude() {
    return magnitude_;
  }
}
