/*
 * $Id: Browser.java 15 2013-04-24 19:16:08Z dwd $
 *
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package ncBrowse;

import gov.noaa.pmel.nc2.station.GeoDomain;
import gov.noaa.pmel.nc2.station.StationCollection;
import gov.noaa.pmel.nc2.station.StationDataset;
import gov.noaa.pmel.swing.JSystemPropertiesDialog;
import gov.noaa.pmel.swing.JViewHTMLFrame;
import gov.noaa.pmel.swing.dapper.DapperWizard;
import gov.noaa.pmel.swing.dapper.SelectionEvent;
import gov.noaa.pmel.swing.dapper.SelectionListener;
import ncBrowse.map.VMapGraph;
import ncBrowse.map.VMapModel;
import ncBrowse.map.VariableMapDialog;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Main class for browsing netCDF files.
 *
 * @author Donald Denbo
 * @version $Revision: 1.72 $, $Date: 2005/03/31 23:44:47 $
 */
public class Browser extends JFrame implements DialogClient, SelectionListener {
  private static boolean useOPeNDAP_ = false;
  private static boolean useVMap_ = false;
  private static boolean useSystemLF_ = false;
  private String OPeNDAPFile_ = null;
  private static Vector<Browser> openBrowsers;
  private static Browser instance_ = null;
  public static boolean can3DMap = true;

  /**
   * @label ncFile_
   */
  private NcFile ncFile_ = null;
  private JFileChooser fileChoose_;
  private URLSelectionDialog usd_ = null;
  private LASSelectionDialog lsd_ = null;
  private OPeNDAPSelectionDialog dsd_ = null;
  private Hashtable<String,String> longName_;
  private Hashtable<String,VMapModel> vMapModel_;
  private boolean showAllVariables_ = false;
  private String timeFormat_ = "yyyy-MM-dd HH:mm:ss";
  private WindowList windowList_;
  private CheckBoxAction cbAction_ = new CheckBoxAction();
  private boolean oceanShare_ = false;
    public static JTextArea ncDumpTextField = new JTextArea();

  private DapperWizard dapperWiz_ = null;

  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout(0,0);
  BorderLayout borderLayout2 = new BorderLayout(0,0);

    BorderLayout borderLayout3 = new BorderLayout(0, 0);

  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public Browser() {
    this(false);
  }

  public Browser(boolean oceanshare) {
    oceanShare_ = oceanshare;
    instance_ = this;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    if(oceanShare_) {
      // test if OPeNDAP should be enabled
      useOPeNDAP_ =
          System.getProperty("ncBrowse.useOPeNDAP","true").equals("true");
      useVMap_ =
          System.getProperty("ncBrowse.useVMap","true").equals("true");
      useSystemLF_ =
          System.getProperty("ncBrowse.useSystemLF", "false").equals("true");
    }
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    vMapModel_ = new Hashtable<String,VMapModel>();
    if(oceanShare_) {
      Dimension ss = getToolkit().getScreenSize();
      Dimension as = getSize();
      if(Debug.DEBUG) {
        System.out.println("Screen size = " + ss);
        System.out.println("Browser size = " + as);
      }
      setLocation(ss.width/2 - as.width/2,
                      (int)(ss.getHeight()*0.05));
      openBrowsers = new Vector<Browser>(5);
      openBrowsers.addElement(this);
      try {
//        Browser.getInstance().getClass();
        Class.forName("javax.media.j3d.Bounds");
      } catch (ClassNotFoundException ex) {
        //Java3D not installed
        can3DMap = false;
      }

    }
  }

