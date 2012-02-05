package scrabbletool.ui;

import javax.swing.*;
import scrabbletool.game.Game;

public class UI {
  public static void createUI(final Game game) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      // getSystemLookAndFeelClassName() should always return a valid LAF.
      throw new RuntimeException(e);
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainWindow window = new MainWindow(game);
        window.setVisible(true);
      }
    });
  }
}
