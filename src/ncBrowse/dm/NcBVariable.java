/**
 *  $Id: NcBVariable.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

import ncBrowse.sgt.geom.SoTRange;
//import gov.noaa.pmel.util.SoTRange;
import ncBrowse.sgt.geom.GeoDate;
import ncBrowse.sgt.geom.GeoDateArray;

import java.util.List;

//import gov.noaa.pmel.util.GeoDateArray;
//import gov.noaa.pmel.util.GeoDate;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public abstract class NcBVariable implements VarOrDim {
  protected NcBDataItem source_ = null;
  /**
   * True if EPIC_CODE is 624
   */
  private boolean is624_ = false;
  /**
   * non_null if EPIC_CODE is 624
   */
  private int[] time2_ = null;
  /**
   * Reference time.  Long.MIN_VALUE indicates a missing value.
   */
  private long refTime_ = Long.MIN_VALUE;
  /**
   * True if variable is  time
   */
  private boolean isTime_ = false;
  /**
   * Increment current GeoDate  by SECONDS, MINUTES, HOURS, DAYS, MONTHS, or YEARS
   */
  private int increment_ = GeoDate.SECONDS;
  static String tFormat_ = "yyyy-MM-dd HH:mm:ss";

  public NcBVariable(NcBDataItem source) {
    source_ = source;
  }

  public NcBDataItem getDataItem() {
    return source_;
  }

  public abstract NcBAttribute findAttribute(String attname);
  public abstract List getDimensions();
  public abstract long getSize();
  public abstract int getRank();
  public abstract int[] getShape();
  public abstract Object getValue(int index);
  public abstract double[] getDoubleArray(int[] origin, int[] shape, boolean transpose, int xindex, int yindex);
  public abstract GeoDateArray getGeoDateArray(int index, int[] origin, int[] shape);

  protected GeoDateArray createGeoDateArray(Object time){
    GeoDate[] tarray = null;
    int len = 0;
    if(time instanceof long[]) {
      len = ((long[])time).length;
      tarray = new GeoDate[len];
      for(int j=0; j < len; j++) {
        if(is624_) {
          tarray[j] = new GeoDate((int)((long[])time)[j], time2_[j]);
        } else {
          tarray[j] = new GeoDate(refTime_);
          tarray[j].increment((double)((long[])time)[j],
                              increment_);
        }
      }
    } else if(time instanceof int[]) {
      len = ((int[])time).length;
      tarray = new GeoDate[len];
      for(int j=0; j < len; j++) {
        if(is624_) {
          tarray[j] = new GeoDate(((int[])time)[j], time2_[j]);
        } else {
          tarray[j] = new GeoDate(refTime_);
          tarray[j].increment((double)((int[])time)[j],
                              increment_);
        }
      }
    } else if(time instanceof short[]) {
      len = ((short[])time).length;
      tarray = new GeoDate[len];
      for(int j=0; j < len; j++) {
        if(is624_) {
          tarray[j] = new GeoDate(((short[])time)[j], time2_[j]);
        } else {
          tarray[j] = new GeoDate(refTime_);
          tarray[j].increment((double)((short[])time)[j],
                              increment_);
        }
      }
    } else if(time instanceof float[]) {
      len = ((float[])time).length;
      tarray = new GeoDate[len];
      for(int j=0; j < len; j++) {
        tarray[j] = new GeoDate(refTime_);
        tarray[j].increment((double)((float[])time)[j],
                            increment_);
      }
    } else if(time instanceof double[]) {
      len = ((double[])time).length;
      tarray = new GeoDate[len];
      for(int j=0; j < len; j++) {
        tarray[j] = new GeoDate(refTime_);
        tarray[j].increment(((double[])time)[j], increment_);
      }
    }
    return new GeoDateArray(tarray);
  }

  protected SoTRange computeRange(double[] array) {
    double start = Double.POSITIVE_INFINITY;
    double end = Double.NEGATIVE_INFINITY;
    int count = 0;
    for (double anArray : array) {
      if (!Double.isNaN(anArray)) {
        start = Math.min(start, anArray);
        end = Math.max(end, anArray);
        count++;
      }
    }
    if(count == 0) {
      return new SoTRange.Double(Double.NaN, Double.NaN);
    } else {
      return new SoTRange.Double(start, end);
    }
  }

  protected SoTRange computeRange(GeoDateArray array) {
    long start = Long.MAX_VALUE;
    long end = Long.MIN_VALUE;
    long value;
    long[] val = array.getTime();
    int count = 0;
    for (long aVal : val) {
      value = aVal;
      if (!(aVal == Long.MIN_VALUE)) {  // test for missing time
        start = Math.min(start, value);
        end = Math.max(end, value);
        count++;
      }
    }
    if(count == 0) {
      return new SoTRange.Time(Long.MIN_VALUE, Long.MIN_VALUE);
    } else {
      return new SoTRange.Time(start, end);
    }
  }

  protected Object getValue(Object array, Object val) {
    int index = getIndex(array, val);
    if(val instanceof Long) {
      return ((long[]) array)[index];
    } else if(val instanceof Integer) {
      return ((int[]) array)[index];
    } else if(val instanceof Short) {
      return ((short[]) array)[index];
    } else if(val instanceof Float) {
      return ((float[]) array)[index];
    } else if(val instanceof Double) {
      return ((double[]) array)[index];
    } else if(val instanceof GeoDate) {
      GeoDateArray tarray = createGeoDateArray(array);
      return tarray.getGeoDate(index);
    }
    return null;
  }

  private int getIndex(Object array, Object val) {
    boolean arrayIsReversed = false;
    int len;
    int index = 0;
    if(val instanceof Long) {
      //
      // values are long
      //
      long[] longArray = (long[])array;
      long value = (Long) val;
      len = longArray.length;
      arrayIsReversed = longArray[len-1] < longArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= longArray[0]) {
            index = 0;
          } else if(value >= longArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value >= longArray[j] && value <= longArray[j+1]) {
                if((value - longArray[j]) < (longArray[j+1] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= longArray[0]) {
            index = 0;
          } else if(value <= longArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value <= longArray[j] && value >= longArray[j+1]) {
                if((value - longArray[j+1]) > (longArray[j] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Integer) {
      //
      // values are int
      //
      int[] intArray = (int[])array;
      int value = (Integer) val;
      len = intArray.length;
      arrayIsReversed = intArray[len-1] < intArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= intArray[0]) {
            index = 0;
          } else if(value >= intArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value >= intArray[j] && value <= intArray[j+1]) {
                if((value - intArray[j]) < (intArray[j+1] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= intArray[0]) {
            index = 0;
          } else if(value <= intArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value <= intArray[j] && value >= intArray[j+1]) {
                if((value - intArray[j+1]) > (intArray[j] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Short) {
      //
      // values are short
      //
      short[] shortArray = (short[])array;
      short value = (Short) val;
      len = shortArray.length;
      arrayIsReversed = shortArray[len-1] < shortArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= shortArray[0]) {
            index = 0;
          } else if(value >= shortArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value >= shortArray[j] && value <= shortArray[j+1]) {
                if((value - shortArray[j]) < (shortArray[j+1] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= shortArray[0]) {
            index = 0;
          } else if(value <= shortArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value <= shortArray[j] && value >= shortArray[j+1]) {
                if((value - shortArray[j+1]) > (shortArray[j] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Float) {
      //
      // values are float
      //
      float[] floatArray = (float[])array;
      float value = (Float) val;
      len = floatArray.length;
      arrayIsReversed = floatArray[len-1] < floatArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= floatArray[0]) {
            index = 0;
          } else if(value >= floatArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value >= floatArray[j] && value <= floatArray[j+1]) {
                if((value - floatArray[j]) < (floatArray[j+1] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= floatArray[0]) {
            index = 0;
          } else if(value <= floatArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value <= floatArray[j] && value >= floatArray[j+1]) {
                if((value - floatArray[j+1]) > (floatArray[j] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Double) {
      //
      // values are double
      //
      double[] doubleArray = (double[])array;
      double value = (Double) val;
      len = doubleArray.length;
      arrayIsReversed = doubleArray[len-1] < doubleArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= doubleArray[0]) {
            index = 0;
          } else if(value >= doubleArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value >= doubleArray[j] && value <= doubleArray[j+1]) {
                if((value - doubleArray[j]) < (doubleArray[j+1] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= doubleArray[0]) {
            index = 0;
          } else if(value <= doubleArray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if(value <= doubleArray[j] && value >= doubleArray[j+1]) {
                if((value - doubleArray[j+1]) > (doubleArray[j] - value)) {
                  index = j;
                } else {
                  index = j+1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof GeoDate) {
      //
      // values are GeoDate
      //
      GeoDate start;
      GeoDate end;
      Object time = array;
      long value = ((GeoDate)val).getTime();
      GeoDateArray gtarray = createGeoDateArray(time);
      long[] tarray = gtarray.getTime();
      len = tarray.length;

      if(len == 1) {
        index = 0;
      } else {
        arrayIsReversed = tarray[len-1] < tarray[0];
        if(!arrayIsReversed) {
          if(value < tarray[0] || value == tarray[0]) {
            index = 0;
          } else if(value > tarray[len-1] || value == tarray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if((value > tarray[j] || value == tarray[j]) &&
                 (value < tarray[j+1] || value == tarray[j+1])) {
              if((value-tarray[j]) < (tarray[j+1]-value)) {
                index = j;
              } else {
                index = j+1;
              }
              break;
            }
            }
          }
        } else {
          if(value > tarray[0] || value == tarray[0]) {
            index = 0;
          } else if(value < tarray[len-1] || value == tarray[len-1]){
            index = len-1;
          } else {
            for(int j=0; j < len-1; j++) {
              if((value < tarray[j] || value == tarray[j]) &&
                 (value > tarray[j+1] || value == tarray[j+1])) {
                if((value-tarray[j+1]) > (tarray[j]-value)) {
                  index = j;
                } else {
                  index = j+1;
                }
              }
            }
          }
        }
      }

    }
    return index;
  }

  protected boolean is624() {
    return is624_;
  }

  protected void set624(boolean is624) {
    is624_ = is624;
  }

  protected long getRefTime() {
    return refTime_;
  }

  protected void setRefTime(long refTime) {
    refTime_ = refTime;
  }

  protected int[] getTime2() {
    return time2_;
  }

  protected void setTime2(int[] time2) {
    time2_ = time2;
  }

  public boolean isTime() {
    return isTime_;
  }

  protected void setTime(boolean isTime) {
    isTime_ = isTime;
  }

  protected int getIncrement() {
    return increment_;
  }

  protected void setIncrement(int increment) {
    increment_ = increment;
  }

  protected long computeTime(long ref, double val, int unit) {
    switch(unit) {
    case GeoDate.MSEC:
      return ref + (long)val;
    case GeoDate.SECONDS:
      return ref + (long)(val*1000);
    case GeoDate.MINUTES:
      return ref + (long)(val*60000);
    case GeoDate.HOURS:
      return ref + (long)(val*3600000);
    case GeoDate.DAYS:                          // days and fraction days
      return ref + (long)(val*GeoDate.MSECS_IN_DAY);
    case GeoDate.MONTHS:                        // nearest day
    case GeoDate.YEARS:                         // nearest day
      GeoDate date = new GeoDate(ref);
      return date.increment(val, unit).getTime();
    }
    return Long.MAX_VALUE;
  }
}
