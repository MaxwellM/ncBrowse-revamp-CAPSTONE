/**
 *  $Id: NcDragSource.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import ncBrowse.Debug;

import java.awt.dnd.DragSource;
import java.awt.datatransfer.FlavorMap;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.3 $, $Date: 2001/11/06 00:05:10 $
 */

public class NcDragSource extends DragSource {

  public NcDragSource() {
    super();
  }

  public FlavorMap getFlavorMap() {
    if(Debug.DEBUG) System.out.println("NcDragSource.getFlavorMap() called");
    return NcFlavorMap.getInstance();
  }
}
