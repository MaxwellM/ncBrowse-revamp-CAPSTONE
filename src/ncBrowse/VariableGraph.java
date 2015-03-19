/**
 *  $Id: VariableGraph.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import java.awt.Frame;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.PrintJob;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Properties;
import javax.swing.*;
import java.awt.print.*;

import gov.noaa.pmel.sgt.JPane;
import gov.noaa.pmel.sgt.AbstractPane;
import gov.noaa.pmel.sgt.LineCartesianRenderer;
import gov.noaa.pmel.sgt.LineAttribute;
import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.swing.JGraphicLayout;
import gov.noaa.pmel.sgt.swing.JPlotLayout;
import gov.noaa.pmel.sgt.swing.JClassTree;
import gov.noaa.pmel.sgt.swing.prop.LineAttributeDialog;

import gov.noaa.pmel.util.Dimension2D;
import gov.noaa.pmel.util.Rectangle2D;
import gov.noaa.pmel.util.GeoDate;
import gov.noaa.pmel.util.IllegalTimeValue;

import ucar.nc2.Variable;
import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
/**
 * Creates a <code>sgt</code> based graph of a netCDF variable.
 *
 * @author Donald Denbo
 * @version $Revision: 1.36 $, $Date: 2004/05/06 20:53:36 $
 */
public class VariableGraph extends VariableProcessThread {
  private MyAction myAction_;
  private JButton closeButton_;
  private SGTData subset_;
  private JFrame display_;
  private JPlotLayout layout_;
  private JPanel graphPane_;
  private JPanel keyPanel_;
  private JScrollPane keyPanePanel_;
  private JPane keyPane_ = null;
  private SymAction lSymAction_;
  private JMenuItem printMenuItem_;
  private JMenuItem exitMenuItem_;
  private JMenuItem resetZoomMenuItem_;
  private JMenuItem classTreeMenuItem_;
  private int x_ = 0;
  private int y_ = 50;
  private MyMouse myMouse_;
  private LineAttributeDialog lad_ = null;
  private DomainSelector ds_;

  /** @link dependency
   * @stereotype access*/
  /*#SelectionRange lnkSelectionRange;*/

  public VariableGraph(Browser parent, String title) {
    super(parent, title);
  }

  public void run() {
    int[] shape;
    int[] origin;
    int[] index;
    int outRank = 0;
    String lname = " ";
    if(Debug.DEBUG) System.out.println("Running VariableGraph! for " + variable_);
    // do action here!
    //
    // open Informational Dialog
    //
    String title1 = ncFile_.getFileName().trim();
    JOptionPane oPane = new JOptionPane("Graphing: " + variable_ +
                                        " from " + title1,
                                        JOptionPane.INFORMATION_MESSAGE,
                                        JOptionPane.DEFAULT_OPTION);
    JDialog dPane = oPane.createDialog(parent_, "Information");
    dPane.setModal(false);
    dPane.setVisible(true);
    //
    if(ds_ != null) range_ = ds_.computeRange();
    //
    index = new int[range_.getRank()];
    shape = range_.getShape();
    for(int i=0; i < shape.length; i++) {
      if(shape[i] > 1) outRank++;
      index[i] = 0;
    }
    if(outRank < 1) {
      Variable ncVar = range_.getVariable();
      origin = range_.getOrigin();
      Array marr = null;
      double val = 0.0;
      Index ind = null;
      try {
        marr = ncVar.read(origin, shape);
        ind = marr.getIndex();
        val = marr.getDouble(ind.set(index));
      } catch (IOException e) {
        System.out.println(e);
      } catch (InvalidRangeException e) {
        e.printStackTrace();
      }
      makeTextFrame(val);
      display_.setSize(400, 90);
      display_.setLocation(x_, y_);
      display_.setVisible(true);
      parent_.getWindowList().addElement(display_);
      display_.addWindowListener(parent_.getWindowList());
    } else {
      try {
        subset_ = range_.getDataModel();
      } catch (RankNotSupportedException e) {
        System.out.println(e);
        return;
      }
      if(subset_.getXRange().isStartOrEndMissing() ||
         subset_.getYRange().isStartOrEndMissing()) {
        JOptionPane.showMessageDialog(parent_,
                                      "Data subset contains no valid data.",
                                      "Data Visualization Error",
                                      JOptionPane.ERROR_MESSAGE);
        dPane.setVisible(false);
        dPane.dispose();
        return;
      }
      layout_ = new JPlotLayout(subset_, "ncBrowse", null, true);
      //
      if(long_name_ != null) lname = long_name_;
      layout_.setTitles(title1, lname.trim(), " ");
      layout_.setLayerSizeP(new Dimension2D(6.0, 5.0));
      layout_.setTitleHeightP(0.25, 0.20);
      makeFrame();
      display_.setSize(533,600);
      display_.setLocation(x_, y_);
      parent_.getWindowList().addElement(display_);
      display_.addWindowListener(parent_.getWindowList());
      layout_.addData(subset_, subset_.getTitle());
      //
      layout_.setBatch(false, "VariableGraph");
      layout_.draw();
      //
      if(keyPane_ != null) {
        //
        keyPane_.setBatch(false, "VariableGraph");
        //
      }
      display_.setVisible(true);
    }
    //
    // close Informational Dialog
    //
    dPane.setVisible(false);
    dPane.dispose();
  }

