package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 5/29/2017.
 */

public enum TestType {
    Unknown (0),
    Blood (1),
    QualityControl (2), // aka QC
    CalVer (3),
    Proficiency (4),
    ThermalCheck (5),
    EQC (6),
    Other (7);

    public final Integer value;

    TestType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,TestType> typeMap = new HashMap<Integer,TestType>();
    static {
        for (TestType type : TestType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static TestType fromInt(int i){
        TestType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return TestType.Unknown;
        }
        return retval;
    }
}
