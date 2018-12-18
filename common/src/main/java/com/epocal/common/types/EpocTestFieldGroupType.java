package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 7/21/2017.
 */

public enum EpocTestFieldGroupType {

    NONE (0),
    MANDATORY (1),
    COMPLIANCE (2),
    PATIENTINFORMATION_OPTIONAL (3),
    SAMPLEINFORMATION_OPTIONAL (4),
    RESPIRATORY_OPTIONAL (5),
    ADDITIONALDOCUMENTATION_OPTIONAL (6),
    TESTSELECTION_OPTIONAL (7),
    CUSTOMTESTVARIABLES_OPTIONAL (8),
    QA_OPTIONAL (9),
    DOCUMENTRESULTS_OPTIONAL (10),                            // only used in QA tests
    QA_TEST_TYPE(11),
    QA_COMMENTS(12);

    public final Integer value;
    EpocTestFieldGroupType (Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,EpocTestFieldGroupType > typeMap = new HashMap<Integer,EpocTestFieldGroupType >();
    static {
        for (EpocTestFieldGroupType  type : EpocTestFieldGroupType .values()){
            typeMap.put(type.value,type);
        }
    }
    public  static EpocTestFieldGroupType  fromInt(int i){
        EpocTestFieldGroupType  retval = typeMap.get(i);
        if (retval ==null){
            return EpocTestFieldGroupType.NONE;
        }
        return retval;
    }
}
