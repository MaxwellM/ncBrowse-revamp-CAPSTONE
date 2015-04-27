/**
 *  $Id: SelectionRange.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTGrid;
import gov.noaa.pmel.sgt.dm.SGTLine;
import gov.noaa.pmel.sgt.dm.SGTMetaData;
import gov.noaa.pmel.util.GeoDate;
import ncBrowse.map.VMapGrid;
import ncBrowse.map.VMapLine;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.Vector;

/**
 * Defines the selection of a netCDF variable.
 *
 * @author Donald Denbo
 * @version $Revision: 1.30 $, $Date: 2006/02/15 22:33:23 $
 */
public class SelectionRange {
  /**
   * @label ncVar_
   */
  Variable ncVar_;
  Object[] dimOrVar_;
  Vector array_;
  Vector min_;
  Vector max_;
  boolean[] reverseAxis_;
  boolean[] arrayIsReversed_;
  boolean[] isXAxis_;
  boolean[] isYAxis_;
  boolean[] isTime_;
  int count_;
  boolean is624_;
  GeoDate refDate_;
  int increment_;
  int[] time2_;

  /**
   * @directed
   */
  /*#SimpleGrid lnkSimpleGrid;*/

  /**
   * @directed
   */
  /*#SimpleLine lnkSimpleLine;*/

  /**
   * Construct SelectionRange object.
   *
   * @param var netCDF Variable for range
   */
  public SelectionRange(Variable var) {
    ncVar_ = var;
    dimOrVar_ = new Object[var.getRank()];
    array_ = new Vector(var.getRank());
    min_ = new Vector(var.getRank());
    max_ = new Vector(var.getRank());
    reverseAxis_ = new boolean[var.getRank()];
    arrayIsReversed_ = new boolean[var.getRank()];
    isXAxis_ = new boolean[var.getRank()];
    isYAxis_ = new boolean[var.getRank()];
    isTime_ = new boolean[var.getRank()];
    count_ = 0;
    if(Debug.DEBUG) {
//      Class type = ncVar_.getElementType();
      DataType type = ncVar_.getDataType();
      //TODO how to find out it is an array?
      System.out.println("Variable " + ncVar_.getName() +
                         " isArray = " + "?????" +
                         ", Name = " + type.toString());
    }
  }

  /**
   * add an <code>int</code> dimension range
   *
   * @param dimOrVar netCDF Dimension or Variable
   * @param array 1-d data array
   * @param rev true if reversed
   * @param xAxis true if its a X axis
   * @param yAxis true if its a Y axis
   * @param min minimum value
   * @param max maximum value
   */
  public void addDimensionRange(Object dimOrVar, Object array,
                                boolean rev,
                                boolean xAxis, boolean yAxis,
                                int min, int max) {
    dimOrVar_[count_] = dimOrVar;
    array_.addElement(array);
    min_.addElement(min);
    max_.addElement(max);
    reverseAxis_[count_] = rev;
    int[] tmp = (int[]) array;
    arrayIsReversed_[count_] = tmp[tmp.length - 1] < tmp[0];
    isXAxis_[count_] = xAxis;
    isYAxis_[count_] = yAxis;
    isTime_[count_] = false;
    count_++;
  }

  /**
   * add an <code>int</code> dimension range
   *
   * @param dimOrVar netCDF Dimension or Variable
   * @param array 1-d data array
   * @param rev true if reversed
   * @param xAxis true if its a X axis
   * @param yAxis true if its a Y axis
   * @param min minimum value
   * @param max maximum value
   */
  public void addDimensionRange(Object dimOrVar, Object array,
                                boolean rev,
                                boolean xAxis, boolean yAxis,
                                byte min, byte max) {
    dimOrVar_[count_] = dimOrVar;
    array_.addElement(array);
    min_.addElement(min);
    max_.addElement(max);
    reverseAxis_[count_] = rev;
    int[] tmp = (int[]) array;
    arrayIsReversed_[count_] = tmp[tmp.length - 1] < tmp[0];
    isXAxis_[count_] = xAxis;
    isYAxis_[count_] = yAxis;
    isTime_[count_] = false;
    count_++;
  }