  private void  jbInit() throws Exception {
      try {
          //recommended way to set Nimbus LaF because old versions of Java 6
          //don't have it included
          for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
              if ("Nimbus".equals(info.getName())) {
                  UIManager.setLookAndFeel(info.getClassName());
                  break;
              }
          }
      } catch (Exception e) {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      contentPane = (JPanel) this.getContentPane();
    SymMouse aSymMouse = new SymMouse();

    box2 = Box.createHorizontalBox();
    box4 = Box.createVerticalBox();
    box5 = Box.createVerticalBox();
    component1 = Box.createHorizontalStrut(8);
    component5 = Box.createVerticalStrut(6);
    component2 = Box.createVerticalStrut(6);
    component4 = Box.createVerticalStrut(6);
    component3 = Box.createVerticalStrut(6);
    component6 = Box.createVerticalStrut(6);
    component7 = Box.createVerticalStrut(6);
    component8 = Box.createHorizontalStrut(8);
    component9 = Box.createHorizontalStrut(16);
    component10 = Box.createVerticalStrut(12);
    component11 = Box.createVerticalStrut(12);
    component12 = Box.createVerticalStrut(12);
    component13 = Box.createVerticalStrut(12);
    component14 = Box.createHorizontalStrut(3);
    setResizable(true);
    setJMenuBar(menuBar);
    setTitle("NetCDF File Browser");
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    contentPane.setLayout(borderLayout1);

      //My CODE
      Rectangle2D result = new Rectangle2D.Double();
      GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
      for (GraphicsDevice gd : localGE.getScreenDevices()) {
          for (GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) {
              result.union(result, graphicsConfiguration.getBounds(), result);
          }
      }
      int fullWidth = (int) result.getWidth();
      int fullHeight = (int) result.getHeight();
      int winSizeWidth = (fullWidth/4);
      int winSizeHeight = (fullHeight/2);
      //My CODE

    setSize(600,300);
    setVisible(false);
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exitItem_actionPerformed(null);
      }
    });
    //$$ menuBar.move(24,204);
    fileMenu.setText("File");
    fileMenu.setActionCommand("File");
    fileMenu.setMnemonic((int)'F');
    JLabel1.setMaximumSize(new Dimension(80, 40));
    JLabel1.setMinimumSize(new Dimension(80, 17));
    JLabel1.setPreferredSize(new Dimension(80, 17));
    JLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel1.setText("File:");
    numDimText.setMaximumSize(new Dimension(2147483647, 40));
    numDimText.setEditable(false);
    numDimText.setColumns(5);
    numVarText.setEditable(false);
    numVarText.setColumns(5);
    numVarText.setMaximumSize(new Dimension(2147483647, 40));
    numVarText.setToolTipText("Right click mouse for Variable List");
    numVarText.addMouseListener(aSymMouse);
    numAttText.setMaximumSize(new Dimension(2147483647, 40));
    numAttText.setEditable(false);
    numAttText.setColumns(5);
    fileText.setMaximumSize(new Dimension(2147483647, 40));
    fileText.setEditable(false);
    fileText.setColumns(5);
    JLabel3.setMaximumSize(new Dimension(80, 40));
    JLabel3.setMinimumSize(new Dimension(80, 17));
    JLabel3.setPreferredSize(new Dimension(80, 17));
    JLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel3.setText("Dimensions:");
    JLabel2.setMaximumSize(new Dimension(80, 40));
    JLabel2.setMinimumSize(new Dimension(80, 17));
    JLabel2.setPreferredSize(new Dimension(80, 17));
    JLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel2.setText("Variables:");
    JLabel4.setMaximumSize(new Dimension(80, 40));
    JLabel4.setMinimumSize(new Dimension(80, 17));
    JLabel4.setPreferredSize(new Dimension(80, 17));
    JLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel4.setText("Attributes:");
    varScrollPane.setMinimumSize(new Dimension(170, 48));
    varScrollPane.setPreferredSize(new Dimension(170, 160));
    JPanel2.setMinimumSize(new Dimension(170, 48));
    JPanel2.setPreferredSize(new Dimension(170, 270));

      JPanel3.setMinimumSize(new Dimension(200, 48));
      JPanel3.setPreferredSize(new Dimension(300, 270));

    borderLayout2.setHgap(3);
    borderLayout2.setVgap(3);
    openWeb.setActionCommand("Web...");
    openWeb.setMnemonic('W');
    openWeb.setText("Web...");
    openWeb.setAccelerator(KeyStroke.getKeyStroke(87, KeyEvent.CTRL_MASK, false));
    openOPeNDAP.setMnemonic('D');
    openOPeNDAP.setText("OPeNDAP...");
    newMapButton.setText("New Map...");
    newMapButton.setEnabled(false);
    jPanel1.setLayout(gridBagLayout2);
    openLas.setText("LAS...");
    openDapper.setText("Dapper...");
    newBrowser.setText("New Window");
    menuBar.add(fileMenu);
    openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
    openFile.setText("Open File...");
    openFile.setActionCommand("Open...");
    openFile.setMnemonic((int)'O');
      openFileInWindows.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
      openFileInWindows.setText("Split File Into Multiple Windows...");
      //openFileInWindows.setActionCommand("Open...");
      //penFileInWindows.setMnemonic((int)'O');
    fileMenu.add(newBrowser);
    fileMenu.addSeparator();
    fileMenu.add(openFile);
      fileMenu.addSeparator();
      fileMenu.add(openFileInWindows);
      fileMenu.addSeparator();
    fileMenu.add(openWeb);
    fileMenu.add(openLas);

    if(useOPeNDAP_) {
      fileMenu.add(openDapper);
      fileMenu.add(openOPeNDAP);
    }
    fileMenu.addSeparator();
    //    fileMenu.add(JSeparator2);
    if(!oceanShare_) {
      exportCdlItem.setEnabled(false);
      exportCdlItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
      exportCdlItem.setText("Export Variable to cdl...");
      exportCdlItem.setMnemonic((int) 'C');
      fileMenu.add(exportCdlItem);
      exportUNHItem.setEnabled(false);
      exportUNHItem.setText("Export Variable to UNH...");
      exportUNHItem.setActionCommand("Export Variable to UNH...");
      exportUNHItem.setMnemonic((int) 'U');
      fileMenu.add(exportUNHItem);
      fileMenu.addSeparator();
    }
    //    fileMenu.add(JSeparator1);
    closeItem.setText("Close");
    closeItem.setActionCommand("Close");
    closeItem.setMnemonic((int)'C');
    fileMenu.add(closeItem);
    exitItem.setText("Exit");
    exitItem.setActionCommand("Exit");
    exitItem.setMnemonic((int)'X');
    if(!oceanShare_) fileMenu.add(exitItem);
    editMenu.setText("Edit");
    editMenu.setActionCommand("Edit");
    editMenu.setMnemonic((int)'E');
    menuBar.add(editMenu);
    optionItem.setText("Options...");
    optionItem.setActionCommand("Options...");
    optionItem.setMnemonic((int)'O');
    editMenu.add(optionItem);
    viewMenu.setText("View");
    menuBar.add(viewMenu);
    asTableItem.setEnabled(false);
    asTableItem.setText("As Table...");
    viewMenu.add(asTableItem);
    asTreeItem.setEnabled(false);
    asTreeItem.setText("As Tree...");
    viewMenu.add(asTreeItem);
    windowMenu.setText("Window");
    windowMenu.setActionCommand("Window");
    windowMenu.setMnemonic((int)'W');
    menuBar.add(windowMenu);
    minAllItem.setText("Minimize All");
    minAllItem.setActionCommand("Minimize All");
    windowMenu.add(minAllItem);
    maxAllItem.setText("Maximize All");
    maxAllItem.setActionCommand("Maximize All");
    windowMenu.add(maxAllItem);
    windowMenu.addSeparator();
    //    windowMenu.add(JSeparator4);
    helpMenu.setText("Help");
    helpMenu.setActionCommand("Help");
    helpMenu.setMnemonic((int)'H');
    menuBar.add(helpMenu);
    newsItem.setText("News...");
    newsItem.setActionCommand("News...");
    helpMenu.add(newsItem);
    sysPropItem.setText("System Properties...");
    sysPropItem.setActionCommand("System Properties...");
    helpMenu.add(sysPropItem);
    helpMenu.addSeparator();
    //    helpMenu.add(JSeparator5);
    helpItem.setText("Help...");
    helpItem.setMnemonic((int)'H');
    helpMenu.add(helpItem);
    helpMenu.addSeparator();
    //    helpMenu.add(JSeparator3);
    aboutItem.setText("About...");
    aboutItem.setActionCommand("About...");
    aboutItem.setMnemonic((int)'A');
    helpMenu.add(aboutItem);
    //$$ titledBorder1.move(48,204);
    namePanel.setBorder(bevelBorder1);
    namePanel.setLayout(gridBagLayout1);
    contentPane.add(namePanel, "South");
    //namePanel.setBounds(0,166,571,25);
    lnameTextField.setEditable(false);
    namePanel.add(lnameTextField, new
      GridBagConstraints(0,0,1,1,1.0,1.0,
                         GridBagConstraints.CENTER,GridBagConstraints.BOTH,
                         new Insets(0,0,0,0),0,2));
   //lnameTextField.setBounds(2,2,567,21);
    //$$ bevelBorder1.move(72,204);
    JPanel2.setLayout(borderLayout2);
    //JPanel2.setBounds(360,10,204,144);

      JPanel3.setLayout(borderLayout3);

    varScrollPane.setBorder(titledBorder1);
    varScrollPane.setOpaque(true);
    contentPane.add(box2, BorderLayout.WEST);
    box2.add(component8, null);
    box2.add(box5, null);
    box5.add(component13, null);
    box5.add(JLabel1, null);
    box5.add(component3, null);
    box5.add(JLabel4, null);
    box5.add(component7, null);
    box5.add(JLabel3, null);
    box5.add(component6, null);
    box5.add(JLabel2, null);
    box5.add(component10, null);
    box2.add(component1, null);
    box2.add(box4, null);
    box4.add(component12, null);
    box4.add(fileText, null);
    box4.add(component4, null);
    box4.add(numAttText, null);
    box4.add(component5, null);
    box4.add(numDimText, null);
    box4.add(component2, null);
    box4.add(numVarText, null);
    box4.add(component11, null);
    box2.add(component9, null);
    contentPane.add(JPanel2, BorderLayout.EAST);
    JPanel2.add(varScrollPane, BorderLayout.CENTER);
    JPanel2.add(component14, BorderLayout.EAST);
    if(useVMap_) {
      JPanel2.add(jPanel1, BorderLayout.SOUTH);
      jPanel1.add(newMapButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
          new Insets(0, 5, 0, 5), 0, 0));
    }
    varScrollPane.getViewport().add(VariableList, null);
    //varScrollPane.setBounds(0,0,204,144);
      ncDump.setBorder(titledBorder2);
      ncDump.setOpaque(true);
      contentPane.add(JPanel3, BorderLayout.CENTER);
      JPanel3.add(ncDump, BorderLayout.CENTER);
      ncDump.getViewport().add(ncDumpTextField, null);
    //$$ JPopupMenu1.move(120,168);
    //$$ JMenuBar1.move(168,312);
    //$$ JMenuBar1.move(168,312);
    exportCdlItem.setActionCommand("Export Variable...");
    viewMenu.setActionCommand("View");
    viewMenu.setMnemonic((int)'V');
    asTableItem.setActionCommand("As Table...");
    asTreeItem.setActionCommand("As Tree...");

    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    SymAction lSymAction = new SymAction();
    openDapper.addActionListener(lSymAction);
    newBrowser.addActionListener(lSymAction);
    openFile.addActionListener(lSymAction);
      openFileInWindows.addActionListener(lSymAction);
    openWeb.addActionListener(lSymAction);
    openOPeNDAP.addActionListener(lSymAction);
    openLas.addActionListener(lSymAction);
    exitItem.addActionListener(lSymAction);
    aboutItem.addActionListener(lSymAction);
    asTableItem.addActionListener(lSymAction);
    asTreeItem.addActionListener(lSymAction);
    sysPropItem.addActionListener(lSymAction);
    exportCdlItem.addActionListener(lSymAction);
    exportUNHItem.addActionListener(lSymAction);
    newMapButton.addActionListener(lSymAction);
    SymListSelection lSymListSelection = new SymListSelection();
    newsItem.addActionListener(lSymAction);
    optionItem.addActionListener(lSymAction);
    closeItem.addActionListener(lSymAction);
    SymMenu lSymMenu = new SymMenu();
    windowMenu.addMenuListener(lSymMenu);
    minAllItem.addActionListener(lSymAction);
    maxAllItem.addActionListener(lSymAction);
    helpItem.addActionListener(lSymAction);

    VariableList.addMouseListener(aSymMouse);
    VariableList.addListSelectionListener(lSymListSelection);

    String dir = System.getProperty("user.dir");
    fileChoose_ = new JFileChooser(dir);
    //

    windowList_ = new WindowList(5);
    windowList_.setParent(this);
