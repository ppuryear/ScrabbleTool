package scrabbletool.gaddag;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import scrabbletool.game.Letter;

/**
 * An edge in the GADDAG graph. An {@code Arc} has a destination {@link Node}
 * and a letter set for determining valid words.
 */
public class Arc {
  private Node destination_;
  private Set<Letter> letterSet_;

  /**
   * Constructs a new arc pointing at the given destination node.
   * 
   * @param destination The node for this arc to point at.
   */
  public Arc(Node destination) {
    destination_ = destination;
    letterSet_ = new TreeSet<Letter>();
  }

  /**
   * Returns true if and only if the given letter exists in the letter set on
   * this arc.
   * 
   * @param letter The letter to test.
   */
  public boolean hasLetter(Letter letter) {
    return letterSet_.contains(letter);
  }

  /**
   * Returns an unmodifiable view of the letter set on this arc.
   */
  public Set<Letter> getLetterSet() {
    return Collections.unmodifiableSet(letterSet_);
  }

  /**
   * Returns this arc's destination node.
   */
  public Node getDestination() {
    return destination_;
  }

  /**
   * Adds the given letter to this arc's letter set.
   * 
   * @param letter The letter to add.
   */
  void addLetter(Letter letter) {
    letterSet_.add(letter);
  }
}
