package com.epocal.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * precision string example "###.000" - leading zeros are omitted, trailing zeros are displayed
 * Created by bmate on 6/13/2017.
 */

public class DecimalConversionUtil {
    public static String DisplayDouble(double value, String precision) {
        String retval = "";
        DecimalFormat df = new DecimalFormat(precision);
        df.setRoundingMode(RoundingMode.DOWN);
        retval = df.format(value);
        return retval;

    }

    public static String DisplayDouble(double value, String precision, Locale locale) {
        String retval = "";
        DecimalFormat df = new DecimalFormat(precision);
        df.setRoundingMode(RoundingMode.DOWN);
        Locale.setDefault(locale);
        retval = df.format(value);
        return retval;
    }

    public static double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static float round(float value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimal.floatValue();
    }
}
