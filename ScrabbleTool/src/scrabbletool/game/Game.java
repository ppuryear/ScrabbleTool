package scrabbletool.game;

import scrabbletool.gaddag.GADDAG;
import scrabbletool.game.board.Board;
import scrabbletool.game.movegen.MoveGenerator;

/**
 * A single Scrabble game.
 * 
 * @author Philip Puryear
 */
public class Game {
  private Board board_;
  private Alphabet alphabet_;
  private GADDAG gaddag_;
  private MoveGenerator moveGen_;

  /**
   * Creates a new game with the given parameters.
   * 
   * @param board The game board.
   * @param alphabet The alphabet to use.
   * @param gaddag The dictionary structure.
   */
  Game(Board board, Alphabet alphabet, GADDAG gaddag) {
    board_ = board;
    alphabet_ = alphabet;
    gaddag_ = gaddag;
  }

  /**
   * Starts a new game.
   */
  public void newGame() {
    moveGen_ = new MoveGenerator(this);
  }

  /**
   * Returns the {@link Board} used by this game.
   */
  public Board getBoard() {
    return board_;
  }

  /**
   * Returns the {@link Alphabet} used by this game.
   */
  public Alphabet getAlphabet() {
    return alphabet_;
  }

  /**
   * Returns the {@link GADDAG} used by this game.
   */
  public GADDAG getGADDAG() {
    return gaddag_;
  }
}
