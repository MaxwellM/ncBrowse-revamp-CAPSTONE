/**
 *  $Id: OdDataItem.java 15 2013-04-24 19:16:08Z dwd $
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

public class OdDataItem extends NcBDataItem {

  public OdDataItem() {
    dataItemType_ = OPENDAP;
  }


  public String getPathName() {
    /**@todo Implement this ncBrowse.dm.NcBDataItem abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getPathName() not yet implemented.");
  }

  public String getFileName() {
    /**@todo Implement this ncBrowse.dm.NcBDataItem abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getFileName() not yet implemented.");
  }

  public NcBVariable findVariable(String name) {
    /**@todo Implement this ncBrowse.dm.NcBDataItem abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getDimensionIterator() not yet implemented.");
  }

  public Iterator getDimensionIterator() {
    /**@todo Implement this ncBrowse.dm.NcBDataItem abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getDimensionIterator() not yet implemented.");
  }

  public Iterator getGlobalAttributeIterator() {
    /**@todo Implement this ncBrowse.dm.NcBDataItem abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getDimensionIterator() not yet implemented.");
  }

  public Iterator getVariableIterator() {
    /**@todo Implement this ncBrowse.dm.NcBDataItem abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getVariableIterator() not yet implemented.");
  }
}