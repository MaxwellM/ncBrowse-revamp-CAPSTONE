/*
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package ncBrowse.sgt.geom;

import java.io.Serializable;

/**
 * <code>SoTValue</code> is an abstract class used to wrap either a
 * <code>double</code> or <code>GeoDate</code>.  SoT stands for
 * space or time, but being basically lazy I've abbreviated it.
 *
 * @author Donald Denbo
 * @version $Revision: 1.7 $, $Date: 2003/08/22 23:02:40 $
 * @since sgt 2.0
 */
public abstract class SoTValue implements Serializable {
    /**
     * Inner class for <code>SoTRange</code> for type
     * <code>GeoDate</code>.
     * @since sgt 2.0
     * @deprecated As of sgt 3.0, replaced by {@link ncBrowse.sgt.geom.SoTValue.Time SoTValue.Time}
     */
    public static class GeoDate extends SoTValue {
        ncBrowse.sgt.geom.GeoDate date_;
        /**
         * Default constructor.
         */
        public GeoDate() {}
        /**
         * Construct a <code>SoTValue</code> from a <code>GeoDate</code>.
         */
        public GeoDate(ncBrowse.sgt.geom.GeoDate date) {
            date_ = date;
        }
        /**
         * Construct a <code>SoTValue</code> and initialize it to
         * represent the specified number of milliseconds since the
         * standard base time known as "the epoch", namely January 1,
         * 1970, 00:00:00.
         */
        public GeoDate(long time) {
            this(new ncBrowse.sgt.geom.GeoDate(time));
        }
        /**
         * Get the value
         */
        public ncBrowse.sgt.geom.GeoDate getValue() {
            return date_;
        }
        /**
         * Set the value from a <code>GeoDate</code>
         */
        public void setValue(ncBrowse.sgt.geom.GeoDate date) {
            date_ = date;
        }
        /**
         * Set the value from the number of milliseconds since the epoch.
         */
        public void setValue(long time) {
            date_ = new ncBrowse.sgt.geom.GeoDate(time);
        }
        public Object getObjectValue() {
            return date_;
        }
        /**
         * Test if <code>SoTValue</code> is time.
         */
        public boolean isTime() {
            return true;
        }
        /**
         * Test for equality
         */
        public boolean equals(SoTValue val) {
            return val.isTime() && date_.equals(((GeoDate) val).getValue());
        }
        /**
         * Add to value.
         *
         * @since 3.0
         */
        public void add(SoTValue val) {
            if(!val.isTime()) return;
            date_.add(((SoTValue.GeoDate)val).getValue());
        }
        /**
         * Get time as <code>long</code> since 1970-01-01.
         *
         * @since 3.0
         */
        public long getLongTime() {
            if(date_ == null) return Long.MAX_VALUE;
            return date_.getTime();
        }
        /**
         * Get time as <code>GeoDate</code>.
         *
         * @since 3.0
         */
        public ncBrowse.sgt.geom.GeoDate getGeoDate() {
            return new ncBrowse.sgt.geom.GeoDate(date_);
        }

        public String toString() {
            return date_.toString();
        }
    }
    /**
     * Inner class for <code>SoTRange</code> for type
     * <code>long</code>.  Used for time storage.
     * @since sgt 3.0
     */
    public static class Time extends SoTValue {
        long value_;
        /**
         * Default constructor.
         */
        public Time() {}
        /**
         * Construct and initialize value.
         */
        public Time(long value) {
            value_ = value;
        }
        public Time(ncBrowse.sgt.geom.GeoDate value) {
            value_ = value.getTime();
        }
        /**
         * Get the value
         */
        public long getValue() {
            return value_;
        }
        /**
         * Set the value
         */
        public void setValue(long value) {
            value_ = value;
        }
        public Object getObjectValue() {
            return value_;
        }
        /**
         * Test if <code>SoTValue</code> is time
         */
        public boolean isTime() {
            return true;
        }
        /**
         * Test for equality
         */
        public boolean equals(SoTValue val) {
            return val.isTime() && value_ == ((Time) val).getValue();
        }
        /**
         * Add to value.
         *
         * @since 3.0
         */
        public void add(SoTValue val) {
            if(!val.isTime()) return;
            long dval = val.getLongTime();
            value_ += dval;
        }
        /**
         * Get time as <code>long</code> since 1970-01-01.
         *
         * @since 3.0
         */
        public long getLongTime() {
            return value_;
        }
        /**
         * Get time as <code>GeoDate</code>.
         *
         * @since 3.0
         */
        public ncBrowse.sgt.geom.GeoDate getGeoDate() {
            return new ncBrowse.sgt.geom.GeoDate(value_);
        }

