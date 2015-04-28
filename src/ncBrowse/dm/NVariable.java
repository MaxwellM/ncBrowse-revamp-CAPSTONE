/**
 *  $Id: NVariable.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;
import ncBrowse.sgt.geom.GeoDateArray;
//import gov.noaa.pmel.util.GeoDateArray;
import ncBrowse.sgt.geom.IllegalTimeValue;
//import gov.noaa.pmel.util.IllegalTimeValue;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public class NVariable extends NcBVariable {
  private Variable var_ = null;
  private List varAttr_ = null;
  private List dims_ = null;

  public NVariable(NcBDataItem source, Variable var) {
    super(source);
    var_ = var;
    testForTime();
    varAttr_ = new Vector();
//    Iterator attrI = var_.getAttributeIterator();
    //    while(attrI.hasNext()) {
    varAttr_.addAll(var_.getAttributes().stream().map(NAttribute::new).collect(Collectors.toList()));
    dims_ = new Vector();
    Iterator dimI = var_.getDimensions().iterator();
    while(dimI.hasNext()) {
      Iterator sourceDimI = source_.getDimensionIterator();
      Dimension vdim = (Dimension)sourceDimI.next();
      while(sourceDimI.hasNext()) {
        NDimension sdim = (NDimension)sourceDimI.next();
        if(vdim == sdim.getDimension()) dims_.add(sdim);
      }
    }
  }

  public List getDimensions() {
    return dims_;
  }

  public long getSize() {
    return var_.getSize();
  }

  public GeoDateArray getGeoDateArray(int index, int[] origin, int[] shape) {
    long[] timeArray = new long[shape[index]];
    int[] time2 = null;
    long refTime = Long.MAX_VALUE;
    int increment = GeoDate.SECONDS;
    if(is624()) {
      time2 = getTime2();
    } else {
      refTime = getRefTime();
      increment = getIncrement();
    }
    Object array = null;
    int out = 0;
    int len;
    try {
      array = var_.read().copyTo1DJavaArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(array instanceof long[]) {
      long[] time = (long[])array;
      len = origin[index] + shape[index];
      for(int j=origin[index]; j < len; j++, out++) {
        if(is624()) {
          timeArray[out] = (new GeoDate((int)time[j], time2[j])).getTime();
        } else {
          timeArray[out] = computeTime(refTime, time[j], increment);
        }
      }
    } else if(array instanceof int[]) {
      int[] time = (int[])array;
      len = origin[index] + shape[index];
      for(int j=origin[index]; j < len; j++, out++) {
        if(is624()) {
          timeArray[out] = (new GeoDate(time[j], time2[j])).getTime();
        } else {
          timeArray[out] = computeTime(refTime, time[j], increment);
        }
      }
    } else if(array instanceof short[]) {
      short[] time = (short[])array;
      len = origin[index] + shape[index];
      for(int j=origin[index]; j < len; j++, out++) {
        if(is624()) {
          timeArray[out] = (new GeoDate(time[j], time2[j])).getTime();
        } else {
          timeArray[out] = computeTime(refTime, time[j], increment);
        }
      }
    } else if(array instanceof float[]) {
      float[] time = (float[])array;
      len = origin[index] + shape[index];
      for(int j=origin[index]; j < len; j++, out++) {
        timeArray[out] = computeTime(refTime, time[j], increment);
      }
    } else if(array instanceof double[]) {
      double[] time = (double[])array;
      len = origin[index] + shape[index];
      for(int j=origin[index]; j < len; j++, out++) {
        timeArray[out] = computeTime(refTime, time[j], increment);
      }
    }
    return new GeoDateArray(timeArray);
  }

  public String getLongName() {
    // look for long_name attribute
    Attribute attr = var_.findAttribute("long_name");
    if(attr != null && attr.isString()) {
      return attr.getStringValue();
    }
    return null;
  }

  public double[] getDoubleArray(int[] origin, int[] shape, boolean transpose, int xIndex, int yIndex) {
    double[] outArray = null;
    double temp;
    Attribute validMinAttr;
    Attribute validMaxAttr;
    Attribute scaleAttr;
    Attribute offsetAttr;
    Attribute missingAttr;
    Attribute fillValueAttr;
    Array marr = null;

    try {
      if(transpose) {
        marr = var_.read(origin, shape).transpose(xIndex, yIndex);
      } else {
        marr = var_.read(origin, shape);
      }
    } catch (IOException | InvalidRangeException ex) {
      ex.printStackTrace();
    }
    //
    // try to get important attributes
    //
    validMinAttr = var_.findAttribute("valid_min");
    validMaxAttr = var_.findAttribute("valid_max");
    scaleAttr = var_.findAttribute("scale_factor");
    offsetAttr = var_.findAttribute("add_offset");
    missingAttr = var_.findAttribute("missing_value");
    fillValueAttr = var_.findAttribute("_FillValue");
    //
    // determine scale and offset, if any
    //
    double scale = 1.0;
    double offset = 0.0;
    boolean scale_offset = false;
    if(scaleAttr != null && !scaleAttr.isString()) {
      scale = scaleAttr.getNumericValue().doubleValue();
      scale_offset = true;
    }
    if(offsetAttr != null && !offsetAttr.isString()) {
      offset = offsetAttr.getNumericValue().doubleValue();
      scale_offset = true;
    }
    //
    // determine missing value, if any
    //
    Number missingNumber = null;
    Number validMinNumber = null;
    Number validMaxNumber = null;
    boolean valids = false;
    boolean defaultMissing = true;
    if(missingAttr != null && !missingAttr.isString()) {
      missingNumber = missingAttr.getNumericValue();
      defaultMissing = false;
    } else if(fillValueAttr != null && !fillValueAttr.isString()) {
      missingNumber = fillValueAttr.getNumericValue();
      defaultMissing = false;
    }
    if(validMinAttr != null && validMaxAttr != null &&
       !validMinAttr.isString() && !validMaxAttr.isString()) {
      valids = true;
      validMinNumber = validMinAttr.getNumericValue();
      validMaxNumber = validMaxAttr.getNumericValue();
    }
//    String aType = var_.getElementType().getName();
    DataType aType = var_.getDataType();
    if(aType == DataType.INT) {
      int missing = Integer.MIN_VALUE;
      int validMin = 0;
      int validMax = 0;
      int[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.intValue();
      }
      if(valids) {
        validMin = validMinNumber.intValue();
        validMax = validMaxNumber.intValue();
      }
      array =(int[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j=0; j < array.length; j++) {
          temp = array[j];
          if(scale_offset) {
            outArray[j] = temp*scale + offset;
          } else {
            outArray[j] = temp;
          }
          if(valids) {
            if(outArray[j] < validMin || outArray[j] > validMax) {
              outArray[j] = Double.NaN;
            }
          }
        }
      } else {
        for(int j=0; j < array.length; j++) {
          temp = array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp*scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    } else if(aType == DataType.SHORT) {
      short missing = Short.MIN_VALUE;
      short validMin = 0;
      short validMax = 0;
      short[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.shortValue();
      }
      if(valids) {
        validMin = validMinNumber.shortValue();
        validMax = validMaxNumber.shortValue();
      }
      array =(short[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j=0; j < array.length; j++) {
          temp = (double)array[j];
          if(scale_offset) {
            outArray[j] = temp*scale + offset;
          } else {
            outArray[j] = temp;
          }
          if(valids) {
            if(outArray[j] < validMin || outArray[j] > validMax) {
              outArray[j] = Double.NaN;
            }
          }
        }
      } else {
        for(int j=0; j < array.length; j++) {
          temp = (double)array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp*scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    } else if(aType == DataType.FLOAT) {
      float missing = 0.9e35f;
      float validMin = 0.0f;
      float validMax = 0.0f;
      float[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.floatValue();
      }
      if(valids) {
        validMin = validMinNumber.floatValue();
        validMax = validMaxNumber.floatValue();
      }
      array =(float[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j=0; j < array.length; j++) {
          temp = (double)array[j];
          if(temp >= missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp*scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      } else {
        for(int j=0; j < array.length; j++) {
          temp = (double)array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp*scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    } else if(aType == DataType.DOUBLE) {
      double missing = 0.9e35;
      double validMin = 0.0;
      double validMax = 0.0;
      double[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.doubleValue();
      }
      if(valids) {
        validMin = validMinNumber.doubleValue();
        validMax = validMaxNumber.doubleValue();
      }
      array =(double[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j=0; j < array.length; j++) {
          temp = array[j];
          if(temp >= missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp*scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      } else {
        for(int j=0; j < array.length; j++) {
          temp = array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp*scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    }
    return outArray;
  }

  public String getName() {
    return var_.getName();
  }

  public int[] getShape() {
    return var_.getShape();
  }

  public Object getValue(int index) {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getValue() not yet implemented.");
  }

  public NcBAttribute findAttribute(String attname) {
    Iterator attrI = varAttr_.iterator();
    NcBAttribute attr = null;
    while(attrI.hasNext()) {
      attr = (NcBAttribute)attrI.next();
      if(attr.getName().equals(attname)) return attr;
    }
    return null;
  }

  public int getRank() {
    return var_.getRank();
  }

  private boolean isVariableTime() {
    Attribute attr;
    Attribute epic_code;
    attr = var_.findAttribute("units");
    if(attr != null) {
      if(attr.getStringValue().contains("since")) return true;
    }
    epic_code = var_.findAttribute("epic_code");
    if(epic_code != null) {
      if(epic_code.getNumericValue().intValue() == 624) return true;
    }
    return false;
  }

  private void testForTime() {
    boolean is624 = false;
    boolean isTime = false;
    int[] time2 = null;
    int increment = GeoDate.SECONDS;
    long refTime = Long.MIN_VALUE;
    GeoDate refDate = null;
    Attribute unitsAttr;
    Attribute epicAttr;
    String ref;
    String units;
    int pos;

    setTime(false);

    unitsAttr = var_.findAttribute("units");
    epicAttr = var_.findAttribute("epic_code");

    if((unitsAttr != null) &&
       ((pos = unitsAttr.getStringValue().indexOf("since")) != -1)) {
      //
      // if the units include "since" assume time
      //
      setTime(true);
      set624(false);
      units = unitsAttr.getStringValue();
      ref = units.substring(pos + 5).trim();
      int len = Math.min(ref.length(), tFormat_.length());
      try {
        refDate = new GeoDate(ref, tFormat_.substring(0, len));
        setRefTime(refDate.getTime());
      } catch (IllegalTimeValue e) {
        System.out.println(e.toString());
        try {
          refDate = new GeoDate("1970-01-01 00:00:00",
                                "yyyy-MM-dd HH:mm:ss");
          setRefTime(refDate.getTime());
          } catch (IllegalTimeValue ignored) {}
          System.out.println("   Setting default reference date: " +
                             refDate.toString());
      }

      if(units.contains("second")) {
        increment = GeoDate.SECONDS;
      } else if(units.contains("min")) {
        increment = GeoDate.MINUTES;
      } else if(units.contains("hour")) {
        increment = GeoDate.HOURS;
      } else if(units.contains("day")) {
        increment = GeoDate.DAYS;
      } else if(units.contains("month")) {
        increment = GeoDate.MONTHS;
      } else if(units.contains("year")) {
        increment = GeoDate.YEARS;
      } else if(units.contains("msec")) {
        increment = GeoDate.MSEC;
      } else {
        increment = GeoDate.SECONDS;
      }
      setIncrement(increment);

    } else if(epicAttr != null &&
              (epicAttr.getNumericValue().intValue() == 624)) {
      //
      // if the epic_code is 624 it's time
      //
      setTime(true);
      set624(true);
      //
      // time is a double integer
      //
      Variable vtime2 = ((NDataItem)source_).getDataItem().findVariable(var_.getName() + "2");
      try {
        Array arr = vtime2.read();
        time2 = (int[])arr.copyTo1DJavaArray();
        setTime2(time2);
      } catch (IOException e) {
        System.out.println(e.toString());
      }
    }
  }
}
