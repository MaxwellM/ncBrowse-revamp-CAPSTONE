/**
 *  $Id: VisADSurfaceRenderer.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.VisAD;

import javax.swing.event.ChangeEvent;
import javax.swing.*;
import visad.*;
import visad.java3d.*;
import java.rmi.RemoteException;
//import gov.noaa.pmel.sgt.dm.*;
import gov.noaa.pmel.util.SoTRange;
import gov.noaa.pmel.util.GeoDate;
import ncBrowse.map.*;
import visad.util.*;
import java.awt.event.*;
import java.awt.*;
import ncBrowse.*;

import gov.noaa.pmel.sgt.dm.SGTGrid;
import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTMetaData;

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

public class VisADSurfaceRenderer extends VisADPlotRenderer {
  // The domain quantities x and y and the dependent quantities z, zcolor
  private RealType X, Y;
  private RealType Z, ZColor;

  // Two Tuples: one to pack longitude and latitude together, as the domain
  // and the other for the range (altitude, temperature)
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
  private ScalarMap yMap, xMap;
  private ScalarMap xRangeMap, yRangeMap, zRangeMap, zRGBRangeMap;
  private ScalarMap zMap, zRGBMap;
  private SGTGrid mGrid, mColorGrid = null;
  private SelectRangeWidget selXRange;
  private SelectRangeWidget selYRange;
  private boolean xIsReversed = false;
  private boolean yIsReversed = false;
  private VisADPlotSpecification mPlotSpec;
  private LabeledColorWidget legend;
  private boolean legendIsVisible = false;
  private JPanel cont;

	public VisADSurfaceRenderer(SGTData inGrid, SGTData inColorGrid, VMapModel model, VisADPlotSpecification spec) {
		mGrid = (SGTGrid)inGrid;
		mColorGrid = (SGTGrid)inColorGrid;
		mModel = model;
		mPlotSpec = spec;
		mModel.addChangeListener(this);
  		xIsReversed = mModel.isXReversed();
  		yIsReversed = mModel.isYReversed();
	}

	public void setPlotSpecification(VisADPlotSpecification spec) {
		mPlotSpec = spec;
	}

    public boolean canDisplayLegend() {
    	return mColorGrid == null;
    }

	public void stateChanged(ChangeEvent e) {
		double xmin = 0.0, xmax = 0.0;
		double ymin = 0.0, ymax = 0.0;
		if(Debug.DEBUG)
			System.out.println("VisADGridRenderer.stateChanged()");
		Object obj = e.getSource();
		if (obj instanceof VMapModel) {
	  		xIsReversed = ((VMapModel)obj).isXReversed();
	  		yIsReversed = ((VMapModel)obj).isYReversed();
			SGTData[] dobj = ((VMapModel)obj).getSGTData();
			boolean hasColor = false;
			if (dobj[1] != null)
				hasColor = true;
			if (dobj[0] instanceof SGTGrid) {
				if (!mGrid.isXTime()) {
					SoTRange xR = ((SGTGrid)dobj[0]).getXRange();
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
 					GeoDate[] gda = ((SGTGrid)dobj[0]).getTimeArray();
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

				if (!mGrid.isYTime()) {
					SoTRange yR = ((SGTGrid)dobj[0]).getYRange();
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
 					GeoDate[] gda = ((SGTGrid)dobj[0]).getTimeArray();
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
			}

			// reset the sample flatfield
		    int count = 0;
 			int xSize;
 			int ySize;

 			if (((SGTGrid)dobj[0]).isXTime()) {
		 		GeoDate[] gda = ((SGTGrid)dobj[0]).getTimeArray();
		 		xSize = gda.length;
 			}
 			else
 				xSize = ((SGTGrid)dobj[0]).getXSize();

 			if (((SGTGrid)dobj[0]).isYTime()) {
		 		GeoDate[] gda = ((SGTGrid)dobj[0]).getTimeArray();
		 		ySize = gda.length;
 			}
 			else {
	 			ySize = ((SGTGrid)dobj[0]).getYSize();
 			}

		    float[][] flat_samples = new float[2][xSize * ySize];
 			double[] zArray = ((SGTGrid)dobj[0]).getZArray();

 			double[] zColorArray = null;
 			if (hasColor)
 				zColorArray = ((SGTGrid)dobj[1]).getZArray();

 			double min = 99e99;
 			double max = -99e99;
 			double cmin = 99e99;
 			double cmax = -99e99;
		    for(int i=0; i < xSize; i++) {
				for(int j=0; j < ySize; j++) {
					double val = zArray[count];
					flat_samples[0][count] = (float)val;
					min = val < min ? val : min;
					max = val > max ? val : max;

					double cval;
					if (hasColor)
						cval = zColorArray[count];
					else
						cval = zArray[count];
					flat_samples[1][count] = (float)cval;
					cmin = cval < cmin ? cval : cmin;
					cmax = cval > cmax ? cval : cmax;
					count++;
				}
		    }

			try {
				// reset the range map
				zRangeMap.setRange(min, max);
				zMap.setRange(min, max);
				zRGBMap.setRange(cmin, cmax);
				zRGBRangeMap.setRange(cmin, cmax);
			}
			catch (Exception ex) {ex.printStackTrace();}

		    try {
   				domain_set = new Linear2DSet(domain_tuple, ymin, ymax, ySize,
                                               xmin, xmax, xSize);
    			vals_ff = new FlatField(func_domain_range, domain_set);
    			vals_ff.setSamples(flat_samples, false);

				int axesMax = Math.max(ySize, xSize);
			    if (mPlotSpec.isResampleAxes()) {
					// resample so cube axes are all the same length
					Set resampled_domain_set = new Linear2DSet(domain_tuple, ymin, ymax, axesMax,
			                                               xmin, xmax, axesMax);
				    vals_ff = (FlatField)vals_ff.resample(resampled_domain_set, mPlotSpec.getSamplingMode(), Data.DEPENDENT);
				}
    			data_ref.setData(vals_ff);

				ProjectionControl projCont = display.getProjectionControl();
				if (mPlotSpec.isRespectDataAspectRatio() != mCurrAspectPolicy || mCurrXScaling != mPlotSpec.getXAxisScaling() ||
				    mCurrYScaling != mPlotSpec.getYAxisScaling()) {
				    projCont.saveProjection();
				    if (mPlotSpec.isRespectDataAspectRatio()) {
					    float xAspect = (float)xSize/(float)axesMax;
						xAspect  *= mPlotSpec.getXAxisScaling();
					    float yAspect = (float)ySize/(float)axesMax;
						yAspect  *= mPlotSpec.getYAxisScaling();
					    float zAspect = 1.0f;
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
		}

		mPlotSpec = ((VMapModel)obj).getVisADPlotSpecification();
		if (legend != null && mPlotSpec.getLegendStyle() > 0 && !legendIsVisible && mColorGrid != null) {
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
 	if (!(mGrid instanceof SGTGrid))
 		return;

 	SGTMetaData xMetaData = mGrid.getXMetaData();
 	SGTMetaData yMetaData = mGrid.getYMetaData();
 	SGTMetaData zMetaData = mGrid.getZMetaData();
 	SGTMetaData zColorMetaData = null;
 	if (mColorGrid != null)
 		zColorMetaData = mColorGrid.getZMetaData();
 	double[] xArray;
 	int xSize;
 	if (!mGrid.isXTime()) {
 		xArray = mGrid.getXArray();
 		xSize = mGrid.getXSize();
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
 	int ySize = mGrid.getYSize();
 	double[] zColorArray = null;
 	SGTGrid zGrid = mGrid.getAssociatedData();
 	SGTGrid zColorGrid = null;
 	if (mColorGrid != null) {
 		zColorArray = mColorGrid.getZArray();
 		zColorGrid = mColorGrid.getAssociatedData();
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

    domain_tuple = new RealTupleType(Y, X);

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

	if (mColorGrid != null) {
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
	}

    // Create the range tuple
    range_tuple = new RealTupleType(Z, ZColor);

    // Create a FunctionType (domain_tuple -> range_tuple )
    func_domain_range = new FunctionType(domain_tuple, range_tuple);

    // Create the domain Set
    int NCOLS = ySize;
    int NROWS = xSize;

    domain_set = new Linear2DSet(domain_tuple, yArray[0], yArray[ySize-1], ySize,
                                               xArray[0], xArray[xSize-1], xSize);

    // Get the Set samples to facilitate the calculations
    float[][] set_samples = domain_set.getSamples(true);

    // We create another array, with the same number of elements of
    // z and zColor, but organized as float[2][ number_of_samples ]
    float[][] flat_samples = new float[2][xSize * ySize];

    // fill our 'flat' array with values by looping over NCOLS and NROWS
    count = 0;
    for(int i=0; i < xSize; i++) {
      for(int j=0; j < ySize; j++) {
        double val = zArray[count];
        flat_samples[0][count] = (float)val;
        if (mColorGrid != null) {
        	double cval = zColorArray[count];
        	flat_samples[1][count] = (float)cval;
        }
        else {
        	flat_samples[1][count] = (float)val;
        }
        count++;
      }
    }

    // Create a FlatField
    // Use FlatField(FunctionType type, Set domain_set)
    vals_ff = new FlatField(func_domain_range, domain_set);

    // ...and put the values above into it. Note the argument false, meaning that the array won't be copied
    vals_ff.setSamples(flat_samples, false);

	int axesMax = Math.max(ySize, xSize);
    if (mPlotSpec.isResampleAxes()) {
		// resample so cube axes are all the same length
		Set resampled_domain_set = new Linear2DSet(domain_tuple, yArray[0], yArray[ySize-1], axesMax,
                                               xArray[0], xArray[xSize-1], axesMax);
	    vals_ff = (FlatField)vals_ff.resample(resampled_domain_set, mPlotSpec.getSamplingMode(), Data.DEPENDENT);
	}

    // Create Display and its maps
    // A 3D display
    display = new DisplayImplJ3D("display1");
    display.addDisplayListener(this);

    // Get display's graphics mode control and draw scales
    GraphicsModeControl dispGMC = (GraphicsModeControl)display.getGraphicsModeControl();
    dispGMC.setScaleEnable(true);

    // get the display 3D Projection control
    ProjectionControlJ3D projCntrl = (ProjectionControlJ3D)display.getProjectionControl();

    if (mPlotSpec.isRespectDataAspectRatio()) {
	    ProjectionControl projCont = display.getProjectionControl();
	    float xAspect = (float)xSize/(float)axesMax;
		xAspect  *= mPlotSpec.getXAxisScaling();

	    float yAspect = (float)ySize/(float)axesMax;
		yAspect  *= mPlotSpec.getYAxisScaling();

	    float zAspect = 1.0f;

	    double[] aspect = new double[]{xAspect, yAspect, zAspect};
	    projCont.setAspect(aspect);
	}

	mCurrAspectPolicy = mPlotSpec.isRespectDataAspectRatio();
	mCurrXScaling = mPlotSpec.getXAxisScaling();
	mCurrYScaling = mPlotSpec.getYAxisScaling();
	mCurrZScaling = 1.0f;

    // Create the ScalarMaps
    yMap = new ScalarMap(Y, Display.YAxis);
    xMap = new ScalarMap(X, Display.XAxis);
    xRangeMap = new ScalarMap(X, Display.SelectRange);
    yRangeMap = new ScalarMap(Y, Display.SelectRange);

    double yMin = 9999999e99;
    double xMin = 9999999e99;
    double yMax = 9999999e99;
    double xMax = -9999999e99;

    for (int i=0; i<xSize; i++) {
    	xMin = xArray[i] < xMin ? xArray[i] : xMin;
    	xMax = xArray[i] > xMax ? xArray[i] : xMax;
    }

    for (int i=0; i<ySize; i++) {
    	yMin = yArray[i] < yMin ? yArray[i] : yMin;
    	yMax = yArray[i] > yMax ? yArray[i] : yMax;
    }

	if (xIsReversed)
		xRangeMap.setRange(xMax, xMin);
	else
		xRangeMap.setRange(xMin, xMax);

	if (yIsReversed)
		yRangeMap.setRange(yMax, yMin);
	else
		yRangeMap.setRange(yMin, yMax);


    // Add maps to display
    display.addMap(yMap);
    display.addMap(xMap);
    display.addMap(xRangeMap);
    display.addMap(yRangeMap);

     // "contour" variable on z-axis
    zMap = new ScalarMap(Z, Display.ZAxis);
    zRangeMap = new ScalarMap(Z, Display.SelectRange);
    display.addMap(zMap);

    if (mColorGrid != null) {
    	zRGBMap = new ScalarMap(ZColor, Display.RGBA);
    }
    else {
    	zRGBMap = new ScalarMap(ZColor, Display.Value);
    }

	zRGBRangeMap = new ScalarMap(ZColor, Display.SelectRange);
	display.addMap(zRGBMap);
	display.addMap(zRGBRangeMap);

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

    // add the menu bar
    mMenuBar = new MenuBar3D(mFrame, (ActionListener)this, false);
    cont = new JPanel();
    cont.setLayout(new BorderLayout(5, 5));
    cont.add("Center", display.getComponent());

	if (mColorGrid != null && zRGBMap != null) {
    	legend = new LabeledColorWidget(zRGBMap, buildColorTable());
		if (mPlotSpec.getLegendStyle() == VisADPlotSpecification.COLOR_LEGEND) {
			// simple color legend in south
    		cont.add("South", legend);
    		legendIsVisible = true;
    	}
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
