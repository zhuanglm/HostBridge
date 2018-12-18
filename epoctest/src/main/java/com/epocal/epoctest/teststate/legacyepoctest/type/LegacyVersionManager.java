package com.epocal.epoctest.teststate.legacyepoctest.type;

import com.epocal.util.Version;

/**
 * Created by Rzhuang on July 30 2018.
 */

public class LegacyVersionManager {

    private static String[] CompatibleReaderVersions = {"2.2.0.0"};
    //private final static String CompatibleEDMVersions = "2.2.0";

    public static boolean IsReaderCompatible(String readerVersion)
    {
        Version readerVersionObject = new Version(readerVersion);

        for (String compatibleReaderVersion : CompatibleReaderVersions) {
            if (readerVersionObject.IsCompatible(new Version(compatibleReaderVersion))) {
                return true;
            }
        }
        // it wasnt compatible with anything in the list
        return false;
    }
}
