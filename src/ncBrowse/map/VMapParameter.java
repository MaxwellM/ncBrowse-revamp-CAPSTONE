/**
 *  $Id: VMapParameter.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import gov.noaa.pmel.util.GeoDate;
import gov.noaa.pmel.util.SoTRange;
import gov.noaa.pmel.util.SoTValue;
import ncBrowse.Debug;
import ncBrowse.NcFile;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * VMap parameter contains values that have a parametric relationship in a VMapModel.
 *
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.21 $, $Date: 2004/03/05 21:38:37 $
 */
public class VMapParameter implements Comparable {
  /**
   * True if this parameter can be a range
   */
  private boolean rangeAllowed_;
  /**
   * True if this parameter is sin
   */
  private boolean single_;
  /**
   * Group number.  For grouped parameters.  At least one of the group must
   * have a range.
   */
  private int group_;
  private int length_;
  private final Object element_;
  private Object values_;
  private SoTRange range_;
  private SoTRange fullRange_ = null;
  private final boolean dimension_;
  private boolean time_;
  /**
   * @label ncFile_
   */
  private final NcFile ncFile_;
  private final Vector changeListeners_ = new Vector();
  private final ChangeEvent event_ = new ChangeEvent(this);

  /**
   * VMapParameter constuctor.
   *
   * @param ncFile netCDF file
   * @param elem parameter element, either Dimension or Variable
   * @param rangeAllowed true if the parameter can be a range
   */
  public VMapParameter(NcFile ncFile, Object elem, boolean rangeAllowed) {
    ncFile_ = ncFile;
    element_ = elem;
    rangeAllowed_ = rangeAllowed;
    single_ = !rangeAllowed_;
    dimension_ = element_ instanceof Dimension;
    computeFullRange();
    range_ = getFullRange();
    if(dimension_) {
      values_ = new int[((Dimension)element_).getLength()];
      for(int i=0; i < ((int[])values_).length; i++) {
        ((int[])values_)[i] = i;
      }
    } else {
      try {
        values_ = ((Variable)element_).read().copyTo1DJavaArray();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public int compareTo(Object obj) {
    if(obj instanceof VMapParameter) {
      VMapParameter param = (VMapParameter)obj;
      if(rangeAllowed_) {
        if(!param.isRangeAllowed()) return -1;
      } else {
        if(param.isRangeAllowed()) return 1;
      }
      if(group_ > param.getGroup()) return 1;
      if(group_ < param.getGroup()) return -1;
      return getName().compareTo(param.getName());
    }
    throw new ClassCastException();
  }

  /**
   * Get parameter values
   *
   * @return Dimension or Variable data values
   */
  public Object getValues() {
    return values_;
  }

  /**
   * Is range allowed for parameter?
   *
   * @return true if range is allowd
   */
  public boolean isRangeAllowed(){
    return rangeAllowed_;
  }

  /**
   * Set range allowed or not allowed.  If range is not allowed parameter
   * can only have a single point as its value.
   *
   * @param rangeAllowed true if range is allowd
   */
  public void setRangeAllowed(boolean rangeAllowed){
    // don't track for ChangeEvent
    rangeAllowed_ = rangeAllowed;
    single_ = !rangeAllowed_;
  }

  /**
   * Set parameter's group membership
   *
   * @param group parameter group
   */
  public void setGroup(int group) {
    // don't track for ChangeEvent
    group_ = group;
  }

  /**
   * Get parameters group membership
   * @return group number
   */
  public int getGroup() {
    return group_;
  }

  /**
   * Turn single mode on/off.  If range is not allowed single is always
   * true.  If range is allowed then optionally single can be set.
   *
   * @param single true if only single value is to be used
   */
  public void setSingle(boolean single) {
    if(rangeAllowed_ && single_ != single) {
      single_ = single;
      fireChangeEvent();
    }
  }

  /**
   * Is single mode set?
   * @return true if single mode on
   */
  public boolean isSingle() {
    return length_ == 1 || single_;
  }

  /**
   * Get the parameter.
   *
   * @return get parameter Dimension or Variable
   */
  public Object getElement() {
    return element_;
  }

  /**
   * Set the parameter range.
   * @param range parameter's range
   */
  public void setSoTRange(SoTRange range) {
    if(!range_.equals(range)) {
      range_ = range;
      fireChangeEvent();
    }
  }

  /**
   * Get the parameter range.
   *
   * @return range
   */
  public SoTRange getSoTRange() {
    SoTRange range = range_.copy();
//      if(isSingle()) {
//        range.setEnd(range.getStart());
//      }
    return range;
  }

  /**
   * Set the paramgeter value.  Sets the range start=end=value.
   * @param value single value
   */
  public void setSoTValue(SoTValue value) {
    SoTValue start = range_.getStart();
    if(!start.equals(value)) {
      range_.setStart(value);
      range_.setEnd(value);
      fireChangeEvent();
    }
  }

  /**
   * Set parameter range to value at index.  start=end=array[index]
   * @param index value index
   */
  public void setValueIndex(int index) {
    int ind = index;
    if(ind < 0) ind = 0;
    if(ind >= length_) ind = length_ - 1;
    SoTValue sValue = null;
    if(dimension_) {
      sValue = new SoTValue.Integer(ind);
      if(Debug.DEBUG)System.out.println(" index = " + ind + ", value = " + ind);
    } else {
      Variable ncVar = (Variable)element_;
      Object value = ncFile_.getArrayValue(ncVar, ind);
      if(value instanceof GeoDate) {
        sValue = new SoTValue.Time((GeoDate)value);
      } else if(value instanceof Long) {
        sValue = new SoTValue.Long(((Long)value).longValue());
      } else if(value instanceof Integer) {
        sValue = new SoTValue.Integer(((Integer)value).intValue());
      } else if(value instanceof Short) {
        sValue = new SoTValue.Short(((Short)value).shortValue());
      } else if(value instanceof Float) {
        sValue = new SoTValue.Float(((Float)value).floatValue());
      } else if(value instanceof Double) {
        sValue = new SoTValue.Double(((Double)value).doubleValue());
      }
      if(Debug.DEBUG)System.out.println(" index = " + ind + ", value = " + value);
    }
    SoTValue start = range_.getStart();
    if(!start.equals(sValue)) {
      range_.setStart(sValue);
      range_.setEnd(sValue);
      fireChangeEvent();
    }
  }

  /**
   * Get paramter start range.
   *
   * @return value
   */
  public SoTValue getSoTValue() {
    return range_.getStart();
  }

  /**
   * Get Dimension or Variable name.
   *
   * @return name
   */
  public String getName() {
    if(dimension_) {
      return ((Dimension)element_).getName();
    } else {
      return ((Variable)element_).getName();
    }
  }

  /**
   * Get unit string.
   *
   * @return units
   */
  public String getUnits() {
    Attribute attr;
    if(!dimension_) {
      attr = ((Variable)element_).findAttribute("units");
      if(attr != null) return attr.getStringValue();
    }
    return "";
  }

  /**
   * Is parameter time?
   *
   * @return true if parameter is time
   */
  public boolean isTime() {
    return time_;
  }

  /**
   * Is parameter a Dimension?
   *
   * @return true if parameter is a Dimension
   */
  public boolean isDimension() {
    return dimension_;
  }

  /**
   * Is parameter a Variable?
   * @return true if parameter is a Variable
   */
  public boolean isVariable() {
    return !dimension_;
  }

  /**
   * Get the complete range for the parameter.
   *
   * @return full parameter range
   */
  public SoTRange getFullRange() {
    return fullRange_.copy();
  }

  private void computeFullRange() {
    if(fullRange_ != null) return;
    if(dimension_) {
      time_ = false;
      length_ = ((Dimension)element_).getLength();
      fullRange_ = new SoTRange.Integer(0, length_-1, 1);
    } else {
      Variable ncVar = (Variable)element_;
      time_ = ncFile_.isVariableTime(ncVar);
      length_ = (int) ncVar.getSize();
      Object start = ncFile_.getArrayValue(ncVar, 0);
      Object end = ncFile_.getArrayValue(ncVar, length_-1);
      if(start instanceof GeoDate) {
        fullRange_ = new SoTRange.Time((GeoDate)start, (GeoDate)end);
      } else if(start instanceof Long) {
        fullRange_ = new SoTRange.Long(((Long)start).longValue(),
                                       ((Long)end).longValue());
      } else if(start instanceof Integer) {
        fullRange_ = new SoTRange.Integer(((Integer)start).intValue(),
                                          ((Integer)end).intValue());
      } else if(start instanceof Short) {
        fullRange_ = new SoTRange.Short(((Short)start).shortValue(),
                                        ((Short)end).shortValue());
      } else if(start instanceof Float) {
        fullRange_ = new SoTRange.Float(((Float)start).floatValue(),
                                        ((Float)end).floatValue());
      } else if(start instanceof Double) {
        fullRange_ = new SoTRange.Double(((Double)start).doubleValue(),
                                         ((Double)end).doubleValue());
      }
    }
  }

  /**
   * Get the parameter length.
   *
   * @return length
   */
  public int getLength() {
    return length_;
  }

  public String toString() {
    return getName() + " " + "(" + getUnits() + ") " + range_ + ", range=" + rangeAllowed_ + ", single=" + single_ + ", group=" + VMapModel.getTitle(group_);
  }

  public void addChangeListener(ChangeListener l) {
    changeListeners_.add(l);
  }

  public void removeChangeListener(ChangeListener l) {
    changeListeners_.remove(l);
  }

  private void fireChangeEvent() {
    for(Enumeration e = changeListeners_.elements(); e.hasMoreElements();) {
      ((ChangeListener)e.nextElement()).stateChanged(event_);
    }
  }
}
