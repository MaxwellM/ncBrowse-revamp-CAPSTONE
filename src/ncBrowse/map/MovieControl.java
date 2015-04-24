/**
 *  $Id: MovieControl.java 15 2013-04-24 19:16:08Z dwd $
 */
package ncBrowse.map;

import gov.noaa.pmel.sgt.JPane;
import ncBrowse.Browser;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * @author Donald Denbo
 * @version 1.0
 */

public class MovieControl extends JFrame implements ActionListener {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonPanel = new JPanel();
  JButton doneButton = new JButton();
  JPanel controlsPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel titlePanel = new JPanel();
  JLabel titleLabel = new JLabel();
  JLabel paramLabel = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel jPanel1 = new JPanel();
  JButton stepForwardButton = new JButton();
  JButton runStopButton = new JButton();
  JButton stepBackButton = new JButton();
  JButton rewindButton = new JButton();
  JPanel speedPanel = new JPanel();
  JSlider speedSlider = new JSlider();

  ImageIcon rewindIcon_;
  ImageIcon stepBackIcon_;
  ImageIcon runIcon_;
  ImageIcon stopIcon_;
  ImageIcon stepForwardIcon_;

  protected ChangeEvent changeEvent = null;
  protected EventListenerList listenerList = new EventListenerList();

  static Collection<MovieControl> movies = new ArrayList<MovieControl>();

  boolean isStopped_ = true;
  Timer timer_ = null;
  private static MovieControl instance_ = null;
  VMapParameter vParam_;
  JPane pane_ = null;
  int paramIndex_ = 0;
  int paramLen_ = 0;

  static int SLIDER_MAX = 5000;
  static int SLIDER_MIN = 100;

  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JPanel framePanel = new JPanel();
  JSlider frameSlider = new JSlider();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  Border border3;
  TitledBorder titledBorder2;
  boolean play_;

  public static MovieControl getInstance() {
//    if(instance_ == null) {
    instance_ = new MovieControl();
    movies.add(instance_);
//    }

    return instance_;
  }

  public MovieControl() {
    timer_ = new Timer(1000, this);
    play_ = false;
    Class bClass = Browser.getInstance().getClass();
    rewindIcon_ = new ImageIcon(bClass.getResource("images/Rewind24.gif"));
    stepBackIcon_ = new ImageIcon(bClass.getResource("images/StepBack24.gif"));
    runIcon_ = new ImageIcon(bClass.getResource("images/Play24.gif"));
    stopIcon_ = new ImageIcon(bClass.getResource("images/Stop24.gif"));
    stepForwardIcon_ = new ImageIcon(bClass.getResource("images/StepForward24.gif"));
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        isStopped_ = true;
        runStopButton.setIcon(runIcon_);
        timer_.stop();
      }

