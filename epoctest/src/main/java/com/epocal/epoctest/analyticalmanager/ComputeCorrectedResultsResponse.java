package com.epocal.epoctest.analyticalmanager;

import com.epocal.common.am.FinalResult;

import java.util.List;

/**
 * Usage: ComputeCorrectedResultsResponse a = new ComputeCorrectedResultsResponse.Builder(mandatory-field1, ...).setField1(val1).setField2(val2).build();
 */
public class ComputeCorrectedResultsResponse {

    private final LibraryCallReturnCode  errorCode;
    private final String errorMessage;
    private final List<FinalResult> correctedResults;

    public LibraryCallReturnCode getErrorCode() {
        return errorCode;
    }
    public boolean isSuccess() { return (errorCode == LibraryCallReturnCode.SUCCESS);}
    public String getErrorMessage() {
        return errorMessage;
    }
    public List<FinalResult> getCorrectedResults() {
        return correctedResults;
    }


    public static class Builder {

        private final LibraryCallReturnCode  errorCode;
        private final String errorMessage;
        private List<FinalResult> correctedResults;

        public static Builder newBuilder(LibraryCallReturnCode  errorCode, String errorMessage) {
            return new Builder(errorCode, errorMessage);
        }

        public Builder(LibraryCallReturnCode  errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.correctedResults = null;
        }

        public Builder correctedResults(List<FinalResult> correctedResults) {
            this.correctedResults =  correctedResults;
            return this;
        }


        public ComputeCorrectedResultsResponse build() {
            return new ComputeCorrectedResultsResponse(this);
        }

    }

    // Protected constructor
    protected ComputeCorrectedResultsResponse(Builder builder) {
        this.errorCode = builder.errorCode;
        this.errorMessage = builder.errorMessage;
        this.correctedResults = builder.correctedResults;

    }
}
