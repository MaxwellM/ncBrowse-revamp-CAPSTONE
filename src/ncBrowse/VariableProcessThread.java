/**
 *  $Id: VariableProcessThread.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import java.awt.Frame;
import java.lang.Thread;

//  import ucar.netcdf.Attribute;

import ucar.nc2.Attribute;

/**
 * Abstract wrapper for <code>Thread</code> support of graphics and
 * export of netCDF variables.
 *
 * @author Donald Denbo
 * @version $Revision: 1.13 $, $Date: 2001/08/29 22:57:11 $
 */
abstract public class VariableProcessThread extends Thread {

  public static final int GRAPHER = 1;
  public static final int EXPORT_CDL = 2;
  public static final int EXPORT_UNH = 3;
  public static final int TEXTUAL = 4;

/**
 * @label range_ 
 * @link aggregationByValue
 * @supplierCardinality 1
 * @undirected
 */
  SelectionRange range_;

  /**
   * @label parent_ 
   */
  Browser parent_;
  String title_;

  /**
   * @label ncFile_ 
   */
  NcFile ncFile_;
  String variable_;
  String long_name_ = null;
  
  public VariableProcessThread(Browser parent, String title) {
    parent_ = parent;
    title_ = title;
  }
  public void setNcFile(NcFile ncFile) {
    ncFile_ = ncFile;
  }
  public void setVariable(String var) {
    Attribute attr = null;
    variable_ = var;
    attr = ncFile_.findVariable(variable_).findAttribute("long_name");
    if(attr != null) {
      long_name_ = attr.getStringValue();
    }
  }
  public void setSelectionRange(SelectionRange selRange) {
    range_ = selRange;
  }
  abstract public void run();
  abstract public void setLocation(int x, int y);
}




