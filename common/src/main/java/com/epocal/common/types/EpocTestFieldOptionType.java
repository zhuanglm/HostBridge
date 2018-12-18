package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 7/21/2017.
 */

public enum EpocTestFieldOptionType {
    NONE (0),
    OPTIONAL (1), // FOR OPTIONAL FIELDS OR CUSTOM FIELDS ASSIGNED TO OTHER GROUPS: PATIENT INFO, SAMPLE INFO ...
    MANDATORY (2),
    REQUIRED (3),   // A.K.A COMPLIANCE
    HIDDEN (4),       // NOT DISPLAYED
    CUSTOM (5); // FOR CUSTOM FIELDS KEPT IN CUSTOM GROUP

    public final Integer value;
    EpocTestFieldOptionType (Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,EpocTestFieldOptionType > typeMap = new HashMap<Integer,EpocTestFieldOptionType >();
    static {
        for (EpocTestFieldOptionType  type : EpocTestFieldOptionType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static EpocTestFieldOptionType  fromInt(int i){
        EpocTestFieldOptionType  retval = typeMap.get(i);
        if (retval ==null){
            return EpocTestFieldOptionType.NONE;
        }
        return retval;
    }
}
