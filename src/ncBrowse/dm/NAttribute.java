/**
 *  $Id: NAttribute.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

import ucar.nc2.Attribute;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public class NAttribute implements NcBAttribute {
  private Attribute attr_ = null;

  public NAttribute(Attribute ncAtt) {
    attr_ = ncAtt;
  }

  public String getName() {
    return attr_.getName();
  }

  public String getStringValue() {
    return attr_.getStringValue();
  }

  public Number getNumericValue() {
    return attr_.getNumericValue();
  }

  public Number getNumericValue(int index) {
    return attr_.getNumericValue(index);
  }

//  public Object getValue() {
//    return attr_.getValue();
//  }

  public boolean isString() {
    return attr_.isString();
  }

  public boolean isArray() {
    return attr_.isArray();
  }

}