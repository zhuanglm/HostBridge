package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionType {
    Unknown (0),
    Bluetooth (1),
    WiFi (2);

    public final Integer value;
    ConnectionType(int value)
    {
        this.value = Integer.valueOf(value);
    }
    private  static final Map<Integer,ConnectionType> typeMap = new HashMap<Integer,ConnectionType>();
    static {
        for (ConnectionType type : ConnectionType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ConnectionType fromInt(int i){
        ConnectionType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return ConnectionType.Unknown;
        }
        return retval;

    }
}
