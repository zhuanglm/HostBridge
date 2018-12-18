package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum SpecificMonth {
    None(0),
    January (1),
    February (2),
    March (4),
    April (8),
    May (16),
    June (32),
    July (64),
    August (128),
    September (256),
    October (512),
    November (1024),
    December (2048);
    public final Integer value;
    SpecificMonth(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SpecificMonth> typeMap = new HashMap<Integer,SpecificMonth>();
    static {
        for (SpecificMonth type : SpecificMonth.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SpecificMonth fromInt(int i){
        SpecificMonth retval = typeMap.get(i);
        if (retval ==null){
            return SpecificMonth.None;
        }
        return retval;
    }

}
