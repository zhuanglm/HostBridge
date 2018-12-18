package com.epocal.common.androidutil;

import com.epocal.common.types.LanguageType;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class LocaleUtil {

    public static String getIntegerValue(String value) throws ParseException
    {
        return getIntegerValue(value, LanguageType.English);
    }

    private static String getIntegerValue(String value, LanguageType languge) throws ParseException
    {
        Locale fmtLocale = getLocale(LanguageType.English);
        int rInt = NumberFormat.getInstance(fmtLocale).parse(value).intValue();
        return NumberFormat.getInstance(fmtLocale).format(rInt);
    }


    private static Locale getLocale(LanguageType language)
    {
        switch (language)
        {
            case English:
                return Locale.ENGLISH;
        }
        return Locale.ENGLISH;
    }
}
