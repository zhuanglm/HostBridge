package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

public enum PrinterType {
    Unknown (0),
    Zebra_ZQ110 (1),
    Martel_LLP1880B_391 (2),
    Other (3);

    public final Integer value;
    PrinterType(int value)
    {
        this.value = Integer.valueOf(value);
    }
    private  static final Map<Integer,PrinterType> typeMap = new HashMap<Integer,PrinterType>();
    static {
        for (PrinterType type : PrinterType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static PrinterType fromInt(int i){
        PrinterType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return PrinterType.Unknown;
        }
        return retval;

    }
}
