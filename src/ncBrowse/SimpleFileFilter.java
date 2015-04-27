/*
 * $Id: SimpleFileFilter.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import javax.swing.filechooser.*;
import java.io.File;

/**
 * A simple implementation of a <code>FileFilter</code>.
 *
 * @author Donald Denbo
 * @version $Revision: 1.2 $, $Date: 2000/02/21 23:43:41 $
 */
public class SimpleFileFilter extends FileFilter {
  private String[] extensions;
  private String description;
  
  public SimpleFileFilter(String ext) {
    this(new String[] {ext}, null);
  }
  public SimpleFileFilter(String[] exts, String descr) {
    extensions = new String[exts.length];
    for(int i=exts.length -1; i >=0; i--) {
      extensions[i]= exts[i].toLowerCase();
    }
    description = (descr ==null? exts[0] + " files" : descr);
  }
  
  public boolean accept(File f) {
    if(f.isDirectory()) {return true;}
    String name = f.getName().toLowerCase();
    for(int i=extensions.length-1; i>=0; i--) {
      if(name.endsWith(extensions[i])) {
	return true;
      }
    }
    return false;
  }
  
  public boolean hasExtension(String ext) {
    for (String extension : extensions) {
      if (extension.equals(ext)) return true;
    }
    return false;
  }
  
  public String getExtension() {
    return getExtension(0);
  }
  
  public String getExtension(int index) {
    int idx = index;
    if(idx < 0 || idx >= extensions.length) idx = 0;
    return extensions[idx];
  }
  public String getDescription() {
    return description;
  }
}

