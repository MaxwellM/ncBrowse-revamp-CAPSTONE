/**
 *  $Id: VisADPlotRenderer.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.VisAD;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.*;
import visad.*;
import visad.java3d.DisplayImplJ3D;
import visad.java3d.ProjectionControlJ3D;
import java.rmi.RemoteException;
import gov.noaa.pmel.sgt.dm.*;
import gov.noaa.pmel.util.SoTRange;
import gov.noaa.pmel.util.GeoDate;
import ncBrowse.map.*;
import gov.noaa.pmel.sgt.GridAttribute;
import gov.noaa.pmel.util.Range2D;
import visad.util.*;
import java.awt.event.*;
import java.awt.*;
import ncBrowse.*;
import java.awt.print.*;
import javax.swing.*;
import com.visualtek.png.*;
import java.io.*;


/**
 * <pre>
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author John Osborne
 * @version $Revision: 1.2 $, $Date: 2003/10/01 22:32:28 $
 */

public abstract class VisADPlotRenderer implements ChangeListener, ActionListener, VisADRenderer, DisplayListener, DialogClient {
	protected MenuBar3D mMenuBar = null;
	protected boolean isRotatingX = false;
	protected boolean isRotatingY = false;
	protected boolean isRotatingZ = false;
	protected boolean isWobblingX = false;
	protected boolean isWobblingY = false;
	protected boolean isWobblingZ = false;
	protected int wobbleCount = 10;
	protected double sign = -1.0;
	protected DisplayImpl display;
	protected JFrame mFrame;
	protected boolean mCurrAspectPolicy = false;
	protected float mCurrXScaling = 0.0f;
	protected float mCurrYScaling = 0.0f;
	protected float mCurrZScaling = 0.0f;
  	protected VMapModel mModel;

    public VisADPlotRenderer() {
    }
    
    public abstract void stateChanged(ChangeEvent e);
    
    public JFrame getDisplayWindow() {
    	return mFrame;
    }
    
    public boolean canDisplayLegend() {
    	return true;
    }
    
    public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
    	ProjectionControlJ3D projCntrl = (ProjectionControlJ3D)display.getProjectionControl();
		Object object = e.getSource();
		
