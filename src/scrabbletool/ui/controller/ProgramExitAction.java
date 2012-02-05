package scrabbletool.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgramExitAction implements ActionListener {
  @Override
  public void actionPerformed(ActionEvent e) {
    System.exit(0);
  }
}
