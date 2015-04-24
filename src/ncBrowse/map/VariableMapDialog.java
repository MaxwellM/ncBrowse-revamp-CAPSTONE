/**
 *  $Id: VariableMapDialog.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import gov.noaa.pmel.swing.JViewHTMLFrame;
import ncBrowse.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.24 $, $Date: 2004/05/17 22:49:34 $
 */
public class VariableMapDialog extends JDialog implements ButtonMaintainer {
  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel sourcePanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton doneButton = new JButton();
  JButton applyButton = new JButton();
  JButton clearButton = new JButton();
  JButton quitButton = new JButton();
  JButton helpButton = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  BorderLayout borderLayout2 = new BorderLayout();
  TwoDTargetPanel twoDPanel;
  ThreeDTargetPanel threeDPanel;
  static boolean is2D = true;

  /**
   * @link aggregationByValue
   */
  DnDTable sourceList = new DnDTable();
  JPanel namePanel = new JPanel();
  JTextField variableNameTF = new JTextField();
  JLabel jLabel2 = new JLabel();
  GridBagLayout gridBagLayout8 = new GridBagLayout();
  Border border1;
  Border border2;
  public Border border3;
  JPanel jPanel1 = new JPanel();
  JLabel jLabel1 = new JLabel();
  GridBagLayout gridBagLayout9 = new GridBagLayout();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  JLabel jLabel3 = new JLabel();
  GridBagLayout gridBagLayout10 = new GridBagLayout();

  /**
   * @label currentMap_
   */
  private static VMapModel currentMap2D_ = null;
  private static VMapModel currentMap3D_ = null;
  private static VMapModel currentMap_ = null;
  private static NcTableModel model_ = null;
  JPanel jPanel4 = new JPanel();
  JCheckBox showDimsCB = new JCheckBox();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JSplitPane jSplitPane1 = new JSplitPane();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel errorLabel = new JLabel();
  DialogClient mClient;
  private ResourceBundle b = ResourceBundle.getBundle(
      "ncBrowse.NcBrowseResources");

