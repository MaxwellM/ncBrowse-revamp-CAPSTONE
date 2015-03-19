/**
 *  $Id: SimpleListModel.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.ListModel;

import java.util.Vector;
import java.util.Enumeration;

/**
 * <pre>
 * Title:        Drag and Drop test application
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.2 $, $Date: 2001/09/10 18:00:47 $
 */

public class SimpleListModel implements ListModel {
  Vector list_;
  Vector listeners_;

  public SimpleListModel() {
    list_ = new Vector();
    listeners_ = new Vector();
  }
  public SimpleListModel(String[] strlist) {
    list_ = new Vector();
    listeners_ = new Vector();
    for(int i=0; i < strlist.length; i++) {
      list_.add(strlist[i]);
    }
  }

  public int getSize() {
    return list_.size();
  }

  public Object getElementAt(int index) {
    return list_.elementAt(index);
  }

  public void setElementAt(int index, Object obj) {
    list_.add(index, obj);
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
