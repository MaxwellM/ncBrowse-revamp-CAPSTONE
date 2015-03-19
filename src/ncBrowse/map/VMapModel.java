/**
 *  $Id: VMapModel.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import java.io.IOException;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.noaa.pmel.sgt.SGLabel;
import gov.noaa.pmel.sgt.dm.*;
import gov.noaa.pmel.util.GeoDate;
import gov.noaa.pmel.util.GeoDateArray;
import gov.noaa.pmel.util.Point2D;
import gov.noaa.pmel.util.Range2D;
import gov.noaa.pmel.util.SoTRange;
import ncBrowse.Debug;
import ncBrowse.NcFile;
import ncBrowse.NcUtil;
import ncBrowse.VisAD.VisADPlotSpecification;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;

/**
 * Variable Map Model. Some definitions for variable mapping.
 * <dl>
 *  <dt>Parameter</dt>
 *  <dd>A netCDF Dimension. They are one-dimensional, integral, and without
 *      units. </dd>
 *
 *  <dt>Axis</dt>
 *  <dd>Either the X or Y graphical axes in SGT</dd>
 *
 *  <dt>Tuple</dt>
 *  <dd>A grouping  of 1 to n numbers that are treated atomically. The X,Y,Z
 *      triplets are a tuple.</dd>
 *
 *  <dt>grid</dt>
 *  <dd>A one or more dimensional collection. For example, <em>h(i,j,k)</em> is a grid of
 *      dimension 3. Each <em>i,j,k</em> tuple references a single value.</dd>
 *
 *  <dt>vector</dt>
 *  <dd>A two  component tuple where each component is a grid.  Typically
 *      used for displaying velocities.</dd>
 *
 *  <dt>variable</dt>
 *  <dd>A netCDF Variable. (independent  variable)</dd>
 *
 *  <dt>dimension-variable</dt>
 *  <dd>A netCDF Variable that is one-dimensional and has the same name as its
 *      single dimension. (dependent  variable)</dd>
 * </dl>
 *
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.38 $, $Date: 2004/05/17 22:49:34 $
 */
public class VMapModel implements Runnable, ChangeListener {
  /**
   * X axis element.
   */
  public static final int X_AXIS = 0;
  /**
   * Y axis element.
   */
  public static final int Y_AXIS = 1;
  /**
   * Contour or area plot element.
   */
  public static final int Z_AXIS = 2;
  public static final int Z_AXISCOLOR = 3;
  public static final int U_COMPONENT = 4;
  public static final int V_COMPONENT = 5;
  public static final int W_COMPONENT = 6;
  public static final int SURFACE = 7;
  public static final int SURFACECOLOR = 8;
  public static final int Z_CONTOUR = 9;
  public static final int VOLUME = 10;
  public static final int LINE_COLOR = 11;
  public static final int ELEMENT_COUNT = 12;

  public static String[] names_ = {
      "X Axis",
      "Y Axis",
      "Z Axis",
      "Z Axis Color",
      "U Vector Component",
      "V Vector Component",
      "W Vector Component",
      "Surface",
      "Surface Color",
      "Contour",
      "Volume",
      "Line Color"};
  public static final int TYPE_UNDEF = 0;
  /**
   * Axis parameters are independent and only one parameter
   * for each axis is varied.
   */
  public static final int TYPE_1 = 1;
  /**
   * Axis parameters are independent but one or more  axis has two or more
   * parameters that are varied.
   */
  public static final int TYPE_2 = 2;
  /**
   * Axis parameters are not independent.
   */
  public static final int TYPE_3 = 3;
  String errorMessage_ = "";
  private String name_ = null;
  /**
   * Object array is treated like an associative array
   */
  Object[] element_ = new Object[ELEMENT_COUNT];

  /**
   *@link aggregation
   *     @associates <{VMapParameter}>
   * @supplierCardinality *
   * @label vmParameter_
   */
  private Hashtable vmParameter_;

  /**
   * @link aggregation
   * @label editor_
   */
  private VMapModelEditor editor_ = null;
  private boolean xReversed_ = false;
  private boolean yReversed_ = false;
  private boolean zReversed_ = false;
  private VisADPlotSpecification plotSpec;

  SGTData[] dataset;
  private SGTGrid uComp_;
  private SGTGrid vComp_;
//  private SGTGrid wComp_;
  private LThreeDGrid uComp3D_;
  private LThreeDGrid vComp3D_;
  private LThreeDGrid wComp3D_;
  private LThreeDGrid volume_;

  private Vector changeListeners_ = new Vector();
  private ChangeEvent event_ = new ChangeEvent(this);

  /**
   * @label ncFile_
   */
  private NcFile ncFile_;
  private boolean processed_ = false;
  private boolean showEditor_ = false;

  private boolean batchOn_ = false;
  private boolean changed_ = false;

  private GeoDate refDate_;
  private boolean is624_;
  private int[] time2_;
  private int increment_;

  /**
   * Constructor.
   *
   * @param ncFile netCDF file to create map from
   */
  public VMapModel(NcFile ncFile) {
    reset();
    ncFile_ = ncFile;
    editor_ = new VMapModelEditor(this, null);
    vmParameter_ = new Hashtable();
  }

//    public void createEditor() {
//      editor_ = new VMapModelEditor(this);
//    }

  /**
   * Get element name.
   *
   * @param element element index
   * @return element title
   */
  static public String getTitle(int element) {
    if(element < 0 || element >= ELEMENT_COUNT) {
      return null;
    }
    return names_[element];
  }

  /**
   * Set VMap name
   * @param name VMap name
   */
  public void setName(String name) {
    name_ = name;
  }

  /**
   * Get VMap name.
   *
   * @return name
   */
  public String getName() {
    return name_;
  }

  /**
   * test for dimension match
   *
   * @param type element type
   * @param ncDim netCDF dimension
   * @return true if match
   */
  public boolean hasMatch(int type, Dimension ncDim) {
    boolean axis = (type == X_AXIS) || (type == Y_AXIS) || (type == Z_AXIS);
    String name = ncDim.getName();
    for(int i = 0; i < ELEMENT_COUNT; i++) {
      if(axis || (!axis && (i == X_AXIS || i == Y_AXIS || i == Z_AXIS))) {
        if((i != type) && (element_[i] != null)) {
          List al = getDimensionList(i);
          for(int j = 0; j < al.size(); j++) {
            String dimName = ((Dimension)al.get(j)).getName();
            if(name.equals(dimName)) {
              return true;
            }
          }
        } // if i type
      } // if axis
    } // for
    return false;
  }

  /**
   * Run thread
   */
  public void run() {
    String name = ncFile_.getFileName();
    //
    // open Informational Dialog
    //
    JOptionPane oPane = new JOptionPane("Reading Axes for " + name_ +
                                        " from " + name,
                                        JOptionPane.INFORMATION_MESSAGE,
                                        JOptionPane.DEFAULT_OPTION);
    JDialog dPane = oPane.createDialog(editor_, "Information");
    dPane.setModal(false);
    dPane.setVisible(true);
    //
    process();
    if(Debug.DEBUG) {
      for(Enumeration e = vmParameter_.elements(); e.hasMoreElements(); ) {
        System.out.println(((VMapParameter)e.nextElement()).toString());
      }
      System.out.println("VMapModel:  run(): editor_.init()");
    }
    editor_.init();
    if(Debug.DEBUG) {
      System.out.println("VMapModel:  run(): editor_.setVisible()");
    }
    if(showEditor_) {
      editor_.setVisible(true);
      //
      // close Informational Dialog
      //
    }
    dPane.setVisible(false);
    dPane.dispose();
  }

  /**
   *  process VMap building VMapParameter list
   */
  public void process() {
    if(processed_) {
      return;
    }
    //
    // process VariableMap building VMapParameter list
    //
    // process dimensions for X_AXIS and Y_AXIS elements
    //
    processXorY(X_AXIS);
    processXorY(Y_AXIS);
    processOther(LINE_COLOR);
    processOther(U_COMPONENT);
    processOther(V_COMPONENT);
    if(is3DLineData() && !isVolumeData()) {
      processOther(Z_AXIS);
      //setLeftOverRangeAllowed();
      if(is3DLineColorData()) {
        processOther(Z_AXISCOLOR);
      }
    } else if(is3DGridData()) {
      processOther(SURFACE);
      if(is3DGridColorData()) {
        processOther(SURFACECOLOR);
      }
    } else if(is3DVectorData()) {
      processOther(W_COMPONENT);
      processXorY(Z_AXIS);
      //if (is3DGridColorData())
      //	processOther(SURFACECOLOR);
    } else if(isVolumeData()) {
      processXorY(Z_AXIS);
      processOther(VOLUME);
    } else {
      processOther(Z_CONTOUR);
      //
      // check for length == 1
      //
    }
    Enumeration pe = vmParameter_.elements();
    while(pe.hasMoreElements()) {
      VMapParameter vmp = (VMapParameter)pe.nextElement();
      if(vmp.getLength() == 1) {
        vmp.setRangeAllowed(false);
      }
    }
    processed_ = true;
  }

  private void processXorY(int type) {
    Object obj = element_[type];
    VMapParameter vParam;
    String name;
    boolean match;
    if(obj instanceof Dimension) {
      name = ((Dimension)obj).getName();
      match = hasMatch(type, (Dimension)obj);
      if(vmParameter_.containsKey(name)) {
        vParam = (VMapParameter)vmParameter_.get(name);
        if(match) {
          vParam.setRangeAllowed(true);
        }
        return;
      }
      vParam = new VMapParameter(ncFile_, obj, match);
      vParam.setGroup(type);
      vParam.addChangeListener(this);
      vmParameter_.put(name, vParam);
    } else { // Variable
      List al = ((Variable)obj).getDimensions();
      for(int j = 0; j < al.size(); j++) {
        Dimension ncDim = (Dimension)al.get(j);
//        Variable ncVar = ncDim.getCoordinateVariable();
        Variable ncVar = ncFile_.findVariable(ncDim.getName());
        name = ncDim.getName();
        match = hasMatch(type, ncDim);
        if(vmParameter_.containsKey(name)) {
          vParam = (VMapParameter)vmParameter_.get(name);
          if(match) {
            vParam.setRangeAllowed(true);
          }
          continue;
        }
        if(ncVar == null) {
          vParam = new VMapParameter(ncFile_, ncDim, match);
        } else {
          vParam = new VMapParameter(ncFile_, ncVar, match);
        }
        vParam.setGroup(type);
        vParam.addChangeListener(this);
        vmParameter_.put(name, vParam);
      } // for al
    } // Dimension
  }

