/**
 *  $Id: NDimension.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

import ucar.nc2.Dimension;
import ucar.nc2.Variable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public class NDimension extends NcBDimension {
  private Dimension dim_ = null;

  public NDimension(NcBDataItem source, Dimension dim) {
    super(source);
    dim_ = dim;
  }

  public int getLength() {
    return dim_.getLength();
  }

  public String getLongName() {
    return getName();
  }

//  public NcBVariable getCoordinateVariable() {
//    Variable var = dim_.getCoordinateVariable();
//    return source_.findVariable(var.getName());
//  }
  public NcBVariable getCoordinateVariable() {
    throw new NoSuchMethodError();
  }

  public boolean isVirtual() {
    return false;
  }

  public String getName() {
    return dim_.getName();
  }

  Dimension getDimension() {
    return dim_;
  }
}