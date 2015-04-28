/**
 *  $Id: VMapTuple.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import ncBrowse.sgt.dm.SGTData;
//import gov.noaa.pmel.sgt.dm.SGTData;
import ncBrowse.sgt.dm.SGTMetaData;
//import gov.noaa.pmel.sgt.dm.SGTMetaData;
import ncBrowse.sgt.dm.SGTTuple;
//import gov.noaa.pmel.sgt.dm.SGTTuple;
import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;
import ncBrowse.sgt.geom.GeoDateArray;
//import gov.noaa.pmel.util.GeoDateArray;
import ncBrowse.sgt.geom.Range2D;
//import gov.noaa.pmel.util.Range2D;

/**
 * Variable Tuple Data.
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.3 $, $Date: 2004/05/07 23:32:57 $
 */
  public class VMapTuple extends VMapData implements SGTTuple {
    private double[] xData_;
    private double[] yData_;
    private double[] zData_;
    private GeoDateArray tData_;

    public VMapTuple() {
    }

    public VMapTuple(String id, String title,
                     double[] xd, SGTMetaData xmeta,
                     double[] yd, SGTMetaData ymeta,
                     double[] zd, SGTMetaData zmeta) {
      super(id, title, xmeta, ymeta, zmeta);
      xData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      yData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
      zData_ = zd;
      zRange_ = VMapModel.computeRange2D(zd);
    }

    public VMapTuple(String id, String title,
                     GeoDate[] xd, SGTMetaData xmeta,
                     double[] yd, SGTMetaData ymeta,
                     double[] zd, SGTMetaData zmeta) {
      super(id, title, xmeta, ymeta, zmeta);
      tData_ = new GeoDateArray(xd);
      xRange_ = VMapModel.computeRange(xd);
      xTime_ = true;
      yData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
      zData_ = zd;
      zRange_ = VMapModel.computeRange2D(zd);
    }

    public VMapTuple(String id, String title,
                     double[] xd, SGTMetaData xmeta,
                     GeoDate[] yd, SGTMetaData ymeta,
                     double[] zd, SGTMetaData zmeta) {
      super(id, title, xmeta, ymeta);
      xData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      tData_ = new GeoDateArray(yd);
      yRange_ = VMapModel.computeRange(yd);
      yTime_ = true;
      zData_ = zd;
      zRange_ = VMapModel.computeRange2D(zd);
    }

    public VMapTuple(String id, String title,
                     GeoDateArray xd, SGTMetaData xmeta,
                     double[] yd, SGTMetaData ymeta,
                     double[] zd, SGTMetaData zmeta) {
      super(id, title, xmeta, ymeta, zmeta);
      tData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      xTime_ = true;
      yData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
      zData_ = zd;
      zRange_ = VMapModel.computeRange2D(zd);
    }

    public VMapTuple(String id, String title,
                     double[] xd, SGTMetaData xmeta,
                     GeoDateArray yd, SGTMetaData ymeta,
                     double[] zd, SGTMetaData zmeta) {
      super(id, title, xmeta, ymeta);
      xData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      tData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
      yTime_ = true;
      zData_ = zd;
      zRange_ = VMapModel.computeRange2D(zd);
    }

    /*public TupleData(String id, String title,
                    double[] xd, SGTMetaData xmeta,
                    double[] yd, SGTMetaData ymeta,
                    GeoDate[] zd, SGTMetaData zmeta) {
      super(id, title, xmeta, ymeta);
      xData_ = xd;
      xRange_ = computeRange(xd);
      yRange_ = computeRange(yd);
      tData_ = zd;
      zRange_ = computeRange2D(zd);
      zTime_ = true;
         }*/

    public void setXArray(double[] xdata) {
      //      changes_.firePropertyChange("dataModified", xData_, xdata);
      xData_ = xdata;
      xRange_ = VMapModel.computeRange(xdata);
      xTime_ = false;
      firePropertyChange();
    }

    public void setYArray(double[] ydata) {
      //      changes_.firePropertyChange("dataModified", xData_, xdata);
      yData_ = ydata;
      yRange_ = VMapModel.computeRange(ydata);
      yTime_ = false;
      firePropertyChange();
    }

    public void setZArray(double[] zdata) {
      //      changes_.firePropertyChange("dataModified", xData_, xdata);
      zData_ = zdata;
      zRange_ = VMapModel.computeRange2D(zdata);
      zTime_ = false;
      firePropertyChange();
    }

    public double[] getXArray() {
      return xData_;
    }

    public double[] getYArray() {
      return yData_;
    }

    public double[] getZArray() {
      return zData_;
    }

    public int getSize() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTTuple method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getSize() not yet implemented.");
    }

    public void setTimeArray(boolean isXTime, GeoDateArray tdata) {
      //      changes_.firePropertyChange("dataModified", tData_, tdata);
      tData_ = tdata;
      if(isXTime) {
        xRange_ = VMapModel.computeRange(tdata);
        xTime_ = true;
      } else {
        yRange_ = VMapModel.computeRange(tdata);
        yTime_ = true;
      }
      firePropertyChange();
    }

    public void setTimeArray(boolean isXTime, GeoDate[] tdata) {
      //      changes_.firePropertyChange("dataModified", tData_, tdata);
      tData_ = new GeoDateArray(tdata);
      if(isXTime) {
        xRange_ = VMapModel.computeRange(tdata);
        xTime_ = true;
      } else {
        yRange_ = VMapModel.computeRange(tdata);
        yTime_ = true;
      }
      firePropertyChange();
    }

    public GeoDate[] getTimeArray() {
      return tData_.getGeoDate();
    }

    public GeoDateArray getGeoDateArray() {
      return tData_;
    }

//    public GeoDateArray getGeoDateArray() {
//      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTTuple method*/
//      throw new java.lang.UnsupportedOperationException("Method getTimeArray() not yet implemented.");
//    }
    public double[] getAssociatedData() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTTuple method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getAssociatedData() not yet implemented.");
    }

    public boolean hasAssociatedData() {
      return false;
    }

    public SGTMetaData getZMetaData() {
      return zMeta_;
    }

    public Range2D getZRange() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTTuple method*/
      throw new java.lang.UnsupportedOperationException(
          "Method getZRange() not yet implemented.");
    }

    public SGTData copy() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTData method*/
      throw new java.lang.UnsupportedOperationException(
          "Method copy() not yet implemented.");
    }
  }
