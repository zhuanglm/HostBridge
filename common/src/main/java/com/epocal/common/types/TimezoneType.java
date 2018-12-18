package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/15/2017.
 */

public enum TimezoneType {
    Unknown (0),
    EASTERNTIME_USACAN (1),       //GMT-05:00
    ATLANTIC_CAN (2),             //GMT-04:00
    BRASILIA (3),                 //GMT-03:00
    GMT (4),                      //GMT
    CENTRALEUROPE (5),            //GMT+01:00
    EASTERNEUROPE (6),            //GMT+02:00
    MOSCOW (7),                   //GMT+03:00
    CAUCASUS (8),                 //GMT+04:00
    ISLAMABAD (9),                //GMT+05:00
    INDIA (10),                   //GMT+05:30
    NOVOSIBIRSK (11),             //GMT+06:00
    BANGKOK (12),                 //GMT+07:00
    BEIJING (13),                 //GMT+08:00
    JAPAN (14),                   //GMT+09:00
    MELBOURNE (15),               //GMT+10:00
    SOLOMON_ILE (16),             //GMT+11:00
    FIJI (17),                    //GMT+12:00
    SAMOA (18),                   //GMT-11:00
    HAWAI (19),                   //GMT-10:00
    ALASKA (20),                  //GMT-9:00
    PACIFICTIME_USACAN (21),      //GMT-8:00
    ARIZONA (22),                 //GMT-7:00
    CENTRALTIME_USACAN (23);      //GMT-6:00

    public final Integer value;
    TimezoneType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,TimezoneType> typeMap = new HashMap<Integer,TimezoneType>();
    static {
        for (TimezoneType type : TimezoneType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static TimezoneType fromInt(int i){
        TimezoneType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return TimezoneType.Unknown;
        }
        return retval;
    }
}
