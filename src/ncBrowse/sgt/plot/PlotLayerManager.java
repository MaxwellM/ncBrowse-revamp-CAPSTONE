/*
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */
 
package ncBrowse.sgt.plot;

import ncBrowse.sgt.LayerNotFoundException;
/**
 * @since 2.x
 */
public class PlotLayerManager {
    public PlotLayerManager(JPlotPane plotPane) {
        plotPane_ = plotPane;
    }

    /**
     *
     */
    public void addLayer(PlotLayer layer) {
    }

    public void removeLayer(int index) throws LayerNotFoundException {
    }

    public void removeLayer(PlotLayer layer) throws LayerNotFoundException {
    }

    public void update() {
    }

    /** @link dependency */
    /*#LayerStack lnkLayerStack;*/

    /** @link dependency */
    /*#PlotLayer lnkPlotLayer;*/

    /** @link dependency */
    /*#AxisTransform lnkAxisTransform;*/

    /** @link dependency */
    /*#PlotLayerHints lnkPlotLayerHints;*/

    /**
     * @label plotPane 
     */
    private JPlotPane plotPane_;
}
