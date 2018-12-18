package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum QATestStatus {
    Unknown (1),
    NotTested (2),
    Passed (3),
    Failed (4);

    public final Integer value;
    QATestStatus(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,QATestStatus> typeMap = new HashMap<Integer,QATestStatus>();
    static {
        for (QATestStatus type : QATestStatus.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static QATestStatus fromInt(int i){
        QATestStatus retval = typeMap.get(i);
        if (retval ==null){
            return QATestStatus.NotTested;
        }
        return retval;
    }

}