//    JLabel1.setBounds(10, 21, 110, 15);
//    numDimText.setBounds(125, 74, 229, 24);
//    numVarText.setBounds(125, 44, 229, 24);
//    numAttText.setBounds(125, 104, 229, 24);
//    fileText.setBounds(125, 14, 229, 24);
//    JLabel3.setBounds(10, 81, 110, 15);
//    JLabel2.setBounds(10, 51, 110, 15);
//    JLabel4.setBounds(10, 111, 110, 15);

    // HTTP event handling from built in HTTP server
    HTTPServer.HTTPHandler handler = new HTTPServer.HTTPHandler(){
      public void handle(URL url){
        SwingUtilities.invokeLater(new UrlHandler(url));
      }
    };
    HTTPServer.start(handler);
  }

  class UrlHandler implements Runnable {
    URL url;
    UrlHandler(URL url){
      this.url = url;
    }
    public void run() {
      String path = url.getPath();
      String query = url.getQuery();
      if (path != null && query != null &&
          path.endsWith("open") && query.startsWith("url=")){
        String[] parts = query.split("=");
        if (parts.length == 2){
          toFront();
          setOPeNDAP(parts[1]);
        }
      }
    }

  }

  /**
   * Creates a new instance of Browser with the given title.
   * @param sTitle the title for the new frame.
   */
  public Browser(String sTitle) {
    this();
    setTitle(sTitle);
  }

  public Browser(String sTitle, boolean oceanshare) {
    this(oceanshare);
    setTitle(sTitle);
  }

  public static Browser getInstance() {
    return instance_;
  }

  /**
   * The entry point for this application.
   * Sets the Look and Feel to the System Look and Feel.
   * Creates a new Browser and makes it visible.
   *
   * @param args startup arguments
   */
  static public void main(String[] args) {
    // test if OPeNDAP should be enabled
    useOPeNDAP_ = System.getProperty("ncBrowse.useOPeNDAP","true").equals("true");
    useVMap_ = System.getProperty("ncBrowse.useVMap","true").equals("true");
    useSystemLF_ = System.getProperty("ncBrowse.useSystemLF", "false").equals("true");
    String OPeNDAPFile = System.getProperty("ncBrowse.OPeNDAP.File");
    Browser app;
    File file = null;
    if(args.length > 0) {
      file = new File(args[0]);
    }
    try {
      try {
        if(useSystemLF_) {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else {
          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
      } catch (Exception e) {}

      //Create a new instance of our application's frame, and make it visible.
      app = new Browser();
      app.setOPeNDAPFile(OPeNDAPFile);
      Dimension ss = app.getToolkit().getScreenSize();
      Dimension as = app.getSize();
      if(Debug.DEBUG) {
        System.out.println("Screen size = " + ss);
        System.out.println("Browser size = " + as);
      }
      app.setLocation(ss.width/2 - as.width/2,
                      (int)(ss.getHeight()*0.05));
      if(file != null) app.setFile(file);
      app.setVisible(true);
      openBrowsers = new Vector<Browser>(5);
      openBrowsers.addElement(app);
    } catch (Throwable t) {
      t.printStackTrace();
      //Ensure the application exits with an error condition.
      System.exit(1);
    }
    try {
//      Browser.getInstance().getClass();
      Class.forName("javax.media.j3d.Bounds");
    }
    catch (ClassNotFoundException ex) {
      //Java3D not installed
      can3DMap = false;
    }
  }

  /**
   * Notifies this component that it has been added to a container
   * This method should be called by <code>Container.add</code>, and
   * not by user code directly.
   * Overridden here to adjust the size of the frame if needed.
   * @see java.awt.Container#removeNotify
   */
  public void addNotify() {
    // Record the size of the window prior to calling parents addNotify.
    Dimension size = getSize();

    super.addNotify();

    if (frameSizeAdjusted)
      return;
    frameSizeAdjusted = true;

    // Adjust size of frame according to the insets and menu bar
    JMenuBar menuBar = getRootPane().getJMenuBar();
    int menuBarHeight = 0;
    if (menuBar != null)
      menuBarHeight = menuBar.getPreferredSize().height;
    Insets insets = getInsets();
    setSize(insets.left + insets.right + size.width,
            insets.top + insets.bottom + size.height + menuBarHeight);
  }

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  JPanel namePanel = new JPanel();
  JTextField lnameTextField = new JTextField();
  JPanel JPanel2 = new JPanel();

    JScrollPane ncDump = new JScrollPane();
    JPanel JPanel3 = new JPanel();
    TitledBorder titledBorder2 = new TitledBorder("ncDump");

  JScrollPane varScrollPane = new JScrollPane();
  JMenuBar menuBar = new JMenuBar();
  JMenu fileMenu = new JMenu();
  JMenuItem openFile = new JMenuItem();
    JMenuItem openFileInWindows = new JMenuItem();
  //  javax.swing.JSeparator JSeparator2 = new javax.swing.JSeparator();
  JMenuItem exportCdlItem = new JMenuItem();
  JMenuItem exportUNHItem = new JMenuItem();
  JMenuItem closeItem = new JMenuItem();
  JMenuItem exitItem = new JMenuItem();
  JMenu editMenu = new JMenu();
  JMenuItem optionItem = new JMenuItem();
  JMenu viewMenu = new JMenu();
  JMenuItem asTableItem = new JMenuItem();
  JMenuItem asTreeItem = new JMenuItem();
  JMenu windowMenu = new JMenu();
  JMenuItem minAllItem = new JMenuItem();
  JMenuItem maxAllItem = new JMenuItem();
  JMenu helpMenu = new JMenu();
  JMenuItem newsItem = new JMenuItem();
  JMenuItem sysPropItem = new JMenuItem();
  JMenuItem helpItem = new JMenuItem();
  JMenuItem aboutItem = new JMenuItem();
  BevelBorder bevelBorder1 = new BevelBorder(BevelBorder.LOWERED);
  TitledBorder titledBorder1 = new TitledBorder("Select Variable for Display");
  JLabel JLabel1 = new JLabel();
  JTextField numDimText = new JTextField();
  JTextField numVarText = new JTextField();
  JTextField numAttText = new JTextField();
  JTextField fileText = new JTextField();
  JLabel JLabel3 = new JLabel();
  JLabel JLabel2 = new JLabel();
  JLabel JLabel4 = new JLabel();
  Box box2;
  Box box4;
  Box box5;
  Component component1;
  Component component5;
  Component component2;
  Component component4;
  Component component3;
  Component component6;
  Component component7;
  Component component8;
  Component component9;
  Component component10;
  Component component11;
  Component component12;
  Component component13;
  JList VariableList = new JList();
  Component component14;
  JMenuItem openWeb = new JMenuItem();
  JMenuItem openOPeNDAP = new JMenuItem();
  JPanel jPanel1 = new JPanel();
  JButton newMapButton = new JButton();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
//  private JButton newNewMapButton = new JButton();
  JMenuItem openLas = new JMenuItem();
  JMenuItem openDapper = new JMenuItem();
  JMenuItem newBrowser = new JMenuItem();

  /** @link dependency */
  /*#DomainSelector lnkDomainSelector;*/

  /** @link dependency */
  /*#TreeView lnkTreeView;*/

  /** @link dependency */
  /*#TableView lnkTableView;*/

  /** @link dependency */
  /*#VariableMapDialog lnkVariableMapDialog;*/

  void exitApplication() {
    try {
      // Beep
      Toolkit.getDefaultToolkit().beep();
      // Show a confirmation dialog
      int reply = JOptionPane.showConfirmDialog(this,
                                                "Do you really want to exit?",
                                                "ncBrowse - Exit" ,
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE);
      // If the confirmation was affirmative, handle exiting.
      if (reply == JOptionPane.YES_OPTION) {
        this.setVisible(false);    // hide the Frame
        this.dispose();            // free the system resources
        System.exit(0);            // close the application
      }
    } catch (Exception e) {
    }
  }

  class SymWindow extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
      Object object = event.getSource();
      if (object == Browser.this)
        Browser_windowClosing(event);
    }
  }

  void Browser_windowClosing(WindowEvent event) {
    openBrowsers.remove(this);
    if(oceanShare_) {
      this.setVisible(false);
      this.windowList_.closeAll();
      return;
    }
    if(openBrowsers.isEmpty()) {
      try {
        this.setVisible(false);
        this.dispose();
        System.exit(0);
      } catch (Exception e) {
      }
    } else {
      this.windowList_.closeAll();
      this.setVisible(false);
      this.dispose();
    }
  }

  class SymAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == newBrowser)
        newBrowser_actionPerformed(event);
      else if (object == openFile) {
          openFile_actionPerformed(event);
          ncDump();
      }
      else if (object == openFileInWindows) {
          openFileInWindows_actionPerformed(event);
          //newMapButton_actionPerformed(event);
          //button_actionPerformed(event);
          //new VMapModel(ncFile_);
          //System.out.println(vMapModel_.size());
      }

      else if (object == openWeb)
        openWeb_actionPerformed(event);
      else if (object == openDapper)
        openDapper_actionPerformed(event);
      else if (object == openOPeNDAP)
        openOPeNDAP_actionPerformed(event);
      else if (object == openLas)
        openLas_actionPerformed(event);
      else if (object == exitItem)
        exitItem_actionPerformed(event);
      else if (object == aboutItem)
        aboutItem_actionPerformed(event);
      else if (object == asTableItem)
        asTableItem_actionPerformed(event);
      else if (object == asTreeItem)
        asTreeItem_actionPerformed(event);
      else if (object == sysPropItem)
        sysPropItem_actionPerformed(event);
      else if (object == exportCdlItem)
        exportCdlItem_actionPerformed(event);
      else if (object == exportUNHItem)
        exportUNHItem_actionPerformed(event);
      else if (object == newsItem)
        newsItem_actionPerformed(event);
      else if (object == optionItem)
        optionItem_actionPerformed(event);
      else if (object == closeItem)
        closeItem_actionPerformed(event);
      else if (object == minAllItem)
        minAllItem_actionPerformed(event);
      else if (object == maxAllItem)
        maxAllItem_actionPerformed(event);
      else if (object == helpItem)
        helpItem_actionPerformed(event);
      else if (object == newMapButton)
        newMapButton_actionPerformed(event);
    }
  }

  class CheckBoxAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object instanceof JCheckBoxMenuItem)
        checkBoxItem_actionPerformed(event);
    }
  }
