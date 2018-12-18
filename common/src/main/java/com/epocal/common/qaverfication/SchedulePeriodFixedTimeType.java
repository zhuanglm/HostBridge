package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum SchedulePeriodFixedTimeType {
    Unknown(0),
    Hours (1),
    Days (2),
    Weeks (3);
    public final Integer value;
    SchedulePeriodFixedTimeType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SchedulePeriodFixedTimeType> typeMap = new HashMap<Integer,SchedulePeriodFixedTimeType>();
    static {
        for (SchedulePeriodFixedTimeType type : SchedulePeriodFixedTimeType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SchedulePeriodFixedTimeType fromInt(int i){
        SchedulePeriodFixedTimeType retval = typeMap.get(i);
        if (retval ==null){
            return SchedulePeriodFixedTimeType.Unknown;
        }
        return retval;
    }
}
