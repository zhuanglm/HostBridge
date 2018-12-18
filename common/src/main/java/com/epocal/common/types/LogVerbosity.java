package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 06/12/2017.
 */

public enum LogVerbosity {
    None(0),
    Low(1),
    Medium(2),
    High(3);

    public final Integer value;
    LogVerbosity(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,LogVerbosity> typeMap = new HashMap<Integer,LogVerbosity>();
    static {
        for (LogVerbosity type : LogVerbosity.values()){
            typeMap.put(type.value,type);
        }
    }

    public  static LogVerbosity fromInt(int i){
        LogVerbosity retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return LogVerbosity.None;
        }
        return retval;
    }
}
