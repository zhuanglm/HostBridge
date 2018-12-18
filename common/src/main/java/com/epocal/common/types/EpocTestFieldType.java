package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 7/20/2017.
 */

public enum EpocTestFieldType {
    UNKNOWN (0),
    PATIENTID (1),
    PATIENTID2 (2),
    TESTSELECTION (3),
    SAMPLETYPE (4),
    PATIENTTEMPERATURE (5),
    HEMODILUTION (6),
    COMMENTS (7),
    DRAWSITE (8),
    ALLENSTEST (9),
    DELIVERYSYSTEM (10),
    MODE (11),
    FIO2 (12),
    VT (13),
    RR (14),
    TR (15),
    PEEP (16),
    PS (17),
    IT (18),
    ET (19),
    PIP (20),
    MAP (21),
    ORDERINGPHYSICIAN (22),
    ORDERDATE (23),
    ORDERTIME (24),
    COLLECTEDBY (25),
    COLLECTIONDATE (26),
    COLLECTIONTIME (27),
    PATIENTLOCATION (28),
    FLOW (29),
    PATIENTGENDER (30),
    PATIENTAGE (31),
    HERTZ (32),
    DELTAP (33),
    NOPPM (34),
    RQ (35),
    NOTIFYACTION (36),
    NOTIFYNAME (37),
    NOTIFYDATE (38),
    NOTIFYTIME (39),
    READBACK (40),
    REJECTTEST (41),
    LOTNUMBER(42),
    FLUIDTYPE(43),
    TESTTYPE (44),
    PATIENTHEIGHT (45),
    // NEXT VALUE

    CUSTOM (999);

    public final Integer value;
    EpocTestFieldType  (Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,EpocTestFieldType  > typeMap = new HashMap<Integer,EpocTestFieldType  >();
    static {
        for (EpocTestFieldType   type : EpocTestFieldType .values()){
            typeMap.put(type.value,type);
        }
    }
    public  static EpocTestFieldType  fromInt(int i){
        EpocTestFieldType  retval = typeMap.get(i);
        if (retval ==null){
            return EpocTestFieldType.UNKNOWN;
        }
        return retval;
    }
}