        public String toString() {
            //      return java.lang.Long.toString(value_);
            return getGeoDate().toString();
        }
    }


    /**
     * Inner class for <code>SoTRange</code> for type
     * <code>int</code>.
     * @since sgt 2.0
     */
    public static class Integer extends SoTValue {
        int value_;
        /**
         * Default constructor.
         */
        public Integer() {}
        /**
         * Construct and initialize value.
         */
        public Integer(int value) {
            value_ = value;
        }
        /**
         * Get the value
         */
        public int getValue() {
            return value_;
        }
        /**
         * Set the value
         */
        public void setValue(int value) {
            value_ = value;
        }
        public Object getObjectValue() {
            return value_;
        }
        /**
         * Test if <code>SoTValue</code> is time
         */
        public boolean isTime() {
            return false;
        }
        /**
         * Test for equality
         */
        public boolean equals(SoTValue val) {
            return !val.isTime() && value_ == ((Integer) val).getValue();
        }
        /**
         * Add to value.
         *
         * @since 3.0
         */
        public void add(SoTValue val) {
            if(val.isTime()) return;
            int dval = ((Number)val.getObjectValue()).intValue();
            value_ += dval;
        }
        /**
         * Get time as <code>long</code> since 1970-01-01.
         *
         * @since 3.0
         */
        public long getLongTime() {
            throw new Error("Method not appropriate for SoTValue.Int");
        }
        /**
         * Get time as <code>GeoDate</code>.
         *
         * @since 3.0
         */
        public ncBrowse.sgt.geom.GeoDate getGeoDate() {
            throw new Error("Method not appropriate for SoTValue.Int");
        }

        public String toString() {
            return java.lang.Integer.toString(value_);
        }
    }

    /**
     * Inner class for <code>SoTRange</code> for type
     * <code>short</code>.
     * @since sgt 2.0
     */
    public static class Short extends SoTValue {
        short value_;
        /**
         * Default constructor.
         */
        public Short() {}
        /**
         * Construct and initialize value.
         */
        public Short(short value) {
            value_ = value;
        }
        /**
         * Get the value
         */
        public short getValue() {
            return value_;
        }
        /**
         * Set the value
         */
        public void setValue(short value) {
            value_ = value;
        }
        public Object getObjectValue() {
            return value_;
        }
        /**
         * Test if <code>SoTValue</code> is time
         */
        public boolean isTime() {
            return false;
        }
        /**
         * Test for equality
         */
        public boolean equals(SoTValue val) {
            return !val.isTime() && value_ == ((Short) val).getValue();
        }
        /**
         * Add to value.
         *
         * @since 3.0
         */
        public void add(SoTValue val) {
            if(val.isTime()) return;
            short dval = ((Number)val.getObjectValue()).shortValue();
            value_ += dval;
        }
        /**
         * Get time as <code>long</code> since 1970-01-01.
         *
         * @since 3.0
         */
        public long getLongTime() {
            throw new Error("Method not appropriate for SoTValue.Short");
        }
        /**
         * Get time as <code>GeoDate</code>.
         *
         * @since 3.0
         */
        public ncBrowse.sgt.geom.GeoDate getGeoDate() {
            throw new Error("Method not appropriate for SoTValue.Short");
        }

        public String toString() {
            return java.lang.Short.toString(value_);
        }
    }

    /**
     * Inner class for <code>SoTRange</code> for type
     * <code>float</code>.
     * @since sgt 2.0
     */
    public static class Float extends SoTValue {
        float value_;
        /**
         * Default constructor.
         */
        public Float() {}
        /**
         * Construct and initialize value.
         */
        public Float(float value) {
            value_ = value;
        }
        /**
         * Get the value
         */
        public float getValue() {
            return value_;
        }
        /**
         * Set the value
         */
        public void setValue(float value) {
            value_ = value;
        }
        public Object getObjectValue() {
            return value_;
        }
        /**
         * Test if <code>SoTValue</code> is time
         */
        public boolean isTime() {
            return false;
        }
        /**
         * Test for equality
         */
        public boolean equals(SoTValue val) {
            return !val.isTime() && value_ == ((Float) val).getValue();
        }
        /**
         * Add to value.
         *
         * @since 3.0
         */
        public void add(SoTValue val) {
            if(val.isTime()) return;
            float dval = ((Number)val.getObjectValue()).floatValue();
            value_ += dval;
        }
        /**
         * Get time as <code>long</code> since 1970-01-01.
         *
         * @since 3.0
         */
        public long getLongTime() {
            throw new Error("Method not appropriate for SoTValue.Float");
        }
        /**
         * Get time as <code>GeoDate</code>.
         *
         * @since 3.0
         */
        public ncBrowse.sgt.geom.GeoDate getGeoDate() {
            throw new Error("Method not appropriate for SoTValue.Float");
        }

