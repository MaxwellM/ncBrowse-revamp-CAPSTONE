/*
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package ncBrowse.sgt.dm;

import ncBrowse.sgt.geom.GeoDate;
import ncBrowse.sgt.geom.GeoDateArray;
import ncBrowse.sgt.geom.Range2D;
import ncBrowse.sgt.geom.SoTRange;

/**
 * Defines a data object to be of Grid type. Interpretation
 * of X and Y is determined by the <code>CoordinateSystem</code>.  For
 * <code>Cartesian</code>, X and Y are the Cartesian coordinates. For
 * <code>Polar</code>, X and Y are R (radius) and Theta (angle), respectively.
 *
 * The <code>SGTGrid</code> interface only specifies the methods required
 * to access information. The methods used to construct an
 * object that implements <code>SGTGrid</code> is left to the developer.
 *
 * @author Donald Denbo
 * @version $Revision: 1.10 $, $Date: 2003/08/22 23:02:38 $
 * @since 1.0
 * @see SGTData
 * @see CoordinateSystem
 * @see Cartesian
 * @see Polar
 * @see SimpleGrid
 */
public interface SGTGrid extends SGTData {
    /**
     * Get the array of X values.
     */
    double[] getXArray();
    /**
     * Get the length of X value array.
     */
    int getXSize();
    /**
     * Get the array of Y values.
     */
    double[] getYArray();
    /**
     * Get the length of Y value array.
     */
    int getYSize();
    /**
     * Get the array of Z values.
     */
    double[] getZArray();
    /**
     * Get the range of Z values.
     */
    Range2D getZRange();
    /**
     * Get the array of temporal values.
     */
    GeoDate[] getTimeArray();
    /**
     * Get the <code>GeoDateArray</code> object.
     *
     * @since 3.0
     */
    GeoDateArray getGeoDateArray();
    /**
     * Get the length of temporal value array.
     */
    int getTSize();
    /**
     * Get the Z SGTMetaData.
     */
    SGTMetaData getZMetaData();
    /**
     * Get the associated data. The associated data must
     * be of the same type (SGTGrid) and shape.
     */
    SGTGrid getAssociatedData();
    /**
     * Is there associated data available?
     */
    boolean hasAssociatedData();
    /**
     * Are X edges available?
     */
    boolean hasXEdges();
    /**
     * Get the X coordinate edges. The XEdge length will
     * be one greater than the XArray length.
     */
    double[] getXEdges();
    /**
     * Get the range of X coordinate edges.
     */
    SoTRange getXEdgesRange();
    /**
     * Are Y edges available?
     */
    boolean hasYEdges();
    /**
     * Get the Y coordinate edges. The YEdge length will
     * be one greater than the YArray length.
     */
    double[] getYEdges();
    /**
     * Get the range of Y coordinate edges.
     */
    SoTRange getYEdgesRange();
    /**
     * Get the Time edges. The TimeEdge length will
     * be one greater than the TimeArray length.
     */
    GeoDate[] getTimeEdges();
    /**
     * Get the <code>GeoDateArray</code> object.
     *
     * @since 3.0
     */
    GeoDateArray getGeoDateArrayEdges();
}
