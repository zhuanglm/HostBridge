package com.epocal.util;

/**
 * Created by bmate on 4/3/2017.
 */

public class StringUtil {
    public static boolean empty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    /**
     * Format (int)timeInSecond to string with format
     * "mm:ss"
     */
    public static String timeInSecondToTimeString(int timeInSecond) {
        String str;
        try {
            int minutes = timeInSecond / 60;
            int seconds = timeInSecond % 60;
            str = String.format("%02d:%02d", minutes, seconds);
        } catch (Exception e) {
            // division error. use input as is
            str = String.format("%d", timeInSecond);
        }
        return str;
    }
}
