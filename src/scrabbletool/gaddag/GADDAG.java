package scrabbletool.gaddag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import scrabbletool.game.Letter;

/**
 * A graph structure designed for efficient scrabble move-generation. For
 * details, see the 1994 paper by Gordon.
 * 
 * @author Philip Puryear
 */
public class GADDAG {
  /**
   * The prefix-suffix separator symbol.
   */
  public static final ArcLetter DELIMITER = new Delimiter();

  private Arc rootArc_;
  private Map<Letter, ArcLetter> alphabetMap_;

  /**
   * Creates a new, empty GADDAG.
   */
  GADDAG() {
    rootArc_ = new Arc(new Node());
    alphabetMap_ = new TreeMap<Letter, ArcLetter>();
  }

  /**
   * Returns the root arc into the graph, i.e. the arc leading to the root node.
   */
  public Arc getRootArc() {
    return rootArc_;
  }

  /**
   * Returns the arc for the given letter leaving the given node.
   * 
   * @param node The node to use.
   * @param letter The letter for the arc to find.
   */
  public Arc getArc(Node node, Letter letter) {
    return node.getArc(alphabetMap_.get(letter));
  }

  /**
   * Adds a word to the GADDAG.
   * 
   * @param word The word to add.
   */
  void addWord(List<Letter> word) {
    int wordSize = word.size();

    // Convert the word to a sequence of ArcLetters, prefixed by the delimiter.
    List<ArcLetter> gaddagWord = wordToGADDAGWord(word);

    // Create a final path for REV(word).
    addPath(gaddagWord.subList(2, wordSize + 1), word.get(0));

    // Create a final path for REV(word[n-2 downto 0])$word[n-1].
    Node node = addPath(gaddagWord.subList(0, wordSize), word.get(wordSize - 1));

    // Create the remaining paths while partially minimizing the graph.
    for (int m = wordSize - 1; m >= 2; m--) {
      Node forceNode = node;
      node = addPath(gaddagWord.subList(0, m), null);
      Arc forcedArc = node.forceArc(gaddagWord.get(m), forceNode);

      // The first path created by this loop needs to be final.
      // TODO: This is ugly.
      if (m == wordSize - 1)
        forcedArc.addLetter(word.get(wordSize - 1));
    }
  }

  /**
   * Converts the given letter list to a list of {@link ArcLetter}s, prefixed by
   * the delimiter.
   * 
   * @param word The word to convert.
   */
  private List<ArcLetter> wordToGADDAGWord(List<Letter> word) {
    List<ArcLetter> gaddagWord = new ArrayList<ArcLetter>(word.size() + 1);
    gaddagWord.add(DELIMITER);
    for (Letter letter : word) {
      // Get the ArcLetter corresponding to this Letter from the alphabet map.
      ArcLetter gaddagLetter = alphabetMap_.get(letter);
      if (gaddagLetter == null) {
        // If one was not found, create one and add it to the map.
        gaddagLetter = new ArcLetter(letter);
        alphabetMap_.put(letter, gaddagLetter);
      }
      gaddagWord.add(gaddagLetter);
    }
    return gaddagWord;
  }

  /**
   * Adds a path to the GADDAG for the reverse of the given sequence of letters.
   * Additionally, if {@code letterSetAddition} is non-null, the path will be
   * made <em>final</em> by the addition of the given letter to the final arc's
   * letter set.
   * 
   * @param letters The letters to comprise the path.
   * @param letterSetAddition The letter to add to the final arc's letter set,
   *          or {@code null} if no addition is desired.
   * @return The final {@link Node} in the created path.
   */
  private Node addPath(List<ArcLetter> letters, Letter letterSetAddition) {
    Arc arc = rootArc_;
    for (int i = letters.size() - 1; i >= 0; i--)
      arc = arc.getDestination().addArc(letters.get(i));
    if (letterSetAddition != null)
      arc.addLetter(letterSetAddition);
    return arc.getDestination();
  }
}
