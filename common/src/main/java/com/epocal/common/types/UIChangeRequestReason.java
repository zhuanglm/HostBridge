package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 19/12/2017.
 */

public enum UIChangeRequestReason {
    Unknown (0),
    CARDINSERTED_NEWTEST_KEEPDATA (1),
    RECALCULATION_STARTED (2),
    CALCULATION_STARTED (3),
    CALCULATION_FINISHED (4),
    EDITABILITY_CHANGED (5),
    CARDINSERTED_NEWTEST_DONTKEEPDATA (6),
    UPDATE_TESTSELECTION(7),
            ;


    public final Integer value;

    UIChangeRequestReason(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,UIChangeRequestReason> typeMap = new HashMap<Integer,UIChangeRequestReason>();
    static {
        for (UIChangeRequestReason type : UIChangeRequestReason.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static UIChangeRequestReason fromInt(int i){
        UIChangeRequestReason retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return UIChangeRequestReason.Unknown;
        }
        return retval;
    }
}
