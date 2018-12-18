package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 5/30/2017.
 */

public enum HostStatus {
    Unknown(0), Normal(1), LockedOut(2);

    public final Integer value;
    HostStatus(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,HostStatus> typeMap = new HashMap<Integer,HostStatus>();
    static {
        for (HostStatus type : HostStatus.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static HostStatus fromInt(int i){
        HostStatus retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return HostStatus.Unknown;
        }
        return retval;
    }

}
