/**
 *  $Id: TimeHelpDialog.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 * Provides help for setting formats for parsing and 
 * printing time/date strings.
 *
 * @author Donald Denbo
 * @version $Revision: 1.5 $, $Date: 2000/12/08 00:46:42 $
 */
public class TimeHelpDialog extends javax.swing.JDialog {
  BorderLayout borderLayout1 = new BorderLayout(0,0);
  FlowLayout flowLayout1 = new FlowLayout(FlowLayout.CENTER,5,5);

  public TimeHelpDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public TimeHelpDialog(Frame parent) {
    this(parent, null, false);
  }
  
  void jbInit() throws Exception {
                
    setTitle("Time Format Syntax");
    getContentPane().setLayout(borderLayout1);
    setSize(678,644);
    setVisible(false);
    buttonPanel.setBorder(etchedBorder1);
    buttonPanel.setLayout(flowLayout1);
    getContentPane().add(buttonPanel, "South");
    buttonPanel.setBounds(0,605,678,39);
    closeButton.setText("Close");
    closeButton.setActionCommand("Close");
    buttonPanel.add(closeButton);
    closeButton.setBounds(305,7,67,25);
    JScrollPane1.setOpaque(true);
    getContentPane().add(JScrollPane1, "Center");
    JScrollPane1.setBounds(0,0,678,605);
    helpText.setEditable(false);
    JScrollPane1.getViewport().add(helpText);
    helpText.setBounds(0,0,675,602);

    setHelpText();
  
    SymAction lSymAction = new SymAction();
    closeButton.addActionListener(lSymAction);
  }

  public TimeHelpDialog() {
    this((Frame)null);
  }

  public TimeHelpDialog(String sTitle) {
    this();
    setTitle(sTitle);
  }

  public void setVisible(boolean b) {
    super.setVisible(b);
  }

  static public void main(String args[]) {
    (new TimeHelpDialog()).setVisible(true);
  }

  public void addNotify() {
    // Record the size of the window prior to calling parents addNotify.
    Dimension size = getSize();

    super.addNotify();

    if (frameSizeAdjusted)
      return;
    frameSizeAdjusted = true;

    // Adjust size of frame according to the insets
    Insets insets = getInsets();
    setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height);
  }

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
  javax.swing.JButton closeButton = new javax.swing.JButton();
  javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
  javax.swing.JEditorPane helpText = new javax.swing.JEditorPane();
  EtchedBorder etchedBorder1 = new EtchedBorder();

  private void setHelpText() {
    helpText.setContentType("text/html");
    helpText.setText(
"<strong>Time Format Syntax:</strong><p>\n" +
"To specify the time format use a <em>time pattern</em> string. \n" +
"In this pattern, all ASCII letters are reserved as pattern letters, \n" +
"which are defined as the following:\n" +
"<blockquote>\n" +
"<pre>\n" +
"Symbol   Meaning                 Presentation        Example\n" +
"------   -------                 ------------        -------\n" +
"G        era designator          (Text)              AD\n" +
"y        year                    (Number)            1996\n" +
"M        month in year           (Text & Number)     July & 07\n" +
"d        day in month            (Number)            10\n" +
"h        hour in am/pm (1~12)    (Number)            12\n" +
"H        hour in day (0~23)      (Number)            0\n" +
"m        minute in hour          (Number)            30\n" +
"s        second in minute        (Number)            55\n" +
"S        millisecond             (Number)            978\n" +
"E        day in week             (Text)              Tuesday\n" +
"D        day in year             (Number)            189\n" +
"F        day of week in month    (Number)            2 (2nd Wed in July)\n" +
"w        week in year            (Number)            27\n" +
"W        week in month           (Number)            2\n" +
"a        am/pm marker            (Text)              PM\n" +
"k        hour in day (1~24)      (Number)            24\n" +
"K        hour in am/pm (0~11)    (Number)            0\n" +
"z        time zone               (Text)              Pacific Standard Time\n" +
"'        escape for text         (Delimiter)\n" +
"''       single quote            (Literal)           '\n" +
"</pre>\n" +
"</blockquote>\n" +
"The count of pattern letters determine the format.\n" +
"<p>\n" +
"<strong>(Text)</strong>: 4 or more pattern letters--use full form,\n" +
"&lt; 4--use short or abbreviated form if one exists.\n" +
"<p>\n" +
"<strong>(Number)</strong>: the minimum number of digits. Shorter\n" +
"numbers are zero-padded to this amount. Year is handled specially;\n" +
"that is, if the count of 'y' is 2, the Year will be truncated to 2 digits.\n" +
"<p>\n" +
"<strong>(Text & Number)</strong>: 3 or over, use text, otherwise use number.\n" +
"<p>\n" +
"Any characters in the pattern that are not in the ranges of ['a'..'z']\n" +
"and ['A'..'Z'] will be treated as quoted text. For instance, characters\n" +
"like ':', '.', ' ', '#' and '@' will appear in the resulting time text\n" +
"even they are not embraced within single quotes.\n" +
"<p>\n");
  }

  class SymAction implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent event) {
      Object object = event.getSource();
      if (object == closeButton)
        closeButton_actionPerformed(event);
    }
  }
  
  void closeButton_actionPerformed(java.awt.event.ActionEvent event) {
    try {
      this.setVisible(false);
      this.dispose();
    } catch (java.lang.Exception e) {
    }
  }
}