//    void button_actionPerformed(ActionEvent event) {
//        JButton obj = (JButton) event.getSource();
//        Point screenLoc = obj.getLocationOnScreen();
////        for(int i = 0; i < minPopupButton_.length; i++) {
////            if(obj == minPopupButton_[i] || obj == maxPopupButton_[i]) {
////                popupDialog(i, screenLoc, new Point(0, 0));
////                return;
////            }
////            if(obj == movieButton_[i]) {
//                popupMovieControl(1,screenLoc);
////                return;
////            }
//        }
//    void popupMovieControl(int i, Point screenLoc) {
//        MovieControl mc = MovieControl.getInstance();
//        //mc.setMapParameter(VariableMapDialog.vParam_[i]);
//        //mc.setJPane(pane_);
//        mc.setLocation(screenLoc);
//        mc.setVisible(true);
//    }


  void openWeb_actionPerformed(ActionEvent event) {
    URL url = null;
    url = getURLFromDialog();
    if(url != null)  setURL(url);
    //
    // frame having problems with repaint force one
    //
    repaint();
  }

  void openOPeNDAP_actionPerformed(ActionEvent event) {
    String path = getOPeNDAPFromDialog();
    if(path != null)  setOPeNDAP(path);
    //
    // frame having problems with repaint force one
    //
    repaint();
  }

  void openLas_actionPerformed(ActionEvent event) {
    if(lsd_ == null) {
      lsd_ = new LASSelectionDialog();
    }
    //lsd_.setModal(true);
    lsd_.setVisible(true);
    ComponentAdapter adapter = new ComponentAdapter() {

      public void componentHidden(ComponentEvent ce) {
        ncFile_ = lsd_.getFile();
        if(ncFile_ != null)  {
            setupDisplay();
          }
          repaint();
        }
    };
    lsd_.addComponentListener(adapter);
  }

