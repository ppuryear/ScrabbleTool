package scrabbletool.game;

/**
 * <p>
 * A {@code Letter} is any letter that may appear in a valid Scrabble word. It
 * has a {@link #getText() textual representation} and a {@link #getValue()
 * letter score value}.
 * </p>
 * <p>
 * This class has a natural ordering that is determined by the natural ordering
 * of its textual representation.
 * </p>
 * 
 * @author Philip Puryear
 */
public class Letter implements Comparable<Letter> {
  private String text_;
  private int value_;

  /**
   * Constructs a new letter with the given text and value.
   * 
   * @param text The textual representation of this letter.
   * @param value The letter score value of this letter.
   */
  public Letter(String text, int value) {
    text_ = text;
    value_ = value;
  }

  /**
   * Returns the textual representation of this letter.
   */
  public String getText() {
    return text_;
  }

  /**
   * Returns the letter score value of this letter.
   */
  public int getValue() {
    return value_;
  }

  @Override
  public int compareTo(Letter other) {
    return text_.compareTo(other.text_);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (!(other instanceof Letter))
      return false;

    Letter otherLetter = (Letter) other;
    return text_.equals(otherLetter.text_);
  }

  @Override
  public int hashCode() {
    return text_.hashCode();
  }
}
