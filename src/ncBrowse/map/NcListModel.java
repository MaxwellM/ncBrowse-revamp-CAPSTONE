/**
 *  $Id: NcListModel.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.ListModel;

import java.util.Vector;
import java.util.Enumeration;

import ucar.nc2.Dimension;
import ucar.nc2.Variable;

/**
 * <pre>
 * Title:        Drag and Drop test application
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.3 $, $Date: 2002/12/09 23:44:24 $
 */

public class NcListModel implements ListModel {
  Vector list_;
  Vector dimOrVar_;
  Vector listeners_;

  public NcListModel() {
    list_ = new Vector();
    dimOrVar_ = new Vector();
    listeners_ = new Vector();
  }

  public NcListModel(Vector dimOrVar) {
    String str = null;
    Object obj;
    dimOrVar_ = dimOrVar;
    list_ = new Vector();
    listeners_ = new Vector();
    Enumeration dov = dimOrVar_.elements();
    while(dov.hasMoreElements()) {
      obj = (Object)dov.nextElement();
      if(obj instanceof Dimension) {
        str = ((Dimension)obj).getName() + " (dim)";
      } else if(obj instanceof Variable) {
        str = ((Variable)obj).getName() + " (var)";
      }
      list_.add(str);
    }
  }

  public int getSize() {
    return list_.size();
  }

  public Object getElementAt(int index) {
    return list_.elementAt(index);
  }

  public Object getDimOrVarAt(int index) {
    return dimOrVar_.elementAt(index);
  }

  public void setElementAt(int index, String str, Object obj) {
    list_.add(index, str);
    dimOrVar_.add(index, obj);
    ListDataEvent event = new ListDataEvent(this,ListDataEvent.CONTENTS_CHANGED, 0, 0);
    Enumeration iter = listeners_.elements();
    while(iter.hasMoreElements()) {
      ((ListDataListener)iter.nextElement()).contentsChanged(event);
    }
  }

  public void addListDataListener(ListDataListener l) {
    listeners_.add(l);
  }

  public void removeListDataListener(ListDataListener l) {
    listeners_.remove(l);
  }
}
