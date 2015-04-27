/**
 *  $Id: WindowList.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import java.awt.Window;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JCheckBoxMenuItem;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Maintains a list of JFrames and JDialogs associated with
 * a Browser.
 *
 * @author Donald Denbo
 * @version $Revision: 1.3 $, $Date: 2004/05/14 21:19:31 $
 */
public class WindowList extends Vector<Window> implements WindowListener {
  Browser browser_ = null;
  Vector<JCheckBoxMenuItem> items_ = new Vector<>();

  public WindowList() {
    super();
  }

  public WindowList(int size) {
    super(size);
  }

  public WindowList(int size, int increment) {
    super(size, increment);
  }

  public void setParent(Browser parent) {
    browser_ = parent;
  }

  public void addElement(JDialog dialog) {
    super.addElement(dialog);
    JCheckBoxMenuItem cb = new JCheckBoxMenuItem(size() + " " +
						 dialog.getTitle());
    cb.setState(dialog.isShowing());
    cb.addActionListener(browser_.getCheckBoxAction());
    items_.addElement(cb);
  }

  public void addElement(JFrame frame) {
    super.addElement(frame);
    JCheckBoxMenuItem cb = new JCheckBoxMenuItem(size() + " " +
						 frame.getTitle());
    cb.setState(frame.isShowing());
    cb.addActionListener(browser_.getCheckBoxAction());
    items_.addElement(cb);
  }

  public Object[] getMenuItems() {
    return items_.toArray();
  }

  public void closeAll() {
    Window win = null;
    for(Enumeration<Window> e = elements(); e.hasMoreElements();) {
      win = e.nextElement();
      win.setVisible(false);
      win.dispose();
    }
  }
  /*
   * WindowListener methods
   */

  public void windowActivated(WindowEvent e) {
  }

  public void windowClosed(WindowEvent e) {
    Window win = e.getWindow();
    windowMessage(e, "closed");
    /*
     * remove from lists
     */
    int index = indexOf(win);
    if(index == -1) return;   // window not found
    items_.remove(index);
    remove(index);
  }

  public void windowClosing(WindowEvent e) {
//    Window win = e.getWindow();
    windowMessage(e, "closing");
  }

  public void windowDeactivated(WindowEvent e) {
//    Window win = e.getWindow();
    windowMessage(e, "deactivated");
  }

  public void windowDeiconified(WindowEvent e) {
//    Window win = e.getWindow();
    windowMessage(e, "deiconified");
  }

  public void windowIconified(WindowEvent e) {
//    Window win = e.getWindow();
    windowMessage(e, "iconified");
  }

  public void windowOpened(WindowEvent e) {
//    Window win = e.getWindow();
    windowMessage(e, "opened");
  }

  private void windowMessage(WindowEvent e, String message) {
    String title = null;
    Window win = e.getWindow();
    if(win instanceof JFrame) {
      title = ((JFrame)win).getTitle();
    } else if(win instanceof JDialog) {
      title = ((JDialog)win).getTitle();
    }
    if(Debug.DEBUG) System.out.println("Window " + title + " " + message);
  }

}