      public void windowOpened(WindowEvent event) {
      }
    });
    pack();
  }

  private void jbInit() throws Exception {
    //TESTING
    SliderListener sl = new SliderListener(this);

    border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(142, 142, 142));
    titledBorder1 = new TitledBorder(border1, "Current Frame");
    border2 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)), "Current Frame"), BorderFactory.createEmptyBorder(5, 5, 10, 5));
    border3 = BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142));
    titledBorder2 = new TitledBorder(border3, "Speed Control");
    this.getContentPane().setLayout(borderLayout1);
    doneButton.setText("Done");
    doneButton.addActionListener(new MovieControl_doneButton_actionAdapter(this));
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    controlsPanel.setLayout(borderLayout2);
    titlePanel.setLayout(gridBagLayout1);
    titleLabel.setText("Animation Controller for");
    paramLabel.setText("cLimlien");
    titlePanel.setBorder(BorderFactory.createEtchedBorder());
    jLabel1.setText("slower");
    jLabel2.setToolTipText("");
    jLabel2.setText("faster");
    rewindButton.setToolTipText("rewind");
    rewindButton.addActionListener(new MovieControl_rewindButton_actionAdapter(this));
    stepBackButton.setToolTipText("step backward");
    stepBackButton.addActionListener(new MovieControl_stepBackButton_actionAdapter(this));
    runStopButton.setToolTipText("run - stop toggle");
    runStopButton.addActionListener(new MovieControl_runStopButton_actionAdapter(this));
    stepForwardButton.setToolTipText("step forward");
    stepForwardButton.addActionListener(new MovieControl_stepForwardButton_actionAdapter(this));
    speedSlider.setMinimum(SLIDER_MIN);
    speedSlider.setMaximum(SLIDER_MAX);
    speedSlider.setValue(1000);
    speedSlider.setToolTipText("adjust animation speed");
    speedSlider.addChangeListener(new MovieControl_speedSlider_changeAdapter(this));
    speedSlider.addInputMethodListener(new MovieControl_speedSlider_inputMethodAdapter(this));
    frameSlider.addChangeListener(new MovieControl_frameSlider_changeAdapter(this));

    //TESTING
    speedSlider.addChangeListener(sl);

    framePanel.setLayout(gridBagLayout2);
    frameSlider.setValue(0);
    frameSlider.setBorder(border2);
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(doneButton, null);
    this.getContentPane().add(controlsPanel, BorderLayout.CENTER);
    controlsPanel.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(rewindButton, null);
    jPanel1.add(stepBackButton, null);
    jPanel1.add(runStopButton, null);
    jPanel1.add(stepForwardButton, null);
    controlsPanel.add(speedPanel, BorderLayout.SOUTH);
    speedPanel.add(jLabel1, null);
    speedPanel.add(speedSlider, null);
    speedPanel.add(jLabel2, null);
    controlsPanel.add(framePanel, BorderLayout.NORTH);
    framePanel.add(frameSlider, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 10, 5), 0, 0));
    this.getContentPane().add(titlePanel, BorderLayout.NORTH);
    titlePanel.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
    titlePanel.add(paramLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
//
    rewindButton.setIcon(rewindIcon_);
    stepBackButton.setIcon(stepBackIcon_);
    runStopButton.setIcon(runIcon_);
    stepForwardButton.setIcon(stepForwardIcon_);
  }

  public void setJPane(JPane pane) {
    pane_ = pane;
  }

  public static long getPID() {
    String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
    return Long.parseLong(processName.split("@")[0]);
  }

  public void setMapParameter(VMapParameter param) {
    vParam_ = param;
    paramIndex_ = 0;
    paramLen_ = vParam_.getLength();
    paramLabel.setText(vParam_.getName() + " (" + vParam_.getUnits() + ")");
    //
    frameSlider.setMinimum(0);
    frameSlider.setMaximum(paramLen_ - 1);
    frameSlider.setValue(paramIndex_);
  }

  void doneButton_actionPerformed(ActionEvent e) {
    isStopped_ = true;
    runStopButton.setIcon(runIcon_);
    timer_.stop();
    setVisible(false);
  }

  void rewindButton_actionPerformed(ActionEvent e) {
    //WORKING!
//    for (MovieControl instance_ : movies) {
//      instance_.paramIndex_ = 0;
//      //paramIndex_ = 0;
//      setValueIndex(instance_.paramIndex_);
//      instance_.frameSlider.setValue(instance_.paramIndex_);
//    }
    paramIndex_ = 0;
    setValueIndex(paramIndex_);
    frameSlider.setValue(paramIndex_);
  }

  void stepBackButton_actionPerformed(ActionEvent e) {
    //TESTING
//    for (MovieControl instance_ : movies) {
//      instance_.paramIndex_--;
//      if (instance_.paramIndex_ < 0) {
//        instance_.paramIndex_ = instance_.paramLen_ - 1;
//      }
//      instance_.vParam_.setValueIndex(instance_.paramIndex_);
//      instance_.frameSlider.setValue(instance_.paramIndex_);
//    }
    paramIndex_--;
    if(paramIndex_ < 0) paramIndex_ = paramLen_ - 1;
    vParam_.setValueIndex(paramIndex_);
    frameSlider.setValue(paramIndex_);
  }

