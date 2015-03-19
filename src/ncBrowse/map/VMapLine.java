/**
 *  $Id: VMapLine.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTLine;
import gov.noaa.pmel.sgt.dm.SGTMetaData;
import gov.noaa.pmel.util.GeoDate;
import gov.noaa.pmel.util.GeoDateArray;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.4 $, $Date: 2004/05/17 22:49:34 $
 */

  public class VMapLine extends VMapData implements SGTLine {
    private double[] xData_;
    private double[] yData_;
    private GeoDateArray tData_;
    private double[] associatedData_ = null;

    public VMapLine() {
    }

    public VMapLine(String id, String title,
                    double[] xd, SGTMetaData xmeta,
                    double[] yd, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      xData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      yData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
    }

    public VMapLine(String id, String title,
                    GeoDate[] xd, SGTMetaData xmeta,
                    double[] yd, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      tData_ = new GeoDateArray(xd);
      xRange_ = VMapModel.computeRange(xd);
      xTime_ = true;
      yData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
    }

    public VMapLine(String id, String title,
                    double[] xd, SGTMetaData xmeta,
                    GeoDate[] yd, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      xData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      tData_ = new GeoDateArray(yd);
      yRange_ = VMapModel.computeRange(yd);
      yTime_ = true;
    }

    public VMapLine(String id, String title,
                    GeoDateArray xd, SGTMetaData xmeta,
                    double[] yd, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      tData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      xTime_ = true;
      yData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
    }

    public VMapLine(String id, String title,
                    double[] xd, SGTMetaData xmeta,
                    GeoDateArray yd, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      xData_ = xd;
      xRange_ = VMapModel.computeRange(xd);
      tData_ = yd;
      yRange_ = VMapModel.computeRange(yd);
      yTime_ = true;
    }

    public double[] getXArray() {
      return xData_;
    }

    public void setXArray(double[] xdata) {
      xData_ = xdata;
      xRange_ = VMapModel.computeRange(xdata);
      xTime_ = false;
      firePropertyChange();
    }

    public double[] getYArray() {
      return yData_;
    }

    public void setYArray(double[] ydata) {
      yData_ = ydata;
      yRange_ = VMapModel.computeRange(ydata);
      yTime_ = false;
      firePropertyChange();
    }

    public void setTimeArray(boolean isXTime, GeoDate[] tdata) {
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

    public void setTimeArray(boolean isXTime, GeoDateArray tdata) {
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

    public GeoDate[] getTimeArray() {
      return tData_.getGeoDate();
    }

    public GeoDateArray getGeoDateArray() {
      return tData_;
    }

    public void setAssociatedData(double[] data) {
      associatedData_ = data;
    }
    public double[] getAssociatedData() {
      return associatedData_;
    }

    public boolean hasAssociatedData() {
      return associatedData_ != null;
    }

    public SGTData copy() {
      /**@todo: Implement this gov.noaa.pmel.sgt.dm.SGTData method*/
      throw new java.lang.UnsupportedOperationException(
          "Method copy() not yet implemented.");
    }
  }

