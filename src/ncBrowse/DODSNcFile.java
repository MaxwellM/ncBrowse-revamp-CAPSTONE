/**
 *  $Id: DODSNcFile.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import dods.dap.DODSException;
import gov.noaa.pmel.util.GeoDate;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.dods.DODSNetcdfFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Vector;

//import java.util.Enumeration;

/**
 * Extends NetcdfFile to provide application required features.
 *
 * @author Donald Denbo
 * @version $Revision: 1.4 $, $Date: 2004/05/06 20:53:36 $
 */
public class DODSNcFile extends DODSNetcdfFile implements NcFile {
  /**
   * @label util_ 
   */
  private NcUtil util_;

  public DODSNcFile(String path)
    throws IOException, DODSException, MalformedURLException {
    super(path);
    util_ = new NcUtil(this);
    System.out.println("Instantiating DODSNcFile = " + path);
  }

  public Iterator<Variable> getDimensionVariables() {
    Vector<Variable> varDim = new Vector<Variable>();
//    Iterator di = getDimensionIterator();
//    while(di.hasNext()) {
    for(ucar.nc2.Dimension dim: getDimensions()) {
//      ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
      Variable var = findVariable(dim.getName());
//      Variable var = dim.getCoordinateVariable();
      if(var != null) varDim.addElement(var);
    }
    return varDim.iterator();
  }

  public Iterator<Variable> getNonDimensionVariables() {
    boolean is624 = false;
    Attribute epic_code;
    Vector<Variable> varDim = new Vector<Variable>();
//    Iterator vi = getVariableIterator();
//    while(vi.hasNext()) {
    for(Variable var: getVariables()) {
//      Variable var = (Variable)vi.next();
      epic_code = var.findAttribute("epic_code");
      if(epic_code != null) {
        is624 = epic_code.getNumericValue().intValue() == 624;
      } else {
        is624 = false;
      }
      if(!is624) {
        if(!var.isCoordinateVariable()) varDim.addElement(var);
      }
    }
    return varDim.iterator();
  }

  public boolean isDODS() {return true;};
  public boolean isFile() {return false;};
  public boolean isHttp() {return false;};

  public String getFileName() {
    String path = getPathName();
    return path.substring(path.lastIndexOf("/")+1);
  }
  //
  // time and array utility methods
  //
  public boolean isVariableTime(Variable var) {
    return util_.isVariableTime(var);
  }

  public Object getArrayValue(Variable var, int index) {
    return util_.getArrayValue(var, index);
  }

  public Object getArray(Variable var, int[] origin, int[] shape) {
    return util_.getArray(var, origin, shape);
  }

  public boolean is624() {
    return util_.is624();
  }

  public int[] getTime2() {
    return util_.getTime2();
  }

  public GeoDate getRefDate() {
    return util_.getRefDate();
  }

  public int getIncrement() {
    return util_.getIncrement();
  }

    public void getVariableWindowData() {

    }

    public String NcDump() {
        return null;
    }

    @Override
  public String getPathName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toStringDebug() {
    // TODO Auto-generated method stub
    return null;
  }
}