		try {
			if (cmd.equals("print")) {
				printGraph();
			}
			else if (cmd.equals("saveas")) {
				saveAsPNG();
			}
			else if (cmd.equals("varmap")) {
				if (mModel != null) {
					VMapModelEditor vmme = mModel.getModelEditor();
					if (!vmme.isShowing())
						vmme.setVisible(true);
				}
			}
			else if (cmd.equals("xfacepos")) {
				projCntrl.setOrthoView(ProjectionControlJ3D.X_PLUS);
			}
			else if (cmd.equals("xfaceneg")) {
				projCntrl.setOrthoView(ProjectionControlJ3D.X_MINUS);
			}
			else  if (cmd.equals("yfacepos")) {
				projCntrl.setOrthoView(ProjectionControlJ3D.Y_PLUS);
			}
			else  if (cmd.equals("yfaceneg")) {
				projCntrl.setOrthoView(ProjectionControlJ3D.Y_MINUS);
			}
			else  if (cmd.equals("zfacepos")) {
				projCntrl.setOrthoView(ProjectionControlJ3D.Z_PLUS);
			}
			else  if (cmd.equals("zfaceneg")) {
				projCntrl.setOrthoView(ProjectionControlJ3D.Z_MINUS);
			}
			else if (cmd.equals("customorient")) {
				showConfigDialog();
			}
			else if (object instanceof JCheckBoxMenuItem) {
				JCheckBoxMenuItem cbmi = (JCheckBoxMenuItem)object;
				mMenuBar.clearBG();
				
				if (cmd.equals("rotatex")) {
					// rotate around the z axis
					isRotatingX = !isRotatingX;
					isRotatingY = false;
					isRotatingZ = false;
					isWobblingX = false;
					isWobblingY = false;
					isWobblingZ = false;
					if (isRotatingX)
						cbmi.setSelected(true);
					else
						mMenuBar.clearBG();
				}
				else  if (cmd.equals("rotatey")) {
					// rotate around the z axis
					isRotatingY = !isRotatingY;
					isRotatingX = false;
					isRotatingZ = false;
					isWobblingX = false;
					isWobblingY = false;
					isWobblingZ = false;
					if (isRotatingY)
						cbmi.setSelected(true);
					else
						mMenuBar.clearBG();
				}
				else  if (cmd.equals("rotatez")) {
					// rotate around the z axis
					isRotatingZ = !isRotatingZ;
					isRotatingY = false;
					isRotatingX = false;
					isWobblingX = false;
					isWobblingY = false;
					isWobblingZ = false;
					if (isRotatingZ)
						cbmi.setSelected(true);
					else
						mMenuBar.clearBG();
				}
				else  if (cmd.equals("wobblex")) {
					// wobble around the z axis
					wobbleCount = 10;
					isRotatingX = false;
					isRotatingY = false;
					isRotatingZ = false;
					isWobblingX = !isWobblingX;
					isWobblingY = false;
					isWobblingZ = false;
					if (isWobblingX)
						cbmi.setSelected(true);
					else
						mMenuBar.clearBG();
				}
				else  if (cmd.equals("wobbley")) {
					// wobble around the z axis
					wobbleCount = 10;
					isRotatingX = false;
					isRotatingY = false;
					isRotatingZ = false;
					isWobblingY = !isWobblingY;
					isWobblingX = false;
					isWobblingZ = false;
					if (isWobblingY)
						cbmi.setSelected(true);
					else
						mMenuBar.clearBG();
				}
				else  if (cmd.equals("wobblez")) {
					// wobble around the z axis
					wobbleCount = 10;
					isRotatingX = false;
					isRotatingY = false;
					isRotatingZ = false;
					isWobblingZ = !isWobblingZ;
					isWobblingX = false;
					isWobblingY = false;
					if (isWobblingZ)
						cbmi.setSelected(true);
					else
						mMenuBar.clearBG();
				}
			}
		}
		catch (Exception ex) {}
	}

	public void displayChanged(DisplayEvent e) throws RemoteException, VisADException {
		if (e.getId() == DisplayEvent.FRAME_DONE) {
			if (isWobblingX || isWobblingY || isWobblingZ || isRotatingX || isRotatingY || isRotatingZ) {
				double rotx = 1.0;
				double roty = 0.0;
				double rotz = 0.0;
				if (isRotatingX) {
					rotx = 1.0;
					roty = 0.0;
					rotz = 0.0;
				}
				else if (isRotatingY) {
					rotx = 0.0;
					roty = 1.0;
					rotz = 0.0;
				}
				else if (isRotatingZ) {
					rotx = 0.0;
					roty = 0.0;
					rotz = 1.0;
				}
				else if (isWobblingX) {
					roty = 0.0;
					rotz = 0.0;
					if (wobbleCount % 20 == 0)
						sign = -sign;
					rotx = sign;
				}
				else if (isWobblingY) {
					rotx = 0.0;
					rotz = 0.0;
					if (wobbleCount % 20 == 0)
						sign = -sign;
					roty = sign;
				}
				else if (isWobblingZ) {
					rotx = 0.0;
					roty = 0.0;
					if (wobbleCount % 20 == 0)
						sign = -sign;
					rotz = sign;
				}
	    		ProjectionControlJ3D projCntrl = (ProjectionControlJ3D)display.getProjectionControl();
			    double[] matrix = projCntrl.getMatrix();
			    double[] mult = display.make_matrix(rotx, roty, rotz, 1.0, 0.0, 0.0, 0.0);
				projCntrl.setMatrix(display.multiply_matrix(mult, matrix));
				wobbleCount++;
			}
		}
	}
	
	public void rotateView(double azimuth, double decAngle) {

        //if (display getDisplayMode() != MODE_3D) return;
        // trap input bad values - not necessary since trig
        // functions handle values outside of 0-360 properly.
        // rotation around z axis, made from user's "azimuth"
        double zAngle = 180.0 - azimuth;
    	ProjectionControlJ3D projCntrl = (ProjectionControlJ3D)display.getProjectionControl();

        try {
            // rotate in z
            double[] aziMatrix = display.make_matrix(0.0, 0.0, zAngle, 1.0, 0.0, 0.0, 0.0);
            double[] combo = display.multiply_matrix(aziMatrix, projCntrl.getMatrix());

            // rotate in x
            double[] decMatrix =display.make_matrix(decAngle, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0);

            // total rotation matrix is computed and applied
            double[] combo2 = display.multiply_matrix(decMatrix, combo);

            projCntrl.setMatrix(combo2);

        } catch (Exception exp) {
            System.out.println("rotate view got " + exp); 
        }
    }
    
	public void showConfigDialog() {
		// show configuration dialog
		ConfigCustomOrientation cp = new ConfigCustomOrientation(new JFrame(),  this);
		cp.pack();
		cp.setVisible(true);	
	}
		
	// OK Button
    public void dialogDismissed(JDialog d) {
    	// get new azimuth and declination
    	double azmuth = ((ConfigCustomOrientation)d).getAzimuth();
    	double decl = ((ConfigCustomOrientation)d).getDeclination();
    	rotateView(azmuth, decl);
    }
    
    // Cancel button
    public void dialogCancelled(JDialog d) {
    }
    
    // something other than the OK button 
    public void dialogDismissedTwo(JDialog d){
    
    }

    public void dialogApplyTwo(Object d) {
    }
    
    // Apply button, OK w/o dismissing the dialog
    public void dialogApply(JDialog d){
    	// get new azimuth and declination
    	double azmuth = ((ConfigCustomOrientation)d).getAzimuth();
    	double decl = ((ConfigCustomOrientation)d).getDeclination();
    	rotateView(azmuth, decl);
    }
    
    public void saveAsPNG() {
    	class BasicThread extends Thread {
	    	// ask for filename
	    	public void run() {
		    	Image image = display.getImage(true);
		    	
		    	FilenameFilter filter = new FilenameFilter() {
		    		public boolean accept(File dir, String name) {
		    			if (name.endsWith("png"))
		    				return true;
		    			else
		    				return false;
		    		}
		    	};
		    	
			    Frame fr = new Frame();
				String directory = System.getProperty("user.dir");// + File.separator + "JOA_Support" + File.separator;
			    FileDialog f = new FileDialog(fr, "Save image as:", FileDialog.SAVE);
			    f.setDirectory(directory);
			    f.setFilenameFilter(filter);
			    f.setFile("untitled.png");
			    f.show();
			    directory = f.getDirectory();
			    f.dispose();
			    if (directory != null && f.getFile() != null) {
					String path = directory + File.separator + f.getFile();
			    	try {
			    		(new PNGEncoder(image, path)).encode();
			    	}
			    	catch (Exception ex) {
			    		ex.printStackTrace();
			    	}
			    	
		        }
		    }
	    }

	  // Create a thread and run it
	  Thread thread = new BasicThread();
	  thread.start();
    }
    
    public void printGraph() {
	  class BasicThread extends Thread {
	    public void run() {
	      PrinterJob printJob = PrinterJob.getPrinterJob();
	      Printable p = display.getPrintable();
	      printJob.setPrintable(p);

	      try {
	        if(printJob.printDialog()) printJob.print();
	      } catch(PrinterException printexception)
	          { System.out.println("PrinterException"); }
	    }
	  }

	  // Create a thread and run it
	  Thread thread = new BasicThread();
	  thread.start();
	}


  /* Utility method to transform a Java color in
     an array of rgb components between 0 and 1*/
  protected float[] colorToFloats(Color c){

    float[] rgb = new float[]{0.5f,0.5f,0.5f};  //init with gray
    if(c != null){
      rgb[0] = (float) c.getRed()/255.0f;
      rgb[1] = (float) c.getGreen()/255.0f;
      rgb[2] = (float) c.getBlue()/255.0f;

    }

    return rgb;
  }
}