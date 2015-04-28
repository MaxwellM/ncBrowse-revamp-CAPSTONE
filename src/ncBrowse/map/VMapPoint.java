/**
 *  $Id: VMapPoint.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import ncBrowse.sgt.dm.SGTData;
//import gov.noaa.pmel.sgt.dm.SGTData;
import ncBrowse.sgt.dm.SGTMetaData;
//import gov.noaa.pmel.sgt.dm.SGTMetaData;
import ncBrowse.sgt.dm.SGTPoint;
//import gov.noaa.pmel.sgt.dm.SGTPoint;
import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;

/**
 * Variable Point Data. 
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.1 $, $Date: 2004/05/07 16:18:41 $
 */
  public class VMapPoint extends VMapData implements SGTPoint {

    public double getX() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTPoint method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getX() not yet implemented.");
    }

    public double getY() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTPoint method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getY() not yet implemented.");
    }

    public boolean hasValue() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTPoint method*/
      throw new java.lang.UnsupportedOperationException(
          "Method hasValue() not yet implemented.");
    }

    public double getValue() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTPoint method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getValue() not yet implemented.");
    }

    public SGTMetaData getValueMetaData() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTPoint method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getValueMetaData() not yet implemented.");
    }

    public GeoDate getTime() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTPoint method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getTime() not yet implemented.");
    }

    public long getLongTime() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTPoint method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getTime() not yet implemented.");
    }

    public SGTData copy() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTData method*/
      throw new java.lang.UnsupportedOperationException(
          "Method copy() not yet implemented.");
    }
  }