        public String toString() {
            return java.lang.Float.toString(value_);
        }
    }

    /**
     * Inner class for <code>SoTRange</code> for type
     * <code>double</code>.
     * @since sgt 2.0
     */
    public static class Double extends SoTValue {
        double value_;
        /**
         * Default constructor.
         */
        public Double() {}
        /**
         * Construct and initialize value.
         */
        public Double(double value) {
            value_ = value;
        }
        /**
         * Get the value
         */
        public double getValue() {
            return value_;
        }
        /**
         * Set the value
         */
        public void setValue(double value) {
            value_ = value;
        }
        public Object getObjectValue() {
            return value_;
        }
        /**
         * Test if <code>SoTValue</code> is time
         */
        public boolean isTime() {
            return false;
        }
        /**
         * Test for equality
         */
        public boolean equals(SoTValue val) {
            return !val.isTime() && value_ == ((Double) val).getValue();
        }
        /**
         * Add to value.
         *
         * @since 3.0
         */
        public void add(SoTValue val) {
            if(val.isTime()) return;
            double dval = ((Number)val.getObjectValue()).doubleValue();
            value_ += dval;
        }
        /**
         * Get time as <code>long</code> since 1970-01-01.
         *
         * @since 3.0
         */
        public long getLongTime() {
            throw new Error("Method not appropriate for SoTValue.Double");
        }
        /**
         * Get time as <code>GeoDate</code>.
         *
         * @since 3.0
         */
        public ncBrowse.sgt.geom.GeoDate getGeoDate() {
            throw new Error("Method not appropriate for SoTValue.Double");
        }
        public String toString() {
            return java.lang.Double.toString(value_);
        }
    }

//    public static class Long extends SoTValue {
//        long value_;
//
//        public Long() {
//        }
//
//        public Long(long var1) {
//            this.value_ = var1;
//        }
//
//        public SoTValue copy() {
//            return new SoTValue.Long(this.value_);
//        }
//
//        public long getValue() {
//            return this.value_;
//        }
//
//        public void setValue(long var1) {
//            this.value_ = var1;
//        }
//
//        public Object getObjectValue() {
//            return new java.lang.Long(this.value_);
//        }
//
//        public double getDouble() {
//            return (double)this.value_;
//        }
//
//        public boolean isTime() {
//            return false;
//        }
//
//        public boolean equals(SoTValue var1) {
//            return var1.isTime()?false:this.value_ == ((SoTValue.Long)var1).getValue();
//        }
//
//        public void add(SoTValue var1) {
//            if(!var1.isTime()) {
//                long var2 = ((Number)var1.getObjectValue()).longValue();
//                this.value_ += var2;
//            }
//        }
//
//        public void subtract(SoTValue var1) {
//            if(!var1.isTime()) {
//                long var2 = ((Number)var1.getObjectValue()).longValue();
//                this.value_ -= var2;
//            }
//        }
//
//        public boolean isNegative() {
//            return this.value_ < 0L;
//        }
//
//        public boolean isPositive() {
//            return this.value_ > 0L;
//        }
//
//        public long getLongTime() {
//            throw new Error("Method not appropriate for SoTValue.Long");
//        }
//
//        public ncBrowse.sgt.geom.GeoDate getGeoDate() {
//            throw new Error("Method not appropriate for SoTValue.Long");
//        }
//
//        public String toString() {
//            return java.lang.Long.toString(this.value_);
//        }
//    }

    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for
     * instantiation and provide a number of formats for storing
     * the information necessary to satisfy the various accessor
     * methods below.
     *
     */
    protected SoTValue() {}

    public abstract boolean isTime();
    public abstract String toString();
    public abstract boolean equals(SoTValue val);
    public abstract Object getObjectValue();
    public abstract long getLongTime();
    public abstract ncBrowse.sgt.geom.GeoDate getGeoDate();
    public abstract void add(SoTValue val);
}

