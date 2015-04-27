package ncBrowse;

import java.awt.*;
import javax.swing.*;

/**
 * DialogClients are notified when the Dialog with which they 
 * are associated is dismissed.  A reference to the dismissed 
 * Dialog is passed as a parameter of dialogDismissed() in case 
 * a DialogClient is a client of more than one Dialog.<p>
 */
 
public interface DialogClient {
	// OK Button
    //abstract public void dialogDismissed(Frame d);
  void dialogDismissed(JDialog d);
    
    // Cancel button
    //abstract public void dialogCancelled(Frame d);
    void dialogCancelled(JDialog d);
    
    // something other than the OK button 
    //abstract public void dialogDismissedTwo(Frame d);
    void dialogDismissedTwo(JDialog d);
    
    // Apply button, OK w/o dismissing the dialog
    //abstract public void dialogApply(Frame d);
    void dialogApply(JDialog d);
    
    // Apply button, OK w/o dismissing the dialog
    //abstract public void dialogApplyTwo(Object d);
    void dialogApplyTwo(Object d);
}
