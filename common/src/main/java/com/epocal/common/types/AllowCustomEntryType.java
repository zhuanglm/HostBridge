package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 4/4/2017.
 */

public enum AllowCustomEntryType {
    NoneAllowed  (0),
    AllowCustomDrawSiteEntry  (1),
    AllowCustomSampleTypeEntry  (2),
    AllowCustomRespiratoryModeEntry  (4),
    AllowCustomAllensTestEntry  (8),
    AllowCustomDeliverySystemEntry  (16),
    AllowCustomQASampleTypeEntry (32);
    public final Integer value;

    AllowCustomEntryType(Integer value)
    {
        this.value = value;
    }

    private  static final Map<Integer, AllowCustomEntryType> typeMap = new HashMap<>();

    static {
        for (AllowCustomEntryType type : AllowCustomEntryType.values()){
            typeMap.put(type.value,type);
        }
    }

    public static AllowCustomEntryType fromInt(int i) {
        AllowCustomEntryType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return AllowCustomEntryType.NoneAllowed;
        }
        return retval;
    }

}
