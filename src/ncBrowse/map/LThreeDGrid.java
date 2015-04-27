/**
 *  $Id: LThreeDGrid.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import gov.noaa.pmel.sgt.dm.ThreeDGrid;

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
  public class LThreeDGrid extends ThreeDGrid {
    protected final PropertyChangeSupport changes_ = new PropertyChangeSupport(this);
    protected boolean inBatch_ = false;
    protected boolean changeMade_ = false;

    public LThreeDGrid() {}

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

    public void addPropertyChangeListener(PropertyChangeListener l) {
      changes_.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
      changes_.removePropertyChangeListener(l);
    }

  }
