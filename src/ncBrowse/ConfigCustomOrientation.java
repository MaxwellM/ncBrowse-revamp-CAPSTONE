package ncBrowse;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.beans.*;
import java.util.zip.*;
import javax.swing.border.*;

public class ConfigCustomOrientation extends JDialog implements ActionListener {
	protected Component mComp;
    protected JButton mOKBtn = null;
    protected JButton mCancelButton = null;
    protected JButton mApplyButton = null;
    protected JTextField mAzimuthFld = null;
    protected JTextField mDeclinationFld = null;
	protected DialogClient mClient = null;
	protected JDialog mFrame = null;
	private ResourceBundle b = ResourceBundle.getBundle("ncBrowse.NcBrowseResources");
	
    public ConfigCustomOrientation(JFrame parent, DialogClient client) {
    	super(parent, "", false);
    	this.setTitle(b.getString("kCustomOrientation"));
		mClient = client;
		this.init();

    	mFrame = this;
		WindowListener windowListener = new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				mClient.dialogCancelled(mFrame);
			}
		};
		this.addWindowListener(windowListener);
	}

    public void init() {
		ResourceBundle b = ResourceBundle.getBundle("ncBrowse.NcBrowseResources");
		
    	// create the two parameter chooser lists
    	Container contents = this.getContentPane();
    	this.getContentPane().setLayout(new BorderLayout(0, 0));
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new BorderLayout(0, 0));

    	// Ranges goes in the middle of the middle panel
    	// y axis
    	JPanel line0 = new JPanel();
	    line0.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
    	line0.add(new JLabel(b.getString("kAzimuth")));
    	mAzimuthFld = new JTextField(6);
		mAzimuthFld.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    	line0.add(mAzimuthFld);
    	line0.add(new JLabel(b.getString("kDeclination")));
    	mDeclinationFld = new JTextField(6);
		mDeclinationFld.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    	line0.add(mDeclinationFld);
	    mAzimuthFld.setText(String.valueOf(0.0));
	    mDeclinationFld.setText(String.valueOf(0.0));

    	// build upper part of dialog
    	mainPanel.add("Center", new TenPixelBorder(line0, 5, 5, 5, 5));
    	
		// lower panel
    	mOKBtn = new JButton(b.getString("kOK"));
		mOKBtn.setActionCommand("ok");
    	mCancelButton = new JButton(b.getString("kCancel"));
		mCancelButton.setActionCommand("cancel");
    	mApplyButton = new JButton(b.getString("kApply"));
		mApplyButton.setActionCommand("apply");
		JPanel dlgBtnsInset = new JPanel();
		JPanel dlgBtnsPanel = new JPanel();
        dlgBtnsInset.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 1));
        dlgBtnsPanel.setLayout(new GridLayout(1, 4, 15, 1));
    	dlgBtnsPanel.add(mOKBtn);
    	dlgBtnsPanel.add(mApplyButton);
    	dlgBtnsPanel.add(mCancelButton);
        dlgBtnsInset.add(dlgBtnsPanel);
        
        mOKBtn.addActionListener(this);
        mApplyButton.addActionListener(this);
        mCancelButton.addActionListener(this);
        
        mainPanel.add(new TenPixelBorder(dlgBtnsInset, 5, 5, 5, 5), "South");
        contents.add("Center", mainPanel);
        this.pack();
		
		// show dialog at center of screen
		Rectangle dBounds = this.getBounds();
		Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
		int x = sd.width/2 - dBounds.width/2;
		int y = sd.height/2 - dBounds.height/2;
		this.setLocation(x, y);
    }
    
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals("cancel")) {
			mClient.dialogCancelled(this);
			this.dispose();
		}
		else if (cmd.equals("ok")) {
			mClient.dialogDismissed(this);
			this.dispose();
		}
		else if (cmd.equals("apply")) {
			mClient.dialogApply(this);
		}
	}
	
    public double getAzimuth() {
		String fldText = mAzimuthFld.getText();
		double azimuth;
		if (fldText.length() == 0)
			azimuth = 0.0;
		else {
			try {
				azimuth = Double.valueOf(fldText).doubleValue();
			}
			catch (NumberFormatException ex) {
				azimuth = 0.0;
			}
		}
		return azimuth;
    }
    
    public double getDeclination() {
		String fldText = mDeclinationFld.getText();
		double declination;

		if (fldText.length() == 0)
			declination = 0.0;
		else {
			try {
				declination = Double.valueOf(fldText).doubleValue();
			}
			catch (NumberFormatException ex) {
				declination = 0.0;
			}
		}
    	return declination;
    }
}

