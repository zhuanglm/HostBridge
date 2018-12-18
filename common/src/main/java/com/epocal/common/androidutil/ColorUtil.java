package com.epocal.common.androidutil;

import com.epocal.common.R;

import java.lang.reflect.Field;

/**
 * Created by bmate on 7/18/2017.
 * will return the color resource id
 * @return int
 */

public  class ColorUtil {

    public static int getColorByName( String name ) {
        int colorId = 0;

        try {
            Class res = R.color.class;
            Field field = res.getField( name );
            colorId = field.getInt(null);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return colorId;
    }
}
