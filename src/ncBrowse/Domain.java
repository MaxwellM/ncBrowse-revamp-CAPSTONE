//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ncBrowse;

import ncBrowse.sgt.geom.Range2D;
//import gov.noaa.pmel.util.Range2D;
import ncBrowse.sgt.geom.SoTRange;
//import gov.noaa.pmel.util.SoTRange;

import java.io.Serializable;

public class Domain implements Serializable {
  Range2D xRange_ = null;
  Range2D yRange_ = null;
  SoTRange tRange_ = null;
  boolean xTime_ = false;
  boolean yTime_ = false;
  boolean xReversed_ = false;
  boolean yReversed_ = false;

  public Domain() {
  }

  public Domain(Domain var1) {
    if(var1.isXTime()) {
      this.tRange_ = var1.getTimeRange();
    } else {
      this.xRange_ = var1.getXRange();
    }

    if(var1.isYTime()) {
      this.tRange_ = var1.getTimeRange();
    } else {
      this.yRange_ = var1.getYRange();
    }

    this.xReversed_ = var1.isXReversed();
    this.yReversed_ = var1.isYReversed();
  }

  public Domain(Range2D var1, Range2D var2) {
    this.xRange_ = var1;
    this.yRange_ = var2;
  }

  public Domain(SoTRange var1, Range2D var2) {
    this.tRange_ = var1;
    this.yRange_ = var2;
    this.xTime_ = true;
  }

  public Domain(Range2D var1, SoTRange var2) {
    this.xRange_ = var1;
    this.tRange_ = var2;
    this.yTime_ = true;
  }

  public Domain(Range2D var1, Range2D var2, boolean var3, boolean var4) {
    this.xRange_ = var1;
    this.yRange_ = var2;
    this.xReversed_ = var3;
    this.yReversed_ = var4;
  }

  public Domain(SoTRange var1, Range2D var2, boolean var3, boolean var4) {
    this.tRange_ = var1;
    this.yRange_ = var2;
    this.xTime_ = true;
    this.xReversed_ = var3;
    this.yReversed_ = var4;
  }

  public Domain(Range2D var1, SoTRange var2, boolean var3, boolean var4) {
    this.xRange_ = var1;
    this.tRange_ = var2;
    this.yTime_ = true;
    this.xReversed_ = var3;
    this.yReversed_ = var4;
  }

  public void setXRange(Range2D var1) {
    this.xTime_ = false;
    this.xRange_ = var1;
  }

  public void setXRange(SoTRange var1) {
    this.xTime_ = true;
    this.tRange_ = var1;
  }

  public Range2D getXRange() {
    return this.xRange_;
  }

  public void setYRange(Range2D var1) {
    this.yTime_ = false;
    this.yRange_ = var1;
  }

  public void setYRange(SoTRange var1) {
    this.yTime_ = true;
    this.tRange_ = var1;
  }

  public Range2D getYRange() {
    return this.yRange_;
  }

  public SoTRange getTimeRange() {
    return this.tRange_;
  }

  public boolean isXTime() {
    return this.xTime_;
  }

  public boolean isYTime() {
    return this.yTime_;
  }

  public void setYReversed(boolean var1) {
    this.yReversed_ = var1;
  }

  public boolean isYReversed() {
    return this.yReversed_;
  }

  public void setXReversed(boolean var1) {
    this.xReversed_ = var1;
  }

  public boolean isXReversed() {
    return this.xReversed_;
  }

  public boolean equals(Domain var1) {
    if(this.xTime_) {
      if(!var1.isXTime()) {
        return false;
      }

      if(!this.tRange_.equals(var1.getTimeRange())) {
        return false;
      }
    } else {
      if(var1.isXTime()) {
        return false;
      }

      if(!this.xRange_.equals(var1.getXRange())) {
        return false;
      }
    }

    if(this.yTime_) {
      if(!var1.isYTime()) {
        return false;
      }

      if(!this.tRange_.equals(var1.getTimeRange())) {
        return false;
      }
    } else {
      if(var1.isYTime()) {
        return false;
      }

      if(!this.yRange_.equals(var1.getYRange())) {
        return false;
      }
    }

    return this.xReversed_ == var1.isXReversed() && this.yReversed_ == var1.isYReversed();
  }

  public String toString() {
    StringBuilder var1 = new StringBuilder(100);
    var1.append("x=");
    if(this.xTime_) {
      var1.append(this.tRange_).append(",y=");
    } else {
      var1.append(this.xRange_).append(",y=");
    }

    if(this.yTime_) {
      var1.append(this.tRange_);
    } else {
      var1.append(this.yRange_);
    }

    var1.append(", xRev=").append(this.xReversed_);
    var1.append(", yRev=").append(this.yReversed_);
    return var1.toString();
  }
}
