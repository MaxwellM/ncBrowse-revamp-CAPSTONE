/**
 *  $Id: ExportCdl.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

/**
 * Export netCDF variable in CDL format.
 * a Browser.
 *
 * @author Donald Denbo
 * @version $Revision: 1.11 $, $Date: 2005/10/21 19:28:53 $
 */
public class ExportCdl extends VariableProcessThread {
  static private JFileChooser fileChoose_;
  private File outFile_;
  private String ext_;
  private String dir_;

  public ExportCdl(Browser parent, String title) {
    super(parent, title);
    dir_ = System.getProperty("user.dir");
    fileChoose_ = new JFileChooser(dir_);
  }

  public void run() {
    if(Debug.DEBUG) System.out.println
                      ("Running Variable Export! for " + variable_);
    File file;
    FileFilter filt;
    String defext = ".txt";

    if(ncFile_ == null) return;
    String[] cdlfile = new String[]{".cdl"};
    SimpleFileFilter cdlfilt =
      new SimpleFileFilter(cdlfile, "CDL format (*.cdl)");

    fileChoose_.setFileFilter(fileChoose_.getAcceptAllFileFilter());
    fileChoose_.resetChoosableFileFilters();
    fileChoose_.addChoosableFileFilter(cdlfilt);
    //
    // set the default output format
    //
    if(cdlfilt.hasExtension(defext)) {
      fileChoose_.setFileFilter(cdlfilt);
    }
    //
    // set the default output file
    //
    File defFile = new File(dir_, variable_);
    fileChoose_.setSelectedFile(defFile);

    int ret_val = fileChoose_.showSaveDialog(parent_);

    if(ret_val == JFileChooser.APPROVE_OPTION) {
      outFile_ = fileChoose_.getSelectedFile();
      filt = (FileFilter)fileChoose_.getFileFilter();
      if(filt instanceof SimpleFileFilter) {
        ext_ = ((SimpleFileFilter)filt).getExtension();
      } else {
        JOptionPane.showInternalMessageDialog
          (parent_,
           "Unrecognized output format selected.",
           "SaveAs Error",
           JOptionPane.ERROR_MESSAGE);
        return;
      }
      if(Debug.DEBUG) {
        System.out.println("save this: " + outFile_.getAbsolutePath() +
                           ", as this: " + filt.getDescription());
        System.out.println("extension = " + ext_);
      }
      if(Debug.DEBUG) System.out.println("Variable: " +
                                         variable_ + " selected");
      Variable ncVar = ncFile_.findVariable(variable_);
      write_cdl(ncVar);
    }
  }
  private void write_cdl(Variable ncVar) {
    Variable dimVar = null;
    Object dimArray = null;
    boolean isEPICtime = false;
    Variable time2Var = null;
    Object varArray = null;
    String outpath;
    FileOutputStream fos = null;
    String name = outFile_.getName().toLowerCase();
    StringBuffer sbuf;
    int[] origin = range_.getOrigin();
    int[] shape = range_.getShape();
    int[] org1 = new int[1];
    int[] shp1 = new int[1];
    int index = 0;
    int time2index = 0;
    //    String tab = "\t";
    //    String str = null;
    //    int len = 0;

    if(!name.endsWith(ext_)) {
      outpath = outFile_.getPath() + ext_;
    } else {
      outpath = outFile_.getPath();
    }
    try {
      fos = new FileOutputStream(outpath);
    } catch (IOException e) {
      System.out.println(e);
      return;
    }
    PrintWriter ps = new PrintWriter(fos, true);

    ps.print("netcdf " + outFile_.getName() + " {\n");
    //
    // print varible dimensions
    //
    ps.print("dimensions:\n");
    Iterator di = ncVar.getDimensions().iterator();
    Dimension ncDim = null;
    index=0;
    while(di.hasNext()) {
      sbuf = new StringBuffer();
      ncDim = (Dimension)di.next();
      sbuf.append(ncDim.getName());
      sbuf.append(" = ");
      if(ncDim.isUnlimited() && (ncDim.getLength() == shape[index])) {
        sbuf.append("UNLIMITED ; // (");
        sbuf.append(ncDim.getLength());
        sbuf.append(" currently)");
      } else {
        sbuf.append(shape[index]);
        sbuf.append(" ;");
      }
      ps.print("     " + sbuf.toString() + "\n");
      ps.flush();
      index++;
    }
    //
    // print variable
    //
    ps.print("variables:\n");
    di = ncVar.getDimensions().iterator();
    index=0;
    while(di.hasNext()) {
      sbuf = new StringBuffer();
//      dimVar = ((ucar.nc2.Dimension)di.next()).getCoordinateVariable();
      dimVar = ncFile_.findVariable(((ucar.nc2.Dimension)di.next()).getName());
      sbuf.append(dimVar.toString());
      fixCDL(sbuf, dimVar);
      ps.print(sbuf.toString());
      ps.flush();
      Attribute ec = dimVar.findAttribute("epic_code");
      if(ec != null) {
        if(ec.getNumericValue().intValue() == 624) {
          time2Var = ncFile_.findVariable(dimVar.getName() + "2");
          if(time2Var != null) {
            isEPICtime = true;
	    time2index=index;
            sbuf = new StringBuffer();
            sbuf.append(time2Var.toString());
            fixCDL(sbuf, time2Var);
            ps.print(sbuf.toString());
          }
        }
      }
      index++;
    }
    if(Debug.DEBUG) System.out.println("cdl formatting");
    sbuf = new StringBuffer();
    sbuf.append(ncVar.toString());
    fixCDL(sbuf, ncVar);
    if(Debug.DEBUG) System.out.println(sbuf.toString());
    ps.print(sbuf.toString());
    ps.flush();
    //
    // print values of dimensions and variable
    //
    ps.print("data:\n\n");
    // dimension variables
    di = ncVar.getDimensions().iterator();
    index=0;
    while(di.hasNext()) {
      sbuf = new StringBuffer();
//      dimVar = ((ucar.nc2.Dimension)di.next()).getCoordinateVariable();
      dimVar = ncFile_.findVariable(((ucar.nc2.Dimension)di.next()).getName());
      org1[0] = origin[index];
      shp1[0] = shape[index];
      try {
        dimArray = dimVar.read(org1, shp1).copyTo1DJavaArray();
        if(Debug.DEBUG) System.out.println("Dim var " + dimVar.getName()
                                           + ": " + dimArray.toString());
      } catch (InvalidRangeException ire) {
	ire.printStackTrace();
      }catch (IOException e) {
        System.out.println(e);
      }
      writeArray(dimArray, ps, dimVar.getName());
      index++;
    }
    // time2 variable
    if(isEPICtime) {
      org1[0] = origin[time2index];
      shp1[0] = shape[time2index];
      try {
        varArray = time2Var.read(org1, shp1).copyTo1DJavaArray();
        if(Debug.DEBUG) System.out.println("var " + time2Var.getName() +
                                           ": " +
					   varArray.toString());
      } catch (InvalidRangeException ire) {
	ire.printStackTrace();
      } catch (IOException e) {
        System.out.println(e);
      }
      writeArray(varArray, ps, time2Var.getName());
    }
    // selected variable
    try {
      varArray = ncVar.read(origin, shape).copyTo1DJavaArray();
      if(Debug.DEBUG) System.out.println("var " + ncVar.getName() +
                                         ": " + varArray.toString());
    } catch (InvalidRangeException ire) {
      ire.printStackTrace();
    } catch (IOException e) {
      System.out.println(e);
    }
    writeArray(varArray, ps, ncVar.getName());
    //
    // close file
    //
    ps.print("}\n");
    ps.close();
  }

