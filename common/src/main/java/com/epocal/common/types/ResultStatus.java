package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/5/2017.
 */

public enum ResultStatus {
    Unknown (0),
    Normal (1),
    ReferenceLow (2),
    ReferenceHigh (3),
    CriticalLow (4),
    CriticalHigh (5),
    CNC (6),
    FailediQC (7),
    BelowReportableRange (8),
    AboveReportableRange (9),
    Expired (10),
    CriticalBelowReportableRange (11),
    CriticalAboveReportableRange (12),
    ReferenceBelowReportableRange (13),
    ReferenceAboveReportableRange (14);

    public final Integer value;
    ResultStatus(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,ResultStatus> typeMap = new HashMap<Integer,ResultStatus>();
    static {
        for (ResultStatus type : ResultStatus.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ResultStatus fromInt(int i){
        ResultStatus retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return ResultStatus.Unknown;
        }
        return retval;
    }
}
