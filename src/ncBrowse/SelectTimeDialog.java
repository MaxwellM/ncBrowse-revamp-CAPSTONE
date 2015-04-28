//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ncBrowse;

import ncBrowse.sgt.swing.util.JSlider2Date;
//import gov.noaa.pmel.swing.JSlider2Date;
import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectTimeDialog extends JDialog {
  public static int APPROVE_OPTION = 1;
  public static int CANCEL_OPTION = 2;
  private int status_;
  BorderLayout borderLayout1;
  FlowLayout flowLayout1;
  boolean frameSizeAdjusted;
  JPanel buttonPanel;
  JButton okButton;
  JButton cancelButton;
  EtchedBorder etchedBorder1;
  JSlider2Date hsDate;

  public SelectTimeDialog(Dialog var1) {
    super(var1);
    this.status_ = CANCEL_OPTION;
    this.borderLayout1 = new BorderLayout(0, 0);
    this.flowLayout1 = new FlowLayout(1, 25, 5);
    this.frameSizeAdjusted = false;
    this.buttonPanel = new JPanel();
    this.okButton = new JButton();
    this.cancelButton = new JButton();
    this.etchedBorder1 = new EtchedBorder();
    this.hsDate = new JSlider2Date();

    try {
      this.jbInit();
      this.pack();
    } catch (Exception var3) {
      var3.printStackTrace();
    }

  }

  public SelectTimeDialog(Frame var1) {
    super(var1);
    this.status_ = CANCEL_OPTION;
    this.borderLayout1 = new BorderLayout(0, 0);
    this.flowLayout1 = new FlowLayout(1, 25, 5);
    this.frameSizeAdjusted = false;
    this.buttonPanel = new JPanel();
    this.okButton = new JButton();
    this.cancelButton = new JButton();
    this.etchedBorder1 = new EtchedBorder();
    this.hsDate = new JSlider2Date();

    try {
      this.jbInit();
      this.pack();
    } catch (Exception var3) {
      var3.printStackTrace();
    }

  }

  private void jbInit() throws Exception {
    this.setResizable(false);
    this.setTitle("Select Time Coordinate");
    this.getContentPane().setLayout(this.borderLayout1);
    this.setSize(400, 220);
    this.setVisible(false);
    this.buttonPanel.setBorder(this.etchedBorder1);
    this.buttonPanel.setLayout(this.flowLayout1);
    this.getContentPane().add(this.buttonPanel, "South");
    this.buttonPanel.setBounds(0, 181, 400, 39);
    this.okButton.setText("OK");
    this.okButton.setActionCommand("OK");
    this.buttonPanel.add(this.okButton);
    this.okButton.setBounds(125, 7, 51, 25);
    this.cancelButton.setText("Cancel");
    this.cancelButton.setActionCommand("Cancel");
    this.buttonPanel.add(this.cancelButton);
    this.cancelButton.setBounds(201, 7, 73, 25);
    this.getContentPane().add(this.hsDate, "Center");
    this.hsDate.setBounds(0, 0, 400, 181);
    SelectTimeDialog.SymAction var1 = new SelectTimeDialog.SymAction();
    this.cancelButton.addActionListener(var1);
    this.okButton.addActionListener(var1);
  }

  public SelectTimeDialog() {
    this((Frame)null);
  }

  public SelectTimeDialog(String var1) {
    this();
    this.setTitle(var1);
  }

  public void setVisible(boolean var1) {
    if(var1) {
      this.setLocation(50, 50);
    }

    super.setVisible(var1);
  }

  public int showDialog(int var1, int var2) {
    this.setLocation(var1, var2);
    super.setVisible(true);
    return this.status_;
  }

  public static void main(String[] var0) {
    (new SelectTimeDialog()).setVisible(true);
  }

  public void addNotify() {
    Dimension var1 = this.getSize();
    super.addNotify();
    if(!this.frameSizeAdjusted) {
      this.frameSizeAdjusted = true;
      Insets var2 = this.getInsets();
      this.setSize(var2.left + var2.right + var1.width, var2.top + var2.bottom + var1.height);
    }
  }

  void cancelButton_actionPerformed(ActionEvent var1) {
    this.status_ = CANCEL_OPTION;
    this.setVisible(false);
  }

  void okButton_actionPerformed(ActionEvent var1) {
    this.status_ = APPROVE_OPTION;
    this.setVisible(false);
  }

  public void setRange(GeoDate var1, GeoDate var2) {
    this.hsDate.setRange(var1, var2);
  }

  public void setRange(long var1, long var3) {
    this.hsDate.setRange(new GeoDate(var1), new GeoDate(var3));
  }

  public void setStartValue(GeoDate var1) {
    this.hsDate.setStartValue(var1);
  }

  public void setEndValue(GeoDate var1) {
    this.hsDate.setEndValue(var1);
  }

  public GeoDate getStartValue() {
    return this.hsDate.getStartValue();
  }

  public GeoDate getEndValue() {
    return this.hsDate.getEndValue();
  }

  public void setTwoHandles(boolean var1) {
    this.hsDate.setTwoHandles(var1);
  }

  class SymAction implements ActionListener {
    SymAction() {
    }

    public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if(var2 == SelectTimeDialog.this.cancelButton) {
        SelectTimeDialog.this.cancelButton_actionPerformed(var1);
      } else if(var2 == SelectTimeDialog.this.okButton) {
        SelectTimeDialog.this.okButton_actionPerformed(var1);
      }

    }
  }
}
