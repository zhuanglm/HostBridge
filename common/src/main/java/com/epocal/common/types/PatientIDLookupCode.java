package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/2/2017.
 */

public enum PatientIDLookupCode {
    NotFound (0),
    Found (1),
    NoId (2),
    DMTimeout(3),
    HostTimeout (4),
    OtherError(5),
    NotEnabled (6),
    NotPerformed (7);

    public final Integer value;

    PatientIDLookupCode(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,PatientIDLookupCode> typeMap = new HashMap<Integer,PatientIDLookupCode>();
    static {
        for (PatientIDLookupCode type : PatientIDLookupCode.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static PatientIDLookupCode fromInt(int i){
        PatientIDLookupCode retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return PatientIDLookupCode.NotFound;
        }
        return retval;
    }
}
