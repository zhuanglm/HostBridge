package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/26/2017.
 * it shows possible rangevalue dependencies:on gender, age, fasting etc ... extensible
 */

public enum RangeValueMetaInfo {
    NO_DEPENDENCY (0),
    DEPENDS_ON_AGE (1),
    DEPENDS_ON_GENDER (2),
    DEPENDS_ON_FASTING (4);
    // DEPENDS_ON_ZODIAC
    public final Integer value;
    RangeValueMetaInfo(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,RangeValueMetaInfo> typeMap = new HashMap<Integer,RangeValueMetaInfo>();
    static {
        for (RangeValueMetaInfo type : RangeValueMetaInfo.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static RangeValueMetaInfo fromInt(int i){
        RangeValueMetaInfo retval = typeMap.get(i);
        if (retval ==null){
            return RangeValueMetaInfo.NO_DEPENDENCY;
        }
        return retval;
    }


}
