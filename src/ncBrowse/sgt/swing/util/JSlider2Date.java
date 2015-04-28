//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ncBrowse.sgt.swing.util;

import gov.noaa.pmel.util.WeakPropertyChangeListener;
import ncBrowse.sgt.geom.GeoDate;
import ncBrowse.sgt.geom.IllegalTimeValue;
import ncBrowse.sgt.geom.TimeRange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;

//import gov.noaa.pmel.swing.JSlider2;
//import gov.noaa.pmel.util.GeoDate;
//import gov.noaa.pmel.util.IllegalTimeValue;
//import gov.noaa.pmel.util.TimeRange;

public class JSlider2Date extends Container implements Serializable {
  boolean twoHandles_;
  TimeRange range_;
  GeoDate minValue_;
  GeoDate oldMinValue_;
  GeoDate maxValue_;
  GeoDate oldMaxValue_;
  double scale_;
  int oldStopMonth_;
  int oldStartMonth_;
  String format_;
  JPanel panel_;
  GridBagLayout layout_;
  JLabel labelStart_;
  JLabel labelStart00_;
  JTextField startDay_;
  JTextField startYear_;
  JTextField startHour_;
  JTextField startMin_;
  JComboBox startMonth_;
  DefaultComboBoxModel startModel_;
  JLabel labelStop_;
  JLabel labelStop00_;
  JTextField stopDay_;
  JTextField stopYear_;
  JTextField stopHour_;
  JTextField stopMin_;
  JComboBox stopMonth_;
  DefaultComboBoxModel stopModel_;
  JSlider2 slider_;
  private PropertyChangeSupport changes;

  public JSlider2Date() {
    this(true);
  }

  public JSlider2Date(boolean var1) {
    this.changes = new PropertyChangeSupport(this);
    this.twoHandles_ = var1;

    try {
      this.jbInit();
    } catch (Exception var3) {
      var3.printStackTrace();
    }

  }

