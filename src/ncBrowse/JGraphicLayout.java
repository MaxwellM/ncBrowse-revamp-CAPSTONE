//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ncBrowse;

import gov.noaa.pmel.sgt.Attribute;
import gov.noaa.pmel.sgt.AxisTransform;
import gov.noaa.pmel.sgt.CartesianGraph;
import gov.noaa.pmel.sgt.DataNotFoundException;
import gov.noaa.pmel.sgt.Graph;
import gov.noaa.pmel.sgt.GridAttribute;
import gov.noaa.pmel.sgt.JPane;
import gov.noaa.pmel.sgt.Layer;
import gov.noaa.pmel.sgt.LineAttribute;
import gov.noaa.pmel.sgt.LineCartesianRenderer;
import gov.noaa.pmel.sgt.Logo;
import gov.noaa.pmel.sgt.PlainAxis;
import gov.noaa.pmel.sgt.SGLabel;
import gov.noaa.pmel.sgt.TimeAxis;
import gov.noaa.pmel.sgt.dm.Collection;
import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTGrid;
import gov.noaa.pmel.sgt.dm.SGTLine;
import gov.noaa.pmel.sgt.dm.SGTVector;
import gov.noaa.pmel.sgt.swing.prop.LogoDialog;
import gov.noaa.pmel.sgt.swing.prop.SGLabelDialog;
import gov.noaa.pmel.sgt.swing.prop.SpaceAxisDialog;
import gov.noaa.pmel.sgt.swing.prop.TimeAxisDialog;
import gov.noaa.pmel.util.Dimension2D;
import gov.noaa.pmel.util.Domain;
import gov.noaa.pmel.util.GeoDate;
import gov.noaa.pmel.util.Range2D;
import gov.noaa.pmel.util.SoTRange;
import gov.noaa.pmel.util.TimeRange;
import gov.noaa.pmel.util.SoTRange.Time;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class JGraphicLayout extends JPane {
  public static final int X_AXIS = 1;
  public static final int Y_AXIS = 2;
  public static final int Z_AXIS = 3;
  protected static double XSIZE_ = 6.0D;
  protected static double XMIN_ = 0.6D;
  protected static double XMAX_ = 5.4D;
  protected static double YSIZE_ = 4.5D;
  protected static double YMIN_ = 0.75D;
  protected static double YMAX_ = 3.3D;
  protected static double MAIN_TITLE_HEIGHT_ = 0.25D;
  protected static double TITLE_HEIGHT_ = 0.22D;
  protected static double LABEL_HEIGHT_ = 0.18D;
  protected static double WARN_HEIGHT_ = 0.15D;
  protected static double KEY_HEIGHT_ = 0.16D;
  protected static double XKEYSIZE_ = 6.0D;
  protected static double YKEYSIZE_ = 12.0D;
  protected static Color PANE_COLOR;
  protected static Color KEYPANE_COLOR;
  protected int base_units_;
  private JGraphicLayout me_;
  protected JPane keyPane_;
  protected Vector data_;
  protected Hashtable dataAttrMap_;
  protected String ident_;
  protected boolean overlayed_;
  protected boolean clipping_;
  protected Image iconImage_;
  protected SGLabel mainTitle_;
  protected SGLabel title2_;
  protected SGLabel title3_;
  protected JGraphicLayout.SymMouse aSymMouse_;
  protected boolean editClasses_;
  protected SGLabelDialog sg_props_;
  protected SpaceAxisDialog pa_props_;
  protected TimeAxisDialog ta_props_;
  protected LogoDialog lo_props_;
  protected PropertyChangeSupport changes_;
  protected VetoableChangeSupport vetos_;

  public JGraphicLayout() {
    this("", (Image)null, new Dimension(50, 50));
  }

  public JGraphicLayout(String var1, Image var2) {
    this(var1, var2, new Dimension(50, 50));
  }

  public JGraphicLayout(String var1, Image var2, Dimension var3) {
    super(var1, var3);
    this.base_units_ = 0;
    this.dataAttrMap_ = new Hashtable();
    this.clipping_ = false;
    this.iconImage_ = null;
    this.editClasses_ = true;
    this.changes_ = new PropertyChangeSupport(this);
    this.vetos_ = new VetoableChangeSupport(this);
    this.ident_ = var1;
    this.overlayed_ = true;
    this.data_ = new Vector(10);
    this.iconImage_ = var2;
    this.mainTitle_ = null;
    this.title2_ = null;
    this.title3_ = null;
    this.me_ = this;
    if(this.aSymMouse_ == null) {
      this.aSymMouse_ = new JGraphicLayout.SymMouse();
    }

    this.addMouseListener(this.aSymMouse_);
  }

  public void setId(String var1) {
    this.ident_ = var1;
  }

  public String getId() {
    return this.ident_;
  }

  public void setTitles(String var1, String var2, String var3) {
    if(this.mainTitle_ != null) {
      this.mainTitle_.setText(var1);
    }

    if(this.title2_ != null) {
      this.title2_.setText(var2);
    }

    if(this.title3_ != null) {
      this.title3_.setText(var3);
    }

  }

  public SGLabel getMainTitle() {
    return this.mainTitle_;
  }

  public void setBaseUnit(int var1) {
    this.base_units_ = var1;
  }

  public int getBaseUnit() {
    return this.base_units_;
  }

  public void setOverlayed(boolean var1) {
    this.overlayed_ = var1;
  }

  public boolean isOverlayed() {
    return this.overlayed_;
  }

  public Image getIconImage() {
    return this.iconImage_;
  }

  public JPane getKeyPane() {
    return this.keyPane_;
  }

  public boolean isKeyPane() {
    return this.keyPane_ != null;
  }

  public void addData(SGTData var1) {
    this.data_.addElement(var1);
  }

  public void addAttribute(SGTData var1, Attribute var2) {
    this.dataAttrMap_.put(var1, var2);
  }

  public Attribute getAttribute(SGTData var1) throws DataNotFoundException {
    Attribute var2 = (Attribute)this.dataAttrMap_.get(var1);
    if(var2 == null) {
      throw new DataNotFoundException();
    } else {
      return var2;
    }
  }

  public Attribute findAttribute(String var1) {
    Attribute var2 = null;
    Enumeration var3 = this.dataAttrMap_.elements();

    do {
      if(!var3.hasMoreElements()) {
        return var2;
      }

      var2 = (Attribute)var3.nextElement();
    } while(!var2.getId().equals(var1));

    return var2;
  }

  public abstract void addData(SGTData var1, String var2);

  public abstract String getLocationSummary(SGTData var1);

  public Range2D findRange(SGTLine var1, int var2) {
    int var5 = 0;
    double var6 = 0.0D;
    double var8 = 0.0D;
    boolean var11 = false;
    int var3;
    double[] var10;
    switch(var2) {
      case 1:
        var10 = var1.getXArray();
        var3 = var10.length;
        break;
      case 2:
      default:
        var10 = var1.getYArray();
        var3 = var10.length;
    }

    int var4;
    for(var4 = 0; var4 < var3; ++var4) {
      if(!Double.isNaN(var10[var4])) {
        var6 = var10[var4];
        var8 = var10[var4];
        var11 = true;
        var5 = var4 + 1;
        break;
      }
    }

    if(!var11) {
      return new Range2D(0.0D / 0.0, 0.0D / 0.0);
    } else {
      for(var4 = var5; var4 < var3; ++var4) {
        if(!Double.isNaN(var10[var4])) {
          var6 = Math.min(var6, var10[var4]);
          var8 = Math.max(var8, var10[var4]);
        }
      }

      return new Range2D(var6, var8);
    }
  }

  public SoTRange findSoTRange(SGTLine var1, int var2) {
    switch(var2) {
      case 1:
        return var1.getXRange();
      case 2:
        return var1.getYRange();
      default:
        return null;
    }
  }

  public SoTRange findSoTRange(SGTVector var1, int var2) {
    int var6 = 0;
    double var7 = 0.0D;
    double var9 = 0.0D;
    boolean var11 = false;
    switch(var2) {
      case 1:
        return var1.getU().getXRange();
      case 2:
        return var1.getU().getYRange();
      case 3:
      default:
        double[] var12 = var1.getU().getZArray();
        double[] var13 = var1.getV().getZArray();
        double[] var3 = new double[var12.length];

        int var5;
        for(var5 = 0; var5 < var3.length; ++var5) {
          var3[var5] = Math.sqrt(var12[var5] * var12[var5] + var13[var5] * var13[var5]);
        }

        int var4 = var3.length;

        for(var5 = 0; var5 < var4; ++var5) {
          if(!Double.isNaN(var3[var5])) {
            var7 = var3[var5];
            var9 = var3[var5];
            var11 = true;
            var6 = var5 + 1;
            break;
          }
        }

        if(!var11) {
          return new gov.noaa.pmel.util.SoTRange.Double(0.0D / 0.0, 0.0D / 0.0);
        } else {
          for(var5 = var6; var5 < var4; ++var5) {
            if(!Double.isNaN(var3[var5])) {
              var7 = Math.min(var7, var3[var5]);
              var9 = Math.max(var9, var3[var5]);
            }
          }

          return new gov.noaa.pmel.util.SoTRange.Double(var7, var9);
        }
    }
  }

  public SoTRange findSoTRange(SGTGrid var1, GridAttribute var2, int var3) {
    int var7 = 0;
    double var8 = 0.0D;
    double var10 = 0.0D;
    boolean var16 = false;
    int var4;
    int var5;
    int var6;
    if(!var2.isRaster() || (!var1.isXTime() || var3 != 1 || var1.hasXEdges()) && (!var1.isYTime() || var3 != 2 || var1.hasYEdges())) {
      double[] var12;
      double[] var13;
      switch(var3) {
        case 1:
          if(!var2.isRaster()) {
            return var1.getXRange();
          }

          if(var1.hasXEdges()) {
            return var1.getXEdgesRange();
          }

          var13 = var1.getXArray();
          var5 = var13.length;
          var12 = new double[var5 + 1];
          var12[0] = var13[0] - (var13[1] - var13[0]) * 0.5D;

          for(var6 = 1; var6 < var5; ++var6) {
            var12[var6] = (var13[var6 - 1] + var13[var6]) * 0.5D;
          }

          var12[var5] = var13[var5 - 1] + (var13[var5 - 1] - var13[var5 - 2]) * 0.5D;
          var4 = var12.length;
          break;
        case 2:
          if(!var2.isRaster()) {
            return var1.getYRange();
          }

          if(var1.hasYEdges()) {
            return var1.getYEdgesRange();
          }

          var13 = var1.getYArray();
          var5 = var13.length;
          var12 = new double[var5 + 1];
          var12[0] = var13[0] - (var13[1] - var13[0]) * 0.5D;

          for(var6 = 1; var6 < var5; ++var6) {
            var12[var6] = (var13[var6 - 1] + var13[var6]) * 0.5D;
          }

          var12[var5] = var13[var5 - 1] + (var13[var5 - 1] - var13[var5 - 2]) * 0.5D;
          var4 = var12.length;
          break;
        case 3:
        default:
          var12 = var1.getZArray();
          var4 = var12.length;
      }

      for(var6 = 0; var6 < var4; ++var6) {
        if(!Double.isNaN(var12[var6])) {
          var8 = var12[var6];
          var10 = var12[var6];
          var16 = true;
          var7 = var6 + 1;
          break;
        }
      }

      if(!var16) {
        return new gov.noaa.pmel.util.SoTRange.Double(0.0D / 0.0, 0.0D / 0.0);
      } else {
        for(var6 = var7; var6 < var4; ++var6) {
          if(!Double.isNaN(var12[var6])) {
            var8 = Math.min(var8, var12[var6]);
            var10 = Math.max(var10, var12[var6]);
          }
        }

        return new gov.noaa.pmel.util.SoTRange.Double(var8, var10);
      }
    } else {
      GeoDate[] var15 = var1.getTimeArray();
      var5 = var15.length;
      GeoDate[] var14 = new GeoDate[var5 + 1];
      var14[0] = var15[0].subtract(var15[1].subtract(var15[0]).divide(2.0D));

      for(var6 = 1; var6 < var5; ++var6) {
        var14[var6] = var15[var6 - 1].add(var15[var6]).divide(2.0D);
      }

      var14[var5] = var15[var5 - 1].add(var15[var5 - 1].subtract(var15[var5 - 2]).divide(2.0D));
      var4 = var14.length;
      return new Time(var14[0].getTime(), var14[var4 - 1].getTime());
    }
  }

  public Range2D findRange(SGTGrid var1, GridAttribute var2, int var3) {
    int var7 = 0;
    double var8 = 0.0D;
    double var10 = 0.0D;
    boolean var14 = false;
    int var4;
    int var5;
    int var6;
    double[] var12;
    double[] var13;
    switch(var3) {
      case 1:
        if(var2.isRaster()) {
          if(var1.hasXEdges()) {
            var12 = var1.getXEdges();
            var4 = var12.length;
          } else {
            var13 = var1.getXArray();
            var5 = var13.length;
            var12 = new double[var5 + 1];
            var12[0] = var13[0] - (var13[1] - var13[0]) * 0.5D;

            for(var6 = 1; var6 < var5; ++var6) {
              var12[var6] = (var13[var6 - 1] + var13[var6]) * 0.5D;
            }

            var12[var5] = var13[var5 - 1] + (var13[var5 - 1] - var13[var5 - 2]) * 0.5D;
            var4 = var12.length;
          }
        } else {
          var12 = var1.getXArray();
          var4 = var12.length;
        }
        break;
      case 2:
        if(var2.isRaster()) {
          if(var1.hasYEdges()) {
            var12 = var1.getYEdges();
            var4 = var12.length;
          } else {
            var13 = var1.getYArray();
            var5 = var13.length;
            var12 = new double[var5 + 1];
            var12[0] = var13[0] - (var13[1] - var13[0]) * 0.5D;

            for(var6 = 1; var6 < var5; ++var6) {
              var12[var6] = (var13[var6 - 1] + var13[var6]) * 0.5D;
            }

            var12[var5] = var13[var5 - 1] + (var13[var5 - 1] - var13[var5 - 2]) * 0.5D;
            var4 = var12.length;
          }
        } else {
          var12 = var1.getYArray();
          var4 = var12.length;
        }
        break;
      case 3:
      default:
        var12 = var1.getZArray();
        var4 = var12.length;
    }

    for(var6 = 0; var6 < var4; ++var6) {
      if(!Double.isNaN(var12[var6])) {
        var8 = var12[var6];
        var10 = var12[var6];
        var14 = true;
        var7 = var6 + 1;
        break;
      }
    }

    if(!var14) {
      return new Range2D(0.0D / 0.0, 0.0D / 0.0);
    } else {
      for(var6 = var7; var6 < var4; ++var6) {
        if(!Double.isNaN(var12[var6])) {
          var8 = Math.min(var8, var12[var6]);
          var10 = Math.max(var10, var12[var6]);
        }
      }

      return new Range2D(var8, var10);
    }
  }

  public TimeRange findTimeRange(SGTLine var1) {
    long[] var2 = var1.getGeoDateArray().getTime();
    int var3 = var2.length;
    return new TimeRange(var2[0], var2[var3 - 1]);
  }

  public TimeRange findTimeRange(SGTGrid var1, GridAttribute var2) {
    long[] var3;
    if(!var2.isRaster() || !var1.isXTime() && !var1.isYTime()) {
      var3 = var1.getGeoDateArray().getTime();
    } else if(!var1.hasXEdges() && !var1.hasYEdges()) {
      long[] var4 = var1.getGeoDateArray().getTime();
      int var6 = var4.length;
      var3 = new long[var6 + 1];
      var3[0] = var4[0] - (var4[1] - var4[0]) / 2L;

      for(int var7 = 1; var7 < var6; ++var7) {
        var3[var7] = (var4[var7 - 1] + var4[var7]) / 2L;
      }

      var3[var6] = var4[var6 - 1] + (var4[var6 - 1] - var4[var6 - 2]) / 2L;
    } else {
      var3 = var1.getGeoDateArrayEdges().getTime();
    }

    int var5 = var3.length;
    return new TimeRange(var3[0], var3[var5 - 1]);
  }

  public void setClipping(boolean var1) {
    this.clipping_ = var1;
  }

  public boolean isClipping() {
    return this.clipping_;
  }

  public abstract void resetZoom();

  public abstract void setRange(Domain var1) throws PropertyVetoException;

  public Domain getRange() {
    Domain var1 = new Domain();
    Layer var2 = this.getFirstLayer();
    Graph var3 = var2.getGraph();
    if(var3 instanceof CartesianGraph) {
      CartesianGraph var4 = (CartesianGraph)var3;
      AxisTransform var5 = var4.getXTransform();
      AxisTransform var6 = var4.getYTransform();
      if(var5.isTime()) {
        var1.setXRange(var5.getTimeRangeU());
      } else {
        var1.setXRange(var5.getRangeU());
      }

      if(var6.isTime()) {
        var1.setYRange(var6.getTimeRangeU());
      } else {
        var1.setYRange(var6.getRangeU());
      }
    }

    return var1;
  }

  public Domain getZoomBoundsU() {
    Domain var1 = new Domain();
    Range2D var2 = new Range2D();
    Range2D var3 = new Range2D();
    Rectangle var8 = this.getZoomBounds();
    Layer var9 = this.getFirstLayer();
    Graph var10 = var9.getGraph();
    if(var10 instanceof CartesianGraph) {
      CartesianGraph var11 = (CartesianGraph)var10;
      AxisTransform var12 = var11.getXTransform();
      AxisTransform var13 = var11.getYTransform();
      long var4;
      long var6;
      double var14;
      long var16;
      if(var12.isSpace()) {
        var2.start = var11.getXPtoU(var9.getXDtoP(var8.x));
        var2.end = var11.getXPtoU(var9.getXDtoP(var8.x + var8.width));
        if(var2.start > var2.end) {
          var14 = var2.start;
          var2.start = var2.end;
          var2.end = var14;
        }

        var1.setXRange(var2);
      } else {
        var4 = var11.getXPtoLongTime(var9.getXDtoP(var8.x));
        var6 = var11.getXPtoLongTime(var9.getXDtoP(var8.x + var8.width));
        if(var4 > var6) {
          var16 = var4;
          var4 = var6;
          var6 = var16;
        }

        var1.setXRange(new TimeRange(new GeoDate(var4), new GeoDate(var6)));
      }

      if(var13.isSpace()) {
        var3.start = var11.getYPtoU(var9.getYDtoP(var8.y));
        var3.end = var11.getYPtoU(var9.getYDtoP(var8.y + var8.height));
        if(var3.start > var3.end) {
          var14 = var3.start;
          var3.start = var3.end;
          var3.end = var14;
        }

        var1.setYRange(var3);
      } else {
        var4 = var11.getYPtoLongTime(var9.getYDtoP(var8.y));
        var6 = var11.getYPtoLongTime(var9.getYDtoP(var8.y + var8.height));
        if(var4 > var6) {
          var16 = var4;
          var4 = var6;
          var6 = var16;
        }

        var1.setYRange(new TimeRange(new GeoDate(var4), new GeoDate(var6)));
      }
    }

    return var1;
  }

  protected String getLatString(double var1) {
    if(var1 == 0.0D) {
      return "Eq";
    } else {
      DecimalFormat var3 = new DecimalFormat("##.##N;##.##S");
      return var3.format(var1);
    }
  }

  protected String getLonString(double var1) {
    DecimalFormat var3 = new DecimalFormat("###.##W;###.##");
    double var4 = (var1 + 360.0D) % 360.0D;
    var3.setNegativeSuffix("E");
    if(var4 > 180.0D) {
      var4 = 360.0D - var4;
    }

    return var3.format(var4);
  }

  public Collection getData() {
    Collection var1 = new Collection(this.ident_ + " Data Collection", this.data_.size());
    Enumeration var2 = this.data_.elements();

    while(var2.hasMoreElements()) {
      var1.addElement(var2.nextElement());
    }

    return var1;
  }

  void Layout_MouseRelease(MouseEvent var1) {
    if((var1.getModifiers() & 16) != 0) {
      Rectangle var2 = this.getZoomBounds();
      if(var2.width > 1 && var2.height > 1) {
        Domain var3 = this.getZoomBoundsU();
        this.setClipping(true);
        if(this.getFirstLayer().getGraph() instanceof CartesianGraph) {
          try {
            this.setRange(var3);
          } catch (PropertyVetoException var5) {
            System.out.println("Zoom denied! " + var5);
          }
        }

      }
    }
  }

  void Layout_MousePress(MouseEvent var1) {
    if(var1.isControlDown()) {
      this.resetZoom();
      this.setClipping(false);
    }

  }

  void Layout_MouseClicked(MouseEvent var1) {
    if(!var1.isControlDown()) {
      Object var2 = this.getSelectedObject();
      if(var2 instanceof LineCartesianRenderer) {
        LineCartesianRenderer var3 = (LineCartesianRenderer)var2;
        LineAttribute var4 = var3.getLineAttribute();
        if(var4.getStyle() == 0) {
          var4.setStyle(3);
        } else if(var4.getStyle() == 3) {
          var4.setStyle(0);
        }
      }

      if((var1.getModifiers() & 4) != 0 && this.editClasses_) {
        this.showProperties(var2);
      }

    }
  }

  void KeyPane_MousePress(MouseEvent var1) {
  }

  void KeyPane_MouseClicked(MouseEvent var1) {
    if(this.keyPane_ != null) {
      Object var2 = this.keyPane_.getSelectedObject();
      if(var2 instanceof LineCartesianRenderer) {
        LineCartesianRenderer var3 = (LineCartesianRenderer)var2;
        LineAttribute var4 = var3.getLineAttribute();
        if(var4.getStyle() == 0) {
          var4.setStyle(3);
        } else if(var4.getStyle() == 3) {
          var4.setStyle(0);
        }
      }

      if(this.editClasses_) {
        this.showProperties(var2);
      }

    }
  }

  public void setEditClasses(boolean var1) {
    this.editClasses_ = var1;
  }

  public boolean isEditClasses() {
    return this.editClasses_;
  }

  void showProperties(Object var1) {
    if(var1 instanceof SGLabel) {
      if(this.sg_props_ == (SGLabelDialog)null) {
        this.sg_props_ = new SGLabelDialog();
      }

      this.sg_props_.setSGLabel((SGLabel)var1, this);
      if(!this.sg_props_.isShowing()) {
        this.sg_props_.setVisible(true);
      }
    } else if(var1 instanceof PlainAxis) {
      if(this.pa_props_ == (SpaceAxisDialog)null) {
        this.pa_props_ = new SpaceAxisDialog();
      }

      this.pa_props_.setSpaceAxis((PlainAxis)var1, this);
      if(!this.pa_props_.isShowing()) {
        this.pa_props_.setVisible(true);
      }
    } else if(var1 instanceof TimeAxis) {
      if(this.ta_props_ == (TimeAxisDialog)null) {
        this.ta_props_ = new TimeAxisDialog();
      }

      this.ta_props_.setTimeAxis((TimeAxis)var1, this);
      if(!this.ta_props_.isShowing()) {
        this.ta_props_.setVisible(true);
      }
    } else if(var1 instanceof Logo) {
      if(this.lo_props_ == (LogoDialog)null) {
        this.lo_props_ = new LogoDialog();
      }

      this.lo_props_.setLogo((Logo)var1, this);
      if(!this.lo_props_.isShowing()) {
        this.lo_props_.setVisible(true);
      }
    }

  }

  protected void setAllClip(SoTRange var1, SoTRange var2) {
    if(var2.isTime()) {
      this.setAllClip(var2.getStart().getLongTime(), var2.getEnd().getLongTime(), ((gov.noaa.pmel.util.SoTRange.Double)var1).start, ((gov.noaa.pmel.util.SoTRange.Double)var1).end);
    } else if(var1.isTime()) {
      this.setAllClip(var1.getStart().getLongTime(), var1.getEnd().getLongTime(), ((gov.noaa.pmel.util.SoTRange.Double)var2).start, ((gov.noaa.pmel.util.SoTRange.Double)var2).end);
    } else {
      this.setAllClip(((gov.noaa.pmel.util.SoTRange.Double)var1).start, ((gov.noaa.pmel.util.SoTRange.Double)var1).end, ((gov.noaa.pmel.util.SoTRange.Double)var2).start, ((gov.noaa.pmel.util.SoTRange.Double)var2).end);
    }

  }

  protected void setAllClip(double var1, double var3, double var5, double var7) {
    Component[] var10 = this.getComponents();

    for(int var11 = 0; var11 < var10.length; ++var11) {
      if(var10[var11] instanceof Layer) {
        Layer var9 = (Layer)var10[var11];
        ((CartesianGraph)var9.getGraph()).setClip(var1, var3, var5, var7);
      }
    }

  }

  protected void setAllClip(GeoDate var1, GeoDate var2, double var3, double var5) {
    this.setAllClip(var1.getTime(), var2.getTime(), var3, var5);
  }

  protected void setAllClip(long var1, long var3, double var5, double var7) {
    Component[] var10 = this.getComponents();

    for(int var11 = 0; var11 < var10.length; ++var11) {
      Layer var9 = (Layer)var10[var11];
      ((CartesianGraph)var9.getGraph()).setClip(var1, var3, var5, var7);
    }

  }

  protected void setAllClipping(boolean var1) {
    Component[] var3 = this.getComponents();

    for(int var4 = 0; var4 < var3.length; ++var4) {
      if(var3[var4] instanceof Layer) {
        Layer var2 = (Layer)var3[var4];
        ((CartesianGraph)var2.getGraph()).setClipping(var1);
      }
    }

  }

  public abstract gov.noaa.pmel.util.Rectangle2D.Double getKeyBoundsP();

  public abstract void setKeyBoundsP(gov.noaa.pmel.util.Rectangle2D.Double var1);

  public Dimension2D getKeyLayerSizeP() {
    return this.keyPane_ != null?this.keyPane_.getFirstLayer().getSizeP():null;
  }

  public void setKeyLayerSizeP(Dimension2D var1) {
    if(this.keyPane_ != null) {
      this.keyPane_.getFirstLayer().setSizeP(var1);
    }

  }

  public void addVetoableChangeListener(VetoableChangeListener var1) {
    this.vetos_.addVetoableChangeListener(var1);
  }

  public void removeVetoableChangeListener(VetoableChangeListener var1) {
    this.vetos_.removeVetoableChangeListener(var1);
  }

  public void addPropertyChangeListener(PropertyChangeListener var1) {
    this.changes_.addPropertyChangeListener(var1);
  }

  public void removePropertyChangeListener(PropertyChangeListener var1) {
    this.changes_.removePropertyChangeListener(var1);
  }

  static {
    PANE_COLOR = Color.white;
    KEYPANE_COLOR = Color.white;
  }

  class SymMouse extends MouseAdapter {
    SymMouse() {
    }

    public void mousePressed(MouseEvent var1) {
      if(JGraphicLayout.this.isMouseEventsEnabled()) {
        Object var2 = var1.getSource();
        if(var2 == JGraphicLayout.this.me_) {
          JGraphicLayout.this.Layout_MousePress(var1);
        } else if(var2 == JGraphicLayout.this.keyPane_) {
          JGraphicLayout.this.KeyPane_MousePress(var1);
        }

      }
    }

    public void mouseClicked(MouseEvent var1) {
      if(JGraphicLayout.this.isMouseEventsEnabled()) {
        Object var2 = var1.getSource();
        if(var2 == JGraphicLayout.this.me_) {
          JGraphicLayout.this.Layout_MouseClicked(var1);
        } else if(var2 == JGraphicLayout.this.keyPane_) {
          JGraphicLayout.this.KeyPane_MouseClicked(var1);
        }

      }
    }

    public void mouseReleased(MouseEvent var1) {
      if(JGraphicLayout.this.isMouseEventsEnabled()) {
        Object var2 = var1.getSource();
        if(var2 == JGraphicLayout.this.me_) {
          JGraphicLayout.this.Layout_MouseRelease(var1);
        }

      }
    }
  }
}
