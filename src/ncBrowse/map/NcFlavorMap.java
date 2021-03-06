/**
 *  $Id: NcFlavorMap.java 15 2013-04-24 19:16:08Z dwd $
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
 * @version $Revision: 1.3 $, $Date: 2001/09/10 18:00:46 $
 */

public class NcFlavorMap implements FlavorMap {

  private HashMap mapFlavors_ = null;
  private HashMap mapNatives_ = null;
  private static NcFlavorMap instance_ = null;

  public NcFlavorMap() {
    mapNatives_ = new HashMap();
    mapNatives_.put("NetCDF Index",NcTransferable.NcIndexFlavor);
    mapFlavors_ = new HashMap();
    mapFlavors_.put(NcTransferable.NcIndexFlavor,"NetCDF Index");
    instance_ = this;
  }

  public static NcFlavorMap getInstance() {
    if(instance_ == null) {
      instance_ = new NcFlavorMap();
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
    return "ncFlavorMap: " + mapNatives_.toString();
  }
}
