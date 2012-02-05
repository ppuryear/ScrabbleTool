package scrabbletool.ui;

import java.awt.*;
import javax.swing.JPanel;
import scrabbletool.game.Game;

public class MainWindowPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final GridBagConstraints BOARD_PANEL_CONSTRAINTS;
  static {
    BOARD_PANEL_CONSTRAINTS = new GridBagConstraints();
    BOARD_PANEL_CONSTRAINTS.gridx = 0;
    BOARD_PANEL_CONSTRAINTS.gridy = 0;
    BOARD_PANEL_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;
    BOARD_PANEL_CONSTRAINTS.weightx = 0.7;
    BOARD_PANEL_CONSTRAINTS.weighty = 0.8;
  }

  private static final GridBagConstraints MOVES_PANEL_CONSTRAINTS;
  static {
    MOVES_PANEL_CONSTRAINTS = new GridBagConstraints();
    MOVES_PANEL_CONSTRAINTS.gridx = 1;
    MOVES_PANEL_CONSTRAINTS.gridy = 0;
    MOVES_PANEL_CONSTRAINTS.fill = GridBagConstraints.BOTH;
    MOVES_PANEL_CONSTRAINTS.weightx = 0.3;
    MOVES_PANEL_CONSTRAINTS.weighty = 0.8;
  }

  private static final GridBagConstraints CONTROL_PANEL_CONSTRAINTS;
  static {
    CONTROL_PANEL_CONSTRAINTS = new GridBagConstraints();
    CONTROL_PANEL_CONSTRAINTS.gridx = 0;
    CONTROL_PANEL_CONSTRAINTS.gridy = 1;
    CONTROL_PANEL_CONSTRAINTS.gridwidth = 2;
    CONTROL_PANEL_CONSTRAINTS.gridheight = 1;
    CONTROL_PANEL_CONSTRAINTS.fill = GridBagConstraints.BOTH;
    CONTROL_PANEL_CONSTRAINTS.weightx = 1.0;
    CONTROL_PANEL_CONSTRAINTS.weighty = 0.2;
  }

  private BoardPanel _boardPanel;
  private MovesPanel _movesPanel;
  private GameControlPanel _controlPanel;
  private Game _game;

  public MainWindowPanel(Game game) {
    super(new GridBagLayout());
    _game = game;
    _boardPanel = new BoardPanel(game.getBoard());
    super.add(_boardPanel, BOARD_PANEL_CONSTRAINTS);
    _movesPanel = new MovesPanel();
    super.add(_movesPanel, MOVES_PANEL_CONSTRAINTS);
    _controlPanel = new GameControlPanel();
    super.add(_controlPanel, CONTROL_PANEL_CONSTRAINTS);
  }
}
