package scrabbletool.game.movegen;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import scrabbletool.gaddag.Arc;
import scrabbletool.gaddag.GADDAG;
import scrabbletool.game.Game;
import scrabbletool.game.Letter;
import scrabbletool.game.Move;
import scrabbletool.game.board.Blank;
import scrabbletool.game.board.Board;
import scrabbletool.game.board.Tile;

/**
 * Given a {@link Board} and a rack of {@link Tile}s, this class is capable of
 * finding all valid Scrabble moves that can be played.
 * 
 * @author Philip Puryear
 * @see #generate(List)
 */
public class MoveGenerator {
  private DataManager dataManager_;
  private Game game_;
  private Board board_;
  private GADDAG gaddag_;
  private Set<Move> moves_;
  private int anchorRow_;
  private int anchorCol_;
  private boolean transposed_;

  /**
   * Constructs a new move generator.
   * 
   * @param game The game to be played.
   */
  public MoveGenerator(Game game, GADDAG gaddag) {
    dataManager_ = new DataManager(game.getBoard(), gaddag, game.getAlphabet());
    game_ = game;
    board_ = game.getBoard();
    gaddag_ = gaddag;
    transposed_ = false;
  }

  /**
   * Lets this generator know that the specified move has been played on the
   * board. This method must be called every time the player (legally) modifies
   * the board so that the generator can maintain a consistent state.
   * 
   * @param move The move that was played.
   */
  public void update(Move move) {
    dataManager_.update(move);
  }

  /**
   * Finds all valid scrabble moves that can be played using the tiles in the
   * specified rack.
   * 
   * @param rack A list of {@link Tile}s that comprise the player's rack.
   * @return A list of all valid moves.
   */
  private void generate(List<Tile> rack) {
    moves_ = new HashSet<Move>();

    // Generate all across moves.
    generateAcrossMoves(rack);

    // Generate all down moves by transposing the board.
    transpose();
    generateAcrossMoves(rack);

    // Restore the board's original orientation.
    transpose();
  }

  /**
   * Transposes the data used by this generator.
   */
  private void transpose() {
    dataManager_.transpose();
    board_ = board_.transpose();
    transposed_ = !transposed_;
  }

  /**
   * Generates all across moves on the board.
   * 
   * @param rack The player's tile rack.
   */
  private void generateAcrossMoves(List<Tile> rack) {
    // For performance reasons, both the rack and the |word| structure must be
    // linked lists.
    LinkedList<Tile> linkedRack = new LinkedList<Tile>(rack);
    LinkedList<Tile> word = new LinkedList<Tile>();

    // Iterate over the board, looking for across anchors.
    for (anchorRow_ = 0; anchorRow_ < board_.size(); anchorRow_++) {
      for (anchorCol_ = 0; anchorCol_ < board_.size(); anchorCol_++) {
        if (dataManager_.getSquareData(anchorRow_, anchorCol_).getAnchor()
                        .isAcrossAnchor())
          generateOn(anchorCol_, word, linkedRack, gaddag_.getRootArc());
      }
    }
  }

  /**
   * This method is one of a pair of recursive coroutines (the other being
   * {@link #evaluateOn}) that perform move generation. For details of their
   * operation, see the 1994 paper by Gordon. (Note: In the Gordon paper, this
   * method is referred to as {@code Gen}).
   * 
   * @param currentCol The generation algorithm's current column.
   * @param word The word formed by the algorithm, up to this point.
   * @param rack The rack of remaining tiles.
   * @param arc The algorithm's current position in the GADDAG.
   */
  private void generateOn(int currentCol,
                          LinkedList<Tile> word,
                          List<Tile> rack,
                          Arc arc) {
    Tile tileOnCurrentSquare = board_.get(anchorRow_, currentCol).getTile();
    if (tileOnCurrentSquare != null) {
      // If there is a tile on the current square, then recurse using the letter
      // on that tile.
      Arc newArc = gaddag_.getArc(arc.getDestination(),
                                  tileOnCurrentSquare.getLetter());
      evaluateOn(currentCol, tileOnCurrentSquare, word, rack, newArc, arc);
    } else if (!rack.isEmpty()) {
      // Otherwise, if we still have tiles in the rack, recurse using each of
      // the rack tiles.

      // Find the cross-set on the current square.
      Set<Letter> crossSet = dataManager_.getSquareData(anchorRow_, currentCol)
                                         .getCrossSet().getAcrossSet();

      // We only need to recurse once for each unique rack tile, so dump the
      // rack into a |Set|.
      Set<Tile> uniqueRackTiles = new TreeSet<Tile>(rack);
      for (Tile rackTile : uniqueRackTiles) {
        // Remove the current tile from the rack in preparation to recurse.
        List<Tile> newRack = removeTileFromRack(rack, rackTile);

        if (!(rackTile instanceof Blank)) {
          // Recurse using this rack tile.
          Arc newArc = gaddag_.getArc(arc.getDestination(),
                                      rackTile.getLetter());
          evaluateOn(currentCol, rackTile, word, newRack, newArc, arc);
        } else if (crossSet.contains(rackTile.getLetter())) {
          // If the tile is blank, then we need to try every possible letter.
          // Luckily, the set of possible letters is simply the cross-set of the
          // current square.
          for (Letter allowedBlankLetter : crossSet) {
            Tile filledBlank = new Blank(allowedBlankLetter);
            Arc newArc = gaddag_.getArc(arc.getDestination(),
                                        allowedBlankLetter);
            evaluateOn(currentCol, filledBlank, word, newRack, newArc, arc);
          }
        }
      }
    }
  }

