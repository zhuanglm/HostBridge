package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum SpecificOccuranceTime {
    AM0000 (0),
    AM0100 (1),
    AM0200 (2),
    AM0300 (3),
    AM0400 (4),
    AM0500 (5),
    AM0600 (6),
    AM0700 (7),
    AM0800 (8),
    AM0900 (9),
    AM1000 (10),
    AM1100 (11),
    PM1200 (12),
    PM1300 (13),
    PM1400 (14),
    PM1500 (15),
    PM1600 (16),
    PM1700 (17),
    PM1800 (18),
    PM1900 (19),
    PM2000 (20),
    PM2100 (21),
    PM2200 (22),
    PM2300 (23);

    public final Integer value;
    SpecificOccuranceTime(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SpecificOccuranceTime> typeMap = new HashMap<Integer,SpecificOccuranceTime>();
    static {
        for (SpecificOccuranceTime type : SpecificOccuranceTime.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SpecificOccuranceTime fromInt(int i){
        SpecificOccuranceTime retval = typeMap.get(i);
        if (retval ==null){
            return SpecificOccuranceTime.AM0000;
        }
        return retval;
    }


}
