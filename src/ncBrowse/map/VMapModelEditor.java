/**
 *  $Id: VMapModelEditor.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import gov.noaa.pmel.swing.JViewHTMLFrame;
import gov.noaa.pmel.swing.SelectDoubleDialog;
import ncBrowse.SelectTimeDialog;
//import gov.noaa.pmel.swing.SelectTimeDialog;
import gov.noaa.pmel.swing.Swatch;
import ncBrowse.*;
import ncBrowse.Debug;
import ncBrowse.VisAD.*;
import ncBrowse.sgt.*;
import ncBrowse.sgt.ColorMap;
import ncBrowse.sgt.dm.*;
import ncBrowse.sgt.geom.*;
import ncBrowse.sgt.swing.JClassTree;
import ncBrowse.sgt.swing.JPlotLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

//import gov.noaa.pmel.sgt.*;
//import gov.noaa.pmel.sgt.ColorMap;
//import gov.noaa.pmel.sgt.dm.*;
//import gov.noaa.pmel.sgt.swing.JClassTree;
//import gov.noaa.pmel.util.*;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.28 $, $Date: 2004/05/14 21:19:32 $
 */
public class VMapModelEditor extends JFrame implements ButtonMaintainer, ChangeListener, DialogClient {
  private final NcFile ncFile_;
  public final Browser parent_;
  private JFrame display_;
  private JPlotLayout layout_;
  private JPanel graphPane_;
  private JPanel keyPanel_;
//  private JScrollPane keyPanePanel_;
  private JPane keyPane_ = null;
  private SymAction lSymAction_;
  private JMenuItem printMenuItem_;
  private JMenuItem mapMenuItem_;
  private JMenuItem exitMenuItem_;
  private JMenuItem resetZoomMenuItem_;
  private JMenuItem classTreeMenuItem_;
  private int x_ = 0;
  private int y_ = 50;
//  private LineAttributeDialog lad_ = null;
//  private VectorAttributeDialog vad_ = null;
  /**
   * @label model_
   */
  private VMapModel model_ = null;
  private JPane pane_ = null;
  VisADRenderer visADRenderer_ = null;
  private JTextField[] name_;
  private JTextField[] units_;
  private JCheckBox[] rBox_;
  private JButton[] movieButton_;
  private JTextField[] min_;
  private JTextField[] max_;
  private JButton[] minPopupButton_;
  private JButton[] maxPopupButton_;
  private Object[] minRange_;
  private Object[] maxRange_;
  private int[] rBoxGroup_;
  private boolean[] movieBtnsUsed;
  public final int win;
  public static int win2 = VariableWindows2.whatFrame();

  private static ArrayList instances = new ArrayList();

  /**
   * @label vParam_
   */
  private VMapParameter[] vParam_;
  private boolean[] isTime_;
  private final MyAction myAction_;
  private MyMouse myMouse_;
  private SelectDoubleDialog sdd_ = null;
  private SelectTimeDialog std_ = null;
  private final String tFormat_ = "yyyy-MM-dd HH:mm:ss";

  private final ImageIcon movieIcon_;
  private final ImageIcon dots_;

  final BorderLayout borderLayout1 = new BorderLayout();
  final JPanel jPanel1 = new JPanel();
  final JButton doneButton = new JButton();
  final JButton applyButton = new JButton();
  final JButton closeButton = new JButton();
  final JButton helpButton = new JButton();
  final JPanel infoPanel = new JPanel();
  final JScrollPane infoScrollPane = new JScrollPane();
  final BorderLayout borderLayout2 = new BorderLayout();
  TitledBorder paramBorder;
  final JEditorPane infoText = new JEditorPane();
  TitledBorder axisBorder;
  final JPanel mainPanel = new JPanel();
  final JCheckBox revXAxisCB = new JCheckBox();
  final JPanel axesPanel = new JPanel();
  final JCheckBox revYAxisCB = new JCheckBox();
  final JCheckBox revZAxisCB = new JCheckBox();
  final BorderLayout borderLayout3 = new BorderLayout();
  final JScrollPane paramScrollPane = new JScrollPane();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  boolean is3D = false;
  private SGTData[] subset_ = null;
  protected VisADRenderer vizRend = null;
  private boolean isPlotted = false;
  private boolean isPlotOpen = false;
  private boolean thisIsOpen = true;
  private int vPsize;
  private MaintenanceTimer mMaintain;
  private final VMapModelEditor mThis;
  TitledBorder optionsBorder;
  private final ResourceBundle b = ResourceBundle.getBundle(
      "ncBrowse.NcBrowseResources");
  private JRadioButton mNoLegend;
  private JRadioButton mCBOnlyLegend;
  private JCheckBox mLegend;
  private Swatch mBGColor;
  private Swatch mAxesColor;
  private final JButton mAdvancedButton = new JButton();
  private JLabel mLabel7;
  private JLabel mLabel8;

  final JButton openButton = new JButton();
  final JComboBox sendCBox = new JComboBox();
  final DefaultComboBoxModel sendCBoxModel = new DefaultComboBoxModel();

  private static int epCount_ = 0;

  private boolean mResampleAxesFlag = false;
  private int mResampleMode = VisADPlotSpecification.WEIGHTED_AVERAGE;
  private double mVectorScale = 0.25;
  private boolean mUIncluded = true;
  private boolean mVIncluded = true;
  private boolean mWIncluded = true;
  private boolean mRespectAspectRatioFlag = true;
  private double mXScaling = 1.0;
  private double mYScaling = 1.0;
  private double mZScaling = 1.0;
//  private boolean win1 = true;
//  private boolean win2 = false;
//  private boolean win3 = false;
//  private boolean win4 = false;
//  private boolean win5 = false;
//  private boolean win6 = false;
//  private boolean win7 = false;
//  private boolean win8 = false;
  //private int win = parent_.win ;
//  private String win1 = VariableWindows.getVarWin1Frame().getTitle();
//  private String win2 = VariableWindows.getVarWin2Frame().getTitle();
//  private String win3 = VariableWindows.getVarWin3Frame().getTitle();
//  private String win4 = VariableWindows.getVarWin4Frame().getTitle();
//  private String win5 = VariableWindows.getVarWin5Frame().getTitle();
//  private String win6 = VariableWindows.getVarWin6Frame().getTitle();
//  private String win7 = VariableWindows.getVarWin7Frame().getTitle();
//  private String win8 = VariableWindows.getVarWin8Frame().getTitle();

  public VMapModelEditor(VMapModel model, Browser parent) {
    super();
    model_ = model;
    parent_ = parent;
    win = Browser.getInstance().getInt();
    //System.out.println("TEST: "+ Browser.getInstance().getInt());
    setSize(800, 600);
    setVisible(false);
    try {
      jbInit();
    } catch(Exception e) {
      e.printStackTrace();
    }
    myAction_ = new MyAction();
    myMouse_ = new MyMouse();
    movieIcon_ = new ImageIcon(Browser.getInstance().getClass().getResource(
        "images/Movie24.gif"));
    dots_ = new ImageIcon(Browser.getInstance().getClass().getResource(
        "images/3Dots16.gif"));
    is3D = model_.is3D();
    ncFile_ = model_.getNcFile();
    model_.addChangeListener(this);

    WindowListener windowListener = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        thisIsOpen = false;
      }