  void jbInit() {
    this.setLayout(new BorderLayout(0, 0));
    this.panel_ = new JPanel();
    this.layout_ = new GridBagLayout();
    this.panel_.setLayout(this.layout_);
    this.panel_.setBounds(0, 0, 368, 177);
    this.slider_ = new JSlider2();
    this.slider_.setBounds(18, 70, 331, 60);
    this.panel_.add(this.slider_, new GridBagConstraints(0, 0, 8, 1, 1.0D, 0.0D, 10, 2, new Insets(10, 5, 5, 5), 0, 0));
    this.labelStart_ = new JLabel("Start:", 4);
    this.panel_.add(this.labelStart_, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(5, 0, 5, 5), 0, 0));
    this.startDay_ = new JTextField();
    this.startDay_.setText("12");
    this.startDay_.setColumns(2);
    this.panel_.add(this.startDay_, new GridBagConstraints(1, 1, 1, 1, 0.5D, 1.0D, 10, 2, new Insets(5, 0, 5, 5), 0, 0));
    this.startMonth_ = new JComboBox();
    this.startMonth_.setFont(new Font("Dialog", 0, 12));
    this.panel_.add(this.startMonth_, new GridBagConstraints(2, 1, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 5, 5), 0, 0));
    this.startYear_ = new JTextField(5);
    this.startYear_.setText("1997");
    this.panel_.add(this.startYear_, new GridBagConstraints(3, 1, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 5, 5), 0, 0));
    this.startHour_ = new JTextField();
    this.startHour_.setColumns(2);
    this.startHour_.setText("10");
    this.panel_.add(this.startHour_, new GridBagConstraints(4, 1, 1, 1, 0.5D, 1.0D, 10, 2, new Insets(5, 5, 5, 0), 0, 0));
    this.labelStart00_ = new JLabel(":");
    this.labelStart00_.setHorizontalAlignment(0);
    this.panel_.add(this.labelStart00_, new GridBagConstraints(5, 1, 1, 1, 0.0D, 1.0D, 10, 0, new Insets(5, 0, 5, 0), 0, 0));
    this.startMin_ = new JTextField();
    this.startMin_.setColumns(2);
    this.startMin_.setText("10");
    this.panel_.add(this.startMin_, new GridBagConstraints(6, 1, 1, 1, 0.5D, 1.0D, 10, 2, new Insets(5, 0, 5, 5), 0, 0));
    this.labelStop_ = new JLabel("Stop:", 4);
    this.stopDay_ = new JTextField(2);
    this.stopDay_.setText("23");
    this.stopMonth_ = new JComboBox();
    this.stopMonth_.setFont(new Font("Dialog", 0, 12));
    this.stopYear_ = new JTextField(5);
    this.stopYear_.setText("1995");
    this.stopHour_ = new JTextField(2);
    this.stopHour_.setText("10");
    this.labelStop00_ = new JLabel(":");
    this.labelStop00_.setHorizontalAlignment(0);
    this.stopMin_ = new JTextField(2);
    this.stopMin_.setText("10");
    if(this.twoHandles_) {
      this.panel_.add(this.labelStop_, new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(5, 0, 10, 5), 0, 0));
      this.panel_.add(this.stopDay_, new GridBagConstraints(1, 2, 1, 1, 0.5D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
      this.panel_.add(this.stopMonth_, new GridBagConstraints(2, 2, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
      this.panel_.add(this.stopYear_, new GridBagConstraints(3, 2, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
      this.panel_.add(this.stopHour_, new GridBagConstraints(4, 2, 1, 1, 0.5D, 1.0D, 10, 2, new Insets(5, 5, 10, 0), 0, 0));
      this.panel_.add(this.labelStop00_, new GridBagConstraints(5, 2, 1, 1, 0.0D, 1.0D, 10, 0, new Insets(5, 0, 10, 0), 0, 0));
      this.panel_.add(this.stopMin_, new GridBagConstraints(6, 2, 1, 1, 0.5D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
    } else {
      this.labelStart_.setText("Date:");
    }

    String[] var1 = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    this.startModel_ = new DefaultComboBoxModel(var1);
    this.stopModel_ = new DefaultComboBoxModel(var1);
    this.startMonth_.setModel(this.startModel_);
    this.stopMonth_.setModel(this.stopModel_);
    this.add("Center", this.panel_);
    JSlider2Date.SymAction var2 = new JSlider2Date.SymAction();
    this.startDay_.addActionListener(var2);
    this.startYear_.addActionListener(var2);
    this.startHour_.addActionListener(var2);
    this.startMin_.addActionListener(var2);
    JSlider2Date.SymPropertyChange var3 = new JSlider2Date.SymPropertyChange();
    this.startMonth_.addActionListener(var2);
    this.slider_.addPropertyChangeListener(new WeakPropertyChangeListener(var3, this.slider_));
    this.stopDay_.addActionListener(var2);
    this.stopMonth_.addActionListener(var2);
    this.stopYear_.addActionListener(var2);
    this.stopHour_.addActionListener(var2);
    this.stopMin_.addActionListener(var2);
    this.slider_.setAlwaysPost(true);
    this.slider_.setDoubleBuffered(true);
    this.format_ = "yyyy-MM-dd";
    GeoDate var4 = new GeoDate();
    var4.now();
    GeoDate var5 = new GeoDate(var4);
    var5.increment(100.0D, 1);
    this.range_ = new TimeRange(var4, var5);
    this.setRange(this.range_);
  }

  public void setRange(GeoDate var1, GeoDate var2) {
    this.setRange(new TimeRange(var1, var2));
  }

  public void setRange(Date var1, Date var2) {
    GeoDate var3 = null;
    GeoDate var4 = null;
    var3 = new GeoDate(var1);
    var4 = new GeoDate(var2);
    this.setRange(new TimeRange(var3, var4));
  }

  public void setRange(TimeRange var1) {
    this.range_ = var1;
    this.scale_ = this.range_.end.offset(this.range_.start);
    this.slider_.setMinLabel(this.range_.start.toString(this.format_));
    this.slider_.setMaxLabel(this.range_.end.toString(this.format_));
    this.minValue_ = this.range_.start;
    this.maxValue_ = this.range_.end;
    this.setStart(false);
    this.setStop(false);
  }

  public TimeRange getRange() {
    return this.range_;
  }

  public void setMinRange(GeoDate var1) {
    this.range_.start = var1;
    this.setRange(this.range_);
  }

  public GeoDate getMinRange() {
    return this.range_.start;
  }

  public void setMaxRange(GeoDate var1) {
    this.range_.end = var1;
    this.setRange(this.range_);
  }

  public GeoDate getMaxRange() {
    return this.range_.end;
  }

  public void reset() {
    this.slider_.reset();
  }

  public void setTwoHandles(boolean var1) {
    this.twoHandles_ = var1;
    this.slider_.setTwoHandles(this.twoHandles_);
    if(this.twoHandles_) {
      if(!this.panel_.isAncestorOf(this.labelStop_)) {
        this.panel_.add(this.labelStop_, new GridBagConstraints(0, 2, 1, 1, 1.0D, 1.0D, 13, 0, new Insets(5, 0, 10, 5), 0, 0));
      }

      if(!this.panel_.isAncestorOf(this.stopDay_)) {
        this.panel_.add(this.stopDay_, new GridBagConstraints(1, 2, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
      }

      if(!this.panel_.isAncestorOf(this.stopMonth_)) {
        this.panel_.add(this.stopMonth_, new GridBagConstraints(2, 2, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
      }

      if(!this.panel_.isAncestorOf(this.stopYear_)) {
        this.panel_.add(this.stopYear_, new GridBagConstraints(3, 2, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
      }

      if(!this.panel_.isAncestorOf(this.stopHour_)) {
        this.panel_.add(this.stopHour_, new GridBagConstraints(4, 2, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 5, 10, 0), 0, 0));
      }

      if(!this.panel_.isAncestorOf(this.labelStop00_)) {
        this.panel_.add(this.labelStop00_, new GridBagConstraints(5, 2, 1, 1, 0.0D, 1.0D, 10, 0, new Insets(5, 0, 10, 0), 0, 0));
      }

      if(!this.panel_.isAncestorOf(this.stopMin_)) {
        this.panel_.add(this.stopMin_, new GridBagConstraints(6, 2, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(5, 0, 10, 5), 0, 0));
      }

      this.labelStart_.setText("Start:");
      this.panel_.invalidate();
    } else {
      this.labelStart_.setText("Date:");
      if(this.panel_.isAncestorOf(this.labelStop_)) {
        this.panel_.remove(this.labelStop_);
      }

      if(this.panel_.isAncestorOf(this.stopDay_)) {
        this.panel_.remove(this.stopDay_);
      }

      if(this.panel_.isAncestorOf(this.stopMonth_)) {
        this.panel_.remove(this.stopMonth_);
      }

      if(this.panel_.isAncestorOf(this.stopYear_)) {
        this.panel_.remove(this.stopYear_);
      }

      if(this.panel_.isAncestorOf(this.stopHour_)) {
        this.panel_.remove(this.stopHour_);
      }

      if(this.panel_.isAncestorOf(this.labelStop00_)) {
        this.panel_.remove(this.labelStop00_);
      }

      if(this.panel_.isAncestorOf(this.stopMin_)) {
        this.panel_.remove(this.stopMin_);
      }

      this.panel_.invalidate();
    }

  }

  public boolean getTwoHandles() {
    return this.twoHandles_;
  }

  public boolean isTwoHandles() {
    return this.twoHandles_;
  }

  public void setMinDate(Date var1) {
    GeoDate var2 = null;
    var2 = new GeoDate(var1);
    this.setMinValue(var2);
  }

  public Date getMinDate() {
    int var1 = Integer.parseInt(this.startDay_.getText());
    int var3 = Integer.parseInt(this.startYear_.getText());
    int var4 = Integer.parseInt(this.startHour_.getText());
    int var2 = this.startMonth_.getSelectedIndex() + 1;
    GeoDate var5 = null;

    try {
      var5 = new GeoDate(var2, var1, var3, var4, 0, 0, 0);
    } catch (IllegalTimeValue var7) {
      System.out.println(var7);
    }

    return var5;
  }

  public void setMaxDate(Date var1) {
    GeoDate var2 = null;
    var2 = new GeoDate(var1);
    this.setMaxValue(var2);
  }

  public Date getMaxDate() {
    int var1 = Integer.parseInt(this.stopDay_.getText());
    int var3 = Integer.parseInt(this.stopYear_.getText());
    int var4 = Integer.parseInt(this.stopHour_.getText());
    int var5 = Integer.parseInt(this.stopMin_.getText());
    int var2 = this.stopMonth_.getSelectedIndex() + 1;
    GeoDate var6 = null;

    try {
      var6 = new GeoDate(var2, var1, var3, var4, var5, 0, 0);
    } catch (IllegalTimeValue var8) {
      System.out.println(var8);
    }

    return var6;
  }

  public GeoDate getStartValue() {
    int var1 = Integer.parseInt(this.startDay_.getText());
    int var3 = Integer.parseInt(this.startYear_.getText());
    int var4 = Integer.parseInt(this.startHour_.getText());
    int var5 = Integer.parseInt(this.startMin_.getText());
    int var2 = this.startMonth_.getSelectedIndex() + 1;

    try {
      this.minValue_ = new GeoDate(var2, var1, var3, var4, var5, 0, 0);
    } catch (IllegalTimeValue var7) {
      ;
    }

    return this.minValue_;
  }

  public GeoDate getEndValue() {
    int var1 = Integer.parseInt(this.stopDay_.getText());
    int var3 = Integer.parseInt(this.stopYear_.getText());
    int var4 = Integer.parseInt(this.stopHour_.getText());
    int var5 = Integer.parseInt(this.stopMin_.getText());
    int var2 = this.stopMonth_.getSelectedIndex() + 1;

    try {
      this.maxValue_ = new GeoDate(var2, var1, var3, var4, var5, 0, 0);
    } catch (IllegalTimeValue var7) {
      ;
    }

    return this.maxValue_;
  }

  public void setStartValue(GeoDate var1) {
    this.minValue_ = var1;
    if(this.minValue_.before(this.range_.start)) {
      this.minValue_ = this.range_.start;
    }

    double var2 = this.minValue_.offset(this.range_.start) / this.scale_;
    this.slider_.setMinValue(var2);
    this.setStart(false);
  }

  public void setEndValue(GeoDate var1) {
    this.maxValue_ = var1;
    if(this.maxValue_.after(this.range_.end)) {
      this.maxValue_ = this.range_.end;
    }

    double var2 = this.maxValue_.offset(this.range_.start) / this.scale_;
    this.slider_.setMaxValue(var2);
    this.setStop(false);
  }

  public void setMinValue(GeoDate var1) {
    this.minValue_ = var1;
    if(this.minValue_.after(this.maxValue_)) {
      this.minValue_ = this.maxValue_;
    }

    double var2 = this.minValue_.offset(this.range_.start) / this.scale_;
    this.slider_.setMinValue(var2);
    this.setStart(false);
  }

  public void setMaxValue(GeoDate var1) {
    this.maxValue_ = var1;
    if(this.maxValue_.before(this.minValue_)) {
      this.maxValue_ = this.minValue_;
    }

    double var2 = this.maxValue_.offset(this.range_.start) / this.scale_;
    this.slider_.setMaxValue(var2);
    this.setStop(false);
  }

  public GeoDate getMinValue() {
    int var1 = Integer.parseInt(this.startDay_.getText());
    int var3 = Integer.parseInt(this.startYear_.getText());
    int var4 = Integer.parseInt(this.startHour_.getText());
    int var5 = Integer.parseInt(this.startMin_.getText());
    int var2 = this.startMonth_.getSelectedIndex() + 1;

    try {
      this.minValue_ = new GeoDate(var2, var1, var3, var4, var5, 0, 0);
    } catch (IllegalTimeValue var7) {
      ;
    }

    if(this.minValue_.after(this.maxValue_)) {
      this.minValue_ = this.maxValue_;
    }

    return this.minValue_;
  }

  public GeoDate getMaxValue() {
    int var1 = Integer.parseInt(this.stopDay_.getText());
    int var3 = Integer.parseInt(this.stopYear_.getText());
    int var4 = Integer.parseInt(this.stopHour_.getText());
    int var5 = Integer.parseInt(this.stopMin_.getText());
    int var2 = this.stopMonth_.getSelectedIndex() + 1;

    try {
      this.maxValue_ = new GeoDate(var2, var1, var3, var4, var5, 0, 0);
    } catch (IllegalTimeValue var7) {
      ;
    }

    if(this.maxValue_.before(this.minValue_)) {
      this.maxValue_ = this.minValue_;
    }

    return this.maxValue_;
  }

  public void setFormat(String var1) {
    this.format_ = var1;
    this.slider_.setMinLabel(this.range_.start.toString(this.format_));
    this.slider_.setMaxLabel(this.range_.end.toString(this.format_));
  }

  public String getFormat() {
    return this.format_;
  }

  public void setShowBorder(boolean var1) {
    this.slider_.setShowBorder(var1);
  }

  public boolean getShowBorder() {
    return this.slider_.getShowBorder();
  }

  public void setHandleSize(int var1) {
    this.slider_.setHandleSize(var1);
  }

  public int getHandleSize() {
    return this.slider_.getHandleSize();
  }

  public void setAlwaysPost(boolean var1) {
    this.slider_.setAlwaysPost(var1);
  }

  public boolean getAlwaysPost() {
    return this.slider_.getAlwaysPost();
  }

  public void addPropertyChangeListener(PropertyChangeListener var1) {
    this.changes.addPropertyChangeListener(var1);
  }

  public void removePropertyChangeListener(PropertyChangeListener var1) {
    this.changes.removePropertyChangeListener(var1);
  }

  public Dimension getMinimumSize() {
    short var1;
    if(this.twoHandles_) {
      var1 = 144;
    } else {
      var1 = 105;
    }

    return new Dimension(375, var1);
  }

  public Dimension getPreferredSize() {
    short var1;
    if(this.twoHandles_) {
      var1 = 144;
    } else {
      var1 = 105;
    }

    return new Dimension(375, var1);
  }

  public Dimension getMaximumSize() {
    return new Dimension(32767, 32767);
  }

  public void setSize(Dimension var1) {
    super.setSize(var1);
    this.validate();
  }

  public void setSize(int var1, int var2) {
    super.setSize(var1, var2);
    this.validate();
  }

  void startMonth_ActionEvent(ActionEvent var1) {
    if(this.oldStartMonth_ != this.startMonth_.getSelectedIndex() + 1) {
      this.minValue_EnterHit();
    }

  }

  void stopMonth_ActionEvent(ActionEvent var1) {
    if(this.oldStopMonth_ != this.stopMonth_.getSelectedIndex() + 1) {
      this.maxValue_EnterHit();
    }

  }

  void minValue_EnterHit() {
    int var1 = Integer.parseInt(this.startDay_.getText());
    int var3 = Integer.parseInt(this.startYear_.getText());
    int var4 = Integer.parseInt(this.startHour_.getText());
    int var5 = Integer.parseInt(this.startMin_.getText());
    int var2 = this.startMonth_.getSelectedIndex() + 1;
    this.oldStartMonth_ = var2;

    try {
      this.minValue_ = new GeoDate(var2, var1, var3, var4, var5, 0, 0);
    } catch (IllegalTimeValue var9) {
      ;
    }

    if(this.minValue_.after(this.maxValue_)) {
      this.minValue_ = this.maxValue_;
    }

    double var6 = this.minValue_.offset(this.range_.start) / this.scale_;
    if(var6 < 0.0D) {
      var6 = 0.0D;
      this.minValue_ = new GeoDate(this.range_.start);
      this.minValue_.increment(var6 * this.scale_, 1);
      this.setStart(false);
    }

    this.slider_.setMinValue(var6);
  }

  void maxValue_EnterHit() {
    int var1 = Integer.parseInt(this.stopDay_.getText());
    int var3 = Integer.parseInt(this.stopYear_.getText());
    int var4 = Integer.parseInt(this.stopHour_.getText());
    int var5 = Integer.parseInt(this.stopMin_.getText());
    int var2 = this.stopMonth_.getSelectedIndex() + 1;
    this.oldStopMonth_ = var2;

    try {
      this.maxValue_ = new GeoDate(var2, var1, var3, var4, var5, 0, 0);
    } catch (IllegalTimeValue var9) {
      ;
    }

    if(this.maxValue_.before(this.minValue_)) {
      this.maxValue_ = this.minValue_;
    }

    double var6 = this.maxValue_.offset(this.range_.start) / this.scale_;
    if(var6 > 1.0D) {
      var6 = 1.0D;
      this.maxValue_ = new GeoDate(this.range_.start);
      this.maxValue_.increment(var6 * this.scale_, 1);
      this.setStop(false);
    }

    this.slider_.setMaxValue(var6);
  }

  void slider_propertyChange(PropertyChangeEvent var1) {
    double var2;
    if(var1.getPropertyName().equals("minValue")) {
      var2 = ((Double)var1.getNewValue()).doubleValue();
      this.minValue_ = new GeoDate(this.range_.start);
      this.minValue_.increment(var2 * this.scale_, 1);
      this.setStart(true);
    } else if(var1.getPropertyName().equals("maxValue")) {
      var2 = ((Double)var1.getNewValue()).doubleValue();
      this.maxValue_ = new GeoDate(this.range_.start);
      this.maxValue_.increment(var2 * this.scale_, 1);
      this.setStop(true);
    }

  }

  void setStart(boolean var1) {
    this.startDay_.setText(this.minValue_.toString("dd"));
    this.oldStartMonth_ = this.minValue_.getGMTMonth();
    this.startMonth_.setSelectedIndex(this.oldStartMonth_ - 1);
    this.startYear_.setText(this.minValue_.toString("yyyy"));
    this.startHour_.setText(this.minValue_.toString("HH"));
    this.startMin_.setText(this.minValue_.toString("mm"));
    if(var1) {
      this.changes.firePropertyChange("minValue", this.oldMinValue_, this.minValue_);
      this.oldMinValue_ = this.minValue_;
    }

  }

  void setStop(boolean var1) {
    this.stopDay_.setText(this.maxValue_.toString("dd"));
    this.oldStopMonth_ = this.maxValue_.getGMTMonth();
    this.stopMonth_.setSelectedIndex(this.oldStopMonth_ - 1);
    this.stopYear_.setText(this.maxValue_.toString("yyyy"));
    this.stopHour_.setText(this.maxValue_.toString("HH"));
    this.stopMin_.setText(this.maxValue_.toString("mm"));
    if(var1) {
      this.changes.firePropertyChange("maxValue", this.oldMaxValue_, this.maxValue_);
      this.oldMaxValue_ = this.maxValue_;
    }

  }

  public static void main(String[] var0) {
    String var1 = "yyyy-MM-dd HH:mm";
    JSlider2Date var6 = new JSlider2Date();

    try {
      GeoDate var2 = new GeoDate("1999-02-15 00:00", var1);
      GeoDate var3 = new GeoDate("2001-05-01 00:00", var1);
      GeoDate var4 = new GeoDate("1999-10-12 12:00", var1);
      GeoDate var5 = new GeoDate("2000-04-01 13:39", var1);
      var6.setRange(new TimeRange(var2, var3));
      var6.setStartValue(var4);
      var6.setEndValue(var5);
      var6.setAlwaysPost(true);
    } catch (IllegalTimeValue var8) {
      ;
    }

    JFrame var7 = new JFrame("JSlider2Date Test");
    var7.setSize(500, 300);
    var7.getContentPane().setLayout(new FlowLayout());
    var7.getContentPane().add(var6);
    var7.setVisible(true);
  }

  class SymPropertyChange implements PropertyChangeListener {
    SymPropertyChange() {
    }

    public void propertyChange(PropertyChangeEvent var1) {
      Object var2 = var1.getSource();
      if(var2 == JSlider2Date.this.slider_) {
        JSlider2Date.this.slider_propertyChange(var1);
      }

    }
  }

  class SymAction implements ActionListener {
    SymAction() {
    }

    public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if(var2 != JSlider2Date.this.startDay_ && var2 != JSlider2Date.this.startYear_ && var2 != JSlider2Date.this.startHour_ && var2 != JSlider2Date.this.startMin_) {
        if(var2 != JSlider2Date.this.stopDay_ && var2 != JSlider2Date.this.stopYear_ && var2 != JSlider2Date.this.stopHour_ && var2 != JSlider2Date.this.stopMin_) {
          if(var2 == JSlider2Date.this.startMonth_) {
            JSlider2Date.this.startMonth_ActionEvent(var1);
          } else if(var2 == JSlider2Date.this.stopMonth_) {
            JSlider2Date.this.stopMonth_ActionEvent(var1);
          }
        } else {
          JSlider2Date.this.maxValue_EnterHit();
        }
      } else {
        JSlider2Date.this.minValue_EnterHit();
      }

    }
  }
}