  /**
   * add a <code>short</code> dimension range
   *
   * @param dimOrVar netCDF Dimension or Variable
   * @param array 1-d data array
   * @param rev true if reversed
   * @param xAxis true if its a X axis
   * @param yAxis true if its a Y axis
   * @param min minimum value
   * @param max maximum value
   */
  public void addDimensionRange(Object dimOrVar, Object array,
                                boolean rev,
                                boolean xAxis, boolean yAxis,
                                short min, short max) {
    dimOrVar_[count_] = dimOrVar;
    array_.addElement(array);
    min_.addElement(min);
    max_.addElement(max);
    reverseAxis_[count_] = rev;
    short[] tmp = (short[]) array;
    arrayIsReversed_[count_] = tmp[tmp.length - 1] < tmp[0];
    isXAxis_[count_] = xAxis;
    isYAxis_[count_] = yAxis;
    isTime_[count_] = false;
    count_++;
  }

  /**
   * add a <code>float</code> dimension range
   *
   * @param dimOrVar netCDF Dimension or Variable
   * @param array 1-d data array
   * @param rev true if reversed
   * @param xAxis true if its a X axis
   * @param yAxis true if its a Y axis
   * @param min minimum value
   * @param max maximum value
   */
  public void addDimensionRange(Object dimOrVar, Object array,
                                boolean rev,
                                boolean xAxis, boolean yAxis,
                                float min, float max) {
    dimOrVar_[count_] = dimOrVar;
    array_.addElement(array);
    min_.addElement(min);
    max_.addElement(max);
    reverseAxis_[count_] = rev;
    float[] tmp = (float[]) array;
    arrayIsReversed_[count_] = tmp[tmp.length - 1] < tmp[0];
    isXAxis_[count_] = xAxis;
    isYAxis_[count_] = yAxis;
    isTime_[count_] = false;
    count_++;
  }

  /**
   * add a <code>double</code> dimension range
   *
   * @param dimOrVar netCDF Dimension or Variable
   * @param array 1-d data array
   * @param rev true if reversed
   * @param xAxis true if its a X axis
   * @param yAxis true if its a Y axis
   * @param min minimum value
   * @param max maximum value
   */
  public void addDimensionRange(Object dimOrVar, Object array,
                                boolean rev,
                                boolean xAxis, boolean yAxis,
                                double min, double max) {
    dimOrVar_[count_] = dimOrVar;
    array_.addElement(array);
    min_.addElement(min);
    max_.addElement(max);
    reverseAxis_[count_] = rev;
    double[] tmp = (double[]) array;
    arrayIsReversed_[count_] = tmp[tmp.length - 1] < tmp[0];
    isXAxis_[count_] = xAxis;
    isYAxis_[count_] = yAxis;
    isTime_[count_] = false;
    count_++;
  }

  /**
   * add a <code>GeoDate</code> dimension range
   *
   * @param dimOrVar netCDF Dimension or Variable
   * @param array 1-d data array
   * @param rev true if reversed
   * @param xAxis true if its a X axis
   * @param yAxis true if its a Y axis
   * @param min minimum value
   * @param max maximum value
   */
  public void addDimensionRange(Object dimOrVar, Object array,
                                boolean rev,
                                boolean xAxis, boolean yAxis,
                                GeoDate min, GeoDate max) {
    dimOrVar_[count_] = dimOrVar;
    array_.addElement(array);
    min_.addElement(min);
    max_.addElement(max);
    reverseAxis_[count_] = false;
    arrayIsReversed_[count_] = rev;
    isXAxis_[count_] = xAxis;
    isYAxis_[count_] = yAxis;
    isTime_[count_] = true;
    count_++;
  }

  /**
   * get the variable
   *
   * @return netCDF Variable object
   */
  public Variable getVariable() {
    return ncVar_;
  }

  /**
   * set the time format
   *
   * @param is624 true if epic_code=624
   * @param time2 millisecond array if epic_code=624
   * @param refDate reference date
   * @param inc increment value
   */
  public void setTimeFormat(boolean is624, int[] time2,
                            GeoDate refDate, int inc) {
    is624_ = is624;
    refDate_ = refDate;
    increment_ = inc;
    time2_ = time2;
  }

  /**
   * Test a dimension.
   *
   * @param index dimension number
   * @return true if dimension is a netCDF Variable
   */
  public boolean isDimensionVariable(int index) {
    return dimOrVar_[index] instanceof Variable;
  }

  /**
   * return a netCDF Variable if dimension is a netCDF variable otherwise
   * return NULL.
   *
   * @param index dimension number
   * @return netCDF variable
   */
  public Variable getDimensionVariable(int index) {
    if(isDimensionVariable(index)) {
      return(Variable) dimOrVar_[index];
    } else {
      return null;
    }
  }