  private void writeArray(Object dimArray, PrintWriter ps, String name) {
    int len = 0;
    int lineCount = 0;
    String str = null;
    long[] longArray = null;
    int[] intArray = null;
    float[] floatArray = null;
    double[] doubleArray = null;
    short[] shortArray = null;
    boolean isLongArray = false;
    boolean isIntArray = false;
    boolean isFloatArray = false;
    boolean isDoubleArray = false;
    boolean isShortArray = false;
    StringBuffer sbuf = new StringBuffer(" " + name  + " = ");
    if(dimArray instanceof long[]) {
      longArray = (long[])dimArray;
      len = longArray.length;
      isLongArray = true;
    } else if(dimArray instanceof int[]) {
      intArray = (int[])dimArray;
      len = intArray.length;
      isIntArray = true;
    } else if(dimArray instanceof float[]) {
      floatArray = (float[])dimArray;
      len = floatArray.length;
      isFloatArray = true;
    } else if(dimArray instanceof double[]) {
      doubleArray = (double[])dimArray;
      len = doubleArray.length;
      isDoubleArray = true;
    } else if(dimArray instanceof short[]) {
      shortArray = (short[])dimArray;
      len = shortArray.length;
      isShortArray = true;
    }
    for(int i=0; i < len; i++) {
      if(isLongArray) {
        str = Long.toString(longArray[i]) + " ";
      } else if(isIntArray) {
        str = Integer.toString(intArray[i]) + ", ";
      } else if(isFloatArray) {
        str = Float.toString(floatArray[i]) + ", ";
      } else if(isDoubleArray) {
        str = Double.toString(doubleArray[i]) + ", ";
      } else if(isShortArray) {
        str = Short.toString(shortArray[i]) + ", ";
      }
      if((sbuf.length() + str.length()) > 78) {
        ps.print(sbuf.toString() + "\n");
        lineCount++;
        if(lineCount%10 == 0) ps.flush();
        sbuf = new StringBuffer("   " + str);
      } else {
        sbuf.append(str);
      }
    }
    sbuf.setCharAt(sbuf.length()-2, ' ');
    sbuf.setCharAt(sbuf.length()-1, ';');
    sbuf.append("\n\n");
    ps.print(sbuf.toString());
    ps.flush();
  }

  private void fixCDL(StringBuffer sbuf, Variable ncVar) {
    String str = sbuf.toString();
    int[] index = new int[sbuf.length()];
    int ind = str.indexOf(" :");
    int count = 0;
    while(ind >= 0) {
      index[count] = ind;
      ind = str.indexOf(" :", index[count] + 2);
      count++;
    }
    for(int i=count-1; i >= 0; i--) {
      sbuf.replace(index[i], index[i]+2, " " + ncVar.getName() + ":");
    }
  }

  public void setLocation(int x, int y) {
  }
}
