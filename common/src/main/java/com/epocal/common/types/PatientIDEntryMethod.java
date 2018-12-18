package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/2/2017.
 */

public enum PatientIDEntryMethod {
    None (0),
    Keyboard (1),
    Scanner (2);
    public final Integer value;
    PatientIDEntryMethod(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,PatientIDEntryMethod> typeMap = new HashMap<Integer,PatientIDEntryMethod>();
    static {
        for (PatientIDEntryMethod type : PatientIDEntryMethod.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static PatientIDEntryMethod fromInt(int i){
        PatientIDEntryMethod retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return PatientIDEntryMethod.None;
        }
        return retval;
    }
}
