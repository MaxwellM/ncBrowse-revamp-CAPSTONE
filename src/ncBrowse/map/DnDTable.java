/**
 *  $Id: DnDTable.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import javax.swing.JTable;

import java.awt.dnd.*;
import java.awt.datatransfer.StringSelection;

import ncBrowse.Debug;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.6 $, $Date: 2002/12/09 23:44:24 $
 */

public class DnDTable extends JTable
  implements DragGestureListener, DragSourceListener {
  final DragSource dragSource;

  public DnDTable() {
    super();
    dragSource = new DragSource();
    dragSource.createDefaultDragGestureRecognizer(this,
                                                  DnDConstants.ACTION_COPY_OR_MOVE,
                                                  this);
  }

  public void dragGestureRecognized(DragGestureEvent dge) {
    int index = getSelectedRow();
    NcTableModel model = (NcTableModel)getModel();
    Object selected = model.getDimOrVarAt(index);
    if ( selected != null ){
      StringBuilder str = new StringBuilder("<html>");
      NcTransferable nct = new NcTransferable(index);
      dragSource.startDrag (dge, DragSource.DefaultCopyDrop, nct, this);
    } else {
      if(Debug.DEBUG) System.out.println( "nothing was selected");
    }
  }

  public void dragEnter(DragSourceDragEvent dsde) {
    if(Debug.DEBUG) System.out.println("DragSource Enter");
  }

  public void dragOver(DragSourceDragEvent dsde) {
    if(Debug.DEBUG) System.out.println("DragSource Over");
  }

  public void dropActionChanged(DragSourceDragEvent dsde) {
    if(Debug.DEBUG) System.out.println("DragSource: Drop Action Changed");
  }

  public void dragExit(DragSourceEvent dse) {
    //    this.setCursor(DragSource.DefaultLinkNoDrop);
    if(Debug.DEBUG) System.out.println("DragSource Exit");
  }

  public void dragDropEnd(DragSourceDropEvent dsde) {
    if(dsde.getDropSuccess()) {
      if(Debug.DEBUG) System.out.println("DragSource Success!");
    }
  }
}
