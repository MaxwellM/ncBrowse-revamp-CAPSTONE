/**
 *  $Id: TableView.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

//  import ucar.netcdf.DimensionIterator;
//  import ucar.netcdf.DimensionSet;
//  import ucar.netcdf.VariableIterator;
//  import ucar.netcdf.Variable;
//  import ucar.netcdf.AttributeIterator;
//  import ucar.netcdf.AttributeSet;
//  import ucar.netcdf.Attribute;

/**
 * Creates a <code>JTable</code> summarizing the netCDF file variables.
 *
 * @author Donald Denbo
 * @version $Revision: 1.18 $, $Date: 2001/06/01 19:00:02 $
 */
public class TableView extends JFrame {
  private NcFile ncFile;
  private JTable varTable = null;
  private JTable dimTable = null;
  private JTable attTable = null;
  private String selectedVar = null;
  private boolean showAllVariables_ = false;
  BorderLayout borderLayout1 = new BorderLayout(0,0);
  FlowLayout flowLayout1 = new FlowLayout(FlowLayout.CENTER, 20,5);

  public TableView() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() {
    getContentPane().setLayout(borderLayout1);
    setSize(658,565);
    setVisible(false);
    buttonPanel.setLayout(flowLayout1);
    getContentPane().add(buttonPanel, "South");
    buttonPanel.setBounds(0,530,658,35);
    closeButton.setText("Close");
    closeButton.setActionCommand("Close");
    buttonPanel.add(closeButton);
    closeButton.setBounds(226,5,51,25);
    tablePanel.setLayout(new GridLayout(3,1,0,1));
    getContentPane().add(tablePanel, "Center");
    tablePanel.setBounds(0,0,658,530);
    TitledBorder dimBorder = new TitledBorder("Dimensions");
    dimScrollPane.setBorder(dimBorder);
    tablePanel.add(dimScrollPane);
    dimScrollPane.setBounds(0,0,658,176);
    TitledBorder attBorder = new TitledBorder("Attributes");
    attScrollPane.setBorder(attBorder);
    tablePanel.add(attScrollPane);
    attScrollPane.setBounds(0,177,658,176);
    TitledBorder varBorder = new TitledBorder("Variables");
    varScrollPane.setBorder(varBorder);
    tablePanel.add(varScrollPane);
    varScrollPane.setBounds(0,354,658,176);
    setTitle("A Simple Frame");

    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    SymAction lSymAction = new SymAction();
    closeButton.addActionListener(lSymAction);

  }

  public TableView(String title) {
    this();
    setTitle(title);
  }
  public void setVisible(boolean b) {
//      if(b) {
//        setLocation(50, 50);
//      }
    super.setVisible(b);
  }

  public void addNotify() {
    // Record the size of the window prior to calling parents addNotify.
    Dimension d = getSize();

    super.addNotify();

    if (fComponentsAdjusted)
      return;

    // Adjust components according to the insets
    Insets ins = getInsets();
    setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
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
      if (object == TableView.this)
  TableView_WindowClosing(event);
    }
  }

  void TableView_WindowClosing(WindowEvent event) {
    dispose();		 // dispose of the Frame.
  }

  public void setShowAllVariables(boolean show) {
    showAllVariables_ = show;
  }

  public NcFile getNcFile() {
    return ncFile;
  }
  public void setNcFile(NcFile ncFile) {
    this.ncFile = ncFile;
    String name = ncFile.getFileName();
    this.setTitle(name + " (Table View)");
    if(showAllVariables_) {
      varTable = new VariableTable(ncFile, VariableTable.ALL);
    } else {
      varTable = new VariableTable(ncFile, VariableTable.NON_DIMENSION);
    }
    attTable = makeAttributeTable(ncFile.getGlobalAttributes().iterator());
    dimTable = makeDimensionTable(ncFile.getDimensions().iterator());

    dimScrollPane.getViewport().add(dimTable);
    attScrollPane.getViewport().add(attTable);
    varScrollPane.getViewport().add(varTable);
  }