  private void processOther(int type) {
    Object obj = element_[type];
    VMapParameter vParam;
    String name;
    if(obj == null) {
      return;
    }
    if(obj instanceof Dimension) {
      name = ((Dimension)obj).getName();
      boolean match = hasMatch(type, (Dimension)obj);
      if(vmParameter_.containsKey(name)) {
        vParam = (VMapParameter)vmParameter_.get(name);
        if(match) {
          vParam.setRangeAllowed(true);
        }
        return;
      }
      vParam = new VMapParameter(ncFile_, obj, match);
      vParam.setGroup(type);
      vParam.addChangeListener(this);
      vmParameter_.put(name, vParam);
    } else {
      List al = ((Variable)obj).getDimensions();
      for(int j = 0; j < al.size(); j++) {
        Dimension ncDim = (Dimension)al.get(j);
//        Variable ncVar = ncDim.getCoordinateVariable();
        Variable ncVar = ncFile_.findVariable(ncDim.getName());
        name = ncDim.getName();
        if(vmParameter_.containsKey(name)) {
          continue;
        }
        if(ncVar == null) {
          vParam = new VMapParameter(ncFile_, ncDim, false);
        } else {
          vParam = new VMapParameter(ncFile_, ncVar, false);
        }
        vParam.setGroup(type);
        vParam.addChangeListener(this);
        vmParameter_.put(name, vParam);
      }
    }
  }

/*  private void setLeftOverRangeAllowed() {
    int[] keepAxes = {
        X_AXIS, Y_AXIS, Z_AXIS};
    for(Enumeration e = vmParameter_.elements(); e.hasMoreElements(); ) {
      for(int i = 0; i < 3; i++) {
        int type = keepAxes[i];
        Object obj = element_[type];
        VMapParameter vParam;
        String name;
        if(obj == null) {
          return;
        }
        if(obj instanceof Dimension) {
          name = ((Dimension)obj).getName();
          boolean match = hasMatch(type, (Dimension)obj);
          if(vmParameter_.containsKey(name)) {
            vParam = (VMapParameter)vmParameter_.get(name);
            if(match) {
              vParam.setRangeAllowed(true);
            }
            return;
          }
          vParam = new VMapParameter(ncFile_, obj, match);
          vParam.setGroup(type);
          vParam.addChangeListener(this);
          vmParameter_.put(name, vParam);
        } else {
          List al = ((Variable)obj).getDimensions();
          for(int j = 0; j < al.size(); j++) {
            Dimension ncDim = (Dimension)al.get(j);
            Variable ncVar = ncDim.getCoordinateVariable();
            name = ncDim.getName();
            if(vmParameter_.containsKey(name)) {
              continue;
            }
            if(ncVar == null) {
              vParam = new VMapParameter(ncFile_, ncDim, false);
            } else {
              vParam = new VMapParameter(ncFile_, ncVar, false);
            }
            vParam.setGroup(type);
            vParam.addChangeListener(this);
            vmParameter_.put(name, vParam);
          }
        }
      }
    }
  } */

  /**
   * Get iterator.
   *
   * @return Iterator to sorted VMap parameters
   */
  public Iterator getSortedVMapParameters() {
    SortedSet ss = new TreeSet();
    for(Enumeration e = vmParameter_.elements(); e.hasMoreElements(); ) {
      ss.add(e.nextElement());
    }
    return ss.iterator();
  }

  /**
   * Get VMap netCDF file
   *
   * @return netCDF file
   */
  public NcFile getNcFile() {
    return ncFile_;
  }

  /**
   * Show the vmap editor
   *
   * @param show show VMap editor if true
   */
  public void setShowEditor(boolean show) {
    showEditor_ = show;
  }

  /**
   * Is VMap editor shown?
   *
   * @return true if VMap editor is shown
   */
  public boolean isShowEditor() {
    return showEditor_;
  }

  /**
   * get model editor.
   *
   * @return model editor
   */
  public VMapModelEditor getModelEditor() {
    return editor_;
  }

  /**
   * Get VMap parameters
   *
   * @return parameters
   */
  public Enumeration getVMapParameters() {
    return vmParameter_.elements();
  }

  /**
   * get parameter count
   * @return count
   */
  public int getVMapParameterCount() {
    return vmParameter_.size();
  }

  /**
   * Set X axis reversed state.
   *
   * @param xr true for reversed X axis
   */
  public void setXReversed(boolean xr) {
    if(xReversed_ != xr) {
      xReversed_ = xr;
      stateChanged();
    }
  }

  /**
   * Is X axis reversed?
   *
   * @return true if X is reversed
   */
  public boolean isXReversed() {
    return xReversed_;
  }

  /**
   * Set Y axis reversed state.
   *
   * @param yr true for reversed Y axis
   */
  public void setYReversed(boolean yr) {
    if(yReversed_ != yr) {
      yReversed_ = yr;
      stateChanged();
    }
  }

  /**
   * Is Y axis reversed?
   * @return true if Y is reversed
   */
  public boolean isYReversed() {
    return yReversed_;
  }

  public void setZReversed(boolean yr) {
    if(zReversed_ != yr) {
      zReversed_ = yr;
      stateChanged();
    }
  }

  public boolean isZReversed() {
    return zReversed_;
  }

  /**
   * Get SGT data object.
   *
   * @return data object
   */
  public SGTData[] getSGTData() {
    refDate_ = ncFile_.getRefDate();
    is624_ = ncFile_.is624();
    time2_ = ncFile_.getTime2();
    increment_ = ncFile_.getIncrement();
    if(isLineData()) {
      dataset = new SGTData[1];
      dataset[0] = new VMapLine();
      updateLineData();
      return dataset;
    } else if(is3DLineData() && !isVolumeData()) {
      dataset = new SGTData[2];
      dataset[0] = new VMapTuple();
      updateTupleData((VMapTuple)dataset[0], Z_AXIS);
      if(is3DLineColorData()) {
        dataset[1] = new VMapTuple();
        updateTupleData((VMapTuple)dataset[1], Z_AXISCOLOR);
      } else {
        dataset[1] = null;
      }
      return dataset;
    } else if(isGridData()) {
      dataset = new SGTData[1];
      dataset[0] = new VMapGrid();
      updateGridData((VMapGrid)dataset[0], Z_CONTOUR);
      return dataset;
    } else if(is3DGridData()) {
      dataset = new SGTData[2];
      dataset[0] = new VMapGrid();
      updateGridData((VMapGrid)dataset[0], SURFACE);
      if(is3DGridColorData()) {
        dataset[1] = new VMapGrid();
        updateGridData((VMapGrid)dataset[1], SURFACECOLOR);
      } else {
        dataset[1] = null;
      }
      return dataset;
    } else if(isVectorData()) {
      uComp_ = new VMapGrid();
      vComp_ = new VMapGrid();
      dataset = new SGTData[1];
      dataset[0] = new SGTVector();
      updateSGTVector();
      return dataset;
    } else if(is3DVectorData()) {
      if(!isFull3DVectorData()) {
        uComp_ = new VMapGrid();
        vComp_ = new VMapGrid();
//        wComp_ = new GridData();
        dataset = new SGTData[1];
        dataset[0] = new SGT3DVector();
        updateSGT3DVector();
      } else {
        uComp3D_ = new LThreeDGrid();
        vComp3D_ = new LThreeDGrid();
        wComp3D_ = new LThreeDGrid();
        dataset = new SGTFull3DVector[1];
        dataset[0] = new SGTFull3DVector();
        updateSGTFull3DVector();
      }
      return dataset;
    } else if(isVolumeData()) {
      volume_ = new LThreeDGrid();
      dataset = new ThreeDGrid[1];
      dataset[0] = volume_;
      updateVolume();
      return dataset;
    }
    return null;
  }

  /**
   * Is VMap line data?
   *
   * @return true if SGTLine
   */
  public boolean isLineData() {
    return(element_[Z_CONTOUR] == null) && (element_[SURFACE] == null) &&
        (element_[U_COMPONENT] == null) &&
        (element_[V_COMPONENT] == null) && (element_[Z_AXIS] == null);
  }

  public boolean isVolumeData() {
    return(element_[VOLUME] != null);
  }

  /**
   * Is VMap grid data?
   *
   * @return true if SGTGrid
   */
  public boolean isGridData() {
    return element_[Z_CONTOUR] != null;
  }

  public boolean is3D() {
    return is3DGridData() || is3DLineData() || is3DVectorData();
  }

  public boolean is3DVectorData() {
    return element_[W_COMPONENT] != null || isFull3DVectorData(); // also need to track whether we want a 2D VisAD vector plot
  }

  public boolean isFull3DVectorData() {
    return element_[W_COMPONENT] != null && element_[Z_AXIS] != null;
  }

  public boolean is3DLineData() {
    return element_[Z_AXIS] != null && element_[U_COMPONENT] == null;
  }

  public boolean is3DGridData() {
    return element_[SURFACE] != null;
  }

  public boolean is3DGridColorData() {
    return element_[SURFACECOLOR] != null;
  }

  public boolean is3DLineColorData() {
    return element_[Z_AXISCOLOR] != null;
  }

  /**
   *Is VMap vector data?
   *
   * @return true if SGTVector
   */
  public boolean isVectorData() {
    return(!is3DVectorData() && (element_[U_COMPONENT] != null) &&
           (element_[V_COMPONENT] != null));
  }

  public boolean hasColor() {
    return element_[Z_AXISCOLOR] != null || element_[SURFACECOLOR] != null ||
        element_[VOLUME] != null;
  }

  /**
   * Reset elements. Setting all elements to <code>null</code>.
   */
  public void reset() {
    for(int i = 0; i < ELEMENT_COUNT; i++) {
      element_[i] = null;
    }
  }

