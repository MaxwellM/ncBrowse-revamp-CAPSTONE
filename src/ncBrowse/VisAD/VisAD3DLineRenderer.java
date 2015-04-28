/**
 *  $Id: VisAD3DLineRenderer.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.VisAD;

import ncBrowse.Debug;
import ncBrowse.MenuBar3D;
import ncBrowse.NcFile;
import ncBrowse.map.VMapModel;
import ncBrowse.sgt.dm.SGTData;
import ncBrowse.sgt.dm.SGTGrid;
import ncBrowse.sgt.dm.SGTMetaData;
import ncBrowse.sgt.dm.SGTTuple;
import ncBrowse.sgt.geom.GeoDate;
import ncBrowse.sgt.geom.SoTRange;
import visad.*;
import visad.java3d.DisplayImplJ3D;
import visad.util.LabeledColorWidget;
import visad.util.SelectRangeWidget;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.rmi.RemoteException;

//import gov.noaa.pmel.sgt.dm.SGTData;
//import gov.noaa.pmel.sgt.dm.SGTGrid;
//mport gov.noaa.pmel.sgt.dm.SGTMetaData;
//import gov.noaa.pmel.sgt.dm.SGTTuple;
//import gov.noaa.pmel.util.GeoDate;
//import gov.noaa.pmel.util.SoTRange;

//import gov.noaa.pmel.sgt.dm.*;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author John Osborne
 * @version $Revision: 1.3 $, $Date: 2004/03/09 22:47:27 $
 */

public class VisAD3DLineRenderer extends VisADPlotRenderer {
  // The domain quantities x and y and the dependent quantities z, zcolor
  private RealType X, Y, Z, ZColor, index;

  // Two Tuples: one to pack longitude and latitude together, as the domain
  // and the other for the range (altitude, temperature)
  private RealTupleType domain_tuple, range_tuple;

  // The function (domain_tuple -> range_tuple)
  private FunctionType func_domain_range;

   // Our Data values for the domain are represented by the Set
  private Set domain_set;
  private Set index_set;

  // The Data class FlatField
  private FlatField vals_ff;

  // The DataReference from data to display
  private DataReferenceImpl data_ref;

  // The 2D display, and its the maps
  private ScalarMap yMap, xMap;
  private ScalarMap xRangeMap, yRangeMap, zRangeMap, zRGBRangeMap;
  private ScalarMap zMap, zRGBMap;
  private ScalarMap colorZMap, colorRGBMap;
  private SGTTuple mGrid, mColorVar = null;
  private SelectRangeWidget selXRange;
  private SelectRangeWidget selYRange;
  private SelectRangeWidget selZRange;
  private boolean xIsReversed = false;
  private boolean yIsReversed = false;
  private boolean zIsReversed = false;
  private VisADPlotSpecification mPlotSpec;
  private boolean legendIsVisible = false;
  private JPanel cont;
  private LabeledColorWidget legend;

	public VisAD3DLineRenderer(SGTData inTuples, SGTData inColorVar, VMapModel model, VisADPlotSpecification spec) {
		mGrid = (SGTTuple)inTuples;
		mColorVar = (SGTTuple)inColorVar;
		mModel = model;
		mPlotSpec = spec;
		mModel.addChangeListener(this);
  		xIsReversed = mModel.isXReversed();
  		yIsReversed = mModel.isYReversed();
  		zIsReversed = mModel.isZReversed();
	}

	public void setPlotSpecification(VisADPlotSpecification spec) {
		mPlotSpec = spec;
	}

    public boolean canDisplayLegend() {
    	return mColorVar == null;
    }

