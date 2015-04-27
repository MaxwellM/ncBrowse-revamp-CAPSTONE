/**
 *  $Id: TargetMonitor3D.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import ucar.nc2.Dimension;
import ucar.nc2.Variable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * <pre>
 * Title:        Drag and Drop test application
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      NOAA/PMEL/EPIC
 * </pre>
 * @author Donald Denbo
 * @version $Revision: 1.2 $, $Date: 2003/10/01 22:32:28 $
 */
public class TargetMonitor3D {
  /**
   * @label instance
   */
  static TargetMonitor3D monitor_ = null;

  final JLabel[] title_;
  final JLabel[] item_;
  final JPanel[] panel_;

  static final Color ENABLED = new Color(186, 217, 217);
  static final Color DEFAULT = new Color(102, 102, 153);

  private TargetMonitor3D() {
    title_ = new JLabel[VMapModel.ELEMENT_COUNT];
    item_ = new JLabel[VMapModel.ELEMENT_COUNT];
    panel_ = new JPanel[VMapModel.ELEMENT_COUNT];
  }

  static public TargetMonitor3D getInstance() {
    if(monitor_ == null) {
      monitor_ = new TargetMonitor3D();
    }
    return monitor_;
  }

  public void reset() {
    panel_[VMapModel.U_COMPONENT].setBackground(ENABLED);
    panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
    panel_[VMapModel.W_COMPONENT].setBackground(Color.lightGray);
    //panel_[VMapModel.Z_CONTOUR].setBackground(ENABLED);
    panel_[VMapModel.SURFACE].setBackground(ENABLED);
    panel_[VMapModel.Z_AXIS].setBackground(ENABLED);
    panel_[VMapModel.Z_AXISCOLOR].setBackground(Color.lightGray);
    panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
    item_[VMapModel.Z_AXIS].getDropTarget().setActive(true);
    item_[VMapModel.Z_AXISCOLOR].getDropTarget().setActive(false);
    item_[VMapModel.U_COMPONENT].getDropTarget().setActive(true);
    item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
    item_[VMapModel.W_COMPONENT].getDropTarget().setActive(false);
    //item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(true);
    item_[VMapModel.SURFACE].getDropTarget().setActive(true);
    item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
    for(int i=0; i < VMapModel.ELEMENT_COUNT; i++) {
      if (item_[i] != null)
        item_[i].setText("");
    }
    title_[VMapModel.X_AXIS].setForeground(Color.red);
    title_[VMapModel.Y_AXIS].setForeground(Color.red);
    title_[VMapModel.Z_AXIS].setForeground(DEFAULT);
    title_[VMapModel.U_COMPONENT].setForeground(DEFAULT);
    title_[VMapModel.V_COMPONENT].setForeground(DEFAULT);
    title_[VMapModel.W_COMPONENT].setForeground(DEFAULT);
    panel_[VMapModel.VOLUME].setBackground(ENABLED);
    item_[VMapModel.VOLUME].getDropTarget().setActive(true);
    title_[VMapModel.VOLUME].setForeground(DEFAULT);
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

      // if a non dimension variable then disable other 3D plot types ==> it's a 3D line
      if (!map.isDimensionVariable(VMapModel.X_AXIS) && map.isSet(VMapModel.Y_AXIS)) {
        if (!map.isDimensionVariable(VMapModel.Y_AXIS)) {
        panel_[VMapModel.U_COMPONENT].setBackground(Color.lightGray);
        item_[VMapModel.U_COMPONENT].getDropTarget().setActive(false);
        panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
        item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
        panel_[VMapModel.W_COMPONENT].setBackground(Color.lightGray);
        item_[VMapModel.W_COMPONENT].getDropTarget().setActive(false);
        panel_[VMapModel.Z_AXISCOLOR].setBackground(ENABLED);
        item_[VMapModel.Z_AXISCOLOR].getDropTarget().setActive(true);
        panel_[VMapModel.VOLUME].setBackground(Color.lightGray);
        item_[VMapModel.VOLUME].getDropTarget().setActive(false);
        panel_[VMapModel.SURFACE].setBackground(Color.lightGray);
        item_[VMapModel.SURFACE].getDropTarget().setActive(false);
        panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
        item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
        }
      }
      break;
    case VMapModel.Y_AXIS:
      title_[VMapModel.Y_AXIS].setForeground(DEFAULT);
      if (!map.isDimensionVariable(VMapModel.Y_AXIS) && map.isSet(VMapModel.X_AXIS)) {
        if (!map.isDimensionVariable(VMapModel.X_AXIS)) {
        panel_[VMapModel.U_COMPONENT].setBackground(Color.lightGray);
        item_[VMapModel.U_COMPONENT].getDropTarget().setActive(false);
        panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
        item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
        panel_[VMapModel.W_COMPONENT].setBackground(Color.lightGray);
        item_[VMapModel.W_COMPONENT].getDropTarget().setActive(false);
        panel_[VMapModel.Z_AXISCOLOR].setBackground(ENABLED);
        item_[VMapModel.Z_AXISCOLOR].getDropTarget().setActive(true);
        panel_[VMapModel.VOLUME].setBackground(Color.lightGray);
        item_[VMapModel.VOLUME].getDropTarget().setActive(false);
        panel_[VMapModel.SURFACE].setBackground(Color.lightGray);
        item_[VMapModel.SURFACE].getDropTarget().setActive(false);
        panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
        item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
        }
      }
      break;
    case VMapModel.Z_AXIS:
      if(!map.isSet(VMapModel.V_COMPONENT) && !map.isSet(VMapModel.VOLUME)) {
          if ((map.isSet(VMapModel.X_AXIS) && !map.isDimensionVariable(VMapModel.X_AXIS)) &&
              (map.isSet(VMapModel.Y_AXIS) && !map.isDimensionVariable(VMapModel.Y_AXIS))) {
          panel_[VMapModel.U_COMPONENT].setBackground(Color.lightGray);
          item_[VMapModel.U_COMPONENT].getDropTarget().setActive(false);
          panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
          item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
          panel_[VMapModel.W_COMPONENT].setBackground(Color.lightGray);
          item_[VMapModel.W_COMPONENT].getDropTarget().setActive(false);
          panel_[VMapModel.Z_AXISCOLOR].setBackground(ENABLED);
          item_[VMapModel.Z_AXISCOLOR].getDropTarget().setActive(true);
          panel_[VMapModel.VOLUME].setBackground(Color.lightGray);
          item_[VMapModel.VOLUME].getDropTarget().setActive(false);
      }
    }
      panel_[VMapModel.SURFACE].setBackground(Color.lightGray);
      item_[VMapModel.SURFACE].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
      item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
      //panel_[VMapModel.Z_CONTOUR].setBackground(Color.lightGray);
      //item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(false);
      title_[VMapModel.Z_AXIS].setForeground(DEFAULT);
      break;
    case VMapModel.U_COMPONENT:
      panel_[VMapModel.V_COMPONENT].setBackground(ENABLED);
      item_[VMapModel.V_COMPONENT].getDropTarget().setActive(true);
      panel_[VMapModel.W_COMPONENT].setBackground(ENABLED);
      item_[VMapModel.W_COMPONENT].getDropTarget().setActive(true);
    case VMapModel.V_COMPONENT:
      item_[VMapModel.W_COMPONENT].getDropTarget().setActive(true);
      panel_[VMapModel.W_COMPONENT].setForeground(DEFAULT);

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
      if(map.isSet(VMapModel.W_COMPONENT)) {
        title_[VMapModel.W_COMPONENT].setForeground(DEFAULT);
      }

      if (map.isSet(VMapModel.Z_AXIS))
        title_[VMapModel.W_COMPONENT].setForeground(Color.red);
      //else {
      //  title_[VMapModel.W_COMPONENT].setForeground(Color.red);
      //}
      //panel_[VMapModel.Z_CONTOUR].setBackground(Color.lightGray);
      //item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACE].setBackground(Color.lightGray);
      item_[VMapModel.SURFACE].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
      item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
      panel_[VMapModel.Z_AXISCOLOR].setBackground(Color.lightGray);
      item_[VMapModel.Z_AXISCOLOR].getDropTarget().setActive(false);
      if (!map.isSet(VMapModel.Z_AXIS)) {
        panel_[VMapModel.Z_AXIS].setBackground(Color.lightGray);
        item_[VMapModel.Z_AXIS].getDropTarget().setActive(false);
      }
      panel_[VMapModel.VOLUME].setBackground(Color.lightGray);
      item_[VMapModel.VOLUME].getDropTarget().setActive(false);
      break;
    case VMapModel.W_COMPONENT:
      panel_[VMapModel.Z_AXIS].setBackground(ENABLED);
      if (!map.isSet(VMapModel.Z_AXIS)) {
        item_[VMapModel.Z_AXIS].getDropTarget().setActive(true);
        title_[VMapModel.Z_AXIS].setForeground(Color.red);
      }
      panel_[VMapModel.VOLUME].setBackground(Color.lightGray);
      item_[VMapModel.VOLUME].getDropTarget().setActive(false);
      //panel_[VMapModel.Z_CONTOUR].setBackground(Color.lightGray);
      //item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACE].setBackground(Color.lightGray);
      item_[VMapModel.SURFACE].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
      item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
    title_[VMapModel.W_COMPONENT].setForeground(DEFAULT);
      break;
   /* case VMapModel.Z_CONTOUR:
      panel_[VMapModel.U_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.U_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.W_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.W_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACE].setBackground(Color.lightGray);
      item_[VMapModel.SURFACE].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
      item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
      panel_[VMapModel.Z_AXIS].setBackground(Color.lightGray);
      item_[VMapModel.Z_AXIS].getDropTarget().setActive(false);
      panel_[VMapModel.VOLUME].setBackground(Color.lightGray);
      item_[VMapModel.VOLUME].getDropTarget().setActive(false);
      break;*/
    case VMapModel.SURFACE:
      panel_[VMapModel.U_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.U_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.W_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.W_COMPONENT].getDropTarget().setActive(false);
      //panel_[VMapModel.Z_CONTOUR].setBackground(Color.lightGray);
      //item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACECOLOR].setBackground(ENABLED);
      item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(true);
      panel_[VMapModel.Z_AXISCOLOR].setBackground(Color.lightGray);
      item_[VMapModel.Z_AXISCOLOR].getDropTarget().setActive(false);
      panel_[VMapModel.Z_AXIS].setBackground(Color.lightGray);
      item_[VMapModel.Z_AXIS].getDropTarget().setActive(false);
      panel_[VMapModel.VOLUME].setBackground(Color.lightGray);
      item_[VMapModel.VOLUME].getDropTarget().setActive(false);
      break;
    case VMapModel.VOLUME:
      if (!map.isSet(VMapModel.X_AXIS)) {
        panel_[VMapModel.X_AXIS].setBackground(ENABLED);
        item_[VMapModel.X_AXIS].getDropTarget().setActive(true);
        title_[VMapModel.X_AXIS].setForeground(Color.red);
    }
      if (!map.isSet(VMapModel.Y_AXIS)) {
        panel_[VMapModel.Y_AXIS].setBackground(ENABLED);
      item_[VMapModel.Y_AXIS].getDropTarget().setActive(true);
      title_[VMapModel.Y_AXIS].setForeground(Color.red);
    }
      if (!map.isSet(VMapModel.Z_AXIS)) {
        panel_[VMapModel.Z_AXIS].setBackground(ENABLED);
        item_[VMapModel.Z_AXIS].getDropTarget().setActive(true);
        title_[VMapModel.Z_AXIS].setForeground(Color.red);
      }
      panel_[VMapModel.U_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.U_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.V_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.V_COMPONENT].getDropTarget().setActive(false);
      panel_[VMapModel.W_COMPONENT].setBackground(Color.lightGray);
      item_[VMapModel.W_COMPONENT].getDropTarget().setActive(false);
      //panel_[VMapModel.Z_CONTOUR].setBackground(Color.lightGray);
      //item_[VMapModel.Z_CONTOUR].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACECOLOR].setBackground(ENABLED);
      item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(true);
      panel_[VMapModel.Z_AXISCOLOR].setBackground(Color.lightGray);
      item_[VMapModel.Z_AXISCOLOR].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACE].setBackground(Color.lightGray);
      item_[VMapModel.SURFACE].getDropTarget().setActive(false);
      panel_[VMapModel.SURFACECOLOR].setBackground(Color.lightGray);
      item_[VMapModel.SURFACECOLOR].getDropTarget().setActive(false);
      break;
    }
  }

  public void updateAll(VMapModel map) {
    for(int i=0; i < VMapModel.ELEMENT_COUNT; i++) {
      if(map.isSet(i))
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
    StringBuilder sbuf = new StringBuilder();
    List al = ncVar.getDimensions();
    if((al.size() == 1) && ((Dimension)al.get(0)).getName().equals(ncVar.getName())) {
      sbuf.append("<b>").append(ncVar.getName()).append("</b>   ");
    } else {
      sbuf.append(ncVar.getName()).append("   ");
    }
    sbuf.append(getDimensionList(type, map, ncVar));
    return sbuf.toString();
  }

  String getDimensionLabel(int type, VMapModel map, Dimension ncDim) {
    StringBuilder sbuf = new StringBuilder();
    if(map.hasMatch(type, ncDim)) {
      sbuf.append("<font color=green>");
    } else {
      sbuf.append("<font color=red>");
    }
    sbuf.append("<em>").append(ncDim.getName()).append("</em></font>  ");
    sbuf.append("<em>(").append(ncDim.getLength()).append(" points)</em>");
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
    StringBuilder sbuf = new StringBuilder("[");
    for(int i=0; i < al.size(); i++) {
      Dimension ncDim = (Dimension)al.get(i);
      match[i] = map.hasMatch(type, ncDim);
      if(match[i]) anyMatch = true;
    }

    for(int i=0; i < al.size(); i++) {
      String name = ((Dimension)al.get(i)).getName();
      if(anyMatch) {
        if(match[i]) {
          sbuf.append("<font color=green>").append(name).append("</font>");
        } else {
          sbuf.append(/*"<font color=blue>" + */name /* + "</font>"*/);
        }
      } else {
        sbuf.append("<font color=red>").append(name).append("</font>");
      }
      if(i < al.size()-1) {
        sbuf.append(",");
      }
    }
    sbuf.append("]");

    return sbuf.toString();
  }

}
