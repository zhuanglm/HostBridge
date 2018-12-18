package com.epocal.common.qaverfication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/19/2017.
 */

public enum QAScheduleTestStatus {

    None(0),
    ScheduleDisabled (1),
    WithinSchedule (2),
    WarningPeriodCompliance (3),
    ExpiredCompliance (4),
    WarningPeriodMandatory (5),
    GracePeriodMandatory (6),
    ExpiredMandatory (7);

    public final Integer value;
    QAScheduleTestStatus(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,QAScheduleTestStatus> typeMap = new HashMap<Integer,QAScheduleTestStatus>();
    static {
        for (QAScheduleTestStatus type : QAScheduleTestStatus.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static QAScheduleTestStatus fromInt(int i){
        QAScheduleTestStatus retval = typeMap.get(i);
        if (retval ==null){
            return QAScheduleTestStatus.None;
        }
        return retval;
    }


}
