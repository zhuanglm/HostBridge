package com.epocal.testhistoryfeature.data.type;

/**
 * Represent a selection type when entering Multi Select screen from Main screen.
 * SELECT_DATE_RANGE is valid only when Main screen is showing date range grouped search list.
 */
public enum SelectionType {
    SELECT_NONE(0),
    SELECT_ALL(1),
    SELECT_ONE_ITEM(2),
    SELECT_DATE_RANGE(3);

    public final int value;

    SelectionType(int value) {
        this.value = value;
    }

    public static SelectionType fromInt(int intValue) {
        for (SelectionType selectionType : SelectionType.values()) {
            if (selectionType.value == intValue) return selectionType;
        }
        return null; // Not a valid intValue representing this enum
    }

}
