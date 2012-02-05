package scrabbletool.ui;

import java.awt.*;
import javax.swing.*;
import scrabbletool.game.board.Board;

public class BoardPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public BoardPanel(Board board) {
    super(new GridLayout(board.size(), board.size()));
    for (int i = 0; i < board.size(); i++) {
      for (int j = 0; j < board.size(); j++)
        super.add(new SquarePanel(board.get(i, j)));
    }
  }
}
