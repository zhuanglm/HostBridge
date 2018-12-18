package com.epocal.common;

import android.content.Context;

import com.epocal.common.types.LanguageType;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add a file header comment !
 * Created on 16/06/2017.
 */

public class LanguageTypeUtil {
    private static List<LanguageType> mLanguageTypeList;

    /**
     * TODO: Build available localization language list. For now, it is hardcoded for the testing purpose.
     */
    public static List<LanguageType> buildAvailableLanguageTypeList(Context context) {
        List<LanguageType> langList = new ArrayList<LanguageType>();
        langList.add(LanguageType.English);
        langList.add(LanguageType.French);
        return langList;
    }

    public static String fromLanguageTypeToLanguageString(Context context, LanguageType type) {
        String languageString;
        switch (type) {
            case English:
                languageString = context.getResources().getString(R.string.language_type_en);
                break;
            case French:
                languageString =  context.getResources().getString(R.string.language_type_fr);
                break;
            default:
                languageString = context.getResources().getString(R.string.language_type_unknown);
        }
        return languageString;
    }

    // position of language list to language type
    // get string list
    public static ArrayList<String> getAvailableLanguageList(Context context) {
        if (mLanguageTypeList == null) {
            mLanguageTypeList = buildAvailableLanguageTypeList(context);
        }

        ArrayList<String> list = new ArrayList<String>();
        for (LanguageType type : mLanguageTypeList) {
            list.add(fromLanguageTypeToLanguageString(context, type));
        };
        return list;
    }

    // list index to LanguageType
    public static LanguageType getLanguageTypeAtListIndex(int index) {
        if ((index < 0) || (index > mLanguageTypeList.size())) {
            // out of bounds
            return LanguageType.Unknown;
        }
        return mLanguageTypeList.get(index);
    }
    // LanguageType to index
    public static int getListIndexFromLanguageType(LanguageType type,  Context context) {
        if(mLanguageTypeList == null)
        {
            getAvailableLanguageList(context);
        }
        return mLanguageTypeList.indexOf(type);
    }

}
