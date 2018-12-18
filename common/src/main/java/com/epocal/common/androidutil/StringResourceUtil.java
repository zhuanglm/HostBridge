package com.epocal.common.androidutil;

import com.epocal.common.R;

import java.lang.reflect.Field;

/**
 * Created by bmate on 7/19/2017.
 */

public class StringResourceUtil {
    public static int getStringIDByName( String name ) {
        int stringId = 0;

        try {
            Class res = R.string.class;
            Field field = res.getField( name );
            stringId = field.getInt(null);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return stringId;
    }
}
