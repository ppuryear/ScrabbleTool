package scrabbletool.gaddag;

/**
 * The prefix-suffix boundary symbol in the GADDAG.
 * 
 * @author Philip Puryear
 */
class Delimiter extends ArcLetter {
  /**
   * Creates a new delimiter.
   */
  public Delimiter() {
    super(null);
  }

  @Override
  public int compareTo(ArcLetter other) {
    if (other instanceof Delimiter)
      return 0;
    return 1;
  }
}
