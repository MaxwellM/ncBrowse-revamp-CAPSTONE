/**
 *  $Id: VisAD3DVolumeRenderer.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.VisAD;

import javax.swing.event.ChangeEvent;
import javax.swing.*;
import visad.*;
import visad.java3d.*;
import java.rmi.RemoteException;
import gov.noaa.pmel.util.SoTRange;
import gov.noaa.pmel.util.GeoDate;
import ncBrowse.map.*;
import visad.util.*;
import java.awt.event.*;
import java.awt.*;
import ncBrowse.*;

import gov.noaa.pmel.sgt.dm.SGTMetaData;
import gov.noaa.pmel.sgt.dm.ThreeDGrid;
import gov.noaa.pmel.sgt.dm.SGTData;

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

public class VisAD3DVolumeRenderer extends VisADPlotRenderer {
  // The domain quantities x and y
  // and the dependent quantities z, zcolor
  private RealType X, Y, Z;
  private RealType V;

  // Two Tuples: one to pack x and y together, as the domain
  // and the other for the range (u, v, w)
  private RealTupleType domain_tuple, range_tuple;

  // The function (domain_tuple -> range_tuple)
  private FunctionType func_domain_range;

   // Our Data values for the domain are represented by the Set
  private Set domain_set;

  // The Data class FlatField
  private FlatField vals_ff;

  // The DataReference from data to display
  private DataReferenceImpl data_ref;

  // The 2D display, and its the maps
  private ScalarMap yMap, xMap, zMap;
  private ScalarMap valMap;
  private ScalarMap xRangeMap, yRangeMap, zRangeMap;
  private ScalarMap valRangeMap;
  ThreeDGrid valGrid = null;
  private SelectRangeWidget selXRange;
  private SelectRangeWidget selYRange;
  private boolean xIsReversed = false;
  private boolean yIsReversed = false;
  private boolean zIsReversed = false;
  private VisADPlotSpecification mPlotSpec;
  private LabeledColorWidget legend;
  private boolean legendIsVisible = false;
  private JPanel cont;

	public VisAD3DVolumeRenderer(ThreeDGrid ingrid, VMapModel model, VisADPlotSpecification spec) {
		valGrid = ingrid;
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

	public void stateChanged(ChangeEvent e) {
		double xmin = 0.0, xmax = 0.0;
		double ymin = 0.0, ymax = 0.0;
		double zmin = 0.0, zmax = 0.0;
		if(Debug.DEBUG)
			System.out.println("VisAD3DVolumeRenderer.stateChanged()");
		Object obj = e.getSource();
		if (obj instanceof VMapModel) {
			mPlotSpec = ((VMapModel)obj).getVisADPlotSpecification();
	  		xIsReversed = ((VMapModel)obj).isXReversed();
	  		yIsReversed = ((VMapModel)obj).isYReversed();
	  		zIsReversed = ((VMapModel)obj).isZReversed();
			SGTData[] dobj = ((VMapModel)obj).getSGTData();
			if (dobj[0] instanceof ThreeDGrid) {
				valGrid = (ThreeDGrid)dobj[0];
				if (!valGrid.isXTime()) {
					SoTRange xR = valGrid.getXRange();
			        xmin = ((Number)xR.getStart().getObjectValue()).floatValue();
			        xmax = ((Number)xR.getEnd().getObjectValue()).floatValue();
			        try {
						if (xIsReversed)
							xMap.setRange(xmax, xmin);
						else
							xMap.setRange(xmin, xmax);
						xRangeMap.setRange(xmin, xmax);
					}
					catch (Exception ex) {}
				}
				else {
 					GeoDate[] gda = valGrid.getTimeArray();
 					SoTRange xR = ((VMapModel)obj).computeRange(gda);
			        double tmin = ((GeoDate)xR.getStart().getObjectValue()).getTime();
			        double tmax = ((GeoDate)xR.getEnd().getObjectValue()).getTime();
			        xmin = Math.min(tmin, tmax);
			        xmax = Math.max(tmin, tmax);
			        try {
						if (xIsReversed)
							xMap.setRange(xmax, xmin);
						else
							xMap.setRange(xmin, xmax);
						xRangeMap.setRange((float)xmin, (float)xmax);
					}
					catch (Exception ex) {}
				}

				if (!valGrid.isYTime()) {
					SoTRange yR = valGrid.getYRange();
			        ymin = ((Number)yR.getStart().getObjectValue()).floatValue();
			        ymax = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						if (yIsReversed)
							yMap.setRange(ymax, ymin);
						else
							yMap.setRange(ymin, ymax);
						yRangeMap.setRange(ymin, ymax);
					}
					catch (Exception ex) {}
				}
				else {
 					GeoDate[] gda = valGrid.getTimeArray();
 					SoTRange yR = ((VMapModel)obj).computeRange(gda);
			        ymin = ((Number)yR.getStart().getObjectValue()).floatValue();
			        ymax = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						if (yIsReversed)
							yMap.setRange(ymax, ymin);
						else
							yMap.setRange(ymin, ymax);
						yRangeMap.setRange(ymin, ymax);
					}
					catch (Exception ex) {}
				}

				if (!valGrid.isZTime()) {
					SoTRange zR = valGrid.getZRange();
			        zmin = ((Number)zR.getStart().getObjectValue()).floatValue();
			        zmax = ((Number)zR.getEnd().getObjectValue()).floatValue();
			        try {
						if (zIsReversed)
							zMap.setRange(zmax, zmin);
						else
							zMap.setRange(zmin, zmax);
						zRangeMap.setRange(zmin, zmax);
					}
					catch (Exception ex) {}
				}
				else {
 					GeoDate[] gda = valGrid.getTimeArray();
 					SoTRange zR = ((VMapModel)obj).computeRange(gda);
			        zmin = ((Number)zR.getStart().getObjectValue()).floatValue();
			        zmax = ((Number)zR.getEnd().getObjectValue()).floatValue();
			        try {
						if (zIsReversed)
							zMap.setRange(zmax, zmin);
						else
							zMap.setRange(zmin, zmax);
						zRangeMap.setRange(zmin, zmax);
					}
					catch (Exception ex) {}
				}
			}

			// reset the sample flatfield
 			int xSize;
 			int ySize;
 			int zSize;

 			if (valGrid.isXTime()) {
		 		GeoDate[] gda = valGrid.getTimeArray();
		 		xSize = gda.length;
 			}
 			else
 				xSize = valGrid.getXSize();

 			if (valGrid.isYTime()) {
		 		GeoDate[] gda = valGrid.getTimeArray();
		 		ySize = gda.length;
 			}
 			else {
	 			ySize = valGrid.getYSize();
 			}

 			if (valGrid.isZTime()) {
		 		GeoDate[] gda = valGrid.getTimeArray();
		 		zSize = gda.length;
 			}
 			else {
	 			zSize = valGrid.getZArray().length;
 			}

		    float[][] flat_samples = new float[1][xSize * ySize * zSize];
 			double[] valArray = valGrid.getValArray();

 			double vmin = 99e99;
 			double vmax = -99e99;
		    int count = 0;
		    for(int i=0; i < xSize; i++) {
				for(int j=0; j < ySize; j++) {
					for (int k=0; k<zSize; k++) {
						double val = valArray[count];

						flat_samples[0][count] = (float)val;
						vmin = val < vmin ? val : vmin;
						vmax = val > vmax ? val : vmax;
						count++;
					}
				}
			}

			try {
				// reset the range map
				valRangeMap.setRange(vmin, vmax);
				valMap.setRange(vmin, vmax);
			}
			catch (Exception ex) {}

		    try {
   				domain_set = new Linear3DSet(domain_tuple, ymin, ymax, ySize,
                                               xmin, xmax, xSize,
                                               zmin, zmax, zSize);
    			vals_ff = new FlatField(func_domain_range, domain_set);
    			vals_ff.setSamples(flat_samples, false);

    			int axesMax = Math.max(ySize, xSize);
    			axesMax = Math.max(axesMax, zSize);
    			if (mPlotSpec.isResampleAxes()) {
	    			// resample so cube axes are all the same length
	    			Set resampled_domain_set = new Linear3DSet(domain_tuple, ymin, ymax, axesMax,
	                                               xmin, xmax, axesMax,
	                                               zmin, zmax, axesMax);
	                vals_ff = (FlatField)vals_ff.resample(resampled_domain_set, mPlotSpec.getSamplingMode(), Data.DEPENDENT);
	            }
    			data_ref.setData(vals_ff);

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
    		}
    		catch (Exception ex) { ex.printStackTrace();}

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

		if (mPlotSpec.getLegendStyle() > 0 && !legendIsVisible) {
			// add a legend
			legendIsVisible = true;
			cont.add("South", legend);
    		mFrame.setSize(mFrame.getSize().width+1, mFrame.getSize().height);
    		mFrame.setSize(mFrame.getSize().width-1, mFrame.getSize().height);
    		mFrame.validate();
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
 	if (!(valGrid instanceof ThreeDGrid))
 		return;

 	SGTMetaData xMetaData = valGrid.getXMetaData();
 	SGTMetaData yMetaData = valGrid.getYMetaData();
 	SGTMetaData zMetaData = valGrid.getZMetaData();
 	SGTMetaData valMetaData = valGrid.getValMetaData();

 	// x axis
 	double[] xArray;
 	int xSize;
 	if (!valGrid.isXTime()) {
 		xArray = valGrid.getXArray();
 		xSize = valGrid.getXSize();
 	}
 	else {
 		// is time
 		GeoDate[] gda = valGrid.getTimeArray();
 		xSize = gda.length;
 		xArray = new double[xSize];
 		for (int i=0; i<xSize; i++) {
 			xArray[i] = (double)(gda[i].getTime());
		}
 	}

 	int ySize;
 	double[] yArray;
 	if (!valGrid.isYTime()) {
 		yArray = valGrid.getYArray();
 		ySize = valGrid.getYSize();
 	}
 	else {
 		// is time
 		GeoDate[] gda = valGrid.getTimeArray();
 		ySize = gda.length;
 		yArray = new double[ySize];
 		for (int i=0; i<ySize; i++) {
 			yArray[i] = (double)(gda[i].getTime());
		}
 	}

 	int zSize;
 	double[] zArray;
 	if (!valGrid.isZTime()) {
 		zArray = valGrid.getZArray();
 		zSize = zArray.length;
 	}
 	else {
 		// is time
 		GeoDate[] gda = valGrid.getTimeArray();
 		zSize = gda.length;
 		zArray = new double[zSize];
 		for (int i=0; i<zSize; i++) {
 			zArray[i] = (double)(gda[i].getTime());
		}
 	}

 	double[] valArray = valGrid.getValArray();
 	int valSize = valArray.length;

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
	    	Y = new RealType(typeName, null, null);
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
	    	X = new RealType(typeName, null, null);
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

    // create the Z axis
    count = 0;
    while (true) {
    	String typeName;
    	if (count > 0)
    		typeName = zMetaData.getName() + "_" + count;
    	else
    		typeName = zMetaData.getName();
	    try {
	    	Z = new RealType(typeName, null, null);
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


    domain_tuple = new RealTupleType(Y, X, Z);

	// create Values
    count = 0;
    while (true) {
    	String typeName;
    	if (count > 0)
    		typeName = valMetaData.getName() + "_" + count;
    	else
    		typeName = valMetaData.getName();
	    try {
	    	V = new RealType(typeName, null, null);
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


    // Create the range tuple
    range_tuple = new RealTupleType(V);

    // Create a FunctionType (domain_tuple -> range_tuple )
    func_domain_range = new FunctionType(domain_tuple, range_tuple);

    // make sure the grids are equal in size--resample all grids smaller than the maximum dimension
    int maxSize = Math.max(Math.max(xSize, ySize), Math.max(ySize, zSize));

    // Create the domain Set
    domain_set = new Linear3DSet(domain_tuple, yArray[0], yArray[ySize-1], ySize,
                                               xArray[0], xArray[xSize-1], xSize,
                                               zArray[0], zArray[zSize-1], zSize);

    // Get the Set samples to facilitate the calculations
    float[][] set_samples = domain_set.getSamples(true);

    // We create another array, with the same number of elements of
    float[][] flat_samples = new float[1][xSize * ySize * zSize];

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

    for (int i=0; i<ySize; i++) {
    	yMin = yArray[i] < yMin ? yArray[i] : yMin;
    	yMax = yArray[i] > yMax ? yArray[i] : yMax;
    }

    for (int i=0; i<zSize; i++) {
    	zMin = zArray[i] < zMin ? zArray[i] : zMin;
    	zMax = zArray[i] > zMax ? zArray[i] : zMax;
    }

    // fill our 'flat' array
	count = 0;
	for(int i=0; i < xSize; i++) {
		for(int j=0; j < ySize; j++) {
			for (int k=0; k<zSize; k++) {
				double val = valArray[count];
				flat_samples[0][count] = (float)val;
				count++;
			}
		}
	}

    // Create a FlatField
    // Use FlatField(FunctionType type, Set domain_set)
    vals_ff = new FlatField(func_domain_range, domain_set);

    // ...and put the values above into it. Note the argument false, meaning that the array won't be copied
    vals_ff.setSamples(flat_samples, false);

	int axesMax = Math.max(ySize, xSize);
	axesMax = Math.max(axesMax, zSize);
    if (mPlotSpec.isResampleAxes()) {
		// resample so cube axes are all the same length
		Set resampled_domain_set = new Linear3DSet(domain_tuple, yMin, yMax, axesMax,
	                                   xMin, xMax, axesMax,
	                                   zMin, zMax, axesMax);
	    vals_ff = (FlatField)vals_ff.resample(resampled_domain_set, mPlotSpec.getSamplingMode(), Data.DEPENDENT);
	}

    // Create Display and its maps
    // A 3D display
    display = new DisplayImplJ3D("display1");
    display.addDisplayListener(this);

    // Get display's graphics mode control and draw scales
    GraphicsModeControl dispGMC = (GraphicsModeControl)display.getGraphicsModeControl();
    dispGMC.setScaleEnable(true);

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

     // volume variable
    valMap = new ScalarMap(V, Display.RGBA);
    //ScalarMap valIsoMap = new ScalarMap(V,  Display.IsoContour );
    valRangeMap = new ScalarMap(V, Display.SelectRange);
    display.addMap(valRangeMap);
    display.addMap(valMap);
    //display.addMap(valIsoMap);

    // create the range widgets
    selXRange = new SelectRangeWidget(xRangeMap);
    selYRange = new SelectRangeWidget(yRangeMap);

    // Create a data reference and set the FlatField as our data
    data_ref = new DataReferenceImpl("data_ref");
    data_ref.setData(vals_ff);

    // Add reference to display
    display.addReference(data_ref);

    // build the legend
    legend = new LabeledColorWidget(valMap, buildColorTable());

    // Create application window and add display to window
    NcFile ncFile = mModel.getNcFile();
    String name = ncFile.getFileName();
    String s = mModel.getName();
    mFrame = new JFrame("3D " + s + " from " + name);

    // add the menu bar
    mMenuBar = new MenuBar3D(mFrame, (ActionListener)this, false);
    cont = new JPanel();
    cont.setLayout(new BorderLayout(5, 5));
    cont.add("Center", display.getComponent());

	if (mPlotSpec.getLegendStyle() == VisADPlotSpecification.COLOR_LEGEND) {
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

	Rectangle dBounds = mFrame.getBounds();
	Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
	int x = sd.width/2 - dBounds.width/2;
	int y = sd.height/2 - dBounds.height/2;
	mFrame.setLocation(x, y);
    mFrame.setVisible(true);
  }

	public float[][] buildColorTable() {
		int tableLength = 15;

		float[][] myColorTable = new float[4][tableLength];

		for(int i=0;i < tableLength;i++){
			myColorTable[0][i]= (float) i / ((float)tableLength-1.0f); // red component
			myColorTable[2][i]= (float) 1.0f - (float)i / ((float)tableLength-1.0f); // blue component

			if(i<(tableLength)/2){ // lower half of table
				myColorTable[1][i]= 2.0f *(float) i / (tableLength-1); // green component
				myColorTable[3][i]= 0.8f * myColorTable[0][i];
			}
			else{ // upper half of table
				myColorTable[1][i]=  2.0f - 2.0f *(float)i / ((float)tableLength-1); // green component
				myColorTable[3][i]= 0.8f * myColorTable[2][i];// alpha component

			}
		}
		// make lower edge "sharp"; alpha values only
		myColorTable[3][0]= (float) 1.0f; // alpha component
		myColorTable[3][1]= (float) 1.0f; // alpha component

		// make upper edge semi-transparent; alpha values only
		myColorTable[3][13]= (float) 0.5f; // alpha component
		myColorTable[3][14]= (float) 0.5f; // alpha component

		return myColorTable;
	}
}
