package scrabbletool.ui;

import java.awt.*;
import javax.swing.*;
import scrabbletool.game.board.Square;

public class SquarePanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final Dimension PREFERRED_SIZE = new Dimension(40, 40);
  private Square _square;
  private JTextField _text;

  public SquarePanel(Square square) {
    super(new GridLayout(1, 1));
    super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    super.setPreferredSize(PREFERRED_SIZE);
    _square = square;
    _text = new JTextField(5);
    _text.setHorizontalAlignment(JTextField.CENTER);
    super.add(_text);
    update();
  }

  public void update() {
    String newLabelText = "";
    Color newColor = Color.BLACK;
    if (_square.getTile() != null)
      newLabelText = _square.getTile().getLetter().toString();
    else if (_square.getModifier() != null) {
      newLabelText = "TMP";
      newColor = Color.GRAY;
    }

    _text.setText(newLabelText);
    _text.setForeground(newColor);
  }
}
