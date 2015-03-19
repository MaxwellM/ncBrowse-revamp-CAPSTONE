/**
 *  $Id: VariableTable.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;

//  import ucar.netcdf.DimensionIterator;
//  import ucar.netcdf.VariableIterator;
//  import ucar.netcdf.Variable;
//  import ucar.netcdf.Attribute;

import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.Attribute;

/**
 * Creates a <code>JTable</code> summarizing netCDF variables.
 *
 * @author Donald Denbo
 * @version $Revision: 1.8 $, $Date: 2001/06/01 19:00:02 $
 */
public class VariableTable extends JTable {
  public static final int ALL = 1;
  public static final int NON_DIMENSION = 2;
  public static final int DIMENSION = 3;

  private NcFile ncFile = null;
  private int type = NON_DIMENSION;

  public VariableTable(NcFile ncFile) {
    this(ncFile, ALL);
  }

  public VariableTable(NcFile ncFile, int type) {
    super();
    this.ncFile = ncFile;
    this.type = type;
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    makeVariableTable();
  }

  private void makeVariableTable() {
    Attribute att = null;
    Vector<String> names = new Vector<String>(10,10);
    Vector<String> values = new Vector<String>(10,10);
    Vector<String> dims = new Vector<String>(10,10);
//      VariableIterator varIter = null;
    Iterator<Variable> varIter = null;
    if(type == NON_DIMENSION) {
      varIter = ncFile.getNonDimensionVariables();
    } else if(type == DIMENSION) {
      varIter = ncFile.getDimensionVariables();
    } else {
      //      varIter = ncFile.iterator();
      varIter = ncFile.getVariables().iterator();
    }
    while(varIter.hasNext()) {
      Variable var = (Variable)varIter.next();
      StringBuffer sbuf = new StringBuffer();
      names.addElement(var.getName());
      att = var.findAttribute("long_name");
      if(att != null) {
	sbuf.append(stripBlanks(att.getStringValue()));
	sbuf.append("; ");
      }
      att = var.findAttribute("units");
      if(att != null) {
	sbuf.append("(");
	sbuf.append(att.getStringValue());
	sbuf.append(")");
      } else {
	sbuf.append(" ");
      }
      values.addElement(sbuf.toString());
      sbuf = new StringBuffer();
      sbuf.append("[");
//        DimensionIterator di = var.getDimensionIterator();
      Iterator<Dimension> di = var.getDimensions().iterator();
      int dimCount = 0;
      while(di.hasNext()) {
	ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
	sbuf.append(dim.getName()+",");
	dimCount++;
      }
      if(dimCount <= 0) {
	sbuf.append("no dimensions found]");
      } else {
	sbuf.setCharAt(sbuf.length()-1, ']');
      }
      //      sbuf.deleteCharAt(sbuf.length()-1);
      //      sbuf.append("]");
      dims.addElement(sbuf.toString());
    }
    Enumeration<String> enames = names.elements();
    Enumeration<String> evalues = values.elements();
    Enumeration<String> edims = dims.elements();
    String[][] data = new String[names.size()][3];
    for(int i=0; i < names.size(); i++) {
      data[i][0] = (String)enames.nextElement();
      data[i][1] = (String)evalues.nextElement();
      data[i][2] = (String)edims.nextElement();
    }
    VarTableModel vtm = new VarTableModel(data, new String[] 
      {"Name", "Description", "Dimensions"});
    setModel(vtm);
    setSize(1000,1000);
    TableColumn tc;
    tc = getColumnModel().getColumn(0);
    tc.setPreferredWidth(100);
    tc = getColumnModel().getColumn(1);
    tc.setPreferredWidth(200);
    tc = getColumnModel().getColumn(2);
    tc.setPreferredWidth(100);
  }
  public String stripBlanks(String in) {
    StringBuffer sbuf = new StringBuffer(in);
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
  class VarTableModel extends AbstractTableModel {
    String[] colNames;
    String[][] data;
    public VarTableModel(String[][] data, String[] columnNames) {
      this.data = data;
      this.colNames = columnNames;
      if(Debug.DEBUG) System.out.println("data.length = " + data.length
					 + ", colNames.length = " 
					 + colNames.length);
    }
    public int getRowCount() {
      return data.length;
    }
    public int getColumnCount() {
      return colNames.length;
    }
    public Object getValueAt(int row, int column) {
      return data[row][column];
    }
    public String getColumnName(int column) {
      return colNames[column];
    }
  }
}
