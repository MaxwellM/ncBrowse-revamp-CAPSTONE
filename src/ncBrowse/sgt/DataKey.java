/*
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package ncBrowse.sgt;

import ncBrowse.sgt.geom.Point2D;
import ncBrowse.sgt.geom.Rectangle2D;

/**
 * Inticates the class is a key or legend.
 *
 * @author Donald Denbo
 * @version $Revision: 1.2 $, $Date: 2003/08/22 23:02:31 $
 * @since 3.0
 * @stereotype container
 **/
public interface DataKey extends LayerChild {
    void setLocationP(Point2D.Double locP);
    void addGraph(CartesianRenderer rend, SGLabel label)
        throws IllegalArgumentException;
    void setAlign(int vert, int horz);
    void setHAlign(int horz);
    void setVAlign(int vert);
    void setBorderStyle(int style);
    void setBoundsP(Rectangle2D.Double r);
    void setColumns(int col);
    void setLineLengthP(double len);
}