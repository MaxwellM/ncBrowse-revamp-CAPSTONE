/**
 *  $Id: DomainSelector.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import gov.noaa.pmel.sgt.dm.Collection;
import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTLine;
import gov.noaa.pmel.swing.SelectDoubleDialog;
import gov.noaa.pmel.swing.SelectTimeDialog;
import gov.noaa.pmel.util.GeoDate;
import gov.noaa.pmel.util.IllegalTimeValue;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Provides an interface for setting the domain for extracting data
 * from a netCDF file.
 *
 * @author Donald Denbo
 * @version $Revision: 1.40 $, $Date: 2006/02/15 22:33:23 $
 */
public class DomainSelector extends JFrame implements Runnable {
  private int processType_;
  private String variable_;

  /**
   * @label ncFile_
   */
  NcFile ncFile_;

  /**
   * @label ncVar_
   */
  Variable ncVar_;

  /**
   * @label dims_
   */
  private ucar.nc2.Dimension[] dims_;
  private ImageIcon dots_ = new ImageIcon(getClass().getResource("images/3Dots16.gif"),"Align Bottom");
  Object[] dimOrVar_;
  JCheckBox[] xBox_;
  JCheckBox[] yBox_;
  JCheckBox[] revBox_;
  private JTextField[] name_;
  private JTextField[] units_;
  JTextField[] min_;
  JTextField[] max_;
  private JButton[] minPopupButton_;
  private JButton[] maxPopupButton_;
  private Object[] minRange_;
  private Object[] maxRange_;
  boolean[] isTime_;
  private boolean[] isRange_;
  int rank_ = 0;
  private MyAction myAction_;
  private MyMouse myMouse_;
  String tFormat_ = "yyyy-MM-dd HH:mm:ss";

  /**
   * @label parent_
   */
  private Browser parent_;

  /**
   * @link aggregation
   * @supplierCardinality 1
   * @undirected
   */
  private SelectDoubleDialog sdd_ = null;

  private static int epCount_ = 0;

  /**
   * @link aggregation
   * @supplierCardinality 1
   * @undirected
   */
  private SelectTimeDialog std_ = null;
  private boolean isGraph_ = false;
  private boolean isCDL_ = false;
  private boolean isUNH_ = false;
  private boolean isText_ = false;
  private int x_ = 50;
  private int y_ = 50;

