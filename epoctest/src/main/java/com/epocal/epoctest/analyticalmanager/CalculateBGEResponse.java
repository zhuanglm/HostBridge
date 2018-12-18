package com.epocal.epoctest.analyticalmanager;

import com.epocal.common.am.BGEParameters;
import com.epocal.common.am.SensorReadings;

import java.util.List;

/**
 * Usage: CalculateBGEResponse a = new CalculateBGEResponse.Builder(mandatory-field1, ...).setField1(val1).setField2(val2).build();
 */
public class CalculateBGEResponse {

    private final LibraryCallReturnCode  errorCode;
    private final String errorMessage;
    private final List<SensorReadings> sensorReadings;
    private final BGEParameters params;

    public LibraryCallReturnCode getErrorCode() {
        return errorCode;
    }
    public boolean isSuccess() { return (errorCode == LibraryCallReturnCode.SUCCESS);}
    public String getErrorMessage() {
        return errorMessage;
    }
    public List<SensorReadings> getSensorReadings() {
        return sensorReadings;
    }
    public BGEParameters getParams() {
        return params;
    }

    public static class Builder {

        private final LibraryCallReturnCode  errorCode;
        private final String errorMessage;
        private List<SensorReadings> sensorReadings;
        private BGEParameters params;

        public static Builder newBuilder(LibraryCallReturnCode  errorCode, String errorMessage) {
            return new Builder(errorCode, errorMessage);
        }

        public Builder(LibraryCallReturnCode  errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.sensorReadings = null;
            this.params = null;
        }

        public Builder sensorReadings(List<SensorReadings> sensorReadings) {
            this.sensorReadings =  sensorReadings;
            return this;
        }

        public Builder params(BGEParameters params) {
            this.params =  params;
            return this;
        }

        public CalculateBGEResponse build() {
            return new CalculateBGEResponse(this);
        }

    }

    // Protected constructor
    protected CalculateBGEResponse(Builder builder) {
        this.errorCode = builder.errorCode;
        this.errorMessage = builder.errorMessage;
        this.sensorReadings = builder.sensorReadings;
        this.params = builder.params;

    }
}
