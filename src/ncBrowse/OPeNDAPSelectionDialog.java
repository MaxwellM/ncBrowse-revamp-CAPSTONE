package ncBrowse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

/**
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * @author Donald Denbo
 * @version 1.0
 */

public class OPeNDAPSelectionDialog extends JDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonPanel = new JPanel();
  JPanel navigationPanel = new JPanel();
  JButton acceptButton = new JButton();
  JButton cancelButton = new JButton();
  JPanel fileURLPanel = new JPanel();
  JLabel jLabel2 = new JLabel();
  JTextField fileURLField = new JTextField();
  private String OPeNDAPFile_ = null;
  private boolean hasFile = false;

  private String filePath = null;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel jLabel3 = new JLabel();
  private JComboBox fileCB = new JComboBox();
  private ComboBoxModel fileCBModel = null;

  public OPeNDAPSelectionDialog(Frame frame, String file, String title, boolean modal) {
    super(frame, title, modal);
    OPeNDAPFile_ = file;
    hasFile = OPeNDAPFile_ != null;
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public OPeNDAPSelectionDialog(String file) {
    this(null, file, "", false);
  }

  public OPeNDAPSelectionDialog() {
    this(null, null, "", false);
  }
  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    acceptButton.setText("Accept");
    acceptButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        acceptButton_actionPerformed(e);
      }
    });
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    navigationPanel.setLayout(gridBagLayout1);
    jLabel2.setText("Data File URL:");
    fileURLField.setColumns(40);
    this.setTitle("OPeNDAP Connection Dialog");
    fileURLPanel.setLayout(gridBagLayout2);
    jLabel1.setFont(new Font("Dialog", 0, 14));
    jLabel1.setText("Enter OPeNDAP URL");
    jLabel3.setText("Select:");
    fileCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fileCB_actionPerformed(e);
      }
    });
    getContentPane().add(panel1);
    panel1.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(acceptButton, null);
    buttonPanel.add(cancelButton, null);
    panel1.add(navigationPanel, BorderLayout.CENTER);
    navigationPanel.add(fileURLPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    fileURLPanel.add(jLabel2,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 0), 0, 0));
    fileURLPanel.add(fileURLField,   new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 15), 0, 0));
    fileURLPanel.add(jLabel3,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 15, 5, 0), 0, 0));
    fileURLPanel.add(fileCB,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 15), 0, 0));
    navigationPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 10, 10, 10), 0, 0));
    if(hasFile) {
      BufferedReader br = null;
      try {
        br = new BufferedReader(new FileReader(OPeNDAPFile_));
      } catch (FileNotFoundException  fnfe) {
        jLabel3.setVisible(false);
        fileCB.setVisible(false);
        hasFile = false;
        return;
      }
      Vector v = new Vector();
      v.add("");
      String str = null;
      do {
        str = br.readLine();
        if(str != null) v.add(str);
      } while (str != null);
      fileCBModel = new DefaultComboBoxModel(v);
      fileCB.setModel(fileCBModel);
    } else {
      jLabel3.setVisible(false);
      fileCB.setVisible(false);
    }
  }

  public String getOPeNDAPPath() {
    return filePath;
  }

  void acceptButton_actionPerformed(ActionEvent e) {
    filePath = fileURLField.getText();
    setVisible(false);
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    filePath = null;
    setVisible(false);
  }

  void fileCB_actionPerformed(ActionEvent e) {
    String selected = (String)fileCB.getSelectedItem();
    fileURLField.setText(selected);
  }
}
