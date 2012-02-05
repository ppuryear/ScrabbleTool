package scrabbletool.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import scrabbletool.ScrabbleTool;
import scrabbletool.gaddag.GADDAG;
import scrabbletool.gaddag.GADDAGFactory;
import scrabbletool.gaddag.GADDAGFactory.WordSizeException;
import scrabbletool.game.XMLUtilities.XMLSyntaxException;
import scrabbletool.game.board.Board;
import scrabbletool.game.board.Board.BoardParameterException;
import scrabbletool.game.board.Modifier;

/**
 * This class is responsible for the creation of {@link Game} objects.
 * 
 * @author Philip Puryear
 */
public class GameFactory {
  private static Map<String, Modifier.Type> modifierTypeMap_;

  static {
    modifierTypeMap_ = new TreeMap<String, Modifier.Type>();
    modifierTypeMap_.put("letterscore", Modifier.Type.LETTER_SCORE);
    modifierTypeMap_.put("wordscore", Modifier.Type.WORD_SCORE);
  }

  /**
   * Instantiates a new {@code Game}.
   * 
   * @param gameTypeFile The game descriptor XML file.
   * @throws IOException If there was an error reading from the file.
   * @throws GameFileException If the file's syntax is incorrect.
   * @throws WordSizeException If the dictionary used by the file contains a
   *           word of length < 2.
   */
  public static Game newGame(Path gameTypeFile) throws IOException,
                                               XMLSyntaxException,
                                               WordSizeException {
    // Parse the game file into an XML document object.
    Document gameDoc = null;
    try (InputStream gameTypeFileIS = Files.newInputStream(gameTypeFile)) {
      DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                 .newDocumentBuilder();
      gameDoc = db.parse(gameTypeFileIS);
    } catch (ParserConfigurationException e) {
      // Shouldn't happen, since we're using the default parser configuration.
      e.printStackTrace();
      return null;
    } catch (SAXException e) {
      // Notify the caller if there is a syntax exception.
      throw new XMLSyntaxException(e);
    }

    // Retrieve the core data elements from the document.
    Element rootElement = gameDoc.getDocumentElement();
    Element tilesElement = XMLUtilities.getSingleChildElementByTagName(rootElement,
                                                                       "tiles");
    Element dictElement = XMLUtilities.getSingleChildElementByTagName(rootElement,
                                                                      "dictionary");
    Element modifiersElement = XMLUtilities.getSingleChildElementByTagName(rootElement,
                                                                           "modifiers");
    Element boardElement = XMLUtilities.getSingleChildElementByTagName(rootElement,
                                                                       "board");

    // Construct the various structures needed by |Game|.
    Alphabet alphabet = newAlphabet(tilesElement);
    GADDAG gaddag = newGADDAG(dictElement, alphabet);
    Board board = newBoard(boardElement, modifiersElement);

    return new Game(board, alphabet, gaddag);
  }

  /**
   * Constructs an {@link Alphabet} object from the XML document.
   * 
   * @param tilesElement The "tiles" element in the document.
   * @throws GameFileException If there is a syntax problem.
   */
  private static Alphabet newAlphabet(Element tilesElement) throws XMLSyntaxException {
    // Extract all of the "letter" elements in the document.
    List<Element> letterElements = XMLUtilities.getChildElementsByTagName(tilesElement,
                                                                          "letter");
    Alphabet alphabet = new Alphabet();
    // Add each letter to the alphabet.
    for (Element letterElement : letterElements) {
      // Get the textual representation of this letter.
      String text = letterElement.getAttribute("text");
      if (text.isEmpty())
        throw new XMLSyntaxException(letterElement);

      // Get the letter score value of this letter.
      int value = 0;
      try {
        value = Integer.parseInt(letterElement.getAttribute("value"));
      } catch (NumberFormatException e) {
        throw new XMLSyntaxException(letterElement);
      }
      alphabet.addLetter(new Letter(text, value));
    }
    return alphabet;
  }