  /**
   * Method to determine what type of grid is defined
   * by the X_AXIS, Y_AXIS, and Z_AXIS elements. The
   * legal types are: TYPE_1 - Axis parameters are independent
   * and only one parameter for each axis is varied, TYPE_2 -
   * Axis parameters are indepenedent but one or more axis has two or
   * more parameters taht are varied, and TYPE_3 - Axis parameters
   * are not independent.
   *
   * @return grid type
   */
  public int getGridType() {
    return gridType();
  }

  /**
   * Set element to a netCDf variable or dimension
   *
   * @param obj netCDF variable or dimension
   * @param type element type
   */
  public void setElement(Object obj, int type) {
    element_[type] = obj;
  }

  /**
   * Get element value.
   *
   * @param type element type
   * @return element
   */
  public Object getElement(int type) {
    return element_[type];
  }

  /**
   * get element name
   *
   * @param type element type
   * @return name
   */
  public String getElementName(int type) {
    if(element_[type] instanceof Dimension) {
      return((Dimension)element_[type]).getName();
    } else if(element_[type] instanceof Variable) {
      return((Variable)element_[type]).getName();
    } else {
      return "Bad Value";
    }
  }

  /**
   * get latest error message
   *
   * @return error message
   */
  public String getErrorMessage() {
    return errorMessage_;
  }

  /**
   * Is element not null?
   * @param type element  type
   * @return true if element is not <code>null</code>
   */
  public boolean isSet(int type) {
    return element_[type] != null;
  }

  public boolean isDimension(int type) {
    return element_[type] instanceof Dimension;
  }

  public boolean isDimensionVariable(int type) {
    List dims = getDimensionList(type);
    //try {
    if(element_[type] instanceof Variable) {
      Variable var = (Variable)element_[type];
      String varName = var.getName();
      for(int i = 0; i < dims.size(); i++) {
        Dimension ncDim = (Dimension)dims.get(i);
        String name = ncDim.getName();
        if(varName.equals(name)) {
          return true;
        }
      }
      //  }
      //  catch (Exception ex) {
      //  	return false;
      //  }
    }
    return false;
  }

  /**
   * Method to determine what type of grid is defined
   * by the X_AXIS, Y_AXIS, and Z_CONTOUR elements. The
   * legal types are: TYPE_1 - Axis parameters are independent
   * and only one parameter for each axis is varied, TYPE_2 -
   * Axis parameters are independent but one or more axis has two or
   * more parameters that are varied, and TYPE_3 - Axis parameters
   * are not independent.
   *
   * @return grid type
   */
  private int gridType() {
    if(!(isValid() && (isGridData() || is3DGridData()))) {
      return TYPE_UNDEF;
    }
    // setup
    List xDims = getDimensionList(X_AXIS);
    List yDims = getDimensionList(Y_AXIS);
    List zDims = getDimensionList(Z_CONTOUR);
    List zDims3D = getDimensionList(SURFACE);
    boolean[] xVaries = new boolean[xDims.size()];
    boolean[] yVaries = new boolean[yDims.size()];
    boolean[] zVaries = new boolean[zDims.size()];
    boolean[] zVaries3D = new boolean[zDims3D.size()];
    Dimension ncDim = null;
    for(int i = 0; i < xDims.size(); i++) {
      ncDim = (Dimension)xDims.get(i);
      xVaries[i] = hasMatch(X_AXIS, ncDim);
      if(ncDim.getLength() <= 1) {
        xVaries[i] = false;
      }
    }
    for(int i = 0; i < yDims.size(); i++) {
      ncDim = (Dimension)yDims.get(i);
      yVaries[i] = hasMatch(Y_AXIS, ncDim);
      if(ncDim.getLength() <= 1) {
        yVaries[i] = false;
      }
    }
    if(!is3DGridData()) {
      for(int i = 0; i < zDims.size(); i++) {
        ncDim = (Dimension)zDims.get(i);
        zVaries[i] = hasMatch(Z_CONTOUR, ncDim);
        if(ncDim.getLength() <= 1) {
          zVaries[i] = false;
        }
      }
    } else {
      for(int i = 0; i < zDims3D.size(); i++) {
        ncDim = (Dimension)zDims3D.get(i);
        zVaries3D[i] = hasMatch(SURFACE, ncDim);
        if(ncDim.getLength() <= 1) {
          zVaries3D[i] = false;
        }
      }
    }
    /**
     *
     */
    if(Debug.DEBUG) {
      System.out.println(getTitle(X_AXIS));
      for(int i = 0; i < xDims.size(); i++) {
        System.out.println("   " + ((Dimension)xDims.get(i)).getName() +
                           ", varies=" + xVaries[i]);
      }
      System.out.println(getTitle(Y_AXIS));
      for(int i = 0; i < yDims.size(); i++) {
        System.out.println("   " + ((Dimension)yDims.get(i)).getName() +
                           ", varies=" + zVaries[i]);
      }
      System.out.println(getTitle(Z_CONTOUR));
      for(int i = 0; i < zDims.size(); i++) {
        System.out.println("   " + ((Dimension)zDims.get(i)).getName() +
                           ", varies=" + zVaries[i]);
      }
    }
    /**
     *
     */
    // test for TYPE_3
    String name = null;
    for(int i = 0; i < xDims.size(); i++) {
      if(xVaries[i]) {
        name = ((Dimension)xDims.get(i)).getName();
        for(int j = 0; j < yDims.size(); j++) {
          if(yVaries[j]) {
            if(name.equals(((Dimension)yDims.get(j)).getName())) {
              return TYPE_3; //  axes are not independent
            }
          }
        } // j
      }
    } // i

    // test for TYPE_2
    int count = 0;
    for(int i = 0; i < xDims.size(); i++) {
      if(xVaries[i]) {
        count++;
      }
    }
    if(count > 1) {
      return TYPE_2;
    }
    count = 0;
    for(int i = 0; i < yDims.size(); i++) {
      if(yVaries[i]) {
        count++;
      }
    }
    if(count > 1) {
      return TYPE_2;
    }

    // if not TYPE_3 or TYPE_2 ...
    return TYPE_1;
  }

  /**
   * Is VMap valid?
   * @return true if VMap is valid
   */
  public boolean isValid() {
    /**
     * Check for required entries.
     */
    if(element_[X_AXIS] == null) {
      errorMessage_ = "X Axis must have a variable or dimension assigned.";
      return false;
    }
    if(element_[Y_AXIS] == null) {
      errorMessage_ = "Y Axis must have a variable or dimension assigned.";
      return false;
    }

    if((element_[X_AXIS] != null && element_[Y_AXIS] != null && element_[Z_AXIS] != null) &&
       ((element_[U_COMPONENT] != null) &&
        ((element_[V_COMPONENT] == null) || element_[W_COMPONENT] == null))) {
      errorMessage_ = "U, V, and W vector components must be specified.";
      return false;
    }

    if(((element_[U_COMPONENT] != null) &&
        (element_[V_COMPONENT] == null)) ||
       ((element_[V_COMPONENT] != null) &&
        (element_[U_COMPONENT] == null))) {
      errorMessage_ = "Both U and V vector components must be specified.";
      return false;
    }

    if(((element_[W_COMPONENT] != null) &&
        ((element_[V_COMPONENT] == null) || element_[U_COMPONENT] == null))) {
      errorMessage_ = "U, V, and W vector components must be specified.";
      return false;
    }

    if(((element_[W_COMPONENT] != null) && element_[V_COMPONENT] != null) &&
       element_[U_COMPONENT] != null) {
      if(element_[Z_AXIS] == null) {
        errorMessage_ = "Z Axis must have a variable or dimension assigned.";
        return false;
      }
    }

    if(element_[VOLUME] != null) {
      if(element_[Z_AXIS] == null) {
        errorMessage_ = "Z Axis must have a variable or dimension assigned.";
        return false;
      }
    }
    /**
     * Check for missing dimensions.  All entries must have at least one match.
     */
    for(int i = 0; i < ELEMENT_COUNT; i++) {
      if(element_[i] == null) {
        continue;
      }
      if(element_[i] instanceof Dimension) {
        if(!hasMatch(i, (Dimension)element_[i])) {
          errorMessage_ = "Dimension " + ((Dimension)element_[i]).getName() +
              " must have at least one match with an axis.";
          return false;
        }
      } else if(element_[i] instanceof Variable) {
        List al = ((Variable)element_[i]).getDimensions();
        boolean anyMatch = false;
        for(int j = 0; j < al.size(); j++) {
          if(hasMatch(i, (Dimension)al.get(j))) {
            anyMatch = true;
          }
        }
        if(!anyMatch) {
          errorMessage_ = "Variable " + ((Variable)element_[i]).getName() +
              " must match at least one of its dimensions in another entry.";
          return false;
        }
      }
    }
    /**
     * U and V must have the same dimensions that are active.  By active I mean
     * dimensions that match a dimension in the X or Y axes.
     */
    if(element_[U_COMPONENT] != null) {
      Variable uVel = (Variable)element_[U_COMPONENT];
      Variable vVel = (Variable)element_[V_COMPONENT];
      List uDim = uVel.getDimensions();
      List vDim = vVel.getDimensions();
      for(int i = 0; i < uDim.size(); i++) {
        if(hasMatch(U_COMPONENT, (Dimension)uDim.get(i))) {
          String name = ((Dimension)uDim.get(i)).getName();
          boolean match = false;
          for(int j = 0; j < vDim.size(); j++) {
            if(name.equals(((Dimension)vDim.get(j)).getName())) {
              match = true;
            }
          }
          if(!match) {
            errorMessage_ = "U component dimension " + name +
                " not matched in V component";
            return false;
          }
        }
      }
      for(int i = 0; i < vDim.size(); i++) {
        if(hasMatch(V_COMPONENT, (Dimension)vDim.get(i))) {
          String name = ((Dimension)vDim.get(i)).getName();
          boolean match = false;
          for(int j = 0; j < uDim.size(); j++) {
            if(name.equals(((Dimension)uDim.get(j)).getName())) {
              match = true;
            }
          }
          if(!match) {
            errorMessage_ = "V component dimension " + name +
                " not matched in U component";
            return false;
          }
        }
      }

    } else if(element_[W_COMPONENT] != null) {
      Variable uVel = (Variable)element_[U_COMPONENT];
      Variable vVel = (Variable)element_[V_COMPONENT];
      Variable wVel = (Variable)element_[W_COMPONENT];
      List uDim = uVel.getDimensions();
      List vDim = vVel.getDimensions();
      List wDim = wVel.getDimensions();
      for(int i = 0; i < uDim.size(); i++) {
        if(hasMatch(U_COMPONENT, (Dimension)uDim.get(i))) {
          String name = ((Dimension)uDim.get(i)).getName();
          boolean match = false;
          for(int j = 0; j < vDim.size(); j++) {
            if(name.equals(((Dimension)vDim.get(j)).getName())) {
              match = true;
            }
          }
          if(!match) {
            errorMessage_ = "U component dimension " + name +
                " not matched in W component";
            return false;
          }
        }
      }
      for(int i = 0; i < vDim.size(); i++) {
        if(hasMatch(V_COMPONENT, (Dimension)vDim.get(i))) {
          String name = ((Dimension)vDim.get(i)).getName();
          boolean match = false;
          for(int j = 0; j < uDim.size(); j++) {
            if(name.equals(((Dimension)uDim.get(j)).getName())) {
              match = true;
            }
          }
          if(!match) {
            errorMessage_ = "V component dimension " + name +
                " not matched in W component";
            return false;
          }
        }
      }
      for(int i = 0; i < wDim.size(); i++) {
        if(hasMatch(W_COMPONENT, (Dimension)wDim.get(i))) {
          String name = ((Dimension)wDim.get(i)).getName();
          boolean match = false;
          for(int j = 0; j < uDim.size(); j++) {
            if(name.equals(((Dimension)uDim.get(j)).getName())) {
              match = true;
            }
          }
          if(!match) {
            errorMessage_ = "W component dimension " + name +
                " not matched in U component";
            return false;
          }
          for(int j = 0; j < vDim.size(); j++) {
            if(name.equals(((Dimension)vDim.get(j)).getName())) {
              match = true;
            }
          }
          if(!match) {
            errorMessage_ = "W component dimension " + name +
                " not matched in V component";
            return false;
          }
        }
      }

    }

    //if(name_ == null) {
    //  errorMessage_ = "Variable Map must be named.";
    //  return false;
    //}
    errorMessage_ = "";
    return true;
  }

