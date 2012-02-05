package scrabbletool.ui;

import javax.swing.*;
import scrabbletool.game.Game;
import scrabbletool.ui.controller.*;

public class MenuBar extends JMenuBar {
  private static final long serialVersionUID = 1L;

  private class ExitMenuItem extends JMenuItem {
    private static final long serialVersionUID = 1L;

    public ExitMenuItem() {
      super();
      super.setText("Exit");
      super.addActionListener(new ProgramExitAction());
    }
  }

  private Game _game;

  public MenuBar(Game game) {
    super();
    _game = game;
    super.add(fileMenu());
    super.add(gameMenu());
  }

  private JMenu fileMenu() {
    JMenu fileMenu = new JMenu();
    fileMenu.setText("File");
    fileMenu.add(new ExitMenuItem());
    return fileMenu;
  }

  private JMenu gameMenu() {
    JMenu gameMenu = new JMenu();
    gameMenu.setText("Game");
    gameMenu.add(newGameItem());
    return gameMenu;
  }

  private JMenuItem newGameItem() {
    JMenuItem newGameItem = new JMenuItem();
    newGameItem.setText("New Game");
    return newGameItem;
  }
}
