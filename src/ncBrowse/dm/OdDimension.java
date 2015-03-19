/**
 *  $Id: OdDimension.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public class OdDimension extends NcBDimension {

  public OdDimension(NcBDataItem source) {
    super(source);
  }

  public int getLength() {
    /**@todo Implement this ncBrowse.dm.NcBDimension abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getLength() not yet implemented.");
  }

  public String getLongName() {
    /**@todo Implement this ncBrowse.dm.VarOrDim abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getLongName() not yet implemented.");
  }

  public NcBVariable getCoordinateVariable() {
    /**@todo Implement this ncBrowse.dm.NcBDimension abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getCoordinateVariable() not yet implemented.");
  }

  public boolean isVirtual() {
    /**@todo Implement this ncBrowse.dm.NcBDimension abstract method*/
    throw new java.lang.UnsupportedOperationException("Method isVirtual() not yet implemented.");
  }

  public String getName() {
    /**@todo Implement this ncBrowse.dm.VarOrDim abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getName() not yet implemented.");
  }
}