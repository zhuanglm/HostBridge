package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 8/8/2017.
 */

public enum SelenaFamilyType {

    UNKNOWN (0),
    DELIVERYSYSTEM (1),
    RESPIRATORYMODE (2),
    DRAWSITE (3),
    SAMPLETYPE (4),
    ALLENSTYPE (5);
    public final Integer value;

    SelenaFamilyType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SelenaFamilyType> typeMap = new HashMap<Integer,SelenaFamilyType>();
    static {
        for (SelenaFamilyType type : SelenaFamilyType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SelenaFamilyType fromInt(int i){
        SelenaFamilyType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return SelenaFamilyType.UNKNOWN;
        }
        return retval;
    }
}
