/**
 *  $Id: VisADPlotSpecification.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.VisAD;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.*;
import visad.*;
import visad.java2d.DisplayImplJ2D;
import java.rmi.RemoteException;
import gov.noaa.pmel.sgt.dm.*;
import gov.noaa.pmel.util.SoTRange;
import gov.noaa.pmel.util.GeoDate;
import ncBrowse.map.*;
import gov.noaa.pmel.sgt.GridAttribute;
import gov.noaa.pmel.util.Range2D;
import visad.util.*;
import ncBrowse.*;
import java.awt.*;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author John Osborne
 * @version $Revision: 1.2 $, $Date: 2003/10/01 22:32:28 $
 */

public class VisADPlotSpecification {
	public static final int NO_LEGEND = 0;
	public static final int COLOR_LEGEND = 1;
	public static final int NEAREST_NEIGHBOR = Data.NEAREST_NEIGHBOR;
	public static final int WEIGHTED_AVERAGE = Data.WEIGHTED_AVERAGE;
	// this is an integer to accomodate future legend options other than presence/absence of legend
	private int mLegendStyle = NO_LEGEND;
	private boolean mResampleAxes = false;
	private float mVectorScale = 0.25f;
	private boolean mUseU = true;
	private boolean mUseV = true;
	private boolean mUseW = true;
	private int mSamplingMode = WEIGHTED_AVERAGE;
	private boolean mRespectDataAspectRatio;
	private float mXAxisScaling = 0.0f;
	private float mYAxisScaling = 0.0f;
	private float mZAxisScaling = 0.0f;
	private Color mPlotBGColor = Color.black;
	private Color mAxesColor = Color.white;
	
	public VisADPlotSpecification() {
	
	}
	
	public float getXAxisScaling() {
		return mXAxisScaling;
	}
	
	public void setXAxisScaling(float val) {
		mXAxisScaling = val;
	}
	
	public float getYAxisScaling() {
		return mYAxisScaling;
	}
	
	public void setYAxisScaling(float val) {
		mYAxisScaling = val;
	}
	
	public float getZAxisScaling() {
		return mZAxisScaling;
	}
	
	public void setZAxisScaling(float val) {
		mZAxisScaling = val;
	}
	
	public boolean isRespectDataAspectRatio() {
		return mRespectDataAspectRatio;
	}
	
	public void setRespectDataAspectRatio(boolean flag) {
		mRespectDataAspectRatio = flag;
	}
	
	public void setLegendStyle(int style) {
		mLegendStyle = style;
	}
	
	public int getLegendStyle() {
		return mLegendStyle;
	}
	
	public boolean isResampleAxes() {
		return mResampleAxes;
	}
	
	public void setResampleAxes(boolean flag) {
		mResampleAxes = flag;
	}
	
	public float getVectorScale() {
		return mVectorScale;
	}
	
	public void setVectorScale(float val) {
		mVectorScale = val;
	}
	
	public boolean isUseU() {
		return mUseU;
	}
	
	public void setUseU(boolean flag) {
		mUseU = flag;
	}
	
	public boolean isUseV() {
		return mUseV;
	}
	
	public void setUseV(boolean flag) {
		mUseV = flag;
	}
	
	public boolean isUseW() {
		return mUseW;
	}
	
	public void setUseW(boolean flag) {
		mUseW = flag;
	}
	
	public void setSamplingMode(int mode) {
		mSamplingMode = mode;
	}
	
	public int getSamplingMode() {
		return mSamplingMode;
	}
	
	public Color getBGColor() {
		return mPlotBGColor;
	}
	
	public Color getAxesColor() {
		return mAxesColor;
	}
	
	public void setBGColor(Color inColor) {
		mPlotBGColor = new Color(inColor.getRed(), inColor.getGreen(), inColor.getBlue());
	}
	
	public void setAxesColor(Color inColor) {
		mAxesColor = new Color(inColor.getRed(), inColor.getGreen(), inColor.getBlue());
	}

}
