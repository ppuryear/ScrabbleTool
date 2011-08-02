package scrabbletool.game;

import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A set of methods for dealing with XML documents.
 * 
 * @author Philip Puryear
 */
class XMLUtilities {
  /**
   * Indicates there was a syntax error in the XML file.
   */
  public static class XMLSyntaxException extends Exception {
    private static final long serialVersionUID = 1L;

    public XMLSyntaxException(Throwable cause) {
      super("There was a problem interpreting the XML file: "
            + cause.getMessage() + ".", cause);
    }

    public XMLSyntaxException(Node problemNode) {
      super("There was a problem interpreting the XML node: "
            + problemNode.toString() + ".");
    }
  }

  /**
   * Returns a list of the direct child {@link Element}s of the given parent.
   * 
   * @param parent The parent element.
   */
  public static List<Element> getChildElements(Element parent) {
    List<Element> childElements = new LinkedList<Element>();
    NodeList childNodes = parent.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node childNode = childNodes.item(i);
      if (childNode instanceof Element)
        childElements.add((Element) childNode);
    }
    return childElements;
  }

  /**
   * Returns a list of the direct child {@link Element}s of the given parent
   * that have the specified tag name.
   * 
   * @param parent The parent element.
   * @param tagName The tag name to filter.
   */
  public static List<Element> getChildElementsByTagName(Element parent,
                                                        String tagName) {
    List<Element> childElementsWithTagName = new LinkedList<Element>();
    List<Element> childElements = getChildElements(parent);
    for (Element child : childElements) {
      if (child.getTagName().equals(tagName))
        childElementsWithTagName.add(child);
    }
    return childElementsWithTagName;
  }

  /**
   * Returns the child {@link Element} of the given parent that has the
   * specified tag name.
   * 
   * @param parent The parent element.
   * @param tagName The tag name of the child.
   * @throws GameFileException If the number of children with the specified tag
   *           name is not equal to 1.
   */
  public static Element getSingleChildElementByTagName(Element parent,
                                                       String tagName) throws XMLSyntaxException {
    List<Element> childElements = getChildElementsByTagName(parent, tagName);
    if (childElements.size() != 1)
      throw new XMLSyntaxException(parent);
    return childElements.get(0);
  }
}
