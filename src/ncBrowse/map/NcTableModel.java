/**
 *  $Id: NcTableModel.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

import ncBrowse.NcFile;

import java.util.Vector;
import java.util.Enumeration;
import java.util.ArrayList;

import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import java.util.List;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.6 $, $Date: 2003/09/16 18:35:46 $
 */

public class NcTableModel implements TableModel {
  final String[] colNames = {"Name","Description"};
  final Vector dimOrVar_;
  final Vector name_;
  final Vector desc_;
  final Vector listeners_;
  Vector indexMap_;
  boolean showAllDims_ = false;
  int rowCount_ = 0;
  NcFile ncFile_ = null;

  public NcTableModel(NcFile ncFile, Vector list) {
    dimOrVar_ = list;
    ncFile_ = ncFile;
    listeners_ = new Vector();
    name_ = new Vector();
    desc_ = new Vector();
    indexMap_ = new Vector();
    Object obj;
    Enumeration e = dimOrVar_.elements();
    int count=0;
    int out=0;
    while(e.hasMoreElements()) {
      obj = e.nextElement();
      if(obj instanceof Dimension) {
        Dimension ncDim = (Dimension)obj;
        name_.add("<html><em>" + ncDim.getName() + "</em></html>");
        desc_.add("(" + ncDim.getLength() + " points)");
//        if((ncDim.getCoordinateVariable() == null) || showAllDims_) {
        if((ncFile_.findVariable(ncDim.getName()) == null) || showAllDims_) {
          indexMap_.add(count);
          out++;
        }
      } else if(obj instanceof Variable) {
        Variable ncVar = (Variable)obj;
        List al = ncVar.getDimensions();
        StringBuilder sbuf = new StringBuilder("[");
        if((al.size() == 1) && ((Dimension)al.get(0)).getName().equals(ncVar.getName())) {
          name_.add("<html><b>" + ncVar.getName() + "</b></html>");
          Dimension ncDim = (Dimension)al.get(0);
          sbuf.append(ncDim.getName()).append(" (").append(ncDim.getLength()).append(" points)");
        } else {
          name_.add(ncVar.getName());
          for(int i=0; i < al.size(); i++) {
            sbuf.append(((Dimension)al.get(i)).getName());
            if(i < al.size()-1) {
              sbuf.append(",");
            }
          }
        }
        sbuf.append("]");
        desc_.add(sbuf.toString());
        indexMap_.add(count);
        out++;
      } else {
        name_.add("Bad Entry");
        desc_.add("Bad Entry");
      }
      count++;
    }
    rowCount_ = out;
  }

  public Object getDimOrVarAt(int rowIndex) {
    int index = (Integer) indexMap_.elementAt(rowIndex);
    return dimOrVar_.get(index);
  }

  public int getRowCount() {
    return rowCount_;
  }

  public int getColumnCount() {
    return 2;
  }

  public String getColumnName(int columnIndex) {
    return colNames[columnIndex];
  }

  public Class getColumnClass(int columnIndex) {
    return colNames[columnIndex].getClass();
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    int index = (Integer) indexMap_.elementAt(rowIndex);
    if(columnIndex == 0) {
      return name_.elementAt(index);
    } else {
      return desc_.elementAt(index);
    }
  }

  public Vector getIndexMap() {
    return indexMap_;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    int index = (Integer) indexMap_.elementAt(rowIndex);
    if(columnIndex == 0) {
      name_.setElementAt(aValue, index);
    } else {
      desc_.setElementAt(aValue, index);
    }
    fireTableModelChange(rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE);
  }

  public void addTableModelListener(TableModelListener l) {
    listeners_.add(l);
  }

  public void removeTableModelListener(TableModelListener l) {
    listeners_.remove(l);
  }

  public void setShowAllDims(boolean showDims) {
    if(showAllDims_ != showDims) {
      showAllDims_ = showDims;
      // process list
      indexMap_ = new Vector();
      Object obj;
      Enumeration e = dimOrVar_.elements();
      int count=0;
      int out=0;
      while(e.hasMoreElements()) {
        obj = e.nextElement();
        if(obj instanceof Dimension) {
          Dimension ncDim = (Dimension)obj;
//          if((ncDim.getCoordinateVariable() == null) || showAllDims_) {
          if((ncFile_.findVariable(ncDim.getName()) == null) || showAllDims_) {
            indexMap_.add(count);
            out++;
          }
        } else if(obj instanceof Variable) {
          indexMap_.add(count);
          out++;
        }
        count++;
      }
      rowCount_ = out;
      fireTableModelChange(0, count, 0, TableModelEvent.ALL_COLUMNS);
    }
  }

  public boolean isShowAllDims() {
    return showAllDims_;
  }

  private void fireTableModelChange(int firstRow, int lastRow, int column, int type) {
    TableModelEvent tme = new TableModelEvent(this, firstRow, lastRow, column, type);
    Enumeration iter = listeners_.elements();
    while(iter.hasMoreElements()) {
      ((TableModelListener)iter.nextElement()).tableChanged(tme);
    }
  }
}
