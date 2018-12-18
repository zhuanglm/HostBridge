package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 13/02/2018.
 */

public enum ReaderDiscoveryMode {
    UNKNOWN(0),
    BLOOD_TEST(1),
    QA_TEST(2),
    STATUS_READER(3),
    DEDICATE_READER(4),
    PAGE_READER(5),
    UPGRADE_READER(6),
    READER_THERMAL_QA(7),
    CHANGE_READER_NAME(8);


    public final Integer value;
    ReaderDiscoveryMode(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,ReaderDiscoveryMode> typeMap = new HashMap<Integer,ReaderDiscoveryMode>();
    static {
        for (ReaderDiscoveryMode type : ReaderDiscoveryMode.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ReaderDiscoveryMode fromInt(int i){
        ReaderDiscoveryMode retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return ReaderDiscoveryMode.UNKNOWN;
        }
        return retval;
    }
}
