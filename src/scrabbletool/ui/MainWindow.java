package scrabbletool.ui;

import javax.swing.*;
import scrabbletool.game.Game;

public class MainWindow extends JFrame {
  private static final long serialVersionUID = 1L;

  public MainWindow(Game game) {
    super();
    super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    super.setTitle("Scrabble Tool");
    super.setJMenuBar(new MenuBar(game));
    super.setContentPane(new MainWindowPanel(game));
    super.pack();
  }
}
