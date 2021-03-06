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

import java.awt.Container;
import java.awt.Graphics;
/**
 * <code>LayerStack</code> is used to manage a group of layers together.
 * @since 2.x
 */
public class LayerStack extends Container implements LayerControl {
    public LayerStack() {
        setLayout(new StackedLayout());
    }
    public void setPane(AbstractPane pane) {
        /**@todo Implement this sgt.LayerControl method*/
        throw new java.lang.UnsupportedOperationException("Method setPane() not yet implemented.");
    }
    public void draw(Graphics g) {
        /**@todo Implement this sgt.LayerControl method*/
        throw new java.lang.UnsupportedOperationException("Method draw() not yet implemented.");
    }
    public void drawDraggableItems(Graphics g) {
        /**@todo Implement this sgt.LayerControl method*/
        throw new java.lang.UnsupportedOperationException("Method drawDraggableItems() not yet implemented.");
    }
    public String getId() {
        return getName();
    }
}