      public void windowActivated(WindowEvent event) {
        if(display_ != null && !display_.isVisible() && !thisIsOpen) {
          display_.setVisible(true);
          isPlotOpen = true;
          isPlotted = true;
          thisIsOpen = true;
        } else if(vizRend != null && !vizRend.getDisplayWindow().isVisible() &&
                  !thisIsOpen) {
          vizRend.getDisplayWindow().setVisible(true);
          isPlotOpen = true;
          isPlotted = true;
          thisIsOpen = true;
        }
      }
    };
    this.addWindowListener(windowListener);
    mThis = this;
  }

  public VMapModelEditor() {
    this(null, null);
  }

  public void init() {
    //
    // build infoText
    //
    infoText.setText(getMappingAsString());
    //
    // construct paramPanel entries from VMapParameter's
    //
    JPanel paramPane = buildParamPane();
    paramScrollPane.getViewport().add(paramPane);
    //
    revXAxisCB.setSelected(model_.isXReversed());
    revYAxisCB.setSelected(model_.isYReversed());
    revYAxisCB.setSelected(model_.isZReversed());
    //
    revXAxisCB.setText(b.getString("kReverseX") + " (" +
            model_.getElementName(VMapModel.X_AXIS) +
            ")");
    revYAxisCB.setText(b.getString("kReverseY") + " (" +
                       model_.getElementName(VMapModel.Y_AXIS) +
                       ")");
    if(model_.getElementName(VMapModel.Z_AXIS) != null &&
       !model_.getElementName(VMapModel.Z_AXIS).equalsIgnoreCase("bad value")) {
      revZAxisCB.setText(b.getString("kReverseZ") + " (" +
                         model_.getElementName(VMapModel.Z_AXIS) + ")");
    } else {
      revZAxisCB.setText(b.getString("kReverseZ"));
      revZAxisCB.setEnabled(false);
    }
  }

  private void jbInit() throws Exception {
    this.setTitle(b.getString("kParameterEditor"));
    this.getContentPane().setLayout(borderLayout1);

    // create the titled borders
    paramBorder = new TitledBorder("");
    paramBorder.setTitle(b.getString("kParameters2"));
    axisBorder = new TitledBorder("");
    axisBorder.setTitle(b.getString("kAxes"));
    optionsBorder = new TitledBorder("");
    optionsBorder.setTitle(b.getString("kPlotOptions"));

    // create the buttons
    doneButton.setText(b.getString("kPlot"));
    doneButton.addActionListener(new VMapModelEditor_doneButton_actionAdapter(this));
    applyButton.setText(b.getString("kApply"));
    applyButton.addActionListener(new VMapModelEditor_applyButton_actionAdapter(this));
    closeButton.setText(b.getString("kClose"));
    closeButton.addActionListener(new VMapModelEditor_closeButton_actionAdapter(this));
    helpButton.setText(b.getString("kHelp2"));
    helpButton.addActionListener(new VMapModelEditor_helpButton_actionAdapter(this));

    // infoPanel contains the mapping summary
    infoPanel.setLayout(borderLayout2);
    infoScrollPane.setBorder(BorderFactory.createEtchedBorder());
    infoScrollPane.setMinimumSize(new Dimension(674, 105));
    infoScrollPane.setPreferredSize(new Dimension(674, 105));
    infoText.setPreferredSize(new Dimension(674, 105));
    infoText.setContentType("text/html"); // give informational message about fonts on MacOS X
    infoText.setMinimumSize(new Dimension(674, 105));
    infoText.setEditable(false);
    openButton.addActionListener(new VMapModelEditor_openButton_actionAdapter(this));
    this.getContentPane().add(infoPanel, BorderLayout.NORTH);
    infoPanel.add(infoScrollPane, BorderLayout.NORTH);
    infoScrollPane.getViewport().add(infoText, null);

    // Axes panel for the reverse buttons
    revXAxisCB.setText(b.getString("kReverseX"));
    revYAxisCB.setText(b.getString("kReverseY"));
    axesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); //gridBagLayout1);
    axesPanel.setBorder(axisBorder);
    axesPanel.add(revXAxisCB); //,
    // new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    //                        ,GridBagConstraints.CENTER,
    //                        GridBagConstraints.NONE,
    //                        new Insets(0, 5, 0, 5), 0, 0));
    axesPanel.add(revYAxisCB); //,
    //new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
    //                       ,GridBagConstraints.WEST,
    //                       GridBagConstraints.NONE,
    //                       new Insets(0, 5, 0, 5), 0, 0));
    axesPanel.add(revZAxisCB); //,
    //new GridBagConstraints(2, 0, 1, 1, 2.0, 0.0
    //                       ,GridBagConstraints.WEST,
    //                       GridBagConstraints.NONE,
    //                       new Insets(0, 5, 0, 5), 0, 0));

    // create the button panel

    if(Browser.getInstance().isOceanShare()) {
      openButton.setText("Open Items in");
      openButton.setActionCommand("Open Items in");
      jPanel1.add(openButton);
      sendCBox.setModel(sendCBoxModel);
      jPanel1.add(sendCBox);
      // get the ComboBoxModel using relfection
      try {
        Class ncSupport = Class.forName("gov.noaa.pmel.oceanshare.NcBrowseSupport");
        Method getModel = ncSupport.getMethod("getPlotListComboBoxModel", null);
        Object obj = getModel.invoke(ncSupport, null);
        sendCBox.setModel((ComboBoxModel)obj);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    } else {
      jPanel1.add(doneButton, null);
    }

    jPanel1.add(applyButton, null);
    jPanel1.add(helpButton, null);
    jPanel1.add(closeButton, null);
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);

    mainPanel.setLayout(borderLayout3);
    mainPanel.add(new TenPixelBorder(axesPanel, 5, 0, 0, 0), BorderLayout.NORTH);
    paramScrollPane.setBorder(paramBorder);
    mainPanel.add(paramScrollPane, BorderLayout.CENTER);

    // add the options panel here in south
    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new ColumnLayout(Orientation.LEFT,
                                            Orientation.CENTER, 5));
    optionsPanel.setBorder(optionsBorder);

    // add the common options for all 3D plots
    JPanel line1 = new JPanel();
    line1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    mNoLegend = new JRadioButton(b.getString("kNo3DLegend"), true);
    mCBOnlyLegend = new JRadioButton(b.getString("kColorBarLegend"));
    ButtonGroup bg = new ButtonGroup();
    bg.add(mNoLegend);
    bg.add(mCBOnlyLegend);
    mLegend = new JCheckBox(b.getString("kLegend"), false);
    line1.add(mLegend);
    //line1.add(mNoLegend);
    //line1.add(mCBOnlyLegend);

    mAdvancedButton.setText(b.getString("kAdvancedOptions2"));
    mAdvancedButton.addActionListener(new VMapModelEditor_advButton_actionAdapter(this));

    JPanel line1c = new JPanel();
    line1c.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    mBGColor = new Swatch(Color.black, 12, 12);
    mAxesColor = new Swatch(Color.white, 12, 12);
    mLabel7 = new JLabel(b.getString("kBackgroundColor2"));
    line1c.add(mLabel7);
    line1c.add(mBGColor);
    mLabel8 = new JLabel(b.getString("kAxesColor"));
    line1c.add(mLabel8);
    line1c.add(mAxesColor);
    line1.add(line1c);
    line1.add(mAdvancedButton);

    // set initial enabled state of options
    set3DColorOptions(false);
    setLegendOptState(false);

    optionsPanel.add(line1);

    mainPanel.add(optionsPanel, BorderLayout.SOUTH);

    // now add the main panel to the dialog
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);

    mMaintain = new MaintenanceTimer(this, 100);
    mMaintain.startMaintainer();
  }

  public void dialogDismissed(JDialog d) {
    mResampleAxesFlag = ((ConfigAdvancedPlotOptions) d).isResampleAxes();
    mResampleMode = ((ConfigAdvancedPlotOptions) d).getResampleMode();
    mVectorScale = ((ConfigAdvancedPlotOptions) d).getVectorScale();
    mUIncluded = ((ConfigAdvancedPlotOptions) d).isUIncluded();
    mVIncluded = ((ConfigAdvancedPlotOptions) d).isVIncluded();
    mWIncluded = ((ConfigAdvancedPlotOptions) d).isWIncluded();
    mRespectAspectRatioFlag = ((ConfigAdvancedPlotOptions) d).
        isRespectAspectRatio();
    mXScaling = ((ConfigAdvancedPlotOptions) d).getXScale();
    mYScaling = ((ConfigAdvancedPlotOptions) d).getYScale();
    mZScaling = ((ConfigAdvancedPlotOptions) d).getZScale();
  }

  // Cancel button
  public void dialogCancelled(JDialog d) {}

  // something other than the OK button
  public void dialogDismissedTwo(JDialog d) {}

  // Apply button, OK w/o dismissing the dialog
  public void dialogApply(JDialog d) {}

  // Apply button, OK w/o dismissing the dialog
  public void dialogApplyTwo(Object d) {}

  public void maintainButtons() {
    if(model_.is3D() && vizRend != null && vizRend.getDisplayWindow() != null &&
       vizRend.getDisplayWindow().isVisible()) {
      isPlotOpen = true;
    }
    if(model_.is3D() && vizRend != null && vizRend.getDisplayWindow() != null &&
       !vizRend.getDisplayWindow().isVisible()) {
      isPlotOpen = false;
    }
    if(!model_.is3D() && display_ != null && display_.isVisible()) {
      isPlotOpen = true;
    }
    if(!model_.is3D() && display_ != null && !display_.isVisible()) {
      isPlotOpen = false;
    }

    if(isPlotted && isPlotOpen) {
      doneButton.setEnabled(false);
      applyButton.setEnabled(true);
      for(int i = 0; i < vPsize; i++) {
        if(movieButton_[i] != null && movieBtnsUsed[i]) {
          movieButton_[i].setEnabled(true);
        }
      }
    } else if(isPlotted && !isPlotOpen) {
      doneButton.setEnabled(true);
      applyButton.setEnabled(false);
      for(int i = 0; i < vPsize; i++) {
        if(movieButton_[i] != null && movieBtnsUsed[i]) {
          movieButton_[i].setEnabled(false);
        }
      }
    } else {
      doneButton.setEnabled(true);
      applyButton.setEnabled(false);
      for(int i = 0; i < vPsize; i++) {
        if(movieButton_[i] != null) {
          movieButton_[i].setEnabled(false);
        }
      }
    }
    if(model_.is3D()) {
      set3DColorOptions(true);

      if(model_.hasColor() && !mLegend.isEnabled()) {
        setLegendOptState(true);
      } else if(!model_.hasColor()) {
        setLegendOptState(false);
      }
    }
  }

  public void stateChanged(ChangeEvent e) {
    if(Debug.DEBUG) {
      System.out.println("VMapGraph.stateChanged()");
    }
    if(layout_ == null) return;
    Object obj = e.getSource();
    if(obj instanceof VMapModel) {
      VMapModel vmm = (VMapModel) obj;
      if(!vmm.is3D()) {
        SGTData dobj = ((VMapModel) obj).dataset[0];
        if(dobj instanceof SGTGrid) {
          GridAttribute gAttr;
          try {
            gAttr = (GridAttribute) layout_.getAttribute(dobj);
            if(gAttr.isRaster()) {
              Range2D vRange = ((SGTGrid) dobj).getZRange();
              ColorMap cmap = gAttr.getColorMap();
              if(cmap instanceof TransformAccess) {
                ((TransformAccess) cmap).setRange(vRange);
              }
            }
          } catch(DataNotFoundException ev) {
            ev.printStackTrace();
          }
        }
        layout_.resetZoom();
        layout_.setClipping(false);
      }
    }
  }

  void closeButton_actionPerformed(ActionEvent e) {
    if(mMaintain != null) {
      mMaintain.endMaintainer();
    }
    mMaintain = null;
    setVisible(false);
  }

  void helpButton_actionPerformed(ActionEvent e) {
    JViewHTMLFrame ub = new JViewHTMLFrame("Model Editor Help");
    ub.setPage("http://www.epic.noaa.gov/java/ncBrowse/help.html");
    ub.setLocationRelativeTo(null);
    //ub.setLocation(100, 100);
    ub.setVisible(true);
  }

  void applyButton_actionPerformed(ActionEvent e) {
    updateVMapModel();
  }

  void doneButton_actionPerformed(ActionEvent e) {
    //Browser.openFileInWindows_actionPerformed(e);
    //parent_.openFileInWindows_actionPerformed(e);
    updateVMapModel();
    if(isPlotted && !isPlotOpen) {
      if(model_.is3D() && vizRend != null && vizRend.getDisplayWindow() != null) {
        vizRend.getDisplayWindow().setVisible(true);
      } else if(!model_.is3D() && display_ != null) {
        display_.setVisible(true);
      }
    } else {
      createPlot();
      //setVisible(false);
    }
  }

  void advButton_actionPerformed(ActionEvent e) {
    ConfigAdvancedPlotOptions cp = new ConfigAdvancedPlotOptions(new JFrame(), this,
        model_, mResampleAxesFlag,
        mResampleMode, mVectorScale, mUIncluded, mVIncluded, mWIncluded,
        mRespectAspectRatioFlag,
        mXScaling, mYScaling, mZScaling);
    cp.pack();
    cp.setVisible(true);
  }

  public void setJPane(JPane pane) {
    pane_ = pane;
  }

  public void setVisADRenderer(VisADRenderer rend) {
    visADRenderer_ = rend;
  }

  void updateVMapModel() {
    if(!model_.is3D()) {
      double min, max;
      if(pane_ != null) {
        pane_.setBatch(true);
        model_.setBatch(true);
      }
      for(int i = 0; i < vParam_.length; i++) {
        vParam_[i].setSingle(!rBox_[i].isSelected());
        if(isTime_[i]) {
          GeoDate start = null;
          GeoDate end = null;
          try {
            start = new GeoDate(min_[i].getText(), tFormat_);
            end = new GeoDate(max_[i].getText(), tFormat_);
          } catch(IllegalTimeValue e) {
            e.printStackTrace();
          }
          vParam_[i].setSoTRange(new SoTRange.Time(start, end));
        } else {
          SoTRange range = null;
          min = Double.parseDouble(min_[i].getText());
          max = Double.parseDouble(max_[i].getText());
          //TESTING
//          if(minRange_[i] instanceof Long) {
//            range = new SoTRange.Long(Math.round(min),
//                Math.round(max));
//          }
          if(minRange_[i] instanceof Integer) {
            range = new SoTRange.Integer((int) Math.round(min),
                                         (int) Math.round(max));
          } else if(minRange_[i] instanceof Short) {
            range = new SoTRange.Short((short) Math.round(min),
                                       (short) Math.round(max));
          } else if(minRange_[i] instanceof Float) {
            range = new SoTRange.Float((float) min, (float) max);
          } else if(minRange_[i] instanceof Double) {
            range = new SoTRange.Double(min, max);
          }
          vParam_[i].setSoTRange(range);
        }
      }
      model_.setXReversed(revXAxisCB.isSelected());
      model_.setYReversed(revYAxisCB.isSelected());
      if(model_.is3D()) {
        model_.setZReversed(revZAxisCB.isSelected());
      }
      if(pane_ != null) {
        model_.setBatch(false);
        pane_.setBatch(false);
      }
    } else {
      // update the plot options
      model_.setBatch(true);
      double min, max;
      for(int i = 0; i < vParam_.length; i++) {
        vParam_[i].setSingle(!rBox_[i].isSelected());
        if(isTime_[i]) {
          GeoDate start = null;
          GeoDate end = null;
          try {
            start = new GeoDate(min_[i].getText(), tFormat_);
            end = new GeoDate(max_[i].getText(), tFormat_);
          } catch(IllegalTimeValue e) {
            e.printStackTrace();
          }
          vParam_[i].setSoTRange(new SoTRange.Time(start, end));
        } else {
          SoTRange range = null;
          min = Double.parseDouble(min_[i].getText());
          max = Double.parseDouble(max_[i].getText());
          //TESTING
//          if(minRange_[i] instanceof Long) {
//            range = new SoTRange.Long(Math.round(min),
//                Math.round(max));
//          }

          if(minRange_[i] instanceof Integer) {
            range = new SoTRange.Integer((int) Math.round(min),
                                         (int) Math.round(max));
          } else if(minRange_[i] instanceof Short) {
            range = new SoTRange.Short((short) Math.round(min),
                                       (short) Math.round(max));
          } else if(minRange_[i] instanceof Float) {
            range = new SoTRange.Float((float) min, (float) max);
          } else if(minRange_[i] instanceof Double) {
            range = new SoTRange.Double(min, max);
          }
          vParam_[i].setSoTRange(range);
        }
      }
      model_.setXReversed(revXAxisCB.isSelected());
      model_.setYReversed(revYAxisCB.isSelected());
      if(model_.is3D()) {
        model_.setZReversed(revZAxisCB.isSelected());
      }
      model_.setVisADPlotSpecification(this.getPlotOptions());
      model_.setBatch(false);
    }
  }

  private JPanel buildParamPane() {
    JPanel panel = new JPanel();
    JLabel label;
    JPanel minGroupPanel;
    JPanel maxGroupPanel;
    GridBagConstraints bag;
    SoTRange range;
    SoTRange fullRange;
    int col;
    int len;
    //
    panel.setLayout(new GridBagLayout());
    //panel.setBounds(0, 0, 449, 205);
    //
    // construct labels
    //
    col = 0;
    //
    // name
    //
    label = new JLabel("Name");
    bag = new
        GridBagConstraints(col, 0, 1, 1, 1.0, 0.0,
                           GridBagConstraints.SOUTH,
                           GridBagConstraints.NONE, new Insets(0, 3, 0, 3),
                           0, 10);
    panel.add(label, bag);
    //
    // units
    //
    col++;
    label = new JLabel("Units");
    bag = new
        GridBagConstraints(col, 0, 1, 1, 1.0, 0.0,
                           GridBagConstraints.SOUTH,
                           GridBagConstraints.NONE, new Insets(0, 3, 0, 3),
                           0, 10);
    panel.add(label, bag);
    //
    // Range
    //
    col++;
    label = new JLabel("Range");
    bag = new
        GridBagConstraints(col, 0, 1, 1, 0.0, 0.0,
                           GridBagConstraints.SOUTH,
                           GridBagConstraints.NONE, new Insets(0, 3, 0, 3),
                           0, 10);
    panel.add(label, bag);
    //
    // Movie
    //
    col++;
    //
    // Start
    //
    col++;
    label = new JLabel("Start");
    bag = new
        GridBagConstraints(col, 0, 1, 1, 1.0, 0.0,
                           GridBagConstraints.SOUTH,
                           GridBagConstraints.NONE, new Insets(0, 3, 0, 3),
                           0, 10);
    panel.add(label, bag);
    //
    // End
    //
    col++;
    label = new JLabel("End");
    bag = new
        GridBagConstraints(col, 0, 1, 1, 1.0, 0.0,
                           GridBagConstraints.SOUTH,
                           GridBagConstraints.NONE, new Insets(0, 3, 0, 3),
                           0, 10);
    panel.add(label, bag);
    //
    // Construct Parameters
    //
    vPsize = model_.getVMapParameterCount();
    name_ = new JTextField[vPsize];
    units_ = new JTextField[vPsize];
    rBox_ = new JCheckBox[vPsize];
    min_ = new JTextField[vPsize];
    max_ = new JTextField[vPsize];
    minRange_ = new Object[vPsize];
    maxRange_ = new Object[vPsize];
    movieButton_ = new JButton[vPsize];
    movieBtnsUsed = new boolean[vPsize];
    minPopupButton_ = new JButton[vPsize];
    maxPopupButton_ = new JButton[vPsize];
    vParam_ = new VMapParameter[vPsize];
    isTime_ = new boolean[vPsize];
    rBoxGroup_ = new int[vPsize];

    Iterator ip = model_.getSortedVMapParameters();

    Insets firstInset = new Insets(8, 3, 0, 3);
    Insets middleInset = new Insets(2, 3, 0, 3);
    Insets lastInset = new Insets(2, 3, 8, 3);
    Insets first_lastInset = new Insets(8, 3, 8, 3);
    Insets boxInset = new Insets(2, 0, 2, 0);

    Insets[] inset = new Insets[vPsize];
    boolean[] first = new boolean[vPsize];
    boolean[] middle = new boolean[vPsize];
    boolean[] last = new boolean[vPsize];

    JPanel subpanel;
    int group = -1;
    int newgroup = -1;
    boolean lastsubpanel = false;
    int rows;
    for(rows = 0; rows < vPsize; rows++) {
      vParam_[rows] = (VMapParameter) ip.next();

      if(Debug.DEBUG) {
        System.out.println("row = " + rows + " " + vParam_[rows]);

      }
      newgroup = vParam_[rows].getGroup();
      rBoxGroup_[rows] = newgroup;
      first[rows] = false;
      middle[rows] = false;
      last[rows] = false;
      if(lastsubpanel) {
        rBoxGroup_[rows] = -1;
        middle[rows] = true;
      } else {
        if(rows == 0) {
          first[rows] = true;
          group = newgroup;
        } else {
          if(!vParam_[rows].isRangeAllowed()) {
            lastsubpanel = true;
            rBoxGroup_[rows] = -1;
            first[rows] = true;
            last[rows - 1] = true;
            middle[rows - 1] = false;
          }
          if(newgroup != group) {
            first[rows] = true;
            last[rows - 1] = true;
            middle[rows - 1] = false;
          } else {
            middle[rows] = true;
          }
        }
      }
    }
    //
    // set the insets
    //
    for(rows = 0; rows < vPsize; rows++) {
      if(last[rows]) {
        inset[rows] = lastInset;
      }
      if(first[rows]) {
        if(last[rows]) { // first and last set - only one
          inset[rows] = first_lastInset;
        } else {
          inset[rows] = firstInset;
        }
      }
      if(middle[rows]) {
        inset[rows] = middleInset;
      }
    }

    int gheight = 0;
    int gpos = 0;
    group = -1;
    newgroup = -1;
    lastsubpanel = false;

    for(rows = 0; rows < vPsize; rows++) {

      if(Debug.DEBUG) {
        System.out.println("Insets = " + inset[rows]);

      }
      range = vParam_[rows].getSoTRange();
      fullRange = vParam_[rows].getFullRange();
      len = vParam_[rows].getLength();
      isTime_[rows] = vParam_[rows].isTime();
      group = vParam_[rows].getGroup();

      if(Debug.DEBUG) {
        System.out.println("group = " + group + ", len = " + len);
        //
        // name
        //
      }
      col = 0;
      name_[rows] = new JTextField(vParam_[rows].getName());
      name_[rows].setEditable(false);
      bag = new
          GridBagConstraints(col, rows + 1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.HORIZONTAL,
                             inset[rows],
                             0, 0);
      panel.add(name_[rows], bag);
      //
      // units
      //
      col++;
      units_[rows] = new JTextField(vParam_[rows].getUnits());
      units_[rows].setEditable(false);
      bag = new
          GridBagConstraints(col, rows + 1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.HORIZONTAL,
                             inset[rows],
                             0, 0);
      panel.add(units_[rows], bag);
      //
      // Range
      //
      col++;
      rBox_[rows] = new JCheckBox();
      rBox_[rows].setHorizontalAlignment(SwingConstants.CENTER);
      rBox_[rows].setSelected(!vParam_[rows].isSingle());
      rBox_[rows].setEnabled(vParam_[rows].isRangeAllowed());
      rBox_[rows].addActionListener(myAction_);
      if(first[rows] && last[rows]) {
        rBox_[rows].setEnabled(false);
      }
      bag = new
          GridBagConstraints(col, rows + 1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             inset[rows],
                             0, 0);
      panel.add(rBox_[rows], bag);
      //
      // Movie
      //
      col++;
      movieButton_[rows] = new JButton();
      movieButton_[rows].setIcon(movieIcon_);
      movieButton_[rows].setMargin(new Insets(0, 0, 0, 0));
      if(vParam_[rows].isSingle()) {
        movieButton_[rows].setEnabled(len != 1);
        movieBtnsUsed[rows] = len != 1;
      } else {
        movieButton_[rows].setEnabled(false);
        movieBtnsUsed[rows] = false;
      }
      movieButton_[rows].addActionListener(myAction_);
      bag = new
          GridBagConstraints(col, rows + 1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             inset[rows],
                             0, 0);
      panel.add(movieButton_[rows], bag);
      //
      // min
      //
      col++;
      minGroupPanel = new JPanel();
      minGroupPanel.setLayout(new GridBagLayout());
      bag = new
          GridBagConstraints(col, rows + 1, 1, 1, 1.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.HORIZONTAL,
                             inset[rows],
                             0, 0);
      panel.add(minGroupPanel, bag);
      if(vParam_[rows].isDimension()) {
        minRange_[rows] = 0;
      } else {
        minRange_[rows] = fullRange.getStartObject();
      }
      min_[rows] = new JTextField(NcUtil.valueAsString(range.getStartObject()));
      min_[rows].addMouseListener(myMouse_);
      bag = new
          GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.HORIZONTAL,
                             new Insets(0, 0, 0, 0),
                             0, 0);
      minGroupPanel.add(min_[rows], bag);
      minPopupButton_[rows] = new JButton(dots_);
      if(!Browser.isSystemLF()) {
        minPopupButton_[rows].setMargin(new Insets(0, 0, 0, 0));
      }
      minPopupButton_[rows].addActionListener(myAction_);
      minPopupButton_[rows].setEnabled(len != 1);
      bag = new
          GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             new Insets(0, 0, 0, 0),
                             0, 0);
      minGroupPanel.add(minPopupButton_[rows], bag);
      //
      // max
      //
      col++;
      maxGroupPanel = new JPanel();
      maxGroupPanel.setLayout(new GridBagLayout());
      bag = new
          GridBagConstraints(col, rows + 1, 1, 1, 1.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.HORIZONTAL,
                             inset[rows],
                             0, 0);
      panel.add(maxGroupPanel, bag);
      if(vParam_[rows].isDimension()) {
        maxRange_[rows] = vParam_[rows].getLength() - 1;
      } else {
        maxRange_[rows] = fullRange.getEndObject();
      }
      max_[rows] = new JTextField(NcUtil.valueAsString(range.getEndObject()));
      max_[rows].addMouseListener(myMouse_);
      max_[rows].setEnabled(vParam_[rows].isRangeAllowed());
      bag = new
          GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.HORIZONTAL,
                             new Insets(0, 0, 0, 0),
                             0, 0);
      maxGroupPanel.add(max_[rows], bag);
      maxPopupButton_[rows] = new JButton(dots_);
      if(!Browser.isSystemLF()) {
        maxPopupButton_[rows].setMargin(new Insets(0, 0, 0, 0));
      }
      maxPopupButton_[rows].addActionListener(myAction_);
      maxPopupButton_[rows].setEnabled(vParam_[rows].isRangeAllowed());
      bag = new
          GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             new Insets(0, 0, 0, 0),
                             0, 0);
      maxGroupPanel.add(maxPopupButton_[rows], bag);
      //      rows++;
      if(first[rows]) {
        gheight = 1;
        gpos = rows + 1;
      }
      if(last[rows]) {
        subpanel = new JPanel();
        subpanel.setLayout(new GridBagLayout());
        subpanel.setBorder(new LineBorder(Color.black));

        bag = new
            GridBagConstraints(0, gpos, 6, gheight, 0.0, 0.0,
                               GridBagConstraints.CENTER,
                               GridBagConstraints.BOTH,
                               boxInset,
                               0, 0);
        panel.add(subpanel, bag);
        if(Debug.DEBUG) {
          System.out.println("Add gpos = " + gpos + ", height = " + gheight);
        }
      } else {
        gheight++;
      }

    }

    return panel;
  }

  private String getMappingAsString() {
    if(!model_.is3D()) {
      TargetMonitor tm = TargetMonitor.getInstance();
      //    VariableMap vMap = model_.getVariableMap();
      StringBuilder sbuf = new StringBuilder("<html>");
      Object obj;

      for(int i = 0; i < VMapModel.ELEMENT_COUNT; i++) {
        if(!model_.isSet(i)) {
          continue;
        }
        obj = model_.getElement(i);
        sbuf.append(VMapModel.getTitle(i)).append(" --> ");
        if(obj instanceof ucar.nc2.Dimension) {
          sbuf.append(tm.getDimensionLabel(i, model_, (ucar.nc2.Dimension) obj));
        } else {
          sbuf.append(tm.getVariableLabel(i, model_, (ucar.nc2.Variable) obj));
        }
        sbuf.append("<br>\n");
      }

      sbuf.append("</html>");
      return sbuf.toString();
    } else {
      TargetMonitor3D tm = TargetMonitor3D.getInstance();
      //    VariableMap vMap = model_.getVariableMap();
      StringBuilder sbuf = new StringBuilder("<html>");
      Object obj;

      for(int i = 0; i < VMapModel.ELEMENT_COUNT; i++) {
        if(!model_.isSet(i)) {
          continue;
        }
        obj = model_.getElement(i);
        sbuf.append(VMapModel.getTitle(i)).append(" --> ");
        if(obj instanceof ucar.nc2.Dimension) {
          sbuf.append(tm.getDimensionLabel(i, model_, (ucar.nc2.Dimension) obj));
        } else {
          sbuf.append(tm.getVariableLabel(i, model_, (ucar.nc2.Variable) obj));
        }
        sbuf.append("<br>\n");
      }

      sbuf.append("</html>");
      return sbuf.toString();
    }
  }

  class MyMouse
      extends MouseAdapter {
    public void mouseReleased(MouseEvent event) {
      Object object = event.getSource();
      if(object instanceof JTextField) {
        maybePopupTrigger(event);
      }
    }

    public void mousePressed(MouseEvent event) {
      Object object = event.getSource();
      if(object instanceof JTextField) {
        maybePopupTrigger(event);
      }
    }
  }

  void maybePopupTrigger(MouseEvent event) {
    Object obj = event.getSource();
    if(event.isPopupTrigger()) {
      for(int i = 0; i < min_.length; i++) {
        if(obj == min_[i] || obj == max_[i]) {
          Point screenLoc = ((JTextField) obj).getLocationOnScreen();
          popupDialog(i,
                      screenLoc,
                      new Point(event.getX(), event.getY()));
        }
      }
    }
  }

  void popupMovieControl(int i, Point screenLoc) {
    //MovieControl mc = new MovieControl();
    //instances.add(new java.lang.ref.WeakReference(mc));
    MovieControl mc = MovieControl.getInstance();
    mc.setMapParameter(vParam_[i]);
    mc.setJPane(pane_);
    mc.setLocation(screenLoc);
    mc.setVisible(true);
  }

  void popupDialog(int i, Point screenLoc, Point mouseLoc) {
    Point offset = new Point( -240, -220);
    boolean twoHandles = false;
    twoHandles = rBox_[i].isSelected();
    if(vParam_[i].getLength() == 1) {
      return;
    }
    if(isTime_[i]) {
      if(std_ == null) {
        std_ = new SelectTimeDialog(this);
        std_.setModal(true);
        std_.setRange(((SoTValue.Time) vParam_[i].getSoTRange().getStart()).
                      getValue(),
                      ((SoTValue.Time) vParam_[i].getSoTRange().getEnd()).
                      getValue());
      }
      std_.setTwoHandles(twoHandles);
      try {
        std_.setStartValue(new GeoDate(min_[i].getText(), tFormat_));
        if(twoHandles) {
          std_.setEndValue(new GeoDate(max_[i].getText(), tFormat_));
        }
      } catch(IllegalTimeValue e) {
        JOptionPane.showMessageDialog(this,
                                      e.toString(),
                                      "Time Format Error",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
      if(std_.showDialog(screenLoc.x + mouseLoc.x + offset.x,
                         screenLoc.y + mouseLoc.y + offset.y) ==
         SelectTimeDialog.APPROVE_OPTION) {
        if(Debug.DEBUG) {
          System.out.println("result = " +
                             std_.getStartValue() +
                             ", " + std_.getEndValue());
        }
        min_[i].setText(std_.getStartValue().toString(tFormat_));
        if(twoHandles) {
          max_[i].setText(std_.getEndValue().toString(tFormat_));
        }
      }
    } else {
      if(sdd_ == null) {
        sdd_ = new SelectDoubleDialog(this);
        sdd_.setModal(true);
      }
      sdd_.setTwoHandles(twoHandles);
      double minRange = 0.0;
      double maxRange = 0.0;
      if(minRange_[i] instanceof Long) {
        minRange = ((Long) minRange_[i]).doubleValue();
        maxRange = ((Long) maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Integer) {
        minRange = ((Integer) minRange_[i]).doubleValue();
        maxRange = ((Integer) maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Short) {
        minRange = ((Short) minRange_[i]).doubleValue();
        maxRange = ((Short) maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Float) {
        minRange = ((Float) minRange_[i]).doubleValue();
        maxRange = ((Float) maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Double) {
        minRange = (Double) minRange_[i];
        maxRange = (Double) maxRange_[i];
      }
      sdd_.setRange(minRange, maxRange);
      sdd_.setStartValue(Double.parseDouble(min_[i].getText()));
      if(twoHandles) {
        sdd_.setEndValue(Double.parseDouble(max_[i].getText()));
      }
      if(sdd_.showDialog(screenLoc.x + mouseLoc.x + offset.x,
                         screenLoc.y + mouseLoc.y + offset.y) ==
         SelectDoubleDialog.APPROVE_OPTION) {
        if(Debug.DEBUG) {
          System.out.println("result = " + sdd_.getStartValue() + ", " +
                             sdd_.getEndValue());
        }
        min_[i].setText(Double.toString(sdd_.getStartValue()));
        if(twoHandles) {
          max_[i].setText(Double.toString(sdd_.getEndValue()));
        }
      }
    }
  }

  class MyAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if(object instanceof JCheckBox) {
        checkBox_actionPerformed(event);
      } else if(object instanceof JButton) {
        button_actionPerformed(event);
      }
    }
  }

  void checkBox_actionPerformed(ActionEvent event) {
    Object box = event.getSource();
    boolean flipped = false;
    int rowChanged = -1;
    //
    // check range flag
    //
    int i;
    for(i = 0; i < rBox_.length; i++) {
      if(rBox_[i].equals(box)) {
        rowChanged = i;
        break;
      }
    }
    if((vParam_[rowChanged].getLength() != 1) &&
       rBox_[rowChanged].isSelected()) {
      max_[rowChanged].setEnabled(true);
    } else {
      max_[rowChanged].setEnabled(false);
    }
    //
    // check if other in group needs to be set
    // if so set the first one in the group
    //
    int group = rBoxGroup_[i];
    boolean isset = false;
    int setRow = -1;
    for(i = 0; i < rBox_.length; i++) {
      if((rBoxGroup_[i] == group) && rBox_[i].isSelected()) {
        isset = true;
        setRow = i;
        break;
      }
    }
    // if change was to select a row show it as set
    if(rBox_[rowChanged].isSelected()) {
      setRow = rowChanged;
      // otherwise use the first selected row

    }
    if(!isset) {
      // at least one must be set
      for(i = 0; i < rBox_.length; i++) {
        if(i == rowChanged) {
          continue;
        }
        if(rBoxGroup_[i] == group) {
          rBox_[i].setSelected(true);
          if((vParam_[i].getLength() != 1)) {
            max_[i].setEnabled(true);
          } else {
            max_[i].setEnabled(false);
          }
          return;
        }
      }
    } else if(model_.isGridData() || model_.isVectorData()) {
      // only one can be set
      for(i = 0; i < rBox_.length; i++) {
        if(i == setRow) {
          continue;
        }
        if(rBoxGroup_[i] == group) {
          rBox_[i].setSelected(false);
          if((vParam_[i].getLength() != 1)) {
            max_[i].setEnabled(true);
          } else {
            max_[i].setEnabled(false);
          }
        }
      }
    }
    // check to see if movieButton_ should be enabled or disabled
    // enable if the range has been disabled for a box in the group
    for(i = 0; i < rBox_.length; i++) {
      if(rBoxGroup_[i] == group) {
        movieButton_[i].setEnabled(!rBox_[i].isSelected());
      }
    }
  }

  void button_actionPerformed(ActionEvent event) {
    JButton obj = (JButton) event.getSource();
    Point screenLoc = obj.getLocationOnScreen();
    for(int i = 0; i < minPopupButton_.length; i++) {
      if(obj == minPopupButton_[i] || obj == maxPopupButton_[i]) {
        popupDialog(i, screenLoc, new Point(0, 0));
        return;
      }
      if(obj == movieButton_[i]) {
        popupMovieControl(i, screenLoc);
        return;
      }
    }
  }

  public void createPlot() {
//    String name = ncFile_.getFileName();
    subset_ = model_.getSGTData();
//    String message;
    if(!model_.is3D()) {
      //
      // create graph frame
      //
      // message = "Creating graphics frame for " + model_.getName() +
      //  " from " + name;
      // oPane.setMessage(message);
      //String title1 = ncFile_.getFileName().trim();
      layout_ = new JPlotLayout(subset_[0], "ncBrowse", null, true);
      //layout_.setPreferredSize(new Dimension(VariableWindows.winSizeWidth,VariableWindows.winSizeHeight));
      //
      //    layout_.setBatch(true, "VMapGraph");
      //
      //String lname = model_.getName();
      //layout_.setTitles(title1, lname.trim(), " ");
      //layout_.setLayerSizeP(new Dimension2D(6.0, 5.0));
      //layout_.setTitleHeightP(0.25, 0.20);
      //System.out.println("Before: " + Browser.getInstance().getInt());
      makeFrame();
      //VariableWindows.setAllVisible();
      //win = win + 1;
     //VariableWindows2.incrementFrame();
      Browser.getInstance().setInt((Browser.getInstance().getInt() + 1));
      //System.out.println("After: "+ Browser.getInstance().getInt());
      //System.out.println(win += 1);
      //win = +1;
//      if (!win1){
//        win2 = true;
//      }
      display_.setSize(VariableWindows.winSizeWidth, VariableWindows.winSizeHeight);
//      if (win1){
//        display_.setLocation(VariableWindows.getVarWin1Frame().getX(), VariableWindows.getVarWin1Frame().getY());
//        win1 = false;
//      }
//      else if ((win2)&&(!win1)){
//        display_.setLocation(VariableWindows.getVarWin2Frame().getX(), VariableWindows.getVarWin2Frame().getY());
//        win2 = false;
//      }
      //display_.setLocation(VariableWindows.getVarWin1Frame().getX(), VariableWindows.getVarWin1Frame().getY());
      //parent_.getWindowList().addElement(display_);
      //display_.addWindowListener(parent_.getWindowList());
      //      layout_.addData(subset_, subset_.getTitle());
      Attribute attr = null;
      if(subset_[0] instanceof SGTLine) {
      } else if(subset_[0] instanceof SGTGrid) {
      } else if(subset_[0] instanceof SGTVector) {
        SGTVector vec = (SGTVector) subset_[0];
        Range2D uRange = vec.getU().getZRange();
        Range2D vRange = vec.getV().getZRange();
        double umax = Math.max(Math.abs(uRange.start), Math.abs(uRange.end));
        double vmax = Math.max(Math.abs(vRange.start), Math.abs(vRange.end));
        // scale = (physical units)/(user units)
        double scale = 0.3 / Math.sqrt(umax * umax + vmax * vmax);
        // make a "nice" scale
        double temp = scale;
        int nt = (int) (0.4342944819 * Math.log(temp));
        if(temp < 1.0) {
          nt--;
        }
        double pow = Math.pow(10.0, (double) nt);
        temp = temp / pow;
        if(temp < 1.414213562) {
          scale = pow;
        } else if(temp < 3.162277660) {
          scale = 2.0 * pow;
        } else if(temp < 7.071067812) {
          scale = 5.0 * pow;
        } else {
          scale = 10.0 * pow;
        }
        attr = new VectorAttribute(VectorAttribute.SCALED_HEAD,
                                   scale,
                                   Color.black,
                                   0.25);
      }

      layout_.addData(subset_[0], attr);
      //    layout_.draw();
      //
      model_.getModelEditor().setJPane(layout_);
      //
      layout_.setBatch(false, "VMapGraph");
      //
      if(keyPane_ != null) {
        //      keyPane_.draw();
        //
        keyPane_.setBatch(false, "VMapGraph");
        //
      }
      display_.setVisible(true);
    } else { // is3D()
      if(model_.is3DGridData()) {
        subset_ = model_.getSGTData();
        vizRend = new VisADSurfaceRenderer(subset_[0], subset_[1], model_,
                                           getPlotOptions());

        // want to plug in other VisAD renderers here
        // need to know what kind of 3D plot we have here
        try {
          vizRend.draw();
        } catch(Exception ex) {
          ex.printStackTrace();
        }
      } else if(!model_.isVolumeData() && model_.is3DLineData()) {
        subset_ = model_.getSGTData();
        // just pass in the same subset for color for now
        vizRend = new VisAD3DLineRenderer(subset_[0], subset_[1], model_,
                                          getPlotOptions());

        // want to plug in other VisAD renderers here
        // need to know what kind of 3D plot we have here
        try {
          vizRend.draw();
        } catch(Exception ex) {
          ex.printStackTrace();
        }
      } else if(model_.is3DVectorData()) {
        subset_ = model_.getSGTData();
        if(!model_.isFull3DVectorData()) {
          vizRend = new VisAD3DVectorRenderer(subset_[0], null, model_);
        } else {
          vizRend = new VisADFull3DVectorRenderer(subset_[0], model_,
                                                  getPlotOptions());
        }
        try {
          vizRend.draw();
        } catch(Exception ex) {
          ex.printStackTrace();
        }
      } else if(model_.isVolumeData()) {
        subset_ = model_.getSGTData();
        vizRend = new VisAD3DVolumeRenderer((ThreeDGrid) subset_[0], model_,
                                            getPlotOptions());
        //vizRend = new VisAD3DVolumeVoxelRenderer((ThreeDGrid)subset_[0], model_, getPlotOptions());
        try {
          vizRend.draw();
        } catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    isPlotted = true;
  }

  public VisADPlotSpecification getPlotOptions() {
    VisADPlotSpecification spec = new VisADPlotSpecification();
    if(!mLegend.isSelected()) {
      spec.setLegendStyle(VisADPlotSpecification.NO_LEGEND);
    } else
    /*if (mCBOnlyLegend.isSelected())*/ {
      spec.setLegendStyle(VisADPlotSpecification.COLOR_LEGEND);

    }
    spec.setResampleAxes(mResampleAxesFlag);
    spec.setSamplingMode(mResampleMode);
    spec.setUseU(mUIncluded);
    spec.setUseV(mVIncluded);
    spec.setUseW(mWIncluded);
    spec.setVectorScale((float) mVectorScale);

    if(mRespectAspectRatioFlag) {
      spec.setRespectDataAspectRatio(true);
      spec.setXAxisScaling((float) mXScaling);
      spec.setYAxisScaling((float) mYScaling);
      spec.setZAxisScaling((float) mZScaling);
    } else {
      spec.setRespectDataAspectRatio(false);
      spec.setXAxisScaling(1.0f);
      spec.setYAxisScaling(1.0f);
      spec.setZAxisScaling(1.0f);
    }

    spec.setBGColor(mBGColor.getColor());
    spec.setAxesColor(mAxesColor.getColor());
    return spec;
  }

  private void setLegendOptState(boolean state) {
    mLegend.setEnabled(state);
    //mNoLegend.setEnabled(state);
    //mCBOnlyLegend.setEnabled(state);
  }

  private void set3DColorOptions(boolean state) {
    mBGColor.setEditable(state);
    mAxesColor.setEditable(state);
    mLabel7.setEnabled(state);
    mLabel8.setEnabled(state);
  }

  public void setLocation(int x, int y) {
    x_ = x;
    y_ = y;
  }

  private void makeFrame() {
    //boolean win1 = true;
    //boolean win2 = true;
    //win = VariableWindows.getVarWin1Frame().getTitle();
//    switch (win){
//      case "Variable Window 1": display_ = VariableWindows.getVarWin1Frame();
//                                display_.setLocation(VariableWindows.getVarWin1Frame().getX(), VariableWindows.getVarWin1Frame().getY());
//                                win = "OneDone";
//                                break;
//      case "OneDone":  display_ = VariableWindows.getVarWin2Frame();
//                        display_.setLocation(VariableWindows.getVarWin2Frame().getX(), VariableWindows.getVarWin2Frame().getY());
//        win = "TwoDone";
//                        break;
//      default: win = "";
//        break;
//    }
//    System.out.println(win);
    switch (win){
      case 1: display_ = VariableWindows.variableWindow1;
              display_.setLocation(VariableWindows.variableWindow1.getX(), VariableWindows.variableWindow1.getY());
              //System.out.println("Case 1");
              display_.setVisible(true);
              break;
      case 2: display_ = VariableWindows.variableWindow2;
              display_.setLocation(VariableWindows.variableWindow2.getX(), VariableWindows.variableWindow2.getY());
              //System.out.println("Case 2");
              display_.setVisible(true);
              break;
      case 3: display_ = VariableWindows.variableWindow3;
              display_.setLocation(VariableWindows.variableWindow3.getX(), VariableWindows.variableWindow3.getY());
              //System.out.println("Case 3");
              display_.setVisible(true);
              break;
      case 4: display_ = VariableWindows.variableWindow4;
              display_.setLocation(VariableWindows.variableWindow4.getX(), VariableWindows.variableWindow4.getY());
              //System.out.println("Case 4");
              display_.setVisible(true);
              break;
      case 5: display_ = VariableWindows.variableWindow5;
              display_.setLocation(VariableWindows.variableWindow5.getX(), VariableWindows.variableWindow5.getY());
              //System.out.println("Case 5");
              display_.setVisible(true);
              break;
      case 6: display_ = VariableWindows.variableWindow6;
              display_.setLocation(VariableWindows.variableWindow6.getX(), VariableWindows.variableWindow6.getY());
              //System.out.println("Case 6");
              display_.setVisible(true);
              break;
      case 7: display_ = VariableWindows.variableWindow7;
              display_.setLocation(VariableWindows.variableWindow7.getX(), VariableWindows.variableWindow7.getY());
              //System.out.println("Case 7");
              display_.setVisible(true);
              break;
      case 8: display_ = VariableWindows.variableWindow8;
              display_.setLocation(VariableWindows.variableWindow8.getX(), VariableWindows.variableWindow8.getY());
              //System.out.println("Case 8");
              display_.setVisible(true);
              break;
      default: System.out.println("Failed!");
               break;
    }
    //win = VariableWindows.getVarWin1Frame().getTitle();
//    System.out.println("Step 1");
//    if(win1){
//      System.out.println("Step 2: win1 true");
//      display_ = VariableWindows.getVarWin1Frame();
//      display_.setLocation(VariableWindows.getVarWin1Frame().getX(), VariableWindows.getVarWin1Frame().getY());
//      win1 = false;
//      System.out.println("Step 3 win1 false");
//    }
//    if (win2){
//      System.out.println("Step 4: win2 true");
//      display_ = VariableWindows.getVarWin2Frame();
//      display_.setLocation(VariableWindows.getVarWin2Frame().getX(), VariableWindows.getVarWin2Frame().getY());
//      win2 = false;
//      System.out.println("Step 5: win2 false");
//    }
    lSymAction_ = new SymAction();

    // set the window name
    String name = ncFile_.getFileName();
    String s = model_.getName();
     // display_ = varWins.variableWindow1;
      //display_ = VariableWindows.getVarWin1Frame();
    //NEVER HAVE THIS NEXT LINE ON!
    //display_ = new JFrame(s + " from " + name);
    display_.getContentPane().setLayout(new BorderLayout(0, 0));
    display_.setJMenuBar(makeMenuBar());
    //
    keyPane_ = layout_.getKeyPane();
    //    layout_.setBatch(true, "VMapGraph");
    if(keyPane_ != null) {
      //      keyPane_.setBatch(true, "VMapGraph");
        //graphPane_ = varWins.controlPanel1;
      graphPane_ = new JPanel();
      keyPanel_ = new JPanel();

      graphPane_.setLayout(new BorderLayout(0, 0));
      display_.getContentPane().add(graphPane_, BorderLayout.CENTER);
      graphPane_.setBackground(new Color(204, 204, 204));
      //graphPane_.setBounds(0, 4, 488, 669);
      graphPane_.add(layout_, BorderLayout.CENTER);
      //
      keyPanel_.setLayout(new BorderLayout());
      keyPanel_.setBorder(new LineBorder(Color.gray, 2));
      keyPane_.setSize(new Dimension(VariableWindows.winSizeWidth, 50));
      //keyPane_.setSize(VariableWindows.WIDTH, VariableWindows.HEIGHT);
      layout_.setKeyLayerSizeP(new Dimension2D(6.7, 1.25));
      layout_.setKeyBoundsP(new Rectangle2D.Double(0.0, 1.25, 6.7, 1.25));
      keyPanel_.add(keyPane_, BorderLayout.CENTER);
      graphPane_.add(keyPanel_, BorderLayout.SOUTH);
      //
      layout_.setBackground(Color.white);
      myMouse_ = new MyMouse();
      keyPane_.addMouseListener(myMouse_);
    } else {
      display_.getContentPane().add(layout_, BorderLayout.CENTER);
    }

    //
    SymWindow aSymWindow = new SymWindow();
    display_.addWindowListener(aSymWindow);
    //
  }

  JMenuBar makeMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JSeparator separator;

    JMenu fileMenu = new JMenu();
    fileMenu.setText("File");
    fileMenu.setMnemonic((int) 'F');
    menuBar.add(fileMenu);
    printMenuItem_ = new JMenuItem();
    printMenuItem_.setText("Print...");
    printMenuItem_.setMnemonic((int) 'P');
    fileMenu.add(printMenuItem_);
    separator = new JSeparator();
    fileMenu.add(separator);
    exitMenuItem_ = new JMenuItem();
    exitMenuItem_.setText("Exit");
    exitMenuItem_.setMnemonic((int) 'X');
    fileMenu.add(exitMenuItem_);

    JMenu editMenu = new JMenu();
    editMenu.setText("Edit");
    editMenu.setMnemonic((int) 'E');
    menuBar.add(editMenu);
    mapMenuItem_ = new JMenuItem();
    mapMenuItem_.setText("Variable Map...");
    mapMenuItem_.setMnemonic((int) 'V');
    editMenu.add(mapMenuItem_);

    JMenu viewMenu = new JMenu();
    viewMenu.setText("View");
    viewMenu.setMnemonic((int) 'V');
    menuBar.add(viewMenu);
    resetZoomMenuItem_ = new JMenuItem();
    resetZoomMenuItem_.setText("Reset Zoom");
    resetZoomMenuItem_.setMnemonic((int) 'Z');
    viewMenu.add(resetZoomMenuItem_);
    separator = new JSeparator();
    viewMenu.add(separator);
    classTreeMenuItem_ = new JMenuItem();
    classTreeMenuItem_.setText("Class Tree...");
    classTreeMenuItem_.setMnemonic((int) 'C');
    viewMenu.add(classTreeMenuItem_);

    exitMenuItem_.addActionListener(lSymAction_);
    printMenuItem_.addActionListener(lSymAction_);
    mapMenuItem_.addActionListener(lSymAction_);
    resetZoomMenuItem_.addActionListener(lSymAction_);
    classTreeMenuItem_.addActionListener(lSymAction_);

    return menuBar;
  }

  class SymWindow
      extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
//      thisIsOpen = false;
      Object object = event.getSource();
      if(object == display_) {
        display_WindowClosing(event);
      }
    }
  }

  void display_WindowClosing(WindowEvent event) {
    // turn off the btn maintainer thread
    display_.setVisible(false);
    //display_.dispose();		 // dispose of the Frame.
  }

  class SymAction
      implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if(object == exitMenuItem_) {
        exitMenuItem_actionPerformed(event);
      } else if(object == printMenuItem_) {
        printMenuItem_actionPerformed(event);
      } else if(object == mapMenuItem_) {
        mapMenuItem_actionPerformed(event);
      } else if(object == resetZoomMenuItem_) {
        resetZoomMenuItem_actionPerformed(event);
      } else if(object == classTreeMenuItem_) {
        classTreeMenuItem_actionPerformed(event);
      }
    }
  }

  void exitMenuItem_actionPerformed(ActionEvent event) {
    display_.setVisible(false);
    //display_.dispose();
  }

  void printMenuItem_actionPerformed(ActionEvent event) {
    Color saveColor;

    if(Debug.DEBUG) {
      printSizes();
    }
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable(layout_);
    printJob.setJobName("ncBrowse");
    if(printJob.printDialog()) {
      try {
        saveColor = layout_.getBackground();
        if(!saveColor.equals(Color.white)) {
          layout_.setBackground(Color.white);
        }
        layout_.setPageAlign(AbstractPane.TOP,
                             AbstractPane.CENTER);
        RepaintManager currentManager = RepaintManager.currentManager(layout_);
        currentManager.setDoubleBufferingEnabled(false);
        printJob.print();
        currentManager.setDoubleBufferingEnabled(true);
        layout_.setBackground(saveColor);
      } catch(PrinterException e) {
        System.out.println("Error printing: " + e);
      }
    }
  }

  void mapMenuItem_actionPerformed(ActionEvent event) {
    model_.getModelEditor().setVisible(true);
  }

  void resetZoomMenuItem_actionPerformed(ActionEvent event) {
    layout_.resetZoom();
    layout_.setClipping(false);
  }

  void classTreeMenuItem_actionPerformed(ActionEvent event) {
    JClassTree ct = new JClassTree();
    ct.setModal(false);
    ct.setJPane(layout_);
    ct.setVisible(true);
  }

  void printSizes() {
    System.out.println("");
    System.out.println("display_; " + display_.getSize());
    System.out.println("contentPane: " + display_.getContentPane().getBounds());
    System.out.println("graphPane: " + graphPane_.getBounds());
    System.out.println("keyPanel: " + keyPanel_.getBounds());
    System.out.println("layout_: " + layout_.getSize());
    System.out.println("layer: " + layout_.getLayerSizeP());
    System.out.println("keyPane_: " + keyPane_.getSize());
    System.out.println("keyPane(layer): " + layout_.getKeyLayerSizeP());
  }

  void openButton_actionPerformed(ActionEvent e) {
    updateVMapModel();
    Collection coll = new Collection("EPS data collection");
    SGTData subset = null;
    int pwindex = sendCBox.getSelectedIndex();
    Class ncSupport = null;
    Method addData = null;
    Method newPlot = null;
    try {
      Class[] addDataArgs = {int.class, SGTData.class};
      Class[] newPlotArgs = {String.class, String.class, String.class, String.class,
          String.class, SGTData.class, SGTLine.class};
      ncSupport = Class.forName("gov.noaa.pmel.oceanshare.NcBrowseSupport");
      addData = ncSupport.getMethod("addData", addDataArgs);
      newPlot = ncSupport.getMethod("newPlot", newPlotArgs);
    } catch(Exception ex) {
      ex.printStackTrace();
      return;
    }
    SGTData[] data = model_.getSGTData();
    if(data[0] instanceof SGTLine) {
      coll.add(data[0]);
      subset = coll;
    } else {
      subset = data[0];
    }
    if(pwindex <= 0) {
      epCount_++;
      String frameName = "ncBrowse";
      String path = ncFile_.getPathName();
      String title1 = path.substring(path.lastIndexOf("/") + 1);
      String title2 = model_.getName().trim();
      String title3 = " ";
      //
      try {
        Object[] npArgs = {null, frameName, title1, title2, title3, subset, null};
        newPlot.invoke(ncSupport, npArgs);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    } else {
      try {
        Object[] adArgs = {pwindex, subset};
        addData.invoke(ncSupport, adArgs);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}

class VMapModelEditor_closeButton_actionAdapter
    implements java.awt.event.ActionListener {
  final VMapModelEditor adaptee;

  VMapModelEditor_closeButton_actionAdapter(VMapModelEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.closeButton_actionPerformed(e);
  }
}

class VMapModelEditor_helpButton_actionAdapter implements ActionListener {
  final VMapModelEditor adaptee;

  VMapModelEditor_helpButton_actionAdapter(VMapModelEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.helpButton_actionPerformed(e);
  }
}

class VMapModelEditor_applyButton_actionAdapter
    implements java.awt.event.ActionListener {
  final VMapModelEditor adaptee;

  VMapModelEditor_applyButton_actionAdapter(VMapModelEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.applyButton_actionPerformed(e);
  }
}

class VMapModelEditor_doneButton_actionAdapter implements ActionListener {
  final VMapModelEditor adaptee;

  VMapModelEditor_doneButton_actionAdapter(VMapModelEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.doneButton_actionPerformed(e);
  }
}

class VMapModelEditor_advButton_actionAdapter implements ActionListener {
  final VMapModelEditor adaptee;

  VMapModelEditor_advButton_actionAdapter(VMapModelEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.advButton_actionPerformed(e);
  }
}

class VMapModelEditor_openButton_actionAdapter implements ActionListener {
  final VMapModelEditor adaptee;

  VMapModelEditor_openButton_actionAdapter(VMapModelEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.openButton_actionPerformed(e);
  }
}
