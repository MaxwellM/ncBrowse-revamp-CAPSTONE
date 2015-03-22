/**
 *  $Id: AboutBrowse.java 16 2013-04-24 21:15:01Z dwd $
 */
package ncBrowse;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Basic information about netCDF Browser application.
 *
 * @author Donald Denbo
 * @version $Revision: 1.45 $, $Date: 2005/10/21 19:28:53 $
 */
public class AboutBrowse extends JDialog {
  String version_ = "Version 1.6.7 (May 20, 2013)";
  BorderLayout borderLayout1 = new BorderLayout(0,0);
  FlowLayout flowLayout1 = new FlowLayout(FlowLayout.CENTER, 5,5);
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  JPanel panel1 = new JPanel();

  public AboutBrowse(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public AboutBrowse(Frame parentFrame) {
    this(parentFrame, null, false);
  }

  void jbInit() throws Exception {

    setResizable(false);
    setTitle("netCDF Browser");
    setModal(true);
    panel1.setLayout(borderLayout1);
    jLabel1.setFont(new Font("Dialog", Font.PLAIN, 14));
    jLabel1.setToolTipText("");
    jLabel1.setText("Java netCDF from Unidata");
    jLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
    jLabel2.setText("(http://www.unidata.ucar.edu/packages/netcdf)");
    jLabel3.setFont(new Font("Dialog", Font.PLAIN, 14));
    jLabel3.setToolTipText("");
    jLabel3.setText("Web Access by HTTPClient");
    jLabel4.setFont(new Font("Dialog", Font.BOLD, 12));
    jLabel4.setText("(http://www.innovation.ch/java/HTTPClient)");
    JLabel5.setFont(new Font("Dialog", Font.BOLD, 12));
    JLabel7.setFont(new Font("Dialog", Font.BOLD, 12));
    jPanel1.setLayout(flowLayout2);
    flowLayout2.setVgap(0);
    jLabel5.setFont(new Font("Dialog", Font.PLAIN, 14));
    jLabel5.setText("Live Acces Server from TMAP");
    jLabel6.setFont(new Font("Dialog", Font.BOLD, 12));
    jLabel6.setText("(http://ferret.pmel.noaa.gov/Ferret/LAS/ferret_LAS.html)");
    jLabel7.setText("Thanks to");
    jLabel8.setText("John Osborne for 3D using VisAD");
    jLabel9.setText("Joe Sirott for LAS access");
    jPanel2.setBorder(etchedBorder2);
    jPanel2.setLayout(gridBagLayout5);
    iconLabel1.setText("");
    iconLabel1.setText("icon");
    iconLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    iconLabel2.setText("");
    iconLabel2.setText("icon");
    iconLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    iconLabel3.setText("");
    iconLabel3.setText("icon");
    iconLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel11.setText("ncBrowse");
    jLabel11.setForeground(Color.blue);
    jLabel11.setFont(new Font("Dialog", Font.PLAIN, 48));
    aboutLabel1.setFont(new Font("Dialog", Font.BOLD, 16));
    aboutLabel1.setText("A netCDF Browser Application");
    aboutLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel10.setText(version_);
    jLabel10.setFont(new Font("Dialog", Font.PLAIN, 14));
    jLabel10.setText("VisAD from Univ of Wisconsin-Madison");
    jLabel12.setFont(new Font("Dialog", Font.BOLD, 12));
    jLabel12.setText("(http://www.ssec.wisc.edu/~billh/visad.html)");
    getContentPane().add(panel1);

    setSize(new Dimension(382, 542));
    setVisible(false);
    buttonPanel.setBorder(etchedBorder1);
    buttonPanel.setLayout(flowLayout1);
    okButton.setText("OK");
    okButton.setOpaque(false);
    okButton.setActionCommand("OK");
    okButton.setMnemonic((int)'O');
    JPanel2.setLayout(gridBagLayout1);
    panel1.add(JPanel2,"Center");
    JPanel1.setBorder(etchedBorder2);
    JPanel1.setLayout(gridBagLayout2);
    JPanel2.add(JPanel1,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
    iconLabel.setText("icon");
    JLabel1.setText("by");
    JPanel1.add(JLabel1,          new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    JLabel2.setText("Donald Denbo");
    JPanel1.add(JLabel2,        new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    JLabel3.setText("NOAA/PMEL/EPIC");
    JPanel1.add(JLabel3,         new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    JPanel1.add(jLabel7,       new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    JPanel1.add(jLabel8,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    JPanel1.add(jLabel9,      new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
    JPanel4.setBorder(etchedBorder2);
    JPanel4.setLayout(gridBagLayout3);
    JPanel2.add(JPanel4,  new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    JLabel8.setText("Graphics powered by");
    JLabel8.setFont(new Font("Dialog", Font.PLAIN, 14));
    JLabel9.setText("sgt!");
    JLabel9.setForeground(Color.red);
    JLabel9.setFont(new Font("Dialog", Font.BOLD, 14));
    JLabel7.setText("(http://www.epic.noaa.gov/sgt)");
    JPanel4.add(jLabel4,    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 10, 12, 10), 0, 0));
    JPanel4.add(jLabel1,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    JPanel4.add(jLabel3,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    JPanel4.add(JLabel7,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 12, 0), 0, 0));
    JPanel4.add(jLabel2,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 10, 12, 10), 0, 0));
    JPanel4.add(jPanel1,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
    jPanel1.add(JLabel8, null);
    jPanel1.add(JLabel9, null);
    JPanel4.add(jLabel5,  new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    JPanel4.add(jLabel6,    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 10, 12, 10), 0, 0));
    JPanel4.add(jLabel10, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    JPanel4.add(jLabel12,  new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 10, 5, 10), 0, 0));
    JPanel3.setBorder(etchedBorder2);
    JPanel3.setLayout(gridBagLayout4);
    JPanel2.add(JPanel3,  new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    JLabel4.setText("More information at:");
    JPanel3.add(JLabel4,new GridBagConstraints(0,0,1,1,1.0,1.0,
    GridBagConstraints.CENTER,GridBagConstraints.NONE
    ,new Insets(5,0,0,0),0,0));
    JLabel5.setText("http://www.epic.noaa.gov/java");
    JPanel3.add(JLabel5,new GridBagConstraints(0,1,1,1,1.0,1.0,
    GridBagConstraints.CENTER,GridBagConstraints.NONE,
    new Insets(0,0,10,0),0,0));
    JPanel2.add(jPanel2,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(JLabel10,      new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    jPanel2.add(iconLabel,      new GridBagConstraints(0, 0, 1, 3, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
    jPanel2.add(aboutLabel1,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    jPanel2.add(jLabel11,      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 15, 5, 15), 0, 0));
    panel1.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(okButton);

    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    SymAction lSymAction = new SymAction();
    okButton.addActionListener(lSymAction);

    ImageIcon icon = new ImageIcon(getClass().getResource("images/ncBrowse96.gif"));
    iconLabel.setText("");
    iconLabel.setIcon(icon);
  }

  public void setVisible(boolean b) {
    if (b) {
      Rectangle bounds = (getParent()).getBounds();
      Dimension size = getSize();
      setLocation(bounds.x + (bounds.width - size.width)/2,
      bounds.y + (bounds.height - size.height)/2);
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
    Insets insets = getInsets();
    setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height);
    Component components[] = getContentPane().getComponents();
      for (Component component : components) {
          Point p = component.getLocation();
          p.translate(insets.left, insets.top);
          component.setLocation(p);
      }
    fComponentsAdjusted = true;
  }

  // Used for addNotify check.
  boolean fComponentsAdjusted = false;

  JPanel buttonPanel = new JPanel();
  JButton okButton = new JButton();
  JPanel JPanel2 = new JPanel();
  JPanel JPanel1 = new JPanel();
  JLabel iconLabel = new JLabel();
  JLabel JLabel1 = new JLabel();
  JLabel JLabel2 = new JLabel();
  JLabel JLabel3 = new JLabel();
  JPanel JPanel4 = new JPanel();
  JLabel JLabel8 = new JLabel();
  JLabel JLabel9 = new JLabel();
  JLabel JLabel7 = new JLabel();
  JPanel JPanel3 = new JPanel();
  JLabel JLabel4 = new JLabel();
  JLabel JLabel5 = new JLabel();
  EtchedBorder etchedBorder1 = new EtchedBorder();
  EtchedBorder etchedBorder2 = new EtchedBorder(EtchedBorder.RAISED);
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JPanel jPanel1 = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  private JLabel jLabel5 = new JLabel();
  private JLabel jLabel6 = new JLabel();
  private JLabel jLabel7 = new JLabel();
  private JLabel jLabel8 = new JLabel();
  private JLabel jLabel9 = new JLabel();
  private JPanel jPanel2 = new JPanel();
  private JLabel iconLabel1 = new JLabel();
  private JLabel iconLabel2 = new JLabel();
  private JLabel iconLabel3 = new JLabel();
  private JLabel jLabel11 = new JLabel();
  private JLabel aboutLabel1 = new JLabel();
  private JLabel JLabel10 = new JLabel();
  private GridBagLayout gridBagLayout5 = new GridBagLayout();
  private JLabel jLabel10 = new JLabel();
  private JLabel jLabel12 = new JLabel();

  class SymWindow extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
      Object object = event.getSource();
      if (object == AboutBrowse.this)
  jAboutDialog_windowClosing(event);
    }
  }

  void jAboutDialog_windowClosing(WindowEvent event) {
    try {
      // JAboutDialog Hide the JAboutDialog
      this.setVisible(false);
    } catch (Exception e) {
    }
  }

  class SymAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == okButton)
  okButton_actionPerformed(event);
    }
  }

  void okButton_actionPerformed(ActionEvent event) {
    try {
      // JAboutDialog Hide the JAboutDialog
      this.setVisible(false);
    } catch (Exception e) {
    }
  }
}
