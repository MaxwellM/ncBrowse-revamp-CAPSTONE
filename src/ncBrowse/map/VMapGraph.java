/**
 *  $Id: VMapGraph.java 15 2013-04-24 19:16:08Z dwd $
 */
/** another stupid change */
package ncBrowse.map;

import gov.noaa.pmel.sgt.*;
import gov.noaa.pmel.sgt.dm.SGTData;
import gov.noaa.pmel.sgt.dm.SGTGrid;
import gov.noaa.pmel.sgt.swing.JClassTree;
import gov.noaa.pmel.sgt.swing.JPlotLayout;
import gov.noaa.pmel.sgt.swing.prop.LineAttributeDialog;
import gov.noaa.pmel.sgt.swing.prop.VectorAttributeDialog;
import gov.noaa.pmel.util.Dimension2D;
import gov.noaa.pmel.util.Range2D;
import gov.noaa.pmel.util.Rectangle2D;
import ncBrowse.Browser;
import ncBrowse.Debug;
import ncBrowse.NcFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;


/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.19 $, $Date: 2003/10/01 22:32:28 $
 */

public class VMapGraph extends Thread implements ChangeListener {
  /**
   * @label model_
   */
  private VMapModel model_ = null;

  /**
   * @label ncFile_
   */
  private NcFile ncFile_;
  private Browser parent_;
  private JFrame display_;
  private JPlotLayout layout_;
  private JPanel graphPane_;
  private JPanel keyPanel_;
  private JScrollPane keyPanePanel_;
  private JPane keyPane_ = null;
  private SymAction lSymAction_;
  private JMenuItem printMenuItem_;
  private JMenuItem mapMenuItem_;
  private JMenuItem exitMenuItem_;
  private JMenuItem resetZoomMenuItem_;
  private JMenuItem classTreeMenuItem_;
  private int x_ = 0;
  private int y_ = 50;
  private MyMouse myMouse_;
  private LineAttributeDialog lad_ = null;
  private VectorAttributeDialog vad_ = null;
  //private boolean is3D = false;
  //private SGTData[] subset_ = null;
  //protected VisADRenderer vizRend = null;

  public VMapGraph(VMapModel model, Browser parent) {
    model_ = model;
    //is3D = model_.is3D();
    //model_.addChangeListener(this);
    ncFile_ = model_.getNcFile();
    parent_ = parent;
  }

  public void run() {
    String name = ncFile_.getFileName();
    String message;

    //
    // open Informational Dialog
    //
    message = "Creating SGTData from VMapModel for " +
      model_.getName() + " from " + name;
    JOptionPane oPane = new JOptionPane(message,
                                        JOptionPane.INFORMATION_MESSAGE,
                                        JOptionPane.DEFAULT_OPTION);
    JDialog dPane = oPane.createDialog(parent_, "Information");
    dPane.setModal(false);
    dPane.setVisible(true);
    //
    // process VMapModel
    //
    message = "Processing VMapModel for " + model_.getName() +
      " from " + name;
    oPane.setMessage(message);
    model_.process();
    //
    // for now initialize and show the editor
    //
    message = "Initializing VMapModelEditor for " + model_.getName() +
      " from " + name;
    oPane.setMessage(message);
    model_.getModelEditor().init();
    model_.getModelEditor().setVisible(true);
    //
    // get data subset
    //
    message = "Creating SGTData from VMapModel for " + model_.getName() +
      " from " + name;
    oPane.setMessage(message);
    //subset_ = model_.getSGTData();
    
  /*  if (!is3D) {
	    //
	    // create graph frame
	    //
	    message = "Creating graphics frame for " + model_.getName() +
	      " from " + name;
	    oPane.setMessage(message);
	    String title1 = ncFile_.getFileName().trim();
	    layout_ = new JPlotLayout(subset_[0], "ncBrowse", null, true);
	    //
	    //    layout_.setBatch(true, "VMapGraph");
	    //
	    String lname = model_.getName();
	    layout_.setTitles(title1, lname.trim(), " ");
	    layout_.setLayerSizeP(new Dimension2D(6.0, 5.0));
	    layout_.setTitleHeightP(0.25, 0.20);
	    makeFrame();
	    display_.setSize(533,600);
	    display_.setLocation(x_, y_);
	    parent_.getWindowList().addElement(display_);
	    display_.addWindowListener(parent_.getWindowList());
	    //      layout_.addData(subset_, subset_.getTitle());
	    Attribute attr = null;
	    if(subset_[0] instanceof SGTLine) {
	    } 
	    else if(subset_[0] instanceof SGTGrid) {
	    } 
	    else if(subset_[0] instanceof SGTVector) {
	      SGTVector vec = (SGTVector)subset_[0];
	      Range2D uRange = vec.getU().getZRange();
	      Range2D vRange = vec.getV().getZRange();
	      double umax = Math.max(Math.abs(uRange.start),Math.abs(uRange.end));
	      double vmax = Math.max(Math.abs(vRange.start),Math.abs(vRange.end));
	      // scale = (physical units)/(user units)
	      double scale = 0.3/Math.sqrt(umax*umax + vmax*vmax);
	      // make a "nice" scale
	      double temp = scale;
	      int nt = (int)(0.4342944819*Math.log(temp));
	      if(temp < 1.0) nt--;
	      double pow = Math.pow(10.0, (double)nt);
	      temp = temp/pow;
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
	}
	else {
	    if (System.getProperty("ncBrowse.visad").equals("true")) {
	    	if (model_.is3DGridData()) {
	   			subset_ = model_.getSGTData();
		    	vizRend = new VisADSurfaceRenderer(subset_[0], subset_[1], model_);
		    	
		    	// want to plug in other VisAD renderers here
		    	// need to know what kind of 3D plot we have here
			    try {
			    	vizRend.draw();
			    }
			    catch (Exception ex) {
			    	ex.printStackTrace();
			    }
			}
			else if (!model_.isVolumeData() && model_.is3DLineData()) {
	   			subset_ = model_.getSGTData();
	   			// just pass in the same subset for color for now
		    	vizRend = new VisAD3DLineRenderer(subset_[0], subset_[1], model_);
		    	
		    	// want to plug in other VisAD renderers here
		    	// need to know what kind of 3D plot we have here
			    try {
			    	vizRend.draw();
			    }
			    catch (Exception ex) {
			    	ex.printStackTrace();
			    }
			}
			else if (model_.is3DVectorData()) {
		   		subset_ = model_.getSGTData();
				if (!model_.isFull3DVectorData()) {
			    	vizRend = new VisAD3DVectorRenderer(subset_[0], null, model_);
			    }
			    else {
			    	vizRend = new VisADFull3DVectorRenderer(subset_[0], model_);
			    }
				try {
			    	vizRend.draw();
			    }
			    catch (Exception ex) {
			    	ex.printStackTrace();
			    }
			}
			else if (model_.isVolumeData()) {
		   		subset_ = model_.getSGTData();
			    vizRend = new VisAD3DVolumeRenderer((ThreeDGrid)subset_[0], model_);
				try {
			    	vizRend.draw();
			    }
			    catch (Exception ex) {
			    	ex.printStackTrace();
			    }
			
			}
	    } 
	}*/
    //
    // close Informational Dialog
    //
    dPane.setVisible(false);
    dPane.dispose();
  }

