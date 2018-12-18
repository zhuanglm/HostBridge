package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 02/11/2017.
 */

public enum EpocTestPanelType {
    UNKNOWN (0),
    GASES (1),
    ELECTROLYTES (2),
    EXTENDEDMETABOLITES (3),
    CUSTOM (4),
    ALL(99); // created at runtime


    public final Integer value;
    EpocTestPanelType  (Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,EpocTestPanelType  > typeMap = new HashMap<Integer,EpocTestPanelType  >();
    static {
        for (EpocTestPanelType   type : EpocTestPanelType .values()){
            typeMap.put(type.value,type);
        }
    }
    public  static EpocTestPanelType  fromInt(int i){
        EpocTestPanelType  retval = typeMap.get(i);
        if (retval ==null){
            return EpocTestPanelType.UNKNOWN;
        }
        return retval;
    }
}
