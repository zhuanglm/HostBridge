package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 5/29/2017.
 */

public enum TestStatus {
    Unknown(0),
    Success (1),
    iQC (2),
    Incomplete (3),
    Expired (4);
    public final Integer value;
    TestStatus(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,TestStatus> typeMap = new HashMap<Integer,TestStatus>();
    static {
        for (TestStatus type : TestStatus.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static TestStatus fromInt(int i){
        TestStatus retval = typeMap.get(i);
        if (retval ==null){
            return TestStatus.Unknown;
        }
        return retval;
    }
}
