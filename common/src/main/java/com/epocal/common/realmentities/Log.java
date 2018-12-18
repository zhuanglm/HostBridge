package com.epocal.common.realmentities;

import com.epocal.common.types.LogContext;
import com.epocal.common.types.LogLevel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 7/13/2017.
 */

public class Log extends RealmObject{
   private String mMessage;
    private String mMessageExtra;
    private String mSubContext;
    private boolean mIsAlert;
    private Date mCreated;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessageExtra() {
        return mMessageExtra;
    }

    public void setMessageExtra(String messageExtra) {
        mMessageExtra = messageExtra;
    }

    public String getSubContext() {
        return mSubContext;
    }

    public void setSubContext(String subContext) {
        mSubContext = subContext;
    }

    public boolean isAlert() {
        return mIsAlert;
    }

    public void setAlert(boolean alert) {
        mIsAlert = alert;
    }

    public Date getCreated() {
        return mCreated;
    }

    public void setCreated(Date created) {
        mCreated = created;
    }

    @Ignore
    private LogLevel mLogLevel;
    private Integer mLogLevelValue;

    public LogLevel getLogLevel() {
        return LogLevel.fromInt(getLogLevelValue());
    }

    public void setLogLevel(LogLevel logLevel) {

       setLogLevelValue(logLevel.value);
    }

    private Integer getLogLevelValue() {
        return mLogLevelValue;
    }

    private void setLogLevelValue(Integer logLevelValue) {
        mLogLevelValue = logLevelValue;
    }

    @Ignore
    private LogContext mLogContext;
    private Integer mLogContextValue;

    public LogContext getLogContext() {
        return LogContext.fromInt(getLogContextValue());
    }

    public void setLogContext(LogContext logContext) {

        setLogContextValue(logContext.value);
    }

    private Integer getLogContextValue() {
        return mLogContextValue;
    }

    private void setLogContextValue(Integer logContextValue) {
        mLogContextValue = logContextValue;
    }
}
