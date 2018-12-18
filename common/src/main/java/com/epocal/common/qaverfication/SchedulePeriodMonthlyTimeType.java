package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum SchedulePeriodMonthlyTimeType {
    Unknown (0),
    First (1),
    Second (2),
    Third (3),
    Last (4);
    public final Integer value;
    SchedulePeriodMonthlyTimeType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SchedulePeriodMonthlyTimeType> typeMap = new HashMap<Integer,SchedulePeriodMonthlyTimeType>();
    static {
        for (SchedulePeriodMonthlyTimeType type : SchedulePeriodMonthlyTimeType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SchedulePeriodMonthlyTimeType fromInt(int i){
        SchedulePeriodMonthlyTimeType retval = typeMap.get(i);
        if (retval ==null){
            return SchedulePeriodMonthlyTimeType.Unknown;
        }
        return retval;
    }
}
