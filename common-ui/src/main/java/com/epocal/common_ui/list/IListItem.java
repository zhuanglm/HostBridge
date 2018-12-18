package com.epocal.common_ui.list;

/**
 * Interface to define ListItem for the List with Section Header.
 * Generic type T - use "String" for general purpose use. Use "SpannableString" when text styling needed.
 */
public interface IListItem<T> {
    boolean isSectionHeader();
    T  getTitle();
    T  getValue();
}
