package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum ScheduleOccuranceType {
    Unknown (0),
    OnSpecificDate (1),
    OnSpecificDateOfWeek (2);
    public final Integer value;
    ScheduleOccuranceType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,ScheduleOccuranceType> typeMap = new HashMap<Integer,ScheduleOccuranceType>();
    static {
        for (ScheduleOccuranceType type : ScheduleOccuranceType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ScheduleOccuranceType fromInt(int i){
        ScheduleOccuranceType retval = typeMap.get(i);
        if (retval ==null){
            return ScheduleOccuranceType.Unknown;
        }
        return retval;
    }
}