  public DomainSelector() {
    getContentPane().setLayout(new BorderLayout(0,0));
    setSize(677,350);
    setVisible(false);
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,25,5));
    getContentPane().add(BorderLayout.SOUTH,buttonPanel);
    buttonPanel.setBounds(0,297,677,35);

    if(Browser.getInstance().isOceanShare()) {
      openButton.setText("Open Items in");
      openButton.setActionCommand("Open Items in");
      buttonPanel.add(openButton);
      sendCBox.setModel(sendCBoxModel);
      buttonPanel.add(sendCBox);
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
      actionButton.setText("Action");
      buttonPanel.add(actionButton);
      actionButton.setBounds(341, 5, 71, 25);
    }

    closeButton.setText("Close");
    closeButton.setActionCommand("Close");
    buttonPanel.add(closeButton);
    closeButton.setBounds(265,5,51,25);

    mainPanel.setLayout(new BorderLayout(0,0));
    getContentPane().add(BorderLayout.CENTER,mainPanel);
    mainPanel.setBounds(0,0,677,297);
    infoScrollPane.setOpaque(true);
    mainPanel.add(BorderLayout.NORTH,infoScrollPane);
    infoScrollPane.setBounds(0,0,677,108);
    infoText.setEditable(false);
    infoText.setRows(7);
    infoText.setTabSize(2);
    infoScrollPane.getViewport().add(infoText);
    infoText.setBounds(0,0,674,105);
    axesBorder = new TitledBorder("Axes");
    axesScrollPane.setBorder(axesBorder);
    mainPanel.add(BorderLayout.CENTER,axesScrollPane);
    axesScrollPane.setBounds(0,108,677,189);

    setTitle("A Simple Frame");

    myAction_ = new MyAction();
    myMouse_ = new MyMouse();

    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    closeButton.addActionListener(myAction_);
    actionButton.addActionListener(myAction_);
    openButton.addActionListener(myAction_);

  }

  public DomainSelector(String title) {
    this();
    setTitle(title);
  }

  public void setVisible(boolean b, int x, int y) {
    setLocation(x, y);
    x_ = x;
    y_ = y;
    super.setVisible(b);
  }

  public void setVisible(boolean b) {
    if(b) {
      setLocation(x_, y_);
    }
    super.setVisible(b);
  }

  public void addNotify() {
    // Record the size of the window prior to calling parents addNotify.
    Dimension d = getSize();

    super.addNotify();

    if (fComponentsAdjusted)
      return;

    // Adjust components according to the insets
    Insets ins = getInsets();
    setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
    Component components[] = getContentPane().getComponents();
    for (int i = 0; i < components.length; i++) {
      Point p = components[i].getLocation();
      p.translate(ins.left, ins.top);
      components[i].setLocation(p);
    }
    fComponentsAdjusted = true;
  }

  // Used for addNotify check.
  boolean fComponentsAdjusted = false;
  class SymWindow extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
      Object object = event.getSource();
      if (object == DomainSelector.this)
        DomainSelector_WindowClosing(event);
    }
  }

  void DomainSelector_WindowClosing(WindowEvent event) {
    dispose();           // dispose of the Frame.
  }

  public void setParent(Browser parent) {
    parent_ = parent;
  }

  public void setTimeFormat(String format) {
    tFormat_ = format;
  }

  public String getTimeFormat() {
    return tFormat_;
  }

  public void setActionButton(String name, int processType) {
    processType_ = processType;
    actionButton.setText(name);
    isText_ = processType_ == VariableProcessThread.TEXTUAL;
    isGraph_ = processType_ == VariableProcessThread.GRAPHER;
    isCDL_ = processType_ == VariableProcessThread.EXPORT_CDL;
    isUNH_ = processType_ == VariableProcessThread.EXPORT_UNH;
  }
  public String getVariable() {
    return variable_;
  }
  public void setVariable(NcFile file, String variable) {
    variable_ = variable;
    ncFile_ = file;
  }
  public void setPosition(int x, int y) {
    x_ = x;
    y_ = y;
    setLocation(x,y);
  }

  public void run() {
    String name = ncFile_.getFileName();
    //
    // open Informational Dialog
    //
    JOptionPane oPane = new JOptionPane("Reading Axes for " + variable_ +
                                        " from " + name,
                                        JOptionPane.INFORMATION_MESSAGE,
                                        JOptionPane.DEFAULT_OPTION);
    JDialog dPane = oPane.createDialog(parent_, "Information");
    dPane.setModal(false);
    dPane.setVisible(true);
    //
    setTitle(variable_ + " from " + name + " (Domain Selector)");
    ncVar_ = ncFile_.findVariable(variable_);
    rank_ = ncVar_.getRank();
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(ncVar_.toString());
    infoText.setText(sbuf.toString());
    //
    JPanel axes_ = makeAxesPanel();
    axesScrollPane.getViewport().add(axes_);
    //
    setVisible(true);
    //
    // close Informational Dialog
    //
    dPane.setVisible(false);
    dPane.dispose();
  }

  public NcFile getNcFile() {
    return ncFile_;
  }

  public JPanel makeAxesPanel() {
    JLabel label;
    JPanel xyPanel;
    JLabel depenLabel;
    JLabel varLabel;
    JLabel xLabel;
    JLabel yLabel;
    GridBagConstraints bag;
    Attribute attr;
    int len;
    int col;
    String value;
    String dimUnits;
    JPanel xyGroupPanel;
    JPanel minGroupPanel;
    JPanel maxGroupPanel;
    boolean noVar = false;

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.setBounds(0,0,449,205);
    //
    // construct labels
    //
    col = 0;
    //
    // name
    //
    label = new JLabel("Name");
    bag = new
      GridBagConstraints(col, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
                         GridBagConstraints.NONE, new Insets(0,3,0,3),
                         0, 10);
    panel.add(label, bag);
    //
    // units
    //
    col++;
    label = new JLabel("Units");
    bag = new
      GridBagConstraints(col, 0, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
                         GridBagConstraints.NONE, new Insets(0,3,0,3),
                         0, 10);
    panel.add(label, bag);
    if(isGraph_) {
      //
      // Dependent variable
      //
      col++;
      xyPanel = new JPanel();
      xyPanel.setLayout(new GridBagLayout());
      bag = new
        GridBagConstraints(col, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.NONE, new Insets(0,3,0,3),
                           0, 10);
      panel.add(xyPanel, bag);
      depenLabel = new JLabel("Dependent");
      depenLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(depenLabel, bag);
      varLabel = new JLabel("Variable");
      varLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(varLabel, bag);
      xLabel = new JLabel("x");
      xLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(xLabel, bag);
      yLabel = new JLabel("y");
      yLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(yLabel, bag);
      //
      // reverse axis
      //
      col++;
      label = new JLabel("Reverse");
      label.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(col, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
                           GridBagConstraints.NONE, new Insets(0,3,0,3),
                           0, 10);
      panel.add(label, bag);
    } else if(isCDL_ || isUNH_) {
      col++;
      xyPanel = new JPanel();
      xyPanel.setLayout(new GridBagLayout());
      bag = new
        GridBagConstraints(col, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.NONE, new Insets(0,3,0,3),
                           0, 10);
      panel.add(xyPanel, bag);
      depenLabel = new JLabel("Use");
      depenLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(depenLabel, bag);
      varLabel = new JLabel("Range");
      varLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(varLabel, bag);
    } else if(isText_) {
      col++;
      xyPanel = new JPanel();
      xyPanel.setLayout(new GridBagLayout());
      bag = new
        GridBagConstraints(col, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.NONE, new Insets(0,3,0,3),
                           0, 10);
      panel.add(xyPanel, bag);
      depenLabel = new JLabel("Show as");
      depenLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(depenLabel, bag);
      varLabel = new JLabel("String");
      varLabel.setHorizontalAlignment(SwingConstants.CENTER);
      bag = new
        GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                           0, 0);
      xyPanel.add(varLabel, bag);
    }
    //
    // minimum
    //
    col++;
    label = new JLabel("Start");
    bag = new
      GridBagConstraints(col, 0, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
                         GridBagConstraints.NONE, new Insets(0,3,0,3),
                         0, 10);
    panel.add(label, bag);
    //
    // maximum
    //
    col++;
    label = new JLabel("End");
    bag = new
      GridBagConstraints(col, 0, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
                         GridBagConstraints.NONE, new Insets(0,3,0,3),
                         0, 10);
    panel.add(label, bag);
    //
    // construct variables
    //
    Iterator di = ncVar_.getDimensions().iterator();
    dims_ = new ucar.nc2.Dimension[rank_];
    dimOrVar_ = new Object[rank_];
    xBox_ = new JCheckBox[rank_];
    yBox_ = new JCheckBox[rank_];
    revBox_ = new JCheckBox[rank_];
    name_ = new JTextField[rank_];
    units_ = new JTextField[rank_];
    min_ = new JTextField[rank_];
    max_ = new JTextField[rank_];
    minPopupButton_ = new JButton[rank_];
    maxPopupButton_ = new JButton[rank_];
    minRange_ = new Object[rank_];
    maxRange_ = new Object[rank_];
    isTime_ = new boolean[rank_];
    isRange_ = new boolean[rank_];

    int rows = 0;
    while(di.hasNext()) {
      isRange_[rows] = false;
      ucar.nc2.Dimension dim = (ucar.nc2.Dimension)di.next();
//      Variable var = dim.getCoordinateVariable();
      Variable var = ncFile_.findVariable(dim.getName());
      /**
       * @todo fix char dimension handling
       */
      if(var == null || var.getRank() > 1) {  // use index if char dimension
        noVar = true;
        dimOrVar_[rows] = dim;
        len = dim.getLength();
      } else {
        noVar = false;
        dimOrVar_[rows] = var;
        len = (int)var.getSize();
      }
      //
      // name
      //
      col = 0;
      if(noVar) {
        name_[rows] = new JTextField(dim.getName());
      } else {
        name_[rows] = new JTextField(var.getName());
      }
      name_[rows].setEditable(false);
      bag = new
        GridBagConstraints(col, rows+1, 1, 1, 0.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL,
                           new Insets(3,3,0,3),
                           0, 0);
      panel.add(name_[rows], bag);
      //
      // units
      //
      col++;
      if(noVar) {
        dimUnits = "(index)";
        isTime_[rows] = false;
      } else {
        attr = var.findAttribute("units");
        if(attr != null) {
          dimUnits = attr.getStringValue();
        } else {
          dimUnits = " ";
        }
        isTime_[rows] = ncFile_.isVariableTime(var);
      }
      units_[rows] = new JTextField(dimUnits);
      units_[rows].setEditable(false);
      bag = new
        GridBagConstraints(col, rows+1, 1, 1, 1.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL,
                           new Insets(3,3,0,3),
                           0, 0);
      panel.add(units_[rows], bag);
      if(isGraph_) {
        //
        // graph checkboxes
        //
        col++;
        xyGroupPanel = new JPanel();
        xyGroupPanel.setLayout(new GridBagLayout());
        bag = new
          GridBagConstraints(col, rows+1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             new Insets(3,0,0,0),
                             0, 0);
        panel.add(xyGroupPanel, bag);
        //
        // xBox
        //
        xBox_[rows] = new JCheckBox();
        xBox_[rows].setHorizontalAlignment(SwingConstants.CENTER);
        bag = new
          GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                             GridBagConstraints.BOTH, new Insets(0,0,0,0),
                             0, 0);
        xyGroupPanel.add(xBox_[rows], bag);
        //
        // yBox
        //
        xBox_[rows].addActionListener(myAction_);
        yBox_[rows] = new JCheckBox();
        yBox_[rows].setHorizontalAlignment(SwingConstants.CENTER);
        bag = new
          GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                             GridBagConstraints.BOTH, new Insets(0,0,0,0),
                             0, 0);
        xyGroupPanel.add(yBox_[rows], bag);
        yBox_[rows].addActionListener(myAction_);
        //
        // reverse axis selection
        //
        col++;
        revBox_[rows] = new JCheckBox();
        bag = new
          GridBagConstraints(col, rows+1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             new Insets(3,0,0,0),
                             0, 0);
        panel.add(revBox_[rows], bag);
      } else if(isCDL_) {
        //
        // export CDL checkboxes
        //
        col++;
        xBox_[rows] = new JCheckBox();
        xBox_[rows].setHorizontalAlignment(SwingConstants.CENTER);
        bag = new
          GridBagConstraints(col, rows+1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             new Insets(3,0,0,0),
                             0, 0);
        panel.add(xBox_[rows], bag);
        xBox_[rows].addActionListener(myAction_);
      } else if(isUNH_) {
        //
        // export UNH checkboxes
        //
        col++;
        xBox_[rows] = new JCheckBox();
        xBox_[rows].setHorizontalAlignment(SwingConstants.CENTER);
        bag = new
          GridBagConstraints(col, rows+1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             new Insets(3,0,0,0),
                             0, 0);
        panel.add(xBox_[rows], bag);
        xBox_[rows].setEnabled(false);
      } else if(isText_) {
        //
        // text checkboxes
        //
        col++;
        xBox_[rows] = new JCheckBox();
        xBox_[rows].setHorizontalAlignment(SwingConstants.CENTER);
        bag = new
          GridBagConstraints(col, rows+1, 1, 1, 0.0, 0.0,
                             GridBagConstraints.CENTER,
                             GridBagConstraints.NONE,
                             new Insets(3,0,0,0),
                             0, 0);
        panel.add(xBox_[rows], bag);
      }
      //
      // min
      //
      col++;
      minGroupPanel = new JPanel();
      minGroupPanel.setLayout(new GridBagLayout());
      bag = new
        GridBagConstraints(col, rows+1, 1, 1, 1.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL,
                           new Insets(3,3,0,3),
                           0, 0);
      panel.add(minGroupPanel, bag);
      if(noVar) {
        minRange_[rows] = new Integer(0);
      } else {
        minRange_[rows] = ncFile_.getArrayValue(var, 0);
      }
      value = NcUtil.valueAsString(minRange_[rows]);
      min_[rows] = new JTextField(value);
      min_[rows].addMouseListener(myMouse_);
      bag = new
        GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL,
                           new Insets(0,0,0,0),
                           0, 0);
      minGroupPanel.add(min_[rows], bag);
