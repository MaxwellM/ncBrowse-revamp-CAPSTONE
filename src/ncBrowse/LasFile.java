package ncBrowse;

import gov.noaa.pmel.las.LASHandler;
import gov.noaa.pmel.util.GeoDate;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author $author$
 * @version 1.0
 */

public class LasFile implements NcFile {
  LASHandler.Variable mLasVar;
  LocalNcFile mDelegate;
  File mFile;
  int mTotal = 0;
  int mSize = -1;
  boolean mFinished = false;
  SwingWorker mWorker;
  IOException mException;

  public LasFile(LASHandler.Variable var) throws IOException {
    init(var);
  }

  public boolean isException() { return mException != null; }

  public IOException getException() { return mException; }

  public void delete() throws IOException {
    if (mDelegate != null) mDelegate.close();
    if (mFile != null){
      mFile.delete();
    }
  }

  public int getSize() {
    return mSize;
  }

  public int getTotalDownloaded(){
    return mTotal;
  }

  protected void init(LASHandler.Variable var) throws IOException {
    mLasVar = var;
    mFile = File.createTempFile("ncbrowse", ".nc");

    mWorker = new SwingWorker() {
        public Object construct() {
            try {
              return new LasReader();
            }
            catch (IOException ex) {
              ex.printStackTrace();
              mException = ex;
              return null;
            }
        }
    };

    mWorker.start();
  }

  public void close() {
    if (mWorker != null){
      mWorker.interrupt();
      mWorker = null;
    }
  }

  public boolean isFinished() {
    return mFinished;
  }

  class LasReader {
    LasReader() throws IOException {
      InputStream sin = null;
      FileOutputStream fout = null;
      try {
        sin = mLasVar.getData();
        mSize = mLasVar.getSize();
        fout = new FileOutputStream(mFile);
        byte[] buf = new byte[4096];
        int count = 0;

        while ( (count = sin.read(buf)) > 0) {
          if (Thread.currentThread().interrupted()){
            throw new InterruptedException();
          }
          mTotal += count;
          fout.write(buf, 0, count);
        }
        fout.close();
        fout = null;
        mDelegate = new LocalNcFile(mFile);
      }
      catch (Exception ex) {
        if (fout != null) fout.close();
        if (mDelegate != null) mDelegate.close();
        mFile.delete();
        mFile = null;
        if (ex instanceof IOException){
          throw (IOException) ex;
        }
      }
      finally {
        if (sin != null) sin.close();
        if (fout != null) fout.close();
        mFinished = true;
      }
    }
  }

  public Iterator<Variable> getDimensionVariables() {
    return mDelegate.getDimensionVariables();
  }

  public String toStringDebug() {
    return mDelegate.toStringDebug();
  }

  public String getFileName() {
    return mDelegate.getFileName();
  }

  public Object getArray(Variable parm1, int[] parm2, int[] parm3) {
    return mDelegate.getArray(parm1,parm2,parm3);
  }

  public GeoDate getRefDate() {
    return mDelegate.getRefDate();
  }

  public Object getArrayValue(Variable var, int index) {
    return mDelegate.getArrayValue(var,index);
  }

  public Iterator<Variable> getNonDimensionVariables() {
    return mDelegate.getNonDimensionVariables();
  }

  public boolean isVariableTime(Variable var) {
    return mDelegate.isVariableTime(var);
  }

  public int[] getTime2() {
    return mDelegate.getTime2();
  }

  public int getIncrement() {
    return mDelegate.getIncrement();
  }

    public void getVariableWindowData() {

    }

    public String NcDump() {
        return null;
    }

    public boolean isHttp() {
    return false;
  }

  public boolean isFile() {
    return true;
  }

  public boolean isDODS() {
    return false;
  }

  public boolean is624() {
    return false;
  }
  public String getPathName() {
    return mDelegate.getPathName();
  }
  public Attribute findGlobalAttributeIgnoreCase(String name) {
    return mDelegate.findGlobalAttributeIgnoreCase(name);
  }
  public List<Variable> getVariables() {
    return mDelegate.getVariables();
  }
//  public Iterator getVariableIterator() {
//    return mDelegate.getVariableIterator();
//  }
  public Attribute findGlobalAttribute(String name) {
    return mDelegate.findGlobalAttribute(name);
  }
  public List<Attribute> getGlobalAttributes() {
    return mDelegate.getGlobalAttributes();
  }
//  public Iterator getGlobalAttributeIterator() {
//    return mDelegate.getGlobalAttributeIterator();
// }
  public String findAttValueIgnoreCase(Variable v, String attName, String defaultValue) {
    return mDelegate.findAttValueIgnoreCase(v,attName,defaultValue);
  }
  public List<Dimension> getDimensions() {
    return mDelegate.getDimensions();
  }
//  public Iterator getDimensionIterator() {
//    return mDelegate.getDimensionIterator();
// }
  public Variable findVariable(String name) {
    return mDelegate.findVariable(name);
  }
  public Dimension findDimension(String name) {
    return mDelegate.findDimension(name);
  }

}