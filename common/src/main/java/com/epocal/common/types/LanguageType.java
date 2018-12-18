package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/15/2017.
 */

public enum LanguageType {
    Unknown (0),
    English (1),
    German (2),
    French (3),
    Spanish (4),
    Italian (5),
    Japanese (6),
    Greek (7),
    Polish (8),
    Romanian (9),
    Russian (10),
    Turkish (11),
    Portugese (12),
    Dutch (13),
    Danish (14),
    Swedish (15),
    Finnish (16),
    Norwegian (17),
    Latvian (18),
    Estonian (19),
    Hungarian (20),
    Czech (21),
    Croatian (22),
    Serbian (23),
    Lithuanian (24);

    public final Integer value;
    LanguageType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,LanguageType> typeMap = new HashMap<Integer,LanguageType>();
    static {
        for (LanguageType type : LanguageType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static LanguageType fromInt(int i){
        LanguageType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return LanguageType.Unknown;
        }
        return retval;
    }
    public int getValue() {
        return value;
    }
}