//      minPopupButton_[rows] = new JButton("...");
      minPopupButton_[rows] = new JButton(dots_);
      if(!Browser.isSystemLF()) {
        minPopupButton_[rows].setMargin(new Insets(0,0,0,0));
      }
      minPopupButton_[rows].addActionListener(myAction_);
      if(len == 1) minPopupButton_[rows].setEnabled(false);
      bag = new
        GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.NONE,
                           new Insets(0,0,0,0),
                           0, 0);
      minGroupPanel.add(minPopupButton_[rows], bag);
      //
      // max
      //
      col++;
      maxGroupPanel = new JPanel();
      maxGroupPanel.setLayout(new GridBagLayout());
      bag = new
        GridBagConstraints(col, rows+1, 1, 1, 1.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL,
                           new Insets(3,3,0,3),
                           0, 0);
      panel.add(maxGroupPanel, bag);
      if(noVar) {
        maxRange_[rows] = new Integer(len-1);
      } else {
        maxRange_[rows] = ncFile_.getArrayValue(var, len-1);
      }
      value = NcUtil.valueAsString(maxRange_[rows]);
      max_[rows] = new JTextField(value);
      max_[rows].addMouseListener(myMouse_);
      if(len == 1) {
        isRange_[rows] = false;
        max_[rows].setEnabled(false);
        xBox_[rows].setEnabled(false);
        if(isGraph_) {
          yBox_[rows].setEnabled(false);
          revBox_[rows].setEnabled(false);
        }
      } else {
        isRange_[rows] = true;
      }
      bag = new
        GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.HORIZONTAL,
                           new Insets(0,0,0,0),
                           0, 0);
      maxGroupPanel.add(max_[rows], bag);
