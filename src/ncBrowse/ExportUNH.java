/**
 *  $Id: ExportUNH.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTGrid;
import ucar.nc2.Variable;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

//  import ucar.netcdf.Variable;
//  import ucar.netcdf.DimensionIterator;
//  import ucar.netcdf.Attribute;

/**
 * Export netCDF variable in UNH ascii format.
 *
 * @author Donald Denbo
 * @version $Revision: 1.9 $, $Date: 2001/06/01 19:00:01 $
 */
public class ExportUNH extends VariableProcessThread {
  static private JFileChooser fileChoose_;
  private File outFile_;
  private String ext_;
  private String dir_;

  /** @link dependency 
   * @stereotype access*/
  /*#SelectionRange lnkSelectionRange;*/
    
  public ExportUNH(Browser parent, String title) {
    super(parent, title);
    dir_ = System.getProperty("user.dir");
    fileChoose_ = new JFileChooser(dir_);
  }

  public void run() {
    if(Debug.DEBUG) System.out.println("Running Variable Export! for " + variable_);
    File file;
    FileFilter filt;
    String defext = ".txt";
	    
    if(ncFile_ == null) return;
    String[] txtfile = new String[]{".txt"};
    SimpleFileFilter txtfilt =
      new SimpleFileFilter(txtfile, "Formatted Text (*.txt)");

    fileChoose_.setFileFilter(fileChoose_.getAcceptAllFileFilter());
    fileChoose_.resetChoosableFileFilters();
    fileChoose_.addChoosableFileFilter(txtfilt);
    //
    // set the default output format
    //
    if(txtfilt.hasExtension(defext)) {
      fileChoose_.setFileFilter(txtfilt);
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
	JOptionPane.showInternalMessageDialog(parent_,
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
      Variable var = ncFile_.findVariable(variable_);
      write_text(var);
    }
  }

  private String makeSpaces(int len) {
    StringBuffer sb = new StringBuffer();
    for(int i=0; i < len; i++) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private void write_text(Variable var) {
    int COL = 6;
    FileOutputStream fos = null;
    String outpath;
    String name = outFile_.getName().toLowerCase();
    int[] origin;
    int[] shape;
    int[] last;
    int tIndex = 0;
    double[] tArray;
    double[] zArray;
    double[] array;
    int zIndex = 1;
    int index;
    SGTData data;
    Object[] start;
    Object[] end;
    String timeUnits = " ";
    StringBuffer line;
    String num;
    DecimalFormat df;
    DecimalFormat tf;

    try {
      data = range_.getDataModel();
    } catch (RankNotSupportedException e) {
      JOptionPane.showMessageDialog(parent_,
				    e,
				    "Data Model Error",
				    JOptionPane.ERROR_MESSAGE);
      return;
    }
    if(!(data instanceof SGTGrid)) {
      JOptionPane.showMessageDialog(parent_,
				    "Must have at two dependent variables",
				    "Legal Rank Error",
				    JOptionPane.ERROR_MESSAGE);
      return;
    }
    int rank = range_.getRank();
    origin = range_.getOrigin();
    start = range_.getValues(origin);
    shape = range_.getShape();
    last = new int[rank];
    for(int i=0; i < rank; i++) {
      last[i] = origin[i] + shape[i] - 1;
    }
    end = range_.getValues(last);

    for(int i=0; i < rank; i++) {
      if(range_.isTime(i)) {
	tIndex = i;
	timeUnits = 
	  range_.getDimensionVariable(i).findAttribute("units").getStringValue();
      } else if(range_.isYAxis(i)) {
	zIndex = i;
      }
    }

    tArray = range_.getDoubleArray(tIndex, origin, shape);
    zArray = range_.getDoubleArray(zIndex, origin, shape);

    double dz = zArray[0] - zArray[1];    // depth is order reversed for OLEM
    double dt = tArray[1] - tArray[0];

    array = ((SGTGrid)data).getZArray();

    if(!name.endsWith(ext_)) {
      outpath = outFile_.getPath() + ext_;
    } else {
      outpath = outFile_.getPath();
    }
    try {
      fos = new FileOutputStream(outpath);
    } catch (IOException e) {
        String s = e.toString();
        System.out.println(s);
      return;
    }
    PrintWriter ps = new PrintWriter(fos, true);

    double zmin = ((Number)end[1]).doubleValue();
    double zmax = ((Number)start[1]).doubleValue();

    ps.print("variable: " + var.getName() + "\n");
    df = new DecimalFormat("###0.0");
    tf = new DecimalFormat("#####0");
    num = df.format(((Number)start[3]).doubleValue());
    ps.print("        x: " + makeSpaces(COL - num.length()) + num + " (m)\n");
    num = df.format(((Number)start[2]).doubleValue());
    ps.print("        y: " + makeSpaces(COL - num.length()) + num + " (m)\n");
    num = df.format(zmin);
    ps.print("     zmin: " + makeSpaces(COL - num.length()) + num + " (m)\n");
    num = df.format(zmax);
    ps.print("     zmax: " + makeSpaces(COL - num.length()) + num + " (m)\n");
    num = df.format(dz);
    ps.print("       dz: " + makeSpaces(COL - num.length()) + num + " (m)\n");
    ps.print("  timeref: " + timeUnits + "\n");
    num = tf.format(tArray[0]);
    ps.print("     tmin: " + makeSpaces(COL - num.length()) + num + " (s)\n");
    num = tf.format(tArray[tArray.length-1]);
    ps.print("     tmax: " + makeSpaces(COL - num.length()) + num + " (s)\n");
    num = tf.format(dt);
    ps.print("       dt: " + makeSpaces(COL - num.length()) + num + " (s)\n");
    ps.print("     grid: not interpolated\n");
    num = tf.format(tArray[0]);
    ps.print(" (" + makeSpaces(COL - num.length()) + num);
    num = df.format(zmin);
    ps.print(") | " + makeSpaces(COL - num.length()) + num);
    num = df.format(zmax);
    ps.print("  ----> " + makeSpaces(COL - num.length()) + num + "\n");
    ps.print("          |\n");
    num = tf.format(tArray[tArray.length-1]);
    ps.print(" (" + makeSpaces(COL - num.length()) + num + ") v\n\n");

    for(int row=0; row < shape[tIndex]; row++) {
      num = tf.format(tArray[row]);
      line = new StringBuffer("(" + 
			      makeSpaces(COL - num.length()) + num + ") ");
      for(int col=shape[zIndex]-1; col >= 0; col--) {
	index = col + row*shape[zIndex];
	line.append((float)array[index] + " ");
      }
      line.append("\n");
      ps.print(line.toString());
    }
    ps.close();
  }
  public void setLocation(int x, int y) {
  }
}



