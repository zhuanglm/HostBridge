package com.epocal.common_ui.list;

import android.text.SpannableString;

/**
 * This class represents ListItem whose member variables are Styled String (SpannableString).
 */
public class StyledTextListItem implements IListItem<SpannableString> {
    private SpannableString mTitle;
    private SpannableString mValue;

    public StyledTextListItem(SpannableString title, SpannableString value) {
        this.mTitle = title;
        this.mValue = value;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public SpannableString getTitle() {
        return mTitle;
    }

    @Override
    public SpannableString getValue() {
        return mValue;
    }
}
