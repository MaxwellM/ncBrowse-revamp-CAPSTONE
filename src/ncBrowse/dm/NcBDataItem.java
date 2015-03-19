/**
 *  $Id: NcBDataItem.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

import java.util.Iterator;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public abstract class NcBDataItem {
  public static final int OPENDAP = 0;
  public static final int NETCDF_FILE = 1;
  public static final int NETCDF_HTTP = 2;
  public static final int NETCDF_DODS = 3;
  public static final int UNDEFINED = -1;

  protected int dataItemType_ = UNDEFINED;

  public static NcBDataItem createDataItem(String url) {
    return null;
  }

  public abstract String getFileName();
  public abstract String getPathName();
  public abstract Iterator getVariableIterator();
  public abstract Iterator getDimensionIterator();
  public abstract Iterator getGlobalAttributeIterator();
  public abstract NcBVariable findVariable(String name);
  public int getType(){
    return dataItemType_;
  }
}