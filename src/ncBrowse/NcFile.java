/**
 *  $Id: NcFile.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Extends NetcdfFile to provide application required features.
 *
 * @author Donald Denbo
 * @version $Revision: 1.8 $, $Date: 2001/09/14 23:22:31 $
 */
public interface NcFile {

  Iterator<Variable> getDimensionVariables();
  Iterator<Variable> getNonDimensionVariables();

  String findAttValueIgnoreCase(Variable v,
                                String attName,
                                String defaultValue);
  Dimension findDimension(String name);
  Attribute findGlobalAttribute(String name);
  Attribute findGlobalAttributeIgnoreCase(String name);
  Variable findVariable(String name);
//  public Iterator getDimensionIterator();
//  public Iterator getGlobalAttributeIterator();
String getPathName();
//  public Iterator getVariableIterator();
String toString();
  String toStringDebug();
  List<Variable> getVariables();
  List<Dimension> getDimensions();
  List<Attribute> getGlobalAttributes();
  void close() throws IOException;

  boolean isFile();
  boolean isDODS();
  boolean isHttp();

  String getFileName();
  //
  // time and array utility methods
  //
  boolean isVariableTime(Variable var);
  Object getArrayValue(Variable var, int index);
  Object getArray(Variable var, int[] origin, int[] shape);
  boolean is624();
  int[] getTime2();
  GeoDate getRefDate();
  int getIncrement();
    void getVariableWindowData();
    String NcDump();
}
