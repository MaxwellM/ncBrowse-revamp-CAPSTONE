/**
 * $Id: VisAD2DVectorRenderer.java 15 2013-04-24 19:16:08Z dwd $
 *
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package ncBrowse.VisAD;

import ncBrowse.sgt.dm.SGTData;
//import gov.noaa.pmel.sgt.dm.SGTData;
import ncBrowse.sgt.dm.SGTGrid;
//import gov.noaa.pmel.sgt.dm.SGTGrid;
import ncBrowse.sgt.dm.SGTMetaData;
//import gov.noaa.pmel.sgt.dm.SGTMetaData;
import ncBrowse.sgt.dm.SGTVector;
//import gov.noaa.pmel.sgt.dm.SGTVector;
import ncBrowse.sgt.geom.GeoDate;
//import gov.noaa.pmel.util.GeoDate;
import ncBrowse.sgt.geom.SoTRange;
//import gov.noaa.pmel.util.SoTRange;
import ncBrowse.Debug;
import ncBrowse.NcFile;
import ncBrowse.map.VMapModel;
import visad.*;
import visad.java2d.DisplayImplJ2D;
import visad.util.SelectRangeWidget;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.rmi.RemoteException;

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

public class VisAD2DVectorRenderer extends VisADPlotRenderer {
  // The domain quantities x and y  and the dependent quantities u, v, and vectorcolor (future)
  private RealType X, Y;
  private RealType U, V, VectorColor;

  // Two Tuples: one to pack x and y together, as the domain  and the other for the range (u, v)
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
  private ScalarMap uMap, vMap;
  private ScalarMap xRangeMap, yRangeMap;
  private ScalarMap uRangeMap, vRangeMap;
  //private ScalarMap vectorColorMap;
  private SGTGrid mColorGrid = null;
  SGTGrid xGrid = null;
  SGTGrid yGrid = null;
  SGTGrid uGrid = null;
  SGTGrid vGrid = null;
  SGTVector mVector = null;
  private VMapModel mModel;
  private SelectRangeWidget selXRange;
  private SelectRangeWidget selYRange;

	public VisAD2DVectorRenderer(SGTData inVector, SGTData inColorGrid, VMapModel model) {
		mVector = (SGTVector)inVector;
		xGrid = ((SGTVector)inVector).getU();
		yGrid = ((SGTVector)inVector).getV();
		uGrid = ((SGTVector)inVector).getU();
		vGrid = ((SGTVector)inVector).getV();
		mColorGrid = (SGTGrid)inColorGrid;
		mModel = model;
		mModel.addChangeListener(this);
	}

	public void stateChanged(ChangeEvent e) {
		if(Debug.DEBUG)
			System.out.println("VisADGridRenderer.stateChanged()");
		Object obj = e.getSource();
		if (obj instanceof VMapModel) {
			SGTData[] dobj = ((VMapModel)obj).getSGTData();
			boolean hasColor = false;
			//if (dobj[1] != null)
			//	hasColor = true;
			if (dobj[0] instanceof SGTVector) {
				mVector = (SGTVector)dobj[0];
				xGrid = mVector.getU();
				yGrid = mVector.getV();
				uGrid = mVector.getU();
				vGrid = mVector.getV();
				if (!xGrid.isXTime()) {
					SoTRange xR = xGrid.getXRange();
			        float min = ((Number)xR.getStart().getObjectValue()).floatValue();
			        float max = ((Number)xR.getEnd().getObjectValue()).floatValue();
			        try {
						xRangeMap.setRange(min, max);
						xMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}
				else {
 					GeoDate[] gda = xGrid.getTimeArray();
 					SoTRange xR = ((VMapModel)obj).computeRange(gda);
			        double tmin = ((GeoDate)xR.getStart().getObjectValue()).getTime();
			        double tmax = ((GeoDate)xR.getEnd().getObjectValue()).getTime();
			        double min = Math.min(tmin, tmax);
			        double max = Math.max(tmin, tmax);
			        try {
						xRangeMap.setRange((float)min, (float)max);
						xMap.setRange((float)min,(float) max);
					}
					catch (Exception ignored) {}
				}

				if (!yGrid.isYTime()) {
					SoTRange yR = yGrid.getYRange();
			        float min = ((Number)yR.getStart().getObjectValue()).floatValue();
			        float max = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						yRangeMap.setRange(min, max);
						yMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}
				else {
 					GeoDate[] gda = yGrid.getTimeArray();
 					SoTRange yR = ((VMapModel)obj).computeRange(gda);
			        float min = ((Number)yR.getStart().getObjectValue()).floatValue();
			        float max = ((Number)yR.getEnd().getObjectValue()).floatValue();
			        try {
						yRangeMap.setRange(min, max);
						yMap.setRange(min, max);
					}
					catch (Exception ignored) {}
				}
			}

			// reset the sample flatfield
 			int xSize;
 			int ySize;

 			if (xGrid.isXTime()) {
		 		GeoDate[] gda = xGrid.getTimeArray();
		 		xSize = gda.length;
 			}
 			else
 				xSize = xGrid.getXSize();

 			if (yGrid.isYTime()) {
		 		GeoDate[] gda = yGrid.getTimeArray();
		 		ySize = gda.length;
 			}
 			else {
	 			ySize = yGrid.getYSize();
 			}

		    float[][] flat_samples = new float[2][xSize * ySize];
 			double[] uArray = uGrid.getZArray();
 			double[] vArray = vGrid.getZArray();

 			//for (int i=0; i<xSize * ySize; i++) {
 			//	System.out.println("u= " + uArray[i] + " v= " + vArray[i]);
 			//}

 			//double[] zColorArray = null;
 			//if (hasColor)
 			//	zColorArray = ((SGTGrid)dobj[1]).getZArray();

 			double umin = 99e99;
 			double umax = -99e99;
 			double vmin = 99e99;
 			double vmax = -99e99;
 			//double cmin = 99e99;
 			//double cmax = -99e99;
		    int count = 0;
		    for(int i=0; i < xSize; i++) {
				for(int j=0; j < ySize; j++) {
					double uval = uArray[count];
					double vval = vArray[count];

					flat_samples[0][count] = (float)uval;
					flat_samples[1][count] = (float)vval;
					umin = uval < umin ? uval : umin;
					umax = uval > umax ? uval : umax;
					vmin = vval < vmin ? vval : vmin;
					vmax = vval > vmax ? vval : vmax;

					//double cval;
					//if (hasColor)
					//	cval = zColorArray[count];
					//else
					//	cval = zArray[count];
					//flat_samples[1][count] = (float)cval;
					//cmin = cval < cmin ? cval : cmin;
					//cmax = cval > cmax ? cval : cmax;
					count++;
				}
			}
			try {
				// reset the range map
				uRangeMap.setRange(umin, umax);
				uMap.setRange(umin, umax);
				vRangeMap.setRange(vmin, vmax);
				vMap.setRange(vmin, vmax);
				//zRGBMap.setRange(cmin, cmax);
				//zRGBRangeMap.setRange(cmin, cmax);
			}
			catch (Exception ignored) {}
		    try {
    			vals_ff.setSamples(flat_samples, false);
    		}
    		catch (Exception ignored) {}
		}
	}

 public void draw() throws VisADException, RemoteException {
 	if (!(mVector instanceof SGTVector))
 		return;

 	SGTMetaData xMetaData = xGrid.getXMetaData();
 	SGTMetaData yMetaData = yGrid.getYMetaData();
 	SGTMetaData uMetaData = uGrid.getXMetaData();
 	SGTMetaData vMetaData = vGrid.getYMetaData();
 	//SGTMetaData vectorColorMetaData = null;
 	//if (mColorGrid != null)
 	//	vectorColorMetaData = mColorGrid.getZMetaData();
 	double[] xArray;
 	int xSize;
 	if (!xGrid.isXTime()) {
 		xArray = xGrid.getXArray();
 		xSize = xGrid.getXSize();
 	}
 	else {
 		// is time
 		GeoDate[] gda = xGrid.getTimeArray();
 		xSize = gda.length;
 		xArray = new double[xSize];
 		for (int i=0; i<xSize; i++) {
 			xArray[i] = (double)(gda[i].getTime());
		}
 	}
 	double[] yArray = yGrid.getYArray();
 	double[] uArray = uGrid.getZArray();
 	double[] vArray = vGrid.getZArray();
 	int ySize = yGrid.getYSize();
 	int uSize = uArray.length;//uGrid.getXSize();
 	int vSize = vArray.length;//vGrid.getYSize();
 	//double[] vectorColorArray = null;
 	//SGTGrid uGrid = mGrid.getAssociatedData();
 	//SGTGrid vectorColorGrid = null;
 	//if (mColorGrid != null) {
 	//	vectorColorArray = mColorGrid.getZArray();
 	//	vectorColorGrid = mColorGrid.getAssociatedData();
 	//}

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

    domain_tuple = new RealTupleType(Y, X);

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

    // Create the range tuple ( altitude, temperature )
    range_tuple = new RealTupleType(U, V);

    // Create a FunctionType (domain_tuple -> range_tuple )
    func_domain_range = new FunctionType(domain_tuple, range_tuple);

    // Create the domain Set
    domain_set = new Linear2DSet(domain_tuple, yArray[0], yArray[ySize-1], ySize,
                                               xArray[0], xArray[xSize-1], xSize);

    // Get the Set samples to facilitate the calculations
    float[][] set_samples = domain_set.getSamples(true);

    // We create another array, with the same number of elements of
    // u and v, but organized as float[2][number_of_samples]
    float[][] flat_samples = new float[2][xSize * ySize];

    // we fill the 'flat' array
    count = 0;
    for(int i=0; i < xSize; i++) {
      for(int j=0; j < ySize; j++) {
        double uval = uArray[count];
        double vval = vArray[count];
        flat_samples[0][count] = (float)uval;
        flat_samples[1][count] = (float)vval;
        //if (mColorGrid != null) {
       // 	double cval = zColorArray[count];
        //	flat_samples[1][count] = (float)cval;
        //}
       // else {
        //	flat_samples[1][count] = (float)val;
       // }
        count++;
      }
    }

    // Create a FlatField
    // Use FlatField(FunctionType type, Set domain_set)
    vals_ff = new FlatField(func_domain_range, domain_set);

    // ...and put the values above into it. Note the argument false, meaning that the array won't be copied
    vals_ff.setSamples(flat_samples, false);

    // Create Display and its maps
    // A 3D display
    display = new DisplayImplJ2D("display1");
    display.addDisplayListener(this);

    // Get display's graphics mode control and draw scales
    GraphicsModeControl dispGMC = display.getGraphicsModeControl();
    dispGMC.setScaleEnable(true);

    // Create the ScalarMaps
    yMap = new ScalarMap(Y, Display.YAxis);
    xMap = new ScalarMap(X, Display.XAxis);
    xRangeMap = new ScalarMap(X, Display.SelectRange);
    yRangeMap = new ScalarMap(Y, Display.SelectRange);

    // Add maps to display
    display.addMap(yMap);
    display.addMap(xMap);
    display.addMap(xRangeMap);
    display.addMap(yRangeMap);

     // "u" variable
    uMap = new ScalarMap(U, Display.Flow1X);
    uRangeMap = new ScalarMap(U, Display.SelectRange);
    display.addMap(uMap);

     // "u" variable
    vMap = new ScalarMap(V, Display.Flow1Y);
    vRangeMap = new ScalarMap(V, Display.SelectRange);
    display.addMap(vMap);

   // if (mColorGrid != null) {
   // 	zRGBMap = new ScalarMap(ZColor, Display.RGB);
   // }
   // else {
   // 	zRGBMap = new ScalarMap(ZColor, Display.Value);
   // }
	//zRGBRangeMap = new ScalarMap(ZColor, Display.SelectRange);
	//display.addMap(zRGBMap);
	//display.addMap(zRGBRangeMap);

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
    mFrame.getContentPane().add(display.getComponent());

    // Set window size and make it visible
    mFrame.setSize(300, 300);
    mFrame.setVisible(true);
  }
}