  /**
   * Test if dimension is time
   * @param index dimension number
   * @return true if dimension is time
   */
  public boolean isTime(int index) {
    return index >= 0 && index < min_.size() && (min_.get(index) instanceof GeoDate);
  }

  /**
   * Test dimension.
   *
   * @param index dimension number
   * @return true if dimension is a X axis
   */
  public boolean isXAxis(int index) {
    return isXAxis_[index];
  }

  /**
   * Test dimension.
   *
   * @param index dimension index
   * @return true if dimension is a Y axis
   */
  public boolean isYAxis(int index) {
    return isYAxis_[index];
  }

  /**
   * Get netCDF origin computed from minimum dimension values.
   *
   * @return origin
   */
  public int[] getOrigin() {
    return getIndex(min_);
  }

  /**
   * Get netCDF shape computed from minimum and maximum dimension values.
   *
   * @return shape
   */
  public int[] getShape() {
    int[] shape = new int[ncVar_.getRank()];

    int[] minIndex = getIndex(min_);
    int[] maxIndex = getIndex(max_);

    for(int i = 0; i < ncVar_.getRank(); i++) {
      shape[i] = maxIndex[i] - minIndex[i] + 1;
    }
    return shape;
  }

  /**
   * Get variable rank.
   * @return rank
   */
  public int getRank() {
    return ncVar_.getRank();
  }

  /**
   * Get coordinate data values.
   *
   * @param valueIndex coordinate index
   * @return object array of coordinate values
   */
  public Object[] getValues(int[] valueIndex) {
    int index;
    Object[] value = new Object[valueIndex.length];
    for(int i = 0; i < valueIndex.length; i++) {
      index = valueIndex[i];
      Object array = array_.get(i);
      if(isTime_[i]) {
        GeoDate val = null;
        if(array instanceof long[]) {
          if(is624_) {
            val = new GeoDate((int) ((long[]) array)[index], time2_[index]);
          } else {
            val = new GeoDate(refDate_);
            val.increment((double) ((long[]) array)[index],
                          increment_);
          }
        } else if(array instanceof int[]) {
          if(is624_) {
            val = new GeoDate(((int[]) array)[index], time2_[index]);
          } else {
            val = new GeoDate(refDate_);
            val.increment((double) ((int[]) array)[index],
                          increment_);
          }
        } else if(array instanceof short[]) {
          if(is624_) {
            val = new GeoDate(((short[]) array)[index], time2_[index]);
          } else {
            val = new GeoDate(refDate_);
            val.increment((double) ((short[]) array)[index],
                          increment_);
          }
        } else if(array instanceof float[]) {
          val = new GeoDate(refDate_);
          val.increment((double) ((float[]) array)[index],
                        increment_);
        } else if(array instanceof double[]) {
          val = new GeoDate(refDate_);
          val.increment(((double[]) array)[index],
                        increment_);
        }
        value[i] = val;
      } else {
        if(array instanceof long[]) {
          value[i] = ((long[]) array)[index];
        } else if(array instanceof int[]) {
          value[i] = ((int[]) array)[index];
        } else if(array instanceof short[]) {
          value[i] = ((short[]) array)[index];
        } else if(array instanceof float[]) {
          value[i] = ((float[]) array)[index];
        } else if(array instanceof double[]) {
          value[i] = ((double[]) array)[index];
        }
      }
    }
    return value;
  }

