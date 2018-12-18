package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 4/4/2017.
 */

public enum SaveRawData {
    Never  (0),
    OnFailure  (1),
    Always (2);
    public final Integer value;
    SaveRawData(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SaveRawData> typeMap = new HashMap<Integer,SaveRawData>();
    static {
        for (SaveRawData type : SaveRawData.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SaveRawData fromInt(int i){
        SaveRawData retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return SaveRawData.Never;
        }
        return retval;
    }
}
