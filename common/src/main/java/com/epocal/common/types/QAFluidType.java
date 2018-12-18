package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/5/2017.
 */

public enum QAFluidType {
    None (0),
    Unknown (1),
    Default (2),

    QCLevel1 (3),   // Metabolites Level1 - Level5
    QCLevel2 (4),
    QCLevel3 (5),
    QCLevel4 (6),
    QCLevel5 (7),
    CV1 (8),    // Calibration Verification fluids CV1-CV5
    CV2 (9),
    CV3 (10),
    CV4 (11),
    CV5 (12),
    QCHCTA (13), // HCT Control Levels A, B, C
    QCHCTB (14),
    QCHCTC (15),
    H1 (16),    // HCT Verification fluids H1 - H5
    H2 (17),
    H3 (18),
    H4 (19),
    H5 (20),
    QCHPB (21), // Hypoxic QC
    QCHPX (22), // Hyperbaric QC

    Custom (99),
    Blood (100),

    QCLevelX (1001);
    public final Integer value;
    QAFluidType(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,QAFluidType> typeMap = new HashMap<Integer,QAFluidType>();
    static {
        for (QAFluidType type : QAFluidType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static QAFluidType fromInt(int i){
        QAFluidType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return QAFluidType.Unknown;
        }
        return retval;
    }
}
