/**
 *  $Id: TreeView.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

//  import ucar.netcdf.Variable;
//  import ucar.netcdf.Attribute;
//  import ucar.netcdf.DimensionSet;
//  import ucar.netcdf.AttributeSet;
//  import ucar.netcdf.VariableIterator;
//  import ucar.netcdf.AttributeIterator;
//  import ucar.netcdf.DimensionIterator;
//  import ucar.netcdf.UnlimitedDimension;

/**
 * Provides a <code>JTree</code> view of netCDF files.
 *
 * @author Donald Denbo
 * @version $Revision: 1.25 $, $Date: 2001/12/18 22:54:08 $
 */
public class TreeView extends JFrame {
  private NcFile ncFile_;
  private JTree treeView_;
  private DefaultMutableTreeNode root_;
  private DefaultMutableTreeNode dimRoot_;
  private DefaultMutableTreeNode attrRoot_;
  private DefaultMutableTreeNode varRoot_;
  private boolean showAllVariables_ = false;
  private Browser parent_ = null;
  BorderLayout borderLayout1 = new BorderLayout(0,0);
  FlowLayout flowLayout1 = new FlowLayout(FlowLayout.CENTER, 5,5);

  public TreeView() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() {

    getContentPane().setLayout(borderLayout1);
    getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
    setSize(414,459);
    setVisible(false);
    buttonPanel.setLayout(flowLayout1);
    getContentPane().add(buttonPanel, "South");
    buttonPanel.setBounds(0,424,414,35);
    expandButton.setText("Expand All");
    expandButton.setActionCommand("Expand All");
    buttonPanel.add(expandButton);
    expandButton.setBounds(71,5,93,25);
    collapseButton.setText("Collapse All");
    collapseButton.setActionCommand("Collapse All");
    buttonPanel.add(collapseButton);
    collapseButton.setBounds(169,5,101,25);
    closeButton.setText("Close");
    closeButton.setActionCommand("Close");
    buttonPanel.add(closeButton);
    closeButton.setBounds(275,5,67,25);
    buttonPanel.add(closeButton);
    getContentPane().add(treeScrollPane, "Center");
    treeScrollPane.setBounds(0,0,414,424);
    //$$ menuBar.move(0,456);
    setTitle("A Simple Frame");

    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    SymAction lSymAction = new SymAction();
    closeButton.addActionListener(lSymAction);
    expandButton.addActionListener(lSymAction);
    collapseButton.addActionListener(lSymAction);

  }

  public TreeView(String title) {
    this();
    setTitle(title);
  }
  public void setVisible(boolean b) {
    //      if(b) {
    //        setLocation(50, 50);
    //      }
    super.setVisible(b);
  }

  public void setBrowser(Browser browser) {
    parent_ = browser;
  }

  public void setShowAllVariables(boolean show) {
    showAllVariables_ = show;
  }

