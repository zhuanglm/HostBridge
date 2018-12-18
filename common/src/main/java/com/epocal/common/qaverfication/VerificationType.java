package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum VerificationType {
    Unknown (0),
    Mandatory(1),
    Compliance(2),
    Disabled(3);
    public final Integer value;
    VerificationType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,VerificationType> typeMap = new HashMap<Integer,VerificationType>();
    static {
        for (VerificationType type : VerificationType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static VerificationType fromInt(int i){
        VerificationType retval = typeMap.get(i);
        if (retval ==null){
            return VerificationType.Unknown;
        }
        return retval;
    }

}