//      maxPopupButton_[rows] = new JButton("...");
      maxPopupButton_[rows] = new JButton(dots_);
      if(!Browser.isSystemLF()) {
        maxPopupButton_[rows].setMargin(new Insets(0,0,0,0));
      }
      maxPopupButton_[rows].addActionListener(myAction_);
      if(len == 1) maxPopupButton_[rows].setEnabled(false);
      bag = new
        GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                           GridBagConstraints.CENTER,
                           GridBagConstraints.NONE,
                           new Insets(0,0,0,0),
                           0, 0);
      maxGroupPanel.add(maxPopupButton_[rows], bag);
      rows++;
    } // end - for
    //
    // set default state for checkboxes
    //
    boolean xUsed = false;
    boolean yUsed = false;
    for(int i=0; i < rank_; i++) {
      if(isRange_[i]) {
        if(isGraph_) {
          if(!xUsed) {
            xBox_[i].setSelected(true);
            yBox_[i].setSelected(false);
            xUsed = true;
          } else if(!yUsed){
            xBox_[i].setSelected(false);
            yBox_[i].setSelected(true);
            yUsed = true;
          } else {
            xBox_[i].setSelected(false);
            yBox_[i].setSelected(false);
            max_[i].setEnabled(false);
          }
        } else if(isCDL_) {
          xBox_[i].setSelected(true);
        } else if(isText_) {
          xBox_[i].setSelected(true);
          for(int j=0; j < i; j++) {
            xBox_[j].setSelected(false);
          }
        }
      }
    }
    //
    // set state for UNH  (assumes OLEM output)
    //
    if(isUNH_) {
      for(int i=0; i < rank_; i++) {
        if(((Variable)dimOrVar_[i]).getName() == "time") {
          xBox_[i].setSelected(true);
        } else if(((Variable)dimOrVar_[i]).getName().indexOf("z") != -1) {
          xBox_[i].setSelected(true);
        } else {
          xBox_[i].setSelected(false);
          max_[i].setEnabled(false);
        }
      }
    }

    return panel;
  }

  JPanel buttonPanel = new JPanel();
  JButton closeButton = new JButton();
  JButton actionButton = new JButton();
  JPanel mainPanel = new JPanel();
  JScrollPane infoScrollPane = new JScrollPane();
  JTextArea infoText = new JTextArea();
  JScrollPane axesScrollPane = new JScrollPane();
  TitledBorder axesBorder;

  JButton openButton = new JButton();
  JComboBox sendCBox = new JComboBox();
  DefaultComboBoxModel sendCBoxModel = new DefaultComboBoxModel();

  /** @link dependency */
  /*#VariableGraph lnkVariableGraph;*/

  /** @link dependency */
  /*#ExportCdl lnkExportCdl;*/

  /** @link dependency */
  /*#ExportUNH lnkExportUNH;*/

  /** @link dependency */
  /*#VariableText lnkVariableText;*/

  class MyMouse extends MouseAdapter {
    public void mouseReleased(MouseEvent event) {
      Object object = event.getSource();
      if (object instanceof JTextField)
        maybePopupTrigger(event);
    }
    public void mousePressed(MouseEvent event) {
      Object object = event.getSource();
      if (object instanceof JTextField)
        maybePopupTrigger(event);
    }
  }

  void maybePopupTrigger(MouseEvent event) {
    Object obj = event.getSource();
    if(event.isPopupTrigger()) {
      for(int i=0; i < rank_; i++) {
        if(obj == min_[i] || obj == max_[i]) {
          Point screenLoc = ((JTextField)obj).getLocationOnScreen();
          popupDialog(i,
                      screenLoc,
                      new Point(event.getX(), event.getY()));
        }
      }
    }
  }

  void popupDialog(int i, Point screenLoc, Point mouseLoc) {
    Point offset = new Point(-240, -220);
    boolean twoHandles = false;
    if(isGraph_) {
      twoHandles = xBox_[i].isSelected() || yBox_[i].isSelected();
    } else {
      twoHandles = xBox_[i].isSelected();
    }
    if(!isRange_[i]) return;
    if(isTime_[i]) {
      if(std_ == null) {
        std_ = new SelectTimeDialog(this);
        std_.setModal(true);
        std_.setRange((GeoDate)minRange_[i],
                      (GeoDate)maxRange_[i]);
      }
      std_.setTwoHandles(twoHandles);
      try {
        std_.setStartValue(new GeoDate(min_[i].getText(), tFormat_));
        if(twoHandles) {
          std_.setEndValue(new GeoDate(max_[i].getText(), tFormat_));
        }
      } catch (IllegalTimeValue e) {
        JOptionPane.showMessageDialog(this,
                                      e.toString(),
                                      "Time Format Error",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
      if(std_.showDialog(screenLoc.x + mouseLoc.x + offset.x,
                         screenLoc.y + mouseLoc.y + offset.y) ==
         SelectTimeDialog.APPROVE_OPTION) {
        if(Debug.DEBUG) System.out.println("result = " +
                                           std_.getStartValue() +
                                           ", " + std_.getEndValue());
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
        minRange = ((Long)minRange_[i]).doubleValue();
        maxRange = ((Long)maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Integer) {
        minRange = ((Integer)minRange_[i]).doubleValue();
        maxRange = ((Integer)maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Short) {
        minRange = ((Short)minRange_[i]).doubleValue();
        maxRange = ((Short)maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Float) {
        minRange = ((Float)minRange_[i]).doubleValue();
        maxRange = ((Float)maxRange_[i]).doubleValue();
      } else if(minRange_[i] instanceof Double) {
        minRange = ((Double)minRange_[i]).doubleValue();
        maxRange = ((Double)maxRange_[i]).doubleValue();
      }
      sdd_.setRange(minRange, maxRange);
      sdd_.setStartValue(Double.parseDouble(min_[i].getText()));
      if(twoHandles) {
        sdd_.setEndValue(Double.parseDouble(max_[i].getText()));
      }
      if(sdd_.showDialog(screenLoc.x + mouseLoc.x + offset.x,
                         screenLoc.y + mouseLoc.y + offset.y) ==
         SelectDoubleDialog.APPROVE_OPTION) {
        if(Debug.DEBUG) System.out.println("result = " +
                                           sdd_.getStartValue() +
                                           ", " + sdd_.getEndValue());
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
      if(object instanceof JCheckBox)
        checkBox_actionPerformed(event);
      else if (object == closeButton)
        closeButton_actionPerformed(event);
      else if (object == actionButton)
        actionButton_actionPerformed(event);
      else if (object == openButton)
        openButton_actionPerformed(event);
      else if (object instanceof JButton)
        button_actionPerformed(event);
    }
  }

  void checkBox_actionPerformed(ActionEvent event) {
    Object box = event.getSource();
    boolean flipped = false;
    int rowChanged = -1;
    if(rank_ == 0 || isUNH_) return;
    if(isCDL_ || isText_) {
      for(int i=0; i < rank_; i++) {
        if(xBox_[i].equals(box)) {
          max_[i].setEnabled(xBox_[i].isSelected());
          return;
        }
      }
    }
    //
    // check x
    //
    for(int i=0; i < rank_; i++) {
      if(xBox_[i].equals(box)) {
        if(xBox_[i].isSelected() && yBox_[i].isSelected()) {
          yBox_[i].setSelected(false);
          flipped = true;
        }
        rowChanged = i;
        break;
      }
    }
    if(rowChanged == -1) {
      //
      // check y
      //
      for(int i=0; i < rank_; i++) {
        if(yBox_[i].equals(box)) {
          if(yBox_[i].isSelected() && xBox_[i].isSelected()) {
            xBox_[i].setSelected(false);
            flipped = true;
          }
          rowChanged = i;
          break;
        }
      }
    }
    for(int i=0; i < rank_; i++) {
      if(i != rowChanged) {
        if(xBox_[rowChanged].isSelected()) {
          if(flipped && xBox_[i].isSelected()) {
            yBox_[i].setSelected(true);
          }
          xBox_[i].setSelected(false);
        }
        if(yBox_[rowChanged].isSelected()) {
          if(flipped && yBox_[i].isSelected()) {
            xBox_[i].setSelected(true);
          }
          yBox_[i].setSelected(false);
        }
      }
      if(isRange_[i] &&
         (xBox_[i].isSelected() || yBox_[i].isSelected())) {
        max_[i].setEnabled(true);
      } else {
        max_[i].setEnabled(false);
      }
    }
  }

  void closeButton_actionPerformed(ActionEvent event) {
    try {
      this.setVisible(false);
      this.dispose();
    } catch (Exception e) {
    }
  }

  void actionButton_actionPerformed(ActionEvent event) {
    if(isText_) {
      processText();
    } else if(isGraph_) {
      processGraph();
    } else {
      processExtract();
    }
  }

  void button_actionPerformed(ActionEvent event) {
    JButton obj = (JButton)event.getSource();
    for(int i=0; i < rank_; i++) {
      if(obj == minPopupButton_[i] || obj == maxPopupButton_[i]) {
        Point screenLoc = obj.getLocationOnScreen();
        popupDialog(i, screenLoc, new Point(0,0));
      }
    }
  }

  void openButton_actionPerformed(ActionEvent event) {
    Collection coll = new Collection("EPS data collection");
    SGTData subset = null;
    SelectionRange range = computeRange();
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
    String id = ncFile_.getPathName() + "-" + ncVar_.getName();
    try {
      SGTData data = range.getDataModel(id);
      if(data instanceof SGTLine) {
        coll.add(data);
        subset = coll;
      } else {
        subset = data;
      }
      if(pwindex <= 0) {
        epCount_++;
        String frameName = "ncBrowse";
        String path = ncFile_.getPathName();
        String title1 = path.substring(path.lastIndexOf("/") + 1);
        String title2 = ncVar_.getName().trim();
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
          Object[] adArgs = {new Integer(pwindex), subset};
          addData.invoke(ncSupport, adArgs);
        } catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    } catch (RankNotSupportedException e) {
        String s = e.toString();
      System.out.println(s);
      return;
    }

  }

  public SelectionRange computeRange() {
    int outRank = 0;
    SelectionRange range = new SelectionRange(ncVar_);
    range.setTimeFormat(ncFile_.is624(),
                        ncFile_.getTime2(),
                        ncFile_.getRefDate(),
                        ncFile_.getIncrement());
    if(Debug.DEBUG) {
      System.out.print("is624_ = " + ncFile_.is624() +
                       ", rank = " + rank_);
      if(ncFile_.getRefDate() != null) {
        System.out.println(" refDate_ = " +
                           ncFile_.getRefDate().toString());
      } else {
        System.out.println(" refDate_ = null");
      }
    }
    boolean selAxis = false;
    for(int i=0; i < rank_; i++) {
      selAxis = xBox_[i].isSelected() || yBox_[i].isSelected();
      if(selAxis) outRank++;
      if(dimOrVar_[i] instanceof ucar.nc2.Dimension) {
        int len = ((ucar.nc2.Dimension)dimOrVar_[i]).getLength();
        Object anArray = new int[len];
        int min = (int)Math.round((new Double(min_[i].getText())).doubleValue());
        int max;
        if(selAxis) {
          max = (int)Math.round((new Double(max_[i].getText())).doubleValue());
        } else {
          max = min;
        }
        if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
        for(int j=0; j < len; j++) {
          ((int[])anArray)[j] = j;
        }
        range.addDimensionRange(dimOrVar_[i], anArray,
                                revBox_[i].isSelected(),
                                xBox_[i].isSelected(),
                                yBox_[i].isSelected(),
                                min, max);
      } else {
        Object anArray = null;
        try {
          anArray = ((Variable)dimOrVar_[i]).read().copyTo1DJavaArray();
        } catch (IOException e) {
            String s = e.toString();
            System.out.println(s);
        }
        if(isTime_[i]) {
          GeoDate min = null;
          GeoDate max = null;
          try {
            min = new GeoDate(min_[i].getText(), tFormat_);
            if(selAxis) {
              max = new GeoDate(max_[i].getText(), tFormat_);
            } else {
              max = min;
            }
          } catch (IllegalTimeValue e) {
            JOptionPane.showMessageDialog(parent_,
                                          e.toString(),
                                          "Time Format Error",
                                          JOptionPane.ERROR_MESSAGE);
            return null;
          }
          if(Debug.DEBUG) {
            System.out.println("min,max = " +
                               min.toString() + ", " + max.toString());
          }
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  revBox_[i].isSelected(),
                                  xBox_[i].isSelected(),
                                  yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof long[]) {
          long min = (long)Math.round((new Double(min_[i].getText())).doubleValue());
          long max;
          if(selAxis) {
            max = (long)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  revBox_[i].isSelected(),
                                  xBox_[i].isSelected(),
                                  yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof byte[]) {
          byte min = (byte)Math.round((new Double(min_[i].getText())).doubleValue());
          byte max;
          if(selAxis) {
            max = (byte)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  revBox_[i].isSelected(),
                                  xBox_[i].isSelected(),
                                  yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof int[]) {
          int min = (int)Math.round((new Double(min_[i].getText())).doubleValue());
          int max;
          if(selAxis) {
            max = (int)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  revBox_[i].isSelected(),
                                  xBox_[i].isSelected(),
                                  yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof short[]) {
          short min = (short)Math.round((new Double(min_[i].getText())).doubleValue());
          short max;
          if(selAxis) {
            max = (short)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  revBox_[i].isSelected(),
                                  xBox_[i].isSelected(),
                                  yBox_[i].isSelected(),
                                  (short)min, (short)max);
        } else if(anArray instanceof float[]) {
          float min = (new Float(min_[i].getText())).floatValue();
          float max;
          if(selAxis) {
            max = (new Float(max_[i].getText())).floatValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  revBox_[i].isSelected(),
                                  xBox_[i].isSelected(),
                                  yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof double[]) {
          double min = (new Double(min_[i].getText())).doubleValue();
          double max;
          if(selAxis) {
            max = (new Double(max_[i].getText())).doubleValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  revBox_[i].isSelected(),
                                  xBox_[i].isSelected(),
                                  yBox_[i].isSelected(),
                                  min, max);
        } else {
          if(Debug.DEBUG) System.out.println("min,max = not found");
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  false, false,
                                  null, null);
        }
      }
    }
    if(Debug.DEBUG) {
      int[] origin = range.getOrigin();
      int[] shape = range.getShape();
      StringBuffer org = new StringBuffer("Origin = [");
      StringBuffer shp = new StringBuffer("Shape = [");
      for(int i=0; i < origin.length; i++) {
        org.append(origin[i] + ", ");
        shp.append(shape[i] + ", ");
      }
      org.setCharAt(org.length()-2, ']');
      shp.setCharAt(shp.length()-2, ']');
      org.setLength(org.length()-1);
      shp.setLength(shp.length()-1);
      System.out.println(org.toString());
      System.out.println(shp.toString());
    }
    return range;
  }

  private void processGraph() {
    //
    // range calculation has been moved to VariableGraph
    // to put heavy I/O inside the thread
    //
    VariableProcessThread process = null;
    if(processType_ == VariableProcessThread.GRAPHER) {
      process = new VariableGraph(parent_, "Graph it!!");
      Point pt = parent_.getLocation();
      process.setLocation(10, pt.y + 50);
    }
    ((VariableGraph)process).setDomainSelector(this);
    process.setNcFile(ncFile_);
    process.setVariable(variable_);
    process.start();
  }

  private void processExtract() {
    Attribute attr = null;
    VariableProcessThread process = null;
    int outRank = 0;
    SelectionRange range = new SelectionRange(ncVar_);
    range.setTimeFormat(ncFile_.is624(),
                        ncFile_.getTime2(),
                        ncFile_.getRefDate(),
                        ncFile_.getIncrement());
    if(Debug.DEBUG) {
      System.out.print("is624_ = " + ncFile_.is624() +
                       ", rank = " + rank_);
      if(ncFile_.getRefDate() != null) {
        System.out.println(" refDate_ = " +
                           ncFile_.getRefDate().toString());
      } else {
        System.out.println(" refDate_ = null");
      }
    }
    boolean selAxis = false;
    for(int i=0; i < rank_; i++) {
      selAxis = xBox_[i].isSelected();
      if(selAxis) outRank++;
      if(dimOrVar_[i] instanceof ucar.nc2.Dimension) {
        int len = ((ucar.nc2.Dimension)dimOrVar_[i]).getLength();
        Object anArray = new int[len];
        int min = (int)Math.round((new Double(min_[i].getText())).doubleValue());
        int max;
        if(selAxis) {
          max = (int)Math.round((new Double(max_[i].getText())).doubleValue());
        } else {
          max = min;
        }
        if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
        for(int j=0; j < len; j++) {
          ((int[])anArray)[j] = j;
        }
        range.addDimensionRange(dimOrVar_[i], anArray,
                                revBox_[i].isSelected(),
                                xBox_[i].isSelected(),
                                yBox_[i].isSelected(),
                                min, max);
      } else {
        Object anArray = null;
        try {
          Array arr = ((Variable)dimOrVar_[i]).read();
          anArray = arr.copyTo1DJavaArray();
        } catch (IOException e) {
            String s = e.toString();
            System.out.println(s);
        }
        if(isTime_[i]) {
          GeoDate min = null;
          GeoDate max = null;
          try {
            min = new GeoDate(min_[i].getText(), tFormat_);
            if(selAxis) {
              max = new GeoDate(max_[i].getText(), tFormat_);
            } else {
              max = min;
            }
          } catch (IllegalTimeValue e) {
            JOptionPane.showMessageDialog(this,
                                          e.toString(),
                                          "Time Format Error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
          }
          if(Debug.DEBUG) {
            System.out.println("min,max = " +
                               min.toString() + ", " + max.toString());
          }
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof long[]) {
          long min = (long)Math.round((new Double(min_[i].getText())).doubleValue());
          long max;
          if(selAxis) {
            max = (long)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof int[]) {
          int min = (int)Math.round((new Double(min_[i].getText())).doubleValue());
          int max;
          if(selAxis) {
            max = (int)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof short[]) {
          short min = (short)Math.round((new Double(min_[i].getText())).doubleValue());
          short max;
          if(selAxis) {
            max = (short)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  (short)min, (short)max);
        } else if(anArray instanceof float[]) {
          float min = (new Float(min_[i].getText())).floatValue();
          float max;
          if(selAxis) {
            max = (new Float(max_[i].getText())).floatValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof double[]) {
          double min = (new Double(min_[i].getText())).doubleValue();
          double max;
          if(selAxis) {
            max = (new Double(max_[i].getText())).doubleValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else {
          if(Debug.DEBUG) System.out.println("min,max = not found");
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  false, false,
                                  null, null);
        }
      }
    }
    if(Debug.DEBUG) {
      int[] origin = range.getOrigin();
      int[] shape = range.getShape();
      StringBuffer org = new StringBuffer("Origin = [");
      StringBuffer shp = new StringBuffer("Shape = [");
      for(int i=0; i < origin.length; i++) {
        org.append(origin[i] + ", ");
        shp.append(shape[i] + ", ");
      }
      org.setCharAt(org.length()-2, ']');
      shp.setCharAt(shp.length()-2, ']');
      org.setLength(org.length()-1);
      shp.setLength(shp.length()-1);
      System.out.println(org.toString());
      System.out.println(shp.toString());
    }
    //
    // insert setting code here
    //
    if(processType_ == VariableProcessThread.EXPORT_CDL) {
      process = new ExportCdl(parent_, "Export it to cdl!");
    } else if(processType_ == VariableProcessThread.EXPORT_UNH) {
      process = new ExportUNH(parent_, "Export it to UNH!");
    }
    process.setNcFile(ncFile_);
    process.setVariable(variable_);
    process.setSelectionRange(range);
    process.start();
    this.setVisible(false);
  }

  private void processText() {
    VariableProcessThread process = null;
    SelectionRange range = new SelectionRange(ncVar_);
    range.setTimeFormat(ncFile_.is624(),
                        ncFile_.getTime2(),
                        ncFile_.getRefDate(),
                        ncFile_.getIncrement());
    int outRank = 0;
    boolean selAxis = false;
    for(int i=0; i < rank_; i++) {
      selAxis = xBox_[i].isSelected();
      if(selAxis) outRank++;
      if(dimOrVar_[i] instanceof ucar.nc2.Dimension) {
        int len = ((ucar.nc2.Dimension)dimOrVar_[i]).getLength();
        Object anArray = new int[len];
        int min = (int)Math.round((new Double(min_[i].getText())).doubleValue());
        int max;
        if(selAxis) {
          max = (int)Math.round((new Double(max_[i].getText())).doubleValue());
        } else {
          max = min;
        }
        if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
        for(int j=0; j < len; j++) {
          ((int[])anArray)[j] = j;
        }
        range.addDimensionRange(dimOrVar_[i], anArray,
                                false,
                                xBox_[i].isSelected(),
                                false,
                                min, max);
      } else {
        Object anArray = null;
        try {
          Array arr = ((Variable)dimOrVar_[i]).read();
          anArray = arr.copyTo1DJavaArray();
        } catch (IOException e) {
            String s = e.toString();
            System.out.println(s);
        }
        if(isTime_[i]) {
          GeoDate min = null;
          GeoDate max = null;
          try {
            min = new GeoDate(min_[i].getText(), tFormat_);
            if(selAxis) {
              max = new GeoDate(max_[i].getText(), tFormat_);
            } else {
              max = min;
            }
          } catch (IllegalTimeValue e) {
            JOptionPane.showMessageDialog(this,
                                          e.toString(),
                                          "Time Format Error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
          }
          if(Debug.DEBUG) {
            System.out.println("min,max = " +
                               min.toString() + ", " + max.toString());
          }
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof long[]) {
          long min = (long)Math.round((new Double(min_[i].getText())).doubleValue());
          long max;
          if(selAxis) {
            max = (long)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof int[]) {
          int min = (int)Math.round((new Double(min_[i].getText())).doubleValue());
          int max;
          if(selAxis) {
            max = (int)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof short[]) {
          short min = (short)Math.round((new Double(min_[i].getText())).doubleValue());
          short max;
          if(selAxis) {
            max = (short)Math.round((new Double(max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  (short)min, (short)max);
        } else if(anArray instanceof float[]) {
          float min = (new Float(min_[i].getText())).floatValue();
          float max;
          if(selAxis) {
            max = (new Float(max_[i].getText())).floatValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else if(anArray instanceof double[]) {
          double min = (new Double(min_[i].getText())).doubleValue();
          double max;
          if(selAxis) {
            max = (new Double(max_[i].getText())).doubleValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  xBox_[i].isSelected(),
                                  false,
                                  min, max);
        } else {
          if(Debug.DEBUG) System.out.println("min,max = not found");
          range.addDimensionRange(dimOrVar_[i], anArray,
                                  false,
                                  false, false,
                                  null, null);
        }
      }
    }
    if(Debug.DEBUG) {
      int[] origin = range.getOrigin();
      int[] shape = range.getShape();
      StringBuffer org = new StringBuffer("Origin = [");
      StringBuffer shp = new StringBuffer("Shape = [");
      for(int i=0; i < origin.length; i++) {
        org.append(origin[i] + ", ");
        shp.append(shape[i] + ", ");
      }
      org.setCharAt(org.length()-2, ']');
      shp.setCharAt(shp.length()-2, ']');
      org.setLength(org.length()-1);
      shp.setLength(shp.length()-1);
      System.out.println(org.toString());
      System.out.println(shp.toString());
    }
    process = new VariableText(parent_, "Export it to cdl!");
    process.setNcFile(ncFile_);
    process.setVariable(variable_);
    process.setSelectionRange(range);
    process.start();
  }

}
