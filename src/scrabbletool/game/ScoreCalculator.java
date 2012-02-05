package scrabbletool.game;

import java.util.Map;
import scrabbletool.game.board.Blank;
import scrabbletool.game.board.Board;
import scrabbletool.game.board.BoardUtilities;
import scrabbletool.game.board.Modifier;
import scrabbletool.game.board.Tile;

public class ScoreCalculator {

  public static int calculateScore(Board board, Move move) {
    int score = 0;
    int multiplier = 1;

    Board moveAlignedBoard = move.isAcross() ? board : board.transpose();
    Board moveOrthoBoard = moveAlignedBoard.transpose();

    int row = move.getRowOrCol();
    int col = 0;
    for (Map.Entry<Integer, Tile> mapEntry : move.getTileMap().entrySet()) {
      col = mapEntry.getKey();
      int crossScore = calculateWordScore(moveOrthoBoard, col, row);
      score += crossScore;

      Modifier modifier = moveAlignedBoard.get(row, col).getModifier();
      if (modifier != null) {
        Modifier.Type type = modifier.getType();
        int magnitude = modifier.getMagnitude();
        if (type == Modifier.Type.LETTER_SCORE) {
          Tile tile = moveAlignedBoard.get(row, col).getTile();
          score += getLetterScore(tile) * (magnitude - 1);
        } else if (type == Modifier.Type.WORD_SCORE) {
          score += crossScore * (magnitude - 1);
          multiplier *= magnitude;
        }
      }
    }

    int wordScore = calculateWordScore(moveAlignedBoard, row, col);
    score += wordScore * multiplier;
    return score;
  }

  private static int calculateWordScore(Board board,
                                        int row,
                                        int wordInternalPos) {
    int wordStart = BoardUtilities.findWordBoundary(board, row,
                                                    wordInternalPos,
                                                    BoardUtilities.LEFT);
    int wordEnd = BoardUtilities.findWordBoundary(board, row, wordInternalPos,
                                                  BoardUtilities.RIGHT);

    if (wordStart == wordEnd)
      return 0;

    int wordScore = 0;
    int col = wordStart;
    while (col <= wordEnd)
      wordScore += getLetterScore(board.get(row, col).getTile());
    return wordScore;
  }

  private static int getLetterScore(Tile tile) {
    if (tile instanceof Blank)
      return 0;
    return tile.getLetter().getValue();
  }
}
