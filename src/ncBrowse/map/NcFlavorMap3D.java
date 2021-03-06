/**
 *  $Id: NcFlavorMap3D.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import java.util.Map;
import java.util.HashMap;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorMap;

import ncBrowse.Debug;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.2 $, $Date: 2003/10/01 22:32:28 $
 */

public class NcFlavorMap3D implements FlavorMap {

  private HashMap mapFlavors_ = null;
  private HashMap mapNatives_ = null;
  private static NcFlavorMap3D instance_ = null;

  public NcFlavorMap3D() {
    mapNatives_ = new HashMap();
    mapNatives_.put("NetCDF Index",NcTransferable.NcIndexFlavor);
    mapFlavors_ = new HashMap();
    mapFlavors_.put(NcTransferable.NcIndexFlavor,"NetCDF Index");
    instance_ = this;
  }

  public static NcFlavorMap3D getInstance() {
    if(instance_ == null) {
      instance_ = new NcFlavorMap3D();
    }
    return instance_;
  }

  public Map getNativesForFlavors(DataFlavor[] flavors) {
    if(flavors == null) return mapFlavors_;
    HashMap map = new HashMap();
    for (DataFlavor flavor : flavors) {
      if (mapFlavors_.containsKey(flavor)) {
        map.put(flavor, mapFlavors_.get(flavor));
      }
    }
    return map;
  }

  public Map getFlavorsForNatives(String[] natives) {
    if(natives == null) return mapNatives_;
    HashMap map = new HashMap();
    for (String aNative : natives) {
      if (mapNatives_.containsKey(aNative)) {
        map.put(aNative, mapNatives_.get(aNative));
      }
    }
    return map;
  }

  public String toString() {
    return "ncFlavorMap3D: " + mapNatives_.toString();
  }
}
