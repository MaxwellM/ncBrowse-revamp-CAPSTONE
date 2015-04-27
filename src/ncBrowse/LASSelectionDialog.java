package ncBrowse;

import gov.noaa.pmel.las.LASHandler;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * @author Joe Sirott
 * @version 1.0
 * @todo cache downloads
 * @todo eliminate pc.sirvin.com link
 */

public class LASSelectionDialog extends JDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel URLPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JPanel navigationPanel = new JPanel();
  JLabel jLabel1 = new JLabel();
  JButton goButton = new JButton();
  JButton acceptButton = new JButton();
  JButton cancelButton = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel fileURLPanel = new JPanel();
  JScrollPane httpScrollPane = new JScrollPane();
  JLabel jLabel2 = new JLabel();
  JTextField fileURLField = new JTextField();
  DefaultComboBoxModel cbModel;
  JTree tree_ = null;
  String[] defaultURLs = {
      //"http://pc.sirvin.com/lasv6-bin/LASserver.pl",
      "http://www.ferret.noaa.gov/NVODS-bin/LASserver.pl",
      //"http://las.aviso.oceanobs.com/las-bin/LASserver.pl"
  };
  NcFile mFile;
  Timer mTimer;
  static List mFileList = new ArrayList();

  private URL fileURL = null;
  JComboBox webURLcBox = new JComboBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  public LASSelectionDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    cbModel = new DefaultComboBoxModel(defaultURLs);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public LASSelectionDialog() {
    this(null, "", false);
  }

  public void dispose(){
    for (Object aMFileList : mFileList) {
      try {
        ((LasFile) aMFileList).delete();
      } catch (IOException ignored) {
      }
    }
    super.dispose();
  }

  void jbInit() {
    panel1.setLayout(borderLayout1);
    jLabel1.setText("LAS URL:");
    goButton.setText("Go");
    goButton.addActionListener(new LASSelectionDialog_goButton_actionAdapter(this));
    acceptButton.setText("Accept");
    acceptButton.addActionListener(new LASSelectionDialog_acceptButton_actionAdapter(this));
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new LASSelectionDialog_cancelButton_actionAdapter(this));
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    navigationPanel.setLayout(borderLayout2);
    jLabel2.setText("LAS URL:");
    fileURLField.setColumns(40);
    fileURLField.addActionListener(new LASSelectionDialog_fileURLField_actionAdapter(this));
    this.setTitle("Open Data File from a Live Access Server");
    panel1.setMinimumSize(new Dimension(550, 700));
    panel1.setPreferredSize(new Dimension(550, 700));
    webURLcBox.setEditable(true);
    webURLcBox.setModel(cbModel);
    webURLcBox.addActionListener(new LASSelectionDialog_webURLcBox_actionAdapter(this));
    URLPanel.setLayout(gridBagLayout1);
    fileURLPanel.setLayout(gridBagLayout2);
    getContentPane().add(panel1);
    panel1.add(URLPanel, BorderLayout.NORTH);
    URLPanel.add(jLabel1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(10, 5, 10, 0), 0, 0));
    URLPanel.add(webURLcBox, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 10, 5), 0, 0));
    URLPanel.add(goButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 10), 0, 0));
    panel1.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(acceptButton, null);
    buttonPanel.add(cancelButton, null);
    panel1.add(navigationPanel, BorderLayout.CENTER);
    navigationPanel.add(fileURLPanel, BorderLayout.SOUTH);
    fileURLPanel.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 0), 0, 0));
    fileURLPanel.add(fileURLField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 15), 0, 0));
    navigationPanel.add(httpScrollPane, BorderLayout.CENTER);
    httpScrollPane.getViewport().add(tree_, null);
  }

  public URL getURL() {
    return fileURL;
  }

  private void selectedNode(Object o){
    if (o instanceof LASHandler.Variable){
      LASHandler.Variable var = (LASHandler.Variable)o;
      mFile = null;
      try {
        mFile = new LasFile(var);
        mFileList.add(mFile);
        mTimer = new Timer(250, new TimerListener(var));
        mTimer.start();
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  class TimerListener implements ActionListener {
    ProgressMonitor pm;
    LASHandler.Variable var;
    boolean gotSize = false;
    String downloadMess;
    int progressSoFar = 1;

    public TimerListener(LASHandler.Variable var){
      this.var = var;
      this.downloadMess = "Downloading variable " + var.getLongName();
      pm = new ProgressMonitor(LASSelectionDialog.this, downloadMess,
                               null, 0, Integer.MAX_VALUE);
      pm.setProgress(progressSoFar);
      pm.setReportDelta(1);
    }

    public void actionPerformed(ActionEvent evt) {
      LasFile file = (LasFile)mFile;
      ++progressSoFar;
      if (!gotSize && file.getSize() != -1){
        pm.setMaximum(file.getSize());
        gotSize = true;
      } else if (gotSize){
        progressSoFar = file.getTotalDownloaded();
      }
      pm.setProgress(progressSoFar);
      if (pm.isCanceled() || file.isException()){
        file.close();
        mFile = null;
        pm.close();
        mTimer.stop();
        if (file.isException()){
          handleException(file);
        }
      } else if (file.isFinished()){
        if (file.isException()){
          handleException(file);
        } else {
          pm.close();
          setVisible(false);
          mTimer.stop();
        }
      }
    }

    private void handleException(LasFile file) throws HeadlessException {
      Exception ex = file.getException();
      String error = "Received error when accessing LAS:\n" + ex.getMessage();
      JOptionPane.showMessageDialog(LASSelectionDialog.this, error);
      ex.printStackTrace();
    }
  }

  void doubleClick(TreePath selPath){
    Object[] path = selPath.getPath();
    selectedNode(path[path.length-1]);
  }

  NcFile getFile() { return mFile; }

  void goButton_actionPerformed(ActionEvent e) {
    String newSelection = (String)webURLcBox.getSelectedItem();
    fileURLField.setText(newSelection);
    fileURLField_actionPerformed(e);
  }


  void acceptButton_actionPerformed(ActionEvent e) {
    TreePath selPath = null;
    if (tree_ != null){
      selPath = tree_.getSelectionPath();
    }
    if (selPath == null){
      JOptionPane.showMessageDialog(this, "You must select a LAS variable");
    } else {
      Object[] path = selPath.getPath();
      selectedNode(path[path.length-1]);
    }
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    mFile = null;
    setVisible(false);
  }


  void webURLcBox_actionPerformed(ActionEvent e) {
    goButton_actionPerformed(e);
  }

  private void updateCBModel(String selection) {
    int index = cbModel.getIndexOf(selection);
    if(index < 0 || index >= cbModel.getSize()) {
      cbModel.insertElementAt(selection, 0);
      index = 0;
    }
    cbModel.setSelectedItem(selection);
  }


  void fileURLField_actionPerformed(ActionEvent e) {
    String newSelection = fileURLField.getText();
    if (newSelection == null || newSelection.trim().equals("")){
      JOptionPane.showMessageDialog(this, "You must enter a LAS URL");
      return;
    }
    if (tree_ == null){
      tree_ = new JTree();
      DefaultTreeSelectionModel selmodel = new DefaultTreeSelectionModel();
      selmodel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
      tree_.setSelectionModel(selmodel);
      MouseListener ml = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          int selRow = tree_.getRowForLocation(e.getX(), e.getY());
          TreePath selPath = tree_.getPathForLocation(e.getX(), e.getY());
          if (selRow != -1) {
            if (e.getClickCount() == 2) {
              doubleClick(selPath);
            }
          }
        }
      };
      tree_.addMouseListener(ml);
      httpScrollPane.getViewport().add(tree_, null);
    }
    try {
      new URL(newSelection); // validate URL
      tree_.setModel(new LasTreeModel(newSelection));
      fileURLField.setText(newSelection);
      updateCBModel(newSelection);
    }
    catch (IOException ex) {
      tree_.setModel(null);
      String error = "Received error when accessing LAS:\n" + ex.getMessage();
      JOptionPane.showMessageDialog(this, error);
      ex.printStackTrace();
    }
  }
}



