/**
 *  $Id: OdVariable.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

import ncBrowse.sgt.geom.GeoDateArray;

import java.util.List;
//import gov.noaa.pmel.util.GeoDateArray;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public class OdVariable extends NcBVariable {

  public OdVariable(NcBDataItem source) {
    super(source);
  }

  public List getDimensions() {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getDimensions() not yet implemented.");
  }

  public long getSize() {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getSize() not yet implemented.");
  }

  public GeoDateArray getGeoDateArray(int index, int[] origin, int[] shape) {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getGeoDateArray() not yet implemented.");
  }

  public String getLongName() {
    /**@todo Implement this ncBrowse.dm.VarOrDim abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getLongName() not yet implemented.");
  }

  public double[] getDoubleArray(int[] origin, int[] shape, boolean transpose, int xIndex, int yIndex) {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getDoubleArray() not yet implemented.");
  }

  public String getName() {
    /**@todo Implement this ncBrowse.dm.VarOrDim abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getName() not yet implemented.");
  }

  public int[] getShape() {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getShape() not yet implemented.");
  }

  public Object getValue(int index) {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getValue() not yet implemented.");
  }

  public NcBAttribute findAttribute(String attname) {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method findAttribute() not yet implemented.");
  }

  public int getRank() {
    /**@todo Implement this ncBrowse.dm.NcBVariable abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getRank() not yet implemented.");
  }
}