  public void setLocation(int x, int y) {
    x_ = x;
    y_ = y;
  }

  private void makeFrame() {
    lSymAction_ = new SymAction();
    String name = ncFile_.getFileName();
    display_ = new JFrame("Graphic Display from " + name);
    //
    display_.getContentPane().setLayout(new BorderLayout(0,0));
    display_.setJMenuBar(makeMenuBar());
    //
    keyPane_ = layout_.getKeyPane();
    if(keyPane_ != null) {
      graphPane_ = new JPanel();
      keyPanel_ = new JPanel();

      graphPane_.setLayout(new BorderLayout(0,0));
      display_.getContentPane().add(graphPane_, BorderLayout.CENTER);
      graphPane_.setBackground(new Color(204,204,204));
      graphPane_.setBounds(0,4,488,669);
      graphPane_.add(layout_, BorderLayout.CENTER);
      //
      keyPanel_.setLayout(new BorderLayout());
      keyPanel_.setBorder(new javax.swing.border.LineBorder(Color.gray, 2));
      keyPane_.setSize(new Dimension(533, 100));
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

  class MyMouse extends MouseAdapter {
    public void mouseReleased(MouseEvent event) {
      Object object = event.getSource();
      if(object == keyPane_)
        maybeShowLineAttributeDialog(event);
    }

    void maybeShowLineAttributeDialog(MouseEvent e) {
      if(e.isPopupTrigger() || e.getClickCount() == 2) {
        Object obj = keyPane_.getObjectAt(e.getX(), e.getY());
        keyPane_.setSelectedObject(obj);
        if(obj instanceof LineCartesianRenderer) {
          LineAttribute attr = ((LineCartesianRenderer)obj).getLineAttribute();
          if(lad_ == null) {
            lad_ = new LineAttributeDialog();
            if(keyPane_ == null) {
              lad_.setJPane(layout_);
            } else {
              JPane[] list = {layout_, keyPane_};
              lad_.setJPaneList(list);
            }
          }
          lad_.setLineAttribute(attr);
          if(!lad_.isShowing())
            lad_.setVisible(true);
        }
      }
    }
  }

  JMenuBar makeMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JSeparator separator;

    JMenu fileMenu = new JMenu();
    fileMenu.setText("File");
    fileMenu.setMnemonic((int)'F');
    menuBar.add(fileMenu);
    printMenuItem_ = new JMenuItem();
    printMenuItem_.setText("Print...");
    printMenuItem_.setMnemonic((int)'P');
    fileMenu.add(printMenuItem_);
    separator = new JSeparator();
    fileMenu.add(separator);
    exitMenuItem_ = new JMenuItem();
    exitMenuItem_.setText("Exit");
    exitMenuItem_.setMnemonic((int)'X');
    fileMenu.add(exitMenuItem_);

    JMenu viewMenu = new JMenu();
    viewMenu.setText("View");
    viewMenu.setMnemonic((int)'V');
    menuBar.add(viewMenu);
    resetZoomMenuItem_ = new JMenuItem();
    resetZoomMenuItem_.setText("Reset Zoom");
    resetZoomMenuItem_.setMnemonic((int)'Z');
    viewMenu.add(resetZoomMenuItem_);
    separator = new JSeparator();
    viewMenu.add(separator);
    classTreeMenuItem_ = new JMenuItem();
    classTreeMenuItem_.setText("Class Tree...");
    classTreeMenuItem_.setMnemonic((int)'C');
    viewMenu.add(classTreeMenuItem_);

    exitMenuItem_.addActionListener(lSymAction_);
    printMenuItem_.addActionListener(lSymAction_);
    resetZoomMenuItem_.addActionListener(lSymAction_);
    classTreeMenuItem_.addActionListener(lSymAction_);

    return menuBar;
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
  class SymWindow extends java.awt.event.WindowAdapter {
    public void windowClosing(java.awt.event.WindowEvent event) {
      Object object = event.getSource();
      if (object == display_)
        display_WindowClosing(event);
    }
  }

  void display_WindowClosing(java.awt.event.WindowEvent event) {
    display_.setVisible(false);
    display_.dispose();		 // dispose of the Frame.
  }

  class SymAction implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent event) {
      Object object = event.getSource();
      if (object == exitMenuItem_)
        exitMenuItem_actionPerformed(event);
      else if (object == printMenuItem_)
        printMenuItem_actionPerformed(event);
      else if (object == resetZoomMenuItem_)
        resetZoomMenuItem_actionPerformed(event);
      else if (object == classTreeMenuItem_)
        classTreeMenuItem_actionPerformed(event);
    }
  }

