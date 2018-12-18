package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 4/4/2017.
 */

public enum LogoutPowerOffType {
    NONE (0)
            {@Override
            public String toString() {
                return "None";
            }},
    ON_BATTERY (1)
            {@Override
            public String toString() {
                return "ON Battery";
            }},
    ON_AC (2)
            {@Override
            public String toString() {
                return "ON AC";
            }},
    BOTH (3)
            {@Override
            public String toString() {
                return "Both";
            }};
    
    public final Integer value;
    LogoutPowerOffType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,LogoutPowerOffType> typeMap = new HashMap<Integer,LogoutPowerOffType>();
    static {
        for (LogoutPowerOffType type : LogoutPowerOffType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static LogoutPowerOffType fromInt(int i){
        LogoutPowerOffType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return LogoutPowerOffType.NONE;
        }
        return retval;
    }

    public int getValue() {
        return value;
    }
}
