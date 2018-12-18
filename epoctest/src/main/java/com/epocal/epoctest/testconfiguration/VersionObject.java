package com.epocal.epoctest.testconfiguration;

import static java.lang.Character.SPACE_SEPARATOR;

/**
 * Created by dning on 8/30/2017.
 */

public class VersionObject {
    public int Major;
    public int Minor;
    public int Revision;
    public int Build;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Major: " + Major + SPACE_SEPARATOR);
        sb.append("Minor: " + Minor + SPACE_SEPARATOR);
        sb.append("Revision: " + Revision + SPACE_SEPARATOR);
        sb.append("Build: " + Build + SPACE_SEPARATOR);
        return sb.toString();
    }
}
