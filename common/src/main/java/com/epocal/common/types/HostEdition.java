package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/15/2017.
 */

public enum HostEdition {
    Unknown (0),
    Professional (1),
    Canadian (2),
    Vet (3),
    CLIA (4);
    public final Integer value;
    HostEdition(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,HostEdition> typeMap = new HashMap<Integer,HostEdition>();
    static {
        for (HostEdition type : HostEdition.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static HostEdition fromInt(int i){
        HostEdition retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return HostEdition.Unknown;
        }
        return retval;
    }
}