	public void stateChanged(ChangeEvent e) {
		double xmin = 0.0, xmax = 0.0;
		double ymin = 0.0, ymax = 0.0;
		if(Debug.DEBUG)
			System.out.println("VisAD3DLineRenderer.stateChanged()");
		Object obj = e.getSource();
		if (obj instanceof VMapModel) {
	  		xIsReversed = ((VMapModel)obj).isXReversed();
	  		yIsReversed = ((VMapModel)obj).isYReversed();
	  		zIsReversed = ((VMapModel)obj).isZReversed();
			SGTData[] dobj = ((VMapModel)obj).getSGTData();
			boolean hasColor = false;
			if (dobj.length > 1 && dobj[1] != null)
				hasColor = true;
			if (dobj[0] instanceof SGTTuple) {
				if (!mGrid.isXTime()) {
					SoTRange xR = dobj[0].getXRange();
			        xmin = ((Number)xR.getStart().getObjectValue()).floatValue();
			        xmax = ((Number)xR.getEnd().getObjectValue()).floatValue();
			        try {
						xRangeMap.setRange(xmin, xmax);
						xMap.setRange(xmin, xmax);
					}
					catch (Exception ignored) {}
				}
				else {
 					GeoDate[] gda = ((SGTTuple)dobj[0]).getTimeArray();
 					SoTRange xR = ((VMapModel)obj).computeRange(gda);
			        double tmin = ((GeoDate)xR.getStart().getObjectValue()).getTime();
			        double tmax = ((GeoDate)xR.getEnd().getObjectValue()).getTime();
			        xmin = Math.min(tmin, tmax);
			        xmax = Math.max(tmin, tmax);
			        try {
						xRangeMap.setRange((float)xmin, (float)xmax);
						xMap.setRange((float)xmin,(float)xmax);
					}
					catch (Exception ignored) {}
				}

				if (!mGrid.isYTime()) {
					SoTRange yR = dobj[0].getYRange();
			        ymin = ((Number)yR.getStart().getObjectValue()).floatValue();
			        ymax = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						yRangeMap.setRange(ymin, ymax);
						yMap.setRange(ymin, ymax);
					}
					catch (Exception ignored) {}
				}
				else {
 					GeoDate[] gda = ((SGTTuple)dobj[0]).getTimeArray();
 					SoTRange yR = ((VMapModel)obj).computeRange(gda);
			        ymin = ((Number)yR.getStart().getObjectValue()).floatValue();
			        ymax = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						yRangeMap.setRange(ymin, ymax);
						yMap.setRange(ymin, ymax);
					}
					catch (Exception ignored) {}
				}
			}

			// reset the sample flatfield
		    int count = 0;
 			int xSize;
 			int ySize;
 			int zSize;
 			double[] xArray = null;
 			double[] yArray = null;

 			if (dobj[0].isXTime()) {
		 		GeoDate[] gda = ((SGTGrid)dobj[0]).getTimeArray();
		 		xSize = gda.length;
 			}
 			else {
 				xArray = ((SGTTuple)dobj[0]).getXArray();
 				xSize = xArray.length;
 			}

 			if (dobj[0].isYTime()) {
		 		GeoDate[] gda = ((SGTGrid)dobj[0]).getTimeArray();
		 		ySize = gda.length;
 			}
 			else {
 				yArray = ((SGTTuple)dobj[0]).getYArray();
	 			ySize = yArray.length;
 			}

 			double[] zArray = ((SGTTuple)dobj[0]).getZArray();
 			zSize = zArray.length;

 			double[] zColorArray = null;
 			if (hasColor)
 				zColorArray = ((SGTTuple)dobj[1]).getZArray();

		    float[][] flat_samples = null;

		    int axisMin = Math.min(xSize, ySize);
		    axisMin = Math.min(zSize, axisMin);

		    if (hasColor)
		    	flat_samples = new float[4][axisMin];
		    else
		    	flat_samples = new float[3][axisMin];

		    //  fill our 'flat' array
 			xmin = 99e99;
 			xmax = -99e99;
 			ymin = 99e99;
 			ymax = -99e99;
 			double zmin = 99e99;
 			double zmax = -99e99;
 			double cmin = 99e99;
 			double cmax = -99e99;
		    for(int i=0; i < axisMin; i++) {
		        double xval = xArray[i];
		        double yval = yArray[i];
		        double zval = zArray[i];
		        if (hasColor) {
		        	double cval = zColorArray[i];
		        	flat_samples[0][i] = (float)cval;
			        flat_samples[1][i] = (float)zval;
			        flat_samples[2][i] = (float)yval;
			        flat_samples[3][i] = (float)xval;
					cmin = cval < cmin ? cval : cmin;
					cmax = cval > cmax ? cval : cmax;
		        }
		        else {
			        flat_samples[0][i] = (float)yval;
			        flat_samples[1][i] = (float)yval;
			        flat_samples[2][i] = (float)xval;
		        }
				xmin = xval < xmin ? xval : xmin;
				xmax = xval > xmax ? xval : xmax;
				ymin = yval < ymin ? yval : ymin;
				ymax = yval > ymax ? yval : ymax;
				zmin = zval < zmin ? zval : zmin;
				zmax = zval > zmax ? zval : zmax;
		    }