  /**
   * Returns a rack that contains every tile in the specified rack
   * <em>minus</em> the specified tile.
   * 
   * @param rack The rack from which to subtract the tile.
   * @param tileToBeRemoved The tile to be subtracted.
   */
  private List<Tile> removeTileFromRack(List<Tile> rack, Tile tileToBeRemoved) {
    // Simply move the specified element to the end of the list and return a
    // sub-list.
    rack.remove(tileToBeRemoved);
    rack.add(tileToBeRemoved);
    return rack.subList(0, rack.size() - 1);
  }

  /**
   * This method is one of a pair of recursive coroutines (the other being
   * {@link #generateOn}) that perform move generation. For details of their
   * operation, see the 1994 paper by Gordon. (Note: In the Gordon paper, this
   * method is referred to as {@code GoOn}).
   * 
   * @param currentCol The generation algorithm's current column.
   * @param letter The letter to evaluate.
   * @param word The word formed by the algorithm, up to this point.
   * @param rack The rack of remaining tiles.
   * @param newArc The algorithm's current position in the GADDAG.
   * @param oldArc The algorithm's previous position in the GADDAG.
   */
  private void evaluateOn(int currentCol,
                          Tile letter,
                          LinkedList<Tile> word,
                          List<Tile> rack,
                          Arc newArc,
                          Arc oldArc) {
    if (currentCol <= anchorCol_) {
      // If we're left of the anchor square, we are generating a prefix.
      word.addFirst(letter);

      // Determine whether there is no tile immediately left of the current
      // position.
      boolean noTileToTheLeft = (currentCol == 0 || board_.get(anchorRow_,
                                                               currentCol - 1)
                                                          .getTile() == null);

      // If we have formed a valid word, record it.
      if (oldArc.hasLetter(letter.getLetter()) && noTileToTheLeft)
        recordMove(currentCol, word);

      if (newArc != null) {
        // If there's additional room to the left, keep generating prefixes.
        if (currentCol > 0)
          generateOn(currentCol - 1, word, rack, newArc);

        // Switch to suffix generation.
        Arc delimiterArc = newArc.getDestination().getArc(GADDAG.DELIMITER);

        // Generate to the right of the anchor square if possible.
        if (delimiterArc != null && noTileToTheLeft
            && anchorCol_ < board_.size() - 1)
          generateOn(anchorCol_ + 1, word, rack, delimiterArc);
      }

      // Remove our addition to |word|.
      word.removeFirst();
    } else {
      // If we're right of the anchor square, we are generating a suffix.
      word.addLast(letter);

      // Determine whether there is no tile immediately right of the current
      // position.
      boolean noTileToTheRight = (currentCol == board_.size() - 1 || board_.get(anchorRow_,
                                                                                currentCol + 1)
                                                                           .getTile() == null);
      // If we have formed a valid word, record it.
      if (oldArc.hasLetter(letter.getLetter()) && noTileToTheRight)
        recordMove(currentCol - word.size() + 1, word);

      // If there's additional room to the right, keep generating suffixes.
      if (newArc != null && currentCol < board_.size() - 1)
        generateOn(currentCol + 1, word, rack, newArc);

      // Remove our addition to |word|.
      word.removeLast();
    }
  }

  /**
   * Adds a new move to the list of moves.
   * 
   * @param wordStartCol The starting column of this move
   * @param word The word formed by this move.
   */
  private void recordMove(int wordStartCol, List<Tile> word) {
    Move newMove = new Move(transposed_, anchorRow_);
    int letterCol = wordStartCol;
    for (Tile wordTile : word) {
      // Only add the tile to the move structure if it isn't already on the
      // board.
      if (board_.get(anchorRow_, letterCol).getTile() == null)
        newMove.addTile(wordTile, letterCol);
    }
    moves_.add(newMove);
  }
}
