//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

//package gov.noaa.pmel.sgt.swing;
package ncBrowse;

import gov.noaa.pmel.sgt.*;
import gov.noaa.pmel.sgt.ColorMap;
import gov.noaa.pmel.sgt.IndexedColorMap;
import gov.noaa.pmel.sgt.LinearTransform;
import gov.noaa.pmel.sgt.dm.*;
//import gov.noaa.pmel.sgt.swing.JGraphicLayout;
import gov.noaa.pmel.util.*;
import gov.noaa.pmel.util.Rectangle2D.Double;
import gov.noaa.pmel.util.SoTRange.Time;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.Objects;

public class JPlotLayout extends JGraphicLayout implements PropertyChangeListener {
  CartesianGraph coastLine_;
  Layer coastLayer_;
  Logo logo_;
  LineKey lineKey_;
  ColorKey colorKey_;
  PointCollectionKey pointKey_;
  VectorKey vectorKey_;
  private boolean computeScroll_;
  int layerCount_;
  boolean revXAxis_;
  boolean revYAxis_;
  Layer firstLayer_;
  boolean inZoom_;
  GridAttribute gAttr_;
  private boolean isXTime_;
  private boolean isYTime_;
  double xSize_;
  double xMin_;
  double xMax_;
  double ySize_;
  double yMin_;
  double yMax_;
  double mainTitleHeight_;
  double titleHeight_;
  double labelHeight_;
  double warnHeight_;
  double keyHeight_;
  double xKeySize_;
  double yKeySize_;
  Color paneColor_;
  Color keyPaneColor_;
  boolean autoRangeX_;
  boolean autoRangeY_;
  int autoXIntervals_;
  int autoYIntervals_;
  public static final int POINTS = 0;
  public static final int LINE = 1;
  public static final int GRID = 2;
  public static final int VECTOR = 3;
  private int plotType_;
  private static final Color[] colorList_;
  private static final Color[] colorList_2;
  private static final int[] markList_;
  private static String LEFT_AXIS;
  private static String BOTTOM_AXIS;

  public JPlotLayout(SGTData var1) {
    this(var1, "", null, false);
  }

  public JPlotLayout(SGTData var1, String var2, Image var3, boolean var4) {
    this(var1 instanceof SGTGrid?2:(var1 instanceof PointCollection?0:(var1 instanceof SGTVector?3:1)), var1.isXTime(), var1.isYTime(), var2, var3, var4);
  }

  public JPlotLayout(boolean var1, boolean var2, boolean var3, String var4, Image var5, boolean var6) {
    this(var1?2:1, var2, var3, var4, var5, var6);
  }

  public JPlotLayout(boolean var1, boolean var2, boolean var3, boolean var4, String var5, Image var6, boolean var7) {
    this(var1?2:(var2?0:1), var3, var4, var5, var6, var7);
  }

