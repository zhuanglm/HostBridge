package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 19/12/2017.
 */

public enum ActionTypeUponTestFieldSaved {
    NO_ACTION (0),
    RESTRICT_EDITABILITY (1),
    REQUEST_RECALCULATION (2);



    public final Integer value;

    ActionTypeUponTestFieldSaved(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,ActionTypeUponTestFieldSaved> typeMap = new HashMap<Integer,ActionTypeUponTestFieldSaved>();
    static {
        for (ActionTypeUponTestFieldSaved type : ActionTypeUponTestFieldSaved.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static ActionTypeUponTestFieldSaved fromInt(int i){
        ActionTypeUponTestFieldSaved retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return ActionTypeUponTestFieldSaved.NO_ACTION;
        }
        return retval;
    }
}
