/**
 *  $Id: NcTransferable.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;

import ncBrowse.Debug;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.5 $, $Date: 2002/12/09 23:44:24 $
 */

public class NcTransferable implements Transferable {

  public static DataFlavor NcIndexFlavor;

  static {
    try {
      NcIndexFlavor = new DataFlavor(Class.forName("java.lang.Integer"),
                                      "netCDF Index");
    } catch (ClassNotFoundException cnfe) {
          cnfe.printStackTrace();
    }
  }

  private DataFlavor flavor_ = null;
  private Object data_ = null;

  public NcTransferable(Integer obj) {
    data_ = obj;
    flavor_ = NcIndexFlavor;
    if(Debug.DEBUG) System.out.println("NcTransferable: " + flavor_.toString());
  }

  public DataFlavor[] getTransferDataFlavors() {
    if(Debug.DEBUG) System.out.println("NcTransferable.getTransferDataFlavors() called");
    DataFlavor[] df = new DataFlavor[1];
    df[0] = flavor_;
    return df;
  }

  public boolean isDataFlavorSupported(DataFlavor flavor) {
    if(Debug.DEBUG) System.out.println("NcTransferable.isDataFlavorSupported() called");
    return flavor.equals(flavor_);
  }

  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if(Debug.DEBUG) System.out.println("NcTransferable.getTransferData() called");
    if(flavor != flavor_) throw new UnsupportedFlavorException(flavor);
    return data_;
  }
}
