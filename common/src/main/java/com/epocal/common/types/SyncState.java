package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 5/29/2017.
 */

public enum SyncState {
    Unknown (0),
    Unsent (1),
    SentSuccessfully (2),
    SentNotAccepted (3);  // Sent but did not saved or accepted at EDM.
    public final Integer value;
    SyncState(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SyncState> typeMap = new HashMap<>();
    static {
        for (SyncState type : SyncState.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SyncState fromInt(int i){
        SyncState retval = typeMap.get(i);
        if (retval ==null){
            return SyncState.Unknown;
        }
        return retval;
    }
}
