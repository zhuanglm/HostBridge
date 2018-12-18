package com.epocal.epoctest.analyticalmanager;

import com.epocal.common.am.FinalResult;

import java.util.List;

public class ComputeCalculatedResultsResponse {

    private final LibraryCallReturnCode  errorCode;
    private final String errorMessage;
    private final List<FinalResult> calculatedResults;


    public LibraryCallReturnCode getErrorCode() {
        return errorCode;
    }
    public boolean isSuccess() { return (errorCode == LibraryCallReturnCode.SUCCESS);}
    public String getErrorMessage() {
        return errorMessage;
    }
    public List<FinalResult> getCalculatedResults() {
        return calculatedResults;
    }


    public static class Builder {

        private final LibraryCallReturnCode  errorCode;
        private final String errorMessage;
        private List<FinalResult> calculatedResults;

        public static Builder newBuilder(LibraryCallReturnCode  errorCode, String errorMessage) {
            return new Builder(errorCode, errorMessage);
        }

        public Builder(LibraryCallReturnCode  errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.calculatedResults = null;
        }

        public Builder calculatedResults(List<FinalResult> calculatedResults) {
            this.calculatedResults =  calculatedResults;
            return this;
        }

        public ComputeCalculatedResultsResponse build() {
            return new ComputeCalculatedResultsResponse(this);
        }

    }

    // Protected constructor
    protected ComputeCalculatedResultsResponse(Builder builder) {
        this.errorCode = builder.errorCode;
        this.errorMessage = builder.errorMessage;
        this.calculatedResults = builder.calculatedResults;
    }
}