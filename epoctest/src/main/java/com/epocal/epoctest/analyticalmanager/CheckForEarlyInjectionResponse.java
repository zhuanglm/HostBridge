package com.epocal.epoctest.analyticalmanager;

import com.epocal.common.am.SensorReadings;
import com.epocal.common.types.am.RealTimeHematocritQCReturnCode;

/**
 * Usage: CheckForEarlyInjectionResponse a = new CheckForEarlyInjectionResponse.Builder(mandatory-field1, ...).setField1(val1).setField2(val2).build();
 */
public class CheckForEarlyInjectionResponse {

    private final LibraryCallReturnCode  errorCode;
    private final String errorMessage;
    private final RealTimeHematocritQCReturnCode amReturnCode;
    private final SensorReadings hematocritReadings;

    public LibraryCallReturnCode getErrorCode() {
        return errorCode;
    }
    public boolean isSuccess() { return (errorCode == LibraryCallReturnCode.SUCCESS);}
    public String getErrorMessage() {
        return errorMessage;
    }
    public RealTimeHematocritQCReturnCode getAmReturnCode() {
        return amReturnCode;
    }
    public SensorReadings getHematocritReadings() {
        return hematocritReadings;
    }

    public static class Builder {
        private final LibraryCallReturnCode  errorCode;
        private final String errorMessage;
        private RealTimeHematocritQCReturnCode amReturnCode;
        private SensorReadings hematocritReadings;

        public static Builder newBuilder(LibraryCallReturnCode  errorCode, String errorMessage) {
            return new Builder(errorCode, errorMessage);
        }

        public Builder(LibraryCallReturnCode  errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            // Set default value for the optional fields
            this.amReturnCode = RealTimeHematocritQCReturnCode.ENUM_UNINITIALIZED;
            this.hematocritReadings = null;
        }

        public Builder amReturnCode(RealTimeHematocritQCReturnCode amReturnCode) {
            this. amReturnCode =  amReturnCode;
            return this;
        }

        public Builder hematocritReadings(SensorReadings hematocritReadings) {
            this. hematocritReadings =  hematocritReadings;
            return this;
        }

        public CheckForEarlyInjectionResponse build() {
            return new CheckForEarlyInjectionResponse(this);
        }

    }

    // Protected constructor
    protected CheckForEarlyInjectionResponse(Builder builder) {
        this.errorCode = builder.errorCode;
        this.errorMessage = builder.errorMessage;
        this.amReturnCode = builder.amReturnCode;
        this.hematocritReadings = builder.hematocritReadings;
    }
}
