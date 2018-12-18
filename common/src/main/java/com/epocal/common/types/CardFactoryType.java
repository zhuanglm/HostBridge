package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 5/30/2017.
 */

public enum CardFactoryType {
    Unknown (0),
    Ottawa1 (1),
    Ottawa2 (2),
    Ottawa3 (3),
    Ottawa4 (4);
    public final Integer value;
    CardFactoryType(int value)
    {
        this.value = Integer.valueOf(value);
    }
    private  static final Map<Integer,CardFactoryType> typeMap = new HashMap<Integer,CardFactoryType>();
    static {
        for (CardFactoryType type : CardFactoryType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static CardFactoryType fromInt(int i){
        CardFactoryType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return CardFactoryType.Unknown;
        }
        return retval;

    }
}
