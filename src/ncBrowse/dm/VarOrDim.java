/**
 *  $Id: VarOrDim.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

public interface VarOrDim {
  String getName();
  String getLongName();
  NcBDataItem getDataItem();
}
