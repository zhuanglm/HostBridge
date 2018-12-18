package com.epocal.common_ui.list;

public interface ISelectableListItem extends IListItem<String> {
    boolean isSelected();
    void setSelected(boolean selected);
}
