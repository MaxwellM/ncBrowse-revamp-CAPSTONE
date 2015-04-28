/**
 *  $Id: LocalNcFile.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//import java.util.Enumeration;

/**
 * Extends NetcdfFile to provide application required features.
 *
 * @author Donald Denbo
 * @version $Revision: 1.5 $, $Date: 2004/05/06 20:53:36 $
 */
public class LocalNcFile implements NcFile {
  /**
   * @label util_
   */
  private NcUtil util_;
  private String path_ = null;
  public NetcdfFile cdfFile_ = null;
  
  public LocalNcFile(File file) throws IOException {
  	cdfFile_ = NetcdfFile.open(file.getAbsolutePath());
    path_ = file.getAbsolutePath();
    util_ = new NcUtil(this);

//      //WORKS FROM HERE! NCDUMP!
//      Browser dump = new Browser();
//      dump.ncDumpTextField;
//      System.out.println(cdfFile_);
//      getVariableWindowData();
//      Browser.ncDumpTextField.setText(cdfFile_.toString());
  }

  public LocalNcFile(String path) throws IOException {
  	cdfFile_ = NetcdfFile.open(path);
//    super(path);
    path_ = path;
//    super(path);
    util_ = new NcUtil(this);
  }
    public void getVariableWindowData(){
        //Browser.ncDumpTextField.setText(cdfFile_.toString());
//        VariableWindows.commentlabel1.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(0).getName()));
//      VariableWindows.commentTextArea1.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(0).getName()))));
//      VariableWindows.commentlabel2.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(1).getName()));
//      VariableWindows.commentTextArea2.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(1).getName()))));
//      VariableWindows.commentlabel3.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(2).getName()));
//      VariableWindows.commentTextArea3.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(2).getName()))));
//      VariableWindows.commentlabel4.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(3).getName()));
//      VariableWindows.commentTextArea4.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(3).getName()))));
//      VariableWindows.commentlabel5.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(4).getName()));
//      VariableWindows.commentTextArea5.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(4).getName()))));
//      VariableWindows.commentlabel6.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(5).getName()));
//      VariableWindows.commentTextArea6.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(5).getName()))));
//      VariableWindows.commentlabel7.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(6).getName()));
//      VariableWindows.commentTextArea7.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(6).getName()))));
//      VariableWindows.commentlabel8.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(7).getName()));
//      VariableWindows.commentTextArea8.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(7).getName()))));
//        VariableWindows.commentlabel3.setText("Variable: "+String.valueOf(cdfFile_.getVariables().get(8).getName()));
//        VariableWindows.commentTextArea3.setText(String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(8).getName()))));
//        System.out.println(String.valueOf("0"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(0).getName())))));
//        System.out.println(String.valueOf("1"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(1).getName())))));
//        System.out.println(String.valueOf("2"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(2).getName())))));
//        System.out.println(String.valueOf("3"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(3).getName())))));
//        System.out.println(String.valueOf("4"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(4).getName())))));
//        System.out.println(String.valueOf("5"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(5).getName())))));
//        System.out.println(String.valueOf("6"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(6).getName())))));
//        System.out.println(String.valueOf("7"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(7).getName())))));
//        System.out.println(String.valueOf("8"+String.valueOf(cdfFile_.findVariable(String.valueOf(cdfFile_.getVariables().get(8).getName())))));

    }

    public String NcDump() {
//        // Create a stream to hold the output
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(baos);
//        // IMPORTANT: Save the old System.out!
//        PrintStream old = System.out;
//        // Tell Java to use your special stream
//        System.setOut(ps);
//        // Print some output: goes to your special stream
//        System.out.println(cdfFile_);
//        // Put things back
//        System.out.flush();
//        System.setOut(old);
//        // Show what happened
//        //System.out.println("Here: " + baos.toString());
//
//        //Browser dump = new Browser();
//        //dump.ncDumpTextField.setText(baos.toString());
//        //ncDumpTextField.setText(baos.toString());
        return cdfFile_.toString();
    }

  /**
   * added by dwd to support URL files
   */
  public LocalNcFile(URL url) throws IOException {
  	cdfFile_ = NetcdfFile.open(url.getPath());
//    super(url.getPath());
    path_ = url.getPath();
//    super(url);
    util_ = new NcUtil(this);
  }
  
  public Iterator<Variable> getDimensionVariables() {
    Vector<Variable> varDim = new Vector<>();
//    Iterator di = getDimensionIterator();
//    while(di.hasNext()) {
    for(ucar.nc2.Dimension dim: getDimensions()) {
//      ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
      Variable var = findVariable(dim.getName());
//      Variable var = dim.getCoordinateVariable();
      if(var != null && var.getRank() == 1) varDim.addElement(var);
    }
    return varDim.iterator();
  }

  public Iterator<Variable> getNonDimensionVariables() {
    boolean is624 = false;
    Attribute epic_code;
    Vector<Variable> varDim = new Vector<>();
//    Iterator vi = getVariableIterator();
//    while(vi.hasNext()) {
    for(Variable var: getVariables()) {
//      Variable var = (Variable)vi.next();
      epic_code = var.findAttribute("epic_code");
      is624 = (epic_code != null) && !epic_code.isString() && (epic_code.getNumericValue().intValue() == 624);
      if (!is624) {
        if (!var.isCoordinateVariable()) varDim.addElement(var);
      }
    }
    return varDim.iterator();
  }

  public boolean isDODS() {return false;}

  public boolean isFile() {
    return !isHttp();
  }

  public boolean isHttp() {
    return getPathName().startsWith("http:");
  }

  public String getFileName() {
    String path = getPathName();
    if(isHttp()) {
      return path.substring(path.lastIndexOf("/")+1);
    } else {
      return path.substring(path.lastIndexOf(System.getProperty("file.separator","/")) + 1);
    }
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

//  @Override
//  public Iterator getDimensionIterator() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Iterator getGlobalAttributeIterator() {
//    return this.getGlobalAttributes().iterator();
//  }

    @Override
    public String toString(){
        return toString();
    }

  @Override
  public String getPathName() {
    return path_;
  }

//  @Override
//  public Iterator getVariableIterator() {
//    return this.getVariables().iterator();
//  }

  @Override
  public String toStringDebug() {
    return toString();
  }

	@Override
  public String findAttValueIgnoreCase(Variable v, String attName, String defaultValue) {
	  return cdfFile_.findAttValueIgnoreCase(v, attName, defaultValue);
  }

	@Override
  public Dimension findDimension(String name) {
	  return cdfFile_.findDimension(name);
  }

	@Override
  public Attribute findGlobalAttribute(String name) {
	  return cdfFile_.findGlobalAttribute(name);
  }

	@Override
  public Attribute findGlobalAttributeIgnoreCase(String name) {
	  return cdfFile_.findGlobalAttributeIgnoreCase(name);
  }

	@Override
  public Variable findVariable(String name) {
	  return cdfFile_.findVariable(name);
  }

	@Override
  public List<Variable> getVariables() {
	  return cdfFile_.getVariables();
  }

	@Override
  public List<Dimension> getDimensions() {
	  return cdfFile_.getDimensions();
  }

	@Override
  public List<Attribute> getGlobalAttributes() {
	  return cdfFile_.getGlobalAttributes();
  }

	@Override
  public void close() throws IOException {
		cdfFile_.close();
  }

}