//  void syncMovies(ActionEvent e){
//    for(MovieControl instance_ : movies ){
//      instance_.paramIndex_ = 0;
//    }
//  }
//  void syncMovies2(InputMethodEvent e){
//    for(MovieControl instance_ : movies ){
//      instance_.paramIndex_ = 0;
//    }
//  }
//  void syncMovies3(ChangeEvent e){
//    for (MovieControl instance_ : movies) {
//      //instance_.paramIndex_ = 0;
//      instance_.frameSlider_stateChanged(e);
//    }
//  }

  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }
//
//  protected void fireStateChanged() {
//    Object[] listeners = listenerList.getListenerList();
//    for (int i = listeners.length - 2; i >= 0; i -= 2) {
//      if (listeners[i] == ChangeListener.class) {
//        if (changeEvent == null) {
//          changeEvent = new ChangeEvent(this);
//        }
//        ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
//      }
//    }
//  }


  void runStopButton_actionPerformed(ActionEvent e) {
    //WORKING!
//    for (MovieControl instance_ : movies) {
//      if (instance_.isStopped_) {
//        instance_.isStopped_ = false;
//        instance_.runStopButton.setIcon(stopIcon_);
//        instance_.timer_.setDelay(SLIDER_MAX - speedSlider.getValue() + SLIDER_MIN);
//        timer_.start();
//      } else {
//        instance_.isStopped_ = true;
//        instance_.runStopButton.setIcon(runIcon_);
//        timer_.stop();
//      }
//    }
    if(isStopped_) {
      for (MovieControl instance_ : movies) {
        instance_.isStopped_ = false;
        instance_.runStopButton.setIcon(stopIcon_);
        instance_.timer_.setDelay(SLIDER_MAX - speedSlider.getValue() + SLIDER_MIN);
        //instance_.timer_.start();
      }
      timer_.start();
    } else {
      for (MovieControl instance_ : movies) {
        instance_.isStopped_ = true;
        instance_.runStopButton.setIcon(runIcon_);
        //instance_.timer_.stop();
      }
      timer_.stop();
    }
  }

  /**
   * Listen for Swing timer
   *
   * @param event to be performed
   */
  public void actionPerformed(ActionEvent event) {
    //WORKING!
//    for (MovieControl instance_ : movies) {
    if (!isStopped_) {
      paramIndex_++;
      if (paramIndex_ >= paramLen_) {
        paramIndex_ = 0;
      }
      setValueIndex(paramIndex_);
      frameSlider.setValue(paramIndex_);
    }
  }