  private List getDimensionList(int element) {
    Object obj = element_[element];
    if(obj instanceof Variable) {
      return((Variable)obj).getDimensions();
    } else {
      List li = new ArrayList(1);
      li.add(obj);
      return li;
    }
  }

  public String getSGTDataTitle() {
    Object array;
    Object start;
    Object end;
    VMapParameter vParam;
    SoTRange range;
    StringBuffer sbuf = new StringBuffer(name_);
    sbuf.append(" [");
    Enumeration e = vmParameter_.elements();
    boolean first = true;
    while(e.hasMoreElements()) {
      vParam = (VMapParameter)e.nextElement();
      array = vParam.getValues();
      range = vParam.getSoTRange();
      if(first) {
        first = false;
      } else {
        sbuf.append(", ");
      }

      sbuf.append(vParam.getName() + "=");
      start = getValue(array, range.getStartObject());
      if(vParam.isSingle()) {
        sbuf.append(NcUtil.valueAsString(start));
      } else {
        sbuf.append(NcUtil.valueAsString(start));
        sbuf.append(";");
        end = getValue(array, range.getEndObject());
        sbuf.append(NcUtil.valueAsString(end));
      }
    }

    sbuf.append("]");
    return sbuf.toString();
  }

  private void updateLineData() {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.updateLineData()");
    }
    SGTMetaData[] meta = new SGTMetaData[2];
    double[][] data = new double[2][];
    double[] associatedData = null;
    GeoDate[] tData = null;
    boolean[] isTime = {
        false, false};
    boolean[] reversed = {
        xReversed_, yReversed_};
    Object[] element = new Object[2];
    String[] label = {
        "X Axis", "Y Axis", "Line Color"};
    VMapParameter vParam;
    Dimension ncDim;
    Variable ncVar;
    Attribute attr;
    String units;
    String title = getSGTDataTitle();
    String id = ncFile_.getPathName() + "-" + name_;
    element[0] = element_[X_AXIS];
    element[1] = element_[Y_AXIS];

    VMapLine ld = (VMapLine)dataset[0];

    SGLabel keyTitle = ld.getKeyTitle();
    if(keyTitle == null) {
      keyTitle = new SGLabel("line data", title, new Point2D.Double(0.0, 0.0));
      ld.setKeyTitle(keyTitle);
    } else {
      keyTitle.setText(title);
    }
    //
    // loop over X_AXIS and Y_AXIS
    //
    for(int j = 0; j < 2; j++) {
      if(element[j] instanceof Dimension) {
        ncDim = (Dimension)element[j];
        vParam = (VMapParameter)vmParameter_.get(ncDim.getName());
        SoTRange range = vParam.getSoTRange();
        int min = ((Number)range.getStart().getObjectValue()).intValue();
        int max = ((Number)range.getEnd().getObjectValue()).intValue();
        data[j] = new double[max - min + 1];
        int count = 0;
        for(int i = min; i <= max; i++) {
          data[j][count] = (double)i;
          count++;
        }
        if(Debug.DEBUG) {
          System.out.println(label[j] + " (dimension):" +
                             data[j].length + " points");
        }
        meta[j] = new SGTMetaData(ncDim.getName(), "", reversed[j], false);
      } else {
        ncVar = (Variable)element[j];
        isTime[j] = ncFile_.isVariableTime(ncVar);
        if(isTime[j]) {
          tData = createGeoDateArray(ncVar);
          if(Debug.DEBUG) {
            System.out.println(label[j] +
                               " (time): " + tData.length + " points");
          }
        } else {
          data[j] = createDoubleArray(ncVar, false, -1, -1, -1);
          if(Debug.DEBUG) {
            System.out.println(label[j] + ": " + data[j].length + " points");
          }
        }
        attr = ncVar.findAttribute("units");
        if(attr != null) {
          units = attr.getStringValue();
        } else {
          units = "";
        }
        meta[j] = new SGTMetaData(ncVar.getName(), units, reversed[j], false);
      }
    }
    //
    // test for LINE_COLOR
    //
    if(element_[LINE_COLOR] != null) {
      ncVar = (Variable)element_[LINE_COLOR];
      associatedData = createDoubleArray(ncVar, false, -1, -1, -1);
      if(Debug.DEBUG) {
        System.out.println(label[2] + ": " + associatedData.length + " points");
      }
    }

