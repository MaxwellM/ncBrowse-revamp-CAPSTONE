/**
 *  $Id: NcFile.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import gov.noaa.pmel.util.GeoDate;
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

  public Iterator<Variable> getDimensionVariables();
  public Iterator<Variable> getNonDimensionVariables();

  public String findAttValueIgnoreCase(Variable v,
                                       String attName,
                                       String defaultValue);
  public Dimension findDimension(String name);
  public Attribute findGlobalAttribute(String name);
  public Attribute findGlobalAttributeIgnoreCase(String name);
  public Variable findVariable(String name);
//  public Iterator getDimensionIterator();
//  public Iterator getGlobalAttributeIterator();
  public String getPathName();
//  public Iterator getVariableIterator();
  public String toString();
  public String toStringDebug();
  public List<Variable> getVariables();
  public List<Dimension> getDimensions();
  public List<Attribute> getGlobalAttributes();
  public void close() throws IOException;

  public boolean isFile();
  public boolean isDODS();
  public boolean isHttp();

  public String getFileName();
  //
  // time and array utility methods
  //
  public boolean isVariableTime(Variable var);
  public Object getArrayValue(Variable var, int index);
  public Object getArray(Variable var, int[] origin, int[] shape);
  public boolean is624();
  public int[] getTime2();
  public GeoDate getRefDate();
  public int getIncrement();
}
