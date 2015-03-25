/*
 * $Id: DapperNcFile.java 15 2013-04-24 19:16:08Z dwd $
 *
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package ncBrowse;

import gov.noaa.pmel.nc2.station.StationDataset;
import gov.noaa.pmel.util.GeoDate;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Extends NetcdfFile to provide application required features.
 *
 * @author Donald Denbo
 * @version $Revision: 1.1 $, $Date: 2004/03/30 19:06:50 $
 */
public class DapperNcFile implements NcFile {
  private NcUtil util_;
  private StationDataset dataset_ = null;

  public DapperNcFile(StationDataset dataset) {
    dataset_ = dataset;
    util_ = new NcUtil(this);
    if(Debug.DEBUG) {
//      System.out.println("Instantiating DapperNcFile = " + dataset.getPathName());
      System.out.println("Instantiating DapperNcFile = " + dataset.getDetailInfo());
    }
  }

  public Iterator<Variable> getDimensionVariables() {
    Vector<Variable> varDim = new Vector<Variable>();
//    Iterator di = getDimensionIterator();
//    while(di.hasNext()) {
    for(ucar.nc2.Dimension dim: getDimensions()) {
//      ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
//      Variable var = dim.getCoordinateVariable();
      Variable var = findVariable(dim.getName());
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

    /**
   * findAttValueIgnoreCase
   *
   * @param v Variable
   * @param attName String
   * @param defaultValue String
   * @return String
   */
  public String findAttValueIgnoreCase(Variable v, String attName,
                                       String defaultValue) {
    return dataset_.findAttValueIgnoreCase(v, attName, defaultValue);
  }

  /**
   * findDimension
   *
   * @param name String
   * @return Dimension
   */
  public Dimension findDimension(String name) {
    return dataset_.findDimension(name);
  }

  /**
   * findGlobalAttribute
   *
   * @param name String
   * @return Attribute
   */
  public Attribute findGlobalAttribute(String name) {
    return dataset_.findGlobalAttribute(name);
  }

  /**
   * findGlobalAttributeIgnoreCase
   *
   * @param name String
   * @return Attribute
   */
  public Attribute findGlobalAttributeIgnoreCase(String name) {
    return dataset_.findGlobalAttributeIgnoreCase(name);
  }

  /**
   * findVariable
   *
   * @param name String
   * @return Variable
   */
  public Variable findVariable(String name) {
    return dataset_.findVariable(name);
  }

  /**
   * getDimensionIterator
   *
   * @return Iterator
   */
//  public Iterator getDimensionIterator() {
////    return dataset_.getDimensionIterator();
//    return dataset_.getDimensions().iterator();
//  }
  public List<Dimension> getDimensions() {
    return dataset_.getDimensions();
  }

  /**
   * getGlobalAttributeIterator
   *
   * @return Iterator
   */
//  public Iterator getGlobalAttributeIterator() {
////    return dataset_.getGlobalAttributeIterator();
//    return dataset_.getGlobalAttributes().iterator();
//  }
  public List<Attribute> getGlobalAttributes() {
    return dataset_.getGlobalAttributes();
  }

  /**
   * getPathName
   *
   * @return String
   */
  public String getPathName() {
//    return dataset_.getPathName();
    //TODO this may be wrong interp
    return dataset_.getDetailInfo();
  }

  /**
   * getVariableIterator
   *
   * @return Iterator
   */
//  public Iterator getVariableIterator() {
////    return dataset_.getVariableIterator();
//    return dataset_.getVariables().iterator();
//  }
  
  public List<Variable> getVariables() {
    return dataset_.getVariables();
  }

  /**
   * toString
   *
   * @return String
   */
  @Override
  public String toString() {
    return dataset_.toString();
  }

  /**
   * toStringDebug
   *
   * @return String
   */
  public String toStringDebug() {
    return dataset_.toStringDebug();
  }

	@Override
  public void close() throws IOException {
	  // TODO Auto-generated method stub
	  
  }
}
