package com.epocal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static Date CreateDate(String dateString)
    {
        Date retval = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            retval = sdf.parse(dateString);
        }
        catch (Exception ex)
        {
            // do something, at least log
        }

        return retval;

    }

    /** Using Calendar - THE CORRECT WAY**/
    public static int daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        int daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static int daysFromYearsToNow(int ageInYears) {
        Calendar now = Calendar.getInstance();
        Calendar past = Calendar.getInstance();
        past.add(Calendar.YEAR, (-1)*ageInYears);
        return daysBetween(past,now);

    }
    public static int daysFromMonthsToNow(int ageInMonths) {
        Calendar now = Calendar.getInstance();
        Calendar past = Calendar.getInstance();
        past.add(Calendar.MONTH, (-1)*ageInMonths);
        return daysBetween(past,now);

    }
    public static Calendar now()
    {
        return Calendar.getInstance();
    }

    public static Calendar CreateDate(String dateString, String dateFormat)
    {
        //"EEE MMM dd HH:mm:ss z yyyy"
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(dateString));
        }catch (ParseException e)
        {
            cal = getZero();
        }
        return cal;
    }

    public static Calendar getZero()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 0);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static String toString(Calendar calendar)
    {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted = format1.format(calendar.getTime());

        return formatted;
    }

    public static String toString(Calendar calendar, String formatString)
    {
        SimpleDateFormat format1 = new SimpleDateFormat(formatString);
        String formatted = format1.format(calendar.getTime());

        return formatted;
    }

    public static String toString(String formatString)
    {
        SimpleDateFormat format1 = new SimpleDateFormat(formatString);
        return format1.format(now().getTime());
    }
}
