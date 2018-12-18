package com.epocal.testhistoryfeature.data.presetfilterdata;

/**
 * This class represents Model object for rre set filter (search filter) in the drop-down menu
 * in Main Screen.
 */
public class PresetFilterData {
    /**
     * These constants number matches the
     * order of string-array "preset_filter_titles"
     */
    public static final int FILTER_ALL = 0;
    public static final int FILTER_UNSENT = 1;
    public static final int FILTER_SENT = 2;

    private String mTitle;
    private boolean isSelected;
    private int mFilterType; // One of above

    public PresetFilterData(String title, boolean isSelected, int filterType) {
        this.mTitle = title;
        this.isSelected = isSelected;
        this.mFilterType = filterType;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getFilterType() {
        return mFilterType;
    }
}
