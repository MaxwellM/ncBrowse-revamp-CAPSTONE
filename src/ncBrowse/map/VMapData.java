/**
 *  $Id: VMapData.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import gov.noaa.pmel.sgt.SGLabel;
import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTMetaData;
import gov.noaa.pmel.util.Range2D;
import gov.noaa.pmel.util.SoTRange;

/**
 * Variable Map Model. Some definitions for variable mapping.
 * <dl>
 *  <dt>Parameter</dt>
 *  <dd>A netCDF Dimension. They are one-dimensional, integral, and without
 *      units. </dd>
 *
 *  <dt>Axis</dt>
 *  <dd>Either the X or Y graphical axes in SGT</dd>
 *
 *  <dt>Tuple</dt>
 *  <dd>A grouping  of 1 to n numbers that are treated atomically. The X,Y,Z
 *      triplets are a tuple.</dd>
 *
 *  <dt>grid</dt>
 *  <dd>A one or more dimensional collection. For example, <em>h(i,j,k)</em> is a grid of
 *      dimension 3. Each <em>i,j,k</em> tuple references a single value.</dd>
 *
 *  <dt>vector</dt>
 *  <dd>A two  component tuple where each component is a grid.  Typically
 *      used for displaying velocities.</dd>
 *
 *  <dt>variable</dt>
 *  <dd>A netCDF Variable. (independent  variable)</dd>
 *
 *  <dt>dimension-variable</dt>
 *  <dd>A netCDF Variable that is one-dimensional and has the same name as its
 *      single dimension. (dependent  variable)</dd>
 * </dl>
 *
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.1 $, $Date: 2004/05/06 20:53:36 $
 */
  public abstract class VMapData implements SGTData, Serializable {
    protected PropertyChangeSupport changes_ = new PropertyChangeSupport(this);
    protected boolean inBatch_ = false;
    protected boolean changeMade_ = false;
    protected String id_;
    protected String title_;
    protected SGLabel keyTitle_ = null;
    protected boolean yTime_ = false;
    protected boolean xTime_ = false;
    protected boolean zTime_ = false;
    protected SGTMetaData xMeta_;
    protected SGTMetaData yMeta_;
    protected SGTMetaData zMeta_;
    protected SoTRange xRange_;
    protected SoTRange yRange_;
    protected Range2D zRange_;

    public VMapData() {
      id_ = null;
      title_ = null;
      xMeta_ = null;
      yMeta_ = null;
    }

    public VMapData(String id, String title,
                SGTMetaData xMeta, SGTMetaData yMeta) {
      id_ = id;
      title_ = title;
      xMeta_ = xMeta;
      yMeta_ = yMeta;
    }

    public VMapData(String id, String title,
                SGTMetaData xMeta, SGTMetaData yMeta, SGTMetaData zMeta) {
      id_ = id;
      title_ = title;
      xMeta_ = xMeta;
      yMeta_ = yMeta;
      zMeta_ = zMeta;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
      changes_.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
      changes_.removePropertyChangeListener(l);
    }

    public void setTitle(String title) {
      if(title_ == null || !title_.equals(title)) {
        title_ = title;
        firePropertyChange();
      }
    }

    public String getTitle() {
      return title_;
    }

    public SGLabel getKeyTitle() {
      return keyTitle_;
    }

    public void setKeyTitle(SGLabel title) {
      keyTitle_ = title;
    }

    public void setId(String id) {
      id_ = id;
    }

    public String getId() {
      return id_;
    }

    public boolean isXTime() {
      return xTime_;
    }

    public boolean isYTime() {
      return yTime_;
    }

    public boolean isZTime() {
      return zTime_;
    }

    public void setXMetaData(SGTMetaData meta) {
      xMeta_ = meta;
    }

    public SGTMetaData getXMetaData() {
      return xMeta_;
    }

    public void setYMetaData(SGTMetaData meta) {
      yMeta_ = meta;
    }

    public SGTMetaData getYMetaData() {
      return yMeta_;
    }

    public void setZMetaData(SGTMetaData meta) {
      zMeta_ = meta;
    }

    public SGTMetaData getZMetaData() {
      return zMeta_;
    }

    public SoTRange getXRange() {
      return xRange_.copy();
    }

    public SoTRange getYRange() {
      return yRange_.copy();
    }

    public Range2D getZRange() {
      return zRange_;
    }

    public void setBatch(boolean batch) {
      inBatch_ = batch;
      if(!batch && changeMade_) {
        firePropertyChange();
      }
    }

    public void firePropertyChange() {
      changeMade_ = true;
      if(inBatch_) {
        return;
      }
      changes_.firePropertyChange("dataModified", new Integer(0), new Integer(1)); // dummy out the new old
      changes_.firePropertyChange("rangeModified", new Integer(0),
                                  new Integer(1));
      changeMade_ = false;
    }

    public abstract SGTData copy();
  }
