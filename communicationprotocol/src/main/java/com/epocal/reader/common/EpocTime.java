package com.epocal.reader.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dning on 6/13/2017.
 */

public class EpocTime
{

    public static Date now()
    {
        return new Date(System.currentTimeMillis());
    }

    public static long timePOSIXMilliseconds()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(1970, Calendar.JANUARY, 1);
        Date date = cal.getTime();
        return System.currentTimeMillis() - date.getTime();
    }

    public static long timePOSIXMilliseconds2009()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2009, Calendar.SEPTEMBER, 1);
        Date date = cal.getTime();
        return System.currentTimeMillis() - date.getTime();
    }

    public static long timePOSIXMilliseconds2009(Date time)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2009, Calendar.SEPTEMBER, 1);
        Date date = cal.getTime();
        return time.getTime() - date.getTime();
    }

    public static long timePOSIXMinutes2009(Date time) {
        return timePOSIXMilliseconds2009(time) / (1000 * 60);
    }
}
