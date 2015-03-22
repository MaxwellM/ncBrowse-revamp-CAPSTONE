/**
 *  $Id: VariableListDialog.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import ucar.nc2.Variable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

//  import ucar.netcdf.VariableIterator;

/**
 * Creates a popup list of netCDF variables.
 *
 * @author Donald Denbo
 * @version $Revision: 1.12 $, $Date: 2001/06/01 19:00:02 $
 */
public class VariableListDialog extends JDialog {
  NcFile ncFile;
  Browser parent_;
  String buttonText;
  int processType;
  BorderLayout borderLayout1 = new BorderLayout(0,0);
  FlowLayout flowLayout1 = new FlowLayout(FlowLayout.CENTER, 5,5);

  public VariableListDialog(Browser parent) {
    super((Frame)parent, null, false);
    parent_ = parent;
    try {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    setResizable(false);
    setTitle("Variable Selection");
    getContentPane().setLayout(borderLayout1);
    setSize(131,292);
    setVisible(false);
    JScrollPane1.setToolTipText("Select a Variable");
    getContentPane().add(JScrollPane1, "Center");
    JScrollPane1.setBounds(0,0,131,257);
    VariableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    VariableList.setToolTipText("Select a Variable");
    JScrollPane1.getViewport().add(VariableList);
    VariableList.setBounds(0,0,128,254);
    buttonPanel.setLayout(flowLayout1);
    getContentPane().add(buttonPanel, "South");
    buttonPanel.setBounds(0,257,131,35);
    closeButton.setText("Close");
    closeButton.setActionCommand("Close");
    buttonPanel.add(closeButton);
    closeButton.setBounds(25,5,81,25);

    SymListSelection lSymListSelection = new SymListSelection();
    VariableList.addListSelectionListener(lSymListSelection);
    SymAction lSymAction = new SymAction();
    closeButton.addActionListener(lSymAction);
  }

  public VariableListDialog() {
    this((Browser)null);
  }

  public VariableListDialog(String sTitle) {
    this();
    setTitle(sTitle);
  }

  public void setVisible(boolean b) {
    super.setVisible(b);
  }

  public void setNcFile(NcFile file) {
    ncFile = file;
    Iterator vi = ncFile.getNonDimensionVariables();
    Vector listdata = new Vector();
    while(vi.hasNext()) {
      listdata.addElement(((Variable)vi.next()).getName());
    }
    VariableList.setListData(listdata);
  }

  public void setAction(String text, int vpt) {
    buttonText = text;
    processType = vpt;
  }

  static public void main(String[] args) {
    (new VariableListDialog()).setVisible(true);
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

  JScrollPane JScrollPane1 = new JScrollPane();
  JList VariableList = new JList();
  JPanel buttonPanel = new JPanel();
  JButton closeButton = new JButton();

  /** @link dependency
   * @stereotype instantiate*/
  /*#DomainSelector lnkDomainSelector;*/

  class SymListSelection implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent event) {
      Object object = event.getSource();
      if (object == VariableList)
	VariableList_valueChanged(event);
    }
  }

  void VariableList_valueChanged(ListSelectionEvent event) {
    JList jl = (JList)event.getSource();
    if(jl.isSelectionEmpty() || event.getValueIsAdjusting()) return;
    String varName = (String)jl.getSelectedValue();
    if(Debug.DEBUG) System.out.println("Variable " + varName + " selected");
    this.setVisible(false);

    Point loc = parent_.getLocationOnScreen();
    Dimension bs = parent_.getSize();
    DomainSelector domSel = new DomainSelector();
    domSel.setParent(parent_);
    domSel.setActionButton(buttonText, processType);
    domSel.setVariable(ncFile, varName);
    Dimension ds = domSel.getSize();
    int x = loc.x + bs.width/2 - ds.width/2;
    int y = loc.y + bs.height + 10;
    domSel.setPosition(x, y);
    new Thread(domSel).start();
    //    domSel.setVisible(true, x, y);

  }

  class SymAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == closeButton)
	closeButton_actionPerformed(event);
    }
  }

  void closeButton_actionPerformed(ActionEvent event) {
    this.setVisible(false);
    this.dispose();
  }
}
