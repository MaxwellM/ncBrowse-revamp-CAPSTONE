/*
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package ncBrowse.sgt.beans;

import ncBrowse.sgt.geom.SoTRange;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author Donald Denbo
 * @version $Revision: 1.2 $, $Date: 2003/08/22 23:02:33 $
 * @since 3.0
 **/
class DataGroupPropertyPanel extends PropertyPanel
    implements ActionListener, ChangeListener, FocusListener {
    private boolean expert_ = false;
    private String[] pNames_ = {"Id", "Margin", "Zoomable", "Z AutoScale", "Z Auto Levels", "Z User Range"};
    private JComponent[] comps_ = new JComponent[pNames_.length];
    private DataGroup dataGroup_ = null;
    private boolean suppressEvent_ = false;
    private int zAutoScale, zUserRange, zAutoLevels;

    public DataGroupPropertyPanel(DataGroup dg, boolean expert) {
        super();
        dataGroup_ = dg;
        dataGroup_.addChangeListener(this);
        expert_ = expert;
        create();
    }

    public void setDataGroup(DataGroup dg, boolean expert) {
        if(dataGroup_ != null) dataGroup_.removeChangeListener(this);
        dataGroup_ = dg;
        dataGroup_.addChangeListener(this);
        expert_ = expert;
        reset();
    }

    void update() {
        int i = -1;
        suppressEvent_ = true;
        int item = -1;
        ((JTextField)comps_[++i]).setText(dataGroup_.getId());
        ((JLabel)comps_[++i]).setText(dataGroup_.getMargin().toString());
        ((JCheckBox)comps_[++i]).setSelected(dataGroup_.isZoomable());
        //
        // z stuff
        //
        ((JCheckBox)comps_[++i]).setSelected(dataGroup_.isZAutoScale());
        ((JTextField)comps_[++i]).setText(format(dataGroup_.getNumberAutoContourLevels()));
        ((JTextField)comps_[++i]).setText(format(dataGroup_.getZRangeU(), false));
        //
        setFieldsEnabled();
        //
        suppressEvent_ = false;
    }

    void create() {
        int i = -1;
        int item = -1;
        comps_[++i] = createTextField(dataGroup_.getId(), pNames_[i], this, !dataGroup_.isInstantiated());
        comps_[++i] = createLabel(dataGroup_.getMargin().toString());
        comps_[++i] = createCheckBox(dataGroup_.isZoomable(), pNames_[i], this);
        //
        // z stuff
        //
        comps_[++i] = createCheckBox(dataGroup_.isZAutoScale(), pNames_[i], this);
        zAutoScale = i;
        comps_[++i] = createTextField(format(dataGroup_.getNumberAutoContourLevels()), pNames_[i], this, true);
        zAutoLevels = i;
        comps_[++i] = createTextField(format(dataGroup_.getZRangeU(), false), pNames_[i], this, true);
        zUserRange = i;
        //
        setFieldsEnabled();
        //
        for(i=0; i < comps_.length; i++) {
            addProperty(i+1, pNames_[i], comps_[i], false);
        }
        addProperty(comps_.length + 1, " ", new JLabel(" "), true);
    }

    void resetFields() {
        for (JComponent aComps_ : comps_) {
            if (aComps_ instanceof JTextField) {
                ((JTextField) aComps_).removeActionListener(this);
                ((JTextField) aComps_).removeFocusListener(this);
            } else if (aComps_ instanceof JCheckBox) {
                ((JCheckBox) aComps_).removeActionListener(this);
                ((JCheckBox) aComps_).removeFocusListener(this);
            } else if (aComps_ instanceof JComboBox) {
                ((JComboBox) aComps_).removeActionListener(this);
                ((JComboBox) aComps_).removeFocusListener(this);
            } else if (aComps_ instanceof JButton) {
                ((JButton) aComps_).removeActionListener(this);
                ((JButton) aComps_).removeFocusListener(this);
            }
        }
    }

    private void setFieldsEnabled() {
        comps_[zUserRange].setEnabled(!((JCheckBox) comps_[zAutoScale]).isSelected());
        comps_[zAutoLevels].setEnabled(((JCheckBox) comps_[zAutoScale]).isSelected());
    }

    private void processEvent(Object obj, String command) {
        String str = null;
        SoTRange range = null;
        switch (command) {
            case "Id":
                String oldId = dataGroup_.getId();
                dataGroup_.getPanelHolder().getDataGroups().remove(oldId);
                dataGroup_.setId(((JTextField) obj).getText());
                dataGroup_.getPanelHolder().getDataGroups().put(dataGroup_.getId(), dataGroup_);
                break;
            case "Zoomable":
                dataGroup_.setZoomable(((JCheckBox) obj).isSelected());
                break;
            case "Z AutoScale":
                dataGroup_.setZAutoScale(((JCheckBox) obj).isSelected());
                break;
            case "Z Auto Levels":
                dataGroup_.setNumberAutoContourLevels(Integer.parseInt(((JTextField) obj).getText()));
                break;
            case "Z User Range":
                range = parseRange(((JTextField) obj).getText(), false);
                if (range != null) dataGroup_.setZRangeU(range);
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(suppressEvent_) return;
        Object obj = e.getSource();
        //    String str = null;
        String command = e.getActionCommand();
        processEvent(obj, command);
        setFieldsEnabled();
        //    System.out.println(e.paramString() + ",rslt=" + str);
    }


    public void stateChanged(ChangeEvent e) {
        update();
    }

    public void focusGained(FocusEvent e) {
        //    Object obj = e.getSource();
        //    System.out.println("DataGroupPropertyPanel.focusGained: " + obj.toString());
    }

    public void focusLost(FocusEvent e) {
        Object obj = e.getSource();
        if(obj instanceof JTextField) {
            JTextField tf = (JTextField)obj;
            String name = tf.getName();
            processEvent(obj, name);
        }
    }

    /*  SoTRange parseRange(String value, boolean isTime) {
        StringTokenizer tok = new StringTokenizer(value, ",\t\n\r\f");
        if(tok.countTokens() != 3) {
        JOptionPane.showMessageDialog(this, "Three values required", "Illegal Response", JOptionPane.ERROR_MESSAGE);
        return null;
        }
        SoTRange range = null;
        double start = Double.parseDouble(tok.nextToken().trim());
        double end = Double.parseDouble(tok.nextToken().trim());
        double delta  = Double.parseDouble(tok.nextToken().trim());
        range = new SoTRange.Double(start, end, delta);
        return range;
        } */

    public void setExpert(boolean expert) {
        boolean save = expert_;
        expert_ = expert;
        if(expert_ != save) reset();
    }

    public boolean isExpert() {
        return expert_;
    }
}