  public void stateChanged(ChangeEvent e) {
    if(Debug.DEBUG) 
    System.out.println("VMapGraph.stateChanged()");
    Object obj = e.getSource();
    if(obj instanceof VMapModel) {
		VMapModel vmm = (VMapModel)obj;
    	if (!vmm.is3D()) {
			SGTData dobj = ((VMapModel)obj).dataset[0];
			if(dobj instanceof SGTGrid) {
				GridAttribute gAttr;
				try {
					gAttr = (GridAttribute)layout_.getAttribute((SGTData)dobj);
					if(gAttr.isRaster()) {
						Range2D vRange = ((SGTGrid)dobj).getZRange();
						ColorMap cmap = gAttr.getColorMap();
						if(cmap instanceof TransformAccess) {
							((TransformAccess)cmap).setRange(vRange);
						}
					}
				} catch (DataNotFoundException ev) {
					ev.printStackTrace();
				}
	    	}
		    layout_.resetZoom();
		    layout_.setClipping(false);
	    }
	 }
  }

  public void setLocation(int x, int y) {
    x_ = x;
    y_ = y;
  }

  private void makeFrame() {
    lSymAction_ = new SymAction();
    
    // set the window name
    String name = ncFile_.getFileName();
    String s = model_.getName();
    display_ = new JFrame(s + " from " + name);
    display_.getContentPane().setLayout(new BorderLayout(0, 0));
    display_.setJMenuBar(makeMenuBar());
    //display_.setLocationRelativeTo(null);
    //
    keyPane_ = layout_.getKeyPane();
    //    layout_.setBatch(true, "VMapGraph");
    if(keyPane_ != null) {
      //      keyPane_.setBatch(true, "VMapGraph");
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
        } else if(obj instanceof VectorCartesianRenderer) {
          VectorAttribute attr = ((VectorCartesianRenderer)obj).getVectorAttribute();
          if(vad_ == null) {
            vad_ = new VectorAttributeDialog();
            if(keyPane_ == null) {
              vad_.setJPane(layout_);
            } else {
              JPane[] list = {layout_, keyPane_};
              vad_.setJPaneList(list);
            }
          }
          vad_.setVectorAttribute(attr);
          if(!vad_.isShowing())
            vad_.setVisible(true);
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

    JMenu editMenu = new JMenu();
    editMenu.setText("Edit");
    editMenu.setMnemonic((int)'E');
    menuBar.add(editMenu);
    mapMenuItem_ = new JMenuItem();
    mapMenuItem_.setText("Variable Map...");
    mapMenuItem_.setMnemonic((int)'V');
    editMenu.add(mapMenuItem_);

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
    mapMenuItem_.addActionListener(lSymAction_);
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

  class SymWindow extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
      Object object = event.getSource();
      if (object == display_)
        display_WindowClosing(event);
    }
  }

  void display_WindowClosing(WindowEvent event) {
    display_.setVisible(false);
    display_.dispose();		 // dispose of the Frame.
  }

  class SymAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == exitMenuItem_)
        exitMenuItem_actionPerformed(event);
      else if (object == printMenuItem_)
        printMenuItem_actionPerformed(event);
      else if (object == mapMenuItem_)
        mapMenuItem_actionPerformed(event);
      else if (object == resetZoomMenuItem_)
        resetZoomMenuItem_actionPerformed(event);
      else if (object == classTreeMenuItem_)
        classTreeMenuItem_actionPerformed(event);
    }
  }

  void exitMenuItem_actionPerformed(ActionEvent event) {
    display_.setVisible(false);
    display_.dispose();
  }
  void printMenuItem_actionPerformed(ActionEvent event) {
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
    //ct.show();
  }
}
