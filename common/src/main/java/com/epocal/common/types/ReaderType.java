package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 4/4/2017.
 */

public enum ReaderType {
    NONE (0),
    BGE_READER (1), // a.k.a 1A reader
    NEXTGEN_READER (2);

    public final Integer value;
    ReaderType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,ReaderType> typeMap = new HashMap<Integer,ReaderType>();
    static {
        for (ReaderType type : ReaderType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ReaderType fromInt(int i){
        ReaderType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return ReaderType.NONE;
        }
        return retval;
    }
}
