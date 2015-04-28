/**
 *  $Id: VisADFull3DVectorRenderer.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.VisAD;

import ncBrowse.Debug;
import ncBrowse.MenuBar3D;
import ncBrowse.NcFile;
import ncBrowse.map.VMapModel;
import ncBrowse.sgt.dm.*;
import ncBrowse.sgt.geom.GeoDate;
import ncBrowse.sgt.geom.SoTRange;
import visad.*;
import visad.java3d.DisplayImplJ3D;
import visad.util.SelectRangeWidget;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.rmi.RemoteException;

//import gov.noaa.pmel.sgt.dm.*;
//import gov.noaa.pmel.util.GeoDate;
//import gov.noaa.pmel.util.SoTRange;

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

public class VisADFull3DVectorRenderer extends VisADPlotRenderer {
  // The domain quantities x and y
  // and the dependent quantities z, zcolor
  private RealType X, Y, Z;
  private RealType U, V, W, VectorColor;

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
  private ScalarMap uMap, vMap, wMap;
  private ScalarMap xRangeMap, yRangeMap, zRangeMap;
  private ScalarMap uRangeMap, vRangeMap, wRangeMap;
  //private ScalarMap vectorColorMap;
  private SGTGrid mColorGrid = null;
  ThreeDGrid uGrid = null;
  ThreeDGrid vGrid = null;
  ThreeDGrid wGrid = null;
  SGTFull3DVector mVector = null;
  private SelectRangeWidget selXRange;
  private SelectRangeWidget selYRange;
  private boolean xIsReversed = false;
  private boolean yIsReversed = false;
  private boolean zIsReversed = false;
  private VisADPlotSpecification mPlotSpec;
  private FlowControl flow_controlU;

	public VisADFull3DVectorRenderer(SGTData inVector, VMapModel model, VisADPlotSpecification spec) {
		mVector = (SGTFull3DVector)inVector;
		uGrid = ((SGTFull3DVector)inVector).getU();
		vGrid = ((SGTFull3DVector)inVector).getV();
		wGrid = ((SGTFull3DVector)inVector).getW();
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
    	return false;
    }

	public void stateChanged(ChangeEvent e) {
		double xmin = 0.0, xmax = 0.0;
		double ymin = 0.0, ymax = 0.0;
		double zmin = 0.0, zmax = 0.0;
		if(Debug.DEBUG)
			System.out.println("VisADFull3DVectorRenderer.stateChanged()");
		Object obj = e.getSource();
		if (obj instanceof VMapModel) {
			mPlotSpec = ((VMapModel)obj).getVisADPlotSpecification();
	  		xIsReversed = ((VMapModel)obj).isXReversed();
	  		yIsReversed = ((VMapModel)obj).isYReversed();
	  		zIsReversed = ((VMapModel)obj).isZReversed();
			SGTData[] dobj = ((VMapModel)obj).getSGTData();
			if (dobj[0] instanceof SGTFull3DVector) {
				mVector = (SGTFull3DVector)dobj[0];
				uGrid = mVector.getU();
				vGrid = mVector.getV();
				wGrid = mVector.getW();
				if (!uGrid.isXTime()) {
					SoTRange xR = uGrid.getXRange();
			        xmin = ((Number)xR.getStart().getObjectValue()).floatValue();
			        xmax = ((Number)xR.getEnd().getObjectValue()).floatValue();
			        try {
						if (xIsReversed)
							xMap.setRange(xmax, xmin);
						else
							xMap.setRange(xmin, xmax);
						xRangeMap.setRange(xmin, xmax);
						//xMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}
				else {
 					GeoDate[] gda = uGrid.getTimeArray();
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
						//xMap.setRange((float)min,(float) max);
					}
					catch (Exception ignored) {}
				}

				if (!uGrid.isYTime()) {
					SoTRange yR = uGrid.getYRange();
			        ymin = ((Number)yR.getStart().getObjectValue()).floatValue();
			        ymax = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						if (yIsReversed)
							yMap.setRange(ymax, ymin);
						else
							yMap.setRange(ymin, ymax);
						yRangeMap.setRange(ymin, ymax);
						//yMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}
				else {
 					GeoDate[] gda = uGrid.getTimeArray();
 					SoTRange yR = ((VMapModel)obj).computeRange(gda);
			        ymin = ((Number)yR.getStart().getObjectValue()).floatValue();
			        ymax = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						if (yIsReversed)
							yMap.setRange(ymax, ymin);
						else
							yMap.setRange(ymin, ymax);
						yRangeMap.setRange(ymin, ymax);
						//yMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}

				if (!uGrid.isZTime()) {
					SoTRange zR = uGrid.getZRange();
			        zmin = ((Number)zR.getStart().getObjectValue()).floatValue();
			        zmax = ((Number)zR.getEnd().getObjectValue()).floatValue();
			        try {
						if (zIsReversed)
							zMap.setRange(zmax, zmin);
						else
							zMap.setRange(zmin, zmax);
						zRangeMap.setRange(zmin, zmax);
						//zMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}
				else {
 					GeoDate[] gda = uGrid.getTimeArray();
 					SoTRange zR = ((VMapModel)obj).computeRange(gda);
			        zmin = ((Number)zR.getStart().getObjectValue()).floatValue();
			        zmax = ((Number)zR.getEnd().getObjectValue()).floatValue();
			        try {
						if (zIsReversed)
							zMap.setRange(zmax, zmin);
						else
							zMap.setRange(zmin, zmax);
						zRangeMap.setRange(zmin, zmax);
						//zMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}
			}

			// reset the sample flatfield
 			int xSize;
 			int ySize;
 			int zSize;

 			if (uGrid.isXTime()) {
		 		GeoDate[] gda = uGrid.getTimeArray();
		 		xSize = gda.length;
 			}
 			else
 				xSize = uGrid.getXSize();

 			if (uGrid.isYTime()) {
		 		GeoDate[] gda = uGrid.getTimeArray();
		 		ySize = gda.length;
 			}
 			else {
	 			ySize = uGrid.getYSize();
 			}

 			if (uGrid.isZTime()) {
		 		GeoDate[] gda = uGrid.getTimeArray();
		 		zSize = gda.length;
 			}
 			else {
	 			zSize = uGrid.getZArray().length;
 			}

		    float[][] flat_samples = new float[3][xSize * ySize * zSize];
 			double[] uArray = uGrid.getValArray();
 			double[] vArray = vGrid.getValArray();
 			double[] wArray = wGrid.getValArray();
		    int count = 0;
		    for(int i=0; i < xSize; i++) {
				for(int j=0; j < ySize; j++) {
					for (int k=0; k<zSize; k++) {
						double uval = uArray[count];
						double vval = vArray[count];
						double wval = wArray[count];

						flat_samples[0][count] = mPlotSpec.isUseU()? (float)uval : 0.0f;
						flat_samples[1][count] = mPlotSpec.isUseV()? (float)vval : 0.0f;
						flat_samples[2][count] = mPlotSpec.isUseW()? (float)wval : 0.0f;
						count++;
					}
				}
			}

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

	    	try {
			    float vScale = mPlotSpec.getVectorScale();
				flow_controlU.setFlowScale(vScale);
			}
			catch (Exception ignored) {}
		}
	}

 public void draw() throws VisADException, RemoteException {
 	if (!(mVector instanceof SGTFull3DVector))
 		return;

 	SGTMetaData xMetaData = uGrid.getXMetaData();
 	SGTMetaData yMetaData = uGrid.getYMetaData();
 	SGTMetaData zMetaData = uGrid.getZMetaData();
 	SGTMetaData uMetaData = uGrid.getValMetaData();
 	SGTMetaData vMetaData = vGrid.getValMetaData();
 	SGTMetaData wMetaData = wGrid.getValMetaData();

 	// x axis
 	double[] xArray;
 	int xSize;
 	if (!uGrid.isXTime()) {
 		xArray = uGrid.getXArray();
 		xSize = uGrid.getXSize();
 	}
 	else {
 		// is time
 		GeoDate[] gda = uGrid.getTimeArray();
 		xSize = gda.length;
 		xArray = new double[xSize];
 		for (int i=0; i<xSize; i++) {
 			xArray[i] = (double)(gda[i].getTime());
		}
 	}

 	int ySize;
 	double[] yArray;
 	if (!uGrid.isYTime()) {
 		yArray = uGrid.getYArray();
 		ySize = uGrid.getYSize();
 	}
 	else {
 		// is time
 		GeoDate[] gda = uGrid.getTimeArray();
 		ySize = gda.length;
 		yArray = new double[ySize];
 		for (int i=0; i<ySize; i++) {
 			yArray[i] = (double)(gda[i].getTime());
		}
 	}

 	int zSize;
 	double[] zArray;
 	if (!uGrid.isZTime()) {
 		zArray = uGrid.getZArray();
 		zSize = zArray.length;
 	}
 	else {
 		// is time
 		GeoDate[] gda = uGrid.getTimeArray();
 		zSize = gda.length;
 		zArray = new double[zSize];
 		for (int i=0; i<zSize; i++) {
 			zArray[i] = (double)(gda[i].getTime());
		}
 	}

 	double[] uArray = uGrid.getValArray();
 	double[] vArray = vGrid.getValArray();
 	double[] wArray = wGrid.getValArray();
 	int uSize = uArray.length;
 	int vSize = vArray.length;
 	int wSize = wArray.length;

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

    // create the Z axis
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


    domain_tuple = new RealTupleType(Y, X, Z);

	// create U and V
    count = 0;
    while (true) {
    	String typeName;
    	if (count > 0)
    		typeName = uMetaData.getName() + "_" + count;
    	else
    		typeName = uMetaData.getName();
	    try {
	    	U = RealType.getRealType(typeName, null, null);
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
    		typeName = vMetaData.getName() + "_" + count;
    	else
    		typeName = vMetaData.getName();
	    try {
	    	V = RealType.getRealType(typeName, null, null);
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
    		typeName = wMetaData.getName() + "_" + count;
    	else
    		typeName = wMetaData.getName();
	    try {
	    	W = RealType.getRealType(typeName, null, null);
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

	/*if (mColorGrid != null) {
	    count = 0;
	    while (true) {
	    	String typeName;
	    	if (count > 0)
	    		typeName = zColorMetaData.getName() + "_color" + "_" + count;
	    	else
	    		typeName = zColorMetaData.getName() + "_color";
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
	}
	else {
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

    // Create the range tuple
    range_tuple = new RealTupleType(U, V, W);

    // Create a FunctionType (domain_tuple -> range_tuple )
    func_domain_range = new FunctionType(domain_tuple, range_tuple);

    // Create the domain Set
    domain_set = new Linear3DSet(domain_tuple, yArray[0], yArray[ySize-1], ySize,
                                               xArray[0], xArray[xSize-1], xSize,
                                               zArray[0], zArray[zSize-1], zSize);

    // Get the Set samples to facilitate the calculations
    //float[][] set_samples = domain_set.getSamples(true);

    // We create another array, with the same number of elements of
    float[][] flat_samples = new float[3][xSize * ySize * zSize];

    // compute the z multiplier
	double umin = 99e99;
	double umax = -99e99;
	double vmin = 99e99;
	double vmax = -99e99;
	double wmin = 99e99;
	double wmax = -99e99;
	count = 0;
	for(int i=0; i < xSize; i++) {
		for(int j=0; j < ySize; j++) {
			for (int k=0; k<zSize; k++) {
				double uval = uArray[count];
				double vval = vArray[count];
				double wval = wArray[count];

				umin = uval < umin ? uval : umin;
				umax = uval > umax ? uval : umax;
				vmin = vval < vmin ? vval : vmin;
				vmax = vval > vmax ? vval : vmax;
				wmin = wval < wmin ? wval : wmin;
				wmax = wval > wmax ? wval : wmax;
				count++;
			}
		}
	}

	double uRange = Math.abs(umax - umin);
	double vRange = Math.abs(vmax - vmin);
	double wRange = Math.abs(wmax - wmin);
	double maxUVRange = Math.max(uRange, vRange);
	maxUVRange = Math.max(maxUVRange, wRange);
	double mult = maxUVRange/wRange;

    // ...and then we fill our 'flat' array
	count = 0;
	for(int i=0; i < xSize; i++) {
		for(int j=0; j < ySize; j++) {
			for (int k=0; k<zSize; k++) {
				double uval = uArray[count];
				double vval = vArray[count];
				double wval = wArray[count];
				flat_samples[0][count] = mPlotSpec.isUseU()? (float)uval : 0.0f;
				flat_samples[1][count] = mPlotSpec.isUseV()? (float)vval : 0.0f;
				flat_samples[2][count] = mPlotSpec.isUseW()? (float)wval : 0.0f;
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
		Set resampled_domain_set = new Linear3DSet(domain_tuple, yArray[0], yArray[ySize-1], axesMax,
                                               xArray[0], xArray[xSize-1], axesMax,
                                               zArray[0], zArray[zSize-1], axesMax);
	    vals_ff = (FlatField)vals_ff.resample(resampled_domain_set, mPlotSpec.getSamplingMode(), Data.DEPENDENT);
	}

    // Create Display and its maps
    // A 3D display
    display = new DisplayImplJ3D("display1");
    display.addDisplayListener(this);

    // Get display's graphics mode control and draw scales
    GraphicsModeControl dispGMC = display.getGraphicsModeControl();
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

    // Create the ScalarMaps
    yMap = new ScalarMap(Y, Display.YAxis);
    xMap = new ScalarMap(X, Display.XAxis);
    zMap = new ScalarMap(Z, Display.ZAxis);
    xRangeMap = new ScalarMap(X, Display.SelectRange);
    yRangeMap = new ScalarMap(Y, Display.SelectRange);
    zRangeMap = new ScalarMap(Z, Display.SelectRange);

    // Add maps to display
    display.addMap(yMap);
    display.addMap(xMap);
    display.addMap(zMap);
    display.addMap(xRangeMap);
    display.addMap(yRangeMap);
    display.addMap(zRangeMap);

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

     // "u" variable
    uMap = new ScalarMap(U, Display.Flow1X);
    uRangeMap = new ScalarMap(U, Display.SelectRange);
    display.addMap(uRangeMap);
    display.addMap(uMap);

     // "u" variable
    vMap = new ScalarMap(V, Display.Flow1Y);
    vRangeMap = new ScalarMap(V, Display.SelectRange);
    display.addMap(vRangeMap);
    display.addMap(vMap);

     // "w" variable
    wMap = new ScalarMap(W, Display.Flow1Z);
    wRangeMap = new ScalarMap(W, Display.SelectRange);
    display.addMap(wRangeMap);
    display.addMap(wMap);

    // dink with the scaling
    float vScale = mPlotSpec.getVectorScale();
	flow_controlU = (FlowControl)uMap.getControl();
	flow_controlU.setFlowScale(vScale);

    // create the range widgets
    selXRange = new SelectRangeWidget(xRangeMap);
    selYRange = new SelectRangeWidget(yRangeMap);

    // Create a data reference and set the FlatField as our data
    data_ref = new DataReferenceImpl("data_ref");
    data_ref.setData(vals_ff);

    // Add reference to display
    display.addReference(data_ref);

    // Create application window and add display to window
    NcFile ncFile = mModel.getNcFile();
    String name = ncFile.getFileName();
    String s = mModel.getName();
    mFrame = new JFrame("3D " + s + " from " + name);
   // JPanel plotPanel = new JPanel();
   // plotPanel.add("Center", display.getComponent());
    //JPanel controlsPanel = new JPanel();
   // controlsPanel.add(selXRange);
   // plotPanel.add("South", controlsPanel);
    //jframe.getContentPane().add(display.getComponent());

    // add the menu bar
    mMenuBar = new MenuBar3D(mFrame, this, false);
    JPanel cont = new JPanel();
    cont.setLayout(new BorderLayout(5, 5));
    cont.add("Center", display.getComponent());

	//if (mColorVar != null && mPlotSpec.getLegendStyle() == VisADPlotSpecification.COLOR_LEGEND) {
	//	// simple color legend in south
	//	ChosenColorWidget legend = new ChosenColorWidget(zRGBMap);
    //	cont.add("South", legend);
	//}

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
}
