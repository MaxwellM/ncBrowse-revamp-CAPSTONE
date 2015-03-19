/**
 *  $Id: NcBDimension.java 15 2013-04-24 19:16:08Z dwd $
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

public abstract class NcBDimension implements VarOrDim {
  protected NcBDataItem source_ = null;

  public NcBDimension(NcBDataItem source) {
    source_ = source;
  }

  public NcBDataItem getDataItem() {
    return source_;
  }

  public abstract int getLength();
  public abstract NcBVariable getCoordinateVariable();
  public abstract boolean isVirtual();
}