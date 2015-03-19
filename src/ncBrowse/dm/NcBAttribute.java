/**
 *  $Id: NcBAttribute.java 15 2013-04-24 19:16:08Z dwd $
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

public interface NcBAttribute {
  String getStringValue();
  Number getNumericValue();
  Number getNumericValue(int index);
//  Object getValue();
  boolean isString();
  boolean isArray();
  String getName();
}