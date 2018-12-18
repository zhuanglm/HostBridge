package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/6/2017.
 */

public enum RangeIgnoreInfo {
    IgnoreNone (0),
    IgnoreCriticalLow (1),
    IgnoreCriticalHigh (2),
    IgnoreReferenceHigh (4),
    IgnoreReferenceLow (8);


    public final Integer value;
    RangeIgnoreInfo(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,RangeIgnoreInfo> typeMap = new HashMap<Integer,RangeIgnoreInfo>();
    static {
        for (RangeIgnoreInfo type : RangeIgnoreInfo.values()){
            typeMap.put(type.value,type);
        }
    }

    public  static RangeIgnoreInfo fromInt(int i){
        RangeIgnoreInfo retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return RangeIgnoreInfo.IgnoreNone;
        }
        return retval;
    }



}
