package com.epocal.common.realmentities;

import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.TestType;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/19/2017.
 */

public class ReaderQCFluidInfo extends RealmObject{
    private Reader reader;
    private String qcFluidInfoString;

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public String getQcFluidInfoString() {
        return qcFluidInfoString;
    }

    public void setQcFluidInfoString(String qcFluidInfoString) {
        this.qcFluidInfoString = qcFluidInfoString;
    }

    @Ignore
    private TestType qcTestType;
    private Integer qcType;

    public TestType getQcTestType() {
        return TestType.fromInt(getQcType());
    }

    public void setQcTestType(TestType qcTestType) {
        setQcType(qcTestType.value);
    }

    private Integer getQcType() {
        return qcType;
    }

    private void setQcType(Integer qcType) {
        this.qcType = qcType;
    }

    @Ignore
    private QAFluidType mQAFluidType;
    private Integer qaControlFluidType;

    public QAFluidType getQAFluidType() {
       return QAFluidType.fromInt(getQaControlFluidType());
    }

    public void setQAFluidType(QAFluidType QAFluidType) {
        setQaControlFluidType(QAFluidType.value);
    }

    private Integer getQaControlFluidType() {
        return qaControlFluidType;
    }

    private void setQaControlFluidType(Integer qaControlFluidType) {
        this.qaControlFluidType = qaControlFluidType;
    }
}
