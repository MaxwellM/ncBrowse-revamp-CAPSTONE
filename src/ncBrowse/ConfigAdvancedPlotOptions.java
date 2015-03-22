package ncBrowse;

import ncBrowse.VisAD.VisADPlotSpecification;
import ncBrowse.map.VMapModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class ConfigAdvancedPlotOptions extends JDialog implements ActionListener, ButtonMaintainer {
	protected Component mComp;
	protected JButton mOKBtn = null;
	protected JButton mCancelButton = null;
	protected DialogClient mClient = null;
	protected JDialog mFrame = null;
	private JCheckBox mResampleAxes;
	private JCheckBox mIncludeU;
	private JCheckBox mIncludeV;
	private JCheckBox mIncludeW;
	private JRadioButton mWeightedAverage;
	private JRadioButton mNearestNeighbor;
	private JLabel mLabel1;
	private JLabel mLabel2;
	private JLabel mLabel3;
	private JLabel mLabel4;
	private JLabel mLabel5;
	private JLabel mLabel6;
	private JLabel mLabel9;
	private JCheckBox mRespectAspectRatio;
	private JTextField mVectorScaleTextField;
	private JTextField mXScalingTextField;
	private JTextField mYScalingTextField;
	private JTextField mZScalingTextField;
	private VMapModel mModel;
	private boolean mResampleAxesFlag;
	private int mResampleMode;
	private double mVectorScale;
	private boolean mUIncluded;
	private boolean mVIncluded;
	private boolean mWIncluded;
	private boolean mRespectAspectRatioFlag;
	private double mXScaling;
	private double mYScaling;
	private double mZScaling;
	private ResourceBundle b = ResourceBundle.getBundle("ncBrowse.NcBrowseResources");

    public ConfigAdvancedPlotOptions(JFrame parent, DialogClient client, VMapModel vmm,
    		boolean resamp, int resampmode, double vscale, boolean includeu, boolean includev, boolean includew,
    		boolean respectaspect, double xscaling, double yscaling, double zscaling) {
    	super(parent, "", false);
    	this.setTitle(b.getString("k3DAdvancedOptions"));
		mClient = client;
		mModel = vmm;
		mResampleAxesFlag = resamp;
		mResampleMode = resampmode;
		mVectorScale = vscale;
		mUIncluded = includeu;
		mVIncluded = includev;
		mWIncluded = includew;
		mRespectAspectRatioFlag = respectaspect;
		mXScaling = xscaling;
		mYScaling = yscaling;
		mZScaling = zscaling;
		this.init();

    	mFrame = this;
		WindowListener windowListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				mClient.dialogCancelled(mFrame);
			}
		};
		this.addWindowListener(windowListener);
	}

    public void init() {
    	// create the two parameter chooser lists
    	Container contents = this.getContentPane();
    	this.getContentPane().setLayout(new BorderLayout(0, 0));
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new BorderLayout(0, 0));
    	
    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new ColumnLayout(Orientation.LEFT, Orientation.CENTER, 5));
	
    JPanel line1a = new JPanel();
    line1a.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
	mRespectAspectRatio = new JCheckBox(b.getString("kRespectAspectRatio"), mRespectAspectRatioFlag);
	line1a.add(mRespectAspectRatio);

	JPanel line1b = new JPanel();
    line1b.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    mLabel9 = new JLabel("     " + b.getString("kScaleFactor"));
	line1b.add(mLabel9);
    mLabel4 = new JLabel(b.getString("kXScaling"));
	line1b.add(mLabel4);
	mXScalingTextField = new JTextField(String.valueOf(mXScaling), 5);
	line1b.add(mXScalingTextField);
    mLabel5 = new JLabel(b.getString("kYScaling"));
	line1b.add(mLabel5);
	mYScalingTextField = new JTextField(String.valueOf(mYScaling), 5);
	line1b.add(mYScalingTextField);
    mLabel6 = new JLabel(b.getString("kZScaling"));
	line1b.add(mLabel6);
	mZScalingTextField = new JTextField(String.valueOf(mZScaling), 5);
	line1b.add(mZScalingTextField);
	
	// add CB for axis resampling
    JPanel line2 = new JPanel();
    line2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
	mResampleAxes = new JCheckBox(b.getString("kResample"), mResampleAxesFlag);
	line2.add(mResampleAxes);
	
    JPanel line2a = new JPanel();
    line2a.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
	mWeightedAverage = new JRadioButton(b.getString("kWeightedAverage"), mResampleMode == VisADPlotSpecification.WEIGHTED_AVERAGE);
	mNearestNeighbor = new JRadioButton(b.getString("kNearestNeighbor"), mResampleMode == VisADPlotSpecification.NEAREST_NEIGHBOR);
	ButtonGroup bg2a = new ButtonGroup();
	bg2a.add(mWeightedAverage);
	bg2a.add(mNearestNeighbor);
	mLabel1 = new JLabel("     " + b.getString("kSamplingMode"));
	line2a.add(mLabel1);
	line2a.add(mWeightedAverage);
	line2a.add(mNearestNeighbor);
	
    JPanel line3 = new JPanel();
    line3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    mLabel3 = new JLabel(b.getString("kVectorScale"));
	line3.add(mLabel3);
	mVectorScaleTextField = new JTextField(String.valueOf(mVectorScale), 8);
	line3.add(mVectorScaleTextField);
	mLabel2 = new JLabel(b.getString("kIncludeComponents"));
	line3.add(mLabel2);
	mIncludeU = new JCheckBox(b.getString("kU"), mUIncluded);
	line3.add(mIncludeU);
	mIncludeV = new JCheckBox(b.getString("kV"), mVIncluded);
	line3.add(mIncludeV);
	mIncludeW = new JCheckBox(b.getString("kW"), mWIncluded);
	line3.add(mIncludeW);
		
	optionsPanel.add(line1a);
	optionsPanel.add(line1b);
	optionsPanel.add(line2);
	optionsPanel.add(line2a);
	optionsPanel.add(line3);
	
    mainPanel.add(optionsPanel, BorderLayout.SOUTH);
	
	// set initial enabled state of options
	mRespectAspectRatio.setEnabled(mRespectAspectRatioFlag);
	setResampleState(mResampleAxesFlag);
	setResampleOptState(mResampleAxesFlag);
	setVectorOptState(false);
	setScalingOptState(false);

    	// build upper part of dialog
    	mainPanel.add("Center", new TenPixelBorder(optionsPanel, 5, 5, 5, 5));
    	
		// lower panel
    	mOKBtn = new JButton(b.getString("kOK"));
		mOKBtn.setActionCommand("ok");
    	mCancelButton = new JButton(b.getString("kCancel"));
		mCancelButton.setActionCommand("cancel");
		JPanel dlgBtnsInset = new JPanel();
		JPanel dlgBtnsPanel = new JPanel();
        dlgBtnsInset.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 1));
        dlgBtnsPanel.setLayout(new GridLayout(1, 4, 15, 1));
    	dlgBtnsPanel.add(mOKBtn);
    	dlgBtnsPanel.add(mCancelButton);
        dlgBtnsInset.add(dlgBtnsPanel);
        
        mOKBtn.addActionListener(this);
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

		MaintenanceTimer mMaintain = new MaintenanceTimer(this, 100);
		mMaintain.startMaintainer();
    }
    
  	public void maintainButtons() {
		mRespectAspectRatio.setEnabled(true);
		
		if (!mModel.is3DLineData()) {
			setResampleState(true); 
		
			if (mResampleAxes.isSelected())
				setResampleOptState(true);
			else
				setResampleOptState(false);
		}
		else 
			setResampleState(false);
			
		if (mModel.isFull3DVectorData()) {
			setVectorOptState(true);
		}
		else {
			setVectorOptState(false);
		}
						
		if (mRespectAspectRatio.isSelected())
			setScalingOptState(true);
		else
			setScalingOptState(false);
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
		
	private void setVectorOptState(boolean state) {
		mVectorScaleTextField.setEnabled(state);
		mIncludeU.setEnabled(state);
		mIncludeU.setEnabled(state);
		mIncludeV.setEnabled(state);
		mIncludeW.setEnabled(state);
  		mLabel2.setEnabled(state);
  		mLabel3.setEnabled(state);
	}
	
	private void setScalingOptState(boolean state) {
		mXScalingTextField.setEnabled(state);
		mYScalingTextField.setEnabled(state);
		mZScalingTextField.setEnabled(state);
  		mLabel4.setEnabled(state);
  		mLabel5.setEnabled(state);
  		mLabel6.setEnabled(state);
  		mLabel9.setEnabled(state);
	}
	
	private void setResampleOptState(boolean state) {
		mWeightedAverage.setEnabled(state);
		mNearestNeighbor.setEnabled(state);
  		mLabel1.setEnabled(state);
	}
	
	private void setResampleState(boolean state) {
		mResampleAxes.setEnabled(state);
	}
		
	public boolean isResampleAxes() {
		return mResampleAxes.isSelected();
	}
	
	public boolean isRespectAspectRatio() {
		return mRespectAspectRatio.isSelected();
	}
	
	public boolean isUIncluded() {
		return mIncludeU.isSelected();
	}
	
	public boolean isVIncluded() {
		return mIncludeV.isSelected();
	}
	
	public boolean isWIncluded() {
		return mIncludeW.isSelected();
	}
	
	public double getVectorScale() {
		String fldText = mVectorScaleTextField.getText();
		double scale;
		if (fldText.length() == 0)
			scale = 0.25;
		else {
			try {
				scale = Double.valueOf(fldText).doubleValue();
			}
			catch (NumberFormatException ex) {
				scale = 0.25;
			}
		}
		return scale;
	}
	
	public double getXScale() {
		String fldText = mXScalingTextField.getText();
		double scale;
		if (fldText.length() == 0)
			scale = 1.00;
		else {
			try {
				scale = Double.valueOf(fldText).doubleValue();
			}
			catch (NumberFormatException ex) {
				scale = 1.00;
			}
		}
		return scale;
	}
	
	public double getYScale() {
		String fldText = mYScalingTextField.getText();
		double scale;
		if (fldText.length() == 0)
			scale = 1.00;
		else {
			try {
				scale = Double.valueOf(fldText).doubleValue();
			}
			catch (NumberFormatException ex) {
				scale = 1.00;
			}
		}
		return scale;
	}
	
	public double getZScale() {
		String fldText = mZScalingTextField.getText();
		double scale;
		if (fldText.length() == 0)
			scale = 1.00;
		else {
			try {
				scale = Double.valueOf(fldText).doubleValue();
			}
			catch (NumberFormatException ex) {
				scale = 1.00;
			}
		}
		return scale;	
	}

	public int getResampleMode() {
		if (mWeightedAverage.isSelected())
			return VisADPlotSpecification.WEIGHTED_AVERAGE;
		else
			return VisADPlotSpecification.NEAREST_NEIGHBOR;
	}
}

