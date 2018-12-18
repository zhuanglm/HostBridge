package com.epocal.epoctest.analyticalmanager;

import com.epocal.common.am.SensorReadings;
import com.epocal.common.types.am.RealTimeQCReturnCode;

import java.util.List;

/**
 * Usage: PerformRealTimeQCResponse a = new PerformRealTimeQCResponse.Builder(mandatory-field1, ...).setField1(val1).setField2(val2).build();
 */
public class PerformRealTimeQCResponse {

    private final LibraryCallReturnCode  errorCode;
    private final String errorMessage;
    private final RealTimeQCReturnCode amReturnCode;
    private final List<SensorReadings> testReadings;

    public LibraryCallReturnCode getErrorCode() {
        return errorCode;
    }
    public boolean isSuccess() { return (errorCode == LibraryCallReturnCode.SUCCESS);}
    public String getErrorMessage() {
        return errorMessage;
    }
    public RealTimeQCReturnCode getAmReturnCode() {
        return amReturnCode;
    }
    public List<SensorReadings> getTestReadings() {
        return testReadings;
    }

    public static class Builder {

        private final LibraryCallReturnCode  errorCode;
        private final String errorMessage;
        private RealTimeQCReturnCode amReturnCode;
        private List<SensorReadings> testReadings;

        public static Builder newBuilder(LibraryCallReturnCode  errorCode, String errorMessage) {
            return new Builder(errorCode, errorMessage);
        }

        public Builder(LibraryCallReturnCode  errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.amReturnCode = RealTimeQCReturnCode.ENUM_UNINITIALIZED;
            this.testReadings = null;
        }

        public Builder amReturnCode(RealTimeQCReturnCode amReturnCode) {
            this.amReturnCode =  amReturnCode;
            return this;
        }

        public Builder testReadings(List<SensorReadings> testReadings) {
            this.testReadings =  testReadings;
            return this;
        }


        public PerformRealTimeQCResponse build() {
            return new PerformRealTimeQCResponse(this);
        }

    }

    // Protected constructor
    protected PerformRealTimeQCResponse(Builder builder) {
        this.errorCode = builder.errorCode;
        this.errorMessage = builder.errorMessage;
        this.amReturnCode = builder.amReturnCode;
        this.testReadings = builder.testReadings;

    }
}