  void exitMenuItem_actionPerformed(java.awt.event.ActionEvent event) {
    //    if(Debug.DEBUG) printSizes();
    display_.setVisible(false);
    display_.dispose();
  }
  void printMenuItem_actionPerformed(java.awt.event.ActionEvent event) {
    Color saveColor;

    if(Debug.DEBUG)printSizes();
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
      } catch (PrinterException e) {
        System.out.println("Error printing: " + e);
      }
    }
  }

  void resetZoomMenuItem_actionPerformed(java.awt.event.ActionEvent event) {
    layout_.resetZoom();
    layout_.setClipping(false);
  }

  void classTreeMenuItem_actionPerformed(java.awt.event.ActionEvent event) {
    JClassTree ct = new JClassTree();
    ct.setModal(false);
    ct.setJPane(layout_);
    ct.show();
  }

  private void makeTextFrame(double val) {
    String name = ncFile_.getFileName();
    display_ = new JFrame("Datum from " + name);
    display_.getContentPane().setLayout(new BorderLayout(0,0));
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,25,5));
    display_.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    closeButton_ = new JButton("Close");
    buttonPanel.add(closeButton_);
    JTextField textField = new JTextField(Double.toString(val));
    display_.getContentPane().add(textField, BorderLayout.CENTER);
    //
    //
    myAction_ = new MyAction();
    SymWindow aSymWindow = new SymWindow();
    display_.addWindowListener(aSymWindow);
    closeButton_.addActionListener(myAction_);
    //
  }

  class MyAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == closeButton_)
        closeButton_actionPerformed(event);
    }
  }

  void closeButton_actionPerformed(java.awt.event.ActionEvent event) {
    try {
      display_.setVisible(false);
      display_.dispose();
    } catch (java.lang.Exception e) {
    }
  }

  public void setDomainSelector(DomainSelector ds) {
    ds_ = ds;
  }

