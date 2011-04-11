//    Data Client is a simple script tasks executor.
//    Copyright (C) 2011 Adrián Romero Corchado.
//
//    This file is part of Task Executor
//
//    Data Client is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Data Client is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Data Client. If not, see <http://www.gnu.org/licenses/>.

package com.adr.dataclient.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

public abstract class Formats<T> {
    
    public final static Formats<Integer> INTEGER = new FormatsInteger();
    public final static Formats<Long> LONG = new FormatsLong();
    public final static Formats<String> STRING = new FormatsString();
    public final static Formats<Long> MEMORY = new FormatsMemory();
    public final static Formats<Double> DOUBLE = new FormatsDOUBLE();
    public final static Formats<Double> CURRENCY = new FormatsCURRENCY();
    public final static Formats<Double> PERCENT = new FormatsPERCENT();
    public final static Formats<Boolean> BOOLEAN = new FormatsBOOLEAN();
    public final static Formats<Date> TIMESTAMP = new FormatsTIMESTAMP();
    public final static Formats<Date> DATE = new FormatsDATE();
    public final static Formats<Date> TIME = new FormatsTIME();
    public final static Formats<byte[]> BYTEA = new FormatsBYTEA();
    
    private static NumberFormat fmtInteger = NumberFormat.getIntegerInstance();
    private static NumberFormat fmtDouble = NumberFormat.getNumberInstance();
    private static NumberFormat fmtCurrency = NumberFormat.getCurrencyInstance();
    private static NumberFormat fmtPercentage = new DecimalFormat("#,##0.##%");
    
    private static DateFormat fmtDate = DateFormat.getDateInstance();
    private static DateFormat fmtTime = DateFormat.getTimeInstance();
    private static DateFormat fmtDateTime = DateFormat.getDateTimeInstance();
    
    /** Creates a new instance of Formats */
    protected Formats() {
    }
    
    public String format(T value) {
        return value == null
                ? ""
                : pformat(value);
    }
    
    public T parse(String value, T defvalue) throws ParseException {
        return value == null || "".equals(value)
                ? defvalue
                : pparse(value);
    }
    
    public T parse(String value) throws ParseException {
        return parse(value, null);
    }
   
    protected abstract String pformat(T value);
    protected abstract T pparse(String value) throws ParseException;

    private static final class FormatsInteger extends Formats<Integer> {
        @Override
        protected String pformat(Integer value) {
            return fmtInteger.format(value);
        }
        @Override
        protected Integer pparse(String value) throws ParseException {
            return fmtInteger.parse(value).intValue();
        }
    }
    private static final class FormatsLong extends Formats<Long> {
        @Override
        protected String pformat(Long value) {
            return fmtInteger.format(value);
        }
        @Override
        protected Long pparse(String value) throws ParseException {
            return fmtInteger.parse(value).longValue();
        }
    }
    private static final class FormatsString extends Formats<String> {
        @Override
        protected String pformat(String value) {
            return value;
        }   
        @Override
        protected String pparse(String value) throws ParseException {
            return value;
        }
    } 
    private static final class FormatsMemory extends Formats<Long> {
            
        private static final long KB = 1024L;
        private static final long MB = KB * KB;
        private static final long GB = KB * MB;
        
        @Override
        protected String pformat(Long value) {
            if (value < KB) {
                return fmtInteger.format(value);
            } else if (value < MB) {
                return fmtInteger.format(value / KB) + " KB";
            } else if (value < GB) {
                return fmtInteger.format(value / MB) + " MB";
            } else {
                return fmtInteger.format(value / GB) + " GB";
            }
        }
        @Override
        protected Long pparse(String value) throws ParseException {
            String trimed = value.trim();
            if (trimed.endsWith("GB")) {
                return fmtInteger.parse(trimed.substring(0, trimed.length() - 2)).longValue() * GB;
            } else if(trimed.endsWith("MB")) {
                return fmtInteger.parse(trimed.substring(0, trimed.length() - 2)).longValue() * MB;
            } else if (trimed.endsWith("KB")) {
                return fmtInteger.parse(trimed.substring(0, trimed.length() - 2)).longValue() * KB;
            } else {
                return fmtInteger.parse(trimed).longValue();
            }
        }
    }
    private static final class FormatsDOUBLE extends Formats<Double> {
        @Override
        protected String pformat(Double value) {
            return fmtDouble.format(value);
        }   
        @Override
        protected Double pparse(String value) throws ParseException {
            return fmtDouble.parse(value).doubleValue();
        }
    }    
    private static final class FormatsPERCENT extends Formats<Double> {
        @Override
        protected String pformat(Double value) {
            return fmtPercentage.format(value);
        }   
        @Override
        protected Double pparse(String value) throws ParseException {
            try {
                return fmtPercentage.parse(value).doubleValue();
            } catch (ParseException e) {
                return fmtDouble.parse(value).doubleValue() / 100.0;
            }
        }
    }  
    private static final class FormatsCURRENCY extends Formats<Double> {
        @Override
        protected String pformat(Double value) {
            return fmtCurrency.format(value);
        }   
        @Override
        protected Double pparse(String value) throws ParseException {
            try {
                return fmtCurrency.parse(value).doubleValue();
            } catch (ParseException e) {
                return fmtDouble.parse(value).doubleValue();
            }
        }
    }  
    private static final class FormatsBOOLEAN extends Formats<Boolean> {
        @Override
        protected String pformat(Boolean value) {
            return value.toString();
        }   
        @Override
        protected Boolean pparse(String value) throws ParseException {
            return Boolean.valueOf(value);
        }
    }    
    private static final class FormatsTIMESTAMP extends Formats<Date> {
        @Override
        protected String pformat(Date value) {
            return fmtDateTime.format(value);
        }   
        @Override
        protected Date pparse(String value) throws ParseException {
            try {
                return fmtDateTime.parse(value);
            } catch (ParseException e) {
                return fmtDate.parse(value);
            }
        }
    }
    private static final class FormatsDATE extends Formats<Date> {
        @Override
        protected String pformat(Date value) {
            return fmtDate.format(value);
        }   
        @Override
        protected Date pparse(String value) throws ParseException {
            return fmtDate.parse(value);
        }
    }  
    private static final class FormatsTIME extends Formats<Date> {
        @Override
        protected String pformat(Date value) {
            return fmtTime.format(value);
        }   
        @Override
        protected Date pparse(String value) throws ParseException {
            return fmtTime.parse(value);
        }
    }    
    private static final class FormatsBYTEA extends Formats<byte[]> {
        @Override
        protected String pformat(byte[] value) {
            try {
                return new String(value, "UTF-8");
            } catch (java.io.UnsupportedEncodingException eu) {
                return "";
            }
        }   
        @Override
        protected byte[] pparse(String value) throws ParseException {
            try {
               return value.getBytes("UTF-8");
            } catch (java.io.UnsupportedEncodingException eu) {
               return new byte[0];
            }
        }
    }     
}
