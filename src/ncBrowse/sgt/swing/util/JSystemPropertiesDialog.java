/*
 */
package ncBrowse.sgt.swing.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * Displays the system properties in a scrolling <code>JTable</code>.  The
 * properties that consist of multiple parts, e.g. CLASSPATH, are
 * displayed one component per line.
 *
 * @author Donald Denbo
 * @version $Revision: 1.10 $, $Date: 2003/08/19 21:19:22 $
 * @see javax.swing.JTable
 * @see java.lang.System#getProperties()
 **/
public class JSystemPropertiesDialog extends javax.swing.JDialog {

    private final static Object Static = null;
    private final static Object[] NoArgs = null;
    private final static Class[] NoParameters = new Class[0];


    private JTable propTable;

    public JSystemPropertiesDialog(String sTitle)  {
        this(null, sTitle, false);
    }

    public JSystemPropertiesDialog()  {
        this(null, null, false);
    }

    public JSystemPropertiesDialog(Frame frame, String title) {
        this(frame, title, false);
    }

    public JSystemPropertiesDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);

        if(title == null) {
            setTitle("System Properties");
        }
        createTable();
        getContentPane().setLayout(new BorderLayout(0,0));
        getContentPane().setBackground(new Color(200, 200, 200));
        setSize(556,305);
        setVisible(false);
        displayPanel.setLayout(new GridBagLayout());
        getContentPane().add(BorderLayout.CENTER,displayPanel);
        displayPanel.setBounds(0, 0, 556, 270);
        JScrollPane1.setOpaque(true);
        JScrollPane1.setBackground(new Color(200, 200, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(5,5,5,5);
        gbc.ipadx = 0;
        gbc.ipady = 0;
        displayPanel.add(JScrollPane1, gbc );

        JScrollPane1.setBounds(5,5,546,260);
        JScrollPane1.getViewport().add(propTable);
        propTable.setBounds(0,0,543,0);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
        getContentPane().add(BorderLayout.SOUTH,buttonPanel);
        buttonPanel.setBounds(0,270,556,35);
        OKButton.setText("OK");
        OKButton.setActionCommand("OK");
        buttonPanel.add(OKButton);
        OKButton.setBounds(252,5,51,25);

        SymAction lSymAction = new SymAction();
        OKButton.addActionListener(lSymAction);

    }

    void createTable() {
        Properties prop = System.getProperties();
        //
        int size = prop.size() + 50;
        Vector names = new Vector(size);
        Vector values = new Vector(size);
        //
        // sniff out the version of sgt!
        //
        String ver = null;
        try {
            Class cls = Class.forName("sgt.JPane");
            java.lang.reflect.Method meth = cls.getMethod("getVersion", NoParameters);
            ver = (String)meth.invoke(Static, NoArgs);
        } catch (Exception ignored) {
        } finally {
            names.add("sgt.version");
            values.add(ver);
        }
        //
        String separator = prop.getProperty("path.separator", ";");
        int row=0;
        Enumeration e = prop.propertyNames();
        while(e.hasMoreElements()) {
            String name = (String)e.nextElement();
            String value = prop.getProperty(name);
            int len = value.length();
            if(value.contains(separator) && !name.equals("path.separator")) {
                int lastIndex = 0;
                int count = 1;
                int ind;
                int[] indicies = new int[200];
                indicies[0] = 0;
                while((ind = value.indexOf(separator, lastIndex)) != -1) {
                    indicies[count] = ind + 1;
                    lastIndex = ind + 1;
                    count++;
                }
                indicies[count] = len;
                for(int i=1; i <= count; i++) {
                    names.addElement(name + "[" + i + "]");
                    values.addElement(value.substring(indicies[i-1], indicies[i]));
                    row++;
                }
            } else {
                names.addElement(name);
                values.addElement(value);
                row++;
            }
        }
        Enumeration enames = names.elements();
        Enumeration evalues = values.elements();
        String[][] data = new String[names.size()][2];
        for(int i=0; i < names.size(); i++) {
            data[i][0] = (String)enames.nextElement();
            data[i][1] = (String)evalues.nextElement();
        }
        propTable = new JTable(data, new String[] {"Property", "Value"});
        propTable.setSize(1000,1000);
        TableColumn tc;
        tc = propTable.getColumnModel().getColumn(0);
        tc.setPreferredWidth(100);
        tc = propTable.getColumnModel().getColumn(1);
        tc.setPreferredWidth(300);
    }


    public void setVisible(boolean b) {
        if (b) {
            setLocation(50, 50);
            //      init();
        }
        super.setVisible(b);
    }

    public void addNotify() {
        // Record the size of the window prior to calling parents addNotify.
        Dimension size = getSize();

        super.addNotify();

        if (frameSizeAdjusted)
            return;
        frameSizeAdjusted = true;

        // Adjust size of frame according to the insets
        Insets insets = getInsets();
        setSize(insets.left + insets.right + size.width,
                insets.top + insets.bottom + size.height);
    }

    // Used by addNotify
    boolean frameSizeAdjusted = false;

    javax.swing.JPanel displayPanel = new javax.swing.JPanel();
    javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
    javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
    javax.swing.JButton OKButton = new javax.swing.JButton();

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event)    {
            Object object = event.getSource();
            if (object == OKButton)
                OKButton_actionPerformed(event);
        }
    }

    void OKButton_actionPerformed(java.awt.event.ActionEvent event) {
        try {
            // JSystemPropertiesDialog Hide the JSystemPropertiesDialog
            this.setVisible(false);
        } catch (java.lang.Exception ignored) {
        }
    }

    public static void main(String[] args) {
        JSystemPropertiesDialog js = new JSystemPropertiesDialog();
        js.setTitle("Test System Properties Dialog");
        js.setVisible(true);
    }
}