  /**
   * Constructs a new GADDAG.
   * 
   * @param dictElement The "dictionary" element in the document.
   * @param alphabet The alphabet to be used by this GADDAG.
   * @throws IOException If there was a problem reading the dictionary file.
   * @throws WordSizeException If the dictionary file contains a word of length
   *           < 2.
   * @throws GameFileException If there is a syntax problem.
   */
  private static GADDAG newGADDAG(Element dictElement, Alphabet alphabet) throws IOException,
                                                                         WordSizeException,
                                                                         XMLSyntaxException {
    // The dictionary filename is contained in the "filename" attribute.
    String dictFileName = dictElement.getAttribute("filename");
    if (dictFileName.isEmpty())
      throw new XMLSyntaxException(dictElement);
    Path dictFile = ScrabbleTool.DICTIONARY_FOLDER.resolve(dictFileName);
    return GADDAGFactory.newGADDAG(dictFile, alphabet);
  }

  /**
   * Constructs a new {@link Board}.
   * 
   * @param boardElement The "board" element in the document.
   * @return
   * @throws GameFileException
   */
  private static Board newBoard(Element boardElement, Element modifiersElement) throws XMLSyntaxException {
    Map<String, Modifier> modifiers = parseModifiers(modifiersElement);
    Board board = null;
    try {
      // Create the board object.
      int size = Integer.parseInt(boardElement.getAttribute("size"));
      int startRow = Integer.parseInt(boardElement.getAttribute("startrow"));
      int startCol = Integer.parseInt(boardElement.getAttribute("startcol"));
      try {
        board = new Board(size, startRow, startCol);
      } catch (BoardParameterException e) {
        throw new XMLSyntaxException(e);
      }

      // Add all of the listed modifiers to the board.
      List<Element> modifierElements = XMLUtilities.getChildElementsByTagName(boardElement,
                                                                              "modifier");
      for (Element modifierElement : modifierElements) {
        String identifier = modifierElement.getAttribute("name");
        int row = Integer.parseInt(modifierElement.getAttribute("row"));
        int col = Integer.parseInt(modifierElement.getAttribute("col"));

        // Find the |Modifier| for this identifier.
        Modifier modifier = modifiers.get(identifier);
        if (modifier == null)
          throw new XMLSyntaxException(modifierElement);

        board.get(row, col).setModifier(modifier);
      }
    } catch (NumberFormatException e) {
      throw new XMLSyntaxException(e);
    }
    return board;
  }

  /**
   * Parses the "modifiers" section of the document and returns a set of
   * {@link Modifier} objects, mapped by their identifier strings.
   * 
   * @param modifiersElement The "modifiers" element in the document.
   * @throws GameFileException If there is a syntax problem.
   */
  private static Map<String, Modifier> parseModifiers(Element modifiersElement) throws XMLSyntaxException {
    // Extract each "modifier" sub-element.
    List<Element> modifierElements = XMLUtilities.getChildElementsByTagName(modifiersElement,
                                                                            "modifier");
    Map<String, Modifier> modifiers = new TreeMap<String, Modifier>();
    for (Element modifierElement : modifierElements) {
      // Extract the identifier of this modifier.
      String identifier = modifierElement.getAttribute("name");

      // Extract the magnitude of this modifier.
      int magnitude = 0;
      try {
        magnitude = Integer.parseInt(modifierElement.getAttribute("magnitude"));
      } catch (NumberFormatException e) {
        throw new XMLSyntaxException(modifierElement);
      }

      // Extract the modifier type.
      String type = modifierElement.getAttribute("type");
      Modifier.Type modifierType = modifierTypeMap_.get(type);
      if (modifierType == null || identifier.isEmpty())
        throw new XMLSyntaxException(modifierElement);

      // Instantiate the modifier.
      Modifier modifier = new Modifier(modifierType, magnitude);
      modifiers.put(identifier, modifier);
    }
    return modifiers;
  }
}