  public JPlotLayout(int var1, boolean var2, boolean var3, String var4, Image var5, boolean var6) {
    super(var4, var5, new Dimension(VariableWindows.getWinSizeWidth(), VariableWindows.getWinSizeHeight()));
    this.coastLine_ = null;
    this.coastLayer_ = null;
    this.computeScroll_ = false;
    this.revXAxis_ = false;
    this.revYAxis_ = false;
    this.inZoom_ = false;
    this.gAttr_ = null;
    this.isXTime_ = false;
    this.isYTime_ = false;
    this.xSize_ = XSIZE_;
    this.xMin_ = XMIN_;
    this.xMax_ = XMAX_;
    this.ySize_ = YSIZE_;
    this.yMin_ = YMIN_;
    this.yMax_ = YSIZE_ - 1.0D * MAIN_TITLE_HEIGHT_ - 2.0D * WARN_HEIGHT_ - 0.5D * WARN_HEIGHT_;
    this.mainTitleHeight_ = MAIN_TITLE_HEIGHT_;
    this.titleHeight_ = TITLE_HEIGHT_;
    this.labelHeight_ = LABEL_HEIGHT_;
    this.warnHeight_ = WARN_HEIGHT_;
    this.keyHeight_ = KEY_HEIGHT_;
    this.xKeySize_ = XKEYSIZE_;
    this.yKeySize_ = YKEYSIZE_;
    this.paneColor_ = PANE_COLOR;
    this.keyPaneColor_ = KEYPANE_COLOR;
    this.autoRangeX_ = false;
    this.autoRangeY_ = false;
    this.autoXIntervals_ = 10;
    this.autoYIntervals_ = 10;
    this.plotType_ = -1;
    PlainAxis var12 = null;
    PlainAxis var13 = null;
    TimeAxis var14 = null;
    TimeAxis var15 = null;
    this.isXTime_ = var2;
    this.isYTime_ = var3;
    this.plotType_ = var1;
    this.setOpaque(true);
    this.setLayout(new StackedLayout());
    this.setBackground(this.paneColor_);
    Layer var7 = new Layer("Layer 1", new Dimension2D(this.xSize_, this.ySize_));
    this.firstLayer_ = var7;
    if(this.coastLine_ == null) {
      this.add(var7, 0);
    } else {
      this.add(var7, 1);
    }

    Layer var8;
    short var22;
    int var23;
    if(this.plotType_ == 2) {
      this.colorKey_ = new ColorKey(new Double(0.01D, 0.01D, this.xKeySize_ - 0.01D, 1.0D), 2, 0);
      this.colorKey_.setId("Color Key");
      this.colorKey_.setVisible(false);
      if(var6) {
        this.colorKey_.setVAlign(0);
        this.colorKey_.setBorderStyle(2);
        this.colorKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(0.0D, this.yKeySize_));
        var22 = 500;
        var23 = (int)((double)var22 / this.xKeySize_ * this.yKeySize_);
        this.keyPane_ = new JPane("KeyPane", new Dimension(var22, var23));
        this.keyPane_.setOpaque(true);
        this.keyPane_.setLayout(new StackedLayout());
        this.keyPane_.setBackground(this.keyPaneColor_);
        var8 = new Layer("Key Layer", new Dimension2D(this.xKeySize_, this.yKeySize_));
        this.keyPane_.add(var8);
        var8.addChild(this.colorKey_);
      } else {
        this.colorKey_.setHAlign(1);
        this.colorKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(this.xSize_ * 0.5D, 0.01D));
        var7.addChild(this.colorKey_);
      }
    } else if(this.plotType_ == 0) {
      this.pointKey_ = new PointCollectionKey();
      this.pointKey_.setSelectable(false);
      this.pointKey_.setId("Point Key");
      this.pointKey_.setVAlign(0);
      if(var6) {
        this.pointKey_.setHAlign(0);
        this.pointKey_.setBorderStyle(2);
        this.pointKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(0.0D, this.yKeySize_));
        var22 = 500;
        var23 = (int)((double)var22 / this.xKeySize_ * this.yKeySize_);
        this.keyPane_ = new JPane("KeyPane", new Dimension(var22, var23));
        this.keyPane_.setOpaque(true);
        this.keyPane_.setLayout(new StackedLayout());
        this.keyPane_.setBackground(this.keyPaneColor_);
        var8 = new Layer("Key Layer", new Dimension2D(this.xKeySize_, this.yKeySize_));
        this.keyPane_.add(var8);
        var8.addChild(this.pointKey_);
      } else {
        this.pointKey_.setVAlign(0);
        this.pointKey_.setHAlign(2);
        this.pointKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(this.xSize_ - 0.02D, this.ySize_ - 0.1D));
        var7.addChild(this.pointKey_);
      }
    } else if(this.plotType_ == 1) {
      this.lineKey_ = new LineKey();
      this.lineKey_.setSelectable(false);
      this.lineKey_.setId("Line Key");
      this.lineKey_.setVAlign(0);
      if(var6) {
        this.lineKey_.setHAlign(0);
        this.lineKey_.setBorderStyle(2);
        this.lineKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(0.0D, this.yKeySize_));
        var22 = 500;
        var23 = (int)((double)var22 / this.xKeySize_ * this.yKeySize_);
        this.keyPane_ = new JPane("KeyPane", new Dimension(var22, var23));
        this.keyPane_.setOpaque(true);
        this.keyPane_.setLayout(new StackedLayout());
        this.keyPane_.setBackground(this.keyPaneColor_);
        var8 = new Layer("Key Layer", new Dimension2D(this.xKeySize_, this.yKeySize_));
        this.keyPane_.add(var8);
        var8.addChild(this.lineKey_);
      } else {
        this.lineKey_.setVAlign(0);
        this.lineKey_.setHAlign(2);
        this.lineKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(this.xSize_ - 0.02D, this.ySize_ - 0.1D));
        var7.addChild(this.lineKey_);
      }
    } else if(this.plotType_ == 3) {
      this.vectorKey_ = new VectorKey();
      this.vectorKey_.setSelectable(false);
      this.vectorKey_.setId("Vector Key");
      this.vectorKey_.setVAlign(0);
      if(var6) {
        this.vectorKey_.setHAlign(0);
        this.vectorKey_.setBorderStyle(2);
        this.vectorKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(0.0D, this.yKeySize_));
        var22 = 500;
        var23 = (int)((double)var22 / this.xKeySize_ * this.yKeySize_);
        this.keyPane_ = new JPane("KeyPane", new Dimension(var22, var23));
        this.keyPane_.setOpaque(true);
        this.keyPane_.setLayout(new StackedLayout());
        this.keyPane_.setBackground(this.keyPaneColor_);
        var8 = new Layer("Key Layer", new Dimension2D(this.xKeySize_, this.yKeySize_));
        this.keyPane_.add(var8);
        var8.addChild(this.vectorKey_);
      } else {
        this.vectorKey_.setVAlign(0);
        this.vectorKey_.setHAlign(2);
        this.vectorKey_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(this.xSize_ - 0.02D, this.ySize_ - 0.1D));
        this.vectorKey_.setSelectable(true);
        var7.addChild(this.vectorKey_);
      }
    }

    double var16;
    byte var20;
    if(this.iconImage_ != null) {
      this.logo_ = new Logo(new gov.noaa.pmel.util.Point2D.Double(0.0D, this.ySize_), 0, 0);
      this.logo_.setImage(this.iconImage_);
      var7.addChild(this.logo_);
      Rectangle var31 = this.logo_.getBounds();
      var16 = var7.getXDtoP(var31.x + var31.width) + 0.05D;
      var20 = 0;
    } else {
      var16 = (this.xMin_ + this.xMax_) * 0.5D;
      var20 = 1;
    }

    double var18 = this.ySize_ - 1.0D * this.mainTitleHeight_;
    Font var32 = new Font("Helvetica", Font.BOLD, 14);
    this.mainTitle_ = new SGLabel("Line Profile Title", "Profile Plot", this.mainTitleHeight_, new gov.noaa.pmel.util.Point2D.Double(var16, var18), 2, var20);
    this.mainTitle_.setFont(var32);
    //var7.addChild(this.mainTitle_);
    var18 -= 1.0D * this.warnHeight_;
    Font var33 = new Font("Helvetica", Font.PLAIN, 10);
    this.title2_ = new SGLabel("Second Title", "Warning: Browse image only", this.warnHeight_, new gov.noaa.pmel.util.Point2D.Double(var16, var18), 2, var20);
    this.title2_.setFont(var33);
    //var7.addChild(this.title2_);
    var18 -= 1.0D * this.warnHeight_;
    this.title3_ = new SGLabel("Warning 2", "Verify accuracy of plot before research use", this.warnHeight_, new gov.noaa.pmel.util.Point2D.Double(var16, var18), 2, var20);
    this.title3_.setFont(var33);
    //var7.addChild(this.title3_);
    this.layerCount_ = 0;
    CartesianGraph var9 = new CartesianGraph("Profile Graph 1");
    GeoDate var24 = null;
    GeoDate var25 = null;

    try {
      var24 = new GeoDate("1992-11-01", "yyyy-MM-dd");
      var25 = new GeoDate("1993-02-20", "yyyy-MM-dd");
    } catch (IllegalTimeValue ignored) {
    }

    Object var26;
    if(this.isXTime_) {
      var26 = new Time(var24.getTime(), var25.getTime());
    } else {
      var26 = new gov.noaa.pmel.util.SoTRange.Double(10.0D, 20.0D, 2.0D);
    }

    Object var27;
    if(this.isYTime_) {
      var27 = new Time(var24.getTime(), var25.getTime());
    } else {
      var27 = new gov.noaa.pmel.util.SoTRange.Double(400.0D, 0.0D, -50.0D);
    }

    LinearTransform var10 = new LinearTransform(new Range2D(this.xMin_, this.xMax_), (SoTRange)var26);
    LinearTransform var11 = new LinearTransform(new Range2D(this.yMin_, this.yMax_), (SoTRange)var27);
    SoTPoint var28 = new SoTPoint(((SoTRange)var26).getStart(), ((SoTRange)var27).getStart());
    var9.setXTransform(var10);
    var9.setYTransform(var11);
    Font var29 = new Font("Helvetica", Font.ITALIC, 14);
    if(this.isXTime_) {
      var14 = new TimeAxis(BOTTOM_AXIS, 0);
      var14.setRangeU((SoTRange)var26);
      var14.setLabelHeightP(this.labelHeight_);
      var14.setLocationU(var28);
      var14.setLabelFont(var29);
      var9.addXAxis(var14);
    } else {
      var12 = new PlainAxis(BOTTOM_AXIS);
      var12.setRangeU((SoTRange)var26);
      var12.setNumberSmallTics(0);
      var12.setLocationU(var28);
      var12.setLabelHeightP(this.labelHeight_);
      var12.setLabelFont(var29);
      var9.addXAxis(var12);
    }

    if(this.isYTime_) {
      var15 = new TimeAxis(LEFT_AXIS, 0);
      var15.setRangeU((SoTRange)var27);
      var15.setLabelHeightP(this.labelHeight_);
      var15.setLocationU(var28);
      var15.setLabelFont(var29);
      var9.addYAxis(var15);
    } else {
      var13 = new PlainAxis(LEFT_AXIS);
      var13.setRangeU((SoTRange)var27);
      var13.setNumberSmallTics(0);
      var13.setLabelHeightP(this.labelHeight_);
      var13.setLocationU(var28);
      var13.setLabelFont(var29);
      var9.addYAxis(var13);
    }

    if(this.plotType_ == 2) {
      ColorMap var21 = this.makeDefaultColorMap();
      this.gAttr_ = new GridAttribute(0, var21);
      this.gAttr_.addPropertyChangeListener(new WeakPropertyChangeListener(this, this.gAttr_));
      this.colorKey_.setColorMap(var21);
    }

    var7.setGraph(var9);
  }

  private ColorMap makeDefaultColorMap() {
    int[] var1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 23, 39, 55, 71, 87, 103, 119, 135, 151, 167, 183, 199, 215, 231, 247, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 246, 228, 211, 193, 175, 158, 140};
    int[] var2 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 27, 43, 59, 75, 91, 107, 123, 139, 155, 171, 187, 203, 219, 235, 251, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 247, 231, 215, 199, 183, 167, 151, 135, 119, 103, 87, 71, 55, 39, 23, 7, 0, 0, 0, 0, 0, 0, 0};
    int[] var3 = new int[]{0, 143, 159, 175, 191, 207, 223, 239, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 247, 231, 215, 199, 183, 167, 151, 135, 119, 103, 87, 71, 55, 39, 23, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    IndexedColorMap var4 = new IndexedColorMap(var1, var2, var3);
    LinearTransform var5 = new LinearTransform(0.0D, (double)var1.length, 0.0D, 1.0D);
    var4.setTransform(var5);
    return var4;
  }

  public String getLocationSummary(SGTData var1) {
    return "";
  }

  public void addData(Collection var1) {
    this.addData(var1, null, null);
  }

  public void addData(Collection var1, Attribute var2) {
    this.addData(var1, var2, null);
  }

  public void addData(PointCollection var1, String var2) {
    this.addData((SGTData)var1, var2);
  }

  public void addData(Collection var1, String var2) {
    this.addData(var1, null, var2);
  }

  public void addData(Collection var1, Attribute var2, String var3) {
    for(int var4 = 0; var4 < var1.size(); ++var4) {
      SGTLine var5 = (SGTLine)var1.elementAt(var4);
      this.addData(var5, var2, var5.getTitle());
    }

  }

  public void addData(SGTData var1) {
    this.addData(var1, null, null);
  }

  public void addData(SGTData var1, Attribute var2) {
    this.addData(var1, var2, null);
  }

  public void addData(SGTData var1, String var2) {
    this.addData(var1, null, var2);
  }

  public void addData(SGTData var1, Attribute var2, String var3) {
    Axis var8 = null;
    Axis var9 = null;
    SGTGrid var16 = null;
    SGTLine var17 = null;
    SGTVector var18 = null;
    PointCollection var19 = null;
    LineAttribute var20 = null;
    GridAttribute var21 = null;
    PointAttribute var22 = null;
    VectorAttribute var23 = null;
    SoTRange var26 = null;
    SoTRange var27 = null;
    SoTRange var28 = null;
    SoTRange var29 = null;
    SoTPoint var30 = null;
    Range2D var31 = null;
    boolean var32 = true;
    boolean var33 = false;
    boolean var34 = false;
    boolean var36 = false;
    Layer var4;
    CartesianGraph var6;
    LinearTransform var10;
    LinearTransform var11;
    SGLabel var14;
    SGTData var15;
    String var24;
    int var35;
    ColorMap var37;
    double[] var38;
    double var39;
    double var41;
    int var43;
    Rectangle var50;
    int var52;
    Range2D var57;
    if(this.data_.size() == 0) {
      super.addData(var1);
      var15 = (SGTData)this.data_.firstElement();
      if(this.plotType_ == 2) {
        var16 = (SGTGrid)var15;
      } else if(this.plotType_ == 0) {
        var19 = (PointCollection)var15;
      } else if(this.plotType_ == 1) {
        var17 = (SGTLine)var15;
      } else if(this.plotType_ == 3) {
        var18 = (SGTVector)var15;
      }

      if(this.plotType_ != 2) {
        if(this.plotType_ != 1 && this.plotType_ != 0) {
          if(this.plotType_ == 3) {
            var26 = this.findSoTRange(var18, 1);
            var27 = this.findSoTRange(var18, 2);
          }
        } else {
          var26 = var15.getXRange();
          var27 = var15.getYRange();
        }
      } else {
        if(var2 != null && var2 instanceof GridAttribute) {
          var21 = (GridAttribute)var2;
          var21.addPropertyChangeListener(new WeakPropertyChangeListener(this, var21));
        } else {
          var21 = this.gAttr_;
        }

        this.addAttribute(var1, var21);
        var36 = var21.isRaster();
        var31 = var16.getZRange();
        if(var21.isRaster()) {
          var37 = var21.getColorMap();
          if(var37 instanceof TransformAccess) {
            ((TransformAccess)var37).setRange(var31);
          }
        }

        var26 = this.findSoTRange(var16, var21, 1);
        var27 = this.findSoTRange(var16, var21, 2);
      }

      var33 = var15.getXMetaData().isReversed();
      var34 = var15.getYMetaData().isReversed();
      var32 = !var26.isStartOrEndMissing() && !var27.isStartOrEndMissing();
      this.revXAxis_ = var33;
      this.revYAxis_ = var34;
      if(var32) {
        if(var33) {
          var26.flipStartAndEnd();
        }

        if(var34) {
          var27.flipStartAndEnd();
        }

        if(this.isXTime_) {
          var28 = var26;
        } else if(this.autoRangeX_) {
          var28 = Graph.computeRange(var26, this.autoXIntervals_);
        } else {
          var28 = var26;
          ((gov.noaa.pmel.util.SoTRange.Double)var26).delta = ((gov.noaa.pmel.util.SoTRange.Double)Graph.computeRange(var26, this.autoXIntervals_)).delta;
        }

        if(this.isYTime_) {
          var29 = var27;
        } else if(this.autoRangeY_) {
          var29 = Graph.computeRange(var27, this.autoYIntervals_);
        } else {
          var29 = var27;
          ((gov.noaa.pmel.util.SoTRange.Double)var27).delta = ((gov.noaa.pmel.util.SoTRange.Double)Graph.computeRange(var27, this.autoYIntervals_)).delta;
        }

        this.adjustRange(var28);
        this.adjustRange(var29);
        var30 = new SoTPoint(var28.getStart(), var29.getStart());
      }

      var24 = var15.getXMetaData().getName() + " (" + var15.getXMetaData().getUnits() + ")";
      String var25 = var15.getYMetaData().getName() + " (" + var15.getYMetaData().getUnits() + ")";

      try {
        var4 = this.getLayer("Layer 1");
      } catch (LayerNotFoundException var47) {
        return;
      }

      var6 = (CartesianGraph)var4.getGraph();

      try {
        Font var48 = new Font("Helvetica", 0, 14);
        SGLabel var12 = new SGLabel("xaxis title", var24, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
        var12.setFont(var48);
        var12.setHeightP(this.titleHeight_);
        var8 = var6.getXAxis(BOTTOM_AXIS);
        var8.setRangeU(var26);
        var8.setLocationU(var30);
        var8.setTitle(var12);
        SGLabel var13 = new SGLabel("yaxis title", var25, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
        var13.setFont(var48);
        var13.setHeightP(this.titleHeight_);
        var9 = var6.getYAxis(LEFT_AXIS);
        var9.setRangeU(var29);
        var9.setLocationU(var30);
        var9.setTitle(var13);
      } catch (AxisNotFoundException ignored) {

      }

      if(var32) {
        var10 = (LinearTransform)var6.getXTransform();
        var10.setRangeU(var28);
        var11 = (LinearTransform)var6.getYTransform();
        var11.setRangeU(var29);
      }

      if(this.plotType_ == 2) {
        var6.setData(var16, var21);
      } else if(this.plotType_ == 0) {
        if(var2 != null && var2 instanceof PointAttribute) {
          var22 = (PointAttribute)var2;
        } else {
          var22 = new PointAttribute(markList_[this.layerCount_ % 8], colorList_[this.layerCount_ % 8]);
          var22.setMarkHeightP(0.15D);
          var22.setLabelHeightP(0.15D);
          var22.setDrawLabel(false);
          var22.setLabelColor(Color.red);
          var22.setLabelPosition(2);
        }

        var22.addPropertyChangeListener(new WeakPropertyChangeListener(this, var22));
        this.addAttribute(var1, var22);
        var6.setData(var19, var22);
      } else if(this.plotType_ == 1) {
        if(this.isYTime_) {
          var35 = var17.getXArray().length;
        } else {
          var35 = var17.getYArray().length;
        }

        if(var2 != null && var2 instanceof LineAttribute) {
          var20 = (LineAttribute)var2;
        } else {
          if(var35 >= 2) {
            var20 = new LineAttribute(0, markList_[this.layerCount_ % 8], colorList_[this.layerCount_ % 8]);
          } else {
            var20 = new LineAttribute(4, markList_[this.layerCount_ % 8], colorList_[this.layerCount_ % 8]);
          }

          if(((SGTLine)var1).hasAssociatedData()) {
            var37 = this.makeDefaultColorMap();
            var38 = ((SGTLine)var1).getAssociatedData();
            var39 = 1.7976931348623157E308D;
            var41 = -1.7976931348623157E308D;

            for(var43 = 0; var43 < var38.length; ++var43) {
              if(!java.lang.Double.isNaN(var38[var43])) {
                var39 = Math.min(var38[var43], var39);
                var41 = Math.max(var38[var43], var41);
              }
            }

            var57 = new Range2D(var39, var41);
            ((IndexedColorMap)var37).getTransform().setRangeU(var57);
            var20.setAssociatedDataCmap(var37);
          }
        }

        var20.addPropertyChangeListener(new WeakPropertyChangeListener(this, var20));
        this.addAttribute(var1, var20);
        var6.setData(var1, var20);
      } else if(this.plotType_ == 3) {
        if(var2 != null && var2 instanceof VectorAttribute) {
          var23 = (VectorAttribute)var2;
        } else {
          var23 = new VectorAttribute(2, 0.1D, Color.black, 0.1D);
        }

        var23.addPropertyChangeListener(new WeakPropertyChangeListener(this, var23));
        this.addAttribute(var1, var23);
        var6.setData(var18, var23);
      }

      if(var3 == null) {
        var14 = var1.getKeyTitle();
        if(var14 == null) {
          var14 = new SGLabel("line title", var24, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
        }
      } else {
        var14 = new SGLabel("line title", var3, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
      }

      var14.setHeightP(this.keyHeight_);
      int var49 = 1;
      if(!this.isShowing()) {
        this.computeScroll_ = true;
      }

      if(this.plotType_ == 2) {
        this.colorKey_.setTitle(var14);
        this.colorKey_.setColorMap(var21.getColorMap());
        this.colorKey_.setVisible(var36);
      } else if(this.plotType_ == 0) {
        this.pointKey_.addPointGraph((PointCartesianRenderer)var6.getRenderer(), var14);
        if(this.keyPane_ != null) {
          var50 = this.keyPane_.getVisibleRect();
          if(this.isShowing()) {
            var49 = this.pointKey_.getRowHeight();
          }

          var52 = var50.height / var49;
          this.keyPane_.setScrollableUnitIncrement(1, var49);
          this.keyPane_.setScrollableBlockIncrement(var50.width, var49 * var52);
        }
      } else if(this.plotType_ == 1) {
        this.lineKey_.addLineGraph((LineCartesianRenderer)var6.getRenderer(), var14);
        if(this.keyPane_ != null) {
          var50 = this.keyPane_.getVisibleRect();
          if(this.isShowing()) {
            var49 = this.lineKey_.getRowHeight();
          }

          var52 = var50.height / var49;
          this.keyPane_.setScrollableUnitIncrement(1, var49);
          this.keyPane_.setScrollableBlockIncrement(var50.width, var49 * var52);
        }
      } else if(this.plotType_ == 3) {
        this.vectorKey_.addVectorGraph((VectorCartesianRenderer)var6.getRenderer(), var14);
        if(this.keyPane_ != null) {
          var50 = this.keyPane_.getVisibleRect();
          if(this.isShowing()) {
            var49 = this.vectorKey_.getRowHeight();
          }

          var52 = var50.height / var49;
          this.keyPane_.setScrollableUnitIncrement(1, var49);
          this.keyPane_.setScrollableBlockIncrement(var50.width, var49 * var52);
        }
      }

      this.updateCoastLine();
    } else {
      if(var1 instanceof SGTLine && var1.getYMetaData().isReversed() != this.revYAxis_) {
        SGTData var53 = this.flipY(var1);
        var1 = var53;
      }

      super.addData(var1);
      var32 = false;
      ++this.layerCount_;
      if(this.isOverlayed()) {
        try {
          var4 = this.getLayer("Layer 1");
        } catch (LayerNotFoundException var45) {
          return;
        }

        var6 = (CartesianGraph)var4.getGraph();
        var10 = (LinearTransform)var6.getXTransform();
        var11 = (LinearTransform)var6.getYTransform();

        try {
          var8 = var6.getXAxis(BOTTOM_AXIS);
          var9 = var6.getYAxis(LEFT_AXIS);
        } catch (AxisNotFoundException ignored) {

        }

        SoTRange var51;
        SoTRange var54;
        if(!this.inZoom_) {
          var54 = null;
          var51 = null;
          boolean var55 = true;
          Enumeration var40 = this.data_.elements();

          while(true) {
            while(var40.hasMoreElements()) {
              var15 = (SGTData)var40.nextElement();
              var26 = var15.getXRange();
              var27 = var15.getYRange();
              var33 = var15.getXMetaData().isReversed();
              var34 = var15.getYMetaData().isReversed();
              this.revXAxis_ = var33;
              this.revYAxis_ = var34;
              if(var33) {
                var26.flipStartAndEnd();
              }

              if(var34) {
                var27.flipStartAndEnd();
              }

              if(var55) {
                var32 = !var26.isStartOrEndMissing() && !var27.isStartOrEndMissing();
                if(!var32) {
                  var55 = true;
                } else {
                  var55 = false;
                  var32 = true;
                  var54 = var26;
                  var51 = var27;
                }
              } else {
                var32 = !var26.isStartOrEndMissing() && !var27.isStartOrEndMissing();
                if(var32) {
                  var54.add(var26);
                  var51.add(var27);
                }
              }
            }

            if(var32) {
              if(this.isXTime_) {
                var28 = var54;
              } else if(this.autoRangeX_) {
                var28 = Graph.computeRange(var54, this.autoXIntervals_);
              } else {
                var28 = var54;
                ((gov.noaa.pmel.util.SoTRange.Double)var54).delta = ((gov.noaa.pmel.util.SoTRange.Double)Graph.computeRange(var54, this.autoXIntervals_)).delta;
              }

              if(this.isYTime_) {
                var29 = var51;
              } else if(this.autoRangeY_) {
                var29 = Graph.computeRange(var51, this.autoYIntervals_);
              } else {
                var29 = var51;
                ((gov.noaa.pmel.util.SoTRange.Double)var51).delta = ((gov.noaa.pmel.util.SoTRange.Double)Graph.computeRange(var51, this.autoYIntervals_)).delta;
              }

              this.adjustRange(var28);
              this.adjustRange(var29);
              var30 = new SoTPoint(var28.getStart(), var29.getStart());
              var8.setRangeU(var28);
              var8.setLocationU(var30);
              var9.setRangeU(var29);
              var9.setLocationU(var30);
              var10.setRangeU(var28);
              var11.setRangeU(var29);
              this.updateCoastLine();
            }
            break;
          }
        }

        Layer var5 = new Layer("Layer " + (this.layerCount_ + 1), new Dimension2D(this.xSize_, this.ySize_));
        CartesianGraph var7 = new CartesianGraph("Graph " + (this.layerCount_ + 1), var10, var11);
        if(this.inZoom_) {
          var37 = null;
          var50 = null;
          var54 = var8.getSoTRangeU();
          var51 = var9.getSoTRangeU();
          var7.setClip(var54, var51);
          var7.setClipping(true);
        }

        this.add(var5);
        var5.setGraph(var7);
        var5.invalidate();
        this.validate();
        if(var1 instanceof PointCollection) {
          if(var2 != null && var2 instanceof PointAttribute) {
            var22 = (PointAttribute)var2;
          } else {
            var22 = new PointAttribute(markList_[this.layerCount_ % 8], colorList_[this.layerCount_ % 8]);
            var22.setMarkHeightP(0.15D);
            var22.setLabelHeightP(0.15D);
            var22.setDrawLabel(false);
            var22.setLabelColor(Color.red);
            var22.setLabelPosition(2);
          }

          var22.addPropertyChangeListener(new WeakPropertyChangeListener(this, var22));
          this.addAttribute(var1, var22);
          var7.setData(var1, var22);
        } else if(var1 instanceof SGTLine) {
          if(this.isYTime_) {
            var35 = ((SGTLine)var1).getXArray().length;
          } else {
            var35 = ((SGTLine)var1).getYArray().length;
          }

          if(var2 != null && var2 instanceof LineAttribute) {
            var20 = (LineAttribute)var2;
          } else {
            if(var35 >= 2) {
              var20 = new LineAttribute(0, markList_[this.layerCount_ % 8], colorList_[this.layerCount_ % 8]);
            } else {
              var20 = new LineAttribute(4, markList_[this.layerCount_ % 8], colorList_[this.layerCount_ % 8]);
            }

            if(((SGTLine)var1).hasAssociatedData()) {
              var37 = this.makeDefaultColorMap();
              var38 = ((SGTLine)var1).getAssociatedData();
              var39 = 1.7976931348623157E308D;
              var41 = -1.7976931348623157E308D;

              for(var43 = 0; var43 < var38.length; ++var43) {
                if(!java.lang.Double.isNaN(var38[var43])) {
                  var39 = Math.min(var38[var43], var39);
                  var41 = Math.max(var38[var43], var41);
                }
              }

              var57 = new Range2D(var39, var41);
              ((IndexedColorMap)var37).getTransform().setRangeU(var57);
              var20.setAssociatedDataCmap(var37);
            }
          }

          var20.addPropertyChangeListener(new WeakPropertyChangeListener(this, var20));
          this.addAttribute(var1, var20);
          var7.setData(var1, var20);
        } else if(var1 instanceof SGTVector) {

        }

        CartesianRenderer var56 = var7.getRenderer();
        if(this.pointKey_ != null && var56 instanceof PointCartesianRenderer) {
          if(var3 == null) {
            var24 = var1.getXMetaData().getName();
            var14 = new SGLabel("line title", var24, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
          } else {
            var14 = new SGLabel("line title", var3, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
          }

          var14.setHeightP(this.keyHeight_);
          this.pointKey_.addPointGraph((PointCartesianRenderer)var56, var14);
          if(this.keyPane_ != null) {
            var50 = this.keyPane_.getVisibleRect();
            var52 = var50.height / this.pointKey_.getRowHeight();
            this.keyPane_.setScrollableUnitIncrement(1, this.pointKey_.getRowHeight());
            this.keyPane_.setScrollableBlockIncrement(var50.width, this.pointKey_.getRowHeight() * var52);
          }
        }

        if(this.lineKey_ != null && var56 instanceof LineCartesianRenderer) {
          if(var3 == null) {
            var24 = var1.getXMetaData().getName();
            var14 = new SGLabel("line title", var24, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
          } else {
            var14 = new SGLabel("line title", var3, new gov.noaa.pmel.util.Point2D.Double(0.0D, 0.0D));
          }

          var14.setHeightP(this.keyHeight_);
          this.lineKey_.addLineGraph((LineCartesianRenderer)var56, var14);
          if(this.keyPane_ != null) {
            var50 = this.keyPane_.getVisibleRect();
            var52 = var50.height / this.lineKey_.getRowHeight();
            this.keyPane_.setScrollableUnitIncrement(1, this.lineKey_.getRowHeight());
            this.keyPane_.setScrollableBlockIncrement(var50.width, this.lineKey_.getRowHeight() * var52);
          }
        }
      }
    }

  }

  private void adjustRange(SoTRange var1) {
    if(var1.isTime()) {
      long var2 = var1.getEnd().getLongTime();
      long var4 = var1.getStart().getLongTime();
      if(var2 == var4) {
        var2 += -1702967296L;
        var4 -= -1702967296L;
        if(var1 instanceof Time) {
          ((Time)var1).end = var2;
          ((Time)var1).start = var4;
        } else {
          ((gov.noaa.pmel.util.SoTRange.Time)var1).end = (var2);
          ((gov.noaa.pmel.util.SoTRange.Time)var1).start = (var4);
        }
      }
    } else {
      double var8 = ((gov.noaa.pmel.util.SoTRange.Double)var1).end;
      double var9 = ((gov.noaa.pmel.util.SoTRange.Double)var1).start;
      double var6 = ((gov.noaa.pmel.util.SoTRange.Double)var1).delta;
      if(var6 == 0.0D) {
        var8 = var9;
      }

      if(var8 == var9) {
        if(var8 == 0.0D) {
          var9 = -1.0D;
          var8 = 1.0D;
        } else {
          if(var8 > 0.0D) {
            var8 = 1.1D * var8;
          } else {
            var8 = 0.9D * var8;
          }

          if(var9 > 0.0D) {
            var9 = 0.9D * var9;
          } else {
            var9 = 1.1D * var9;
          }
        }

        ((gov.noaa.pmel.util.SoTRange.Double)var1).end = var8;
        ((gov.noaa.pmel.util.SoTRange.Double)var1).start = var9;
        ((gov.noaa.pmel.util.SoTRange.Double)var1).delta = ((gov.noaa.pmel.util.SoTRange.Double)Graph.computeRange(var1, 10)).delta;
      }
    }

  }

  private SGTData flipY(SGTData var1) {
    SimpleLine var4 = null;
    SGTLine var5 = (SGTLine)var1;
    double[] var6 = var5.getYArray();
    double[] var7 = new double[var6.length];

    for(int var8 = 0; var8 < var6.length; ++var8) {
      var7[var8] = -var6[var8];
    }

    var4 = new SimpleLine(var5.getXArray(), var7, var5.getTitle());
    SGTMetaData var3 = var5.getYMetaData();
    SGTMetaData var2 = new SGTMetaData(var3.getName(), var3.getUnits(), var3.isReversed(), var3.isModulo());
    var2.setModuloValue(var3.getModuloValue());
    var2.setModuloTime(var3.getModuloTime());
    var4.setXMetaData(var5.getXMetaData());
    var4.setYMetaData(var2);
    return var4;
  }

  public void resetZoom() {
    GridAttribute var2 = null;
    SoTRange var4 = null;
    SoTRange var5 = null;
    SoTRange var6 = null;
    SoTRange var7 = null;
    boolean var8 = false;
    boolean var9 = false;
    boolean var10 = false;
    boolean var11 = this.isBatch();
    this.setBatch(true, "JPlotLayout: resetZoom");
    this.inZoom_ = false;
    this.setAllClipping(false);
    this.setClipping(false);
    boolean var12 = true;
    Enumeration var13 = this.data_.elements();

    while(var13.hasMoreElements()) {
      SGTData var3 = (SGTData)var13.nextElement();

      Attribute var1;
      try {
        var1 = this.getAttribute(var3);
      } catch (DataNotFoundException var16) {
        System.out.println(var16);
        var1 = null;
      }

      if(this.plotType_ == 2) {
        if(var1 != null && var1 instanceof GridAttribute) {
          var2 = (GridAttribute)var1;
        } else {
          var2 = this.gAttr_;
        }

        var4 = this.findSoTRange((SGTGrid)var3, var2, 1);
        var5 = this.findSoTRange((SGTGrid)var3, var2, 2);
      } else if(this.plotType_ != 0 && this.plotType_ != 1) {
        if(this.plotType_ == 3) {
          var4 = this.findSoTRange((SGTVector)var3, 1);
          var5 = this.findSoTRange((SGTVector)var3, 2);
        }
      } else {
        var4 = var3.getXRange();
        var5 = var3.getYRange();
      }

      var10 = var3.getXMetaData().isReversed();
      var9 = var3.getYMetaData().isReversed();
      this.revXAxis_ = var10;
      this.revYAxis_ = var9;
      if(var10) {
        var4.flipStartAndEnd();
      }

      if(var9) {
        var5.flipStartAndEnd();
      }

      if(var12) {
        var8 = !var4.isStartOrEndMissing() && !var5.isStartOrEndMissing();
        if(!var8) {
          var12 = true;
        } else {
          var12 = false;
          var8 = true;
          var6 = var4;
          var7 = var5;
        }
      } else {
        var8 = !var4.isStartOrEndMissing() && !var5.isStartOrEndMissing();
        if(var8) {
          var6.add(var4);
          var7.add(var5);
        }
      }
    }

    if(var6 != null && var7 != null) {
      this.adjustRange(var6);
      this.adjustRange(var7);
      if(var8) {
        try {
          this.setRange(new SoTDomain(var6, var7, var10, var9));
        } catch (PropertyVetoException var15) {
          System.out.println("zoom reset denied! " + var15);
        }
      }
    }

    this.inZoom_ = false;
    this.updateCoastLine();
    if(!var11) {
      this.setBatch(false, "JPlotLayout: resetZoom");
    }

  }

  public Domain getRange() {
    CartesianGraph var1 = (CartesianGraph)this.firstLayer_.getGraph();
    LinearTransform var2 = (LinearTransform)var1.getXTransform();
    LinearTransform var3 = (LinearTransform)var1.getYTransform();
    Range2D var4 = null;
    Range2D var5 = null;
    SoTRange var6 = null;
    if(var2.isTime()) {
      var6 = var2.getSoTRangeU();
    } else {
      var4 = var2.getRangeU();
    }

    if(var3.isTime()) {
      var6 = var3.getSoTRangeU();
    } else {
      var5 = var3.getRangeU();
    }

    return var2.isTime()?new Domain(var6, var5):(var3.isTime()?new Domain(var4, var6):new Domain(var4, var5));
  }

  public void setRange(SoTDomain var1) throws PropertyVetoException {
    Domain var2 = new Domain();
    SoTRange var3;
    Time var4;
    gov.noaa.pmel.util.SoTRange.Double var5;
    gov.noaa.pmel.util.SoTRange.Time var6;
    if(var1.isXTime()) {
      var3 = var1.getXRange();
      if(var3 instanceof Time) {
        var4 = (Time)var3;
        var2.setXRange(new SoTRange(var4.start, var4.end));
      } else {
        var6 = (gov.noaa.pmel.util.SoTRange.Time)var3;
        var2.setXRange(new SoTRange(var6.start, var6.end));
      }
    } else {
      var5 = (gov.noaa.pmel.util.SoTRange.Double)var1.getXRange();
      var2.setXRange(new Range2D(var5.start, var5.end, var5.delta));
    }

    if(var1.isYTime()) {
      var3 = var1.getYRange();
      if(var3 instanceof Time) {
        var4 = (Time)var3;
        var2.setYRange(new SoTRange(var4.start, var4.end));
      } else {
        var6 = (gov.noaa.pmel.util.SoTRange.Time)var3;
        var2.setYRange(new SoTRange(var6.start, var6.end));
      }
    } else {
      var5 = (gov.noaa.pmel.util.SoTRange.Double)var1.getYRange();
      var2.setYRange(new Range2D(var5.start, var5.end, var5.delta));
    }

    var2.setXReversed(var1.isXReversed());
    var2.setYReversed(var1.isYReversed());
    this.setRange(var2);
  }

  public void setRange(Domain var1) throws PropertyVetoException {
    Domain var2 = this.getRange();
    if(!var1.equals(var2)) {
      boolean var3 = this.isBatch();
      this.setBatch(true, "JPlotLayout: setRange");
      this.vetos_.fireVetoableChange("domainRange", var2, var1);
      this.inZoom_ = true;
      if(!var1.isXTime()) {
        this.setXRange(var1.getXRange());
      } else {
        this.setXRange(var1.getTimeRange());
      }

      if(!var1.isYTime()) {
        this.setYRange(var1.getYRange(), var1.isYReversed());
      } else {
        this.setYRange(var1.getTimeRange());
      }

      this.changes_.firePropertyChange("domainRange", var2, var1);
      if(!var3) {
        this.setBatch(false, "JPlotLayout: setRange");
      }

      this.updateCoastLine();
    }

  }

  public void setRangeNoVeto(Domain var1) {
    boolean var2 = this.isBatch();
    this.setBatch(true, "JPlotLayout: setRangeNoVeto");
    this.inZoom_ = true;
    this.setClipping(true);
    if(!var1.isXTime()) {
      this.setXRange(var1.getXRange());
    } else {
      this.setXRange(var1.getTimeRange());
    }

    if(!var1.isYTime()) {
      this.setYRange(var1.getYRange(), var1.isYReversed());
    } else {
      this.setYRange(var1.getTimeRange());
    }

    if(!var2) {
      this.setBatch(false, "JPlotLayout: setRangeNoVeto");
    }

    this.updateCoastLine();
  }

  void setXRange(SoTRange var1) {
    Time var5 = (Time) var1;
    CartesianGraph var7 = (CartesianGraph)this.firstLayer_.getGraph();
    LinearTransform var8 = (LinearTransform)var7.getXTransform();
    var8.setRangeU(var5);

    try {
      Axis var2 = var7.getXAxis(BOTTOM_AXIS);
      Axis var3 = var7.getYAxis(LEFT_AXIS);
      var2.setRangeU(var5);
      SoTRange var4 = var3.getSoTRangeU();
      SoTPoint var6 = new SoTPoint(var5.getStart(), var4.getStart());
      var2.setLocationU(var6);
      var3.setLocationU(var6);
      if(this.clipping_) {
        this.setAllClip(var5, var4);
      } else {
        this.setAllClipping(false);
      }
    } catch (AxisNotFoundException ignored) {

    }

  }

  void setXRange(Range2D var1) {
    gov.noaa.pmel.util.SoTRange.Double var4 = new gov.noaa.pmel.util.SoTRange.Double(var1);
    CartesianGraph var8 = (CartesianGraph)this.firstLayer_.getGraph();
    LinearTransform var9 = (LinearTransform)var8.getXTransform();
    Object var7;
    if(this.autoRangeX_) {
      var7 = Graph.computeRange(var4, this.autoXIntervals_);
    } else {
      var7 = var4;
      var4.delta = ((gov.noaa.pmel.util.SoTRange.Double)Graph.computeRange(var4, this.autoXIntervals_)).delta;
    }

    var9.setRangeU((SoTRange)var7);

    try {
      Axis var2 = var8.getXAxis(BOTTOM_AXIS);
      Axis var3 = var8.getYAxis(LEFT_AXIS);
      SoTRange var5 = var3.getSoTRangeU();
      var2.setRangeU((SoTRange)var7);
      SoTRange var12 = var2.getSoTRangeU();
      SoTPoint var6 = new SoTPoint(var12.getStart(), var5.getStart());
      var2.setLocationU(var6);
      var3.setLocationU(var6);
      if(this.clipping_) {
        this.setAllClip(var12, var5);
      } else {
        this.setAllClipping(false);
      }
    } catch (AxisNotFoundException ignored) {
    }

  }

  void setYRange(SoTRange var1) {
    Time var5 = (Time) (var1);
    CartesianGraph var7 = (CartesianGraph)this.firstLayer_.getGraph();
    LinearTransform var8 = (LinearTransform)var7.getYTransform();
    var8.setRangeU(var5);

    try {
      Axis var2 = var7.getXAxis(BOTTOM_AXIS);
      Axis var3 = var7.getYAxis(LEFT_AXIS);
      var3.setRangeU(var5);
      SoTRange var4 = var2.getSoTRangeU();
      SoTPoint var6 = new SoTPoint(var4.getStart(), var5.getStart());
      var3.setLocationU(var6);
      var2.setLocationU(var6);
      if(this.clipping_) {
        this.setAllClip(var4, var5);
      } else {
        this.setAllClipping(false);
      }
    } catch (AxisNotFoundException ignored) {
    }

  }

  void setYRange(Range2D var1) {
    this.setYRange(var1, true);
  }

  void setYRange(Range2D var1, boolean var2) {
    gov.noaa.pmel.util.SoTRange.Double var7 = new gov.noaa.pmel.util.SoTRange.Double(var1);
    CartesianGraph var10 = (CartesianGraph)this.firstLayer_.getGraph();
    LinearTransform var11 = (LinearTransform)var10.getYTransform();
    if(!this.data_.isEmpty()) {
      SGTData var3 = (SGTData)this.data_.elements().nextElement();
      if(this.data_.size() > 0 && var2 != var3.getYMetaData().isReversed()) {
        var7.flipStartAndEnd();
      }
    }

    Object var8;
    if(this.autoRangeY_) {
      var8 = Graph.computeRange(var7, this.autoYIntervals_);
    } else {
      var8 = var7;
      var7.delta = ((gov.noaa.pmel.util.SoTRange.Double)Graph.computeRange(var7, this.autoYIntervals_)).delta;
    }

    var11.setRangeU((SoTRange)var8);

    try {
      Axis var4 = var10.getXAxis(BOTTOM_AXIS);
      Axis var5 = var10.getYAxis(LEFT_AXIS);
      SoTRange var6 = var4.getSoTRangeU();
      var5.setRangeU((SoTRange)var8);
      SoTRange var14 = var5.getSoTRangeU();
      SoTPoint var9 = new SoTPoint(var6.getStart(), var14.getStart());
      var5.setLocationU(var9);
      var4.setLocationU(var9);
      if(this.clipping_) {
        this.setAllClip(var6, var14);
      } else {
        this.setAllClipping(false);
      }
    } catch (AxisNotFoundException ignored) {
    }

  }

  public SGTData getData(String var1) {
    try {
      Layer var2 = this.getLayerFromDataId(var1);
      if(var2 != null) {
        CartesianRenderer var3 = ((CartesianGraph)var2.getGraph()).getRenderer();
        if(var3 != null) {
          if(var3 instanceof LineCartesianRenderer) {
            return ((LineCartesianRenderer)var3).getLine();
          }

          if(var3 instanceof GridCartesianRenderer) {
            return ((GridCartesianRenderer)var3).getGrid();
          }
        }
      }
    } catch (LayerNotFoundException ignored) {

    }

    return null;
  }

  public SGTData getData(CartesianRenderer var1) {
    return var1 instanceof LineCartesianRenderer?((LineCartesianRenderer)var1).getLine():(var1 instanceof GridCartesianRenderer?((GridCartesianRenderer)var1).getGrid():null);
  }

  public void clear() {
    this.data_.removeAllElements();
    ((CartesianGraph)this.firstLayer_.getGraph()).setRenderer(null);
    this.removeAll();
    this.add(this.firstLayer_, 0);
    if(this.coastLine_ != null) {
      this.add(this.coastLayer_, 0);
    }

    if(this.lineKey_ != null) {
      this.lineKey_.clearAll();
    }

    if(this.pointKey_ != null) {
      this.pointKey_.clearAll();
    }

    this.inZoom_ = false;
  }

  public void clear(String var1) {
    Layer var2 = null;

    try {
      var2 = this.getLayerFromDataId(var1);
      this.remove(var2);
    } catch (LayerNotFoundException ignored) {

    }

    Enumeration var4 = this.data_.elements();

    while(var4.hasMoreElements()) {
      SGTData var3 = (SGTData)var4.nextElement();
      if(var3.getId().equals(var1)) {
        this.data_.removeElement(var3);
      }
    }

    if(this.lineKey_ != null) {
      this.lineKey_.clear(var1);
    }

    if(this.getComponentCount() <= 0 || var2.equals(this.firstLayer_)) {
      ((CartesianGraph)this.firstLayer_.getGraph()).setRenderer(null);
      this.add(this.firstLayer_, 0);
    }

  }

  public Dimension2D getLayerSizeP() {
    return new Dimension2D(this.xSize_, this.ySize_);
  }

  public Layer getFirstLayer() {
    return this.firstLayer_;
  }

  public void setAxesOriginP(gov.noaa.pmel.util.Point2D.Double var1) {
    this.xMin_ = var1.x;
    this.yMin_ = var1.y;
  }

  public SoTDomain getGraphDomain() {
    SoTRange var1 = null;
    SoTRange var2 = null;
    CartesianGraph var3 = null;

    try {
      Layer var4 = this.getLayer("Layer 1");
      var3 = (CartesianGraph)var4.getGraph();
    } catch (LayerNotFoundException var7) {
      return null;
    }

    try {
      Axis var8 = var3.getXAxis(BOTTOM_AXIS);
      Axis var5 = var3.getYAxis(LEFT_AXIS);
      var1 = var8.getSoTRangeU();
      var2 = var5.getSoTRangeU();
    } catch (AxisNotFoundException var6) {
      return null;
    }

    return new SoTDomain(var1, var2);
  }

  public void setLayerSizeP(Dimension2D var1) {
    Component[] var2 = this.getComponents();
    CartesianGraph var3 = (CartesianGraph)this.firstLayer_.getGraph();
    LinearTransform var4 = (LinearTransform)var3.getYTransform();
    LinearTransform var5 = (LinearTransform)var3.getXTransform();
    this.xMax_ = var1.width - (this.xSize_ - this.xMax_);
    this.yMax_ = var1.height - (this.ySize_ - this.yMax_);
    this.xSize_ = var1.width;
    this.ySize_ = var1.height;

    for (Component aVar2 : var2) {
      if (aVar2 instanceof Layer) {
        ((Layer) aVar2).setSizeP(var1);
      }
    }

    var4.setRangeP(new Range2D(this.yMin_, this.yMax_));
    var5.setRangeP(new Range2D(this.xMin_, this.xMax_));
    double var10;
    if(this.iconImage_ != null) {
      Rectangle var8 = this.logo_.getBounds();
      var10 = this.firstLayer_.getXDtoP(var8.x + var8.width) + 0.05D;
    } else {
      var10 = (this.xMin_ + this.xMax_) * 0.5D;
    }

    double var11 = this.ySize_ - 1.0D * this.mainTitleHeight_;
    this.mainTitle_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(var10, var11));
    var11 -= 1.0D * this.warnHeight_;
    this.title2_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(var10, var11));
    var11 -= 1.0D * this.warnHeight_;
    this.title3_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(var10, var11));
  }

  public void setTitleHeightP(double var1, double var3) {
    double var5 = this.ySize_ - 1.0D * var1;
    double var7 = this.mainTitle_.getLocationP().x;
    boolean var9 = this.isBatch();
    this.setBatch(true, "JPlotLayout: setTitleHeightP");
    if(var1 != this.mainTitleHeight_) {
      this.mainTitleHeight_ = var1;
      this.mainTitle_.setHeightP(var1);
      this.mainTitle_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(var7, var5));
    }

    if(var3 != this.warnHeight_) {
      this.warnHeight_ = var3;
      var5 -= 1.0D * var3;
      this.title2_.setHeightP(var3);
      this.title2_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(var7, var5));
      var5 -= 1.0D * this.warnHeight_;
      this.title3_.setHeightP(var3);
      this.title3_.setLocationP(new gov.noaa.pmel.util.Point2D.Double(var7, var5));
    }

    if(!var9) {
      this.setBatch(false, "JPlotLayout: setTitleHeightP");
    }

  }

  public double getMainTitleHeightP() {
    return this.mainTitleHeight_;
  }

  public double getSecondaryTitleHeightP() {
    return this.warnHeight_;
  }

  public void setKeyBoundsP(Double var1) {
    if(this.plotType_ == 2 && this.colorKey_ != null) {
      this.colorKey_.setBoundsP(var1);
    } else if(this.plotType_ == 0 && this.pointKey_ != null) {
      this.pointKey_.setBoundsP(var1);
    } else if(this.plotType_ == 1 && this.lineKey_ != null) {
      this.lineKey_.setBoundsP(var1);
    } else if(this.plotType_ == 3 && this.vectorKey_ != null) {
      this.vectorKey_.setBoundsP(var1);
    }

  }

  public Double getKeyBoundsP() {
    return this.plotType_ == 2 && this.colorKey_ != null?this.colorKey_.getBoundsP():(this.plotType_ == 0 && this.pointKey_ != null?this.pointKey_.getBoundsP():(this.plotType_ == 1 && this.lineKey_ != null?this.lineKey_.getBoundsP():(this.plotType_ == 3 && this.vectorKey_ != null?this.vectorKey_.getBoundsP():null)));
  }

  public void setKeyAlignment(int var1, int var2) {
    if(this.plotType_ == 2 && this.colorKey_ != null) {
      this.colorKey_.setAlign(var1, var2);
    } else if(this.plotType_ == 0 && this.pointKey_ != null) {
      this.pointKey_.setAlign(var1, var2);
    } else if(this.plotType_ == 1 && this.lineKey_ != null) {
      this.lineKey_.setAlign(var1, var2);
    } else if(this.plotType_ == 3 && this.vectorKey_ != null) {
      this.vectorKey_.setAlign(var1, var2);
    }

  }

  public gov.noaa.pmel.util.Point2D.Double getKeyPositionP() {
    Double var1 = this.getKeyBoundsP();
    double var2 = var1.x;
    double var4 = var1.y;
    return new gov.noaa.pmel.util.Point2D.Double(var2, var4);
  }

  public void setKeyLocationP(gov.noaa.pmel.util.Point2D.Double var1) {
    if(this.keyPane_ == null) {
      if(this.plotType_ == 2) {
        this.colorKey_.setLocationP(var1);
      } else if(this.plotType_ == 0) {
        this.pointKey_.setLocationP(var1);
      } else if(this.plotType_ == 1) {
        this.lineKey_.setLocationP(var1);
      } else if(this.plotType_ == 3) {
        this.vectorKey_.setLocationP(var1);
      }
    }

  }

  public void propertyChange(PropertyChangeEvent var1) {
    if(var1.getSource() instanceof GridAttribute && Objects.equals(var1.getPropertyName(), "style") && this.plotType_ == 2) {
      SGTGrid var2 = (SGTGrid)this.data_.firstElement();

      try {
        GridAttribute var3 = (GridAttribute)this.getAttribute(var2);
        Range2D var4 = this.findRange(var2, var3, 3);
        if(var3.isRaster()) {
          ColorMap var5 = var3.getColorMap();
          if(var5 instanceof TransformAccess) {
            ((TransformAccess)var5).setRange(var4);
          }

          this.colorKey_.setColorMap(var5);
          this.colorKey_.setVisible(true);
        } else {
          this.colorKey_.setVisible(false);
        }
      } catch (DataNotFoundException var6) {
        System.out.println(var6);
      }

      if(this.keyPane_ != null) {
        this.keyPane_.setModified(true, "JPlotLayout: forced setModified");
        this.keyPane_.setBatch(false, "JPlotLayout: propertyChange");
      }
    }

    if(var1.getSource() instanceof Attribute) {
      boolean var7 = true;
      if(var1 instanceof AttributeChangeEvent) {
        var7 = ((AttributeChangeEvent)var1).isLocal();
      }

      this.changes_.firePropertyChange(new AttributeChangeEvent(this, "attribute", null, var1.getSource(), var7));
    }

  }

  public void setCoastLine(SGTLine var1) {
    if(this.coastLine_ == null) {
      CartesianGraph var2 = (CartesianGraph)this.firstLayer_.getGraph();
      LinearTransform var3 = (LinearTransform)var2.getXTransform();
      LinearTransform var4 = (LinearTransform)var2.getYTransform();
      Range2D var5 = var3.getRangeU();
      Range2D var6 = var4.getRangeU();
      this.coastLayer_ = new Layer("CoastLine", new Dimension2D(this.xSize_, this.ySize_));
      this.add(this.coastLayer_, 0);
      this.coastLine_ = new CartesianGraph("CoastLine Graph");
      this.coastLayer_.setGraph(this.coastLine_);
      this.coastLine_.setXTransform(var3);
      this.coastLine_.setYTransform(var4);
      LineAttribute var7 = new LineAttribute();
      var7.setColor(new Color(244, 164, 96));
      var7.addPropertyChangeListener(new WeakPropertyChangeListener(this, var7));
      this.coastLine_.setData(var1, var7);
      this.coastLine_.setClip(var5.start, var5.end, var6.start, var6.end);
      this.coastLine_.setClipping(true);
      this.coastLayer_.invalidate();
      this.validate();
    }

  }

  void updateCoastLine() {
    if(this.coastLine_ != null) {
      CartesianGraph var1 = (CartesianGraph)this.coastLayer_.getGraph();
      LinearTransform var2 = (LinearTransform)var1.getXTransform();
      LinearTransform var3 = (LinearTransform)var1.getYTransform();
      Range2D var4 = var2.getRangeU();
      Range2D var5 = var3.getRangeU();
      this.coastLine_.setClip(var4.start, var4.end, var5.start, var5.end);
      this.coastLine_.setClipping(true);
      this.coastLayer_.invalidate();
      this.validate();
    }

  }

  public int print(Graphics var1, PageFormat var2, int var3) {
    if(var3 > 0) {
      return 1;
    } else {
      Graphics2D var4 = (Graphics2D)var1;
      this.drawPage(var4, var2);
      if(this.keyPane_ != null) {
        var4.setTransform(new AffineTransform());
        Point var5 = this.keyPane_.getLocation();
        double var6 = 72.0D;
        double var8 = 0.5D;
        int var10 = (int)((this.getLayerSizeP().getHeight() + var8) * var6) - var5.y;
        int var11 = -var5.x;
        Point var12 = new Point(var11, var10);
        this.keyPane_.setPageOrigin(var12);
        this.keyPane_.setPageVAlign(-1);
        this.keyPane_.setPageHAlign(1);
        var4.setClip(-1000, -1000, 5000, 5000);
        this.keyPane_.drawPage(var4, var2, true);
      }

      return 0;
    }
  }

  public void setXAutoRange(boolean var1) {
    this.autoRangeX_ = var1;
  }

  public void setYAutoRange(boolean var1) {
    this.autoRangeY_ = var1;
  }

  public void setAutoRange(boolean var1, boolean var2) {
    this.autoRangeX_ = var1;
    this.autoRangeY_ = var2;
  }

  public boolean isXAutoRange() {
    return this.autoRangeX_;
  }

  public boolean isYAutoRange() {
    return this.autoRangeY_;
  }

  public void setXAutoIntervals(int var1) {
    this.autoXIntervals_ = var1;
  }

  public void setYAutoIntervals(int var1) {
    this.autoYIntervals_ = var1;
  }

  public void setAutoIntervals(int var1, int var2) {
    this.autoXIntervals_ = var1;
    this.autoYIntervals_ = var2;
  }

  public int getXAutoIntervals() {
    return this.autoXIntervals_;
  }

  public int getYAutoIntervals() {
    return this.autoYIntervals_;
  }

  public void init() {
    if(this.computeScroll_) {
      this.computeScroll_ = false;
      boolean var1 = true;
      if(this.plotType_ != 2) {
        Rectangle var2;
        int var3;
        int var4;
        if(this.plotType_ == 0) {
          if(this.keyPane_ != null) {
            var2 = this.keyPane_.getVisibleRect();
            var4 = this.pointKey_.getRowHeight();
            var3 = var2.height / var4;
            this.keyPane_.setScrollableUnitIncrement(1, var4);
            this.keyPane_.setScrollableBlockIncrement(var2.width, var4 * var3);
          }
        } else if(this.plotType_ == 1) {
          if(this.keyPane_ != null) {
            var2 = this.keyPane_.getVisibleRect();
            var4 = this.lineKey_.getRowHeight();
            var3 = var2.height / var4;
            this.keyPane_.setScrollableUnitIncrement(1, var4);
            this.keyPane_.setScrollableBlockIncrement(var2.width, var4 * var3);
          }
        } else if(this.plotType_ == 3 && this.keyPane_ != null) {
          var2 = this.keyPane_.getVisibleRect();
          var4 = this.vectorKey_.getRowHeight();
          var3 = var2.height / var4;
          this.keyPane_.setScrollableUnitIncrement(1, var4);
          this.keyPane_.setScrollableBlockIncrement(var2.width, var4 * var3);
        }
      }
    }

  }

  static {
    Color b = new Color(0, 0, .1f, 0.1f);
    Color lb = new Color(0,0,.05f, 0.1f);
    Color r = new Color(.1f, 0, 0, 0.1f);
    Color p = new Color(.05f,0,0, 0.1f);
    Color o = new Color(.025f,0,0, 0.1f);
    Color g = new Color(0, .1f, 0, 0.1f);
    Color bl = new Color(.1f, .1f, .1f, 0.1f);
    Color w = new Color(0, 0, 0, 0.1f);
    colorList_2 = new Color[]{lb,b,g,o,r,p,bl,w};
    colorList_ = new Color[]{Color.blue, Color.cyan.darker(), Color.green, Color.orange.darker(), Color.red, Color.magenta, Color.black, Color.gray};
    //colorList_ = new Color[]{Color.orange, Color.orange.darker(), Color.lightGray, Color.yellow.darker(), Color.pink, Color.pink.brighter(), Color.white, Color.BLUE};
    markList_ = new int[]{1, 2, 9, 15, 10, 24, 11, 44};
    LEFT_AXIS = "Left Axis";
    BOTTOM_AXIS = "Bottom Axis";
  }
}
