package com.epocal.common;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * CU stands for Common Utilities. This is a final class and includes methods which are commonly
 * shared throughout the application.
 *
 * Created by Zeeshan A Zakaria on 6/13/2017.
 */

public final class CU {

    private static String APP_SESSION = "session";

    public static void addToSharedPrefs(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(APP_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void addToSharedPrefs(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(APP_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void addToSharedPrefs(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(APP_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     *
     * @param input the input String
     * @return      the title case String
     */
    public static String titleCase(String input) {
        String[] words = input.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        return sb.toString();
    }

    public static void allowStatusBarExpansion(Context context) {
        CustomViewGroup view = new CustomViewGroup(context);
        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        manager.removeView(view);
    }

    // Stop expansion of the status bar on swipe down.
    public static void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity) context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to receive touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        CustomViewGroup view = new CustomViewGroup(context);

        manager.addView(view, localLayoutParams);
    }

    /**
     * Utility class to replace a character entered in Password field with '#' character
     * to hide the clear text.
     */
    public class HashTagPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return '#'; // Replace input char with '#' to hide password
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }

    static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Convert dp to px
     * @param dp
     * @return px
     */
    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    /**
     * Convert px to dp
     * @param px
     * @return dp
     */
    public static int px2dp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px/displayMetrics.density);
    }

    /**
     * This class creates the overlay on the status bar which stops it from expanding.
     */
    public static class CustomViewGroup extends ViewGroup {

        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "********** Status bar swipe intercepted");
            return true;
        }
    }

    public static final String DATE_SEPARATOR = "/";
    public static final String TIME_SEPARATOR = ":";
    public static final String EPOC_DATE_STRING_KEY = "epoc_date_string_key";
    public static final String EPOC_TIME_STRING_KEY = "epoc_time_string_key";

    // Convert Calendar obj to  Date String "MM/DD/YYYY"
    public static String calendarToEpocDateString(Calendar calendar) {
        int year       = calendar.get(Calendar.YEAR);
        int month      = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Format Month and Day integer to 2 digits, adding leading 0 if needed.
        String monthAsString = String.format("%02d", month+1);
        String dayOfMonthAsString = String.format("%02d", dayOfMonth);
        String yearAsString = String.valueOf(year);

        return monthAsString + DATE_SEPARATOR + dayOfMonthAsString + DATE_SEPARATOR + yearAsString;
    }

    // Convert Date String "MM/DD/YYYY" to Calendar obj
    public static Calendar epocDateStringToCalendar(String epocDateString) throws IllegalArgumentException {
        if ((epocDateString == null) || (epocDateString.isEmpty())) {
            throw new IllegalArgumentException("Date string is null or an empty string.");
        }

        // date format check
        try {
            epocDateStringToDate(epocDateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Date Parsing Error: Date string passed has a wrong format. It must be MM/DD/YYYY");
        }

        String[] parts = epocDateString.split(DATE_SEPARATOR);
        if (parts.length < 3) {
            throw new IllegalArgumentException("Date Parsing Error: Date string passed has a wrong format. Could not find date, month or year.");
        }

        // range check
        int month = Integer.parseInt(parts[0]) - 1; // Jan=0, Dec=11
        int dayOfMonth = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        boolean isInRange = isIntValueBetweenRange(month, 0, 11);
        isInRange = isInRange && isIntValueBetweenRange(dayOfMonth, 1, 31);

        if (!isInRange) {
            throw new IllegalArgumentException("Date out of range: month="+month+" dayOfMonth="+dayOfMonth+" year="+year);
        }

        return new GregorianCalendar(year, month, dayOfMonth);
    }

    // Convert Calendar obj to Time String "HH:MM"
    public static String calendarToEpocTimeString(Calendar calendar) {
        int hour        = calendar.get(Calendar.HOUR_OF_DAY);
        int minute      = calendar.get(Calendar.MINUTE);

        // Format Hour and Min integer to 2 digits, adding leading 0 if needed.
        String hourAsString = String.format("%02d", hour);
        String minAsString = String.format("%02d", minute);

        return hourAsString + TIME_SEPARATOR + minAsString;
    }

    // Convert Date String "HH:MM" to Calendar obj
    public static Calendar epocTimeStringToCalendar(String epocTimeString) throws IllegalArgumentException {
        String[] parts = epocTimeString.split(TIME_SEPARATOR);

        if (parts.length < 2) {
            throw new IllegalArgumentException("Date Parsing Error: Time string passed has a wrong format. It must be HH:MM");
        }

        int hourOfDay = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        // Range check - hourOfDay is between 0-23
        if ((hourOfDay < 0) || (hourOfDay > 23)) {
            throw new IllegalArgumentException("Date Parsing Error: hourOfDay("+hourOfDay+") is out of range. It must be between 0-23");
        }

        // Range check - minute is between 0-59
        if ((minute < 0) || (minute > 59)) {
            throw new IllegalArgumentException("Date Parsing Error: minute("+minute+") is out of range. It must be between 0-23");
        }

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        return calendar;
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    public static boolean isIntValueBetweenRange(int value, int minInclusive, int maxInclusive) {
        if ((value >= minInclusive) && (value <= maxInclusive)) {
            return true;
        } else {
            return false;
        }
    }

    public static Date epocDateStringToDate(String epocDateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/yyyy");
        sdf.setLenient(true);
        return sdf.parse(epocDateString);
    }

    public static String dateToEpocDateString(Date aDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/yyyy");
        return sdf.format(aDate);
    }

    public static void hideKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