  public JTree makeTree() {
    TreeModel tm = getTreeModel();
    treeView_ = new JTree(tm);
    MouseListener ml = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          int selRow = treeView_.getRowForLocation(e.getX(), e.getY());
          TreePath selPath = treeView_.getPathForLocation(e.getX(), e.getY());
          if(selRow != -1) {
            if((e.getClickCount() == 2) ||
               ((e.getModifiers()&InputEvent.BUTTON3_MASK) != 0)) {
              doubleClick(selRow, selPath);
            }
          }
        }
      };
    treeView_.addMouseListener(ml);
    return treeView_;
  }

  void doubleClick(int selRow, TreePath selPath) {
    if(Debug.DEBUG) System.out.println("row " + selRow + " selected");
    Object[] objs = selPath.getPath();
    Object thing =
      ((DefaultMutableTreeNode)objs[objs.length-1]).getUserObject();
    if(thing instanceof VariableNode) {
      parent_.openDomainSelector(((VariableNode)thing).getName());
    }
  }

  public TreeModel getTreeModel() {
    //    if(ncFile_.isFile()) {
      root_ = new DefaultMutableTreeNode(ncFile_.getPathName());
//      } else {
//        root_ = new DefaultMutableTreeNode(ncFile_.getURL().toExternalForm());
//      }

    root_.add(getDimensionNodes());
    root_.add(getAttributeNodes());
    root_.add(getVariableNodes());

    return new DefaultTreeModel(root_);
  }

  public MutableTreeNode getDimensionNodes() {
    Attribute attr = null;
//      AttributeSet attrSet;
//      DimensionSet dimSet = ncFile_.getDimensions();
//      DimensionIterator di = dimSet.iterator();
    Iterator di = ncFile_.getDimensions().iterator();
    Iterator ai;
    dimRoot_ = new DefaultMutableTreeNode("Dimensions");

    while(di.hasNext()) {
      ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
//        Variable dimVar = ncFile_.get(dim.getName());
      Variable dimVar = ncFile_.findVariable(dim.getName());
//      Variable dimVar = dim.getCoordinateVariable();
      DefaultMutableTreeNode dimNode;
      if(dim.isUnlimited()) {
        dimNode = new DefaultMutableTreeNode(dim.getName() +
                                             " = UNLIMITED (" +
                                             dim.getLength() +
                                             " currently)");
      } else {
        dimNode = new DefaultMutableTreeNode(dim.getName() +
                                             " = " +
                                             dim.getLength());
      }
      if(dimVar != null) {
//  	attrSet = dimVar.getAttributes();
//  	AttributeIterator ai = attrSet.iterator();
//        ai = dimVar.getAttributeIterator();
//        while(ai.hasNext()) {
        for(Attribute att: dimVar.getAttributes()) {
          dimNode.add(getAttributeNode(att));
        }
      }
      dimRoot_.add(dimNode);
    }
    return dimRoot_;
  }

  public MutableTreeNode getAttributeNode(Attribute attr) {
    char quote = '"';
    DefaultMutableTreeNode attrRoot;
    String name = attr.getName();
    if(attr.isString()) {
      attrRoot = new DefaultMutableTreeNode(name +
                                            " = " + quote +
                                            attr.getStringValue() +
                                            quote);
    } else if(attr.getLength() == 1){
      attrRoot = new DefaultMutableTreeNode(name +
                                            " = " +
                                            attr.getNumericValue().toString());
    } else {
      attrRoot = new DefaultMutableTreeNode(name);
      for(int i=0; i < attr.getLength(); i++) {
        DefaultMutableTreeNode valNode = new DefaultMutableTreeNode
          (name + "[" + i + "] = " +
           attr.getNumericValue(i).toString());
        attrRoot.add(valNode);
      }
    }
    return attrRoot;
  }

  public MutableTreeNode getAttributeNodes() {
//      AttributeSet attrSet = ncFile_.getAttributes();
//      AttributeIterator ai = attrSet.iterator();
    Iterator ai = ncFile_.getGlobalAttributes().iterator();
    attrRoot_ = new DefaultMutableTreeNode("Attributes");
    while(ai.hasNext()){
      attrRoot_.add(getAttributeNode((Attribute)ai.next()));
    }
    return attrRoot_;
  }

  public MutableTreeNode getVariableNodes() {
    VariableNode vn = null;
    String ctype;
    String vdims;
//      AttributeIterator ai;
//      DimensionIterator di;
//      VariableIterator vi;
    Iterator ai;
    Iterator di;
    Iterator vi;
    if(showAllVariables_) {
      vi = ncFile_.getVariables().iterator();
    } else {
      vi = ncFile_.getNonDimensionVariables();
    }
    varRoot_ = new DefaultMutableTreeNode("Variables");

    DefaultMutableTreeNode varNode;
    DefaultMutableTreeNode vAttrNode;
    DefaultMutableTreeNode vDimNode;

    while(vi.hasNext()) {
      Variable var = (Variable)vi.next();
//      ctype = var.getElementType().getName();
      ctype = var.getDataType().toString();
      vdims = getDimensionsAsString(var);
      vn = new VariableNode(var.getName(), ctype + " " +
                            var.getName() +
                            vdims);
      varNode = new DefaultMutableTreeNode(vn);
      vDimNode = new DefaultMutableTreeNode("Dimensions");
      varNode.add(vDimNode);
//        di = var.getDimensionIterator();
      di = var.getDimensions().iterator();
      while(di.hasNext()) {
        ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
        DefaultMutableTreeNode dimNode;
        if(dim.isUnlimited()) {
          dimNode = new DefaultMutableTreeNode(dim.getName() +
                                               " = UNLIMITED (" +
                                               dim.getLength() +
                                               " currently)");
        } else {
          dimNode = new DefaultMutableTreeNode(dim.getName() +
                                               " = " +
                                               dim.getLength());
        }
        vDimNode.add(dimNode);
      }
      vAttrNode = new DefaultMutableTreeNode("Attributes");
      varNode.add(vAttrNode);
//      ai = var.getAttributeIterator();
//      while(ai.hasNext()) {
      for(Attribute att: var.getAttributes()) {
        vAttrNode.add(getAttributeNode(att));
      }
      varRoot_.add(varNode);
    }
    return varRoot_;
  }

  public void addNotify() {
    // Record the size of the window prior to calling parents addNotify.
    Dimension d = getSize();

    super.addNotify();

    if (fComponentsAdjusted)
      return;

    // Adjust components according to the insets
    Insets ins = getInsets();
    setSize(ins.left + ins.right + d.width,
            ins.top + ins.bottom + d.height);
    Component components[] = getContentPane().getComponents();
    for (Component component : components) {
      Point p = component.getLocation();
      p.translate(ins.left, ins.top);
      component.setLocation(p);
    }
    fComponentsAdjusted = true;
  }

  // Used for addNotify check.
  boolean fComponentsAdjusted = false;

  class SymWindow extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
      Object object = event.getSource();
      if (object == TreeView.this)
        TreeView_WindowClosing(event);
    }
  }

  void TreeView_WindowClosing(WindowEvent event) {
    this.setVisible(false);
    dispose();		 // dispose of the Frame.
  }

  public NcFile getNcFile() {
    return ncFile_;
  }

  public void setNcFile(NcFile ncFile) {
    ncFile_ = ncFile;

    String name = ncFile_.getFileName();
    this.setTitle(name + " (Tree View)");
    JTree tree = makeTree();
    treeScrollPane.setViewportView(tree);
  }

  JPanel buttonPanel = new JPanel();
  JButton expandButton = new JButton();
  JButton collapseButton = new JButton();
  JButton closeButton = new JButton();
  JScrollPane treeScrollPane = new JScrollPane();

  class SymAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == closeButton)
        closeButton_actionPerformed(event);
      else if (object == expandButton)
        expandButton_actionPerformed(event);
      else if (object == collapseButton)
        collapseButton_actionPerformed(event);
    }
  }

  void closeButton_actionPerformed(ActionEvent event) {
    this.setVisible(false);
    this.dispose();
  }

  void expandButton_actionPerformed(ActionEvent event) {
    int row=0;
    while(row < treeView_.getRowCount()) {
      if(treeView_.isCollapsed(row)) {
        treeView_.expandRow(row);
      }
      row++;
    }
  }

  void collapseButton_actionPerformed(ActionEvent event) {
    DefaultTreeModel tm = (DefaultTreeModel)treeView_.getModel();
    TreePath tp = new TreePath(tm.getPathToRoot(dimRoot_));
    treeView_.collapsePath(tp);
    tp = new TreePath(tm.getPathToRoot(attrRoot_));
    treeView_.collapsePath(tp);
    tp = new TreePath(tm.getPathToRoot(varRoot_));
    treeView_.collapsePath(tp);
  }

  private String getDimensionsAsString(Variable var) {
    StringBuilder sbuf = new StringBuilder("(");
    Iterator di = var.getDimensions().iterator();
    int dimCount = 0;
    while(di.hasNext()) {
      ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
      sbuf.append(dim.getName()).append(", ");
      dimCount++;
    }
    if(dimCount <= 0) {
      return "(no dimensions found)";
    } else {
      sbuf.setCharAt(sbuf.length()-2, ')');
      sbuf.setLength(sbuf.length()-1);
      return sbuf.toString();
    }
  }

  class VariableNode {
    String variableName_;
    String description_;
    public VariableNode(String name, String desc) {
      variableName_ = name;
      description_ = desc;
    }
    public String toString() {
      return description_;
    }
    public String getName() {
      return variableName_;
    }
  }
}
