package scrabbletool.gaddag;

import java.util.Map;
import java.util.TreeMap;

/**
 * A vertex in the GADDAG graph. A {@code Node} has zero or more outgoing
 * {@link Arc}s to other nodes.
 * 
 * @author Philip Puryear
 */
public class Node {
  /**
   * Thrown when the GADDAG construction logic detects a malformed graph.
   */
  public class MalformedGADDAGException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MalformedGADDAGException(ArcLetter letter,
                                    Node wrongDest,
                                    Node rightDest) {
      super("Tried to force arc for " + letter + " from "
            + Node.this.toString() + " to " + rightDest.toString()
            + ", but an arc already exists going to " + wrongDest.toString()
            + ".");
    }
  }

  private Map<ArcLetter, Arc> outgoingArcs_;

  /**
   * Constructs a new node with no outgoing arcs.
   */
  public Node() {
    outgoingArcs_ = new TreeMap<ArcLetter, Arc>();
  }

  /**
   * Returns the arc for the specified letter leaving this node.
   * 
   * @param letter The letter associated with the arc to fetch.
   */
  public Arc getArc(ArcLetter letter) {
    return outgoingArcs_.get(letter);
  }

  /**
   * Adds an outgoing arc to this node for the specified letter, if one does not
   * already exist. Arcs created by this method will point to new, empty nodes.
   * 
   * @param letter The letter associated with the arc to create.
   * @return The created arc, or the existing one if one was found.
   */
  Arc addArc(ArcLetter letter) {
    Arc arc = getArc(letter);
    if (arc == null) {
      Node destination = new Node();
      arc = new Arc(destination);
      outgoingArcs_.put(letter, arc);
    }
    return arc;
  }

  /**
   * Adds an arc from this node to the specified destination node for the given
   * letter.
   * 
   * @param letter The letter associated with the arc to create.
   * @param destination The destination node for the arc.
   * @return The created arc, or the existing one if one was found.
   * @throws MalformedGADDAGException If an arc already exists for the given
   *           letter, but pointing at a node other than {@code destination}.
   */
  Arc forceArc(ArcLetter letter, Node destination) throws MalformedGADDAGException {
    Arc arc = getArc(letter);
    if (arc == null) {
      arc = new Arc(destination);
      outgoingArcs_.put(letter, arc);
    } else {
      Node arcDest = arc.getDestination();
      if (!arcDest.equals(destination))
        throw new MalformedGADDAGException(letter, arcDest, destination);
    }
    return arc;
  }
}
