package com.epocal.common.hostoperationstate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 08/01/2018.
 */
// these are the states the Host can be in during operation
    // 'multiple' denotes that the Host is simultaneously in more than one operation state
public enum OperationStateType {
    IDLE (0),
    TESTING (1),
    COMMUNICATING(2),
    PRINTING (3),
    SYNCHRONIZING (4),
    TESTCOMPLETED (5),
    MULTIOPERATING (6);

    public final Integer value;
    OperationStateType(int value)
    {
        this.value = Integer.valueOf(value);
    }
    private  static final Map<Integer,OperationStateType> typeMap = new HashMap<Integer,OperationStateType>();
    static {
        for (OperationStateType type : OperationStateType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static OperationStateType fromInt(int i){
        OperationStateType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return OperationStateType.IDLE;
        }
        return retval;

    }
}
