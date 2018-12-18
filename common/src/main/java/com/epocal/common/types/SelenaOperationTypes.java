package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/16/2017.
 */

public enum SelenaOperationTypes {
    None (0),
    Enable (1),
    Disable (2),
    Select (3),
    UnSelect (4);

    public final Integer value;
    SelenaOperationTypes(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,SelenaOperationTypes> typeMap = new HashMap<Integer,SelenaOperationTypes>();
    static {
        for (SelenaOperationTypes type : SelenaOperationTypes.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static SelenaOperationTypes fromInt(int i){
        SelenaOperationTypes retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return SelenaOperationTypes.None;
        }
        return retval;
    }
}
