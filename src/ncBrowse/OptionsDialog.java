/**
 *  $Id: OptionsDialog.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Title:        netCDF File Browser
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * @author Donald Denbo
 * @version 1.0
 */

public class OptionsDialog extends JDialog {
  Browser parent_;
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();

  public OptionsDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    parent_ = (Browser)frame;
    try {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public OptionsDialog() {
    this(null, "", false);
  }

  public OptionsDialog(Frame frame) {
    this(frame, null, false);
  }

  void jbInit() {
    panel1.setLayout(borderLayout1);
    tFormatField.setColumns(25);
    getContentPane().add(panel1);

    setResizable(false);
    setTitle("ncBrowse Options");
//    setSize(369,152);
    setVisible(false);
    buttonPanel.setBorder(etchedBorder1);
    buttonPanel.setLayout(flowLayout1);
    panel1.add(buttonPanel,"South");
    buttonPanel.setBounds(0,113,369,39);
    okButton.setText("OK");
    okButton.setActionCommand("OK");
    buttonPanel.add(okButton);
    okButton.setBounds(110,7,51,25);
    cancelButton.setText("Cancel");
    cancelButton.setActionCommand("Cancel");
    buttonPanel.add(cancelButton);
    cancelButton.setBounds(186,7,73,25);
    mainPanel.setLayout(gridBagLayout1);
    panel1.add(mainPanel,"Center");
    mainPanel.setBounds(0,0,369,113);
    showAllVariables.setToolTipText("Show dimension and non-dimension variables");
    showAllVariables.setText("Show all variables");
    showAllVariables.setActionCommand("Show all variables");
    mainPanel.add(showAllVariables, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 20, 0), 0, 0));
    showAllVariables.setBounds(120,63,128,23);
    JLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel1.setText("Time units format:");
    mainPanel.add(JLabel1, new GridBagConstraints(0,0,1,1,0.0,0.0,
                 GridBagConstraints.CENTER,GridBagConstraints.NONE,
                new Insets(20,10,0,5),0,0));
    JLabel1.setBounds(10,31,103,15);
    mainPanel.add(tFormatField, new GridBagConstraints(1,0,1,1,1.0,1.0,
                 GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,
                 new Insets(20,0,0,0),0,0));
    tFormatField.setBounds(118,29,177,19);
    tFormatHelp.setText("help");
    mainPanel.add(tFormatHelp, new GridBagConstraints(2,0,1,1,0.0,0.0,
                 GridBagConstraints.EAST,GridBagConstraints.NONE,
                 new Insets(20,5,0,10),0,0));
    tFormatHelp.setBounds(300,26,59,25);

    SymAction lSymAction = new SymAction();
    cancelButton.addActionListener(lSymAction);
    okButton.addActionListener(lSymAction);
    tFormatHelp.addActionListener(lSymAction);


    tFormatHelp.setMargin(new Insets(0,2,0,2));

  }
  public void setVisible(boolean b) {
    if (b)
      setLocation(50, 50);
    super.setVisible(b);
    showAllVariables.setSelected(parent_.isShowAllVariables());
    tFormatField.setText(parent_.getTimeFormat());
  }

  JPanel buttonPanel = new JPanel();
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();
  JPanel mainPanel = new JPanel();
  JCheckBox showAllVariables = new JCheckBox();
  JLabel JLabel1 = new JLabel();
  JTextField tFormatField = new JTextField();
  JButton tFormatHelp = new JButton();
  EtchedBorder etchedBorder1 = new EtchedBorder();
  FlowLayout flowLayout1 = new FlowLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  class SymAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == cancelButton)
        cancelButton_actionPerformed(event);
      else if (object == okButton)
        okButton_actionPerformed(event);
      else if (object == tFormatHelp)
        tFormatHelp_actionPerformed(event);
    }
  }

  void cancelButton_actionPerformed(ActionEvent event) {
    try {
      // OptionsDialog Hide the OptionsDialog
      this.setVisible(false);
    } catch (Exception ignored) {
    }
  }

  void okButton_actionPerformed(ActionEvent event) {
    parent_.setShowAllVariables(showAllVariables.isSelected());
    parent_.setTimeFormat(tFormatField.getText());
    try {
      // OptionsDialog Hide the OptionsDialog
      this.setVisible(false);
    } catch (Exception ignored) {
    }
  }

  void tFormatHelp_actionPerformed(ActionEvent event) {
    try {
      {
        TimeHelpDialog TimeHelpDialog1 = new TimeHelpDialog(((Frame)getParent()));
        //      TimeHelpDialog1.setModal(true);
        Point pt = getLocationOnScreen();
        Dimension sz = getSize();
        int x = pt.x + sz.width + 20;
        int y = 50;
        TimeHelpDialog1.setLocation(x, y);
        TimeHelpDialog1.setVisible(true);
      }
    } catch (Exception ignored) {
    }
  }
}
