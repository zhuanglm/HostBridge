package com.epocal.epoctest.testconfiguration;

import java.util.Date;

import static java.lang.Character.SPACE_SEPARATOR;

/**
 * Created by dning on 8/30/2017.
 */

public class TestConfigVersion {
    public VersionObject Version;
    public Operator CreatedByOperator;
    public Date DateCreated;
    public String Comments;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Version: " + Version + SPACE_SEPARATOR);
        sb.append("createdByOperator: " + CreatedByOperator.toString() + SPACE_SEPARATOR);
        sb.append("dateCreated: " + DateCreated.toString() + SPACE_SEPARATOR);
        sb.append("comments: " + Comments + SPACE_SEPARATOR);
        return sb.toString();
    }
}
