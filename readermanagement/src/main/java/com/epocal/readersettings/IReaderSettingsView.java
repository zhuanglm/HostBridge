package com.epocal.readersettings;

/**
 * Created by Zeeshan A Zakaria on 4/11/2017.
 * <p>
 * Created on 01/15/2017.
 * <p>
 * Based on class in hostsettings
 */

public interface IReaderSettingsView {

    void handleSelectReaderStatusClicked();

    void handleSelectDedicatedReadersClicked();

    void handleSelectPageReaderClicked();

    void handleSelectReaderUpgradeClicked();

    void handleSelectReaderThermalQA();

    void handleSelectChangeReaderName();

    void enableReaderStatus(boolean enabled);

    void enableDedicatedReaders(boolean enabled);

    void enablePageReaderC(boolean enabled);

    void enableReaderUpgrade(boolean enabled);

    void enableReaderThermalQA(boolean enabled);

    void enableChangeReaderName(boolean enabled);
}
