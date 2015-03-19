/**
 *  $Id: LabelTarget.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import java.io.IOException;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JLabel;

import ncBrowse.Debug;


/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.12 $, $Date: 2004/05/17 22:49:34 $
 */
public class LabelTarget implements DropTargetListener {
  JLabel label_;
  int type_;

  public LabelTarget(JLabel tf, int type) {
    label_ = tf;
    type_ = type;
  }

  public void dragEnter(DropTargetDragEvent dtde) {
    if(Debug.DEBUG) System.out.println("DropTarget Enter");
  }

  public void dragOver(DropTargetDragEvent dtde) {
    if(Debug.DEBUG) System.out.println("DropTarget Over");
  }

  public void dropActionChanged(DropTargetDragEvent dtde) {
    if(Debug.DEBUG) System.out.println("DropTarget Action Changed");
  }

  public void dragExit(DropTargetEvent dte) {
    if(Debug.DEBUG) System.out.println("DropTarget Exit");
  }

  public void drop(DropTargetDropEvent dtde) {
    if(Debug.DEBUG) System.out.println("DropTarget drop");

    try {
      Transferable transferable = dtde.getTransferable();

      // we accept only NcIndex flavor
      if(transferable.isDataFlavorSupported(NcTransferable.NcIndexFlavor)){

        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        if(transferable.isDataFlavorSupported(NcTransferable.NcIndexFlavor)) {  // redundent test
          int index = ((Integer)transferable.getTransferData(NcTransferable.NcIndexFlavor)).intValue();
          VMapModel map = VariableMapDialog.getCurrentMap();
          Object obj = VariableMapDialog.getCurrentModel().getDimOrVarAt(index);
          map.setElement(obj, type_);
          VariableMapDialog.updateAll(map);
        }
        dtde.getDropTargetContext().dropComplete(true);
        VariableMapDialog.valueChanged(type_);

      } else{
        dtde.rejectDrop();
      }
    }
    catch (IOException exception) {
      exception.printStackTrace();
      System.err.println( "Exception" + exception.getMessage());
      dtde.rejectDrop();
    }
    catch (UnsupportedFlavorException ufException ) {
      ufException.printStackTrace();
      System.err.println( "Exception" + ufException.getMessage());
      dtde.rejectDrop();
    }
  }
}
