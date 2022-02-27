package org.maoji.programming.textreportreader;

import org.maoji.programming.textreportreader.annotation.value.BooleanValue;
import org.maoji.programming.textreportreader.annotation.value.DateValue;
import org.maoji.programming.textreportreader.annotation.value.NumericValue;
import org.maoji.programming.textreportreader.model.TxtReportConfig;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Utility of handling raw text data and retrieve the value with the corresponding type
 *
 * @author Maoji
 * @version 1.0
 * */
class TxtReportDataUtil {
    /**
     * Retrieve date value by loading the annotations of the field
     *
     * @return date retrieved from raw input, null for invalid raw input
     * */
    static Date dateValue(Field fd, String input){
        if(fd.getAnnotation(DateValue.class) != null){
            SimpleDateFormat df = new SimpleDateFormat(fd.getAnnotation(DateValue.class).format());
            Date dateValue;
            try{
                dateValue = df.parse(input);
            }catch(ParseException e){
                dateValue = null;
            }
            return dateValue;

        }
        return null;
    }

    private static String valueOf(String num){
        return num
                .replace(",","")
                .replace(" ","")
                .replaceAll(TxtReportConfig.CURRENCIES_PATTERN,"")
                ;
    }
    private static String absolute(String num){
        return num.replaceAll("[-)(]","");
    }
    /**
     * Retrieve big decimal value by loading the annotations of the field
     *
     * @return number retrieved from raw input, 0 for invalid raw input
     * */
    static BigDecimal bigDecimalNumber(Field fd, String input) {
        BigDecimal result = new BigDecimal("0");
        try {
            BigDecimal direction;
            if (valueOf(input).matches("^[(][0-9]+[.]?[0-9]*[)]$") || input.contains("-")) {
                direction = new BigDecimal("-1");
            } else {
                direction = new BigDecimal("1");
            }
            BigDecimal numberValue = new BigDecimal(absolute(valueOf(input)));
            if (fd.getDeclaredAnnotation(NumericValue.class) != null) {

                if (fd.getDeclaredAnnotation(NumericValue.class).precision() > 0) {
                    result = numberValue.round(new MathContext(fd.getDeclaredAnnotation(NumericValue.class).precision(), fd.getDeclaredAnnotation(NumericValue.class).roundingMode()));
                }
                if (fd.getDeclaredAnnotation(NumericValue.class).scale() >= 0) {
                    result = numberValue.setScale(fd.getDeclaredAnnotation(NumericValue.class).scale(), fd.getDeclaredAnnotation(NumericValue.class).roundingMode());
                }

                return result.multiply(direction);
            } else {
                //default setting
                result = numberValue.round(new MathContext(15, RoundingMode.HALF_UP)).setScale(15, RoundingMode.HALF_UP);
            }
        }catch(NumberFormatException e){
            //equal 0 for invalid value
        }
        return result;
    }
    /**
     * Retrieve float value by loading the annotations of the field
     *
     * @return number retrieved from raw input, 0 for invalid raw input
     * */
    static Float floatNumber(Field fd, String input){
        BigDecimal result = bigDecimalNumber(fd, input);
        return result != null? result.floatValue(): 0.0f;
    }
    /**
     * Retrieve double value by loading the annotations of the field
     *
     * @return number retrieved from raw input, 0 for invalid raw input
     * */
    static Double doubleNumber(Field fd, String input){
        BigDecimal result = bigDecimalNumber(fd, input);
        return result != null? result.doubleValue(): 0.0;
    }
    /**
     * Retrieve long value by loading the annotations of the field
     *
     * @return number retrieved from raw input, 0 for invalid raw input
     * */
    static Long longNumber(Field fd, String input){
        BigDecimal result = bigDecimalNumber(fd, input);
        return result != null? result.longValue() : 0l;
    }
    /**
     * Retrieve integer value by loading the annotations of the field
     *
     * @return number retrieved from raw input, 0 for invalid raw input
     * */
    static Integer integerNumber(Field fd, String input){
        BigDecimal result = bigDecimalNumber(fd, input);
        return result != null? result.intValue() : 0;
    }

    /**
     * Retrieve boolean value by loading the annotations of the field
     *
     * @return boolean value retrieved from raw input, null for invalid raw input
     * */
    static Boolean booleanValue(Field fd, String input){
        if(fd.getAnnotation(BooleanValue.class) != null) {
            if(input.contains(fd.getAnnotation(BooleanValue.class).keywordOfTrue())){
                return true;
            }else if(input.contains(fd.getAnnotation(BooleanValue.class).keywordOfFalse())){
                return false;
            }else{
                return null;
            }
        }
        return null;
    }
}
