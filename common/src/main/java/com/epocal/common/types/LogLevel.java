package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/15/2017.
 */

public enum LogLevel {
    None(0),
    Debug(1),
    Information(2),
    Error(4);

    public final Integer value;
    LogLevel(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,LogLevel> typeMap = new HashMap<Integer,LogLevel>();
    static {
        for (LogLevel type : LogLevel.values()){
            typeMap.put(type.value,type);
        }
    }

    public  static LogLevel fromInt(int i){
        LogLevel retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return LogLevel.None;
        }
        return retval;
    }
}
