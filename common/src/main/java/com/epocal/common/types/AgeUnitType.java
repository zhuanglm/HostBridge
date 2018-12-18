package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/28/2017.
 */

public enum AgeUnitType {
    UNKNOWN (0),
    DAYS (1),
    MONTHS (2),
    YEARS (3);

    public final Integer value;
    AgeUnitType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,AgeUnitType> typeMap = new HashMap<Integer,AgeUnitType>();
    static {
        for (AgeUnitType type : AgeUnitType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static AgeUnitType fromInt(int i){
        AgeUnitType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return AgeUnitType.UNKNOWN;
        }
        return retval;
    }
}
