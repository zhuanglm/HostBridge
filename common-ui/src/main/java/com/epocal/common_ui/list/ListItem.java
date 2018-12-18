package com.epocal.common_ui.list;

public class ListItem implements IListItem<String> {
    private String mTitle;
    private String mValue;

    public ListItem(String title, String value) {
        this.mTitle = title;
        this.mValue = value;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getValue() {
        return mValue;
    }
}
