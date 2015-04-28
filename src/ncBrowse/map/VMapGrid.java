/**
 *  $Id: VMapGrid.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import ncBrowse.sgt.dm.SGTData;
//import gov.noaa.pmel.sgt.dm.SGTData;
import ncBrowse.sgt.dm.SGTGrid;
//import gov.noaa.pmel.sgt.dm.SGTGrid;
import ncBrowse.sgt.dm.SGTMetaData;
//import gov.noaa.pmel.sgt.dm.SGTMetaData;
import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;
import ncBrowse.sgt.geom.GeoDateArray;
//import gov.noaa.pmel.util.GeoDateArray;
import ncBrowse.sgt.geom.Range2D;
//import gov.noaa.pmel.util.Range2D;
import ncBrowse.sgt.geom.SoTRange;
//import gov.noaa.pmel.util.SoTRange;
import ncBrowse.sgt.geom.SoTValue;
//import gov.noaa.pmel.util.SoTValue;

/**
 * Variable Grid Data.
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.3 $, $Date: 2004/05/07 23:32:57 $
 */
  public class VMapGrid extends VMapData implements SGTGrid {

    protected double[] xloc_;
    protected double[] yloc_;
//    protected GeoDate[] tloc_;
    protected GeoDateArray tloc_;
    protected double[] grid_;
    protected double[] xEdges_;
    protected double[] yEdges_;
    protected GeoDate[] tEdges_;
    protected boolean hasXEdges_;
    protected boolean hasYEdges_;
    protected SGTMetaData zMeta_ = null;
    protected SGTGrid associatedData_;
    private SoTRange xEdgesRange_ = null;
    private SoTRange yEdgesRange_ = null;
    private Range2D zRange_ = null;

    public VMapGrid() {
    }

    public VMapGrid(String id, String title,
                    double[] grid, SGTMetaData zmeta,
                    double[] xloc, SGTMetaData xmeta,
                    double[] yloc, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      grid_ = grid;
      zMeta_ = zmeta;
      xloc_ = xloc;
      yloc_ = yloc;
      xTime_ = false;
      yTime_ = false;
      hasXEdges_ = false;
      hasYEdges_ = false;
      xRange_ = VMapModel.computeRange(xloc);
      yRange_ = VMapModel.computeRange(yloc);
      zRange_ = VMapModel.computeRange2D(grid);
    }

    public VMapGrid(String id, String title,
                    double[] grid, SGTMetaData zmeta,
                    GeoDate[] tloc, SGTMetaData xmeta,
                    double[] yloc, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      grid_ = grid;
      tloc_ = new GeoDateArray(tloc);
      yloc_ = yloc;
      xTime_ = true;
      yTime_ = false;
      hasXEdges_ = false;
      hasYEdges_ = false;
      xRange_ = VMapModel.computeRange(tloc);
      yRange_ = VMapModel.computeRange(yloc);
      zRange_ = VMapModel.computeRange2D(grid);
    }

    public VMapGrid(String id, String title,
                    double[] grid, SGTMetaData zmeta,
                    double[] xloc, SGTMetaData xmeta,
                    GeoDate[] tloc, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      grid_ = grid;
      xloc_ = xloc;
      tloc_ = new GeoDateArray(tloc);
      xTime_ = false;
      yTime_ = true;
      hasXEdges_ = false;
      hasYEdges_ = false;
      xRange_ = VMapModel.computeRange(xloc);
      yRange_ = VMapModel.computeRange(tloc);
      zRange_ = VMapModel.computeRange2D(grid);
    }

    public VMapGrid(String id, String title,
                    double[] grid, SGTMetaData zmeta,
                    GeoDateArray tloc, SGTMetaData xmeta,
                    double[] yloc, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      grid_ = grid;
      tloc_ = tloc;
      yloc_ = yloc;
      xTime_ = true;
      yTime_ = false;
      hasXEdges_ = false;
      hasYEdges_ = false;
      xRange_ = VMapModel.computeRange(tloc);
      yRange_ = VMapModel.computeRange(yloc);
      zRange_ = VMapModel.computeRange2D(grid);
    }

    public VMapGrid(String id, String title,
                    double[] grid, SGTMetaData zmeta,
                    double[] xloc, SGTMetaData xmeta,
                    GeoDateArray tloc, SGTMetaData ymeta) {
      super(id, title, xmeta, ymeta);
      grid_ = grid;
      xloc_ = xloc;
      tloc_ = tloc;
      xTime_ = false;
      yTime_ = true;
      hasXEdges_ = false;
      hasYEdges_ = false;
      xRange_ = VMapModel.computeRange(xloc);
      yRange_ = VMapModel.computeRange(tloc);
      zRange_ = VMapModel.computeRange2D(grid);
    }

    public SGTData copy() {
      return null;
    }

    public double[] getXArray() {
      return xloc_;
    }

    /**
     * Get the length of the x axis
     *
     * @since 2.0
     * @return X axis length
     */
    public int getXSize() {
      return xloc_.length;
    }

    public double[] getYArray() {
      return yloc_;
    }

    /**
     * Get the length of the y axis
     *
     * @since 2.0
     * @return Y axis length
     */
    public int getYSize() {
      return yloc_.length;
    }

    public double[] getZArray() {
      return grid_;
    }

    public GeoDate[] getTimeArray() {
      return tloc_.getGeoDate();
    }

    public GeoDateArray getGeoDateArray() {
      return tloc_;
    }

    /**
     * Get the length of the Time axis
     *
     * @since 2.0
     * @return time axis length
     */
    public int getTSize() {
      return tloc_.getLength();
    }

    public SGTMetaData getZMetaData() {
      return zMeta_;
    }

    /**
     * Set the associated data grid.
     *
     * @since 2.0
     * @param assoc associated data
     */
    public void setAssociatedData(SGTGrid assoc) {
      associatedData_ = assoc;
      firePropertyChange();
    }

    public SGTGrid getAssociatedData() {
      return associatedData_;
    }

    public boolean hasAssociatedData() {
      return(associatedData_ != null);
    }

    public boolean hasXEdges() {
      return hasXEdges_;
    }

    public double[] getXEdges() {
      return xEdges_;
    }

    /**
     * Set the values for the x grid edges.
     *
     * @param edge x grid edges
     */
    public void setXEdges(double[] edge) {
      xEdges_ = edge;
      hasXEdges_ = true;
      xEdgesRange_ = VMapModel.computeRange(edge);
    }

    public boolean hasYEdges() {
      return hasYEdges_;
    }

    public double[] getYEdges() {
      return yEdges_;
    }

    /**
     * Set the values for the y grid edges.
     *
     * @param edge y grid edges
     */
    public void setYEdges(double[] edge) {
      yEdges_ = edge;
      hasYEdges_ = true;
      yEdgesRange_ = VMapModel.computeRange(edge);
    }

    public GeoDate[] getTimeEdges() {
      return tEdges_;
    }

    public GeoDateArray getGeoDateArrayEdges() {
      return new GeoDateArray(tEdges_);
    }

    /**
     * Set the values for the temporal grid edges.
     *
     * @param edge time grid edges
     */
    public void setTimeEdges(GeoDate[] edge) {
      tEdges_ = edge;
      if(xTime_) {
        hasXEdges_ = true;
        xEdgesRange_ = VMapModel.computeRange(edge);
      } else if(yTime_) {
        hasYEdges_ = true;
        yEdgesRange_ = VMapModel.computeRange(edge);
      }
    }

    public void setZMetaData(SGTMetaData md) {
      zMeta_ = md;
    }

    /**
     * Set the x coordinate grid centers
     *
     * @param xloc x axis values
     */
    public void setXArray(double[] xloc) {
      xloc_ = xloc;
      xTime_ = false;
      xRange_ = VMapModel.computeRange(xloc);
      firePropertyChange();
    }

    /**
     * Set the y coordinate grid centers
     *
     * @param yloc y axis values
     */
    public void setYArray(double[] yloc) {
      yloc_ = yloc;
      yTime_ = false;
      yRange_ = VMapModel.computeRange(yloc);
      firePropertyChange();
    }

    /**
     * Set the z grid values.
     *
     * @param grid z data values
     */
    public void setZArray(double[] grid) {
      grid_ = grid;
      zRange_ = VMapModel.computeRange2D(grid);
      firePropertyChange();
    }

    /**
     * set the temporal grid centers
     *
     * @param isXTime true if X axis is time
     * @param tloc time axis values
     */
    public void setTimeArray(boolean isXTime, GeoDateArray tloc) {
      //      changes_.firePropertyChange("dataModified", tData_, tdata);
      tloc_ = tloc;
      if(isXTime) {
        xRange_ = VMapModel.computeRange(tloc);
        xTime_ = true;
      } else {
        yRange_ = VMapModel.computeRange(tloc);
        yTime_ = true;
      }
      firePropertyChange();
    }

    public void setTimeArray(boolean isXTime, GeoDate[] tloc) {
      //      changes_.firePropertyChange("dataModified", tData_, tdata);
      tloc_ = new GeoDateArray(tloc);
      if(isXTime) {
        xRange_ = VMapModel.computeRange(tloc);
        xTime_ = true;
      } else {
        yRange_ = VMapModel.computeRange(tloc);
        yTime_ = true;
      }
      firePropertyChange();
    }

    public Range2D getZRange() {
      return zRange_;
    }

    /**
     * Return the range of the x edges
     *
     * @since 2.0
     * @return x edges range
     */
    public SoTRange getXEdgesRange() {
      return xEdgesRange_;
    }

    /**
     * Return the range of the y edges
     *
     * @since 2.0
     * @return y edges range
     */
    public SoTRange getYEdgesRange() {
      return yEdgesRange_;
    }

   // @Override
    public double getValueAt(SoTValue arg0, SoTValue arg1) {
      // TODO Auto-generated method stub
      return 0;
    }

  }


