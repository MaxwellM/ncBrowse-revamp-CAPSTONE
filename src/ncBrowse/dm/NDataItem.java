/**
 *  $Id: NDataItem.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.dm;

import dods.dap.DODSException;
import ncBrowse.NcFile;
import ucar.nc2.NetcdfFile;
import ucar.nc2.dods.DODSNetcdfFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: NOAA/PMEL/EPIC</p>
 * @author Donald Denbo
 * @version 1.0
 */

public class NDataItem extends NcBDataItem {
  private NetcdfFile dataItem_ = null;
  private File file_ = null;
  private URL url_ = null;
  private List dimension_ = null;
  private List variable_ = null;
  private List globalAttribute_ = null;

  /**
   * Open a local netCDF file.
   *
   * @param file netCDF file
   * @throws IOException Error connecting to local or web resource
   */
  public NDataItem(File file) throws IOException {
    dataItemType_ = NETCDF_FILE;
    file_ = file;
    dataItem_ = NetcdfFile.open(file.getAbsolutePath());
    init();
  }

  /**
   * Supported protocols are file, http, and dods.  The http protocol will
   * use the web access. The dods protocol is required to connect to an OPeNDAP
   * server using the DODS-netCDF libraries.
   *
   * @param url URL pointing to data
   * @throws IOException Error connecting to local or web resource
   * @throws DODSException Error openning OPeNDAP resource
   */
  public NDataItem(URL url) throws IOException, DODSException {
    url_ = url;
    switch (url.getProtocol()) {
      case "file":
        dataItemType_ = NETCDF_FILE;
        dataItem_ = NetcdfFile.open(url.getPath());
        break;
      case "http":
        dataItemType_ = NETCDF_HTTP;
        dataItem_ = NetcdfFile.open(String.valueOf(url));
        break;
      case "dods":
        dataItemType_ = NETCDF_DODS;
        dataItem_ = new DODSNetcdfFile(url.toExternalForm());
        break;
    }
    init();
  }

  /**
   * Open a file specified by the path.  The path is converted to a URL.
   *
   * @param path Path of data item
   * @throws IOException Error connecting to local or web resource
   * @throws DODSException Error openning OPeNDAP resource
   */
  public NDataItem(String path) throws IOException, DODSException {
    this(new URL(path));
  }
  /**
   * Wrap an existing NcFile, either LocalNcFile or DODSNcFile.
   *
   * @param ncFile existing NcFile
   */
  public NDataItem(NcFile ncFile) {
    dataItem_ = (NetcdfFile)ncFile;
    if(ncFile.isFile()) {
      dataItemType_ = NETCDF_FILE;
      file_ = new File(ncFile.getPathName());
    } else if(ncFile.isHttp()) {
      dataItemType_ = NETCDF_HTTP;
      try {
        url_ = new URL(ncFile.getPathName());
      } catch (MalformedURLException mue) {
        url_ = null;
      }
    } else if(ncFile.isDODS()) {
      dataItemType_ = NETCDF_DODS;
      try {
        url_ = new URL(ncFile.getPathName());
      } catch (MalformedURLException mue) {
        url_ = null;
      }
    }
    init();
  }

  NetcdfFile getDataItem() {
    return dataItem_;
  }

  public String getPathName() {
    if(file_ != null) {
      return file_.getAbsolutePath();
    } else {
      return url_.getPath();
    }
  }

  public String getFileName() {
    if(file_ != null) {
      return file_.getName();
    } else {
      return url_.getFile();
    }
  }

  public Iterator getDimensionIterator() {
    return dimension_.iterator();
  }

  public Iterator getVariableIterator() {
    return variable_.iterator();
  }

  public Iterator getGlobalAttributeIterator() {
    return globalAttribute_.iterator();
  }

  public NcBVariable findVariable(String name) {
    Iterator iter = getVariableIterator();
    NcBVariable var = null;
    while(iter.hasNext()) {
      var = (NcBVariable)iter.next();
      if(var.getName().equals(name)) return var;
    }
    return null;
  }

  private void init() {
    // setup Dimensions
    dimension_ = new Vector();
//    Iterator dimI = dataItem_.getDimensionIterator();
//    while(dimI.hasNext()) {
    dimension_.addAll(dataItem_.getDimensions().stream().map(dim -> new NDimension(this, dim)).collect(Collectors.toList()));
    // setup Variables
    variable_ = new Vector();
//    Iterator varI = dataItem_.getVariableIterator();
//    while(varI.hasNext()) {
    variable_.addAll(dataItem_.getVariables().stream().map(var -> new NVariable(this, var)).collect(Collectors.toList()));
    // setup global attributes
    globalAttribute_ = new Vector();
//    Iterator attI = dataItem_.getGlobalAttributeIterator();
//    while(attI.hasNext()) {
    globalAttribute_.addAll(dataItem_.getGlobalAttributes().stream().map(NAttribute::new).collect(Collectors.toList()));
  }
}