//    private JTable makeAttributeTable(AttributeSet attrSet) {
  private JTable makeAttributeTable(Iterator attrIter) {
    //    int size = attrSet.size();
    Vector names = new Vector(10);
    Vector values = new Vector(10);
    //    AttributeIterator ai = attrSet.iterator();
    while(attrIter.hasNext()) {
      Attribute att = (Attribute)attrIter.next();
      names.addElement(att.getName());
      if(att.isString()) {
	values.addElement(att.getStringValue());
      } else {
	if(att.getLength() > 1) {
	  StringBuilder sbuf = new StringBuilder(80);
	  for(int i=0; i < att.getLength(); i++) {
	    sbuf.append(att.getNumericValue(i).toString()).append(", ");
	  }
	  values.addElement(sbuf.toString());
	} else {
	  String value = att.getNumericValue().toString();
	  values.addElement(value);
	}
      }
    }
    Enumeration enames = names.elements();
    Enumeration evalues = values.elements();
    String[][] data = new String[names.size()][2];
    for(int i=0; i < names.size(); i++) {
      data[i][0] = (String)enames.nextElement();
      data[i][1] = (String)evalues.nextElement();
    }
    JTable attTable = new JTable(data, new String[] {"Name", "Value"});
    attTable.setSize(1000,1000);
    TableColumn tc;
    tc = attTable.getColumnModel().getColumn(0);
    tc.setPreferredWidth(100);
    tc = attTable.getColumnModel().getColumn(1);
    tc.setPreferredWidth(300);

    return attTable;
  }
  private JTable makeDimensionTable(Iterator dimIter) {
    //    int size = dimSet.size();
    Attribute attr = null;
    StringBuffer line;
    Vector names = new Vector(10);
    Vector values = new Vector(10);
    //    DimensionIterator di = dimSet.iterator();
    while(dimIter.hasNext()) {
      ucar.nc2.Dimension dim = (ucar.nc2.Dimension)dimIter.next();
      //      Variable ncVar = ncFile.get(dim.getName());
//      Variable ncVar = dim.getCoordinateVariable();
      Variable ncVar = ncFile.findVariable(dim.getName());
      names.addElement(dim.getName());
      line = new StringBuffer("length = " + dim.getLength());
      if(ncVar == null) {
	line.append("; (index)");
      } else {
	attr = ncVar.findAttribute("units");
	if(attr == null) {
	  line.append("; No Units Available!");
	} else {
	  line.append(": (").append(attr.getStringValue()).append(")");
	}
      }
      values.addElement(line.toString());
    }
    Enumeration enames = names.elements();
    Enumeration evalues = values.elements();
    String[][] data = new String[names.size()][2];
    for(int i=0; i < names.size(); i++) {
      data[i][0] = (String)enames.nextElement();
      data[i][1] = (String)evalues.nextElement();
    }
    JTable dimTable = new JTable(data, new String[] {"Name", "Description"});
    dimTable.setSize(1000,1000);
    TableColumn tc;
    tc = dimTable.getColumnModel().getColumn(0);
    tc.setPreferredWidth(100);
    tc = dimTable.getColumnModel().getColumn(1);
    tc.setPreferredWidth(300);

    return dimTable;
  }
  public String stripBlanks(String in) {
    StringBuilder sbuf = new StringBuilder(in);
    int len;
    int i;
    len = sbuf.length();
    // remove trailing blanks
    for(i=len - 1; i>=0; i--) {
      if(sbuf.charAt(i) != ' ') {
  len = i+1;
  break;
      }
    }
    sbuf.setLength(len);
    return sbuf.toString();
  }
  public String cleanAttribute(String in) {
    int i;
    int len;
    StringBuffer sbuf = new StringBuffer(in.length());
    int start = 0;
    int stop = in.length();
    if(in.charAt(0) == ':') start = 1;
    if(in.charAt(stop - 1) == ';') stop--;
    for(i=start; i<stop; i++) {
      if(in.charAt(i) != 0) sbuf = sbuf.append(in.charAt(i));
    }
    // remove trailing blanks
    len = sbuf.length();
    for(i=len - 1; i >= 0; i--) {
      if(sbuf.charAt(i) != ' ') {
        len = i+1;
        break;
      }
    }
    int newlen = len;
    if(sbuf.charAt(len - 1) == '"') {
      // remove blanks right of last quote
      for(i=len - 2; i >= 0; i--) {
        if(sbuf.charAt(i) != ' ') {
          sbuf.setCharAt(i+1, '"');
          newlen = i+2;
          break;
        }
      }
      sbuf.setLength(newlen);
    }
    return sbuf.toString();
  }

  JPanel buttonPanel = new JPanel();
  JButton closeButton = new JButton();
  JPanel tablePanel = new JPanel();
  JScrollPane dimScrollPane = new JScrollPane();
  JScrollPane attScrollPane = new JScrollPane();
  JScrollPane varScrollPane = new JScrollPane();

  class SymAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == closeButton)
  closeButton_actionPerformed(event);
    }
  }

  void closeButton_actionPerformed(ActionEvent event) {
    try {
      this.setVisible(false);
      this.dispose();
    } catch (Exception ignored) {
    }
  }
}

