package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/1/2017.
 */

public enum NotifyActionType {
    None (0),
    NotifyPhysician (1),
    NotifyRegisteredNurse (2),
    RepeatedTest (3),
    SentToLab (4),
    Other (5),
    ExpectedValues (6);
    public final Integer value;
    NotifyActionType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,NotifyActionType> typeMap = new HashMap<Integer,NotifyActionType>();
    static {
        for (NotifyActionType type : NotifyActionType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static NotifyActionType fromInt(int i){
        NotifyActionType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return NotifyActionType.None;
        }
        return retval;
    }
}