			try {
				// reset the range map
				if (zIsReversed)
					zMap.setRange(zmax, zmin);
				else
					zMap.setRange(zmin, zmax);

				if (xIsReversed)
					xMap.setRange(xmax, xmin);
				else
					xMap.setRange(xmin, xmax);

				if (yIsReversed)
					yMap.setRange(ymax, ymin);
				else
					yMap.setRange(ymin, ymax);

		        if (hasColor) {
					colorZMap.setRange(cmin, cmax);
					colorRGBMap.setRange(cmin, cmax);
				}
			}
			catch (Exception ignored) {}

		    try {
    			vals_ff.setSamples(flat_samples, false);

				int axesMax = Math.max(ySize, xSize);
				axesMax = Math.max(axesMax, zSize);
				ProjectionControl projCont = display.getProjectionControl();
				if (mPlotSpec.isRespectDataAspectRatio() != mCurrAspectPolicy || mCurrXScaling != mPlotSpec.getXAxisScaling() ||
				    mCurrYScaling != mPlotSpec.getYAxisScaling() || mCurrZScaling != mPlotSpec.getZAxisScaling()) {
				    projCont.saveProjection();
				    if (mPlotSpec.isRespectDataAspectRatio()) {
					    float xAspect = (float)xSize/(float)axesMax;
						xAspect  *= mPlotSpec.getXAxisScaling();
					    float yAspect = (float)ySize/(float)axesMax;
						yAspect  *= mPlotSpec.getYAxisScaling();
					    float zAspect = (float)zSize/(float)axesMax;
						zAspect  *= mPlotSpec.getZAxisScaling();
					    double[] aspect = new double[]{xAspect, yAspect, zAspect};
					    projCont.setAspect(aspect);
					}
					else {
					    double[] aspect = new double[]{1.0, 1.0, 1.0};
					    projCont.setAspect(aspect);
					}
				    projCont.setMatrix(projCont.getSavedProjectionMatrix());
					mCurrAspectPolicy = mPlotSpec.isRespectDataAspectRatio();
					mCurrXScaling = mPlotSpec.getXAxisScaling();
					mCurrYScaling = mPlotSpec.getYAxisScaling();
					mCurrZScaling = mPlotSpec.getZAxisScaling();
				}
    			data_ref.setData(vals_ff);
    		}
    		catch (Exception ex) {ex.printStackTrace();}

