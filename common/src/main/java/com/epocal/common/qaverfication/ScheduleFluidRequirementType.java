package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum ScheduleFluidRequirementType {
    Unknown(0),
    MinimumLevel (2),
    SpecificFluids (3);
    public final Integer value;
    ScheduleFluidRequirementType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,ScheduleFluidRequirementType> typeMap = new HashMap<Integer,ScheduleFluidRequirementType>();
    static {
        for (ScheduleFluidRequirementType type : ScheduleFluidRequirementType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ScheduleFluidRequirementType fromInt(int i){
        ScheduleFluidRequirementType retval = typeMap.get(i);
        if (retval ==null){
            return ScheduleFluidRequirementType.Unknown;
        }
        return retval;
    }

}
