package scrabbletool.game.board;

/**
 * A container class for the various board {@link Modifier}s.
 * 
 * @author Philip Puryear
 */
public class Modifiers {
  /**
   * A word-score modifier. The magnitude represents the factor by which the
   * player's word score is multiplied.
   */
  public static class WordScoreModifier extends Modifier {
    public WordScoreModifier(int magnitude) {
      super(magnitude);
    }
  }

  /**
   * A letter-score modifier. The magnitude represents the factor by which the
   * player's letter score is multiplied.
   */
  public static class LetterScoreModifier extends Modifier {
    public LetterScoreModifier(int magnitude) {
      super(magnitude);
    }
  }
}
