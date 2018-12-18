package com.epocal.common_ui.testresults;

/**
 *  This data item is used to distinguish between "section header" and "test result" data.
 *  isSectionHeader -- true = the item represents Section Header, false = the item represents Test Results
 *  getSectionTitle -- returns AnalyteGroup name
 */

public interface IGroupedTestResultDataItem {
    boolean isListHeader();
    boolean isSectionHeader();
    String  getTitle();
    int     getTestResultIndex();
}