//    void NcDump() {
//        // Create a stream to hold the output
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(baos);
//        // IMPORTANT: Save the old System.out!
//        PrintStream old = System.out;
//        // Tell Java to use your special stream
//        System.setOut(ps);
//        // Print some output: goes to your special stream
//        System.out.println(ncFile_.getVariables());
//        // Put things back
//        System.out.flush();
//        System.setOut(old);
//        // Show what happened
//        //System.out.println("Here: " + baos.toString());
//
//        ncDumpTextField.setText(baos.toString());
//    }
    void openFileInWindows_actionPerformed(ActionEvent event) {
        VariableWindows variableWindows = new VariableWindows();
        variableWindows.openVariableWindows();
        windowList_.addElement(variableWindows);
        ncFile_.getVariableWindowData();
        if (!vMapModel_.isEmpty()) {
            ArrayList<String> vMapModels = new ArrayList<String>();
            Enumeration<String> evm = vMapModel_.keys();
            VMapModel model = null;
            while (evm.hasMoreElements()) {
                String name = (String) evm.nextElement();
                //listdata.addElement(name);
                model = vMapModel_.get(name);
                if (model instanceof VMapModel) {
                    //System.out.println(((VMapModel) model).getName());
                    vMapModels.add(model.getName());

                }
            }
            //prints the elements in the ArrayList!
            for (int i = 0; i < vMapModels.size(); i++) {
                String name = vMapModels.get(i);
                System.out.println(name);
            }
        }
    }

    void ncDump(){
        ncDumpTextField.setText(ncFile_.NcDump());
    }


  void openFile_actionPerformed(ActionEvent event) {
    File file = null;
    file = getFileFromDialog();
    if(file != null){
        setFile(file);
        //System.out.println(file);
        //ncDumpTextField.setText(String.valueOf(file));
    }
    //
    // frame having problems with repaint force one
    //
    repaint();
  }

  private URL getURLFromDialog() {
    URL url = null;
    if(usd_ == null) {
      usd_ = new URLSelectionDialog();
    }
    usd_.setModal(true);
    usd_.setVisible(true);
    url = usd_.getURL();
    return url;
  }

  private String getOPeNDAPFromDialog() {
    String dods = null;
    if(dsd_ == null) {
      dsd_ = new OPeNDAPSelectionDialog(OPeNDAPFile_);
    }
    dsd_.setModal(true);
    dsd_.setVisible(true);
    dods = dsd_.getOPeNDAPPath();
    return dods;
  }

  private File getFileFromDialog() {
    File file = null;
    String[] netfile = new String[]{"cdf", "nc"};
    SimpleFileFilter netfilt = new SimpleFileFilter(netfile, "Data Files (*.nc, *.cdf)");
    fileChoose_.addChoosableFileFilter(netfilt);

    fileChoose_.setFileFilter(fileChoose_.getAcceptAllFileFilter());
    fileChoose_.resetChoosableFileFilters();
    fileChoose_.setMultiSelectionEnabled(true);
    fileChoose_.setFileSelectionMode(JFileChooser.FILES_ONLY);

    fileChoose_.setFileFilter(netfilt);
    int ret_val = fileChoose_.showOpenDialog(this);
    if(ret_val == JFileChooser.APPROVE_OPTION) {
      file = fileChoose_.getSelectedFile();
      if(Debug.DEBUG) System.out.println(file.getAbsolutePath());
    }
    return file;
  }

  @SuppressWarnings("unused")
  private void setNewVariableList() {
    Vector<String> listdata;
    Variable ncVar;
    Iterator<Variable> vi;
    Attribute attr;
    if(ncFile_ == null) return;

    if(showAllVariables_) {
      vi = ncFile_.getVariables().iterator();
    } else {
      vi = ncFile_.getNonDimensionVariables();
    }
    listdata = new Vector<String>();
    longName_ = new Hashtable<String,String>();
    // Variable Map
    if(!vMapModel_.isEmpty()) {
      Enumeration<String> evm = vMapModel_.keys();
      Object model = null;
      while(evm.hasMoreElements()) {
        String name = (String)evm.nextElement();
        listdata.addElement(name);
        model = vMapModel_.get(name);
//        if(model instanceof ncBrowse.vmap.VMapModel) {
//          longName_.put(name, ((ncBrowse.vmap.VMapModel)model).getName() + "  (New Variable Map)");
        if(model instanceof VMapModel) {
          longName_.put(name, ((VMapModel)model).getName() + "  (Variable Map)");

        }
      }
    }
    // original Variables
    while(vi.hasNext()) {
      ncVar = (Variable)vi.next();
      listdata.addElement(ncVar.getName());
      attr = ncVar.findAttribute("long_name");
      if(attr != null) {
        longName_.put(ncVar.getName(), attr.getStringValue());
      } else {
        longName_.put(ncVar.getName(), " ");
      }
    }
    VariableList.setListData(listdata);

  }

  private void setVariableList() {
    Vector<String> listdata;
    Variable ncVar;
    Iterator<Variable> vi;
    Attribute attr;
    if(ncFile_ == null) return;

    if(showAllVariables_) {
      vi = ncFile_.getVariables().iterator();
    } else {
      vi = ncFile_.getNonDimensionVariables();
    }
    listdata = new Vector<String>();
    longName_ = new Hashtable<String,String>();
    while(vi.hasNext()) {
      ncVar = (Variable)vi.next();
      listdata.addElement(ncVar.getName());
      attr = ncVar.findAttribute("long_name");
      if(attr != null) {
        longName_.put(ncVar.getName(), attr.getStringValue());
      } else {
        longName_.put(ncVar.getName(), " ");
      }
    }
    // Variable Map
    if(!vMapModel_.isEmpty()) {
      Enumeration<String> evm = vMapModel_.keys();
      VMapModel model = null;
      while(evm.hasMoreElements()) {
        String name = (String)evm.nextElement();
        listdata.addElement(name);
        model = vMapModel_.get(name);
//        if(model instanceof ncBrowse.vmap.VMapModel) {
//          longName_.put(name, ((ncBrowse.vmap.VMapModel)model).getName() + "  (New Variable Map)");
        if(model instanceof VMapModel) {
          longName_.put(name, ((VMapModel)model).getName() + "  (Variable Map)");

        }
      }
    }
    VariableList.setListData(listdata);
    newMapButton.setEnabled(true);
  }

  public void setURL(URL url) {
    try {
      ncFile_ = new LocalNcFile(url);
    } catch (IOException e) {
      System.out.println(e + ": new ncFile(URL)");
      return;
    }
    setupDisplay();
  }

  public void setFile(File file) {
    try{
      ncFile_ = new LocalNcFile(file);
    } catch (IOException e) {
      System.out.println(e + ": new NcFile: " + file.getName());
      return;
    }
    setupDisplay();
  }

  public void setOPeNDAP(String path) {
    try{
      ncFile_ = new DODSNcFile(path);
    } catch (dods.dap.DODSException e) {
      System.out.println(e + ": new DODSNcFile");
      return;
    } catch (MalformedURLException e) {
      System.out.println(e + ": new DODSNcFile");
      return;
    } catch (IOException e) {
      System.out.println(e + ": new DODSNcFile");
      return;
    }
    setupDisplay();
  }

  @SuppressWarnings("unused")
  private void setupDisplay() {
//    Attribute attr;
//    Variable ncVar;

    //
    // extract meta information for display in mainTextArea
    //
    String name = ncFile_.getFileName();
      if(ncFile_.isFile()) {
        name = name + " (local)";
      } else if(ncFile_.isHttp()) {
        name = name + " (Web)";
      } else if(ncFile_.isDODS()) {
        name = name + " (OPeNDAP)";
      }
//    Iterator vi = ncFile_.getVariableIterator();
//    Iterator as = ncFile_.getGlobalAttributeIterator();
//    Iterator ds = ncFile_.getDimensionIterator();
    fileText.setText(name);
      fileText.setHorizontalAlignment(JTextField.CENTER);
    setTitle(name + " - NetCDF File Browser");
    int vcount = 0;
    int dcount = 0;
    int acount = 0;
//    while(vi.hasNext()) {
    for(Variable ncVar: ncFile_.getVariables()) {
      vcount++;
//      ncVar = (Variable)vi.next();
    }
    for(ucar.nc2.Dimension dim: ncFile_.getDimensions()) {
      dcount++;
    }
//    while(ds.hasNext()) {
//      dcount++;
//      ds.next();
//    }
//    while(as.hasNext()) {
//      acount++;
//      as.next();
//    }
    for(Attribute at: ncFile_.getGlobalAttributes()) {
      acount++;
    }
    numVarText.setText(Integer.toString(vcount));
      numVarText.setHorizontalAlignment(JTextField.CENTER);
    numDimText.setText(Integer.toString(dcount));
      numDimText.setHorizontalAlignment(JTextField.CENTER);
    numAttText.setText(Integer.toString(acount));
      numAttText.setHorizontalAlignment(JTextField.CENTER);
    //
    // clear vMapModel list
    //
    vMapModel_.clear();

    setVariableList();

    exportCdlItem.setEnabled(true);
    exportUNHItem.setEnabled(true);
    asTableItem.setEnabled(true);
    asTreeItem.setEnabled(true);
  }

  void exitItem_actionPerformed(ActionEvent event) {
    openBrowsers.remove(this);
    if(oceanShare_) {
      this.setVisible(false);
      this.windowList_.closeAll();
      return;
    }
    if(openBrowsers.isEmpty()) {
      try {
        this.setVisible(false);
        if (lsd_ != null) lsd_.dispose();
        this.dispose();
        System.exit(0);
      } catch (Exception e) {
      }
    } else {
      this.exitApplication();
    }
  }

  void aboutItem_actionPerformed(ActionEvent event) {
    try {
      // JAboutDialog Create with owner and show as modal
      {
        AboutBrowse ab = new AboutBrowse(this);
        ab.setModal(true);
        ab.setVisible(true);
      }
    } catch (Exception e) {
    }
  }

  void asTableItem_actionPerformed(ActionEvent event) {
    TableView tblView = new TableView();
    tblView.setShowAllVariables(showAllVariables_);
    tblView.setNcFile(ncFile_);
    Point pt = this.getLocationOnScreen();
    tblView.setLocation(pt.x + 50, pt.y + 50);
    tblView.setVisible(true);
    //
    windowList_.addElement(tblView);
    tblView.addWindowListener(windowList_);
  }

  void asTreeItem_actionPerformed(ActionEvent event) {
    TreeView treeView = new TreeView();
    treeView.setBrowser(this);
    treeView.setShowAllVariables(showAllVariables_);
    treeView.setNcFile(ncFile_);
    Point pt = this.getLocationOnScreen();
    treeView.setLocation(pt.x + 50, pt.y + 50);
    treeView.setVisible(true);
    //
    windowList_.addElement(treeView);
    treeView.addWindowListener(windowList_);
  }

  void sysPropItem_actionPerformed(ActionEvent event) {
    try {
      // PropDisplay Create and show as non-modal
      {
        JSystemPropertiesDialog sysProps =
          new JSystemPropertiesDialog(this, "System Properties", false);
        sysProps.setVisible(true);
        windowList_.addElement(sysProps);
        sysProps.addWindowListener(windowList_);
      }
    } catch (Exception e) {
    }
  }

  void exportCdlItem_actionPerformed(ActionEvent event) {
    VariableListDialog vld = new VariableListDialog(this);
    vld.setNcFile(ncFile_);
    vld.setAction("Export Variable", VariableProcessThread.EXPORT_CDL);
    Point pt = this.getLocationOnScreen();
    vld.setLocation(pt.x + 50, pt.y);
    vld.setVisible(true);
  }


  class SymMouse extends MouseAdapter {
    public void mouseClicked(MouseEvent event)
    {
      Object object = event.getSource();
      if (object == VariableList)
        VariableList_mouseClicked(event);
    }

    public void mouseReleased(MouseEvent event) {
      Object object = event.getSource();
      if (object == numVarText)
        maybePopupTrigger(event);
    }

    public void mousePressed(MouseEvent event) {
      Object object = event.getSource();
      if (object == numVarText)
        maybePopupTrigger(event);
    }
  }

  void maybePopupTrigger(MouseEvent event) {
    if(event.isPopupTrigger()) {
      VariableListDialog vld = new VariableListDialog(this);
      vld.setNcFile(ncFile_);
      vld.setAction("Graph Variable", VariableProcessThread.GRAPHER);
      Point pt = this.getLocationOnScreen();
      vld.setLocation(pt.x + event.getX(), pt.y + event.getY());
      vld.setVisible(true);
    }
  }

  void exportUNHItem_actionPerformed(ActionEvent event) {
    VariableListDialog vld = new VariableListDialog(this);
    vld.setNcFile(ncFile_);
    vld.setAction("Export Variable", VariableProcessThread.EXPORT_UNH);
    Point pt = this.getLocationOnScreen();
    vld.setLocation(pt.x + 50, pt.y);
    vld.setVisible(true);
  }

  class SymListSelection implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent event) {
      Object object = event.getSource();
      if (object == VariableList)
        VariableList_valueChanged(event);
    }
  }

  void VariableList_valueChanged(ListSelectionEvent event) {
    JList jl = (JList)event.getSource();
    if(jl.isSelectionEmpty()) return;

    String varName = (String)jl.getSelectedValue();
    String lname = (String)longName_.get(varName);
    lnameTextField.setText("  " + lname);
  }

  void VariableList_mouseClicked(MouseEvent event) {
    if(!((event.getClickCount() == 2) ||
         ((event.getModifiers()&InputEvent.BUTTON3_MASK) != 0))) return;
    String varName = (String)VariableList.getSelectedValue();
    openDomainSelector(varName);
  }

  public void openDomainSelector(String varName) {
    //
    if(varName != null) {
      if(ncFile_.findVariable(varName) != null) {   //  netCDF file
//        String lname = (String)longName_.get(varName);
        if(Debug.DEBUG) System.out.println("Variable " + varName + " selected");

        Point loc = getLocationOnScreen();
        Dimension bs = getSize();
        DomainSelector domSel = new DomainSelector();
        domSel.setTimeFormat(timeFormat_);
        domSel.setParent(this);
//        if(ncFile_.findVariable(varName).getElementType().getName().equals("char")) {
        if(ncFile_.findVariable(varName).getDataType() == DataType.CHAR) {
          domSel.setActionButton("Show Char Data",
                                 VariableProcessThread.TEXTUAL);
        } else {
          domSel.setActionButton("Graph Variable",
                                 VariableProcessThread.GRAPHER);
        }
        domSel.setVariable(ncFile_, varName);
        Dimension ds = domSel.getSize();
        int x = loc.x + bs.width/2 - ds.width/2;
        int y = loc.y + bs.height + 10;
        domSel.setPosition(x, y);
        new Thread(domSel).start();
        //
        windowList_.addElement(domSel);
        domSel.addWindowListener(windowList_);
      }
      else {  //  Variable Map
        VMapModel model = vMapModel_.get(varName);
        if(model == null) {
          JOptionPane.showMessageDialog(this,
                                        "Variable not found in netCDF or Variable Map Lists",
                                        "Variable Not Found",
                                        JOptionPane.ERROR_MESSAGE);
          return;
        }
        /**
         * @todo include Variable Map stuff here
         */
        //
        // VMapModel has been selected open graph
        //
//	vmm.setShowEditor(true);
        if(model instanceof VMapModel) {
          VMapModel vmm = (VMapModel)model;
          VMapGraph vmg = new VMapGraph(vmm, this);
          vmg.start();
          //
          if(Debug.DEBUG) {
            System.out.println("Variable map model for " + varName);
            for(int i=0; i < VMapModel.ELEMENT_COUNT; i++) {
              if(!vmm.isSet(i)) continue;
              String name = vmm.getElementName(i);
              String target = VMapModel.getTitle(i);
              System.out.println("   " + name + " ---> " + target);
            }
          }
//        } else if(model instanceof ncBrowse.vmap.VMapModel) {
//          ncBrowse.vmap.VMapModel vmm = (ncBrowse.vmap.VMapModel)model;
//          ncBrowse.vmap.VMapGraph vmg = new ncBrowse.vmap.VMapGraph(vmm, this);
//          vmg.start();
//          //
//          if(Debug.DEBUG) {
//            System.out.println("Variable map model for " + varName);
//            for(int i=0; i < VMapModel.ELEMENT_COUNT; i++) {
//              if(!vmm.isSet(i)) continue;
//              String name = vmm.getElementName(i);
//              String target = VMapModel.getTitle(i);
//              System.out.println("   " + name + " ---> " + target);
//            }
//          }

        }
      }
    }
  }

  void newsItem_actionPerformed(ActionEvent event) {
//    JViewHTMLDialog ub = new JViewHTMLDialog(this, "ncBrowse News", false);
    JViewHTMLFrame ub = new JViewHTMLFrame("ncBrowse News");
    ub.setPage("http://www.epic.noaa.gov/java/ncBrowse/news.html");
    ub.setLocation(100,100);
    ub.setVisible(true);
  }

  public void setTimeFormat(String format) {
    timeFormat_ = format;
  }

  public String getTimeFormat() {
    return timeFormat_;
  }

  public void setShowAllVariables(boolean show) {
    showAllVariables_ = show;
    setVariableList();
  }

  public boolean isShowAllVariables() {
    return showAllVariables_;
  }

  void optionItem_actionPerformed(ActionEvent event) {
    try {
      // OptionsDialog Create with owner and title, show as modal
      {
        OptionsDialog OptionsDialog1 = new OptionsDialog(this);
        OptionsDialog1.setModal(true);
        OptionsDialog1.setTitle("ncBrowse Options");
        OptionsDialog1.setVisible(true);
      }
    } catch (Exception e) {
    }
  }

  void closeItem_actionPerformed(ActionEvent event) {
    openBrowsers.remove(this);
    if(oceanShare_) {
      this.setVisible(false);
      this.windowList_.closeAll();
      return;
    }
    if(openBrowsers.isEmpty()) {
      try {
        this.setVisible(false);
        this.dispose();
        System.exit(0);
      } catch (Exception e) {
      }
    } else {
      this.windowList_.closeAll();
      this.setVisible(false);
      this.dispose();
    }
  }

  class SymMenu implements MenuListener {
    public void menuSelected(MenuEvent event) {
      Object object = event.getSource();
      if (object == windowMenu)
        windowMenu_menuSelected(event);
    }

    public void menuCanceled(MenuEvent event) {
    }

    public void menuDeselected(MenuEvent event) {
    }
  }

  void windowMenu_menuSelected(MenuEvent event) {
    Object[] cb = windowList_.getMenuItems();
    int len = windowMenu.getItemCount();
    JCheckBoxMenuItem cbmi = null;
    Window win = null;
    boolean showing = false;
    if(Debug.DEBUG) {
      System.out.println("Menu Selected (Window)");
      System.out.println("item count = " + len);
    }

    for(int i=3; i < len; i++) {
      windowMenu.remove(3);
    }

    for(int i=0; i < cb.length; i++) {
      win = (Window)windowList_.get(i);
      cbmi = (JCheckBoxMenuItem)(cb[i]);
      if(win instanceof JFrame) {
        showing = win.isShowing() &&
          ((JFrame)win).getState() == Frame.NORMAL;
      } else {
        showing = win.isShowing();
      }
      cbmi.setState(showing);
      windowMenu.add(cbmi);
    }
  }

  public CheckBoxAction getCheckBoxAction() {
    return cbAction_;
  }

  void checkBoxItem_actionPerformed(ActionEvent event) {
    Window win = null;
    JCheckBoxMenuItem cbmi = (JCheckBoxMenuItem)event.getSource();
    Object[] cb = windowList_.getMenuItems();
    for(int i=0; i < cb.length; i++) {
      if(cb[i].equals(cbmi)) {
        win = (Window)windowList_.get(i);
      }
    }
    win.setVisible(cbmi.getState());
    if(win instanceof JFrame) {
      ((JFrame)win).setState(Frame.NORMAL);
    }
  }

  void minAllItem_actionPerformed(ActionEvent event) {
    Window win = null;
    for(Enumeration<Window> e = windowList_.elements(); e.hasMoreElements();) {
      win = e.nextElement();
      win.setVisible(false);
    }
  }

  void maxAllItem_actionPerformed(ActionEvent event) {
    Window win = null;
    for(Enumeration<Window> e = windowList_.elements(); e.hasMoreElements();) {
      win = e.nextElement();
      win.setVisible(true);
      if(win instanceof JFrame) {
        ((JFrame)win).setState(Frame.NORMAL);
      }
    }
  }

  public WindowList getWindowList() {
    return windowList_;
  }

  void helpItem_actionPerformed(ActionEvent event)       {
//    JViewHTMLDialog ub = new JViewHTMLDialog(this, "ncBrowse Help", false);
    JViewHTMLFrame ub = new JViewHTMLFrame("ncBrowse Help");
    ub.setPage("http://www.epic.noaa.gov/java/ncBrowse/help.html");
    ub.setLocation(100,100);
    ub.setVisible(true);
  }

  void newMapButton_actionPerformed(ActionEvent e) {
    Vector<Object> listdata = new Vector<Object>();
    Vector<Variable> varList = new Vector<Variable>();
    Vector<Variable> varDimList = new Vector<Variable>();
//    Variable ncVar;
//    ucar.nc2.Dimension ncDim;
//    Iterator iter;
    if(ncFile_ == null) return;

//    iter = ncFile_.getDimensionIterator();
//    while(iter.hasNext()) {
    for(ucar.nc2.Dimension ncDim: ncFile_.getDimensions()) {
//      ncDim = (ucar.nc2.Dimension)iter.next();
      listdata.addElement(ncDim);
    }

//    iter = ncFile_.getVariableIterator();

//    while(iter.hasNext()) {
    for(Variable ncVar: ncFile_.getVariables()) {
//      ncVar = (Variable)iter.next();
      List<ucar.nc2.Dimension> al = ncVar.getDimensions();
      if((al.size() == 1) && ((ucar.nc2.Dimension)al.get(0)).getName().equals(ncVar.getName())) {
        varDimList.addElement(ncVar);
      } else {
        varList.addElement(ncVar);
      }
    }
    
    // combine lists
    listdata.addAll(varDimList);
    listdata.addAll(varList);

    VariableMapDialog vmd = new VariableMapDialog(this,
                                                  "Create new Variable Map",
                                                  ncFile_, false);
    vmd.setLocation(100,100);
    vmd.setList(ncFile_, listdata);
    vmd.setVisible(true);

// now handled by dialogDismissed method
/*    VMapModel vmm = vmd.getCurrentMap();
    if(vmm.isValid()) {
      String vmName = "<html><font color='blue'>" + vmm.getName() + "</font></html>";
      vMapModel_.put(vmName, vmm);
      setVariableList();   // update list
    } */
  }


  static public boolean isSystemLF() {
    return useSystemLF_;
  }
  public String getOPeNDAPFile() {
    return OPeNDAPFile_;
  }
  public void setOPeNDAPFile(String OPeNDAPFile) {
    this.OPeNDAPFile_ = OPeNDAPFile;
  }

  public void dialogDismissed(JDialog d) {
    VMapModel vmm = VariableMapDialog.getCurrentMap();
    if(vmm.isValid() && vmm.getName() != null) {
      String vmName = "<html><font color='blue'>" + vmm.getName() + "</font></html>";
      vMapModel_.put(vmName, vmm);

      longName_.put(vmName, vmName + " (Variable Map)");
      setVariableList();
    }
  }

    // Cancel button
  public void dialogCancelled(JDialog d){}

    // something other than the OK button
  public void dialogDismissedTwo(JDialog d){}

    // Apply button, OK w/o dismissing the dialog
  public void dialogApply(JDialog d){}

    // Apply button, OK w/o dismissing the dialog
  public void dialogApplyTwo(Object d){}

  void openDapper_actionPerformed(ActionEvent e) {
    if(dapperWiz_ == null) {
      dapperWiz_ = new DapperWizard(true);
      dapperWiz_.addSelectionListener(this);
    } else {
      dapperWiz_.reset();
    }
    dapperWiz_.setVisible(true);
  }

  void newBrowser_actionPerformed(ActionEvent e) {
    Browser newBrowser = null;
    newBrowser = new Browser();
    Dimension ss = getSize();
    Point pt = getLocationOnScreen();
    newBrowser.setLocation(pt.x, pt.y + ss.height + 10);
    newBrowser.setVisible(true);
    openBrowsers.addElement(newBrowser);
  }

  /**
   * selectionPerformed
   *
   * @param e SelectionEvent
   * @todo Implement this ncBrowse.dapper.SelectionListener method
   */
  public void selectionPerformed(SelectionEvent e) {
    StationCollection coll = e.getCollection();
    GeoDomain[] refs = e.getReferences();
    Iterator stIter = coll.getStationIterator(refs, null);
//    Iterator stIter = e.getIterator();
    while(stIter.hasNext()) {
      ncFile_ = new DapperNcFile((StationDataset)stIter.next());
      setupDisplay();
    }
  }
  public boolean isOceanShare() {
    return oceanShare_;
  }

}
