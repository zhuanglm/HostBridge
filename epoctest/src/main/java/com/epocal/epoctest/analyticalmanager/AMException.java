package com.epocal.epoctest.analyticalmanager;



public class AMException extends Exception {
    private LibraryCallReturnCode errorCode;
    private String errorMessage;


    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public AMException() {
        init("");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AMException(String message) {
        super(message);
        init(message);
    }

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public AMException(LibraryCallReturnCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private void init(String message) {
        errorCode = LibraryCallReturnCode.UNDEFINED;
        errorMessage = "AMException: " + message;
    }

    public LibraryCallReturnCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(LibraryCallReturnCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
