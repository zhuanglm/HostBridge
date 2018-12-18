package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 5/30/2017.
 */

public enum CardType {
    Unknown (0),
    BGE (1),
    BGEM (2),
    NextGen (3);

    public final Integer value;
    CardType(int value)
    {
        this.value = Integer.valueOf(value);
    }
    private  static final Map<Integer,CardType> typeMap = new HashMap<Integer,CardType>();
    static {
        for (CardType type : CardType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static CardType fromInt(int i){
        CardType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return CardType.Unknown;
        }
        return retval;

    }
}

