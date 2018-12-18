package com.epocal.common_ui.list;

public class ListItemSectionHeader implements IListItem<String> {
    private String mTitle;

    public ListItemSectionHeader(String title) {
        this.mTitle = title;
    }

    @Override
    public boolean isSectionHeader() {
        return true;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getValue() {
        return "";
    }
}
