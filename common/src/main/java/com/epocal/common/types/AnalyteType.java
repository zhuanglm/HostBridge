package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/12/2017.
 */

public enum AnalyteType {

    Measured (0),
    Calculated (1),
    Corrected (2);

    public final Integer value;
    AnalyteType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,AnalyteType> typeMap = new HashMap<Integer,AnalyteType>();
    static {
        for (AnalyteType type : AnalyteType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static AnalyteType fromInt(int i){
        AnalyteType retval = typeMap.get(Integer.valueOf(i));
        if (retval == null){
            return AnalyteType.Measured;
        }
        return retval;
    }
}