  private int[] getIndex(Vector val) {
    int len;
    int[] index = new int[ncVar_.getRank()];
    for(int i = 0; i < ncVar_.getRank(); i++) {
      if(val.get(i) instanceof Long) {
        //
        // values are long
        //
        long[] longArray = (long[]) array_.get(i);
        long value = (Long) val.get(i);
        len = longArray.length;
        if(len == 1) {
          index[i] = 0;
        } else {
          if(!arrayIsReversed_[i]) {
            if(value <= longArray[0]) {
              index[i] = 0;
            } else if(value >= longArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value >= longArray[j] && value <= longArray[j + 1]) {
                  if((value - longArray[j]) < (longArray[j + 1] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          } else {
            if(value >= longArray[0]) {
              index[i] = 0;
            } else if(value <= longArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value <= longArray[j] && value >= longArray[j + 1]) {
                  if((value - longArray[j + 1]) > (longArray[j] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          }
        }
      } else if(val.get(i) instanceof Integer) {
        //
        // values are int
        //
        int[] intArray = (int[]) array_.get(i);
        int value = (Integer) val.get(i);
        len = intArray.length;
        if(len == 1) {
          index[i] = 0;
        } else {
          if(!arrayIsReversed_[i]) {
            if(value <= intArray[0]) {
              index[i] = 0;
            } else if(value >= intArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value >= intArray[j] && value <= intArray[j + 1]) {
                  if((value - intArray[j]) < (intArray[j + 1] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          } else {
            if(value >= intArray[0]) {
              index[i] = 0;
            } else if(value <= intArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value <= intArray[j] && value >= intArray[j + 1]) {
                  if((value - intArray[j + 1]) > (intArray[j] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          }
        }
      } else if(val.get(i) instanceof Short) {
        //
        // values are short
        //
        short[] shortArray = (short[]) array_.get(i);
        short value = (Short) val.get(i);
        len = shortArray.length;
        if(len == 1) {
          index[i] = 0;
        } else {
          if(!arrayIsReversed_[i]) {
            if(value <= shortArray[0]) {
              index[i] = 0;
            } else if(value >= shortArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value >= shortArray[j] && value <= shortArray[j + 1]) {
                  if((value - shortArray[j]) < (shortArray[j + 1] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          } else {
            if(value >= shortArray[0]) {
              index[i] = 0;
            } else if(value <= shortArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value <= shortArray[j] && value >= shortArray[j + 1]) {
                  if((value - shortArray[j + 1]) > (shortArray[j] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          }
        }
      } else if(val.get(i) instanceof Float) {
        //
        // values are float
        //
        float[] floatArray = (float[]) array_.get(i);
        float value = (Float) val.get(i);
        len = floatArray.length;
        if(len == 1) {
          index[i] = 0;
        } else {
          if(!arrayIsReversed_[i]) {
            if(value <= floatArray[0]) {
              index[i] = 0;
            } else if(value >= floatArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value >= floatArray[j] && value <= floatArray[j + 1]) {
                  if((value - floatArray[j]) < (floatArray[j + 1] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          } else {
            if(value >= floatArray[0]) {
              index[i] = 0;
            } else if(value <= floatArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value <= floatArray[j] && value >= floatArray[j + 1]) {
                  if((value - floatArray[j + 1]) > (floatArray[j] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          }
        }
      } else if(val.get(i) instanceof Double) {
        //
        // values are double
        //
        double[] doubleArray = (double[]) array_.get(i);
        double value = (Double) val.get(i);
        len = doubleArray.length;
        if(len == 1) {
          index[i] = 0;
        } else {
          if(!arrayIsReversed_[i]) {
            if(value <= doubleArray[0]) {
              index[i] = 0;
            } else if(value >= doubleArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value >= doubleArray[j] && value <= doubleArray[j + 1]) {
                  if((value - doubleArray[j]) < (doubleArray[j + 1] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          } else {
            if(value >= doubleArray[0]) {
              index[i] = 0;
            } else if(value <= doubleArray[len - 1]) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if(value <= doubleArray[j] && value >= doubleArray[j + 1]) {
                  if((value - doubleArray[j + 1]) > (doubleArray[j] - value)) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          }
        }
      } else if(val.get(i) instanceof GeoDate) {
        //
        // values are GeoDate
        //
        GeoDate start;
        GeoDate end;
        Object time = array_.get(i);
        GeoDate value = (GeoDate) val.get(i);
        GeoDate[] array = null;
        len = 0;
        if(time instanceof long[]) {
          len = ((long[]) time).length;
          array = new GeoDate[len];
          for(int j = 0; j < len; j++) {
            if(is624_) {
              array[j] = new GeoDate((int) ((long[]) time)[j], time2_[j]);
            } else {
              array[j] = new GeoDate(refDate_);
              array[j].increment((double) ((long[]) time)[j],
                                 increment_);
            }
          }
        } else
        if(time instanceof long[]) {
          len = ((long[]) time).length;
          array = new GeoDate[len];
          for(int j = 0; j < len; j++) {
            if(is624_) {
              array[j] = new GeoDate((int) ((long[]) time)[j], time2_[j]);
            } else {
              array[j] = new GeoDate(refDate_);
              array[j].increment((double) ((long[]) time)[j],
                                 increment_);
            }
          }
        } else if(time instanceof int[]) {
          len = ((int[]) time).length;
          array = new GeoDate[len];
          for(int j = 0; j < len; j++) {
            if(is624_) {
              array[j] = new GeoDate(((int[]) time)[j], time2_[j]);
            } else {
              array[j] = new GeoDate(refDate_);
              array[j].increment((double) ((int[]) time)[j],
                                 increment_);
            }
          }
        } else if(time instanceof short[]) {
          len = ((short[]) time).length;
          array = new GeoDate[len];
          for(int j = 0; j < len; j++) {
            if(is624_) {
              array[j] = new GeoDate(((short[]) time)[j], time2_[j]);
            } else {
              array[j] = new GeoDate(refDate_);
              array[j].increment((double) ((short[]) time)[j],
                                 increment_);
            }
          }
        } else if(time instanceof float[]) {
          len = ((float[]) time).length;
          array = new GeoDate[len];
          for(int j = 0; j < len; j++) {
            array[j] = new GeoDate(refDate_);
            array[j].increment((double) ((float[]) time)[j],
                               increment_);
          }
        } else if(time instanceof double[]) {
          len = ((double[]) time).length;
          array = new GeoDate[len];
          for(int j = 0; j < len; j++) {
            array[j] = new GeoDate(refDate_);
            array[j].increment(((double[]) time)[j], increment_);
          }
        }

        if(len == 1) {
          index[i] = 0;
        } else {
          arrayIsReversed_[i] = array[len - 1].before(array[0]);
          if(!arrayIsReversed_[i]) {
            if(value.before(array[0]) || value.equals(array[0])) {
              index[i] = 0;
            } else if(value.after(array[len - 1]) || value.equals(array[len - 1])) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if((value.after(array[j]) || value.equals(array[j])) &&
                   (value.before(array[j + 1]) || value.equals(array[j + 1]))) {
                  if(value.subtract(array[j]).before(array[j +
                      1].subtract(value))) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                  break;
                }
              }
            }
          } else {
            if(value.after(array[0]) || value.equals(array[0])) {
              index[i] = 0;
            } else if(value.before(array[len - 1]) ||
                      value.equals(array[len - 1])) {
              index[i] = len - 1;
            } else {
              for(int j = 0; j < len - 1; j++) {
                if((value.before(array[j]) || value.equals(array[j])) &&
                   (value.after(array[j + 1]) || value.equals(array[j + 1]))) {
                  if(value.subtract(array[j + 1]).after(array[j].subtract(value))) {
                    index[i] = j;
                  } else {
                    index[i] = j + 1;
                  }
                }
              }
            }
          }
        }

      }
    }
    return index;
  }

  public SGTData getDataModel() throws RankNotSupportedException {
    return getDataModel(null);
  }

  /**
   * Get the SGTData object, either SGTGrid or SGTLine, from the Variable.
   *
   * @return SGTData object
   * @throws RankNotSupportedException
   */
  public SGTData getDataModel(String id) throws RankNotSupportedException {
    int[] origin = getOrigin();
    int[] shape = getShape();
    int outRank = 0;
    for(int i = 0; i < ncVar_.getRank(); i++) {
      if(shape[i] != 1) {
        outRank++;
      }
    }
    if(outRank < 1 || outRank > 2) {
      throw new
          RankNotSupportedException();
    }
    if(outRank == 1) {
      return makeVMapLine(origin, shape, id);
    } else {
      return makeVMapGrid(origin, shape, id);
    }
  }

  private SGTLine makeVMapLine(int[] origin, int[] shape, String id) {
    VMapLine line;
    SGTMetaData xMetaData;
    SGTMetaData yMetaData;
    Attribute attr = null;
    String units;
    String name;
    StringBuffer title;

    int xIndex = -1;
    for(int i = 0; i < shape.length; i++) {
      if(shape[i] != 1) {
        xIndex = i;
        break;
      }
    }
    GeoDate[] timeArray = null;
    double[] xArray = null;
    if(isTime_[xIndex]) {
      timeArray = getTimeArray(array_.get(xIndex), xIndex,
                               origin, shape);
    } else {
      xArray = getDoubleArray(array_.get(xIndex), xIndex,
                              origin, shape);
    }
    float[] array = null;
    double[] yArray = getDoubleArray(ncVar_, origin, shape,
                                     xIndex, xIndex + 1);
    //
    // create attributes
    //
    if(dimOrVar_[xIndex] instanceof ucar.nc2.Dimension) {
      name = ((ucar.nc2.Dimension) dimOrVar_[xIndex]).getName();
      units = " ";
    } else {
      name = ((Variable) dimOrVar_[xIndex]).getName();
      attr = ((Variable) dimOrVar_[xIndex]).findAttribute("units");
      if(attr == null) {
        units = " ";
      } else {
        units = attr.getStringValue();
      }
    }
    xMetaData = new SGTMetaData(name,
                                units,
                                reverseAxis_[xIndex], false);
    attr = ncVar_.findAttribute("units");
    if(attr == null) {
      units = " ";
    } else {
      units = attr.getStringValue();
    }
    yMetaData = new SGTMetaData(ncVar_.getName(), units, false,
                                false);
    //
    // create title
    //
    Object[] values = getValues(origin);
    title = new StringBuffer(ncVar_.getName());
    title.append(" [");
    for(int i = shape.length - 1; i >= 0; i--) {
      if(dimOrVar_[i] instanceof ucar.nc2.Dimension) {
        title.append(((ucar.nc2.Dimension) dimOrVar_[i]).getName()).append("=");
      } else {
        title.append(((Variable) dimOrVar_[i]).getName()).append("=");
      }
      if(i == xIndex) {
        title.append("*, ");
      } else if(isTime_[i]) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Long) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Integer) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Short) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Float) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Double) {
        title.append(values[i].toString()).append(", ");
      }
    }
    title.setCharAt(title.length() - 2, ']');
    title.setLength(title.length() - 1);
    if(isTime_[xIndex]) {
      if(isYAxis_[xIndex]) {
        line = new VMapLine("ncBrowse line", title.toString(), yArray,
                            yMetaData, timeArray, xMetaData);
      } else {
        line = new VMapLine("ncBrowse line", title.toString(), timeArray,
                            xMetaData, yArray, yMetaData);
      }
    } else {
      if(isYAxis_[xIndex]) {
        line = new VMapLine("ncBrowse line", title.toString(), yArray,
                            yMetaData, xArray, xMetaData);
      } else {
        line = new VMapLine("ncBrowse line", title.toString(), xArray,
                            xMetaData, yArray, yMetaData);
      }
    }
    line.setId(id);
    return line;
  }

  private SGTGrid makeVMapGrid(int[] origin, int[] shape, String id) {
    VMapGrid grid;
    SGTMetaData xMetaData;
    SGTMetaData yMetaData;
    SGTMetaData zMetaData;
    Attribute attr = null;
    String units;
    String name;
    StringBuffer title;

    int xIndex = -1;
    int yIndex = -1;
    for(int i = 0; i < shape.length; i++) {
      if(shape[i] != 1) {
        yIndex = i;
        break;
      }
    }
    for(int i = yIndex + 1; i < shape.length; i++) {
      if(shape[i] != 1) {
        xIndex = i;
        break;
      }
    }
    if(isXAxis_[yIndex]) {
      int tmp = yIndex;
      yIndex = xIndex;
      xIndex = tmp;
    }
    GeoDate[] timeArray = null;
    double[] xArray = null;
    double[] yArray = null;
    if(isTime_[xIndex]) {
      timeArray = getTimeArray(array_.get(xIndex), xIndex,
                               origin, shape);
    } else {
      xArray = getDoubleArray(array_.get(xIndex), xIndex,
                              origin, shape);
    }
    if(isTime_[yIndex]) {
      timeArray = getTimeArray(array_.get(yIndex), yIndex,
                               origin, shape);
    } else {
      yArray = getDoubleArray(array_.get(yIndex), yIndex,
                              origin, shape);
    }
    float[] array = null;
    double[] zArray = getDoubleArray(ncVar_, origin, shape,
                                     xIndex, yIndex);
    //
    // create attributes
    //
    if(dimOrVar_[xIndex] instanceof ucar.nc2.Dimension) {
      name = ((ucar.nc2.Dimension) dimOrVar_[xIndex]).getName();
      units = " ";
    } else {
      name = ((Variable) dimOrVar_[xIndex]).getName();
      attr = ((Variable) dimOrVar_[xIndex]).findAttribute("units");
      if(attr == null) {
        units = " ";
      } else {
        units = attr.getStringValue();
      }
    }
    xMetaData = new SGTMetaData(name,
                                units,
                                reverseAxis_[xIndex], false);
    if(dimOrVar_[yIndex] instanceof ucar.nc2.Dimension) {
      name = ((ucar.nc2.Dimension) dimOrVar_[yIndex]).getName();
      units = " ";
    } else {
      name = ((Variable) dimOrVar_[yIndex]).getName();
      attr = ((Variable) dimOrVar_[yIndex]).findAttribute("units");
      if(attr == null) {
        units = " ";
      } else {
        units = attr.getStringValue();
      }
    }
    yMetaData = new SGTMetaData(name,
                                units,
                                reverseAxis_[yIndex], false);
    attr = ncVar_.findAttribute("units");
    if(attr == null) {
      units = " ";
    } else {
      units = attr.getStringValue();
    }
    zMetaData = new SGTMetaData(ncVar_.getName(), units, false,
                                false);
    //
    // create title
    //
    Object[] values = getValues(origin);
    title = new StringBuffer(ncVar_.getName());
    title.append(" [");
    for(int i = shape.length - 1; i >= 0; i--) {
      if(dimOrVar_[i] instanceof ucar.nc2.Dimension) {
        title.append(((ucar.nc2.Dimension) dimOrVar_[i]).getName()).append("=");
      } else {
        title.append(((Variable) dimOrVar_[i]).getName()).append("=");
      }
      if(i == xIndex || i == yIndex) {
        title.append("*, ");
      } else if(isTime_[i]) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Long) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Integer) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Short) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Float) {
        title.append(values[i].toString()).append(", ");
      } else if(min_.get(i) instanceof Double) {
        title.append(values[i].toString()).append(", ");
      }
    }
    title.setCharAt(title.length() - 2, ']');
    title.setLength(title.length() - 1);
    if(isTime_[xIndex]) {
      grid = new VMapGrid("ncBrowse grid", title.toString(), zArray, zMetaData,
                          timeArray, xMetaData, yArray, yMetaData);
    } else if(isTime_[yIndex]) {
      grid = new VMapGrid("ncBrowse grid", title.toString(), zArray, zMetaData,
                          xArray, xMetaData, timeArray, yMetaData);
    } else {
      grid = new VMapGrid("ncBrowse grid", title.toString(), zArray, zMetaData,
                          xArray, xMetaData, yArray, yMetaData);
    }
    grid.setId(id);
    return grid;
  }

  private GeoDate[] getTimeArray(Object array, int index,
                                 int[] origin, int[] shape) {
    GeoDate[] timeArray = new GeoDate[shape[index]];
    int out = 0;
    int len;
    if(array instanceof long[]) {
      long[] time = (long[]) array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        if(is624_) {
          timeArray[out] = new GeoDate((int) time[j], time2_[j]);
        } else {
          timeArray[out] = new GeoDate(refDate_);
          timeArray[out].increment(time[j], increment_);
        }
      }
    } else if(array instanceof int[]) {
      int[] time = (int[]) array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        if(is624_) {
          timeArray[out] = new GeoDate(time[j], time2_[j]);
        } else {
          timeArray[out] = new GeoDate(refDate_);
          timeArray[out].increment(time[j], increment_);
        }
      }
    } else if(array instanceof short[]) {
      short[] time = (short[]) array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        if(is624_) {
          timeArray[out] = new GeoDate(time[j], time2_[j]);
        } else {
          timeArray[out] = new GeoDate(refDate_);
          timeArray[out].increment(time[j], increment_);
        }
      }
    } else if(array instanceof float[]) {
      float[] time = (float[]) array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        timeArray[out] = new GeoDate(refDate_);
        timeArray[out].increment((double) time[j], increment_);
      }
    } else if(array instanceof double[]) {
      double[] time = (double[]) array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        timeArray[out] = new GeoDate(refDate_);
        timeArray[out].increment(time[j], increment_);
      }
    }
    return timeArray;
  }

  /**
   * Get array of <code>double</code>s from dimension
   *
   * @param index dimension index
   * @param origin netCDF origin
   * @param shape netCDF shape
   * @return double array
   */
  public double[] getDoubleArray(int index, int[] origin, int[] shape) {
    return getDoubleArray(array_.get(index), index, origin, shape);
  }

  private double[] getDoubleArray(Object array, int index,
                                  int[] origin, int[] shape) {
    double[] xArray = new double[shape[index]];
    int out = 0;
    int len = origin[index] + shape[index];
    for(int j = origin[index]; j < len; j++, out++) {
      if(array instanceof long[]) {
        xArray[out] = (double) ((long[]) array)[j];
      } else if(array instanceof int[]) {
        xArray[out] = (double) ((int[]) array)[j];
      } else if(array instanceof short[]) {
        xArray[out] = (double) ((short[]) array)[j];
      } else if(array instanceof double[]) {
        xArray[out] = ((double[]) array)[j];
      } else if(array instanceof byte[]) {
        xArray[out] = ((byte[])array)[j];
      } else {
        xArray[out] = (double) ((float[]) array)[j];
      }
    }
    return xArray;
  }

  private double[] getDoubleArray(Variable var,
                                  int[] origin,
                                  int[] shape,
                                  int xIndex, int yIndex) {
    double[] outArray = null;
    double temp;
    Attribute validMinAttr;
    Attribute validMaxAttr;
    Attribute scaleAttr;
    Attribute offsetAttr;
    Attribute missingAttr;
    Attribute fillValueAttr;
    Array marr = null;
    //
    boolean mapVariable = xIndex > yIndex;
    //
    try {
      if(mapVariable) {
        if(Debug.DEBUG) {
          System.out.println("xIndex = " + xIndex + ", yIndex = " + yIndex);
        }
        marr = var.read(origin, shape).transpose(xIndex, yIndex);
      } else {
        marr = var.read(origin, shape);
      }
    } catch(IOException | InvalidRangeException ex) {
      ex.printStackTrace();
    }
    //
    // try to get important attributes
    //
    validMinAttr = var.findAttribute("valid_min");
    validMaxAttr = var.findAttribute("valid_max");
    scaleAttr = var.findAttribute("scale_factor");
    offsetAttr = var.findAttribute("add_offset");
    missingAttr = var.findAttribute("missing_value");
    fillValueAttr = var.findAttribute("_FillValue");
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
//    String aType = var.getElementType().getName();
    DataType aType = var.getDataType();
    if(aType == DataType.INT) {
      int missing = Integer.MIN_VALUE;
      int validMin = 0;
      int validMax = 0;
      int[] array = null;
      if(!defaultMissing && missingNumber != null) {
        missing = missingNumber.intValue();
      }
      if(valids) {
        validMin = validMinNumber.intValue();
        validMax = validMaxNumber.intValue();
      }
      array = (int[]) marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = array[j];
          if(scale_offset) {
            outArray[j] = temp * scale + offset;
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
        for(int j = 0; j < array.length; j++) {
          temp = array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
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
    } else if(aType == DataType.BYTE) {
      byte missing = Byte.MIN_VALUE;
      byte validMin = 0;
      byte validMax = 0;
      byte[] array = null;
      if(!defaultMissing && missingNumber != null) {
        missing = missingNumber.byteValue();
      }
      if(valids) {
        validMin = validMinNumber.byteValue();
        validMax = validMaxNumber.byteValue();
      }
      array = (byte[]) marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = (double) array[j];
          if(scale_offset) {
            outArray[j] = temp * scale + offset;
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
        for(int j = 0; j < array.length; j++) {
          temp = (double) array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
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
      if(!defaultMissing && missingNumber != null) {
        missing = missingNumber.shortValue();
      }
      if(valids) {
        validMin = validMinNumber.shortValue();
        validMax = validMaxNumber.shortValue();
      }
      array = (short[]) marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = (double) array[j];
          if(scale_offset) {
            outArray[j] = temp * scale + offset;
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
        for(int j = 0; j < array.length; j++) {
          temp = (double) array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
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
      if(!defaultMissing && missingNumber != null) {
        missing = missingNumber.floatValue();
      }
      if(valids) {
        validMin = validMinNumber.floatValue();
        validMax = validMaxNumber.floatValue();
      }
      array = (float[]) marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = (double) array[j];
          if(temp >= missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
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
        for(int j = 0; j < array.length; j++) {
          temp = (double) array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
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
      if(!defaultMissing && missingNumber != null) {
        missing = missingNumber.doubleValue();
      }
      if(valids) {
        validMin = validMinNumber.doubleValue();
        validMax = validMaxNumber.doubleValue();
      }
      array = (double[]) marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = array[j];
          if(temp >= missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
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
        for(int j = 0; j < array.length; j++) {
          temp = array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
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
}
