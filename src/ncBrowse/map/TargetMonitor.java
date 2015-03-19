/**
 *  $Id: TargetMonitor.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import java.util.List;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ucar.nc2.Dimension;
import ucar.nc2.Variable;
/**
 * <pre>
 * Title:        Drag and Drop test application
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.19 $, $Date: 2004/05/17 22:49:34 $
 */
public class TargetMonitor {
  /**
   * @label instance
   */
  static TargetMonitor monitor_ = null;

  JLabel[] title_;
  JLabel[] item_;
  JPanel[] panel_;

  static Color ENABLED = new Color(186, 217, 217);
  static Color DEFAULT = new Color(102, 102, 153);

  private TargetMonitor() {
    title_ = new JLabel[VMapModel.ELEMENT_COUNT];
    item_ = new JLabel[VMapModel.ELEMENT_COUNT];
    panel_ = new JPanel[VMapModel.ELEMENT_COUNT];
  }

  static public TargetMonitor getInstance() {
    if(monitor_ == null) {
      monitor_ = new TargetMonitor();
    }
    return monitor_;
  }

  public void reset() {
    panel_[VMapModel.U_COMPONENT].setBackground(ENABLED);
    panel_[VMapModel.V_COMPONENT].setBackground(ENABLED);
    panel_[VMapModel.Z_CONTOUR].setBackground(ENABLED);
    panel_[VMapModel.LINE_COLOR].setBackground(ENABLED);
    item_[VMapModel.U_COMPONENT].getDropTarget().setActive(true);
    item_[VMapModel.V_COMPONENT].getDropTarget().setActive(true);
    item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(true);
    item_[VMapModel.LINE_COLOR].getDropTarget().setActive(true);
    for(int i=0; i < VMapModel.ELEMENT_COUNT; i++) {
    	if (item_[i] != null)
      		item_[i].setText("");
    }
    title_[VMapModel.X_AXIS].setForeground(Color.red);
    title_[VMapModel.Y_AXIS].setForeground(Color.red);
    title_[VMapModel.U_COMPONENT].setForeground(DEFAULT);
    title_[VMapModel.V_COMPONENT].setForeground(DEFAULT);
  }

  public void setItem(int type, JLabel item, JPanel panel, JLabel title) {
    item_[type] = item;
    panel_[type] = panel;
    title_[type] = title;
  }

  public void valueChanged(int type) {
    VMapModel map = VariableMapDialog.getCurrentMap();
    switch(type) {
    case VMapModel.X_AXIS:
      title_[VMapModel.X_AXIS].setForeground(DEFAULT);
      break;
    case VMapModel.Y_AXIS:
      title_[VMapModel.Y_AXIS].setForeground(DEFAULT);
      break;
    case VMapModel.LINE_COLOR:
      panel_[VMapModel.Z_CONTOUR].setBackground(Color.lightGray);
      item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(false);
      break;
    case VMapModel.U_COMPONENT:
    case VMapModel.V_COMPONENT:
      if(map.isSet(VMapModel.V_COMPONENT)) {
        title_[VMapModel.V_COMPONENT].setForeground(DEFAULT);
      } else {
        title_[VMapModel.V_COMPONENT].setForeground(Color.red);
      }
      if(map.isSet(VMapModel.U_COMPONENT)) {
        title_[VMapModel.U_COMPONENT].setForeground(DEFAULT);
      } else {
        title_[VMapModel.U_COMPONENT].setForeground(Color.red);
      }
      panel_[VMapModel.Z_CONTOUR].setBackground(Color.lightGray);
      item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(false);
      break;
    case VMapModel.Z_CONTOUR:
      panel_[VMapModel.U_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.U_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.LINE_COLOR].setBackground(Color.lightGray);
      item_[VMapModel.LINE_COLOR].getDropTarget().setActive(false);
      break;
    }
  }

  public void updateAll(VMapModel map) {
    for(int i=0; i < VMapModel.ELEMENT_COUNT; i++) {
      if(map.isSet(i) && item_[i] != null)
        updateItem(i, map);
    }
  }

  public void updateItem(int type, VMapModel map) {
    String s = null;
    Object obj = map.getElement(type);
    if(obj instanceof Dimension) {
      s = getDimensionLabel(type, map, (Dimension)obj);
    } else if(obj instanceof Variable) {
      s = getVariableLabel(type, map, (Variable)obj);
    } else {
      s = "Bad Value";
    }
    item_[type].setText("<html>"+s+"</html>");
  }

  String getVariableLabel(int type, VMapModel map, Variable ncVar) {
    StringBuffer sbuf = new StringBuffer();
    List al = ncVar.getDimensions();
    if((al.size() == 1) && ((Dimension)al.get(0)).getName().equals(ncVar.getName())) {
      sbuf.append("<b>" + ncVar.getName() + "</b>   ");
    } else {
      sbuf.append(ncVar.getName() + "   ");
    }
    sbuf.append(getDimensionList(type, map, ncVar));
    return sbuf.toString();
  }

  String getDimensionLabel(int type, VMapModel map, Dimension ncDim) {
    StringBuffer sbuf = new StringBuffer();
    if(map.hasMatch(type, ncDim)) {
      sbuf.append("<font color=green>");
    } else {
      sbuf.append("<font color=red>");
    }
    sbuf.append("<em>" + ncDim.getName() + "</em></font>  ");
    sbuf.append("<em>(" + ncDim.getLength() + " points)</em>");
    return sbuf.toString();
  }

  /**
   * Look for dimension match.  If no match is found all dimensions are shown in
   * red.  If any match is found, they are shown in green, and the rest in blue.
   */
  String getDimensionList(int type, VMapModel map, Variable ncVar) {
    List al = ncVar.getDimensions();
    boolean[] match = new boolean[al.size()];
    boolean anyMatch = false;
    StringBuffer sbuf = new StringBuffer("[");
    for(int i=0; i < al.size(); i++) {
      Dimension ncDim = (Dimension)al.get(i);
      match[i] = map.hasMatch(type, ncDim);
      if(match[i]) anyMatch = true;
    }

    for(int i=0; i < al.size(); i++) {
      String name = ((Dimension)al.get(i)).getName();
      if(anyMatch) {
        if(match[i]) {
          sbuf.append("<font color=green>" + name + "</font>");
        } else {
          sbuf.append("<font color=black>" + name + "</font>");
        }
      } else {
        sbuf.append("<font color=red>" + name + "</font>");
      }
      if(i < al.size()-1) {
        sbuf.append(",");
      }
    }
    sbuf.append("]");

    return sbuf.toString();
  }

}
