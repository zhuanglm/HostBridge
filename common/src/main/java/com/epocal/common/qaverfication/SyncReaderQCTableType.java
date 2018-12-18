package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum SyncReaderQCTableType {

    None (0),
    CleanResetQCTable (1),
    RestoreQCTable (2),
    GetQCTableDescriptor (4),
    GetQCTable (8),
    SetTQA (16),
    SetEQC (32),
    GetEQC (64);

    public final Integer value;
    SyncReaderQCTableType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SyncReaderQCTableType> typeMap = new HashMap<Integer,SyncReaderQCTableType>();
    static {
        for (SyncReaderQCTableType type : SyncReaderQCTableType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SyncReaderQCTableType fromInt(int i){
        SyncReaderQCTableType retval = typeMap.get(i);
        if (retval ==null){
            return SyncReaderQCTableType.None;
        }
        return retval;
    }
}
