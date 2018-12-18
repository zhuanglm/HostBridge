package com.epocal.common_ui.testresults;

/**
 * This interface provides the callback interface on
 * the Patient Test TestResults screen has the following
 * click events:
 *
 * -- TestResults list row click
 * -- Document button click
 * -- Close and Transmit button click
 */
public interface ITestResultPanelListener {
    void onDocumentButtonClick();
    void onCloseTransmitButtonClick();
    void onListRowClick();
}