/*  public void computeRange() {
    int outRank = 0;
    NcFile ncFile = ds_.ncFile_;
    Variable ncVar = ds_.ncVar_;
    SelectionRange range = new SelectionRange(ncVar);
    range.setTimeFormat(ncFile.is624(),
                        ncFile.getTime2(),
                        ncFile.getRefDate(),
                        ncFile.getIncrement());
    if(Debug.DEBUG) {
      System.out.print("is624_ = " + ncFile.is624() +
                       ", rank = " + ds_.rank_);
      if(ncFile.getRefDate() != null) {
        System.out.println(" refDate_ = " +
                           ncFile.getRefDate().toString());
      } else {
        System.out.println(" refDate_ = null");
      }
    }
    boolean selAxis = false;
    for(int i=0; i < ds_.rank_; i++) {
      selAxis = ds_.xBox_[i].isSelected() || ds_.yBox_[i].isSelected();
      if(selAxis) outRank++;
      if(ds_.dimOrVar_[i] instanceof ucar.nc2.Dimension) {
        int len = ((ucar.nc2.Dimension)ds_.dimOrVar_[i]).getLength();
        Object anArray = new int[len];
        int min = (int)Math.round((new Double(ds_.min_[i].getText())).doubleValue());
        int max;
        if(selAxis) {
          max = (int)Math.round((new Double(ds_.max_[i].getText())).doubleValue());
        } else {
          max = min;
        }
        if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
        for(int j=0; j < len; j++) {
          ((int[])anArray)[j] = j;
        }
        range.addDimensionRange(ds_.dimOrVar_[i], anArray,
                                ds_.revBox_[i].isSelected(),
                                ds_.xBox_[i].isSelected(),
                                ds_.yBox_[i].isSelected(),
                                min, max);
      } else {
        Object anArray = null;
        try {
          anArray = ((Variable)ds_.dimOrVar_[i]).read().copyTo1DJavaArray();
        } catch (IOException e) {
          System.out.println(e);
        }
        if(ds_.isTime_[i]) {
          GeoDate min = null;
          GeoDate max = null;
          try {
            min = new GeoDate(ds_.min_[i].getText(), ds_.tFormat_);
            if(selAxis) {
              max = new GeoDate(ds_.max_[i].getText(), ds_.tFormat_);
            } else {
              max = min;
            }
          } catch (IllegalTimeValue e) {
            JOptionPane.showMessageDialog(parent_,
                                          e.toString(),
                                          "Time Format Error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
          }
          if(Debug.DEBUG) {
            System.out.println("min,max = " +
                               min.toString() + ", " + max.toString());
          }
          range.addDimensionRange(ds_.dimOrVar_[i], anArray,
                                  ds_.revBox_[i].isSelected(),
                                  ds_.xBox_[i].isSelected(),
                                  ds_.yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof long[]) {
          long min = (long)Math.round((new Double(ds_.min_[i].getText())).doubleValue());
          long max;
          if(selAxis) {
            max = (long)Math.round((new Double(ds_.max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(ds_.dimOrVar_[i], anArray,
                                  ds_.revBox_[i].isSelected(),
                                  ds_.xBox_[i].isSelected(),
                                  ds_.yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof int[]) {
          int min = (int)Math.round((new Double(ds_.min_[i].getText())).doubleValue());
          int max;
          if(selAxis) {
            max = (int)Math.round((new Double(ds_.max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(ds_.dimOrVar_[i], anArray,
                                  ds_.revBox_[i].isSelected(),
                                  ds_.xBox_[i].isSelected(),
                                  ds_.yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof short[]) {
          short min = (short)Math.round((new Double(ds_.min_[i].getText())).doubleValue());
          short max;
          if(selAxis) {
            max = (short)Math.round((new Double(ds_.max_[i].getText())).doubleValue());
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(ds_.dimOrVar_[i], anArray,
                                  ds_.revBox_[i].isSelected(),
                                  ds_.xBox_[i].isSelected(),
                                  ds_.yBox_[i].isSelected(),
                                  (short)min, (short)max);
        } else if(anArray instanceof float[]) {
          float min = (new Float(ds_.min_[i].getText())).floatValue();
          float max;
          if(selAxis) {
            max = (new Float(ds_.max_[i].getText())).floatValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(ds_.dimOrVar_[i], anArray,
                                  ds_.revBox_[i].isSelected(),
                                  ds_.xBox_[i].isSelected(),
                                  ds_.yBox_[i].isSelected(),
                                  min, max);
        } else if(anArray instanceof double[]) {
          double min = (new Double(ds_.min_[i].getText())).doubleValue();
          double max;
          if(selAxis) {
            max = (new Double(ds_.max_[i].getText())).doubleValue();
          } else {
            max = min;
          }
          if(Debug.DEBUG) System.out.println("min,max = " + min + ", " +  max);
          range.addDimensionRange(ds_.dimOrVar_[i], anArray,
                                  ds_.revBox_[i].isSelected(),
                                  ds_.xBox_[i].isSelected(),
                                  ds_.yBox_[i].isSelected(),
                                  min, max);
        } else {
          if(Debug.DEBUG) System.out.println("min,max = not found");
          range.addDimensionRange(ds_.dimOrVar_[i], anArray,
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
    range_ = range;
  } */
}

