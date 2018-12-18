package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum SchedulePeriodType {
    Unknown (0),
    Fixed (1),
    Weekly (2),
    Monthly (3),
    BiAnnual (4);

    public final Integer value;
    SchedulePeriodType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SchedulePeriodType> typeMap = new HashMap<Integer,SchedulePeriodType>();
    static {
        for (SchedulePeriodType type : SchedulePeriodType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SchedulePeriodType fromInt(int i){
        SchedulePeriodType retval = typeMap.get(i);
        if (retval ==null){
            return SchedulePeriodType.Unknown;
        }
        return retval;
    }
}
