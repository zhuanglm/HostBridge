package com.epocal.epoclog;

import com.epocal.common.DeviceInfoUtil;
import com.epocal.common.androidutil.ZipFileUtil;

import io.reactivex.functions.Consumer;

/**
 * Created by ray zhuang on Nov 8 2018.
 */

public class LogFile implements Consumer<String> {
    public ZipFileUtil mLogZipFile;
    private final static String EncryptionKey = "345098k_32478jlksdfjhaf730-5nsd09834-mqsfhskf78143yt5rg389193409845jhasf98732kjh";

    @Override
    public void accept(String s) {
        mLogZipFile.write(s);
    }

    public LogFile() {

    }

    public void createLogFile(String timeSpecificString, String dateSpecificString, String serialNumber) {
        if (mLogZipFile == null) {
            mLogZipFile = new ZipFileUtil("P_" + timeSpecificString,
                    ZipFileUtil.FileLocation.External, dateSpecificString + "//" + serialNumber,
                    encryptionKeyGenerator());
        }
    }

    public void close() {
        if (mLogZipFile != null) {
            mLogZipFile.close();
            mLogZipFile = null;
        }

    }

    private String encryptionKeyGenerator() {
        int intReplaceLength = 8;
        int intReplacePosition = 8;
        String first8Digital;
        String last8Digital;
        StringBuilder newEncryptionKey = new StringBuilder(EncryptionKey);
        String mask;
        int maskLength;

        mask = DeviceInfoUtil.getDeviceID();

        maskLength = mask.length();

        //get first 8-digital and last 8-digital from idPDA
        if (maskLength == 0) {
            first8Digital = "";
            last8Digital = "";
        } else if (maskLength >= intReplaceLength) {
            first8Digital = mask.substring(0, intReplaceLength);
            last8Digital = mask.substring(maskLength - intReplaceLength, maskLength);
        } else {
            first8Digital = String.format("%-*8s", mask);
            last8Digital = String.format("%*8s", mask);
        }

        //replace encryption with firstdigital starting from 8
        newEncryptionKey = newEncryptionKey.replace(intReplacePosition,
                intReplacePosition + first8Digital.length(), first8Digital);

        //replace with lastdigital
        newEncryptionKey = newEncryptionKey.replace(newEncryptionKey.length() - intReplacePosition * 2,
                newEncryptionKey.length() - intReplacePosition, last8Digital);

        return newEncryptionKey.toString();
    }


}
