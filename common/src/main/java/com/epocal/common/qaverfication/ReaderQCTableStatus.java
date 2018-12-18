package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum ReaderQCTableStatus {

    Unknown (0),
    ReaderQCNoSupported (1),
    CleanResetReader (2), //Zero Table
    RestoreReader (3),
    SyncFromReader (4);

    public final Integer value;
    ReaderQCTableStatus(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,ReaderQCTableStatus> typeMap = new HashMap<Integer,ReaderQCTableStatus>();
    static {
        for (ReaderQCTableStatus type : ReaderQCTableStatus.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ReaderQCTableStatus fromInt(int i){
        ReaderQCTableStatus retval = typeMap.get(i);
        if (retval ==null){
            return ReaderQCTableStatus.Unknown;
        }
        return retval;
    }
}
