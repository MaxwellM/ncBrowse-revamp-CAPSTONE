package ncBrowse;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.beans.*;
import java.util.zip.*;

public class MenuBar3D {
	ActionListener mThis;
	JFrame mFrame;
	JMenuItem mXFacePos;
	JMenuItem mXFaceNeg;
	JMenuItem mYFacePos;
	JMenuItem mYFaceNeg;
	JMenuItem mZFacePos;
	JMenuItem mZFaceNeg;
	JCheckBoxMenuItem mRotateX;
	JCheckBoxMenuItem mWobbleX;
	JCheckBoxMenuItem mRotateY;
	JCheckBoxMenuItem mWobbleY;
	JCheckBoxMenuItem mRotateZ;
	JCheckBoxMenuItem mWobbleZ;
	JMenuItem mPrint;
	JMenuItem mSaveAs;
	JMenuItem mCustom;
	JMenuItem mVariableMap;
	boolean mEnablePrinting = false;
	Vector mBG = null;
	
	public MenuBar3D(JFrame frame, ActionListener c, boolean enablePrinting) {
		mThis = c;
		mFrame = frame;
		mEnablePrinting = enablePrinting;
    	buildJMenuBar();
		addJListeners();
	}

	public void buildJMenuBar() {
		JPopupMenu.setDefaultLightWeightPopupEnabled( false );
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled( false );
		ResourceBundle b = ResourceBundle.getBundle("ncBrowse.NcBrowseResources");
		JMenuBar menubar = new JMenuBar();

		// create the menus
		JMenu file = new JMenu(b.getString("kFile"));
		JMenu edit = new JMenu(b.getString("kEditMenu"));
		JMenu orient = new JMenu(b.getString("kOrientation"));
		
		// add the file menu items
		mPrint = new JMenuItem(b.getString("kPrint"));
		mPrint.setActionCommand("print");
		
		mSaveAs = new JMenuItem(b.getString("kSaveAsPNG"));
		mSaveAs.setActionCommand("saveas");
		
		mVariableMap = new JMenuItem(b.getString("kVariableMap"));
		mVariableMap.setActionCommand("varmap");
		
		// add orientation menu items
		mXFacePos = new JMenuItem(b.getString("kXFacePos"));
		mXFacePos.setActionCommand("xfacepos");
		mXFaceNeg = new JMenuItem(b.getString("kXFaceNeg"));
		mXFaceNeg.setActionCommand("xfaceneg");
		
		mYFacePos = new JMenuItem(b.getString("kYFacePos"));
		mYFacePos.setActionCommand("yfacepos");
		mYFaceNeg = new JMenuItem(b.getString("kYFaceNeg"));
		mYFaceNeg.setActionCommand("yfaceneg");
		
		mZFacePos = new JMenuItem(b.getString("kZFacePos"));
		mZFacePos.setActionCommand("zfacepos");
		mZFaceNeg = new JMenuItem(b.getString("kZFaceNeg"));
		mZFaceNeg.setActionCommand("zfaceneg");
		
		mRotateX = new JCheckBoxMenuItem(b.getString("kRotateX"));
		mRotateX.setActionCommand("rotatex");
		
		mRotateY = new JCheckBoxMenuItem(b.getString("kRotateY"));
		mRotateY.setActionCommand("rotatey");
		
		mRotateZ = new JCheckBoxMenuItem(b.getString("kRotateZ"));
		mRotateZ.setActionCommand("rotatez");
		
		mWobbleX = new JCheckBoxMenuItem(b.getString("kWobbleX"));
		mWobbleX.setActionCommand("wobblex");
		
		mWobbleY = new JCheckBoxMenuItem(b.getString("kWobbleY"));
		mWobbleY.setActionCommand("wobbley");
		
		mWobbleZ = new JCheckBoxMenuItem(b.getString("kWobbleZ"));
		mWobbleZ.setActionCommand("wobblez");
		
		mCustom = new JMenuItem(b.getString("kCustomOrient"));
		mCustom.setActionCommand("customorient");
		
		mBG = new Vector();
		mBG.add(mRotateX);
		mBG.add(mRotateY);
		mBG.add(mRotateZ);
		mBG.add(mWobbleX);
		mBG.add(mWobbleY);
		mBG.add(mWobbleZ);
		
		edit.add(mVariableMap);

		orient.add(mXFacePos);
		orient.add(mXFaceNeg);
		orient.addSeparator();
		orient.add(mYFacePos);
		orient.add(mYFaceNeg);
		orient.addSeparator();
		orient.add(mZFacePos);
		orient.add(mZFaceNeg);
		orient.addSeparator();
		orient.add(mRotateX);
		orient.add(mRotateY);
		orient.add(mRotateZ);
		orient.add(mWobbleX);
		orient.add(mWobbleY);
		orient.add(mWobbleZ);
		orient.addSeparator();
		orient.add(mCustom);

		file.add(mPrint);
		file.add(mSaveAs);
		
		// add to the menubar
		menubar.add(file);
		menubar.add(edit);
		menubar.add(orient);
		
		menubar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		mFrame.setJMenuBar(menubar);
	}
	
	//public MenuBar getAWTMenuBar() {
	//	return mATWmenubar;
	//}
	
	
	public void buildAWTMenuBar() {}
	
	public void addJListeners() {
	    // add the listeners
	    mXFacePos.addActionListener((ActionListener)mThis);
	    mXFaceNeg.addActionListener((ActionListener)mThis);
	    mYFacePos.addActionListener((ActionListener)mThis);
	    mYFaceNeg.addActionListener((ActionListener)mThis);
	    mZFacePos.addActionListener((ActionListener)mThis);
	    mZFaceNeg.addActionListener((ActionListener)mThis);
	    mRotateX.addActionListener((ActionListener)mThis);
	    mWobbleX.addActionListener((ActionListener)mThis);
	    mRotateY.addActionListener((ActionListener)mThis);
	    mWobbleY.addActionListener((ActionListener)mThis);
	    mRotateZ.addActionListener((ActionListener)mThis);
	    mWobbleZ.addActionListener((ActionListener)mThis);
	    mCustom.addActionListener((ActionListener)mThis);
	    mPrint.addActionListener((ActionListener)mThis);
	    mSaveAs.addActionListener((ActionListener)mThis);
	    mVariableMap.addActionListener((ActionListener)mThis);
	}
	
	public void addAWTListeners() {}
	
	public void clearBG() {
		for (int i=0; i< mBG.size(); i++) {
        	JCheckBoxMenuItem jrb = (JCheckBoxMenuItem)mBG.elementAt(i);
			jrb.setSelected(false);
		}
	}
}