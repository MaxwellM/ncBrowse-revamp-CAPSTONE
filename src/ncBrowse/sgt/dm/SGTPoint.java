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

/**
 * Defines a data object to be of Point type. Interpretation
 * of X and Y is determined by the <code>CoordinateSystem</code>.  For
 * <code>Cartesian</code>, X and Y are the Cartesian coordinates. For
 * <code>Polar</code>,
 * X and Y are R (radius) and Theta (angle), respectively.
 *
 * The <code>SGTPoint</code> interface only defines data access, not how
 * the data will be constructed or set.
 *
 * @author Donald Denbo
 * @version $Revision: 1.7 $, $Date: 2003/08/22 23:02:38 $
 * @since 1.0
 * @see SGTData
 * @see CoordinateSystem
 * @see Cartesian
 * @see Polar
 * @see SimplePoint
 */
public interface SGTPoint extends SGTData {
    /**
     * Get the x coordinate.
     */
    double getX();
    /**
     * Get the y coordinate.
     */
    double getY();
    /**
     * Test if a value is associated with the SGTPoint.
     */
    boolean hasValue();
    /**
     * Get the associated value.
     */
    double getValue();
    /**
     * Get the SGTMetaData object associated with the value.
     */
    SGTMetaData getValueMetaData();
    /**
     * Get the Time value.
     */
    GeoDate getTime();
    /**
     * Get the time as <code>long</code> referenced from
     * 1970-01-01.
     *
     * @since 3.0
     */
    long getLongTime();
}
