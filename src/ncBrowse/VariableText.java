/**
 *  $Id: VariableText.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse;

import gov.noaa.pmel.sgt.dm.SGTData;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

//  import ucar.netcdf.Variable;

/**
 * Presents <code>String</code> variable data in a window.
 *
 * @author Donald Denbo
 * @version $Revision: 1.9 $, $Date: 2002/04/23 22:37:55 $
 */
public class VariableText extends VariableProcessThread {
  private MyAction myAction_;
  private SGTData subset_;
  private JFrame display_;
  private JButton closeButton_;
  private String text_;
  private int x_ = 0;
  private int y_ = 50;

  /** @link dependency
   * @stereotype access*/
  /*#SelectionRange lnkSelectionRange;*/

  public VariableText(Browser parent, String title) {
    super(parent, title);
  }

  private String getVariableText(SelectionRange range) {
    int[] origin = range.getOrigin();
    int[] shape = range.getShape();
    int rank = origin.length;
    int[] maOrigin = new int[rank];
    int[] maShape = new int[rank];
    int[] finish = new int[rank];
    int[] area = new int[rank];
    int lastIndex = 0;
    int firstIndex = 0;
    int count = 1;
    int outRank = 0;
    int[] index;
    boolean first = true;
    char[] array = null;
    StringBuffer sbuf = new StringBuffer();

    Variable ncVar = ncFile_.findVariable(variable_);

    for(int i=0; i < rank; i++) {
      finish[i] = origin[i] + shape[i] - 1;
      maOrigin[i] = origin[i];
      maShape[i] = shape[i];
      if(shape[i] != 1) {
        lastIndex = i;
        outRank = outRank + 1;
      }
      if(shape[i] != 1 && first) {
        first = false;
        firstIndex = i;
      }
    }
    for(int i=0; i < lastIndex; i++) {
      count = count * shape[i];
      maShape[i] = 1;
    }
    if(outRank <= 1) {
      try {
        //	array = (char[])ncVar.toArray(array, maOrigin, maShape);
        array = (char[])ncVar.read(maOrigin, maShape).copyTo1DJavaArray();
      } catch (IOException e) {
          String s = e.toString();
          System.out.println(s);
      } catch (InvalidRangeException e) {
        e.printStackTrace();
      }
      sbuf.append(array, 0, lastNonNull(array));
    } else if(outRank == 2) {
      maShape[firstIndex] = 1;
      for(int i=0; i < shape[firstIndex]; i++) {
        maOrigin[firstIndex] = origin[firstIndex] + i;
        try {
          //	  array = (char[])ncVar.toArray(array, maOrigin, maShape);
        array = (char[])ncVar.read(maOrigin, maShape).copyTo1DJavaArray();
        } catch (IOException e) {
            String s = e.toString();
            System.out.println(s);
        } catch (InvalidRangeException e) {
          e.printStackTrace();
        }
        sbuf.append(array, 0, lastNonNull(array));
        sbuf.append('\n');
      }
    } else {
      //
      // higher order extraction
      //
      index = new int[rank];
      area[lastIndex] = 0;
      area[lastIndex-1] = shape[lastIndex-2];
      for(int i=lastIndex-2; i >= 0; i--) {
        area[i] = area[i-1]*shape[i-1];
        if(Debug.DEBUG) {
          System.out.println("area]" + i + "] = " + area[i]);
        }
      }
      for(int i=0; i < lastIndex; i++) {
        maShape[i] = 1;
      }
      int residual;
      for(int i=0; i < count; i++) {
        residual = i;
        for(int j=0; j < lastIndex; j++) {
          index[j] = residual/area[j];
          residual = residual - index[j]*area[j];
        }
        for(int j=0; j < lastIndex; j++) {
          maOrigin[j] = origin[j] + index[j];
        }
        try {
          //	  array = (char[])ncVar.toArray(array, maOrigin, maShape);
        array = (char[])ncVar.read(maOrigin, maShape).copyTo1DJavaArray();
        } catch (IOException e) {
            String s = e.toString();
            System.out.println(s);
        } catch (InvalidRangeException e) {
          e.printStackTrace();
        }
        sbuf.append(array, 0, lastNonNull(array));
        sbuf.append('\n');
      }
    }

    if(Debug.DEBUG) System.out.println(" firstIndex, lastIndex, count = " +
                                       firstIndex + ", " +
                                       lastIndex + ", " +
                                       count);
    return sbuf.toString();
  }

  private int lastNonNull(char[] array) {
    for(int i=0; i < array.length; i++) {
      if(array[i] == 0) return i;
    }
    return array.length;
  }

  public void run() {
    String lname = " ";
    if(Debug.DEBUG) System.out.println("Running VariableText! for " + variable_);

    text_ = getVariableText(range_);

    makeFrame();
    display_.setSize(400,200);
    display_.setLocation(x_, y_);
    display_.setVisible(true);
  }

  public void setLocation(int x, int y) {
    x_ = x;
    y_ = y;
  }

  private void makeFrame() {
    String name = ncFile_.getFileName();
    display_ = new JFrame("Textual Data from " + name);
    display_.getContentPane().setLayout(new BorderLayout(0,0));
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,25,5));
    display_.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    closeButton_ = new JButton("Close");
    buttonPanel.add(closeButton_);
    JScrollPane textsp = new JScrollPane();
    display_.getContentPane().add(textsp, BorderLayout.CENTER);
    JTextArea texta = new JTextArea(text_);
    textsp.getViewport().add(texta);
    //
    //
    myAction_ = new MyAction();
    SymWindow aSymWindow = new SymWindow();
    display_.addWindowListener(aSymWindow);
    closeButton_.addActionListener(myAction_);
    parent_.getWindowList().addElement(display_);
    display_.addWindowListener(parent_.getWindowList());
    //
  }

  class SymWindow extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
      Object object = event.getSource();
      if (object == display_)
        display_WindowClosing(event);
    }
  }

  void display_WindowClosing(WindowEvent event) {
    display_.setVisible(false);
    display_.dispose();		 // dispose of the Frame.
  }
  class MyAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      Object object = event.getSource();
      if (object == closeButton_)
        closeButton_actionPerformed(event);
    }
  }

  void closeButton_actionPerformed(ActionEvent event) {
    try {
      display_.setVisible(false);
      display_.dispose();
    } catch (Exception e) {
    }
  }

}