    ld.setBatch(true);
    ld.setTitle(title);
    ld.setId(id);
    ld.setXMetaData(meta[0]);
    ld.setYMetaData(meta[1]);
    if(isTime[0]) {
      ld.setTimeArray(true, tData);
      ld.setYArray(data[1]);
    } else {
      if(isTime[1]) {
        ld.setXArray(data[0]);
        ld.setTimeArray(false, tData);
      } else {
        ld.setXArray(data[0]);
        ld.setYArray(data[1]);
      }
    }
    if(associatedData != null) ld.setAssociatedData(associatedData);
    ld.setBatch(false);
  }

  private void updateTupleData(VMapTuple tuples, int zElem) {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.updateTupleData()");
    }
    SGTMetaData[] meta = new SGTMetaData[3];
    double[][] data = new double[3][];
    GeoDate[] tData = null;
    boolean[] isTime = {
        false, false, false};
    boolean[] reversed = {
        xReversed_, yReversed_, zReversed_};
    Object[] element = new Object[3];
    String[] label = {
        "X Axis", "Y Axis", "Z Axis"};
    VMapParameter vParam;
    Dimension ncDim;
    Variable ncVar;
    Attribute attr;
    String units;
    String title = getSGTDataTitle();
    String id = ncFile_.getPathName() + "-" + name_;
    element[0] = element_[X_AXIS];
    element[1] = element_[Y_AXIS];
    element[2] = element_[zElem];

    SGLabel keyTitle = tuples.getKeyTitle();
    if(keyTitle == null) {
      keyTitle = new SGLabel("3D line data", title, new Point2D.Double(0.0, 0.0));
      tuples.setKeyTitle(keyTitle);
    } else {
      keyTitle.setText(title);
    }
    //
    // loop over X_AXIS, Y_AXIS, and Z_AXIS
    //
    for(int j = 0; j < 3; j++) {
      if(element[j] instanceof Dimension) {
        ncDim = (Dimension)element[j];
        vParam = (VMapParameter)vmParameter_.get(ncDim.getName());
        SoTRange range = vParam.getSoTRange();
        int min = ((Number)range.getStart().getObjectValue()).intValue();
        int max = ((Number)range.getEnd().getObjectValue()).intValue();
        data[j] = new double[max - min + 1];
        int count = 0;
        for(int i = min; i <= max; i++) {
          data[j][count] = (double)i;
          count++;
        }
        if(Debug.DEBUG) {
          System.out.println(label[j] + " (dimension):" +
                             data[j].length + " points");
        }
        meta[j] = new SGTMetaData(ncDim.getName(), "", reversed[j], false);
      } else {
        ncVar = (Variable)element[j];
        isTime[j] = ncFile_.isVariableTime(ncVar);
        if(isTime[j]) {
          tData = createGeoDateArray(ncVar);
          if(Debug.DEBUG) {
            System.out.println(label[j] +
                               " (time): " + tData.length + " points");
          }
        } else {
          data[j] = createDoubleArray(ncVar, false, -1, -1, -1);
          if(Debug.DEBUG) {
            System.out.println(label[j] + ": " + data[j].length + " points");
          }
        }
        attr = ncVar.findAttribute("units");
        if(attr != null) {
          units = attr.getStringValue();
        } else {
          units = "";
        }
        meta[j] = new SGTMetaData(ncVar.getName(), units, reversed[j], false);
      }
    }

    tuples.setBatch(true);
    tuples.setTitle(title);
    tuples.setId(id);
    tuples.setXMetaData(meta[0]);
    tuples.setYMetaData(meta[1]);
    tuples.setZMetaData(meta[2]);
    if(isTime[0]) {
      tuples.setTimeArray(true, tData);
      tuples.setYArray(data[1]);
      tuples.setZArray(data[2]);
    } else {
      if(isTime[1]) {
        tuples.setXArray(data[0]);
        tuples.setTimeArray(false, tData);
      } else {
        tuples.setXArray(data[0]);
        tuples.setYArray(data[1]);
        tuples.setZArray(data[2]);
      }
    }
    tuples.setBatch(false);
  }

  private void update3DGridData(LThreeDGrid grid, int valElem) {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.update3DGridData(grid,element)");
    }
    SGTMetaData[] meta = new SGTMetaData[4];
    double[][] data = new double[4][];
    GeoDate[] tData = null;
    boolean[] isTime = {
        false, false, false, false};
    boolean[] reversed = {
        xReversed_, yReversed_, zReversed_, false};
    Object[] element = new Object[4];
    String[] label = {
        "X Axis", "Y Axis", "Z Axis", "Val Axis"};
    VMapParameter vParam;
    Dimension ncDim;
    List[] dims = new List[4];
    int xIndex = -1;
    int yIndex = -1;
    int zIndex = -1;
    int valIndex = -1;
    int[] valIndexes;
    boolean[] transpose = new boolean[4];
    String name;
    Variable ncVar;
    Attribute attr;
    String units;
    String title = getSGTDataTitle();
    String id = ncFile_.getPathName() + "_" + name_;

    SGLabel keyTitle = grid.getKeyTitle();
    if(keyTitle == null) {
      keyTitle = new SGLabel("grid data", title, new Point2D.Double(0.0, 0.0));
      grid.setKeyTitle(keyTitle);
    } else {
      keyTitle.setText(title);
    }

    label[3] = getTitle(valElem);

    element[0] = element_[X_AXIS];
    element[1] = element_[Y_AXIS];
    element[2] = element_[Z_AXIS];
    element[3] = element_[valElem];
    dims[0] = getDimensionList(X_AXIS);
    dims[1] = getDimensionList(Y_AXIS);
    dims[2] = getDimensionList(Z_AXIS);
    dims[3] = getDimensionList(valElem);
    int size = dims[3].size();
    valIndexes = new int[size];

    //    GridData gd = (GridData)dataset_;

    //
    // find valIndexes, xIndex, and yIndex, and zIndex
    //
    for(int jj = 0; jj < valIndexes.length; jj++) {
      name = ((Dimension)dims[3].get(jj)).getName();
      valIndexes[jj] = -1; // not varied
      for(int l = 0; l < 3; l++) {
        for(int k = 0; k < dims[l].size(); k++) {
          if(name.equals(((Dimension)dims[l].get(k)).getName())) {
            valIndexes[jj] = l + 1;
          }
        }
      }
    }

    for(int j = 0; j < valIndexes.length; j++) {
      if(valIndexes[j] == 1) {
        if(xIndex == -1) {
          xIndex = j;
        } else {
          if(Debug.DEBUG) {
            System.out.println("Grid not type 1");
          }
        }
      }
      if(valIndexes[j] == 2) {
        if(yIndex == -1) {
          yIndex = j;
        } else {
          if(Debug.DEBUG) {
            System.out.println("Grid not type 1");
          }
        }
      }
      if(valIndexes[j] == 3) {
        if(zIndex == -1) {
          zIndex = j;
        } else {
          if(Debug.DEBUG) {
            System.out.println("Grid not type 1");
          }
        }
      }
    }
    if(Debug.DEBUG) {
      System.out.println("xIndex, yIndex = " + xIndex + ", " + yIndex);
      System.out.print("valIndexes = ");
      for(int j = 0; j < valIndexes.length; j++) {
        System.out.print(valIndexes[j]);
        if(j < valIndexes.length - 1) {
          System.out.print(", ");
        }
      }
      System.out.print("\n");
    }
    transpose[0] = false;
    transpose[1] = xIndex > yIndex;
    transpose[2] = false;
    transpose[3] = false;

    //
    // loop over X_AXIS, Y_AXIS, Z_Axis, and valElem
    //
    for(int j = 0; j < 4; j++) {
      meta[j] = null;
      if(element[j] instanceof Dimension) {
        ncDim = (Dimension)element[j];
        vParam = (VMapParameter)vmParameter_.get(ncDim.getName());
        SoTRange range = vParam.getSoTRange();
        int min = ((Number)range.getStart().getObjectValue()).intValue();
        int max = ((Number)range.getEnd().getObjectValue()).intValue();
        data[j] = new double[max - min + 1];
        int count = 0;
        for(int i = min; i <= max; i++) {
          data[j][count] = (double)i;
          count++;
        }
        if(Debug.DEBUG) {
          System.out.println(label[j] + " (dimension):" +
                             data[j].length + " points");
        }
        meta[j] = new SGTMetaData(ncDim.getName(), "", reversed[j], false);
      } else {
        ncVar = (Variable)element[j];
        isTime[j] = ncFile_.isVariableTime(ncVar);
        if(isTime[j]) {
          tData = createGeoDateArray(ncVar);
          if(Debug.DEBUG) {
            System.out.println(label[j] + " (time): " + tData.length +
                               " points");
          }
        } else {
          data[j] = createDoubleArray(ncVar, transpose[j], xIndex, yIndex,
                                      zIndex);
          if(Debug.DEBUG) {
            System.out.println(label[j] + ": " + data[j].length + " points");
          }
        }
        attr = ncVar.findAttribute("units");
        if(attr != null) {
          units = attr.getStringValue();
        } else {
          units = "";
        }
        meta[j] = new SGTMetaData(ncVar.getName(), units, reversed[j], false);
      }
    }

    grid.setBatch(true);
    grid.setTitle(title);
    grid.setId(id);
    grid.setXMetaData(meta[0]);
    grid.setYMetaData(meta[1]);
    grid.setZMetaData(meta[2]);
    grid.setValMetaData(meta[3]);
    if(isTime[0]) {
      grid.setTimeArray(tData);
      grid.setXTime(true);
      grid.setYArray(data[1]);
      grid.setZArray(data[2]);
    } else {
      grid.setXArray(data[0]);
      if(isTime[1]) {
        grid.setYTime(true);
        grid.setTimeArray(tData);
        grid.setZArray(data[2]);
      } else {
        grid.setYArray(data[1]);
        if(isTime[2]) {
          grid.setZTime(true);
          grid.setTimeArray(tData);
        } else {
          grid.setZArray(data[2]);
        }
      }
    }
    grid.setValArray(data[3]);
    grid.setBatch(false);

    forceType1Grid();
  }

  private void updateGridData(VMapGrid grid, int zElem) {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.updateGridData(grid,element)");
    }
    SGTMetaData[] meta = new SGTMetaData[3];
    double[][] data = new double[3][];
    GeoDate[] tData = null;
    boolean[] isTime = {
        false, false, false};
    boolean[] reversed = {
        xReversed_, yReversed_, false};
    Object[] element = new Object[3];
    String[] label = {
        "X Axis", "Y Axis", "Z Axis"};
    VMapParameter vParam;
    Dimension ncDim;
    List[] dims = new List[3];
    int xIndex = -1;
    int yIndex = -1;
    int[] zIndexes;
    boolean[] transpose = new boolean[3];
    String name;
    Variable ncVar;
    Attribute attr;
    String units;
    String title = getSGTDataTitle();
    String id = ncFile_.getPathName() + "-" + name_;

    SGLabel keyTitle = grid.getKeyTitle();
    if(keyTitle == null) {
      keyTitle = new SGLabel("grid data", title, new Point2D.Double(0.0, 0.0));
      grid.setKeyTitle(keyTitle);
    } else {
      keyTitle.setText(title);
    }

    label[2] = getTitle(zElem);

    element[0] = element_[X_AXIS];
    element[1] = element_[Y_AXIS];
    element[2] = element_[zElem];
    dims[0] = getDimensionList(X_AXIS);
    dims[1] = getDimensionList(Y_AXIS);
    dims[2] = getDimensionList(zElem);
    zIndexes = new int[dims[2].size()];

    //    GridData gd = (GridData)dataset_;

    //
    // find zIndexes, xIndex, and yIndex
    //
    for(int j = 0; j < zIndexes.length; j++) {
      name = ((Dimension)dims[2].get(j)).getName();
      zIndexes[j] = -1; // not varied
      for(int l = 0; l < 2; l++) {
        for(int k = 0; k < dims[l].size(); k++) {
          if(name.equals(((Dimension)dims[l].get(k)).getName())) {
            zIndexes[j] = l + 1;
          }
        }
      }
    }
    for(int j = 0; j < zIndexes.length; j++) {
      if(zIndexes[j] == 1) {
        if(xIndex == -1) {
          xIndex = j;
        } else {
          if(Debug.DEBUG) {
            System.out.println("Grid not type 1");
          }
        }
      }
      if(zIndexes[j] == 2) {
        if(yIndex == -1) {
          yIndex = j;
        } else {
          if(Debug.DEBUG) {
            System.out.println("Grid not type 1");
          }
        }
      }
    }
    if(Debug.DEBUG) {
      System.out.println("xIndex, yIndex = " + xIndex + ", " + yIndex);
      System.out.print("zIndexes = ");
      for(int j = 0; j < zIndexes.length; j++) {
        System.out.print(zIndexes[j]);
        if(j < zIndexes.length - 1) {
          System.out.print(", ");
        }
      }
      System.out.print("\n");
    }
    transpose[0] = false;
    transpose[1] = false;
    transpose[2] = xIndex > yIndex;

    //
    // loop over X_AXIS, Y_AXIS, and zElem
    //
    for(int j = 0; j < 3; j++) {
      if(element[j] instanceof Dimension) {
        ncDim = (Dimension)element[j];
        vParam = (VMapParameter)vmParameter_.get(ncDim.getName());
        SoTRange range = vParam.getSoTRange();
        int min = ((Number)range.getStart().getObjectValue()).intValue();
        int max = ((Number)range.getEnd().getObjectValue()).intValue();
        data[j] = new double[max - min + 1];
        int count = 0;
        for(int i = min; i <= max; i++) {
          data[j][count] = (double)i;
          count++;
        }
        if(Debug.DEBUG) {
          System.out.println(label[j] + " (dimension):" +
                             data[j].length + " points");
        }
        meta[j] = new SGTMetaData(ncDim.getName(), "", reversed[j], false);
      } else {
        ncVar = (Variable)element[j];
        isTime[j] = ncFile_.isVariableTime(ncVar);
        if(isTime[j]) {
          tData = createGeoDateArray(ncVar);
          if(Debug.DEBUG) {
            System.out.println(label[j] + " (time): " + tData.length +
                               " points");
          }
        } else {
          data[j] = createDoubleArray(ncVar, transpose[j], xIndex, yIndex, -1);
          if(Debug.DEBUG) {
            System.out.println(label[j] + ": " + data[j].length + " points");
          }
        }
        attr = ncVar.findAttribute("units");
        if(attr != null) {
          units = attr.getStringValue();
        } else {
          units = "";
        }
        meta[j] = new SGTMetaData(ncVar.getName(), units, reversed[j], false);
      }
    }

    grid.setBatch(true);
    grid.setTitle(title);
    grid.setId(id);
    grid.setXMetaData(meta[0]);
    grid.setYMetaData(meta[1]);
    grid.setZMetaData(meta[2]);
    if(isTime[0]) {
      grid.setTimeArray(true, tData);
      grid.setYArray(data[1]);
    } else {
      if(isTime[1]) {
        grid.setXArray(data[0]);
        grid.setTimeArray(false, tData);
      } else {
        grid.setXArray(data[0]);
        grid.setYArray(data[1]);
      }
    }
    grid.setZArray(data[2]);
    grid.setBatch(false);

    forceType1Grid();
  }

  private void updateSGTVector() {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.updateSGTVector()");

    }
    updateGridData((VMapGrid)uComp_, U_COMPONENT);
    updateGridData((VMapGrid)vComp_, V_COMPONENT);

    SGTVector vect = (SGTVector)dataset[0];
    vect.setComponents(uComp_, vComp_);
    String title = getSGTDataTitle();
    vect.setTitle(title);

    SGLabel keyTitle = vect.getKeyTitle();
    if(keyTitle == null) {
      keyTitle = new SGLabel("vector data", title, new Point2D.Double(0.0, 0.0));
      vect.setKeyTitle(keyTitle);
    } else {
      keyTitle.setText(title);
    }

    vect.setId(uComp_.getId());

  }

  private void updateSGTFull3DVector() {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.updateSGTVector()");

      // for full 3D
    }
    int vPsize = this.getVMapParameterCount();
    VMapParameter[] vParam_ = new VMapParameter[vPsize];
    Iterator ip = this.getSortedVMapParameters();
    int rows;
    for(rows = 0; rows < vPsize; rows++) {
      vParam_[rows] = (VMapParameter)ip.next();
    }
    // for (int i=0; i < vParam_.length; i++) {
    // 	// need the set the z value
    //     SoTRange range = vParam.getSoTRange();
    // 	vParam_[i].setSoTRange(range);
    // }

    update3DGridData((LThreeDGrid)uComp3D_, U_COMPONENT);
    update3DGridData((LThreeDGrid)vComp3D_, V_COMPONENT);
    update3DGridData((LThreeDGrid)wComp3D_, W_COMPONENT);

    SGTFull3DVector vect = (SGTFull3DVector)dataset[0];
    vect.setComponents(uComp3D_, uComp3D_, uComp3D_);
    String title = getSGTDataTitle();
    vect.setTitle(title);

    SGLabel keyTitle = vect.getKeyTitle();
    if(keyTitle == null) {
      keyTitle = new SGLabel("vector data", title, new Point2D.Double(0.0, 0.0));
      vect.setKeyTitle(keyTitle);
    } else {
      keyTitle.setText(title);
    }

    vect.setId(uComp3D_.getId());
  }

  private void updateVolume() {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.updateVolume()");

      // for full 3D
    }
    int vPsize = this.getVMapParameterCount();
    VMapParameter[] vParam_ = new VMapParameter[vPsize];
    Iterator ip = this.getSortedVMapParameters();
    int rows;
    for(rows = 0; rows < vPsize; rows++) {
      vParam_[rows] = (VMapParameter)ip.next();
    }
    // for (int i=0; i < vParam_.length; i++) {
    // 	// need the set the z value
    //     SoTRange range = vParam.getSoTRange();
    // 	vParam_[i].setSoTRange(range);
    // }

    update3DGridData((LThreeDGrid)volume_, VOLUME);
  }

  private void updateSGT3DVector() {
    if(Debug.DEBUG) {
      System.out.println("VMapModel.updateSGTVector()");

      // for full 3D
      /*int vPsize = this.getVMapParameterCount();
           VMapParameter[] vParam_ = new VMapParameter[vPsize];
           Iterator ip = this.getSortedVMapParameters();
           int rows ;
           for(rows=0; rows < vPsize; rows++) {
        vParam_[rows] = (VMapParameter)ip.next();
       }
           for (int i=0; i < vParam_.length; i++) {
        // need the set the z value
        vParam_[i].setSoTRange(range);
           }*/

      /*  updateGridData((GridData)uComp_, U_COMPONENT);
        updateGridData((GridData)vComp_, V_COMPONENT);
        updateGridData((GridData)wComp_, W_COMPONENT);

        SGT3DVector vect = (SGT3DVector)dataset[0];
        vect.setComponents(uComp_, vComp_, wComp_);
        String title = getSGTDataTitle();
        vect.setTitle(title);

        SGLabel keyTitle = vect.getKeyTitle();
        if(keyTitle == null) {
       keyTitle = new SGLabel("vector data", title, new Point2D.Double(0.0,0.0));
          vect.setKeyTitle(keyTitle);
        } else {
          keyTitle.setText(title);
        }

        vect.setId(uComp_.getId());*/

    }
  }

  /**
   * Only allow one VMapParameter per group to not be single
   */
  private void forceType1Grid() {
    VMapParameter vParam;
    int group = -1;
    int newgroup = -1;
    boolean isset = false;
    Enumeration e = vmParameter_.elements();

    while(e.hasMoreElements()) {
      vParam = (VMapParameter)e.nextElement();
      newgroup = vParam.getGroup();
      if(newgroup != group) {
        group = newgroup;
        isset = false;
      }
      if(!vParam.isSingle()) {
        if(isset) {
          vParam.setSingle(true);
        }
        isset = true;
      }
    }

  }

  /**
   * Get array of GeoDate objects based on VMapParameter. This is always a
   * 1-D operation.
   *
   * @param ncVar netCDF variable
   * @return array of GeoDate objects
   */
  private GeoDate[] createGeoDateArray(Variable ncVar) {
    int rank = ncVar.getRank();
    int[] origin = new int[rank];
    int[] shape = new int[rank];
    Object min;
    Object max;
    Object array;
    SoTRange range;
    VMapParameter[] vParam = new VMapParameter[rank];
//    Variable[] vars = new Variable[rank];
    List dims = ncVar.getDimensions();

    if(Debug.DEBUG) {
      System.out.println("var: " + ncVar.getName() + ", shape = " +
                         arrayToString(ncVar.getShape()));
    }
    for(int i = 0; i < rank; i++) {
      Dimension ncDim = (Dimension)dims.get(i);
      vParam[i] = (VMapParameter)vmParameter_.get(ncDim.getName());
//      vars[i] = ncDim.getCoordinateVariable();
    }
    for(int i = 0; i < rank; i++) {
      range = vParam[i].getSoTRange();
      min = range.getStart().getObjectValue();
      if(vParam[i].isSingle()) {
        max = min;
      } else {
        max = range.getEnd().getObjectValue();
      }
      if(vParam[i].isDimension()) {
        origin[i] = ((Number)min).intValue();
        shape[i] = ((Number)max).intValue() - origin[i] + 1;
      } else { // variable
        array = vParam[i].getValues();
        origin[i] = getIndex(array, min);
        shape[i] = getIndex(array, max) - origin[i] + 1;
      }
    }
    if(Debug.DEBUG) {
      System.out.println("     origin = " + arrayToString(origin));
      System.out.println("      shape = " + arrayToString(shape));
    }
    return createGeoDateArray(ncVar, 0, origin, shape);
  }

  /**
   * Read the data from ncVar using the VMapParameter shapes. This can be a 1-
   * or 2-D operation.
   *
   * @param ncVar netCDF Variable
   * @param transpose transpose result
   * @param xIndex index to transpose
   * @param yIndex index to transpose
   * @return double array
   */
  private double[] createDoubleArray(Variable ncVar,
                                     boolean transpose,
                                     int xIndex, int yIndex, int zIndex) {
    int rank = ncVar.getRank();
    int[] origin = new int[rank];
    int[] shape = new int[rank];
    Object min;
    Object max;
    Object array;
    SoTRange range;
    VMapParameter[] vParam = new VMapParameter[rank];
    List dims = ncVar.getDimensions();

    if(Debug.DEBUG) {
      System.out.println("var: " + ncVar.getName() +
                         ", shape = " + arrayToString(ncVar.getShape()) +
                         " rank=" + rank);
      System.out.println("xIndex = " + xIndex + " yIndex=" + yIndex);
      System.out.println("zIndex = " + zIndex);
    }
    for(int i = 0; i < rank; i++) {
      Dimension ncDim = (Dimension)dims.get(i);
      String name = ncDim.getName();
      vParam[i] = (VMapParameter)vmParameter_.get(name);
//      vars[i] = ncDim.getCoordinateVariable();
    }

    for(int i = 0; i < rank; i++) {
      range = vParam[i].getSoTRange();
      min = range.getStart().getObjectValue();
      if(vParam[i].isSingle()) {
        max = min;
      } else {
        max = range.getEnd().getObjectValue();
      }
      if(vParam[i].isDimension()) {
        origin[i] = ((Number)min).intValue();
        shape[i] = ((Number)max).intValue() - origin[i] + 1;
      } else { // variable
        array = vParam[i].getValues();
        origin[i] = getIndex(array, min);
        shape[i] = getIndex(array, max) - origin[i] + 1;
      }
    }
    if(Debug.DEBUG) {
      System.out.println("     origin = " + arrayToString(origin));
      System.out.println("      shape = " + arrayToString(shape));
    }
    return createDoubleArray(ncVar, origin, shape, transpose, xIndex, yIndex,
                             zIndex);
  }

  private String arrayToString(int[] val) {
    StringBuffer sbuf = new StringBuffer();
    for(int i = 0; i < val.length; i++) {
      if(i != 0) {
        sbuf.append(",");
      }
      sbuf.append(val[i]);
    }
    return sbuf.toString();
  }

  private Object getValue(Object array, Object val) {
    int index = getIndex(array, val);
    if(val instanceof Long) {
      return new Long(((long[])array)[index]);
    } else if(val instanceof Integer) {
      return new Integer(((int[])array)[index]);
    } else if(val instanceof Short) {
      return new Short(((short[])array)[index]);
    } else if(val instanceof Float) {
      return new Float(((float[])array)[index]);
    } else if(val instanceof Double) {
      return new Double(((double[])array)[index]);
    } else if(val instanceof GeoDate) {
      GeoDate[] tarray = createGeoDateArray(array);
      return new GeoDate(tarray[index]);
    }
    return null;
  }

  private int getIndex(Object array, Object val) {
    boolean arrayIsReversed = false;
    int len;
    int index = 0;
    if(val instanceof Long) {
      //
      // values are long
      //
      long[] longArray = (long[])array;
      long value = ((Long)val).longValue();
      len = longArray.length;
      arrayIsReversed = longArray[len - 1] < longArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= longArray[0]) {
            index = 0;
          } else if(value >= longArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value >= longArray[j] && value <= longArray[j + 1]) {
                if((value - longArray[j]) < (longArray[j + 1] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= longArray[0]) {
            index = 0;
          } else if(value <= longArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value <= longArray[j] && value >= longArray[j + 1]) {
                if((value - longArray[j + 1]) > (longArray[j] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Integer) {
      //
      // values are int
      //
      int[] intArray = (int[])array;
      int value = ((Integer)val).intValue();
      len = intArray.length;
      arrayIsReversed = intArray[len - 1] < intArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= intArray[0]) {
            index = 0;
          } else if(value >= intArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value >= intArray[j] && value <= intArray[j + 1]) {
                if((value - intArray[j]) < (intArray[j + 1] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= intArray[0]) {
            index = 0;
          } else if(value <= intArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value <= intArray[j] && value >= intArray[j + 1]) {
                if((value - intArray[j + 1]) > (intArray[j] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Short) {
      //
      // values are short
      //
      short[] shortArray = (short[])array;
      short value = ((Short)val).shortValue();
      len = shortArray.length;
      arrayIsReversed = shortArray[len - 1] < shortArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= shortArray[0]) {
            index = 0;
          } else if(value >= shortArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value >= shortArray[j] && value <= shortArray[j + 1]) {
                if((value - shortArray[j]) < (shortArray[j + 1] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= shortArray[0]) {
            index = 0;
          } else if(value <= shortArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value <= shortArray[j] && value >= shortArray[j + 1]) {
                if((value - shortArray[j + 1]) > (shortArray[j] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Float) {
      //
      // values are float
      //
      float[] floatArray = (float[])array;
      float value = ((Float)val).floatValue();
      len = floatArray.length;
      arrayIsReversed = floatArray[len - 1] < floatArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= floatArray[0]) {
            index = 0;
          } else if(value >= floatArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value >= floatArray[j] && value <= floatArray[j + 1]) {
                if((value - floatArray[j]) < (floatArray[j + 1] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= floatArray[0]) {
            index = 0;
          } else if(value <= floatArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value <= floatArray[j] && value >= floatArray[j + 1]) {
                if((value - floatArray[j + 1]) > (floatArray[j] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof Double) {
      //
      // values are double
      //
      double[] doubleArray = (double[])array;
      double value = ((Double)val).doubleValue();
      len = doubleArray.length;
      arrayIsReversed = doubleArray[len - 1] < doubleArray[0];
      if(len == 1) {
        index = 0;
      } else {
        if(!arrayIsReversed) {
          if(value <= doubleArray[0]) {
            index = 0;
          } else if(value >= doubleArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value >= doubleArray[j] && value <= doubleArray[j + 1]) {
                if((value - doubleArray[j]) < (doubleArray[j + 1] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        } else {
          if(value >= doubleArray[0]) {
            index = 0;
          } else if(value <= doubleArray[len - 1]) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if(value <= doubleArray[j] && value >= doubleArray[j + 1]) {
                if((value - doubleArray[j + 1]) > (doubleArray[j] - value)) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        }
      }
    } else if(val instanceof GeoDate) {
      //
      // values are GeoDate
      //
      GeoDate start;
      GeoDate end;
      Object time = array;
      GeoDate value = (GeoDate)val;
      GeoDate[] tarray = createGeoDateArray(time);
      len = tarray.length;

      if(len == 1) {
        index = 0;
      } else {
        arrayIsReversed = tarray[len - 1].before(tarray[0]);
        if(!arrayIsReversed) {
          if(value.before(tarray[0]) || value.equals(tarray[0])) {
            index = 0;
          } else if(value.after(tarray[len - 1]) || value.equals(tarray[len - 1])) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if((value.after(tarray[j]) || value.equals(tarray[j])) &&
                 (value.before(tarray[j + 1]) || value.equals(tarray[j + 1]))) {
                if(value.subtract(tarray[j]).before(tarray[j +
                    1].subtract(value))) {
                  index = j;
                } else {
                  index = j + 1;
                }
                break;
              }
            }
          }
        } else {
          if(value.after(tarray[0]) || value.equals(tarray[0])) {
            index = 0;
          } else if(value.before(tarray[len - 1]) ||
                    value.equals(tarray[len - 1])) {
            index = len - 1;
          } else {
            for(int j = 0; j < len - 1; j++) {
              if((value.before(tarray[j]) || value.equals(tarray[j])) &&
                 (value.after(tarray[j + 1]) || value.equals(tarray[j + 1]))) {
                if(value.subtract(tarray[j + 1]).after(tarray[j].subtract(value))) {
                  index = j;
                } else {
                  index = j + 1;
                }
              }
            }
          }
        }
      }

    }
    return index;
  }

  private double[] createDoubleArray(Variable var,
                                     int[] origin,
                                     int[] shape,
                                     boolean transpose,
                                     int xIndex,
                                     int yIndex,
                                     int zIndex) {
    double[] outArray = null;
    double temp;
    Attribute validMinAttr;
    Attribute validMaxAttr;
    Attribute scaleAttr;
    Attribute offsetAttr;
    Attribute missingAttr;
    Attribute fillValueAttr;
    Array marr = null;

    //The Great Transpose Mystery
    try {
      if(transpose && !(isVolumeData() || isFull3DVectorData())) {
        //marr = var.read(origin, shape);//.transpose(xIndex, yIndex); //transpose needs to commented out for 3D stuff
        marr = var.read(origin, shape).transpose(xIndex, yIndex); // use this line for 2D ncBrowse stuff
      } else {
        marr = var.read(origin, shape);
      }
    } catch(Exception ex) {
      ex.printStackTrace();
      //} catch (InvalidRangeException ex) {
      // ex.printStackTrace();
    }
    //
    // try to get important attributes
    //
    validMinAttr = var.findAttribute("valid_min");
    validMaxAttr = var.findAttribute("valid_max");
    scaleAttr = var.findAttribute("scale_factor");
    offsetAttr = var.findAttribute("add_offset");
    missingAttr = var.findAttribute("missing_value");
    fillValueAttr = var.findAttribute("_FillValue");
    //
    // determine scale and offset, if any
    //
    double scale = 1.0;
    double offset = 0.0;
    boolean scale_offset = false;
    if(scaleAttr != null && !scaleAttr.isString()) {
      scale = scaleAttr.getNumericValue().doubleValue();
      scale_offset = true;
    }
    if(offsetAttr != null && !offsetAttr.isString()) {
      offset = offsetAttr.getNumericValue().doubleValue();
      scale_offset = true;
    }
    //
    // determine missing value, if any
    //
    Number missingNumber = null;
    Number validMinNumber = null;
    Number validMaxNumber = null;
    boolean valids = false;
    boolean defaultMissing = true;
    if(missingAttr != null && !missingAttr.isString()) {
      missingNumber = missingAttr.getNumericValue();
      defaultMissing = false;
    } else if(fillValueAttr != null && !fillValueAttr.isString()) {
      missingNumber = fillValueAttr.getNumericValue();
      defaultMissing = false;
    }
    if(validMinAttr != null && validMaxAttr != null &&
       !validMinAttr.isString() && !validMaxAttr.isString()) {
      valids = true;
      validMinNumber = validMinAttr.getNumericValue();
      validMaxNumber = validMaxAttr.getNumericValue();
    }
//    String aType = var.getElementType().getName();
    DataType aType = var.getDataType();
    if(aType == DataType.INT) {
      int missing = Integer.MIN_VALUE;
      int validMin = 0;
      int validMax = 0;
      int[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.intValue();
      }
      if(valids) {
        validMin = validMinNumber.intValue();
        validMax = validMaxNumber.intValue();
      }
      array = (int[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = (int)array[j];
          if(scale_offset) {
            outArray[j] = temp * scale + offset;
          } else {
            outArray[j] = temp;
          }
          if(valids) {
            if(outArray[j] < validMin || outArray[j] > validMax) {
              outArray[j] = Double.NaN;
            }
          }
        }
      } else {
        for(int j = 0; j < array.length; j++) {
          temp = (int)array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    } else if(aType == DataType.SHORT) {
      short missing = Short.MIN_VALUE;
      short validMin = 0;
      short validMax = 0;
      short[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.shortValue();
      }
      if(valids) {
        validMin = validMinNumber.shortValue();
        validMax = validMaxNumber.shortValue();
      }
      array = (short[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = (double)array[j];
          if(scale_offset) {
            outArray[j] = temp * scale + offset;
          } else {
            outArray[j] = temp;
          }
          if(valids) {
            if(outArray[j] < validMin || outArray[j] > validMax) {
              outArray[j] = Double.NaN;
            }
          }
        }
      } else {
        for(int j = 0; j < array.length; j++) {
          temp = (double)array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    } else if(aType == DataType.FLOAT) {
      float missing = 0.9e35f;
      float validMin = 0.0f;
      float validMax = 0.0f;
      float[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.floatValue();
      }
      if(valids) {
        validMin = validMinNumber.floatValue();
        validMax = validMaxNumber.floatValue();
      }
      array = (float[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = (double)array[j];
          if(temp >= missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      } else {
        for(int j = 0; j < array.length; j++) {
          temp = (double)array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    } else if(aType == DataType.DOUBLE) {
      double missing = 0.9e35;
      double validMin = 0.0;
      double validMax = 0.0;
      double[] array = null;
      if(!defaultMissing && missingAttr != null) {
        missing = missingNumber.doubleValue();
      }
      if(valids) {
        validMin = validMinNumber.doubleValue();
        validMax = validMaxNumber.doubleValue();
      }
      array = (double[])marr.copyTo1DJavaArray();
      outArray = new double[array.length];
      if(defaultMissing) {
        for(int j = 0; j < array.length; j++) {
          temp = array[j];
          if(temp >= missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      } else {
        for(int j = 0; j < array.length; j++) {
          temp = array[j];
          if(temp == missing) {
            outArray[j] = Double.NaN;
          } else {
            if(scale_offset) {
              outArray[j] = temp * scale + offset;
            } else {
              outArray[j] = temp;
            }
            if(valids) {
              if(outArray[j] < validMin || outArray[j] > validMax) {
                outArray[j] = Double.NaN;
              }
            }
          }
        }
      }
    }
    return outArray;
  }

  private GeoDate[] createGeoDateArray(Object time) {
    GeoDate[] tarray = null;
    int len = 0;
    if(time instanceof long[]) {
      len = ((long[])time).length;
      tarray = new GeoDate[len];
      for(int j = 0; j < len; j++) {
        if(is624_) {
          tarray[j] = new GeoDate((int)((long[])time)[j], time2_[j]);
        } else {
          tarray[j] = new GeoDate(refDate_);
          tarray[j].increment((double)((long[])time)[j],
                              increment_);
        }
      }
    } else if(time instanceof int[]) {
      len = ((int[])time).length;
      tarray = new GeoDate[len];
      for(int j = 0; j < len; j++) {
        if(is624_) {
          tarray[j] = new GeoDate(((int[])time)[j], time2_[j]);
        } else {
          tarray[j] = new GeoDate(refDate_);
          tarray[j].increment((double)((int[])time)[j],
                              increment_);
        }
      }
    } else if(time instanceof short[]) {
      len = ((short[])time).length;
      tarray = new GeoDate[len];
      for(int j = 0; j < len; j++) {
        if(is624_) {
          tarray[j] = new GeoDate(((short[])time)[j], time2_[j]);
        } else {
          tarray[j] = new GeoDate(refDate_);
          tarray[j].increment((double)((short[])time)[j],
                              increment_);
        }
      }
    } else if(time instanceof float[]) {
      len = ((float[])time).length;
      tarray = new GeoDate[len];
      for(int j = 0; j < len; j++) {
        tarray[j] = new GeoDate(refDate_);
        tarray[j].increment((double)((float[])time)[j],
                            increment_);
      }
    } else if(time instanceof double[]) {
      len = ((double[])time).length;
      tarray = new GeoDate[len];
      for(int j = 0; j < len; j++) {
        tarray[j] = new GeoDate(refDate_);
        tarray[j].increment(((double[])time)[j], increment_);
      }
    }
    return tarray;
  }

  private GeoDate[] createGeoDateArray(Variable ncVar, int index,
                                       int[] origin, int[] shape) {
    GeoDate[] timeArray = new GeoDate[shape[index]];
    Object array = null;
    int out = 0;
    int len;
    try {
      array = ncVar.read().copyTo1DJavaArray();
    } catch(IOException e) {
      e.printStackTrace();
    }
    if(array instanceof long[]) {
      long[] time = (long[])array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        if(is624_) {
          timeArray[out] = new GeoDate((int)time[j], time2_[j]);
        } else {
          timeArray[out] = new GeoDate(refDate_);
          timeArray[out].increment((double)time[j], increment_);
        }
      }
    } else if(array instanceof int[]) {
      int[] time = (int[])array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        if(is624_) {
          timeArray[out] = new GeoDate(time[j], time2_[j]);
        } else {
          timeArray[out] = new GeoDate(refDate_);
          timeArray[out].increment(time[j], increment_);
        }
      }
    } else if(array instanceof short[]) {
      short[] time = (short[])array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        if(is624_) {
          timeArray[out] = new GeoDate(time[j], time2_[j]);
        } else {
          timeArray[out] = new GeoDate(refDate_);
          timeArray[out].increment(time[j], increment_);
        }
      }
    } else if(array instanceof float[]) {
      float[] time = (float[])array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        timeArray[out] = new GeoDate(refDate_);
        timeArray[out].increment((double)time[j], increment_);
      }
    } else if(array instanceof double[]) {
      double[] time = (double[])array;
      len = origin[index] + shape[index];
      for(int j = origin[index]; j < len; j++, out++) {
        timeArray[out] = new GeoDate(refDate_);
        timeArray[out].increment(time[j], increment_);
      }
    }
    return timeArray;
  }

  private void stateChanged() {
    if(batchOn_) {
      changed_ = true;
      return;
    }
    if(dataset != null) {
      if(dataset[0] instanceof VMapLine) {
        updateLineData();
        fireChangeEvent();
      }
      if(dataset[0] instanceof VMapTuple) {
        updateTupleData((VMapTuple)dataset[0], Z_AXIS);
        fireChangeEvent();
        if(is3DLineColorData()) {
          updateTupleData((VMapTuple)dataset[0], Z_AXISCOLOR);
          fireChangeEvent();
        }
      } else if(dataset[0] instanceof VMapGrid && !is3DGridData()) {
        updateGridData((VMapGrid)dataset[0], Z_CONTOUR);
        fireChangeEvent();
      } else if(dataset[0] instanceof VMapGrid && is3DGridData()) {
        updateGridData((VMapGrid)dataset[0], SURFACE);
        fireChangeEvent();
        if(is3DGridColorData()) {
          updateGridData((VMapGrid)dataset[1], SURFACECOLOR);
          fireChangeEvent();
        }
      } else if(dataset[0] instanceof SGTVector) {
        updateSGTVector();
        fireChangeEvent();
      } else if(dataset[0] instanceof SGT3DVector) {
        updateSGT3DVector();
        fireChangeEvent();
      } else if(is3DVectorData()) {
        if(!isFull3DVectorData()) {
          updateSGT3DVector();
          fireChangeEvent();
        } else {
          updateSGTFull3DVector();
          fireChangeEvent();
        }
      } else if(isVolumeData()) {
        updateVolume();
        fireChangeEvent();
      }
    }
  }

  public void stateChanged(ChangeEvent e) {
    Object obj = e.getSource();
    if(obj instanceof VMapParameter) {
      if(Debug.DEBUG) {
        System.out.println((VMapParameter)obj);
      }
      stateChanged();
    } else {
      if(Debug.DEBUG) {
        System.out.println("stateChanged - " + obj);
      }
    }
  }

  /**
   * Turn on/off batching
   *
   * @param batch batching on if true
   */
  public void setBatch(boolean batch) {
    batchOn_ = batch;
    if(!batch && changed_) {
      stateChanged();
      changed_ = false;
    }
  }

  /**
   * test batching
   * @return true if batching on
   */
  public boolean isBatch() {
    return batchOn_;
  }

  public void addChangeListener(ChangeListener l) {
    changeListeners_.add(l);
  }

  public void removeChangeListener(ChangeListener l) {
    changeListeners_.remove(l);
  }

  private void fireChangeEvent() {
    for(Enumeration e = changeListeners_.elements(); e.hasMoreElements(); ) {
      ((ChangeListener)e.nextElement()).stateChanged(event_);
    }
  }

  static public SoTRange computeRange(double[] val) {
    return new SoTRange.Double(computeRange2D(val));
  }

  static public SoTRange computeRange(GeoDate[] val) {
    long start = Long.MAX_VALUE;
    long end = Long.MIN_VALUE;
    long value;
    int count = 0;
    for(int i = 0; i < val.length; i++) {
      if(!(val[i] == null || val[i].isMissing())) {
        value = val[i].getTime();
        start = Math.min(start, value);
        end = Math.max(end, value);
        count++;
      }
    }
    if(count == 0) {
      return new SoTRange.GeoDate(new GeoDate(Long.MIN_VALUE),
                                  new GeoDate(Long.MAX_VALUE));
    } else {
      return new SoTRange.GeoDate(new GeoDate(start), new GeoDate(end));
    }
  }

  static public SoTRange computeRange(GeoDateArray val) {
    long tstart = Long.MAX_VALUE;
    long tend = Long.MIN_VALUE;
    long[] tar = val.getTime();
    int count = 0;
    for(int i=0; i < tar.length; i++) {
      if(!(tar[i] == Long.MAX_VALUE)) {
        tstart = Math.min(tstart, tar[i]);
        tend = Math.max(tend, tar[i]);
        count++;
      }
    }
    if(count == 0) {
      return new SoTRange.Time(Long.MAX_VALUE,
                               Long.MAX_VALUE);
    } else {
      return new SoTRange.Time(tstart, tend);
    }
  }

  static public Range2D computeRange2D(double[] array) {
    double start = Double.POSITIVE_INFINITY;
    double end = Double.NEGATIVE_INFINITY;
    int count = 0;
    for(int i = 0; i < array.length; i++) {
      if(!Double.isNaN(array[i])) {
        start = Math.min(start, array[i]);
        end = Math.max(end, array[i]);
        count++;
      }
    }
    if(count == 0) {
      return new Range2D(Double.NaN, Double.NaN);
    } else {
      return new Range2D(start, end);
    }
  }

  public VisADPlotSpecification getVisADPlotSpecification() {
    return plotSpec;
  }

  public void setVisADPlotSpecification(VisADPlotSpecification spec) {
    plotSpec = spec;
    stateChanged();
  }

}