//  }

  void stepForwardButton_actionPerformed(ActionEvent e) {
    //TESTING
//    for (MovieControl instance_ : movies) {
//      paramIndex_++;
//      if (instance_.paramIndex_ >= instance_.paramLen_) {
//        instance_.paramIndex_ = 0;
//      }
//      setValueIndex(instance_.paramIndex_);
//      instance_.frameSlider.setValue(instance_.paramIndex_);
//    }
    paramIndex_++;
    if(paramIndex_ >= paramLen_) paramIndex_ = 0;
    setValueIndex(paramIndex_);
    frameSlider.setValue(paramIndex_);
  }

  void speedSlider_caretPositionChanged(InputMethodEvent e) {
    //TESTING
//    if(speedSlider.getModel().getValueIsAdjusting()) {
//      System.out.println("No, speedSlider_stateChanged");
//      return;
//    }
//    System.out.println("slider(caretPosition) value = " + speedSlider.getValue());
    timer_.setDelay(SLIDER_MAX - speedSlider.getValue() + SLIDER_MIN);
  }

  int getSpeedSliderValue() {
    return speedSlider.getValue();
  }


  void speedSlider_stateChanged(ChangeEvent e) {
    //TESTING
    //int newvalue = instance_.speedSlider.getValue();
    for (MovieControl instance_ : movies) {
//      if (instance_.speedSlider.getModel().getValueIsAdjusting()) {
//        System.out.println("No, speedSlider_stateChanged");
//        return;
//      }
//      System.out.println("slider(stateChanged) value = " + instance_.speedSlider.getValue());
      instance_.timer_.setDelay(SLIDER_MAX - speedSlider.getValue() + SLIDER_MIN);
      instance_.speedSlider.setValue(speedSlider.getValue());
    }
  }

  void setValueIndex(int index) {
    if (pane_ != null) {
      pane_.setBatch(true);
    }
    vParam_.setValueIndex(index);
    if (pane_ != null) {
      pane_.setBatch(false);
    }
  }

  void frameSlider_stateChanged(ChangeEvent e) {
    //TESTING
    for (MovieControl instance_ : movies) {
//    if (frameSlider.getModel().getValueIsAdjusting()) {
//      return;
//    }
      int index = frameSlider.getValue();
      if (index != paramIndex_) {
        paramIndex_ = index;
        setValueIndex(paramIndex_);
      }
      instance_.frameSlider.setValue(frameSlider.getValue());
    }
  }
}

class SliderListener implements ChangeListener {
  MovieControl adaptee;

  SliderListener(MovieControl adaptee){
    this.adaptee = adaptee;
  }

  public void stateChanged(ChangeEvent e)
  {
    JSlider source = (JSlider)e.getSource();
  }
}


class MovieControl_doneButton_actionAdapter implements ActionListener {
  MovieControl adaptee;

  MovieControl_doneButton_actionAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    //adaptee.syncMovies(e);
    adaptee.doneButton_actionPerformed(e);
    //adaptee.syncMovies(e);
  }
}

class MovieControl_rewindButton_actionAdapter implements ActionListener {
  MovieControl adaptee;

  MovieControl_rewindButton_actionAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    //adaptee.syncMovies(e);
    adaptee.rewindButton_actionPerformed(e);
    //adaptee.syncMovies(e);
  }
}

class MovieControl_stepBackButton_actionAdapter implements ActionListener {
  MovieControl adaptee;

  MovieControl_stepBackButton_actionAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.stepBackButton_actionPerformed(e);
    //adaptee.syncMovies(e);
  }
}

class MovieControl_runStopButton_actionAdapter implements ActionListener {
  MovieControl adaptee;

  MovieControl_runStopButton_actionAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.runStopButton_actionPerformed(e);
    //adaptee.syncMovies(e);
  }
}

class MovieControl_stepForwardButton_actionAdapter implements ActionListener {
  MovieControl adaptee;

  MovieControl_stepForwardButton_actionAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.stepForwardButton_actionPerformed(e);
    //adaptee.syncMovies(e);
  }
}

class MovieControl_speedSlider_inputMethodAdapter implements InputMethodListener {
  MovieControl adaptee;

  MovieControl_speedSlider_inputMethodAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
  }
  public void inputMethodTextChanged(InputMethodEvent e) {
  }
  public void caretPositionChanged(InputMethodEvent e) {
    adaptee.speedSlider_caretPositionChanged(e);
    //adaptee.syncMovies2(e);
  }
}

class MovieControl_speedSlider_changeAdapter implements ChangeListener {
  MovieControl adaptee;

  MovieControl_speedSlider_changeAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
    //TESTING
    adaptee.addChangeListener(this);
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.speedSlider_stateChanged(e);
    //adaptee.fireStateChanged();
  }
}

class MovieControl_frameSlider_changeAdapter implements ChangeListener {
  MovieControl adaptee;

  MovieControl_frameSlider_changeAdapter(MovieControl adaptee) {
    this.adaptee = adaptee;
    //TESTING
    adaptee.addChangeListener(this);
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.frameSlider_stateChanged(e);
    //adaptee.syncMovies3(e);
  }
}
