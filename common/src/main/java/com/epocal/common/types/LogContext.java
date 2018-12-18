package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 7/13/2017.
 */

public enum LogContext {
    UNKNOWN (0),
    EDM(1),
    HOST (2),
    REALM (3),
    BLUETOOTH_COMMUNICATION (4),
    SCANNER (4),
    SYNCHRONIZATION (5),
    DEVICE(6);
    public final Integer value;
    LogContext(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,LogContext> typeMap = new HashMap<Integer,LogContext>();
    static {
        for (LogContext type : LogContext.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static LogContext fromInt(int i){
        LogContext retval = typeMap.get(i);
        if (retval ==null){
            return LogContext.UNKNOWN;
        }
        return retval;
    }
}