  public VariableMapDialog(Frame frame, String title, NcFile ncFile, boolean modal) {
    super(frame, title, modal);
    mClient = (DialogClient)frame;
    try {
      jbInit();
      //      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    currentMap2D_ = new VMapModel(ncFile);
    currentMap3D_ = new VMapModel(ncFile);
    currentMap_ = currentMap2D_;
  }

  public static VMapModel getCurrentMap() {
    return currentMap_;
  }

  static NcTableModel getCurrentModel() {
    return model_;
  }

  public VariableMapDialog() {
    this(null, "", null, false);
  }

  void jbInit() throws Exception {
    is2D = true;
    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(new BorderLayout(5, 5));
    JPanel upperContents = new JPanel();
    JPanel errLabelCont = new JPanel();
    errLabelCont.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
    errLabelCont.add(errorLabel);
    border1 = BorderFactory.createEmptyBorder(5, 10, 5, 5);
    border2 = BorderFactory.createLineBorder(Color.white, 1);
    border3 = BorderFactory.createCompoundBorder(
        BorderFactory.createEtchedBorder(Color.white,
                                         new Color(148, 145, 140)),
        BorderFactory.createEmptyBorder(0, 5, 0, 0));
    upperContents.setLayout(borderLayout1);
    upperContents.setMinimumSize(new Dimension(800, 600));
    setSize(800, 500);
    setTitle("Variable Map Designer");
    sourcePanel.setBorder(border1);
    sourcePanel.setLayout(borderLayout2);
    buttonPanel.setLayout(flowLayout1);
    doneButton.setText(b.getString("kDDone"));
    doneButton.addActionListener(new VariableMapDialog_doneButton_actionAdapter(this));
    this.getRootPane().setDefaultButton(doneButton);
    applyButton.setText(b.getString("kApply"));
    applyButton.addActionListener(
        new VariableMapDialog_applyButton_actionAdapter(this));
    clearButton.setText(b.getString("kClear"));
    clearButton.addActionListener(
        new VariableMapDialog_clearButton_actionAdapter(this));
    quitButton.setText(b.getString("kClose"));
    quitButton.addActionListener(
        new VariableMapDialog_quitButton_actionAdapter(this));
    helpButton.setText(b.getString("kHelp"));
    helpButton.addActionListener(
        new VariableMapDialog_helpButton_actionAdapter(this));
    jScrollPane1.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jScrollPane1.setBorder(BorderFactory.createEtchedBorder());
    sourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    //sourceList.getInputMap().remove(enter);
    jLabel2.setText(b.getString("kVariableMapName"));
    namePanel.setLayout(gridBagLayout8);
    borderLayout2.setHgap(5);
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    namePanel.setBorder(BorderFactory.createEtchedBorder());
    jLabel1.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));
    jLabel1.setText(b.getString("kSource"));
    jPanel1.setLayout(gridBagLayout9);
    jPanel2.setLayout(borderLayout3);
    jLabel3.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));
    jLabel3.setText(b.getString("kTarget"));
    jPanel3.setLayout(gridBagLayout10);

    showDimsCB.setText(b.getString("kShowAllDimensions"));
    showDimsCB.addActionListener(
        new VariableMapDialog_showDimsCB_actionAdapter(this));
    jPanel4.setLayout(gridBagLayout2);
    upperContents.add(namePanel, BorderLayout.NORTH);
    namePanel.add(jLabel2,
                  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                         GridBagConstraints.EAST,
                                         GridBagConstraints.NONE,
                                         new Insets(10, 15, 10, 5), 0, 0));
    enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    variableNameTF.getKeymap().removeKeyStrokeBinding(enter);
    namePanel.add(variableNameTF,
                  new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                                         GridBagConstraints.CENTER,
                                         GridBagConstraints.HORIZONTAL,
                                         new Insets(10, 5, 10, 15), 0, 0));
    buttonPanel.add(doneButton, null);
    //buttonPanel.add(applyButton, null);
    buttonPanel.add(clearButton, null);
    buttonPanel.add(helpButton, null);
    buttonPanel.add(quitButton, null);
    upperContents.add(jSplitPane1, BorderLayout.CENTER);
    jSplitPane1.add(sourcePanel, JSplitPane.LEFT);
    sourcePanel.add(jScrollPane1, BorderLayout.CENTER);
    sourcePanel.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel1,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                                       GridBagConstraints.WEST,
                                       GridBagConstraints.NONE,
                                       new Insets(5, 5, 5, 5), 0, 0));
    sourcePanel.add(jPanel4, BorderLayout.SOUTH);
    jPanel4.add(showDimsCB,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                                       GridBagConstraints.WEST,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(5, 0, 0, 10), 0, 0));
    jSplitPane1.add(jPanel2, JSplitPane.RIGHT);
    jPanel2.add(jPanel3, BorderLayout.NORTH);
    jPanel3.add(jLabel3,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                                       GridBagConstraints.WEST,
                                       GridBagConstraints.NONE,
                                       new Insets(5, 5, 5, 5), 0, 5));

    twoDPanel = new TwoDTargetPanel();
    threeDPanel = new ThreeDTargetPanel();

    // make a tab panel for these to go in
    JTabbedPane everyThingPanel = new JTabbedPane();
    everyThingPanel.addTab("2D", twoDPanel);
    everyThingPanel.addTab("3D", threeDPanel);
    if(!ncBrowse.Browser.can3DMap) {
      everyThingPanel.setEnabledAt(1, false);

    }
    jPanel2.add(everyThingPanel, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(sourceList, null);
    jSplitPane1.setDividerLocation(300);

    upperContents.add(buttonPanel, BorderLayout.SOUTH);
    contentPane.add(upperContents, BorderLayout.CENTER);
    contentPane.add(errLabelCont, BorderLayout.SOUTH);

    ChangeListener changeListener = new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        is2D = !is2D;
        if(is2D) {
          currentMap_ = currentMap2D_;
        } else {
          currentMap_ = currentMap3D_;
        }
        currentMap_.reset();
        if(is2D) {
          TargetMonitor.getInstance().reset();
        } else {
          TargetMonitor3D.getInstance().reset();
        }
      }
    };
    everyThingPanel.addChangeListener(changeListener);

    MaintenanceTimer mMaintain = new MaintenanceTimer(this, 100);
    mMaintain.startMaintainer();
  }

  public static void updateAll(VMapModel map) {
    if(is2D) {
      TargetMonitor.getInstance().updateAll(map);
    } else {
      TargetMonitor3D.getInstance().updateAll(map);
    }
  }

  public static void valueChanged(int type) {
    if(is2D) {
      TargetMonitor.getInstance().valueChanged(type);
    } else {
      TargetMonitor3D.getInstance().valueChanged(type);
    }
  }

  public void setList(NcFile ncFile, Vector list) {
    model_ = new NcTableModel(ncFile, list);
    model_.setShowAllDims(false);
    sourceList.setModel(model_);
  }

  public void maintainButtons() {
    if(!variableNameTF.getText().equals(" ") &&
       !variableNameTF.getText().equals("")) {
      if(currentMap_ != null && currentMap_.isValid()) {
        doneButton.setEnabled(true);
        applyButton.setEnabled(true);
        errorLabel.setText(" ");
      } else {
        if(currentMap_ != null) {
          errorLabel.setText(currentMap_.getErrorMessage());
        }
        doneButton.setEnabled(false);
        applyButton.setEnabled(false);
      }
    } else {
      if(currentMap_ != null && currentMap_.isValid()) {
        doneButton.setEnabled(false);
        applyButton.setEnabled(false);
        errorLabel.setText(b.getString("kVariableMapmustbenamed"));
      } else {
        if(currentMap_ != null) {
          errorLabel.setText(currentMap_.getErrorMessage());
        }
        doneButton.setEnabled(false);
        applyButton.setEnabled(false);
      }
    }
  }

  void quitButton_actionPerformed(ActionEvent e) {
    setVisible(false);
    //    System.exit(0);
  }

  void helpButton_actionPerformed(ActionEvent e) {
    JViewHTMLFrame ub = new JViewHTMLFrame("Mapping Help");
    ub.setPage("http://www.epic.noaa.gov/java/ncBrowse/help.html");
    ub.setLocation(100, 100);
    ub.setVisible(true);
  }

  void clearButton_actionPerformed(ActionEvent e) {
    currentMap_.reset();
    if(is2D) {
      TargetMonitor.getInstance().reset();
    } else {
      TargetMonitor3D.getInstance().reset();
    }
  }

  void applyButton_actionPerformed(ActionEvent e) {
    finis();
      System.out.println("DONE BUTTON");
    if(Debug.DEBUG) {
      System.out.println("Grid Type = " + currentMap_.getGridType());
    }
  }

  void doneButton_actionPerformed(ActionEvent e) {
    if(finis()) {
      setVisible(false);
      if(Debug.DEBUG) {
        System.out.println("Grid Type = " + currentMap_.getGridType());
      }
      mClient.dialogDismissed(this);
    }
  }

  private boolean finis() {
    if(!variableNameTF.getText().equals(" ") && !variableNameTF.getText().equals("")) {
      if(Debug.DEBUG) {
        System.out.println("Variable = " + variableNameTF.getText());
      }
      currentMap_.setName(variableNameTF.getText());
    } else {
      JOptionPane.showMessageDialog(this, "Variable name must be defined", "Definition Incomplete", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if(!currentMap_.isValid()) {
      JOptionPane.showMessageDialog(this, currentMap_.getErrorMessage(), "Definition Incomplete", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if(currentMap_.getGridType() == VMapModel.TYPE_3) {
      JOptionPane.showMessageDialog(this, "X and Y axes must not have common parameters", "Unsupported Grid Structure", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if(Debug.DEBUG) {
      if(!twoDPanel.getxAxisLabel().getText().trim().equals("")) {
        System.out.println("   " +
                           currentMap_.getElementName(VMapModel.X_AXIS) +
                           " --> X Axis");
      }
      if(!twoDPanel.getyAxisLabel().getText().trim().equals("")) {
        System.out.println("   " +
                           currentMap_.getElementName(VMapModel.Y_AXIS) +
                           " --> Y Axis");
      }
      if(!twoDPanel.getColorLabel().getText().trim().equals("")) {
        System.out.println("   " +
                           currentMap_.getElementName(VMapModel.LINE_COLOR) +
                           " --> Line Color");
      }
      if(!twoDPanel.getuLabel().getText().trim().equals("")) {
        System.out.println("   " +
                           currentMap_.getElementName(VMapModel.U_COMPONENT) +
                           " --> U Vector Component");
      }
      if(!twoDPanel.getvLabel().getText().trim().equals("")) {
        System.out.println("   " +
                           currentMap_.getElementName(VMapModel.V_COMPONENT) +
                           " --> V Vector Component");
      }
      if(!twoDPanel.getcontLabel().getText().trim().equals("")) {
        System.out.println("   " +
                           currentMap_.getElementName(VMapModel.Z_CONTOUR) +
                           " --> Z Axis (Contour)");
      }
    }
    return true;
  }

  void showDimsCB_actionPerformed(ActionEvent e) {
    model_.setShowAllDims(showDimsCB.isSelected());
  }

  private class TwoDTargetPanel extends JPanel {
    JPanel jPanel1 = new JPanel();
    JLabel jLabel1 = new JLabel();
    GridBagLayout gridBagLayout9 = new GridBagLayout();
    JPanel jPanel2 = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    JPanel jPanel3 = new JPanel();
    JLabel jLabel3 = new JLabel();
    GridBagLayout gridBagLayout10 = new GridBagLayout();
    JPanel targetPanel = new JPanel();
    JLabel xAxisLabel = new JLabel();
    JLabel yAxisLabel = new JLabel();
    JLabel colorLabel = new JLabel();
    JLabel uLabel = new JLabel();
    JLabel vLabel = new JLabel();
    JLabel contLabel = new JLabel();
    JLabel xAxisTitle = new JLabel();
    JLabel yAxisTitle = new JLabel();
    JLabel colorTitle = new JLabel();
    JLabel uTitle = new JLabel();
    JLabel vTitle = new JLabel();
    JLabel contTitle = new JLabel();
    JPanel xAxisPanel = new JPanel();
    BorderLayout borderLayout4 = new BorderLayout();
    JPanel yAxisPanel = new JPanel();
    JPanel colorPanel = new JPanel();
    JPanel uComponentPanel = new JPanel();
    JPanel vComponentPanel = new JPanel();
    JPanel contourPanel = new JPanel();
    BorderLayout borderLayout5 = new BorderLayout();
    BorderLayout borderLayout6 = new BorderLayout();
    BorderLayout borderLayout7 = new BorderLayout();
    BorderLayout borderLayout8 = new BorderLayout();
    BorderLayout borderLayout9 = new BorderLayout();

    public TwoDTargetPanel() {
      GridBagLayout gridBagLayout1 = new GridBagLayout();
      this.setLayout(gridBagLayout1);
      Border border3 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), BorderFactory.createEmptyBorder(0, 5, 0, 0));
      this.setBorder(border3);

      TargetMonitor tm = TargetMonitor.getInstance();

      tm.setItem(VMapModel.X_AXIS, xAxisLabel, xAxisPanel, xAxisTitle);
      tm.setItem(VMapModel.Y_AXIS, yAxisLabel, yAxisPanel, yAxisTitle);
      tm.setItem(VMapModel.LINE_COLOR, colorLabel, colorPanel, colorTitle);
      tm.setItem(VMapModel.U_COMPONENT, uLabel, uComponentPanel, uTitle);
      tm.setItem(VMapModel.V_COMPONENT, vLabel, vComponentPanel, vTitle);
      tm.setItem(VMapModel.Z_CONTOUR, contLabel, contourPanel, contTitle);

      NcFlavorMap ncfm = NcFlavorMap.getInstance();

      // V component
      vComponentPanel.setBackground(new Color(186, 217, 217));
      //    vLabel.setEditable(false);
      vLabel.setDropTarget(new DropTarget(vLabel, DnDConstants.ACTION_COPY_OR_MOVE, new LabelTarget(vLabel, VMapModel.V_COMPONENT), true, ncfm));
      vLabel.setForeground(Color.black);
      vLabel.setMaximumSize(new Dimension(0, 20));
      vLabel.setMinimumSize(new Dimension(0, 20));
      vLabel.setPreferredSize(new Dimension(0, 20));
      vTitle.setText(b.getString("kVComponent"));
      vComponentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      vComponentPanel.setLayout(borderLayout7);

      // Z (Contour) Axis
      //    contLabel.setEditable(false);
      contLabel.setDropTarget(new DropTarget(contLabel, DnDConstants.ACTION_COPY_OR_MOVE, new LabelTarget(contLabel, VMapModel.Z_CONTOUR), true, ncfm));
      contLabel.setForeground(Color.black);
      contLabel.setMaximumSize(new Dimension(0, 20));
      contLabel.setMinimumSize(new Dimension(0, 20));
      contLabel.setPreferredSize(new Dimension(0, 20)); ;
      contourPanel.setBackground(new Color(186, 217, 217));
      contourPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      contourPanel.setLayout(borderLayout8);
      contTitle.setText(b.getString("kContour2"));

      // U Component
      //    uLabel.setEditable(false);
      uLabel.setDropTarget(
          new DropTarget(uLabel, DnDConstants.ACTION_COPY_OR_MOVE, new LabelTarget(uLabel, VMapModel.U_COMPONENT), true, ncfm));
      uLabel.setForeground(Color.black);
      uLabel.setMaximumSize(new Dimension(0, 20));
      uLabel.setMinimumSize(new Dimension(0, 20));
      uLabel.setPreferredSize(new Dimension(0, 20));
      uComponentPanel.setBackground(new Color(186, 217, 217));
      uTitle.setText(b.getString("kUComponent"));
      uComponentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      uComponentPanel.setLayout(borderLayout6);

      // Color
      colorPanel.setBackground(new Color(186, 217, 217)); ;
      colorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      colorPanel.setLayout(new BorderLayout());
      colorLabel.setDropTarget(new DropTarget(colorLabel, DnDConstants.ACTION_COPY_OR_MOVE, new LabelTarget(colorLabel, VMapModel.LINE_COLOR), true, ncfm));
      colorLabel.setForeground(Color.black);
      colorLabel.setMaximumSize(new Dimension(0, 20));
      colorLabel.setMinimumSize(new Dimension(0, 20));
      colorLabel.setPreferredSize(new Dimension(0, 20));
      colorTitle.setForeground(Color.black);
      colorTitle.setText(b.getString("kColorVariable"));

      // Y Axis
      yAxisPanel.setBackground(new Color(186, 217, 217)); ;
      yAxisPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      yAxisPanel.setLayout(borderLayout5);
      yAxisLabel.setDropTarget(new DropTarget(yAxisLabel, DnDConstants.ACTION_COPY_OR_MOVE, new LabelTarget(yAxisLabel, VMapModel.Y_AXIS), true, ncfm));
      yAxisLabel.setForeground(Color.black);
      yAxisLabel.setMaximumSize(new Dimension(0, 20));
      yAxisLabel.setMinimumSize(new Dimension(0, 20));
      yAxisLabel.setPreferredSize(new Dimension(0, 20));
      yAxisTitle.setForeground(Color.red);
      yAxisTitle.setText(b.getString("kYAxisVariable"));

      //X Axis
      //    aniLabel.setEditable(false);
      xAxisTitle.setForeground(Color.red);
      xAxisTitle.setHorizontalAlignment(SwingConstants.LEFT);
      xAxisTitle.setText(b.getString("kXAxisVariable"));
      xAxisPanel.setBackground(new Color(186, 217, 217));
      xAxisPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      xAxisPanel.setLayout(borderLayout4);
      xAxisLabel.setDropTarget(new DropTarget(xAxisLabel, new LabelTarget(xAxisLabel, VMapModel.X_AXIS)));
      xAxisLabel.setForeground(Color.black);
      xAxisLabel.setMaximumSize(new Dimension(0, 20));
      xAxisLabel.setMinimumSize(new Dimension(0, 20));
      xAxisLabel.setPreferredSize(new Dimension(0, 20));
      xAxisLabel.setToolTipText("");

      this.add(xAxisTitle,
               new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(xAxisPanel,
               new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      xAxisPanel.add(xAxisLabel, BorderLayout.CENTER);
      this.add(yAxisTitle,
               new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(yAxisPanel,
               new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      yAxisPanel.add(yAxisLabel, BorderLayout.CENTER);
      this.add(colorTitle,
               new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(colorPanel,
               new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      colorPanel.add(colorLabel, BorderLayout.CENTER);
      this.add(uTitle,
               new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(uComponentPanel,
               new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      uComponentPanel.add(uLabel, BorderLayout.CENTER);
      this.add(vTitle,
               new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(vComponentPanel,
               new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      vComponentPanel.add(vLabel, BorderLayout.CENTER);
      this.add(contTitle,
               new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(contourPanel,
               new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      contourPanel.add(contLabel, BorderLayout.CENTER);
      tm.reset();
    }

    public JLabel getxAxisLabel() {
      return xAxisLabel;
    }

    public JLabel getyAxisLabel() {
      return yAxisLabel;
    }

    public JLabel getuLabel() {
      return uLabel;
    }

    public JLabel getvLabel() {
      return vLabel;
    }

    public JLabel getcontLabel() {
      return contLabel;
    }

    public JLabel getColorLabel() {
      return colorLabel;
    }
  }

  private class ThreeDTargetPanel extends JPanel {
    JPanel jPanel1 = new JPanel();
    JLabel jLabel1 = new JLabel();
    GridBagLayout gridBagLayout9 = new GridBagLayout();
    JPanel jPanel2 = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    JPanel jPanel3 = new JPanel();
    JLabel jLabel3 = new JLabel();
    GridBagLayout gridBagLayout10 = new GridBagLayout();
    JPanel targetPanel = new JPanel();
    JLabel xAxisLabel = new JLabel();
    JLabel yAxisLabel = new JLabel();
    JLabel zAxisLabel = new JLabel();
    JLabel zAxisColorLabel = new JLabel();
    JLabel uLabel = new JLabel();
    JLabel vLabel = new JLabel();
    JLabel wLabel = new JLabel();
    //JLabel contLabel = new JLabel();
    JLabel surfLabel = new JLabel();
    JLabel surfColorLabel = new JLabel();
    JLabel volumeLabel = new JLabel();
    JLabel xAxisTitle = new JLabel();
    JLabel yAxisTitle = new JLabel();
    JLabel zAxisTitle = new JLabel();
    JLabel zAxisColorTitle = new JLabel();
    JLabel uTitle = new JLabel();
    JLabel vTitle = new JLabel();
    JLabel wTitle = new JLabel();
    //JLabel contTitle = new JLabel();
    JLabel surfTitle = new JLabel();
    JLabel surfColorTitle = new JLabel();
    JLabel volumeTitle = new JLabel();
    JPanel xAxisPanel = new JPanel();
    JPanel yAxisPanel = new JPanel();
    JPanel zAxisPanel = new JPanel();
    JPanel zAxisColorPanel = new JPanel();
    JPanel uComponentPanel = new JPanel();
    JPanel vComponentPanel = new JPanel();
    JPanel wComponentPanel = new JPanel();
    //JPanel contourPanel = new JPanel();
    JPanel surfPanel = new JPanel();
    JPanel surfColorPanel = new JPanel();
    JPanel volumePanel = new JPanel();
    BorderLayout borderLayout4 = new BorderLayout();
    BorderLayout borderLayout5 = new BorderLayout();
    BorderLayout borderLayout55 = new BorderLayout();
    BorderLayout borderLayout66 = new BorderLayout();
    BorderLayout borderLayout6 = new BorderLayout();
    BorderLayout borderLayout7 = new BorderLayout();
    BorderLayout borderLayout8 = new BorderLayout();
    BorderLayout borderLayout9 = new BorderLayout();
    BorderLayout borderLayout10 = new BorderLayout();
    BorderLayout borderLayout11 = new BorderLayout();
    BorderLayout borderLayout12 = new BorderLayout();
    BorderLayout borderLayout13 = new BorderLayout();

    public ThreeDTargetPanel() {
      GridBagLayout gridBagLayout1 = new GridBagLayout();
      this.setLayout(gridBagLayout1);
      Border border3 = BorderFactory.createCompoundBorder(
          BorderFactory.createEtchedBorder(Color.white,
                                           new Color(148, 145, 140)),
          BorderFactory.createEmptyBorder(0, 5, 0, 0));
      this.setBorder(border3);

      TargetMonitor3D tm = TargetMonitor3D.getInstance();

      tm.setItem(VMapModel.X_AXIS, xAxisLabel, xAxisPanel, xAxisTitle);
      tm.setItem(VMapModel.Y_AXIS, yAxisLabel, yAxisPanel, yAxisTitle);
      tm.setItem(VMapModel.Z_AXIS, zAxisLabel, zAxisPanel, zAxisTitle);
      tm.setItem(VMapModel.Z_AXISCOLOR, zAxisColorLabel, zAxisColorPanel,
                 zAxisColorTitle);
      tm.setItem(VMapModel.U_COMPONENT, uLabel, uComponentPanel, uTitle);
      tm.setItem(VMapModel.V_COMPONENT, vLabel, vComponentPanel, vTitle);
      tm.setItem(VMapModel.W_COMPONENT, wLabel, wComponentPanel, wTitle);
      //tm.setItem(VMapModel.Z_CONTOUR, contLabel, contourPanel, contTitle);
      tm.setItem(VMapModel.SURFACE, surfLabel, surfPanel, surfTitle);
      tm.setItem(VMapModel.SURFACECOLOR, surfColorLabel, surfColorPanel,
                 surfColorTitle);
      tm.setItem(VMapModel.VOLUME, volumeLabel, volumePanel, volumeTitle);

      NcFlavorMap3D ncf3dm = NcFlavorMap3D.getInstance();

      // V component
      vComponentPanel.setBackground(new Color(186, 217, 217));
      //    vLabel.setEditable(false);
      vLabel.setDropTarget(
          new DropTarget(vLabel, DnDConstants.ACTION_COPY_OR_MOVE,
                         new LabelTarget(vLabel, VMapModel.V_COMPONENT), true,
                         ncf3dm));
      vLabel.setForeground(Color.black);
      vLabel.setMaximumSize(new Dimension(0, 20));
      vLabel.setMinimumSize(new Dimension(0, 20));
      vLabel.setPreferredSize(new Dimension(0, 20));
      vTitle.setText(b.getString("kVComponent"));
      vComponentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      vComponentPanel.setLayout(borderLayout7);

      // Z (Contour) Axis
      /* contLabel.setDropTarget(
               new DropTarget(contLabel, DnDConstants.ACTION_COPY_OR_MOVE,
               new LabelTarget(contLabel, VMapModel.Z_CONTOUR), true, ncf3dm));
       contLabel.setForeground(Color.black);
       contLabel.setMaximumSize(new Dimension(0, 20));
       contLabel.setMinimumSize(new Dimension(0, 20));
       contLabel.setPreferredSize(new Dimension(0, 20));;
       contourPanel.setBackground(new Color(186, 217, 217));
       contourPanel.setBorder(BorderFactory.createLoweredBevelBorder());
       contourPanel.setLayout(borderLayout8);
       contTitle.setText("Contour:");*/

      // U Component
      uLabel.setDropTarget(
          new DropTarget(uLabel, DnDConstants.ACTION_COPY_OR_MOVE,
                         new LabelTarget(uLabel, VMapModel.U_COMPONENT), true,
                         ncf3dm));
      uLabel.setForeground(Color.black);
      uLabel.setMaximumSize(new Dimension(0, 20));
      uLabel.setMinimumSize(new Dimension(0, 20));
      uLabel.setPreferredSize(new Dimension(0, 20));
      uComponentPanel.setBackground(new Color(186, 217, 217));
      uTitle.setText(b.getString("kUComponent"));
      uComponentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      uComponentPanel.setLayout(borderLayout6);

      // W Component
      wLabel.setDropTarget(
          new DropTarget(wLabel, DnDConstants.ACTION_COPY_OR_MOVE,
                         new LabelTarget(wLabel, VMapModel.W_COMPONENT), true,
                         ncf3dm));
      wLabel.setForeground(Color.black);
      wLabel.setMaximumSize(new Dimension(0, 20));
      wLabel.setMinimumSize(new Dimension(0, 20));
      wLabel.setPreferredSize(new Dimension(0, 20));
      wComponentPanel.setBackground(new Color(186, 217, 217));
      wTitle.setText(b.getString("kWComponent"));
      wComponentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      wComponentPanel.setLayout(borderLayout12);

      // Y Axis
      yAxisPanel.setBackground(new Color(186, 217, 217));
      yAxisPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      yAxisPanel.setLayout(borderLayout5);
      yAxisLabel.setDropTarget(new DropTarget(yAxisLabel,
                                              DnDConstants.ACTION_COPY_OR_MOVE,
                                              new LabelTarget(yAxisLabel,
          VMapModel.Y_AXIS), true, ncf3dm));
      yAxisLabel.setForeground(Color.black);
      yAxisLabel.setMaximumSize(new Dimension(0, 20));
      yAxisLabel.setMinimumSize(new Dimension(0, 20));
      yAxisLabel.setPreferredSize(new Dimension(0, 20));
      yAxisTitle.setForeground(Color.red);
      yAxisTitle.setText(b.getString("kYAxisVariable"));

      //X Axis
      xAxisTitle.setForeground(Color.red);
      xAxisTitle.setHorizontalAlignment(SwingConstants.LEFT);
      xAxisTitle.setText(b.getString("kXAxisVariable"));
      xAxisPanel.setBackground(new Color(186, 217, 217));
      xAxisPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      xAxisPanel.setLayout(borderLayout4);
      xAxisLabel.setDropTarget(new DropTarget(xAxisLabel,
                                              new LabelTarget(xAxisLabel,
          VMapModel.X_AXIS)));
      xAxisLabel.setForeground(Color.black);
      xAxisLabel.setMaximumSize(new Dimension(0, 20));
      xAxisLabel.setMinimumSize(new Dimension(0, 20));
      xAxisLabel.setPreferredSize(new Dimension(0, 20));
      xAxisLabel.setToolTipText("");

      // Z Axis
      zAxisPanel.setBackground(new Color(186, 217, 217));
      zAxisPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      zAxisPanel.setLayout(borderLayout55);
      zAxisLabel.setDropTarget(new DropTarget(zAxisLabel,
                                              // DnDConstants.ACTION_COPY_OR_MOVE,
                                              new LabelTarget(zAxisLabel,
          VMapModel.Z_AXIS)));
      zAxisLabel.setForeground(Color.black);
      zAxisLabel.setMaximumSize(new Dimension(0, 20));
      zAxisLabel.setMinimumSize(new Dimension(0, 20));
      zAxisLabel.setPreferredSize(new Dimension(0, 20));
      zAxisTitle.setText(b.getString("kZAxisVariable"));

      // Z Axis Color
      zAxisColorPanel.setBackground(new Color(186, 217, 217));
      zAxisColorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      zAxisColorPanel.setLayout(borderLayout66);
      zAxisColorLabel.setDropTarget(new DropTarget(zAxisColorLabel,
          DnDConstants.ACTION_COPY_OR_MOVE,
          new LabelTarget(zAxisColorLabel, VMapModel.Z_AXISCOLOR), true, ncf3dm));
      zAxisColorLabel.setForeground(Color.black);
      zAxisColorLabel.setMaximumSize(new Dimension(0, 20));
      zAxisColorLabel.setMinimumSize(new Dimension(0, 20));
      zAxisColorLabel.setPreferredSize(new Dimension(0, 20));
      zAxisColorTitle.setText(b.getString("kColorVariable"));

      // Z-3D Axis
      surfLabel.setDropTarget(
          new DropTarget(surfLabel, DnDConstants.ACTION_COPY_OR_MOVE,
                         new LabelTarget(surfLabel, VMapModel.SURFACE), true,
                         ncf3dm));
      surfLabel.setForeground(Color.black);
      surfLabel.setMaximumSize(new Dimension(0, 20));
      surfLabel.setMinimumSize(new Dimension(0, 20));
      surfLabel.setPreferredSize(new Dimension(0, 20));
      surfPanel.setBackground(new Color(186, 217, 217));
      surfPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      surfPanel.setLayout(borderLayout10);
      surfTitle.setText(b.getString("kSurface"));

      // Z-3D Axis Color
      surfColorLabel.setDropTarget(
          new DropTarget(surfColorLabel, DnDConstants.ACTION_COPY_OR_MOVE,
                         new LabelTarget(surfColorLabel, VMapModel.SURFACECOLOR), true,
                         ncf3dm));
      surfColorLabel.setForeground(Color.black);
      surfColorLabel.setMaximumSize(new Dimension(0, 20));
      surfColorLabel.setMinimumSize(new Dimension(0, 20));
      surfColorLabel.setPreferredSize(new Dimension(0, 20));
      surfColorPanel.setBackground(new Color(186, 217, 217));
      surfColorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      surfColorPanel.setLayout(borderLayout11);
      surfColorTitle.setText(b.getString("kSurfaceColor"));

      // Volume
      volumeLabel.setDropTarget(
          new DropTarget(volumeLabel, DnDConstants.ACTION_COPY_OR_MOVE,
                         new LabelTarget(volumeLabel, VMapModel.VOLUME), true,
                         ncf3dm));
      volumeLabel.setForeground(Color.black);
      volumeLabel.setMaximumSize(new Dimension(0, 20));
      volumeLabel.setMinimumSize(new Dimension(0, 20));
      volumeLabel.setPreferredSize(new Dimension(0, 20));
      volumeLabel.setBackground(new Color(186, 217, 217));
      volumeLabel.setBorder(BorderFactory.createLoweredBevelBorder());
      volumePanel.setLayout(borderLayout13);
      volumeTitle.setText(b.getString("kVolume"));

      this.add(xAxisTitle,
               new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(xAxisPanel,
               new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      xAxisPanel.add(xAxisLabel, BorderLayout.CENTER);
      this.add(yAxisTitle,
               new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(yAxisPanel,
               new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      yAxisPanel.add(yAxisLabel, BorderLayout.CENTER);
      this.add(zAxisTitle,
               new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(zAxisPanel,
               new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      zAxisPanel.add(zAxisLabel, BorderLayout.CENTER);
      this.add(zAxisColorTitle,
               new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(zAxisColorPanel,
               new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      zAxisColorPanel.add(zAxisColorLabel, BorderLayout.CENTER);
      this.add(uTitle,
               new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(uComponentPanel,
               new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      uComponentPanel.add(uLabel, BorderLayout.CENTER);
      this.add(vTitle,
               new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(vComponentPanel,
               new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      vComponentPanel.add(vLabel, BorderLayout.CENTER);
      this.add(wTitle,
               new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(wComponentPanel,
               new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      wComponentPanel.add(wLabel, BorderLayout.CENTER);
      //this.add(contTitle,
      //new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0 ,
      // GridBagConstraints.EAST, GridBagConstraints.NONE,
      // new Insets(0, 0, 0, 5), 0, 0));
      //this.add(contourPanel,
      //new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0 ,
      //GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      //new Insets(2, 0, 2, 5), 0, 0));

      this.add(surfTitle,
               new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(surfPanel,
               new GridBagConstraints(1, 8, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));

      this.add(surfColorTitle,
               new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));
      this.add(surfColorPanel,
               new GridBagConstraints(1, 9, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));

      this.add(volumePanel,
               new GridBagConstraints(1, 10, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(2, 0, 2, 5), 0, 0));
      this.add(volumeTitle,
               new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,
                                      GridBagConstraints.EAST,
                                      GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 5), 0, 0));

      //contourPanel.add(contLabel, BorderLayout.CENTER);
      surfPanel.add(surfLabel, BorderLayout.CENTER);
      surfColorPanel.add(surfColorLabel, BorderLayout.CENTER);
      volumePanel.add(volumeLabel, BorderLayout.CENTER);
      tm.reset();
    }

    public JLabel getxAxisLabel() {
      return xAxisLabel;
    }

    public JLabel getyAxisLabel() {
      return yAxisLabel;
    }

    public JLabel getzAxisLabel() {
      return zAxisLabel;
    }

    public JLabel getzAxisColorLabel() {
      return zAxisColorLabel;
    }

    public JLabel getuLabel() {
      return uLabel;
    }

    public JLabel getvLabel() {
      return vLabel;
    }

    public JLabel getwLabel() {
      return wLabel;
    }

    //public JLabel getcontLabel() {
    //	return contLabel;
    //}

    public String getMappingName() {
// Object[] selXVars = mXParamList.getJList().getSelectedValues();

//        int numXAxes = selXVars.length <= 7 ? selXVars.length : 7;
//        String xVarText;
//        if (numXAxes > 1) {
//       		xVarText = "(";
//	        for (int i=0; i<numXAxes; i++) {
//	        	xVarText += (String)selXVars[i];
//	        	xVarText = xVarText.trim();
//	        	if (i < numXAxes-1)
//	        		xVarText += ",";
//			}
//	        xVarText += ")";
//		}
//		else {
//        	xVarText = (String)mXParamList.getJList().getSelectedValue();
//        }
//    	if (xVarText == null || xVarText.length() == 0)
//    		xVarText = "?";

//        String yVarText = (String)mYParamList.getJList().getSelectedValue();
//    	if (yVarText == null || yVarText.length() == 0)
//    		yVarText = "?";

//    	String nameString = new String(yVarText + "-" + xVarText + " (" + mFileViewer.mFileViewerName + ")");
//    	mNameField.setText(nameString);
      return null;
    }
  }

  class VariableMapDialog_quitButton_actionAdapter implements ActionListener {
    VariableMapDialog adaptee;

    VariableMapDialog_quitButton_actionAdapter(VariableMapDialog adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.quitButton_actionPerformed(e);
    }
  }

  class VariableMapDialog_helpButton_actionAdapter implements ActionListener {
    VariableMapDialog adaptee;

    VariableMapDialog_helpButton_actionAdapter(VariableMapDialog adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.helpButton_actionPerformed(e);
    }
  }

  class VariableMapDialog_clearButton_actionAdapter implements ActionListener {
    VariableMapDialog adaptee;

    VariableMapDialog_clearButton_actionAdapter(VariableMapDialog adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.clearButton_actionPerformed(e);
    }
  }

  class VariableMapDialog_applyButton_actionAdapter implements ActionListener {
    VariableMapDialog adaptee;

    VariableMapDialog_applyButton_actionAdapter(VariableMapDialog adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.applyButton_actionPerformed(e);
    }
  }

  class VariableMapDialog_doneButton_actionAdapter implements ActionListener {
    VariableMapDialog adaptee;

    VariableMapDialog_doneButton_actionAdapter(VariableMapDialog adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.doneButton_actionPerformed(e);
    }
  }

  class VariableMapDialog_showDimsCB_actionAdapter implements ActionListener {
    VariableMapDialog adaptee;

    VariableMapDialog_showDimsCB_actionAdapter(VariableMapDialog adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.showDimsCB_actionPerformed(e);
    }

  }
}
