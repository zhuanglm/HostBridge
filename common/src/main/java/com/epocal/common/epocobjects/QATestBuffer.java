package com.epocal.common.epocobjects;

import com.epocal.common.realmentities.QASampleInfo;
import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.TestType;

public class QATestBuffer {
    private static QATestBuffer instance = null;
    private static final Object syncRoot = new Object();
    private TestType testType;
    private QAFluidType fluidType;
    private QASampleInfo sampleInfo;

    private QATestBuffer() {
        reset();
    }

    public static QATestBuffer getInstance() {
        if (instance == null) {
            synchronized (syncRoot) {
                instance = new QATestBuffer();
            }
        }
        return instance;
    }

    public void reset() {
        testType = null;
        fluidType = null;
        sampleInfo = null;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public QAFluidType getFluidType() {
        return fluidType;
    }

    public void setFluidType(QAFluidType fluidType) {
        this.fluidType = fluidType;
    }

    public QASampleInfo getSampleInfo() {
        return sampleInfo;
    }

    public void setSampleInfo(QASampleInfo sampleInfo) {
        this.sampleInfo = sampleInfo;
    }
}