    		try {
			    // Set the display background color and axes color
		    	DisplayRenderer dRenderer = display.getDisplayRenderer();
			    float[] backColor = colorToFloats(mPlotSpec.getBGColor());
			    dRenderer.setBackgroundColor(backColor[0], backColor[1],backColor[2]);
			    float[] axesColor = colorToFloats(mPlotSpec.getAxesColor());
			    dRenderer.setForegroundColor(axesColor[0], axesColor[1],axesColor[2]);
		   	}
		   	catch (Exception ex) {
		   		ex.printStackTrace();
		   	}
		}

		mPlotSpec = ((VMapModel)obj).getVisADPlotSpecification();
		if (mPlotSpec.getLegendStyle() > 0 && !legendIsVisible && mColorVar != null) {
			try {
				if (legend == null)
					legend = new LabeledColorWidget(zRGBMap, buildColorTable());
				// add a legend
				legendIsVisible = true;
				cont.add("South", legend);
	    		mFrame.setSize(mFrame.getSize().width+1, mFrame.getSize().height);
	    		mFrame.setSize(mFrame.getSize().width-1, mFrame.getSize().height);
	    		mFrame.validate();
	    	}
	    	catch (Exception ex) {
	    		ex.printStackTrace();
	    	}
		}
		else if (mPlotSpec.getLegendStyle() == 0 && legendIsVisible) {
			// remove the legend
			legendIsVisible = false;
			cont.remove(legend);
    		mFrame.setSize(mFrame.getSize().width+1, mFrame.getSize().height);
    		mFrame.setSize(mFrame.getSize().width-1, mFrame.getSize().height);
    		mFrame.validate();
		}
	}

 public void draw() throws VisADException, RemoteException {
 	if (!(mGrid instanceof SGTTuple))
 		return;
 	SGTMetaData xMetaData = mGrid.getXMetaData();
 	SGTMetaData yMetaData = mGrid.getYMetaData();
 	SGTMetaData zMetaData = mGrid.getZMetaData();
 	SGTMetaData zColorMetaData = null;
 	if (mColorVar != null)
 		zColorMetaData = mColorVar.getZMetaData();
 	double[] xArray;
 	int xSize;
 	if (!mGrid.isXTime()) {
 		xArray = mGrid.getXArray();
 		xSize = xArray.length;
 	}
 	else {
 		// is time
 		GeoDate[] gda = mGrid.getTimeArray();
 		xSize = gda.length;
 		xArray = new double[xSize];
 		for (int i=0; i<xSize; i++) {
 			xArray[i] = (double)(gda[i].getTime());
		}
 	}
 	double[] yArray = mGrid.getYArray();
 	double[] zArray = mGrid.getZArray();
 	int ySize = yArray.length;
 	int zSize = zArray.length;
 	double[] zColorArray = null;
 	if (mColorVar != null) {
 		zColorArray = mColorVar.getZArray();
 	}

 	// to do: need to turn axis units into VISAD Units

    // Create the Y axis
    int count = 0;
    while (true) {
    	String typeName;
    	if (count > 0)
    		typeName = yMetaData.getName() + "_" + count;
    	else
    		typeName = yMetaData.getName();
	    try {
	    	Y = RealType.getRealType(typeName, null, null);
	    	break;
	    }
	    catch (Exception ex) {
	    	if (ex instanceof visad.TypeException) {
	    		count++;
	    	}
	    	else {
	    		throw new VisADException();
	    	}
	    }
	}

    // create the X axis
    count = 0;
    while (true) {
    	String typeName;
    	if (count > 0)
    		typeName = xMetaData.getName() + "_" + count;
    	else
    		typeName = xMetaData.getName();
	    try {
	    	X = RealType.getRealType(typeName, null, null);
	    	break;
	    }
	    catch (Exception ex) {
	    	if (ex instanceof visad.TypeException) {
	    		count++;
	    	}
	    	else {
	    		throw new VisADException();
	    	}
	    }
	}

    count = 0;
    while (true) {
    	String typeName;
    	if (count > 0)
    		typeName = zMetaData.getName() + "_" + count;
    	else
    		typeName = zMetaData.getName();
	    try {
	    	Z = RealType.getRealType(typeName, null, null);
	    	break;
	    }
	    catch (Exception ex) {
	    	if (ex instanceof visad.TypeException) {
	    		count++;
	    	}
	    	else {
	    		throw new VisADException();
	    	}
	    }
	}

	if (mColorVar != null) {
	    count = 0;
	    while (true) {
	    	String typeName;
	    	if (count > 0)
	    		typeName = zColorMetaData.getName() + "_color" + "_" + count;
	    	else
	    		typeName = zColorMetaData.getName() + "_color";
		    try {
		    	ZColor = RealType.getRealType(typeName, null, null);
		    	break;
		    }
		    catch (Exception ex) {
		    	if (ex instanceof visad.TypeException) {
		    		count++;
		    	}
		    	else {
		    		throw new VisADException();
		    	}
		    }
		}
	}
	/*else {
	    count = 0;
	    while (true) {
	    	String typeName;
	    	if (count > 0)
	    		typeName = zMetaData.getName() + "_color" + "_" + count;
	    	else
	    		typeName = zMetaData.getName() + "_color";
		    try {
		    	ZColor = new RealType(typeName, null, null);
		    	break;
		    }
		    catch (Exception ex) {
		    	if (ex instanceof visad.TypeException) {
		    		count++;
		    	}
		    	else {
		    		throw new VisADException();
		    	}
		    }
		}
	}*/

	if (mColorVar != null)
    	domain_tuple = new RealTupleType(ZColor, Z, Y, X);
    else
    	domain_tuple = new RealTupleType(Z, Y, X);

	count = 0;
	while (true) {
		try {
    		index = RealType.getRealType("index" + "_" + count);
    		break;
    	}
	    catch (Exception ex) {
	    	if (ex instanceof visad.TypeException) {
	    		count++;
	    	}
	    	else {
	    		throw new VisADException();
	    	}
	    }
    }


    // Create a FunctionType (domain_tuple -> range_tuple )
    func_domain_range = new FunctionType(index, domain_tuple);

    // Create the domain Set
    // get the max min vals of the values for the axes
    double yMin = 9999999e99;
    double xMin = 9999999e99;
    double zMin = 9999999e99;
    double yMax = -9999999e99;
    double xMax = -9999999e99;
    double zMax = -9999999e99;

    for (int i=0; i<xSize; i++) {
    	xMin = xArray[i] < xMin ? xArray[i] : xMin;
    	xMax = xArray[i] > xMax ? xArray[i] : xMax;
    }

	 for (double aYArray : yArray) {
		 yMin = aYArray < yMin ? aYArray : yMin;
		 yMax = aYArray > yMax ? aYArray : yMax;
	 }

	 for (double aZArray : zArray) {
		 zMin = aZArray < zMin ? aZArray : zMin;
		 zMax = aZArray > zMax ? aZArray : zMax;
	 }

    int axisMin = Math.min(xSize, ySize);
    axisMin = Math.min(zSize, axisMin);

    index_set = new Integer1DSet(index, axisMin);

    // We create another array, with the same number of elements of
    // z and zColor, but organized as float[2][ number_of_samples ]
    float[][] flat_samples = null;

    if (mColorVar != null)
    	flat_samples = new float[4][axisMin];
    else
    	flat_samples = new float[3][axisMin];

    // ...and then we fill our 'flat' array
    for(int i=0; i < axisMin; i++) {
        double xval = xArray[i];
        double yval = yArray[i];
        double zval = zArray[i];
        if (mColorVar != null) {
        	double cval = zColorArray[i];
        	flat_samples[0][i] = (float)cval;
	        flat_samples[1][i] = (float)zval;
	        flat_samples[2][i] = (float)yval;
	        flat_samples[3][i] = (float)xval;
        }
        else {
	        flat_samples[0][i] = (float)zval;
	        flat_samples[1][i] = (float)yval;
	        flat_samples[2][i] = (float)xval;
        }
    }

    // Create a FlatField
    // Use FlatField(FunctionType type, Set domain_set)
    vals_ff = new FlatField(func_domain_range, index_set/*domain_set*/);

    // ...and put the values above into it. Note the argument false, meaning that the array won't be copied
    vals_ff.setSamples(flat_samples, false);

    // Create Display and its maps
    // A 3D display
    display = new DisplayImplJ3D("display1");
    display.addDisplayListener(this);

    // Get display's graphics mode control and draw scales
    GraphicsModeControl dispGMC = display.getGraphicsModeControl();
    dispGMC.setScaleEnable(true);

	int axesMax = Math.max(ySize, xSize);
	axesMax = Math.max(axesMax, zSize);
    if (mPlotSpec.isRespectDataAspectRatio()) {
	    ProjectionControl projCont = display.getProjectionControl();
	    float xAspect = (float)xSize/(float)axesMax;
		xAspect  *= mPlotSpec.getXAxisScaling();

	    float yAspect = (float)ySize/(float)axesMax;
		yAspect  *= mPlotSpec.getYAxisScaling();

	    float zAspect = (float)zSize/(float)axesMax;
		zAspect  *= mPlotSpec.getZAxisScaling();

	    double[] aspect = new double[]{xAspect, yAspect, zAspect};
	    projCont.setAspect(aspect);
	}
	mCurrAspectPolicy = mPlotSpec.isRespectDataAspectRatio();
	mCurrXScaling = mPlotSpec.getXAxisScaling();
	mCurrYScaling = mPlotSpec.getYAxisScaling();
	mCurrZScaling = mPlotSpec.getZAxisScaling();

    // Create the ScalarMaps
    yMap = new ScalarMap(Y, Display.YAxis);
    xMap = new ScalarMap(X, Display.XAxis);
    zMap = new ScalarMap(Z, Display.ZAxis);
    xRangeMap = new ScalarMap(X, Display.SelectRange);
    yRangeMap = new ScalarMap(Y, Display.SelectRange);
    zRangeMap = new ScalarMap(Z, Display.SelectRange);

    // reverse any axes
	if (zIsReversed)
		zMap.setRange(zMax, zMin);
	else
		zMap.setRange(zMin, zMax);

	if (xIsReversed)
		xMap.setRange(xMax, xMin);
	else
		xMap.setRange(xMin, xMax);

	if (yIsReversed)
		yMap.setRange(yMax, yMin);
	else
		yMap.setRange(yMin, yMax);

    // Add maps to display
    display.addMap(yMap);
    display.addMap(xMap);
    display.addMap(zMap);
    display.addMap(xRangeMap);
    display.addMap(yRangeMap);
    display.addMap(zRangeMap);

    if (mColorVar != null) {
    	zRGBMap = new ScalarMap(ZColor, Display.RGBA);
		zRGBRangeMap = new ScalarMap(ZColor, Display.SelectRange);
		display.addMap(zRGBMap);
		display.addMap(zRGBRangeMap);
    }

    // create the range widgets
    selXRange = new SelectRangeWidget(xRangeMap);
    selYRange = new SelectRangeWidget(yRangeMap);
    selZRange = new SelectRangeWidget(zRangeMap);

    // Create a data reference and set the FlatField as our data
    data_ref = new DataReferenceImpl("data_ref");
    data_ref.setData(vals_ff);

    // Add reference to display
    ConstantMap[] pointsCMap = {new ConstantMap(3.50f, Display.PointSize)};
    display.addReference(data_ref, pointsCMap);

    // Create application window and add display to window
    NcFile ncFile = mModel.getNcFile();
    String name = ncFile.getFileName();
    String s = mModel.getName();
    mFrame = new JFrame("3D " + s + " from " + name);
    mFrame.getContentPane().add(display.getComponent());

    // add the menu bar
    mMenuBar = new MenuBar3D(mFrame, this, false);
    cont = new JPanel();
    cont.setLayout(new BorderLayout(5, 5));
    cont.add("Center", display.getComponent());

	if (mColorVar != null && mPlotSpec.getLegendStyle() == VisADPlotSpecification.COLOR_LEGEND && mColorVar != null) {
		legend = new LabeledColorWidget(zRGBMap, buildColorTable());
		// simple color legend in south
    	cont.add("South", legend);
    	legendIsVisible = true;
	}

	try {
    	// Get the display renderer
	    DisplayRenderer dRenderer = display.getDisplayRenderer();

	    // Set the display background color and axes color
	    float[] backColor = colorToFloats(mPlotSpec.getBGColor());
	    dRenderer.setBackgroundColor(backColor[0], backColor[1],backColor[2]);
	    float[] axesColor = colorToFloats(mPlotSpec.getAxesColor());
	    dRenderer.setForegroundColor(axesColor[0], axesColor[1],axesColor[2]);
   	}
   	catch (Exception ex) {
   		ex.printStackTrace();
   	}
    mFrame.getContentPane().add(cont);

    // Set window size and make it visible
    mFrame.setSize(600, 600);
    mFrame.setVisible(true);
  }

	public float[][] buildColorTable() {
		int tableLength = 15;

		float[][] myColorTable = new float[4][tableLength];

		for(int i=0;i < tableLength;i++){
			myColorTable[0][i]= (float) i / ((float)tableLength-1.0f); // red component
			myColorTable[2][i]= 1.0f - (float)i / ((float)tableLength-1.0f); // blue component

			if(i<(tableLength)/2){ // lower half of table
				myColorTable[1][i]= 2.0f *(float) i / (tableLength-1); // green component
				myColorTable[3][i]= 1.0f;
			}
			else{ // upper half of table
				myColorTable[1][i]=  2.0f - 2.0f *(float)i / ((float)tableLength-1); // green component
				myColorTable[3][i]= 1.0f;// alpha component
			}
		}
		return myColorTable;
	}
}
