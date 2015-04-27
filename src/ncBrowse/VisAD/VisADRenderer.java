package ncBrowse.VisAD;

import visad.VisADException;

import javax.swing.*;
import java.rmi.RemoteException;


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

public interface VisADRenderer {
	void draw() throws VisADException, RemoteException;
	JFrame getDisplayWindow();
	boolean canDisplayLegend();
	
    /**
     * Change point of view of a 3D VisAD display,
     * using user's input angles (unit = degree):
     * For example, a view from the
     * southwest has azimuth of 225 and decAngle say 20 to 70 or so.
     * Preserves initial scaling and aspect ratios.
     *
     * @param azimuth  azimuth from "north," clockwise, 0 to 360
     * @param decAngle tilt angle down from upward vertical. 0-180
     *
     */
}