class LASSelectionDialog_goButton_actionAdapter implements ActionListener {
  LASSelectionDialog adaptee;

  LASSelectionDialog_goButton_actionAdapter(LASSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.goButton_actionPerformed(e);
  }
}

class LASSelectionDialog_acceptButton_actionAdapter implements ActionListener {
  LASSelectionDialog adaptee;

  LASSelectionDialog_acceptButton_actionAdapter(LASSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.acceptButton_actionPerformed(e);
  }
}

class LASSelectionDialog_cancelButton_actionAdapter implements ActionListener {
  LASSelectionDialog adaptee;

  LASSelectionDialog_cancelButton_actionAdapter(LASSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class LASSelectionDialog_webURLcBox_actionAdapter implements ActionListener {
  LASSelectionDialog adaptee;

  LASSelectionDialog_webURLcBox_actionAdapter(LASSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.webURLcBox_actionPerformed(e);
  }
}

class LasTreeModel implements TreeModel {
  LASHandler mHandler;
  Set mDatasets;
  Map mVarCache = new HashMap();
  public LasTreeModel(String name) throws IOException {
    mHandler = LASHandler.getInstance(name);
    Comparator c = (o1, o2) -> o1.toString().compareTo(o2.toString());
    mDatasets = new TreeSet(c);
    mDatasets.addAll(mHandler.getDatasets());
  }

  public Object getRoot() {
    return mHandler;
  }

  private Set getVariables(LASHandler.Dataset dset){
    Set vars = (Set) mVarCache.get(dset.getName());
    if (vars == null) {
      Comparator c = (o1, o2) -> o1.toString().compareTo(o2.toString());
      try {
        vars = new TreeSet(c);
        vars.addAll(dset.getVariables());
        mVarCache.put(dset.getName(), vars);
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return vars;
  }

  public Object getChild(Object parm1, int parm2) {
    if (parm1 instanceof LASHandler){
      if (mDatasets.size() > parm2){
        return mDatasets.toArray()[parm2];
      }
    }
    if (parm1 instanceof LASHandler.Dataset){
      LASHandler.Dataset dset = (LASHandler.Dataset)parm1;
      return getVariables(dset).toArray()[parm2];
   }
    return null;
  }

  public int getChildCount(Object parm1) {
    if (parm1 instanceof LASHandler){
      return mDatasets.size();
    }
    if (parm1 instanceof LASHandler.Dataset){
      return getVariables((LASHandler.Dataset)parm1).size();
    }
    return 0;
  }

  public int getIndexOfChild(Object parent, Object child) {
    int count = -1;
    if (parent instanceof LASHandler){
      for (Object mDataset : mDatasets) {
        if (child.equals(mDataset)) {
          return ++count;
        }
      }
    }
    if (parent instanceof LASHandler.Dataset){
      for (Object o : getVariables((LASHandler.Dataset) parent)) {
        if (child.equals(o)) {
          return ++count;
        }
      }
    }
    return -1;
  }

  public boolean isLeaf(Object parm1) {
    return (parm1 instanceof LASHandler.Variable);
  }

  public void valueForPathChanged(TreePath parm1, Object parm2) {
  }

  public void addTreeModelListener(TreeModelListener parm1) {
  }
  public void removeTreeModelListener(TreeModelListener parm1) {
  }

}

class LASSelectionDialog_fileURLField_actionAdapter implements ActionListener {
  LASSelectionDialog adaptee;

  LASSelectionDialog_fileURLField_actionAdapter(LASSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.fileURLField_actionPerformed(e);
  }
}
