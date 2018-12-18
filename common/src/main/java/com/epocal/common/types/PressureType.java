package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/1/2017.
 */

public enum PressureType {
    Unknown(0),
    kPa(1),
    mmHg(2);
    public final Integer value;
    PressureType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,PressureType> typeMap = new HashMap<Integer,PressureType>();
    static {
        for (PressureType type : PressureType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static PressureType fromInt(int i){
        PressureType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return PressureType.Unknown;
        }
        return retval;
    }
}

